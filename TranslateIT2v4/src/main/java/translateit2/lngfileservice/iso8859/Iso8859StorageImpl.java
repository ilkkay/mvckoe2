package translateit2.lngfileservice.iso8859;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import translateit2.configuration.CharSetResolver;
import translateit2.fileloader.FileLoaderServiceException;
import translateit2.fileloader.LanguageFileLoaderService;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.UnitDto;
import translateit2.persistence.dto.WorkDto;
import translateit2.persistence.model.Source;
import translateit2.persistence.model.State;
import translateit2.persistence.model.Target;
import translateit2.service.ProjectService;
import translateit2.util.Messages;
import translateit2.util.OrderedProperties;
import translateit2.validator.Iso8859ValidatorImpl;
import translateit2.validator.LanguageFileValidator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Iso8859StorageImpl implements Iso8859Storage {
    @Autowired
    private CharSetResolver charSetResolver;

    private LanguageFileLoaderService fileStorage;

    @Autowired
    public void setFileStorage(LanguageFileLoaderService fileStorage) {
        this.fileStorage = fileStorage;
    }

    private ProjectService projectService;

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    Messages messages;

    @Autowired
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    LanguageFileValidator iso8859util;

    @Autowired
    public void setIso8859util(Iso8859ValidatorImpl iso8859util) {
        this.iso8859util = iso8859util;
    }

    @Override
    public LanguageFileFormat getFileFormat() {
        return LanguageFileFormat.PROPERTIES;
    }

    // TODO: remove used for testing at he moment
    @Override
    public Path getPath(String filename) {
        return fileStorage.getPath(filename);
    }

    @Override
    public Path storeFile(MultipartFile file) {
        return fileStorage.storeToUploadDirectory(file);
    }

    @Override
    public String checkValidity(Path uploadedLngFile, long workId) throws FileLoaderServiceException {
        String appName = null;
        iso8859util.checkFileExtension(uploadedLngFile);
        appName = iso8859util.checkFileNameFormat(uploadedLngFile);
        iso8859util.checkFileCharSet(uploadedLngFile, workId);
        iso8859util.checkEmptyFile(uploadedLngFile, workId);

        return appName;
    }

    // TODO: this a general routine, not iso8859 specific, so it should be
    // somewhere else
    private void storeBackupFile(Path uploadedLngFile, long workId) throws IOException {
        WorkDto work = projectService.getWorkDtoById(workId);
        ProjectDto prj = projectService.getProjectDtoById(work.getProjectId());
        String fid = UUID.randomUUID().toString();
        String bupFile = fid + "." + prj.getFormat().toString();
        Path target = Paths.get(uploadedLngFile.getParent().toString(), bupFile);
        // TODO: if old backup file exists, remove it
        Files.copy(uploadedLngFile, target, StandardCopyOption.REPLACE_EXISTING);
        work.setBackupFile(target.toString());
        projectService.updateWorkDto(work);
    }

    /**
     * 
     * Creates upload a valid properties file (e.g. dotcms_en.properties)
     * 
     * @param uploadedLngFile
     *            a file copied from server's temporary storage to temporary
     *            directory. It is either UTF-8 or ISO8859-1 encoded-
     * @param workId
     *            work entity identifier
     * @return nothing
     * @throws FileLoaderServiceException
     *             if not able read file.
     */
    @Override
    public void uploadSourceToDb(Path uploadedLngFile, long workId) throws IOException {
        // Charset charset =
        // charSetResolver.getProjectCharSet(work.getProjectId());
        Charset charset = iso8859util.getCharSet(workId);

        LinkedHashMap<String, String> segments = null;
        try {
            segments = (LinkedHashMap<String, String>) getPropSegments(uploadedLngFile, charset);
        } catch (IOException e) { //
            throw new FileLoaderServiceException((messages.get("FileStorageService.not_read_properties_file")) + " "
                    + uploadedLngFile.getFileName());
        }

        storeBackupFile(uploadedLngFile, workId);

        List<UnitDto> unitDtos = new ArrayList<UnitDto>();
        int serialNum = 1;
        for (Map.Entry<String, String> entry : segments.entrySet()) {
            UnitDto unit = new UnitDto();
            unit.setSerialNumber(serialNum);
            unit.setSegmentKey(entry.getKey());
            Source s = new Source();
            s.setText(entry.getValue());
            Target t = new Target();
            t.setText("");
            t.setSkeletonTag("TARGET_TAG_" + serialNum);
            // TODO: this is business logic, so it should be somewhere else?
            // On the other hand this update is rather slow.
            t.setState(State.NEEDS_TRANSLATION);
            unit.setSource(s);
            unit.setTarget(t);
            unitDtos.add(unit);
            serialNum++;
        }
        projectService.createUnitDtos(unitDtos, workId);
    }

    // TODO: there may be need for validating the target upload
    // e.g. source file should exist with the same name
    // and comply with the project specifications
    @Override
    public void uploadTargetToDb(Path uploadedLngFile, long workId) {
        // Charset charset =
        // charSetResolver.getProjectCharSet(work.getProjectId());
        Charset charset = iso8859util.getCharSet(workId);
        LinkedHashMap<String, String> segments = null;
        try {
            segments = (LinkedHashMap<String, String>) getPropSegments(uploadedLngFile, charset);
        } catch (IOException e) { //
            throw new FileLoaderServiceException((messages.get("FileStorageService.not_read_properties_file")) + " "
                    + uploadedLngFile.getFileName());
        }

        List<UnitDto> unitDtos = projectService.listUnitDtos(workId);
        for (UnitDto unit : unitDtos) {
            unit.getTarget().setText(segments.get(unit.getSegmentKey()));
            // TODO: this is business logic, so it should be somewhere else?
            // On the other hand this update is rather slow.
            unit.getTarget().setState(State.TRANSLATED);
        }

        projectService.updateUnitDtos(unitDtos, workId);
    }

    @Override
    public Stream<Path> downloadFromDb(final long workId) throws IOException {
        Path dstDir = null; // default location
        try {
            dstDir = downloadTargetLngFile(dstDir, workId);
        } catch (IOException e) {
            throw new FileLoaderServiceException(e.getLocalizedMessage());
        }

        return Files.walk(dstDir); // TODO: at the moment, just testing
    }

    @Override
    public Path downloadTargetLngFile(Path dstDir, final long workId) throws IOException {
        // Charset charset = iso8859util.getCharSet(workId);
        WorkDto work = projectService.getWorkDtoById(workId);
        Charset charset = charSetResolver.getProjectCharSet(work.getProjectId());

        List<UnitDto> unitDtos = projectService.listUnitDtos(workId);

        // long startTime = System.nanoTime();
        long translated = projectService.getStatistics(workId);
        // long endTime = System.nanoTime();
        // double timeElapsed = (endTime - startTime)/1000.0;

        // TODO: could this be a private method storage services
        // or should storageService be unaware of projectService
        String tgtFilenameStr = work.getOriginalFile() + "_" + work.getLocale().toString() + ".properties";

        // will NOT fail even if path is null or empty. It returns path where
        // file resides.
        if ((dstDir != null) && (!(Files.isDirectory(dstDir)))) {
            Files.createDirectory(dstDir);
        }

        Path target = (dstDir == null) ? getPath(tgtFilenameStr)
                : dstDir.resolve(dstDir.getFileSystem().getPath(tgtFilenameStr));

        /*
         * TODO: deleteIfExists: If the file does not exist, no exception is
         * thrown. Failing silently is useful when you have multiple threads
         * deleting files and you don't want to throw an exception.
         * 
         */
        boolean success = Files.deleteIfExists(target);
        if (!success) {
            // throw new StorageException("File " +
            // target.toAbsolutePath().toString() +
            // " existed already and could not be removed");
            // TODO: what now ???
        }

        Path storedOriginalFile = Paths.get(work.getBackupFile());
        List<String> inLines = Files.readAllLines(storedOriginalFile, charset);
        List<String> outLines = new ArrayList<String>();
        Map<String, String> map = new HashMap<String, String>();
        unitDtos.stream().forEach(dto -> map.put(dto.getSegmentKey(), dto.getTarget().getText()));

        boolean isFirstLine = true; // <= optional byte order mark (BOM)
        for (String line : inLines) {
            if ((isEmptyLine(line)) || (isCommentLine(line)) || (isFirstLine)) {
                outLines.add(line);
                isFirstLine = false;
            } else if (isKeyValuePair(line)) {
                String key = getKey(line);
                System.out.println(getKey(line) + "=" + map.get(key));
                outLines.add(getKey(line) + "=" + map.get(key));
            } else
                throw new FileLoaderServiceException("Could not create file for download");
        }
        Files.write(target, outLines);

        /*
         * DONT REMOVE YET !!! String tgtFileLocationStr = target.toString();
         * OutputStream out = null; OutputStreamWriter osr = null;
         * OrderedProperties dstProp = new OrderedProperties(); try { out = new
         * FileOutputStream(tgtFileLocationStr); } catch (Exception e) { throw
         * new StorageException((messages.get(
         * "FileStorageService.not_write_properties_file")) + " " +
         * tgtFileLocationStr); } try { osr = new
         * OutputStreamWriter(out,charset); } catch (Exception e) { throw new
         * StorageException((messages.get(
         * "FileStorageService.not_write_properties_file")) + " " +
         * tgtFileLocationStr); }
         * unitDtos.forEach(unit->dstProp.setProperty(unit.getSegmentKey(),unit.
         * getTarget().getText())); try { dstProp.store(osr, "Translate IT 2");
         * } catch (Exception e) { throw new StorageException((messages.get(
         * "FileStorageService.not_write_properties_file")) + " " +
         * tgtFileLocationStr + "\\n" + e.getLocalizedMessage()); } finally { if
         * (osr != null) osr.close(); if (out != null) out.close(); }
         */
        return target;
    }

    private boolean isEmptyLine(String line) {
        return line.isEmpty();
    }

    private boolean isCommentLine(String line) {
        return (line.trim().startsWith("#") || line.trim().startsWith("<"));
    }

    private boolean isKeyValuePair(String line) {
        if (getKey(line) != null)
            return true;
        else
            return false;
    }

    private String getKey(String line) {
        String parts[] = line.split("=");
        if (parts.length < 2)
            return null;
        else
            return parts[0].trim();
    }

    private HashMap<String, String> getPropSegments(Path inputPath, Charset charset)
            throws FileLoaderServiceException, IOException {

        HashMap<String, String> map = new LinkedHashMap<String, String>();
        OrderedProperties srcProp = new OrderedProperties();

        try (InputStream stream = new FileInputStream(inputPath.toString());
             InputStreamReader isr = new InputStreamReader(stream, charset)) {
            srcProp.load(isr);
            Set<String> keys = srcProp.stringPropertyNames();
            // checks for at least one (ASCII) alphanumeric character.
            map = keys.stream().filter(k -> k.toString().matches(".*\\w.*")).collect(Collectors.toMap(k -> k.toString(),
                    k -> srcProp.getProperty(k), (v1, v2) -> v1, LinkedHashMap::new));
        } catch (FileNotFoundException e) {
            throw new FileLoaderServiceException((messages.get("FileStorageService.not_find_file")) + " " + inputPath.toString(),
                    e);
        } catch (IOException e) {
            throw new FileLoaderServiceException(
                    (messages.get("FileStorageService.not_read_properties_file")) + " " + inputPath.toString(), e);
        }

        return map;
    }

    // nothing useful beneath this line
    // --------------------------------
    @Override
    public Path createSkeletonLngFile(Path storedOriginalFile, final long workId) throws IOException {

        WorkDto work = projectService.getWorkDtoById(workId);
        ProjectDto prj = projectService.getProjectDtoById(work.getProjectId());
        // Charset charset = iso8859util.getCharSet(workId);
        Charset charset = charSetResolver.getProjectCharSet(work.getProjectId());
        List<String> in = Files.readAllLines(storedOriginalFile, charset);
        List<String> out = new ArrayList<String>();
        List<UnitDto> unitDtos = projectService.listUnitDtos(workId);
        Map<String, String> map = new HashMap<String, String>();

        unitDtos.stream().forEach(dto -> map.put(dto.getSegmentKey(), dto.getTarget().getSkeletonTag()));

        boolean isFirstLine = true;
        for (String line : in) {
            if ((isEmptyLine(line)) || (isCommentLine(line)) || (isFirstLine)) {
                out.add(line);
                isFirstLine = false;
            } else if (isKeyValuePair(line)) {
                String key = getKey(line);
                String s = getKey(line) + "=" + map.get(key);
                System.out.println(s);
                out.add(s);
            } else
                throw new FileLoaderServiceException("Could not make skeleton file");
        }

        // WorkDto work = projectService.getWorkDtoById(workId);
        // ProjectDto prj =
        // projectService.getProjectDtoById(work.getProjectId());
        Path target = Paths.get(work.getSkeletonFile() + "." + prj.getFormat());
        Files.write(target, out);

        return target;
    }

}
