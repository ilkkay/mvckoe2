package translateit2.languagebeancache;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import translateit2.fileloader.FileLoaderException;
import translateit2.languagebeancache.PropertiesFileReaderImpl;
import translateit2.languagefile.LanguageFileFormat;

public class PropertiesFileWriterUnitTest {

    @Test
    public void readCommentLine_EmptyLine_And_ProperSegmentLine_assertLineCount() {
        HashMap <String, String> map = new LinkedHashMap<String, String>();
        List <String> originalFileAsList = new ArrayList<String>();

        // WHEN         
        originalFileAsList.add("## DOTCMS-3022");
        originalFileAsList.add("");
        originalFileAsList.add("alert-file-too-large-takes-lot-of-time=Saving the file may take a longer time because of its size");
        map.put("alert-file-too-large-takes-lot-of-time", "Tiedoston tallennus voi kestää pidempään, jos sen koko on suuri.");

        List <List<String>> stringsList = new ArrayList <List<String>> (); 
        assertThatCode(() ->{ stringsList.add((List<String>) writer().mergeWithOriginalFile(map, originalFileAsList)); })
        .doesNotThrowAnyException();
        
        int returnedSegmentCount = stringsList.get(0).size();
        int expectedSegmentCount = originalFileAsList.size();
        assertThat(expectedSegmentCount,equalTo(returnedSegmentCount));
        
    }
    
    private PropertiesFileWriterImpl writer() {
        return new PropertiesFileWriterImpl();
    }

}
