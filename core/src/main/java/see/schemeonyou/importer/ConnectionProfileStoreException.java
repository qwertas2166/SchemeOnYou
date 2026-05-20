package see.schemeonyou.importer;

import java.io.IOException;

public class ConnectionProfileStoreException extends IOException {
    public ConnectionProfileStoreException(String message) {
        super(message);
    }

    public ConnectionProfileStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
