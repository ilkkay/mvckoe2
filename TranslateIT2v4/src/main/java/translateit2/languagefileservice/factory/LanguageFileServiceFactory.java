package translateit2.languagefileservice.factory;

import java.util.List;
import java.util.Optional;

import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileStorage;

public interface LanguageFileServiceFactory {
	public Optional <LanguageFileStorage> getService(LanguageFileFormat type);
	public List<LanguageFileFormat> listFormatsSupported();
}
