package see.schemeonyou.ui.command;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class KeyLogController {
    private static final DateTimeFormatter KEY_LOG_TIME = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final String KEY_LOG_PROPERTY = "schemeonyou.debug.keyLog";
    private static final String KEY_LOG_ENV = "SCHEMEONYOU_DEBUG_KEY_LOG";

    private Stage keyLogStage;
    private TextArea keyLogArea;
    private long keyLogCounter;

    public static boolean isEnabled() {
        return Boolean.getBoolean(KEY_LOG_PROPERTY) || "true".equalsIgnoreCase(System.getenv(KEY_LOG_ENV));
    }

    public void logKeyPress(KeyEvent event) {
        if (keyLogArea == null) return;
        keyLogArea.appendText(String.format(
                "%04d  %s  %-12s  text='%s'  modifiers=%s%n",
                ++keyLogCounter,
                LocalTime.now().format(KEY_LOG_TIME),
                event.getCode(),
                printable(event.getText()),
                modifiers(event)
        ));
    }

    public void show(Stage owner) {
        if (keyLogStage == null) {
            keyLogArea = new TextArea();
            keyLogArea.setEditable(false);
            keyLogArea.setWrapText(false);
            keyLogArea.setFont(Font.font("Monospaced", 12));
            keyLogArea.setText("Key press log. Press keys in the main window; F12 shows this window.\n");

            Button clear = new Button("Clear");
            clear.setOnAction(e -> clear());

            BorderPane root = new BorderPane(keyLogArea);
            root.setTop(new ToolBar(new Label("SchemeOnYou key log"), new Separator(), clear));
            Scene scene = new Scene(root, 680, 420);
            scene.addEventFilter(KeyEvent.KEY_PRESSED, this::logKeyPress);
            scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
                if (e.getCode() == KeyCode.F12) {
                    owner.requestFocus();
                    e.consume();
                }
            });

            keyLogStage = new Stage();
            keyLogStage.setTitle("SchemeOnYou — Key Log");
            keyLogStage.initOwner(owner);
            keyLogStage.setScene(scene);
            keyLogStage.setOnCloseRequest(e -> {
                keyLogStage.hide();
                e.consume();
            });
        }
        keyLogStage.show();
        keyLogStage.toFront();
    }

    private void clear() {
        keyLogCounter = 0;
        keyLogArea.setText("Key press log cleared.\n");
    }

    private String modifiers(KeyEvent event) {
        StringBuilder modifiers = new StringBuilder();
        if (event.isControlDown()) modifiers.append("Ctrl+");
        if (event.isAltDown()) modifiers.append("Alt+");
        if (event.isShiftDown()) modifiers.append("Shift+");
        if (event.isMetaDown()) modifiers.append("Meta+");
        if (modifiers.isEmpty()) return "none";
        modifiers.setLength(modifiers.length() - 1);
        return modifiers.toString();
    }

    private String printable(String text) {
        if (text == null || text.isEmpty()) return "";
        return text.replace("\\", "\\\\").replace("'", "\\'").replace("\n", "\\n").replace("\r", "\\r");
    }
}
