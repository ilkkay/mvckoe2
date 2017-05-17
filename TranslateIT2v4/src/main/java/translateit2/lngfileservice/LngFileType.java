package translateit2.lngfileservice;

public enum LngFileType {

	// http://crunchify.com/why-and-for-what-should-i-use-enum-java-enum-examples/
	UTF_8(1), ISO8859_1(2);
	private int value;
	private String types[] = {"UTF_8", "ISO8859_1"};
	
	private LngFileType(int value) {
		this.value = value;
	}
	
	public String toString() {
		return types[value];
	}

}
