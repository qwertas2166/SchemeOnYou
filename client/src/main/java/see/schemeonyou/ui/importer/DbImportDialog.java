package see.schemeonyou.ui.importer;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import see.schemeonyou.importer.ConnectionProfile;
import see.schemeonyou.importer.ConnectionProfileDraft;
import see.schemeonyou.importer.ConnectionProfileStore;
import see.schemeonyou.importer.DbImportDiagramReplacer;
import see.schemeonyou.importer.DbImportResult;
import see.schemeonyou.importer.PostgreSqlMetadataImporter;
import see.schemeonyou.model.SchemeProject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** JavaFX modal flow for importing PostgreSQL metadata into the selected DB diagram. */
public final class DbImportDialog {
    private static final String DRIVER_POSTGRESQL = "postgresql";

    private final Window owner;
    private final SchemeProject project;
    private final Supplier<String> selectedDiagramId;
    private final ConnectionProfileStore profileStore;
    private final PostgreSqlMetadataImporter metadataImporter;
    private final BiFunction<String, DbImportResult, DbImportDiagramReplacer.Result> importReplace;
    private final Consumer<DbImportDiagramReplacer.Result> onImported;

    private final ComboBox<ConnectionProfile> profiles = new ComboBox<>();
    private final TextField displayName = new TextField();
    private final TextField host = new TextField("localhost");
    private final TextField port = new TextField("5432");
    private final TextField database = new TextField();
    private final TextField user = new TextField();
    private final PasswordField password = new PasswordField();
    private final TextField schema = new TextField("public");
    private final Label status = new Label("Choose or create a profile.");
    private final ProgressIndicator progress = new ProgressIndicator();
    private final Button saveProfile = new Button("Save profile");
    private final Button newProfile = new Button("New");
    private final Button deleteProfile = new Button("Delete");
    private final Button importButton = new Button("Import and replace…");
    private final Button cancelImport = new Button("Cancel import");
    private Task<DbImportResult> runningTask;
    private long importRunId;
    private boolean running;

    public DbImportDialog(
            Window owner,
            SchemeProject project,
            Supplier<String> selectedDiagramId,
            ConnectionProfileStore profileStore,
            PostgreSqlMetadataImporter metadataImporter,
            BiFunction<String, DbImportResult, DbImportDiagramReplacer.Result> importReplace,
            Consumer<DbImportDiagramReplacer.Result> onImported
    ) {
        this.owner = owner;
        this.project = Objects.requireNonNull(project, "project");
        this.selectedDiagramId = Objects.requireNonNull(selectedDiagramId, "selectedDiagramId");
        this.profileStore = Objects.requireNonNull(profileStore, "profileStore");
        this.metadataImporter = Objects.requireNonNull(metadataImporter, "metadataImporter");
        this.importReplace = Objects.requireNonNull(importReplace, "importReplace");
        this.onImported = Objects.requireNonNull(onImported, "onImported");
    }

    public void show() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Import tables from PostgreSQL");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().setContent(content());
        configureActions(dialog);
        reloadProfiles();
        dialog.showAndWait();
    }

    private VBox content() {
        profiles.setPromptText("Saved profiles");
        profiles.setMaxWidth(Double.MAX_VALUE);
        profiles.setCellFactory(list -> profileCell());
        profiles.setButtonCell(profileCell());
        profiles.valueProperty().addListener((obs, old, profile) -> populate(profile));

        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);
        form.addRow(0, new Label("Profile"), profiles);
        form.addRow(1, new Label("Name"), displayName);
        form.addRow(2, new Label("Host"), host);
        form.addRow(3, new Label("Port"), port);
        form.addRow(4, new Label("Database"), database);
        form.addRow(5, new Label("User"), user);
        form.addRow(6, new Label("Password"), password);
        form.addRow(7, new Label("Schema/catalog"), schema);
        GridPane.setHgrow(profiles, Priority.ALWAYS);
        GridPane.setHgrow(displayName, Priority.ALWAYS);
        GridPane.setHgrow(host, Priority.ALWAYS);
        GridPane.setHgrow(database, Priority.ALWAYS);
        GridPane.setHgrow(user, Priority.ALWAYS);
        GridPane.setHgrow(password, Priority.ALWAYS);
        GridPane.setHgrow(schema, Priority.ALWAYS);

        progress.setVisible(false);
        progress.setManaged(false);
        progress.setPrefSize(22, 22);
        HBox profileActions = new HBox(8, newProfile, saveProfile, deleteProfile);
        cancelImport.setVisible(false);
        cancelImport.setManaged(false);
        HBox importActions = new HBox(8, importButton, cancelImport, progress);
        VBox root = new VBox(10, form, profileActions, importActions, status);
        root.setPadding(new Insets(12));
        root.setPrefWidth(520);
        return root;
    }

    private ListCell<ConnectionProfile> profileCell() {
        return new ListCell<>() {
            @Override protected void updateItem(ConnectionProfile item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.displayName() + " — " + item.user() + "@" + item.host() + "/" + item.database());
            }
        };
    }

    private void configureActions(Dialog<Void> dialog) {
        newProfile.setOnAction(event -> {
            profiles.getSelectionModel().clearSelection();
            displayName.clear();
            host.setText("localhost");
            port.setText("5432");
            database.clear();
            user.clear();
            password.clear();
            schema.setText("public");
            status.setText("New unsaved profile.");
        });
        saveProfile.setOnAction(event -> saveProfile());
        deleteProfile.setOnAction(event -> deleteSelectedProfile());
        importButton.setOnAction(event -> importAndReplace(dialog));
        cancelImport.setOnAction(event -> cancelRunningImport());
        dialog.setOnCloseRequest(event -> {
            if (!running) return;
            cancelRunningImport();
            event.consume();
        });
        Button closeButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            if (!running) return;
            cancelRunningImport();
            event.consume();
        });
    }

    private void reloadProfiles() {
        try {
            List<ConnectionProfile> loaded = profileStore.list();
            profiles.getItems().setAll(loaded);
            if (!loaded.isEmpty() && profiles.getValue() == null) profiles.getSelectionModel().selectFirst();
            status.setText(loaded.isEmpty() ? "No saved profiles yet." : "Profiles loaded from " + profileStore.file());
        } catch (IOException e) {
            status.setText(redact("Could not load profiles: " + e.getMessage()));
        }
    }

    private void populate(ConnectionProfile profile) {
        if (profile == null) return;
        displayName.setText(profile.displayName());
        host.setText(profile.host());
        port.setText(Integer.toString(profile.port()));
        database.setText(profile.database());
        user.setText(profile.user());
        password.setText(profile.password());
        schema.setText(profile.defaultSchemaOrCatalog().isBlank() ? "public" : profile.defaultSchemaOrCatalog());
    }

    private void saveProfile() {
        try {
            ConnectionProfileDraft draft = draftFromFields();
            ConnectionProfile selected = profiles.getValue();
            ConnectionProfile saved = selected == null ? profileStore.create(draft) : profileStore.update(selected.id(), draft);
            profiles.getItems().setAll(profileStore.list());
            profiles.getSelectionModel().select(saved);
            status.setText("Profile saved.");
        } catch (Exception e) {
            status.setText(redact("Could not save profile: " + e.getMessage()));
        }
    }

    private void deleteSelectedProfile() {
        ConnectionProfile selected = profiles.getValue();
        if (selected == null) {
            status.setText("No profile selected.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete local profile '" + selected.displayName() + "'?", ButtonType.CANCEL, ButtonType.OK);
        confirm.initOwner(owner);
        confirm.setHeaderText("Delete connection profile");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;
        try {
            profileStore.delete(selected.id());
            profiles.getItems().setAll(profileStore.list());
            profiles.getSelectionModel().clearSelection();
            status.setText("Profile deleted.");
        } catch (IOException e) {
            status.setText(redact("Could not delete profile: " + e.getMessage()));
        }
    }

    private void importAndReplace(Dialog<Void> dialog) {
        ConnectionProfileDraft draft;
        try {
            draft = draftFromFields();
        } catch (RuntimeException e) {
            status.setText(redact("Invalid profile: " + e.getMessage()));
            return;
        }
        ConnectionProfile selected = profiles.getValue();
        ConnectionProfile profile = selected == null
                ? new ConnectionProfile("unsaved", draft.displayName(), draft.host(), draft.port(), draft.database(), draft.user(), draft.password(), draft.defaultSchemaOrCatalog(), DRIVER_POSTGRESQL)
                : selected.withDraft(draft);

        Alert warning = new Alert(Alert.AlertType.WARNING,
                "All current content of the selected database diagram will be deleted and replaced by imported tables. Continue?",
                ButtonType.CANCEL, ButtonType.OK);
        warning.initOwner(owner);
        warning.setHeaderText("Replace selected DB diagram");
        if (warning.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        setRunning(true);
        status.setText("Connecting…");
        long runId = ++importRunId;
        Task<DbImportResult> task = new Task<>() {
            @Override protected DbImportResult call() throws Exception {
                updateMessage("Reading metadata…");
                DbImportResult result = metadataImporter.importProfile(profile, schema.getText());
                updateMessage("Importing " + result.tables().size() + " tables…");
                return result;
            }
        };
        runningTask = task;
        status.textProperty().bind(task.messageProperty());
        task.setOnSucceeded(event -> {
            status.textProperty().unbind();
            if (isStaleOrCancelled(task, runId)) return;
            DbImportResult result = task.getValue();
            DbImportDiagramReplacer.Result replaced = importReplace.apply(selectedDiagramId.get(), result);
            onImported.accept(replaced);
            status.setText(formatImportSuccessStatus(
                    replaced.tableCount(),
                    replaced.foreignKeyCount(),
                    result.importWarnings(),
                    password.getText()
            ));
            setRunning(false);
            if (result.importWarnings().isEmpty()) Platform.runLater(dialog::close);
        });
        task.setOnCancelled(event -> {
            status.textProperty().unbind();
            if (runningTask == task) runningTask = null;
            status.setText("Import cancelled. No project changes were applied.");
            setRunning(false);
        });
        task.setOnFailed(event -> {
            status.textProperty().unbind();
            if (isStaleOrCancelled(task, runId)) return;
            status.setText(redact("Import failed: " + task.getException().getMessage()));
            setRunning(false);
        });
        Thread worker = new Thread(task, "db-import-modal");
        worker.setDaemon(true);
        worker.start();
    }

    private ConnectionProfileDraft draftFromFields() {
        return ConnectionProfileDraft.postgresql(
                displayName.getText(),
                host.getText(),
                Integer.parseInt(port.getText().trim()),
                database.getText(),
                user.getText(),
                password.getText(),
                schema.getText()
        );
    }

    private void cancelRunningImport() {
        Task<DbImportResult> task = runningTask;
        importRunId++;
        if (task != null) task.cancel(true);
        if (status.textProperty().isBound()) status.textProperty().unbind();
        runningTask = null;
        status.setText("Import cancelled. No project changes were applied.");
        setRunning(false);
    }

    private boolean isStaleOrCancelled(Task<DbImportResult> task, long runId) {
        if (task.isCancelled() || runningTask != task || importRunId != runId) {
            if (runningTask == task) runningTask = null;
            status.setText("Import cancelled. Late DB response ignored; no project changes were applied.");
            setRunning(false);
            return true;
        }
        runningTask = null;
        return false;
    }

    private void setRunning(boolean running) {
        this.running = running;
        importButton.setDisable(running);
        saveProfile.setDisable(running);
        newProfile.setDisable(running);
        deleteProfile.setDisable(running);
        progress.setVisible(running);
        progress.setManaged(running);
        cancelImport.setVisible(running);
        cancelImport.setManaged(running);
    }

    static String formatImportSuccessStatus(int tableCount, int foreignKeyCount, List<String> importWarnings, String secret) {
        String base = "Done: imported " + tableCount + " tables and " + foreignKeyCount
                + " foreign keys. Use Ctrl+Z to undo.";
        if (importWarnings.isEmpty()) return base;
        List<String> redactedWarnings = importWarnings.stream()
                .map(warning -> redact(warning, secret))
                .toList();
        if (redactedWarnings.size() == 1) return base + " Warning: " + redactedWarnings.getFirst();
        String details = redactedWarnings.stream()
                .map(warning -> "- " + warning)
                .reduce((left, right) -> left + "\n" + right)
                .orElse("");
        return base + " Warnings (" + redactedWarnings.size() + "):\n" + details;
    }

    private String redact(String message) {
        return redact(message, password.getText());
    }

    private static String redact(String message, String secret) {
        if (secret == null || secret.isBlank()) return message;
        return message.replace(secret, "******");
    }
}
