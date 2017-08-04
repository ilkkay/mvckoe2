package translateit2.languagefileservice;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import translateit2.util.OrderedProperties;

//
// just for testing
//

public class Iso8859TestLoader {

    public static void copyISO8859toUTF8(String srcFileLocationStr, String dstFileLocationStr) throws IOException {
        Path srcFilePath = Paths.get(srcFileLocationStr);
        Path dstFilePath = Paths.get(dstFileLocationStr);

        List<String> lines = Files.readAllLines(srcFilePath, StandardCharsets.ISO_8859_1);
        Files.write(dstFilePath, lines, StandardCharsets.UTF_8);
    }

    public static HashMap<String, String> getPropSegments(Path inputPath, Charset charset) throws IOException {

        InputStreamReader isr = null;
        InputStream stream = null;
        HashMap<String, String> map = new LinkedHashMap<String, String>();
        OrderedProperties srcProp = new OrderedProperties();

        try {
            stream = new FileInputStream(inputPath.toString());
            // isr = new InputStreamReader(stream,"UTF-8");
            isr = new InputStreamReader(stream, charset);
            srcProp.load(isr);
            Set<String> keys = srcProp.stringPropertyNames();
            // checks for at least one (ASCII) alphanumeric character.
            map = keys.stream().filter(k -> k.toString().matches(".*\\w.*")).collect(Collectors.toMap(k -> k.toString(),
                    k -> srcProp.getProperty(k), (v1, v2) -> v1, LinkedHashMap::new));

            map.forEach((k, v) -> System.out.println(k + "\n" + v));

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IOException("Error loading reading property file.", e);
        } finally {
            stream.close();
            isr.close();
        }

        return map;
    }

    public static HashMap<String, String> getPropSegments(String inputFileStr) throws IOException {

        InputStreamReader isr = null;
        InputStream stream = null;
        HashMap<String, String> map = new LinkedHashMap<String, String>();
        OrderedProperties srcProp = new OrderedProperties();

        try {
            stream = new FileInputStream(inputFileStr);
            // isr = new InputStreamReader(stream,"UTF-8");
            isr = new InputStreamReader(stream, "ISO-8859-1");
            srcProp.load(isr);
            Set<String> keys = srcProp.stringPropertyNames();
            map = keys.stream().collect(Collectors.toMap(k -> k.toString(), k -> srcProp.getProperty(k), (v1, v2) -> v1,
                    LinkedHashMap::new));

            map.forEach((k, v) -> System.out.println(k + "\n" + v));

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IOException("Error loading reading property file.", e);
        } finally {
            stream.close();
            isr.close();
        }

        return map;
    }

    /**
     * 
     * Creates empty target properties file (e.g. dotcms_fi_FI.properties) that
     * contains only property keys.
     * 
     * @param srcPath
     *            Uploaded properties file, UTF-8 or ISO8859-1 encoded
     * @param propFilename
     *            Filename of the uploaded file without language code or any
     *            other extension.
     * @param tgtLocale
     *            Locale of the target language
     * @return
     * @throws IOException
     */
    public static String initTargetLanguageFile(Path srcPath, String propFilename, Locale tgtLocale)
            throws IOException {

        if (tgtLocale == null)
            tgtLocale = Locale.getDefault();
        String tgtFilenameStr = propFilename + "_" + tgtLocale.toString() + ".properties";

        Path dir = srcPath.getParent();
        Path fn = srcPath.getFileSystem().getPath(tgtFilenameStr);
        Path target = (dir == null) ? fn : dir.resolve(fn);
        String tgtFileLocationStr = target.toString();
        String srcFileLocationStr = srcPath.toString();

        InputStream in = null;
        InputStreamReader isr = null;
        OutputStream out = null;
        OutputStreamWriter osr = null;

        OrderedProperties dstProp = new OrderedProperties();
        OrderedProperties srcProp = new OrderedProperties();

        try {
            in = new FileInputStream(srcFileLocationStr);
            out = new FileOutputStream(tgtFileLocationStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // isr = new InputStreamReader(in,"ISO-8859-1");
            isr = new InputStreamReader(in);
            osr = new OutputStreamWriter(out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            srcProp.load(isr);
            // nullifies property values
            Set<String> keys = srcProp.stringPropertyNames();
            keys.stream().forEach(key -> dstProp.setProperty(key.toString(), ""));
            dstProp.store(osr, "Translate IT 2");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            if (isr != null)
                isr.close();
            if (in != null)
                in.close();
            if (osr != null)
                osr.close();
            if (out != null)
                out.close();
        }

        return tgtFileLocationStr;
    }

    public static boolean isISO8859File(String srcFileLocationStr) throws IOException {
        Path srcFilePath = Paths.get(srcFileLocationStr);

        List<String> lines = Files.readAllLines(srcFilePath, StandardCharsets.UTF_8); // StandardCharsets.ISO_8859_1
                                                                                      // );

        // Check if a string that was decoded from bytes in latin1
        // could have been decoded in UTF-8, too. => Check if illegal byte
        // sequences are replaced
        // by the character \ufffd:
        boolean noReplacementCharacterFound = false;
        int count = 0;
        int negCount = 0;
        for (String line : lines) {
            String recoded = new String(line.getBytes("iso-8859-1"), "UTF-8");
            // noReplacementCharacterFound = recoded.indexOf('\uFFFD') == -1;
            if (recoded.indexOf('\uFFFD') == -1)
                count++;
            if (recoded.indexOf('\uFFFD') != -1)
                negCount++;
        }

        return noReplacementCharacterFound;
    }

    /*
     * public static List<Transu> testGetTransus() throws IOException { List
     * <Transu> transus =new ArrayList<Transu>();
     * 
     * try { int id=1;
     * 
     * ResourceBundle rb =
     * ResourceBundle.getBundle("translateit2.myresr.dotcms_en"); ResourceBundle
     * rb_fi = ResourceBundle.getBundle("translateit2.myresr.dotcms_fi");
     * 
     * Enumeration<String> keys = rb.getKeys(); while(keys.hasMoreElements()) {
     * String key = keys.nextElement(); Transu t=new Transu();
     * t.setSourceSegm(rb.getObject(key).toString()); if (true)
     * t.setTargetSegm(rb_fi.getObject(key).toString()); else
     * t.setTargetSegm(""); transus.add(t); } } catch (Exception e) {
     * e.printStackTrace(); throw new IOException("Error loading config.", e); }
     * 
     * return transus; }
     */
    // http://javarevisited.blogspot.fi/2014/04/how-to-convert-byte-array-to-inputstream-outputstream-java-example.html
    public static InputStream ISO8859toUTF8Stream(Path propPath) throws IOException {
        byte[] inData = Files.readAllBytes(propPath);
        String iso8859str = new String(inData, "ISO-8859-1");
        String utf8str = new String(iso8859str.getBytes(StandardCharsets.UTF_8));
        byte[] outData = utf8str.getBytes(StandardCharsets.UTF_8);
        InputStream newStream = new ByteArrayInputStream(outData);

        return newStream;
    }

    // none english properties file returns malformed if is neing read using
    // wrong character set
    // ISO8859-1 is almost identical to -15 where -15 replaces one encoding
    // with the Euro symbol and includes a few more french symbols. The only
    // way to tell them apart would be to look at the symbols in context.

    // UTF-8 is identical to ISO8859 for the first 128 ASCII characters which
    // include all the standard keyboard characters. After that, characters
    // are encoded as a multi-byte sequence.

    @Test
    public boolean isoTest() throws IOException {
        boolean isUTF_8;
        // Path uploadedLngFile = Paths.get("d:\\messages_fi_utf8.properties");
        Path uploadedLngFile = Paths.get("d:\\messages_fi.properties");

        try {
            List<String> lines = Files.readAllLines(uploadedLngFile, StandardCharsets.UTF_8);
            // StandardCharsets.ISO_8859_1 );
        } catch (MalformedInputException e) {
            // TODO Auto-generated catch block
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new IOException("Unexpected exception thrown in testing UTF-8 encoding");
        }
        return true; // UTF8 read as UTF8 and no exceptions

    }

}
