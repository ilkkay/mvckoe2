package translateit2.languagefactory;

import org.springframework.stereotype.Component;

import translateit2.lngfileservice.LanguageFileFormat;

@Component
public class DefaultLanguageFileWriterImpl implements LanguageFileWriter {

    public DefaultLanguageFileWriterImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public LanguageFileFormat getFileFormat() {
        return LanguageFileFormat.DEFAULT;
    }

    @Override
    public void write() { };
}
