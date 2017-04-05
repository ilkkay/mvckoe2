package translateit2.lngfileservice.factory;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import translateit2.lngfileservice.LngFileStorage;

@Component
public class LngFileServiceProvider implements ApplicationContextAware, LngFileServiceFactory {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getContext() {
        return applicationContext;
    }
 

    private Optional<LngFileStorage> castIntoOptional(Object obj) {
	if (LngFileStorage.class.isInstance(obj))
		return Optional.of((LngFileStorage) obj);
	else
		return Optional.empty();
    }

    @Override
	public Optional<LngFileStorage> getService(String serviceName) {
    	LngFileStorage service = (LngFileStorage) applicationContext.
    			getBean(serviceName);
    
    	return castIntoOptional(service);
    }

	@Override
	public List<String> listFormatsSupported() {
		// TODO Auto-generated method stub
		return null;
	}

}
