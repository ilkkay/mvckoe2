package translateit2.languagebeancache;

import org.springframework.stereotype.Component;

import translateit2.lngfileservice.LanguageFileFormat;

@Component
public class PropertiesFileWriterImpl implements LanguageFileWriter {

    public PropertiesFileWriterImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public LanguageFileFormat getFileFormat() {
        return LanguageFileFormat.PROPERTIES;
    }

    @Override
    public void write() { };
}
