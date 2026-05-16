package see.launcher;

import lombok.CustomLog;
import see.di.See;

@See
@CustomLog
public class SeeInfo {

    public void info(Runnable runnable) {
        log.info("It's 'Scheme On You' application. Welcome");
        runnable.run();
    }
}
