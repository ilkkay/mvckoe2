package translateit2.lngfileservice;

import java.nio.file.Path;

public interface LngFileStorage {
	String getType();
	String getGreetings();
	Path getPath(String filename);
}
