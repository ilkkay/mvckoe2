package translateit2.languagefileservice.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileStorage;

//@Component
public class LngFileServiceProvider implements ApplicationContextAware, LanguageFileServiceFactory {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getContext() {
        return applicationContext;
    }

    private void printBeans() {
        System.out.println(Arrays.asList(this.getContext().getBeanDefinitionNames()));
    }

    private Optional<LanguageFileStorage> castIntoOptional(Object obj) {
        if (LanguageFileStorage.class.isInstance(obj))
            return Optional.of((LanguageFileStorage) obj);
        else
            return Optional.empty();
    }

    @Override
    public Optional<LanguageFileStorage> getService(LanguageFileFormat serviceName) {
        LanguageFileStorage service = (LanguageFileStorage) applicationContext.getBean(serviceName.toString());

        return castIntoOptional(service);
    }

    @Override
    public List<LanguageFileFormat> listFormatsSupported() {
        // TODO Auto-generated method stub
        return null;
    }

}
