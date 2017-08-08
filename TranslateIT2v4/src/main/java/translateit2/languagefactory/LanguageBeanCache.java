package translateit2.languagefactory;

import translateit2.languagefileservice.factory.AbstractLanguageFile;

public interface LanguageBeanCache <T extends AbstractLanguageFile <FORMAT>, FORMAT> 
    extends AbstractFactory <T, FORMAT> {
    
}