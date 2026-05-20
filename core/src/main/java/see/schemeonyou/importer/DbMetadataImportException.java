package see.schemeonyou.importer;

public class DbMetadataImportException extends Exception {
    public DbMetadataImportException(String message) {
        super(message);
    }

    public DbMetadataImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
