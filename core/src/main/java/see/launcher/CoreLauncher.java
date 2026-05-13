package see.launcher;

import lombok.CustomLog;
import see.di.launcher.SeeConfigDiLauncher;
import see.di.launcher.SeeDiLauncher;
import see.di.launcher.SeeObjectDiLauncher;
import see.startup.Launcher;

import java.util.Properties;

@CustomLog
public class CoreLauncher extends Launcher {

    @Override
    public void launch(Properties properties) {
        SeeDiLauncher seeConfigDiLauncher = new SeeConfigDiLauncher();
        properties.put(SeeDiLauncher.class, seeConfigDiLauncher);
        seeConfigDiLauncher.launch(properties);
        SeeDiLauncher seeObjectDiLauncher = new SeeObjectDiLauncher();
        seeObjectDiLauncher.launch(properties);
        SeeInfo seeInfo = seeObjectDiLauncher.get(SeeInfo.class);
        seeInfo.info(this::printLogo);
    }

    @Override
    protected void printLogo() {
        System.out.println(LEAN_LOGO);
    }
}
