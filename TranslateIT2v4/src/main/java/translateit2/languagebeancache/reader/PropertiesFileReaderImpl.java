package translateit2.languagebeancache.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import translateit2.fileloader.FileLoadError;
import translateit2.fileloader.FileLoaderException;
import translateit2.languagefile.LanguageFileFormat;
import translateit2.persistence.model.Work;
import translateit2.util.OrderedProperties;

@Component
public class PropertiesFileReaderImpl implements LanguageFileReader {

    public PropertiesFileReaderImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public LanguageFileFormat getFileFormat() {
        return LanguageFileFormat.PROPERTIES;
    }

    @Override
    public HashMap<String, String> getSegments(Path inputPath, Charset charset) throws FileLoaderException {
        HashMap<String, String> map = new LinkedHashMap<String, String>();
        OrderedProperties srcProp = new OrderedProperties();

        try (InputStream stream = new FileInputStream(inputPath.toString());
                InputStreamReader isr = new InputStreamReader(stream, charset)) {
            srcProp.load(isr);
            Set<String> keys = srcProp.stringPropertyNames();
            // checks for at least one (ASCII) alphanumeric character.
            map = keys.stream().filter(k -> k.toString().matches(".*\\w.*")).collect(Collectors.toMap(k -> k.toString(),
                    k -> srcProp.getProperty(k), (v1, v2) -> v1, LinkedHashMap::new));

            //map.forEach((k, v) -> System.out.println(k + "\n" + v));

        } catch (IOException e) {
            throw new FileLoaderException(FileLoadError.CANNOT_READ_FILE,e.getCause());
        }

        return map;
    }

    @Override
    public List<String> getOriginalFileAsList(Path storedOriginalFile, Charset charSet) throws FileLoaderException {

        try {
            return Files.readAllLines(storedOriginalFile, charSet);
        } catch (IOException e) {
            throw new FileLoaderException(FileLoadError.CANNOT_READ_FILE,e.getCause());
        }
    }
}
