package translateit2.languagefactory;

import org.springframework.stereotype.Component;

import translateit2.lngfileservice.LanguageFileFormat;

@Component
public class DefaultLanguageFileValidatorImpl implements LanguageFileValidator {

    public DefaultLanguageFileValidatorImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public LanguageFileFormat getFileFormat() {
        return LanguageFileFormat.DEFAULT;
    }

    public void validate() { };
}
