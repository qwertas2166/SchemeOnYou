package see;

import see.launcher.CoreLauncher;

import java.util.Properties;

public class Main {

    static void main() {
        Properties properties = new Properties();
        // Core launcher
        CoreLauncher coreLauncher = new CoreLauncher();
        coreLauncher.launch(properties);
    }
}
