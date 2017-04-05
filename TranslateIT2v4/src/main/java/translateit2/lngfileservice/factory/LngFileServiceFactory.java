package translateit2.lngfileservice.factory;

import java.util.List;
import java.util.Optional;

import translateit2.lngfileservice.LngFileStorage;

public interface LngFileServiceFactory {
	public Optional <LngFileStorage> getService(String type);
	public List<String> listFormatsSupported();
}
