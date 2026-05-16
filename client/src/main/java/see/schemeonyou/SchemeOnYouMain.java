package see.schemeonyou;

import see.launcher.CoreLauncher;

import java.util.Properties;

public class SchemeOnYouMain {
    public static void main(String[] args) {
        boolean coreOnly = Boolean.getBoolean("schemeonyou.coreOnly") || (args != null && args.length > 0 && "--core-only".equals(args[0]));
        if (coreOnly) {
            Properties properties = new Properties();
            properties.put("args", args == null ? new String[0] : args.clone());
            new CoreLauncher().launch(properties);
            return;
        }
        SchemeOnYouApplication.main(args);
    }
}
