package translateit2.filenameresolver;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import translateit2.fileloader.FileLoaderException;

@RunWith(MockitoJUnitRunner.class)
public class FileNameResolverUnitTests {

    @Test
    public void resolveFileName_assertAppName() {
        // when
        String returnAppName = fileNameResolver().getApplicationName("dotCMS_fi.properties");      
        // then
        assertThat(returnAppName, is(equalTo("dotCMS")));        

        // when
        returnAppName = fileNameResolver().getApplicationName("dotCMS-fi_FI_var.po");      
        // then
        assertThat(returnAppName, is(equalTo("dotCMS-fi")));
    }

    @Test
    public void resolveFileName_assertLocale() {
        
        // initialize
        String expected = "fi_FI";

        // when
        Locale returnLocale = fileNameResolver().getLocaleFromString("dotCMS_FI.properties",
                ext -> ext.equals("properties"));      
        // then
        String returned = returnLocale.toString(); 
        assertThat(expected, is(equalTo(returned)));

        // when
        returnLocale = fileNameResolver().getLocaleFromString("dotCMS_fi_fi.properties",
                ext -> ext.equals("properties"));      
        // then
        returned = returnLocale.toString(); 
        assertThat(expected, is(equalTo(returned)));

        // when
        returnLocale = fileNameResolver().getLocaleFromString("dotCMS_FI_FI_var.properties",
                ext -> ext.equals("properties"));      
        // then
        returned = returnLocale.toString(); 
        assertThat(expected, is(equalTo(returned)));

    }

    // https://www.petrikainulainen.net/programming/testing/writing-clean-tests-java-8-to-the-rescue/
    @Test
    public void resolveFileName_failIfAppNameOnlyFile() {

        assertThatThrownBy(() -> fileNameResolver().getApplicationName("dotCMS.properties"))
        .isExactlyInstanceOf(FileLoaderException.class);

        assertThatThrownBy(() -> fileNameResolver().getLocaleFromString("XXX.properties",
                ext -> ext.equals("properties")))
        .isExactlyInstanceOf(FileLoaderException.class);
    }

    @Test
    public void resolveFileName_failIfExtensionIncorrect() {

        assertThatThrownBy(() -> fileNameResolver().getLocaleFromString("XXX.xxx",
                ext -> ext.equals("properties")))
        .isExactlyInstanceOf(FileLoaderException.class);
    }

    @Test
    public void resolveFileName_failIfLocaleMissingOrIncorrect() {


        assertThatThrownBy(() -> fileNameResolver().getLocaleFromString("dotCMS_XXX.properties",
                ext -> ext.equals("properties")))
        .isExactlyInstanceOf(FileLoaderException.class);

        assertThatThrownBy(() -> fileNameResolver().getLocaleFromString("dotCMS-xx-XX.properties",
                ext -> ext.equals("properties")))
        .isExactlyInstanceOf(FileLoaderException.class);
    }

    private FileNameResolver fileNameResolver() {
        return new FileNameResolverImpl();
    }

}
