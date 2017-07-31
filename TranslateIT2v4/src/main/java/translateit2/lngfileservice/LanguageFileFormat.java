package translateit2.lngfileservice;

public enum LanguageFileFormat {
    // http://crunchify.com/why-and-for-what-should-i-use-enum-java-enum-examples/
    XLIFF(0), PROPERTIES(1), DEFAULT(2), PO(3);
    private int value;
    private String types[] = { "xliff", "properties", "default", "po" };

    private LanguageFileFormat(int value) {
        this.value = value;
    }

    public String toString() {
        return types[value];
    }

}
