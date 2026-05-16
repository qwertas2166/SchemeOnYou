package see.schemeonyou;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import see.schemeonyou.command.*;
import see.schemeonyou.export.SvgExporter;
import see.schemeonyou.layout.DeterministicLayoutEngine;
import see.schemeonyou.model.*;
import see.schemeonyou.service.ProjectFactory;
import see.schemeonyou.storage.SchemeProjectStorage;
import see.schemeonyou.ui.ApplicationLayoutConstraints;
import see.schemeonyou.ui.DocumentState;
import see.schemeonyou.ui.FocusAreaNavigator;
import see.schemeonyou.ui.canvas.CanvasHitTestPresenter;
import see.schemeonyou.ui.command.CommandRouter;
import see.schemeonyou.ui.command.ShortcutMap;
import see.schemeonyou.ui.command.SpaceCommandSheet;
import see.schemeonyou.ui.canvas.CanvasShell;
import see.schemeonyou.ui.canvas.DeleteSelectedController;
import see.schemeonyou.ui.presenter.DocumentLifecycleGuard;
import see.schemeonyou.ui.presenter.InspectorPresenter;
import see.schemeonyou.ui.search.FindElementPresenter;
import see.schemeonyou.validation.DiagramValidator;
import see.schemeonyou.validation.ValidationIssue;
import see.schemeonyou.validation.ValidationResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class SchemeOnYouApplication extends Application {
    private static final DateTimeFormatter KEY_LOG_TIME = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final double DESIGN_FOOTER_OVERLAY_HEIGHT = 78.0;
    private static final String APP_BG = "#1F232A";
    private static final String PANEL_BG = "#252A32";
    private static final String PANEL_ELEVATED = "#2B313B";
    private static final String CANVAS_BG = "#303640";
    private static final String CARD_BG = "#252B34";
    private static final String CARD_HEADER = "#303847";
    private static final String BORDER = "#48515F";
    private static final String TEXT_PRIMARY = "#E6EAF0";
    private static final String TEXT_SECONDARY = "#A8B0BD";
    private static final String SELECTION = "#4C8DFF";
    private static final String FK_COLOR = "#62AEEF";
    private static final String PK_COLOR = "#D7A84A";
    private static final String PIN_COLOR = "#E6B450";
    private static final String WARNING = "#D19A3E";
    private static final String ERROR = "#E06C75";
    private static final String FOCUS_RING = "-fx-effect: dropshadow(gaussian, rgba(76, 141, 255, 0.75), 0, 0, 0, 0); -fx-border-color: " + SELECTION + "; -fx-border-width: 2; -fx-border-radius: 6;";
    private static final String UI_FONT_FAMILY = "Monaspace Krypton";

    private final ProjectFactory factory = new ProjectFactory();
    private final UndoRedoStack undo = new UndoRedoStack();
    private final DeterministicLayoutEngine layout = new DeterministicLayoutEngine();
    private final SvgExporter svgExporter = new SvgExporter();
    private final SchemeProjectStorage storage = new SchemeProjectStorage();
    private static final String KEY_LOG_PROPERTY = "schemeonyou.debug.keyLog";
    private static final String KEY_LOG_ENV = "SCHEMEONYOU_DEBUG_KEY_LOG";

    private final boolean keyLogEnabled = isKeyLogEnabled();
    private final ShortcutMap shortcuts = new ShortcutMap(keyLogEnabled);
    private final CommandRouter commandRouter = new CommandRouter();
    private final SpaceCommandSheet spaceSheet = new SpaceCommandSheet();
    private final DiagramValidator validator = new DiagramValidator();
    private final CanvasHitTestPresenter canvasHitTestPresenter = new CanvasHitTestPresenter();
    private final DeleteSelectedController deleteSelectedController = new DeleteSelectedController();
    private final CanvasShell deleteSelection = new CanvasShell();
    private final see.schemeonyou.ui.ContextLineResolver contextLineResolver = new see.schemeonyou.ui.ContextLineResolver();
    private final CreateJoinTableCommandFactory joinTableFactory = new CreateJoinTableCommandFactory();
    private final DocumentState documentState = new DocumentState();
    private final DocumentLifecycleGuard documentLifecycleGuard = new DocumentLifecycleGuard();
    private final FindElementPresenter findElementPresenter = new FindElementPresenter();
    private final InspectorPresenter inspectorPresenter = new InspectorPresenter();

    private SchemeProject project;
    private Diagram activeDiagram;
    private String selectedTableId;
    private String selectedColumnId;
    private String selectedForeignKeyId;
    private String selectedParticipantId;
    private String selectedMessageId;
    private final RelationPin relationPin = new RelationPin();
    private FkPreview activeFkPreview;
    private Stage stage;
    private ToolBar topMenu;
    private Label projectTitle;
    private Label dirtyState;
    private SplitPane leftMenu;
    private VBox inspectorPanel;
    private ListView<Diagram> diagramList;
    private ListView<DbTable> tableList;
    private Canvas canvas;
    private StackPane canvasStack;
    private VBox canvasHintFooter;
    private Label canvasHintTitle;
    private Label canvasHintBody;
    private String canvasHintKind;
    private VBox commandPaletteOverlay;
    private TextField commandPaletteSearch;
    private ListView<CommandMetadata> commandPaletteResults;
    private VBox inspectorFields;
    private ScrollPane inspector;
    private Label context;
    private Label status;
    private Stage keyLogStage;
    private TextArea keyLogArea;
    private long keyLogCounter;
    private boolean spaceCommandMode;
    private double lastPanSceneX;
    private double lastPanSceneY;
    private String draggingElementId;
    private Rect draggingOldRect;
    private double draggingOffsetX;
    private double draggingOffsetY;
    private boolean draggingMoved;
    private final StringBuilder spaceCommandBuffer = new StringBuilder();

    private Font uiFont(double size) {
        return Font.font(UI_FONT_FAMILY, FontWeight.EXTRA_LIGHT, size);
    }

    private Font uiHeadingFont(double size) {
        return Font.font(UI_FONT_FAMILY, FontWeight.NORMAL, size);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        seedProject();
        registerCommands();

        StackPane root = appRoot();
        BorderPane layout = appLayout();
        StackPane leftArea = functionalArea("functional-area-left", "1 Left menu", projectTree());
        StackPane canvasArea = functionalArea("functional-area-canvas", "2 Canvas", canvasPane());
        StackPane inspectorArea = functionalArea("functional-area-inspector", "3 Inspector", inspectorPane());
        ApplicationLayoutConstraints.configureLeftPanel(leftArea);
        ApplicationLayoutConstraints.configureCanvasArea(canvasArea);
        ApplicationLayoutConstraints.configureInspector(inspectorArea);
        layout.setTop(functionalArea("functional-area-top", "0 Top menu", topBar()));
        layout.setLeft(leftArea);
        layout.setCenter(canvasArea);
        layout.setRight(inspectorArea);
        layout.setBottom(functionalArea("functional-area-footer", "Status", footer()));
        root.getChildren().add(layout);

        Scene scene = new Scene(root, 1280, 820);
        scene.getStylesheets().add(java.util.Objects.requireNonNull(getClass().getResource("/see/schemeonyou/ui/theme.css")).toExternalForm());
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::logKeyPress);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleShortcut);
        stage.setTitle("SchemeOnYou");
        stage.setOnCloseRequest(event -> {
            if (!confirmUnsavedChanges()) {
                event.consume();
            }
        });
        updateDocumentChrome();
        stage.setScene(scene);
        stage.show();
        Platform.runLater(() -> focusArea(leftMenu, "левое меню"));
        redraw();
    }


    private StackPane appRoot() {
        Pane emptyBackground = new Pane();
        emptyBackground.getStyleClass().add("app-empty-background");
        emptyBackground.setMouseTransparent(true);

        StackPane root = new StackPane(emptyBackground);
        root.getStyleClass().add("app-root-background");
        return root;
    }

    private BorderPane appLayout() {
        BorderPane layout = new BorderPane();
        layout.getStyleClass().add("app-layout");
        return layout;
    }

    private StackPane functionalArea(String styleClass, String title, Node content) {
        Label legend = new Label(title);
        legend.getStyleClass().add("functional-area-legend");
        legend.setMouseTransparent(true);

        StackPane container = new StackPane(content, legend);
        container.getStyleClass().addAll("functional-area", styleClass);
        StackPane.setAlignment(content, Pos.CENTER);
        StackPane.setAlignment(legend, Pos.TOP_LEFT);
        return container;
    }

    private void seedProject() {
        project = factory.createProject("Untitled SchemeOnYou project");
        activeDiagram = project.getDiagrams().stream().filter(d -> d.getType() == DiagramType.DATABASE).findFirst().orElse(project.getDiagrams().getFirst());
        DbTable users = factory.table("users");
        DbColumn usersId = factory.column("id", "uuid"); usersId.setPrimaryKey(true); usersId.setNullable(false);
        DbColumn email = factory.column("email", "text"); email.setUnique(true); email.setNullable(false);
        users.getColumns().addAll(List.of(usersId, email));
        DbTable orders = factory.table("orders");
        DbColumn ordersId = factory.column("id", "uuid"); ordersId.setPrimaryKey(true); ordersId.setNullable(false);
        DbColumn userId = factory.column("user_id", "uuid"); userId.setNullable(false);
        orders.getColumns().addAll(List.of(ordersId, userId));
        activeDiagram.getTables().addAll(List.of(users, orders));
        activeDiagram.getForeignKeys().add(factory.foreignKey(orders.getId(), userId.getId(), users.getId(), usersId.getId()));
        selectedTableId = users.getId();
        selectedColumnId = null;
        selectedForeignKeyId = null;
        selectedParticipantId = null;
        selectedMessageId = null;
        layout.layout(activeDiagram);
    }

    private ToolBar topBar() {
        Button newDb = new Button("New DB");
        newDb.setOnAction(e -> run(new AddDiagramCommand(project, new Diagram(factoryId("diagram"), "Database diagram", DiagramType.DATABASE))));
        Button open = new Button("Open");
        open.setOnAction(e -> openProject());
        Button save = new Button("Save");
        save.setOnAction(e -> saveProject());
        Button export = new Button("Export SVG");
        export.setOnAction(e -> exportSvg());
        Button fit = new Button("Fit");
        fit.setOnAction(e -> fitDiagramToCanvas());
        Button actual = new Button("100%");
        actual.setOnAction(e -> actualSizeCanvas());
        Button zoomOut = new Button("−");
        zoomOut.setOnAction(e -> zoomCanvasBy(1.0 / 1.15));
        Button zoomIn = new Button("+");
        zoomIn.setOnAction(e -> zoomCanvasBy(1.15));
        Button help = new Button("F1 Help");
        help.setOnAction(e -> showShortcuts());
        projectTitle = new Label();
        dirtyState = new Label();
        topMenu = new ToolBar(projectTitle, dirtyState, new Separator(), newDb, open, save, export, new Separator(), fit, actual, zoomOut, zoomIn, new Separator(), help);
        updateDocumentChrome();
        topMenu.setFocusTraversable(true);
        applyFocusIndicator(topMenu, "-fx-background-color: " + PANEL_BG + "; -fx-border-color: " + BORDER + "; -fx-border-width: 0 0 1 0;");
        return topMenu;
    }

    private SplitPane projectTree() {
        diagramList = new ListView<>();
        diagramList.getItems().setAll(project.getDiagrams());
        diagramList.setCellFactory(v -> namedCell(Diagram::getName));
        diagramList.getSelectionModel().select(activeDiagram);
        diagramList.getSelectionModel().selectedItemProperty().addListener((obs, old, next) -> { if (next != null) { activeDiagram = next; selectedTableId = firstTableId().orElse(null); selectedColumnId = null; selectedParticipantId = firstParticipantId().orElse(null); selectedMessageId = null; relationPin.clear(); redraw(); }});

        tableList = new ListView<>();
        tableList.setCellFactory(v -> namedCell(DbTable::getName));
        tableList.getSelectionModel().selectedItemProperty().addListener((obs, old, next) -> { if (next != null) { selectedParticipantId = null; selectedMessageId = null; selectedTableId = next.getId(); selectedColumnId = null; redraw(); }});

        VBox box = new VBox(6, new Label("Diagrams"), diagramList, new Label("Tables"), tableList);
        box.setPadding(new Insets(10));
        box.setPrefWidth(ApplicationLayoutConstraints.LEFT_PANEL_PREF_WIDTH - 30);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setStyle("-fx-background-color: " + PANEL_BG + ";");
        ApplicationLayoutConstraints.fillVertically(diagramList);
        ApplicationLayoutConstraints.fillVertically(tableList);
        SplitPane pane = new SplitPane(box);
        ApplicationLayoutConstraints.configureLeftPanel(pane);
        pane.setStyle("-fx-background-color: " + PANEL_BG + ";");
        pane.setFocusTraversable(true);
        applyFocusIndicator(pane, "-fx-background-color: " + PANEL_BG + ";");
        leftMenu = pane;
        return pane;
    }

    private <T> ListCell<T> namedCell(java.util.function.Function<T, String> name) {
        return new ListCell<>() { @Override protected void updateItem(T item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? null : name.apply(item)); }};
    }

    private StackPane canvasPane() {
        canvas = new Canvas(780, 620);
        canvas.setFocusTraversable(true);
        canvas.focusedProperty().addListener((obs, old, focused) -> redraw());
        canvas.setOnMouseClicked(e -> {
            canvas.requestFocus();
            if (!draggingMoved) selectElementAt(e.getX(), e.getY());
        });
        canvas.setOnScroll(e -> {
            if (activeDiagram == null) return;
            if (e.isControlDown()) {
                zoomCanvasBy(e.getDeltaY() > 0 ? 1.10 : 1.0 / 1.10);
            } else {
                panCanvasByPixels(-e.getDeltaX(), -e.getDeltaY());
            }
            e.consume();
        });
        canvas.setOnMousePressed(e -> {
            canvas.requestFocus();
            draggingMoved = false;
            lastPanSceneX = e.getSceneX();
            lastPanSceneY = e.getSceneY();
            if (activeDiagram == null || e.getButton() != MouseButton.PRIMARY) return;
            double modelX = modelX(e.getX());
            double modelY = modelY(e.getY());
            hitDraggableElement(modelX, modelY).ifPresent(elementId -> {
                Rect rect = activeDiagram.getCanvasState().getBoundsByElementId().get(elementId);
                draggingElementId = elementId;
                draggingOldRect = rect;
                draggingOffsetX = modelX - rect.x();
                draggingOffsetY = modelY - rect.y();
                selectDraggedElement(elementId);
                e.consume();
            });
        });
        canvas.setOnMouseDragged(e -> {
            if (activeDiagram == null) return;
            if (draggingElementId != null) {
                Rect old = activeDiagram.getCanvasState().getBoundsByElementId().get(draggingElementId);
                if (old == null) return;
                Rect next = new Rect(modelX(e.getX()) - draggingOffsetX, modelY(e.getY()) - draggingOffsetY, old.width(), old.height());
                activeDiagram.getCanvasState().getBoundsByElementId().put(draggingElementId, next);
                draggingMoved = true;
                context.setText("Dragging: " + elementName(draggingElementId));
                redraw();
            } else {
                panCanvasByPixels(lastPanSceneX - e.getSceneX(), lastPanSceneY - e.getSceneY());
                lastPanSceneX = e.getSceneX();
                lastPanSceneY = e.getSceneY();
            }
            e.consume();
        });
        canvas.setOnMouseReleased(e -> {
            if (draggingElementId == null) return;
            String elementId = draggingElementId;
            Rect oldRect = draggingOldRect;
            Rect newRect = activeDiagram.getCanvasState().getBoundsByElementId().get(elementId);
            draggingElementId = null;
            draggingOldRect = null;
            if (draggingMoved && oldRect != null && newRect != null && !oldRect.equals(newRect)) {
                runMutating(new MoveCanvasElementCommand(activeDiagram.getCanvasState(), elementId, oldRect, newRect));
                context.setText("Moved: " + elementName(elementId));
                redraw();
            }
            e.consume();
        });

        canvasHintTitle = new Label();
        canvasHintTitle.setTextFill(Color.web(TEXT_PRIMARY));
        canvasHintTitle.setFont(uiHeadingFont(12));
        canvasHintBody = new Label();
        canvasHintBody.setTextFill(Color.web(TEXT_SECONDARY));
        canvasHintBody.setFont(uiFont(11));
        canvasHintBody.setWrapText(true);
        canvasHintFooter = new VBox(2, canvasHintTitle, canvasHintBody);
        canvasHintFooter.setPadding(new Insets(6, 10, 6, 10));
        canvasHintFooter.setMaxWidth(Double.MAX_VALUE);
        canvasHintFooter.setMinHeight(DESIGN_FOOTER_OVERLAY_HEIGHT);
        canvasHintFooter.setPrefHeight(DESIGN_FOOTER_OVERLAY_HEIGHT);
        canvasHintFooter.setMaxHeight(DESIGN_FOOTER_OVERLAY_HEIGHT);
        Rectangle hintClip = new Rectangle();
        hintClip.widthProperty().bind(canvasHintFooter.widthProperty());
        hintClip.setHeight(DESIGN_FOOTER_OVERLAY_HEIGHT);
        canvasHintFooter.setClip(hintClip);
        canvasHintFooter.setStyle("-fx-background-color: rgba(37, 42, 50, 0.96); -fx-border-color: " + BORDER + "; -fx-border-width: 1 0 0 0;");
        canvasHintFooter.setVisible(false);
        canvasHintFooter.setManaged(false);
        canvasHintFooter.setMouseTransparent(true);

        StackPane pane = new StackPane(canvas, canvasHintFooter);
        canvasStack = pane;
        StackPane.setAlignment(canvasHintFooter, Pos.BOTTOM_CENTER);
        pane.setPadding(new Insets(16));
        pane.setStyle("-fx-background-color: " + CANVAS_BG + ";");
        ApplicationLayoutConstraints.configureCanvasArea(pane);
        return pane;
    }

    private VBox inspectorPane() {
        inspectorFields = new VBox(5);
        inspectorFields.setPadding(new Insets(4, 0, 4, 0));
        inspector = new ScrollPane(inspectorFields);
        inspector.setFitToWidth(true);
        inspector.setPrefWidth(ApplicationLayoutConstraints.INSPECTOR_PREF_WIDTH - 24);
        ApplicationLayoutConstraints.fillVertically(inspector);
        VBox box = new VBox(6, new Label("Inspector"), inspector);
        box.setPadding(new Insets(8));
        box.setStyle("-fx-background-color: " + PANEL_BG + ";");
        ApplicationLayoutConstraints.configureInspector(box);
        box.setFocusTraversable(true);
        applyFocusIndicator(box, "-fx-background-color: " + PANEL_BG + ";");
        inspectorPanel = box;
        return box;
    }

    private VBox footer() {
        context = new Label("Ready");
        status = new Label(statusHintText());
        VBox footer = new VBox(2, context, status);
        footer.setPadding(new Insets(6, 10, 8, 10));
        footer.setStyle("-fx-background-color: " + PANEL_BG + "; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-border-color: " + BORDER + "; -fx-border-width: 1 0 0 0;");
        context.setTextFill(Color.web(TEXT_PRIMARY));
        status.setTextFill(Color.web(TEXT_SECONDARY));
        return footer;
    }

    private static boolean isKeyLogEnabled() {
        String property = System.getProperty(KEY_LOG_PROPERTY);
        if (property != null) return Boolean.parseBoolean(property);
        String env = System.getenv(KEY_LOG_ENV);
        return env != null && (env.equalsIgnoreCase("true") || env.equals("1") || env.equalsIgnoreCase("yes"));
    }

    private String statusHintText() {
        String base = "0 top menu · 1 left menu · 2 canvas · 3 inspector · Tab within panels · Ctrl+K/Ctrl+Shift+P palette · Ctrl+N new · Ctrl+± zoom · Ctrl+0 fit · Ctrl+1 100% · Ctrl+Arrows pan · Space commands";
        return keyLogEnabled ? base + " · F12 key log · F1 help" : base + " · F1 help";
    }

    private void registerCommands() {
        registerCommand(CommandMetadata.of("project.new", "New project", "Ctrl+N", "new"), this::newProject);
        registerCommand(CommandMetadata.of("project.open", "Open project", "Ctrl+O", "open"), this::openProject);
        registerCommand(CommandMetadata.of("project.save", "Save project", "Ctrl+S", "save"), () -> saveProject());
        registerCommand(CommandMetadata.of("diagram.database.new", "New database diagram", "", "database", "diagram"), () -> addDiagram(DiagramType.DATABASE));
        registerCommand(CommandMetadata.of("diagram.sequence.new", "New sequence diagram", "", "sequence", "diagram"), () -> addDiagram(DiagramType.SEQUENCE));
        registerCommand(CommandMetadata.of("db.table.add", "Add table", "Space A T", "table"), this::addTableFromKeyboard);
        registerCommand(CommandMetadata.of("db.column.add", "Add column", "Space A C", "column"), this::addColumnFromKeyboard);
        registerCommand(CommandMetadata.of("sequence.participant.add", "Add participant", "Space A P", "sequence", "participant"), this::addSequenceParticipantFromKeyboard);
        registerCommand(CommandMetadata.of("sequence.message.add", "Add message", "Space A M", "sequence", "message"), this::addSequenceMessageFromKeyboard);
        registerCommand(CommandMetadata.of("db.fk.add", "Add foreign key", "Space A F", "foreign", "relation"), this::addForeignKeyFromSelection);
        registerCommand(CommandMetadata.of("db.joinTable.add", "Add join table", "Space A J", "join", "many-to-many"), this::addJoinTableFromSelection);
        registerCommand(CommandMetadata.of("db.relation.pin", "Pin relation target", "Space P", "pin", "relation"), this::pinSelectedRelationTarget);
        registerCommand(CommandMetadata.of("db.relation.clearPin", "Clear relation pin", "Space U", "unpin", "relation"), this::clearRelationPin);
        registerCommand(CommandMetadata.of("element.rename", "Rename selected", "Space E", "rename", "edit"), this::renameSelected);
        registerCommand(CommandMetadata.of("element.edit", "Edit selected", "Space E", "edit", "rename"), this::renameSelected);
        registerCommand(CommandMetadata.of("element.deleteSelected", "Delete selected", "Delete", "delete", "remove"), this::deleteSelected);
        registerCommand(CommandMetadata.of("element.find", "Find element", "Ctrl+F", "find", "search"), this::findElement);
        registerCommand(CommandMetadata.of("export.svg", "Export SVG", "", "svg", "export"), this::exportSvg);
        registerCommand(CommandMetadata.of("help.shortcuts", "Show shortcuts", "F1", "shortcuts", "help"), this::showShortcuts);
        registerCommand(CommandMetadata.of("undo", "Undo", "Ctrl+Z", "undo"), this::undoCommand);
        registerCommand(CommandMetadata.of("redo", "Redo", "Ctrl+Shift+Z / Ctrl+Y", "redo"), this::redoCommand);
        registerCommand(CommandMetadata.of("canvas.zoom.in", "Zoom in", "Ctrl+Plus", "zoom", "in"), () -> zoomCanvasBy(1.15));
        registerCommand(CommandMetadata.of("canvas.zoom.out", "Zoom out", "Ctrl+Minus", "zoom", "out"), () -> zoomCanvasBy(1.0 / 1.15));
        registerCommand(CommandMetadata.of("canvas.fit", "Fit whole diagram", "Ctrl+0", "fit", "zoom"), this::fitDiagramToCanvas);
        registerCommand(CommandMetadata.of("canvas.actual-size", "Actual size", "Ctrl+1", "100%", "zoom"), this::actualSizeCanvas);
    }

    private void registerCommand(CommandMetadata metadata, Runnable action) {
        commandRouter.register(metadata, action);
    }

    private void handleShortcut(KeyEvent event) {
        if (keyLogEnabled && event.getCode() == KeyCode.F12) { showKeyLogWindow(); event.consume(); return; }
        if (handleDirectFocusShortcut(event)) { event.consume(); return; }
        if (event.getCode() == KeyCode.F1) { toggleShortcuts(); event.consume(); return; }
        if (spaceCommandMode && event.getCode() == KeyCode.SPACE) { cancelSpaceCommand("Space command hidden"); event.consume(); return; }
        if (spaceCommandMode) { handleSpaceCommandKey(event); event.consume(); return; }
        if (handleFkPreviewKey(event)) { event.consume(); return; }
        if (handleViewportShortcut(event)) { event.consume(); return; }
        if (handleFunctionalAreaTab(event)) { event.consume(); return; }
        if (handleCanvasNavigation(event)) { event.consume(); return; }
        if (event.isControlDown() && event.getCode() == KeyCode.S) { saveProject(); event.consume(); return; }
        if (event.isControlDown() && event.getCode() == KeyCode.O) { openProject(); event.consume(); return; }
        if (event.isControlDown() && event.getCode() == KeyCode.F) { findElement(); event.consume(); return; }
        if (event.getCode() == KeyCode.DELETE) { deleteSelected(); event.consume(); return; }
        if (event.isControlDown() && event.getCode() == KeyCode.Z) { undoCommand(); event.consume(); return; }
        if ((event.isControlDown() && event.getCode() == KeyCode.Y) || (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.Z)) { redoCommand(); event.consume(); return; }
        if (event.isControlDown() && event.getCode() == KeyCode.K) { showPalette(); event.consume(); return; }
        if (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.P) { showPalette(); event.consume(); return; }
        if (event.isControlDown() && event.getCode() == KeyCode.N) { showPalette("new"); event.consume(); return; }
        if (event.getCode() == KeyCode.SPACE && canvas.isFocused()) { toggleSpaceCommands(); event.consume(); }
    }

    private boolean handleCanvasNavigation(KeyEvent event) {
        if (canvas == null || !canvas.isFocused() || activeDiagram == null) return false;
        if (event.isControlDown() || event.isAltDown() || event.isMetaDown() || event.isShiftDown()) return false;
        if (activeDiagram.getType() == DiagramType.SEQUENCE) return handleSequenceNavigation(event);
        if (activeDiagram.getType() != DiagramType.DATABASE || activeDiagram.getTables().isEmpty()) return false;
        layout.layout(activeDiagram);
        return switch (event.getCode()) {
            case HOME -> { selectTable(activeDiagram.getTables().getFirst().getId(), "Selected first table"); yield true; }
            case END -> { selectTable(activeDiagram.getTables().getLast().getId(), "Selected last table"); yield true; }
            case LEFT -> { selectGeometricTable(Direction.LEFT); yield true; }
            case RIGHT -> { selectGeometricTable(Direction.RIGHT); yield true; }
            case UP -> { if (isColumnDepth()) selectAdjacentColumn(-1); else selectGeometricTable(Direction.UP); yield true; }
            case DOWN -> { if (isColumnDepth()) selectAdjacentColumn(1); else selectGeometricTable(Direction.DOWN); yield true; }
            case ENTER -> { enterColumnDepth(); yield true; }
            case ESCAPE -> { if (isColumnDepth()) { exitColumnDepth(); yield true; } yield false; }
            default -> false;
        };
    }

    private boolean isColumnDepth() {
        return selectedColumnId != null;
    }

    private boolean handleSequenceNavigation(KeyEvent event) {
        if (activeDiagram.getParticipants().isEmpty()) return false;
        return switch (event.getCode()) {
            case HOME -> { selectParticipant(activeDiagram.getParticipants().getFirst().getId(), "Selected first participant"); yield true; }
            case END -> { selectParticipant(activeDiagram.getParticipants().getLast().getId(), "Selected last participant"); yield true; }
            case LEFT -> { selectAdjacentParticipant(-1); yield true; }
            case RIGHT -> { selectAdjacentParticipant(1); yield true; }
            case UP -> { selectAdjacentMessage(-1); yield true; }
            case DOWN -> { selectAdjacentMessage(1); yield true; }
            case ESCAPE -> { selectedMessageId = null; context.setText("Sequence participant depth"); redraw(); yield true; }
            default -> false;
        };
    }

    private enum Direction { LEFT, RIGHT, UP, DOWN }

    private boolean handleViewportShortcut(KeyEvent event) {
        if (!event.isControlDown() || event.isAltDown() || event.isMetaDown()) return false;
        return switch (event.getCode()) {
            case PLUS, EQUALS, ADD -> { zoomCanvasBy(1.15); yield true; }
            case MINUS, SUBTRACT -> { zoomCanvasBy(1.0 / 1.15); yield true; }
            case DIGIT0, NUMPAD0 -> { fitDiagramToCanvas(); yield true; }
            case DIGIT1, NUMPAD1 -> { actualSizeCanvas(); yield true; }
            case LEFT -> { panCanvasByPixels(-80, 0); yield true; }
            case RIGHT -> { panCanvasByPixels(80, 0); yield true; }
            case UP -> { panCanvasByPixels(0, -80); yield true; }
            case DOWN -> { panCanvasByPixels(0, 80); yield true; }
            default -> false;
        };
    }

    private boolean handleFunctionalAreaTab(KeyEvent event) {
        if (event.getCode() != KeyCode.TAB) return false;
        if (event.isControlDown() || event.isAltDown() || event.isMetaDown()) return false;
        Node owner = stage == null || stage.getScene() == null ? null : stage.getScene().getFocusOwner();
        if (owner == null) return false;
        Node area = functionalAreaFor(owner);
        if (area == null) return false;
        List<Node> focusable = FocusAreaNavigator.focusableNodes(area);
        if (focusable.isEmpty()) return false;
        int current = focusable.indexOf(owner);
        if (current < 0) current = event.isShiftDown() ? 0 : -1;
        int next = event.isShiftDown()
                ? Math.floorMod(current - 1, focusable.size())
                : Math.floorMod(current + 1, focusable.size());
        focusable.get(next).requestFocus();
        return true;
    }

    private Node functionalAreaFor(Node node) {
        for (Node area : List.of(topMenu, leftMenu, canvas, inspectorPanel)) {
            if (isDescendantOrSelf(area, node)) return area;
        }
        return null;
    }

    private boolean isDescendantOrSelf(Node ancestor, Node node) {
        for (Node current = node; current != null; current = current.getParent()) {
            if (current == ancestor) return true;
        }
        return false;
    }

    private boolean handleDirectFocusShortcut(KeyEvent event) {
        if (isTextInputEvent(event)) return false;
        if (event.isControlDown() || event.isAltDown() || event.isMetaDown() || event.isShiftDown()) return false;
        return switch (event.getCode()) {
            case DIGIT0, NUMPAD0 -> { focusArea(topMenu, "верхнее меню"); yield true; }
            case DIGIT1, NUMPAD1 -> { focusArea(leftMenu, "левое меню"); yield true; }
            case DIGIT2, NUMPAD2 -> { focusArea(canvas, "центральная область"); yield true; }
            case DIGIT3, NUMPAD3 -> { focusArea(inspectorPanel, "инспектор"); yield true; }
            default -> false;
        };
    }

    private void selectElementAt(double canvasX, double canvasY) {
        if (activeDiagram == null) return;
        canvasHitTestPresenter.hitAt(activeDiagram, canvasX, canvasY).ifPresent(hit -> {
            if (hit instanceof CanvasHitTestPresenter.TableHit table) {
                selectTable(table.elementId(), "Selected table: " + table.name());
            } else if (hit instanceof CanvasHitTestPresenter.ForeignKeyHit foreignKey) {
                selectForeignKey(foreignKey.elementId(), "Selected FK: " + foreignKey.elementId());
            } else if (hit instanceof CanvasHitTestPresenter.MessageHit message) {
                selectMessage(activeDiagram.getMessages().stream()
                        .filter(candidate -> candidate.getId().equals(message.elementId()))
                        .findFirst()
                        .orElseThrow(), "Selected message: " + message.label());
            } else if (hit instanceof CanvasHitTestPresenter.ParticipantHit participant) {
                selectParticipant(participant.elementId(), "Selected participant: " + participant.name());
            }
        });
    }

    private Optional<String> hitDraggableElement(double modelX, double modelY) {
        return canvasHitTestPresenter.draggableElementAtModel(activeDiagram, modelX, modelY);
    }

    private double sequenceMessageY(int index) {
        return 150 + index * 72.0;
    }

    private double modelX(double canvasX) {
        CanvasState state = activeDiagram.getCanvasState();
        return canvasX / state.getZoom() + state.getViewportX();
    }

    private double modelY(double canvasY) {
        CanvasState state = activeDiagram.getCanvasState();
        return canvasY / state.getZoom() + state.getViewportY();
    }

    private void selectDraggedElement(String elementId) {
        if (activeDiagram.getType() == DiagramType.DATABASE) {
            selectedParticipantId = null;
            selectedMessageId = null;
            selectedTableId = elementId;
            selectedColumnId = null;
            selectedForeignKeyId = null;
            if (tableList != null) activeDiagram.findTable(elementId).ifPresent(table -> tableList.getSelectionModel().select(table));
        } else if (activeDiagram.getType() == DiagramType.SEQUENCE) {
            selectedTableId = null;
            selectedColumnId = null;
            selectedForeignKeyId = null;
            selectedParticipantId = elementId;
            selectedMessageId = null;
        }
    }

    private String elementName(String elementId) {
        return activeDiagram.findTable(elementId).map(DbTable::getName)
                .or(() -> activeDiagram.getParticipants().stream().filter(participant -> participant.getId().equals(elementId)).map(SequenceParticipant::getName).findFirst())
                .orElse(elementId);
    }

    private void selectTable(String tableId, String message) {
        selectedParticipantId = null;
        selectedMessageId = null;
        selectedTableId = tableId;
        selectedColumnId = null;
        selectedForeignKeyId = null;
        if (tableList != null) activeDiagram.findTable(tableId).ifPresent(table -> tableList.getSelectionModel().select(table));
        context.setText(message);
        redraw();
    }

    private void selectForeignKey(String foreignKeyId, String message) {
        selectedParticipantId = null;
        selectedMessageId = null;
        selectedTableId = null;
        selectedColumnId = null;
        selectedForeignKeyId = foreignKeyId;
        context.setText(message);
        redraw();
    }

    private void selectParticipant(String participantId, String message) {
        selectedTableId = null;
        selectedColumnId = null;
        selectedForeignKeyId = null;
        selectedParticipantId = participantId;
        selectedMessageId = null;
        context.setText(message);
        redraw();
    }

    private void selectMessage(SequenceMessage sequenceMessage, String message) {
        selectedTableId = null;
        selectedColumnId = null;
        selectedForeignKeyId = null;
        selectedParticipantId = sequenceMessage.getFromParticipantId();
        selectedMessageId = sequenceMessage.getId();
        context.setText(message);
        redraw();
    }

    private void selectGeometricTable(Direction direction) {
        String baseId = selectedTableId == null ? firstTableId().orElse(null) : selectedTableId;
        if (baseId == null) return;
        Rect base = activeDiagram.getCanvasState().getBoundsByElementId().get(baseId);
        if (base == null) return;
        double bx = base.center().x();
        double by = base.center().y();
        activeDiagram.getTables().stream()
                .filter(table -> !table.getId().equals(baseId))
                .map(table -> new java.util.AbstractMap.SimpleEntry<>(table, activeDiagram.getCanvasState().getBoundsByElementId().get(table.getId())))
                .filter(entry -> entry.getValue() != null && isInDirection(direction, bx, by, entry.getValue().center().x(), entry.getValue().center().y()))
                .min(Comparator.comparingDouble(entry -> directionalDistance(direction, bx, by, entry.getValue().center().x(), entry.getValue().center().y())))
                .ifPresentOrElse(entry -> selectTable(entry.getKey().getId(), "Selected table: " + entry.getKey().getName()),
                        () -> context.setText("No table " + direction.name().toLowerCase() + " of selection"));
    }

    private boolean isInDirection(Direction direction, double bx, double by, double x, double y) {
        return switch (direction) {
            case LEFT -> x < bx;
            case RIGHT -> x > bx;
            case UP -> y < by;
            case DOWN -> y > by;
        };
    }

    private double directionalDistance(Direction direction, double bx, double by, double x, double y) {
        double primary = switch (direction) {
            case LEFT, RIGHT -> Math.abs(x - bx);
            case UP, DOWN -> Math.abs(y - by);
        };
        double secondary = switch (direction) {
            case LEFT, RIGHT -> Math.abs(y - by);
            case UP, DOWN -> Math.abs(x - bx);
        };
        return primary * 10_000 + secondary;
    }

    private void enterColumnDepth() {
        Optional<DbTable> selected = selectedTableId == null ? Optional.empty() : activeDiagram.findTable(selectedTableId);
        if (selected.isEmpty() || selected.get().getColumns().isEmpty()) {
            context.setText("Selected table has no columns");
            return;
        }
        selectedColumnId = selected.get().getColumns().getFirst().getId();
        context.setText("Column depth: " + selected.get().getName() + "." + selected.get().getColumns().getFirst().getName());
        redraw();
    }

    private void selectAdjacentColumn(int delta) {
        Optional<DbTable> selected = selectedTableId == null ? Optional.empty() : activeDiagram.findTable(selectedTableId);
        if (selected.isEmpty() || selected.get().getColumns().isEmpty()) return;
        List<DbColumn> columns = selected.get().getColumns();
        int index = 0;
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getId().equals(selectedColumnId)) { index = i; break; }
        }
        int next = Math.max(0, Math.min(columns.size() - 1, index + delta));
        selectedColumnId = columns.get(next).getId();
        context.setText("Column depth: " + selected.get().getName() + "." + columns.get(next).getName());
        redraw();
    }

    private void exitColumnDepth() {
        Optional<DbTable> selected = selectedTableId == null ? Optional.empty() : activeDiagram.findTable(selectedTableId);
        selectedColumnId = null;
        context.setText(selected.map(table -> "Table depth: " + table.getName()).orElse("Table depth"));
        redraw();
    }

    private void selectAdjacentParticipant(int delta) {
        List<SequenceParticipant> participants = activeDiagram.getParticipants();
        if (participants.isEmpty()) return;
        int index = 0;
        for (int i = 0; i < participants.size(); i++) {
            if (participants.get(i).getId().equals(selectedParticipantId)) { index = i; break; }
        }
        int next = Math.max(0, Math.min(participants.size() - 1, index + delta));
        SequenceParticipant participant = participants.get(next);
        selectParticipant(participant.getId(), "Selected participant: " + participant.getName());
    }

    private void selectAdjacentMessage(int delta) {
        List<SequenceMessage> messages = activeDiagram.getMessages();
        if (messages.isEmpty()) {
            context.setText("Sequence has no messages");
            return;
        }
        int index = delta > 0 ? -1 : messages.size();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(selectedMessageId)) { index = i; break; }
        }
        int next = Math.max(0, Math.min(messages.size() - 1, index + delta));
        SequenceMessage message = messages.get(next);
        selectMessage(message, "Selected message: " + message.getLabel());
    }

    private void focusArea(Node node, String title) {
        if (node == null) return;
        FocusAreaNavigator.firstFocusableInArea(node).orElse(node).requestFocus();
        if (node == canvas) selectFirstCanvasObjectOnAreaSwitch();
        context.setText("Focus: " + title);
    }

    private void selectFirstCanvasObjectOnAreaSwitch() {
        if (activeDiagram == null) return;
        if (activeDiagram.getType() == DiagramType.SEQUENCE) {
            selectedTableId = null;
            selectedColumnId = null;
            selectedForeignKeyId = null;
            activeDiagram.getParticipants().stream().findFirst().ifPresentOrElse(
                    participant -> {
                        selectedParticipantId = participant.getId();
                        selectedMessageId = null;
                    },
                    () -> {
                        selectedParticipantId = null;
                        selectedMessageId = null;
                    });
        } else {
            selectedParticipantId = null;
            selectedMessageId = null;
            selectedForeignKeyId = null;
            selectedColumnId = null;
            selectedTableId = firstTableId().orElse(null);
            if (tableList != null) {
                if (selectedTableId == null) tableList.getSelectionModel().clearSelection();
                else activeDiagram.findTable(selectedTableId).ifPresent(table -> tableList.getSelectionModel().select(table));
            }
        }
        redraw();
    }

    private boolean isTextInputEvent(KeyEvent event) {
        return event.getTarget() instanceof TextInputControl;
    }

    private void applyFocusIndicator(Region region, String baseStyle) {
        region.focusedProperty().addListener((obs, old, focused) -> region.setStyle(focused ? baseStyle + FOCUS_RING : baseStyle));
        region.setStyle(baseStyle);
    }

    private void toggleSpaceCommands() {
        if (isCanvasHintVisible() && "space".equals(canvasHintKind)) {
            cancelSpaceCommand("Space command hidden");
            return;
        }
        beginSpaceCommandMode();
    }

    private void beginSpaceCommandMode() {
        spaceCommandMode = true;
        spaceCommandBuffer.setLength(0);
        context.setText("Space command mode");
        showCanvasHint("space", "Space commands", spaceCommandHintText(""));
    }

    private void handleSpaceCommandKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            cancelSpaceCommand("Space command cancelled");
            return;
        }
        if (!event.getCode().isLetterKey()) return;
        if (!spaceCommandBuffer.isEmpty()) spaceCommandBuffer.append(' ');
        spaceCommandBuffer.append(event.getCode().getName().toUpperCase());
        String command = spaceCommandBuffer.toString();
        if (executeSpaceCommand(command)) {
            spaceCommandMode = false;
            spaceCommandBuffer.setLength(0);
            return;
        }
        if (isSpaceCommandPrefix(command)) {
            context.setText("Space " + command + " …");
            showCanvasHint("space", "Space " + command + " …", spaceCommandHintText(command));
            return;
        }
        cancelSpaceCommand("Unknown Space command: " + command);
    }

    private boolean executeSpaceCommand(String command) {
        switch (command) {
            case "A T" -> { addTableFromKeyboard(); return true; }
            case "A C" -> { addColumnFromKeyboard(); return true; }
            case "A P" -> { addSequenceParticipantFromKeyboard(); return true; }
            case "A M" -> { addSequenceMessageFromKeyboard(); return true; }
            case "A F" -> { addForeignKeyFromSelection(); return true; }
            case "A J" -> { addJoinTableFromSelection(); return true; }
            case "U" -> { clearRelationPin(); return true; }
            case "D" -> { deleteSelected(); hideCanvasHint(); return true; }
            case "E" -> { renameSelected(); hideCanvasHint(); return true; }
            case "G S" -> { findElement(); hideCanvasHint(); return true; }
            case "P" -> { pinSelectedRelationTarget(); return true; }
            case "L D" -> { layout.resetLayout(activeDiagram); markDirty(); redraw(); context.setText("Diagram layout reset"); hideCanvasHint(); return true; }
            default -> { return false; }
        }
    }

    private boolean isSpaceCommandPrefix(String command) {
        return List.of("A", "G", "L").contains(command);
    }

    private void cancelSpaceCommand(String message) {
        spaceCommandMode = false;
        spaceCommandBuffer.setLength(0);
        context.setText(message);
        hideCanvasHint();
    }

    private void addTableFromKeyboard() {
        if (!requireDatabaseContext("Add table")) return;
        DbTable table = factory.table(nextTableName());
        DbColumn id = factory.column("id", "uuid");
        id.setPrimaryKey(true);
        id.setNullable(false);
        table.getColumns().add(id);
        runMutating(new AddTableCommand(activeDiagram, table));
        selectedTableId = table.getId();
        selectedColumnId = null;
        redraw();
        context.setText("Added table: " + table.getName());
        hideCanvasHint();
    }

    private String nextTableName() {
        int index = activeDiagram.getTables().size() + 1;
        while (true) {
            String candidate = "table_" + index;
            boolean exists = activeDiagram.getTables().stream().anyMatch(table -> table.getName().equals(candidate));
            if (!exists) return candidate;
            index++;
        }
    }

    private void addColumnFromKeyboard() {
        if (!requireDatabaseContext("Add column")) return;
        Optional<DbTable> selected = selectedTableId == null ? Optional.empty() : activeDiagram.findTable(selectedTableId);
        if (selected.isEmpty()) {
            context.setText("Select a table before Space A C");
            return;
        }
        DbTable table = selected.get();
        DbColumn column = factory.column(nextColumnName(table), "text");
        runMutating(new AddColumnCommand(table, column));
        redraw();
        context.setText("Added column: " + table.getName() + "." + column.getName());
        hideCanvasHint();
    }

    private String nextColumnName(DbTable table) {
        int index = table.getColumns().size() + 1;
        while (true) {
            String candidate = "column_" + index;
            boolean exists = table.getColumns().stream().anyMatch(column -> column.getName().equals(candidate));
            if (!exists) return candidate;
            index++;
        }
    }

    private boolean requireDatabaseContext(String commandName) {
        if (activeDiagram != null && activeDiagram.getType() == DiagramType.DATABASE) return true;
        context.setText(commandName + " is only available on database diagrams");
        hideCanvasHint();
        return false;
    }

    private void addSequenceParticipantFromKeyboard() {
        if (activeDiagram == null || activeDiagram.getType() != DiagramType.SEQUENCE) {
            context.setText("Switch to a sequence diagram before Space A P");
            return;
        }
        SequenceParticipant participant = new SequenceParticipant(factoryId("participant"), nextParticipantName());
        runMutating(new AddSequenceParticipantCommand(activeDiagram, participant));
        selectedTableId = null;
        selectedColumnId = null;
        selectedForeignKeyId = null;
        selectedParticipantId = participant.getId();
        selectedMessageId = null;
        redraw();
        context.setText("Added participant: " + participant.getName());
        hideCanvasHint();
    }

    private String nextParticipantName() {
        int index = activeDiagram.getParticipants().size() + 1;
        while (true) {
            String candidate = "Participant " + index;
            boolean exists = activeDiagram.getParticipants().stream().anyMatch(participant -> participant.getName().equals(candidate));
            if (!exists) return candidate;
            index++;
        }
    }

    private void addSequenceMessageFromKeyboard() {
        if (activeDiagram == null || activeDiagram.getType() != DiagramType.SEQUENCE) {
            context.setText("Switch to a sequence diagram before Space A M");
            return;
        }
        if (activeDiagram.getParticipants().size() < 2) {
            context.setText("Add at least two participants before Space A M");
            return;
        }
        String fromId = selectedParticipantId != null ? selectedParticipantId : activeDiagram.getParticipants().getFirst().getId();
        String toId = activeDiagram.getParticipants().stream()
                .map(SequenceParticipant::getId)
                .filter(id -> !id.equals(fromId))
                .findFirst()
                .orElse(activeDiagram.getParticipants().getFirst().getId());
        SequenceMessage message = new SequenceMessage(factoryId("message"), fromId, toId, nextMessageLabel());
        runMutating(new AddSequenceMessageCommand(activeDiagram, message));
        selectMessage(message, "Added message: " + message.getLabel());
        hideCanvasHint();
    }

    private String nextMessageLabel() {
        int index = activeDiagram.getMessages().size() + 1;
        while (true) {
            String candidate = "message " + index;
            boolean exists = activeDiagram.getMessages().stream().anyMatch(message -> message.getLabel().equals(candidate));
            if (!exists) return candidate;
            index++;
        }
    }

    private void pinSelectedRelationTarget() {
        if (!requireDatabaseContext("Pin relation target")) return;
        if (selectedTableId == null) {
            context.setText("Select a table before Space P");
            return;
        }
        if (selectedColumnId != null) relationPin.pin(selectedTableId, selectedColumnId);
        else relationPin.pin(selectedTableId);
        context.setText(relationPinContextLine());
        redraw();
        hideCanvasHint();
    }

    private void clearRelationPin() {
        if (!requireDatabaseContext("Clear relation pin")) return;
        if (relationPin.isPinned()) {
            relationPin.clear();
            context.setText("Relation pin cleared");
        } else {
            context.setText("No relation pin to clear");
        }
        redraw();
        hideCanvasHint();
    }

    private void logKeyPress(KeyEvent event) {
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

    private void showKeyLogWindow() {
        if (keyLogStage == null) {
            keyLogArea = new TextArea();
            keyLogArea.setEditable(false);
            keyLogArea.setWrapText(false);
            keyLogArea.setFont(Font.font("Monospaced", 12));
            keyLogArea.setText("Key press log. Press keys in the main window; F12 shows this window.\n");

            Button clear = new Button("Clear");
            clear.setOnAction(e -> {
                keyLogCounter = 0;
                keyLogArea.setText("Key press log cleared.\n");
            });

            BorderPane root = new BorderPane(keyLogArea);
            root.setTop(new ToolBar(new Label("SchemeOnYou key log"), new Separator(), clear));
            Scene scene = new Scene(root, 680, 420);
            scene.addEventFilter(KeyEvent.KEY_PRESSED, this::logKeyPress);
            scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
                if (e.getCode() == KeyCode.F12) {
                    stage.requestFocus();
                    e.consume();
                }
            });

            keyLogStage = new Stage();
            keyLogStage.setTitle("SchemeOnYou — Key Log");
            keyLogStage.initOwner(stage);
            keyLogStage.setScene(scene);
            keyLogStage.setOnCloseRequest(e -> {
                keyLogStage.hide();
                e.consume();
            });
        }
        keyLogStage.show();
        keyLogStage.toFront();
        context.setText("Key log window opened");
    }

    private void showPalette() {
        showPalette("");
    }

    private void showPalette(String initialQuery) {
        ensureCommandPaletteOverlay();
        commandPaletteSearch.setText(initialQuery == null ? "" : initialQuery);
        refreshCommandPaletteResults();
        if (!canvasStack.getChildren().contains(commandPaletteOverlay)) {
            canvasStack.getChildren().add(commandPaletteOverlay);
            StackPane.setAlignment(commandPaletteOverlay, Pos.TOP_CENTER);
            StackPane.setMargin(commandPaletteOverlay, new Insets(28, 0, 0, 0));
        }
        commandPaletteOverlay.setVisible(true);
        commandPaletteOverlay.setManaged(true);
        context.setText("Command palette opened — Enter to run, Esc to close");
        Platform.runLater(() -> {
            commandPaletteSearch.requestFocus();
            commandPaletteSearch.selectAll();
        });
    }

    private void ensureCommandPaletteOverlay() {
        if (commandPaletteOverlay != null) return;

        Label title = new Label("Command palette");
        title.setTextFill(Color.web(TEXT_PRIMARY));
        title.setFont(uiHeadingFont(13));
        title.getStyleClass().add("section-title");

        commandPaletteSearch = new TextField();
        commandPaletteSearch.setPromptText("Command, shortcut, or keyword");
        commandPaletteSearch.textProperty().addListener((obs, old, next) -> refreshCommandPaletteResults());

        commandPaletteResults = new ListView<>();
        commandPaletteResults.setPrefHeight(260);
        commandPaletteResults.setCellFactory(v -> new ListCell<>() {
            @Override protected void updateItem(CommandMetadata item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : commandLabel(item));
            }
        });
        commandPaletteResults.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) runSelectedPaletteCommand();
        });

        commandPaletteOverlay = new VBox(8, title, commandPaletteSearch, commandPaletteResults);
        commandPaletteOverlay.setPrefSize(560, 340);
        commandPaletteOverlay.setMaxSize(560, 340);
        commandPaletteOverlay.setPadding(new Insets(12));
        commandPaletteOverlay.setStyle("-fx-background-color: " + PANEL_ELEVATED + "; -fx-border-color: " + SELECTION + "; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");
        commandPaletteOverlay.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                hideCommandPalette("Command palette closed");
                e.consume();
            } else if (e.getCode() == KeyCode.ENTER) {
                runSelectedPaletteCommand();
                e.consume();
            }
        });
    }

    private void refreshCommandPaletteResults() {
        if (commandPaletteSearch == null || commandPaletteResults == null) return;
        commandPaletteResults.getItems().setAll(commandRouter.search(commandPaletteSearch.getText(), activeDiagramType()));
        if (!commandPaletteResults.getItems().isEmpty()) {
            commandPaletteResults.getSelectionModel().selectFirst();
        }
    }

    private void runSelectedPaletteCommand() {
        CommandMetadata selected = commandPaletteResults.getSelectionModel().getSelectedItem();
        if (selected == null) {
            context.setText("No command selected");
            return;
        }
        hideCommandPalette(null);
        executePaletteCommand(selected);
    }

    private void hideCommandPalette(String message) {
        if (commandPaletteOverlay == null) return;
        commandPaletteOverlay.setVisible(false);
        commandPaletteOverlay.setManaged(false);
        canvas.requestFocus();
        if (message != null) context.setText(message);
    }

    private String commandLabel(CommandMetadata command) {
        return command.shortcut().isBlank() ? command.title() : command.title() + " — " + command.shortcut();
    }

    private void executePaletteCommand(CommandMetadata command) {
        if (!commandRouter.execute(command, activeDiagramType())) {
            context.setText("Command is not available in this diagram context: " + command.title());
        }
    }

    private DiagramType activeDiagramType() {
        return activeDiagram == null ? null : activeDiagram.getType();
    }

    private void showSpaceSheet() {
        showCanvasHint("space", "Canvas commands", spaceCommandHintText(""));
    }

    private void toggleShortcuts() {
        if (isCanvasHintVisible() && "shortcuts".equals(canvasHintKind)) {
            hideCanvasHint();
            context.setText("Shortcuts hidden");
            return;
        }
        showShortcuts();
    }

    private void showShortcuts() {
        spaceCommandMode = false;
        spaceCommandBuffer.setLength(0);
        StringBuilder body = new StringBuilder();
        shortcuts.asMap().forEach((k, v) -> body.append(k).append(" — ").append(v).append("   "));
        showCanvasHint("shortcuts", "Shortcuts", body.toString());
        canvas.requestFocus();
    }

    private String spaceCommandHintText(String prefix) {
        if ("A".equals(prefix)) return addCommandHintText();
        if ("G".equals(prefix)) return activeDiagramType() == DiagramType.SEQUENCE ? "G S — search   Esc — cancel" : "G T — go to table   G S — search   Esc — cancel";
        if ("L".equals(prefix)) return activeDiagramType() == DiagramType.SEQUENCE ? "L D — layout diagram   Esc — cancel" : "L D — layout diagram   L S — layout selection   Esc — cancel";
        StringBuilder body = new StringBuilder("Ctrl+±/0/1 — zoom/fit   Ctrl+Arrows — pan   Esc — cancel");
        body.append("\n");
        spaceSheet.entries(activeDiagramType()).forEach((k, v) -> body.append("Space ").append(k).append(" — ").append(v).append("   "));
        return body.toString();
    }

    private String addCommandHintText() {
        if (activeDiagramType() == DiagramType.SEQUENCE) return "A P — add participant   A M — add message   Esc — cancel";
        return "A T — add table   A C — add column   A F — add foreign key   A J — add join table   Esc — cancel";
    }

    private void showCanvasHint(String kind, String title, String body) {
        if (canvasHintFooter == null) return;
        canvasHintKind = kind;
        canvasHintTitle.setText(title);
        canvasHintBody.setText(body);
        canvasHintFooter.setManaged(true);
        canvasHintFooter.setVisible(true);
    }

    private boolean isCanvasHintVisible() {
        return canvasHintFooter != null && canvasHintFooter.isVisible();
    }

    private void hideCanvasHint() {
        if (canvasHintFooter == null) return;
        canvasHintFooter.setVisible(false);
        canvasHintFooter.setManaged(false);
        canvasHintKind = null;
    }

    private void newProject() {
        if (!confirmUnsavedChanges()) return;
        project = factory.createProject("Untitled SchemeOnYou project");
        activeDiagram = project.getDiagrams().stream().filter(d -> d.getType() == DiagramType.DATABASE).findFirst().orElse(project.getDiagrams().getFirst());
        selectedTableId = null;
        selectedColumnId = null;
        selectedForeignKeyId = null;
        selectedParticipantId = null;
        selectedMessageId = null;
        relationPin.clear();
        activeFkPreview = null;
        undo.clear();
        documentState.resetUntitledClean();
        refreshProjectLists();
        redraw();
        updateDocumentChrome();
        context.setText("New project created");
    }

    private void openProject() {
        if (!confirmUnsavedChanges()) return;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open SchemeOnYou project");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SchemeOnYou JSON", "*.json"));
        var file = chooser.showOpenDialog(stage);
        if (file == null) return;
        try {
            SchemeProject loaded = storage.load(file.toPath());
            if (loaded.getDiagrams().isEmpty()) {
                throw new IOException("Project has no diagrams");
            }
            project = loaded;
            activeDiagram = project.getDiagrams().stream().filter(d -> d.getType() == DiagramType.DATABASE).findFirst().orElse(project.getDiagrams().getFirst());
            selectedTableId = firstTableId().orElse(null);
            selectedColumnId = null;
            selectedForeignKeyId = null;
            selectedParticipantId = firstParticipantId().orElse(null);
            selectedMessageId = null;
            relationPin.clear();
            activeFkPreview = null;
            undo.clear();
            documentState.bindCleanFile(file.toPath());
            refreshProjectLists();
            redraw();
            updateDocumentChrome();
            context.setText("Opened " + file.getName());
        } catch (IOException | RuntimeException ex) {
            new Alert(Alert.AlertType.ERROR, "Could not open project: " + ex.getMessage()).showAndWait();
            context.setText("Open project failed");
        }
    }

    private void addDiagram(DiagramType type) {
        String title = type == DiagramType.DATABASE ? "Database diagram" : "Sequence diagram";
        Diagram diagram = new Diagram(factoryId("diagram"), title + " " + (project.getDiagrams().size() + 1), type);
        runMutating(new AddDiagramCommand(project, diagram));
        activeDiagram = diagram;
        selectedTableId = null;
        selectedColumnId = null;
        selectedForeignKeyId = null;
        selectedParticipantId = null;
        selectedMessageId = null;
        relationPin.clear();
        activeFkPreview = null;
        refreshProjectLists();
        redraw();
        context.setText("Added " + diagram.getName());
    }

    private void addForeignKeyFromSelection() {
        if (!requireDatabaseContext("Add foreign key")) return;
        if (activeFkPreview != null) {
            confirmFkPreview();
            return;
        }
        Optional<FkPreview> preview = buildFkPreviewFromSelection();
        if (preview.isEmpty()) return;
        activeFkPreview = preview.get();
        redraw();
        context.setText(fkTargetSelectionContextLine(activeFkPreview));
    }

    private Optional<FkPreview> buildFkPreviewFromSelection() {
        Optional<DbTable> source = selectedTableId == null ? Optional.empty() : activeDiagram.findTable(selectedTableId);
        if (source.isEmpty()) {
            context.setText("Select a source table before Add foreign key");
            return Optional.empty();
        }
        Optional<DbColumn> sourceColumn = selectedColumnId == null
                ? firstNonPrimaryColumn(source.get()).or(() -> firstColumn(source.get()))
                : source.get().findColumn(selectedColumnId).or(() -> firstNonPrimaryColumn(source.get())).or(() -> firstColumn(source.get()));
        Optional<DbTable> target = relationPin.targetTableId().flatMap(activeDiagram::findTable)
                .filter(t -> !t.getId().equals(source.get().getId()))
                .or(() -> activeDiagram.getTables().stream().filter(t -> !t.getId().equals(source.get().getId())).findFirst());
        if (sourceColumn.isEmpty() || target.isEmpty()) {
            context.setText("Add foreign key needs a selected table with a column and another target table");
            return Optional.empty();
        }
        Optional<DbColumn> targetColumn = relationPin.targetColumnId()
                .flatMap(target.get()::findColumn)
                .or(() -> primaryKey(target.get()))
                .or(() -> firstColumn(target.get()));
        if (targetColumn.isEmpty()) {
            context.setText("Target table has no column for foreign key");
            return Optional.empty();
        }
        return Optional.of(new FkPreview(source.get().getId(), sourceColumn.get().getId(), target.get().getId(), targetColumn.get().getId()));
    }

    private boolean handleFkPreviewKey(KeyEvent event) {
        if (activeFkPreview == null || event.isControlDown() || event.isAltDown() || event.isMetaDown()) return false;
        return switch (event.getCode()) {
            case ENTER -> { confirmFkPreview(); yield true; }
            case ESCAPE -> { cancelFkPreview(); yield true; }
            case X -> { swapFkPreview(); yield true; }
            case LEFT -> { selectFkPreviewTarget(Direction.LEFT); yield true; }
            case RIGHT -> { selectFkPreviewTarget(Direction.RIGHT); yield true; }
            case UP -> { selectFkPreviewTarget(Direction.UP); yield true; }
            case DOWN -> { selectFkPreviewTarget(Direction.DOWN); yield true; }
            case HOME -> { selectFirstFkPreviewTarget(); yield true; }
            case END -> { selectLastFkPreviewTarget(); yield true; }
            default -> false;
        };
    }

    private void confirmFkPreview() {
        if (activeFkPreview == null) return;
        FkPreview preview = activeFkPreview;
        List<String> errors = fkPreviewErrors(preview);
        if (!errors.isEmpty()) {
            context.setText("Cannot create FK: " + String.join("; ", errors));
            redraw();
            return;
        }
        ForeignKey fk = factory.foreignKey(preview.getSourceTableId(), preview.getSourceColumnId(), preview.getTargetTableId(), preview.getTargetColumnId());
        runMutating(new AddForeignKeyCommand(activeDiagram, fk));
        if (preview.isKeepTargetPinnedAfterCreate()) relationPin.pin(preview.getTargetTableId(), preview.getTargetColumnId());
        else relationPin.clear();
        activeFkPreview = null;
        redraw();
        String result = "Added FK: " + tableName(fk.getSourceTableId()) + "." + columnName(fk.getSourceTableId(), fk.getSourceColumnId())
                + " → " + tableName(fk.getTargetTableId()) + "." + columnName(fk.getTargetTableId(), fk.getTargetColumnId());
        context.setText(preview.isKeepTargetPinnedAfterCreate() ? result + " • " + relationPinContextLine() : result);
    }

    private void cancelFkPreview() {
        activeFkPreview = null;
        redraw();
        context.setText("FK preview canceled");
    }

    private void swapFkPreview() {
        if (activeFkPreview == null) return;
        activeFkPreview.swapSourceAndTarget();
        redraw();
        context.setText(fkTargetSelectionContextLine(activeFkPreview));
    }

    private void selectFkPreviewTarget(Direction direction) {
        if (activeFkPreview == null) return;
        layout.layout(activeDiagram);
        String baseId = activeFkPreview.getTargetTableId();
        Rect base = activeDiagram.getCanvasState().getBoundsByElementId().get(baseId);
        if (base == null) {
            selectFirstFkPreviewTarget();
            return;
        }
        double bx = base.center().x();
        double by = base.center().y();
        activeDiagram.getTables().stream()
                .filter(table -> !table.getId().equals(activeFkPreview.getSourceTableId()))
                .filter(table -> !table.getId().equals(baseId))
                .map(table -> new java.util.AbstractMap.SimpleEntry<>(table, activeDiagram.getCanvasState().getBoundsByElementId().get(table.getId())))
                .filter(entry -> entry.getValue() != null && isInDirection(direction, bx, by, entry.getValue().center().x(), entry.getValue().center().y()))
                .min(Comparator.comparingDouble(entry -> directionalDistance(direction, bx, by, entry.getValue().center().x(), entry.getValue().center().y())))
                .ifPresentOrElse(entry -> setFkPreviewTarget(entry.getKey()),
                        () -> context.setText("No target table " + direction.name().toLowerCase() + " of current target • Enter Create • Esc Cancel"));
    }

    private void selectFirstFkPreviewTarget() {
        if (activeFkPreview == null) return;
        activeDiagram.getTables().stream()
                .filter(table -> !table.getId().equals(activeFkPreview.getSourceTableId()))
                .findFirst()
                .ifPresentOrElse(this::setFkPreviewTarget,
                        () -> context.setText("No valid target table for FK"));
    }

    private void selectLastFkPreviewTarget() {
        if (activeFkPreview == null) return;
        List<DbTable> targets = activeDiagram.getTables().stream()
                .filter(table -> !table.getId().equals(activeFkPreview.getSourceTableId()))
                .toList();
        if (targets.isEmpty()) context.setText("No valid target table for FK");
        else setFkPreviewTarget(targets.getLast());
    }

    private void setFkPreviewTarget(DbTable target) {
        Optional<DbColumn> targetColumn = primaryKey(target).or(() -> firstColumn(target));
        if (targetColumn.isEmpty()) {
            context.setText("Target table has no column for foreign key");
            return;
        }
        activeFkPreview.setTarget(target.getId(), targetColumn.get().getId());
        redraw();
        context.setText(fkTargetSelectionContextLine(activeFkPreview));
    }

    private void addJoinTableFromSelection() {
        if (!requireDatabaseContext("Add join table")) return;
        Optional<DbTable> left = selectedTableId == null ? Optional.empty() : activeDiagram.findTable(selectedTableId);
        Optional<DbTable> right = relationPin.targetTableId().flatMap(activeDiagram::findTable)
                .filter(t -> left.isEmpty() || !t.getId().equals(left.get().getId()))
                .or(() -> activeDiagram.getTables().stream().filter(t -> left.isPresent() && !t.getId().equals(left.get().getId())).findFirst());
        if (left.isEmpty() || right.isEmpty()) {
            context.setText("Add join table needs selected and pinned/second table");
            return;
        }
        Optional<DbColumn> leftPk = primaryKey(left.get()).or(() -> firstColumn(left.get()));
        Optional<DbColumn> rightPk = primaryKey(right.get()).or(() -> firstColumn(right.get()));
        if (leftPk.isEmpty() || rightPk.isEmpty()) {
            context.setText("Both tables need a key column for join table");
            return;
        }
        DbTable join = factory.table(left.get().getName() + "_" + right.get().getName());
        DbColumn leftFk = factory.column(left.get().getName() + "_id", leftPk.get().getType());
        DbColumn rightFk = factory.column(right.get().getName() + "_id", rightPk.get().getType());
        leftFk.setNullable(false);
        rightFk.setNullable(false);
        leftFk.setPrimaryKey(true);
        rightFk.setPrimaryKey(true);
        ForeignKey leftRelation = factory.foreignKey(join.getId(), leftFk.getId(), left.get().getId(), leftPk.get().getId());
        ForeignKey rightRelation = factory.foreignKey(join.getId(), rightFk.getId(), right.get().getId(), rightPk.get().getId());
        if (!confirmJoinTablePreview(left.get(), right.get(), join, leftFk, rightFk)) {
            context.setText("Join table preview canceled");
            return;
        }
        runMutating(joinTableFactory.create(activeDiagram, left.get(), leftPk.get(), right.get(), rightPk.get(), join, leftFk, rightFk, leftRelation, rightRelation));
        relationPin.clear();
        selectedParticipantId = null;
        selectedTableId = join.getId();
        selectedColumnId = null;
        redraw();
        context.setText("Added join table: " + join.getName());
    }

    private boolean confirmJoinTablePreview(DbTable left, DbTable right, DbTable join, DbColumn leftFk, DbColumn rightFk) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Create join table");
        confirm.setHeaderText("Create join table: " + join.getName());
        confirm.setContentText("Participants: " + left.getName() + " ↔ " + right.getName()
                + "\nColumns: " + leftFk.getName() + ", " + rightFk.getName()
                + "\nAction: create table, two PK/FK columns, and two FK edges as one undoable command.");
        return confirm.showAndWait().filter(ButtonType.OK::equals).isPresent();
    }

    private void renameSelected() {
        if (!requireDatabaseContext("Rename table")) return;
        Optional<DbTable> selected = selectedTableId == null ? Optional.empty() : activeDiagram.findTable(selectedTableId);
        if (selected.isEmpty()) {
            context.setText("Select a table before Rename/Edit");
            return;
        }
        TextInputDialog dialog = new TextInputDialog(selected.get().getName());
        dialog.setTitle("Rename selected");
        dialog.setHeaderText("Rename table");
        dialog.showAndWait().map(String::trim).filter(s -> !s.isBlank()).ifPresent(name -> {
            runMutating(new RenameTableCommand(selected.get(), name));
            redraw();
            context.setText("Renamed table: " + name);
        });
    }

    private void deleteSelected() {
        if (activeDiagram == null) return;
        configureDeleteSelection();
        Optional<DeletePreview> preview = deleteSelectedController.requestDelete(activeDiagram, deleteSelection);
        if (preview.isEmpty()) {
            context.setText("Select a table, column, FK, participant, or message before Delete selected");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, preview.get().contextLine(), ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("Delete selected");
        confirm.setHeaderText("Confirm destructive action");
        confirm.showAndWait().filter(ButtonType.OK::equals).ifPresent(button -> {
            DeletePreview executed = deleteSelectedController.confirmDelete(undo, deleteSelection).orElse(preview.get());
            markDirty();
            cleanupAfterDelete(executed);
            redraw();
            context.setText("Deleted: " + executed.targetName());
        });
    }

    private void configureDeleteSelection() {
        if (selectedMessageId != null) {
            deleteSelection.selectionDepth(SelectionDepth.MESSAGE);
            deleteSelection.selectedElementId(selectedMessageId);
        } else if (selectedParticipantId != null) {
            deleteSelection.selectionDepth(SelectionDepth.PARTICIPANT);
            deleteSelection.selectedElementId(selectedParticipantId);
        } else if (selectedForeignKeyId != null) {
            deleteSelection.selectionDepth(SelectionDepth.RELATION);
            deleteSelection.selectedElementId(selectedForeignKeyId);
        } else if (selectedColumnId != null) {
            deleteSelection.selectionDepth(SelectionDepth.COLUMN);
            deleteSelection.selectedElementId(selectedColumnId);
        } else if (selectedTableId != null) {
            deleteSelection.selectionDepth(SelectionDepth.TABLE);
            deleteSelection.selectedElementId(selectedTableId);
        } else {
            deleteSelection.selectionDepth(SelectionDepth.DIAGRAM);
            deleteSelection.selectedElementId(null);
        }
    }

    private void cleanupAfterDelete(DeletePreview preview) {
        if (selectedTableId != null && (relationPin.matchesTable(selectedTableId) || preview.affectedForeignKeyIds().contains(selectedForeignKeyId))) {
            relationPin.clear();
        }
        if (selectedColumnId != null && relationPin.matchesColumn(selectedTableId, selectedColumnId)) {
            relationPin.clear();
        }
        selectedForeignKeyId = null;
        selectedColumnId = null;
        if (selectedMessageId != null && activeDiagram.getMessages().stream().noneMatch(message -> message.getId().equals(selectedMessageId))) {
            selectedMessageId = null;
        }
        if (selectedParticipantId != null && activeDiagram.getParticipants().stream().noneMatch(participant -> participant.getId().equals(selectedParticipantId))) {
            selectedParticipantId = firstParticipantId().orElse(null);
        }
        if (selectedTableId == null || activeDiagram.findTable(selectedTableId).isEmpty()) {
            selectedTableId = firstTableId().orElse(null);
        }
    }

    private void findElement() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Find element");
        dialog.setHeaderText("Find table, column, relation, participant, or message");
        dialog.showAndWait().map(String::trim).filter(s -> !s.isBlank()).ifPresent(query -> {
            List<FindElementPresenter.SearchResult> results = findElementPresenter.search(activeDiagram, query);
            if (results.isEmpty()) {
                context.setText("No element found for: " + query);
                return;
            }
            FindElementPresenter.SearchResult result = results.getFirst();
            applyFindSelection(result.selection());
            redraw();
            context.setText("Found " + findElementKindLabel(result.kind()) + ": " + result.label());
        });
    }

    private void applyFindSelection(FindElementPresenter.SelectionTarget selection) {
        selectedTableId = selection.tableId();
        selectedColumnId = selection.columnId();
        selectedForeignKeyId = selection.foreignKeyId();
        selectedParticipantId = selection.participantId();
        selectedMessageId = selection.messageId();
    }

    private String findElementKindLabel(FindElementPresenter.ElementKind kind) {
        return switch (kind) {
            case DB_TABLE -> "table";
            case DB_COLUMN -> "column";
            case FOREIGN_KEY -> "foreign key";
            case SEQUENCE_PARTICIPANT -> "participant";
            case SEQUENCE_MESSAGE -> "message";
        };
    }

    private Optional<DbColumn> primaryKey(DbTable table) {
        return table.getColumns().stream().filter(DbColumn::isPrimaryKey).findFirst();
    }

    private Optional<DbColumn> firstNonPrimaryColumn(DbTable table) {
        return table.getColumns().stream().filter(column -> !column.isPrimaryKey()).findFirst();
    }

    private Optional<DbColumn> firstColumn(DbTable table) {
        return table.getColumns().stream().findFirst();
    }

    private void refreshProjectLists() {
        if (diagramList != null) {
            diagramList.getItems().setAll(project.getDiagrams());
            diagramList.getSelectionModel().select(activeDiagram);
        }
        if (tableList != null && activeDiagram != null) {
            tableList.getItems().setAll(activeDiagram.getTables());
        }
    }

    private boolean saveProject() {
        Path target = documentState.currentFile().orElseGet(() -> chooseSavePath().orElse(null));
        if (target == null) return false;
        try {
            storage.save(target, project);
            documentState.bindCleanFile(target);
            updateDocumentChrome();
            context.setText("Saved " + target.getFileName());
            return true;
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            return false;
        }
    }

    private Optional<Path> chooseSavePath() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save SchemeOnYou project");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SchemeOnYou JSON", "*.json"));
        var file = chooser.showSaveDialog(stage);
        return file == null ? Optional.empty() : Optional.of(file.toPath());
    }

    private void exportSvg() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export SVG");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SVG", "*.svg"));
        var file = chooser.showSaveDialog(stage);
        if (file == null) return;
        try {
            Files.writeString(file.toPath(), svgExporter.export(activeDiagram));
            context.setText("Exported " + file.getName());
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
        }
    }

    private boolean confirmUnsavedChanges() {
        Optional<DocumentLifecycleGuard.UnsavedPrompt> prompt = documentLifecycleGuard.prompt(documentState, project.getName());
        if (prompt.isEmpty()) return true;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(prompt.get().title());
        alert.setHeaderText(prompt.get().header());
        alert.setContentText(prompt.get().content());
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.YES);
        ButtonType discard = new ButtonType("Discard", ButtonBar.ButtonData.NO);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(save, discard, cancel);
        Optional<ButtonType> choice = alert.showAndWait();
        DocumentLifecycleGuard.Decision decision;
        if (choice.filter(save::equals).isPresent()) {
            decision = documentLifecycleGuard.decide(DocumentLifecycleGuard.UserChoice.SAVE, saveProject());
        } else if (choice.filter(discard::equals).isPresent()) {
            decision = documentLifecycleGuard.decide(DocumentLifecycleGuard.UserChoice.DISCARD, false);
        } else {
            decision = documentLifecycleGuard.decide(DocumentLifecycleGuard.UserChoice.CANCEL, false);
        }
        return decision == DocumentLifecycleGuard.Decision.PROCEED;
    }

    private void runMutating(Command command) {
        undo.run(command);
        markDirty();
    }

    private void undoCommand() {
        undo.undo().ifPresent(ignored -> {
            markDirty();
            redraw();
            context.setText("Undo");
        });
    }

    private void redoCommand() {
        undo.redo().ifPresent(ignored -> {
            markDirty();
            redraw();
            context.setText("Redo");
        });
    }

    private void markDirty() {
        if (project != null) project.touch();
        documentState.markDirty();
        updateDocumentChrome();
    }

    private void updateDocumentChrome() {
        if (projectTitle != null) projectTitle.setText("Project: " + documentState.displayName(project.getName()));
        if (dirtyState != null) dirtyState.setText(documentState.dirtyMarker());
        if (stage != null) stage.setTitle((documentState.isDirty() ? "* " : "") + documentState.displayName(project.getName()) + " - SchemeOnYou");
    }

    private void run(Command command) {
        runMutating(command);
        activeDiagram = project.getDiagrams().getLast();
        diagramList.getItems().setAll(project.getDiagrams());
        diagramList.getSelectionModel().select(activeDiagram);
        redraw();
    }

    private void redraw() {
        if (activeDiagram == null || canvas == null) return;
        layout.layout(activeDiagram);
        pruneMissingRelationPin();
        if (tableList != null) tableList.getItems().setAll(activeDiagram.getTables());
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.web(CANVAS_BG));
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFont(uiFont(14));
        CanvasState state = activeDiagram.getCanvasState();
        g.save();
        g.translate(-state.getViewportX() * state.getZoom(), -state.getViewportY() * state.getZoom());
        g.scale(state.getZoom(), state.getZoom());
        if (activeDiagram.getType() == DiagramType.DATABASE) drawDatabase(g); else drawSequence(g);
        g.restore();
        drawViewportStatus(g);
        drawCanvasFocusRing(g);
        ValidationResult validation = validator.validate(activeDiagram);
        updateInspector(validation);
        updateContextAfterRedraw(validation);
    }

    private void zoomCanvasBy(double factor) {
        if (activeDiagram == null) return;
        CanvasState state = activeDiagram.getCanvasState();
        double oldZoom = state.getZoom();
        double centerX = state.getViewportX() + canvas.getWidth() / 2.0 / oldZoom;
        double centerY = state.getViewportY() + canvas.getHeight() / 2.0 / oldZoom;
        state.setZoom(oldZoom * factor);
        double newZoom = state.getZoom();
        state.setViewportX(centerX - canvas.getWidth() / 2.0 / newZoom);
        state.setViewportY(centerY - canvas.getHeight() / 2.0 / newZoom);
        markDirty();
        context.setText("Canvas zoom: " + Math.round(newZoom * 100) + "%");
        redraw();
    }

    private void panCanvasByPixels(double screenDeltaX, double screenDeltaY) {
        if (activeDiagram == null) return;
        CanvasState state = activeDiagram.getCanvasState();
        state.pan(screenDeltaX / state.getZoom(), screenDeltaY / state.getZoom());
        markDirty();
        context.setText("Canvas pan: " + Math.round(state.getViewportX()) + ", " + Math.round(state.getViewportY()));
        redraw();
    }

    private void actualSizeCanvas() {
        if (activeDiagram == null) return;
        activeDiagram.getCanvasState().actualSize();
        markDirty();
        context.setText("Canvas actual size: 100%");
        redraw();
    }

    private void fitDiagramToCanvas() {
        if (activeDiagram == null || canvas == null) return;
        layout.layout(activeDiagram);
        Rect bounds = diagramBounds();
        CanvasState state = activeDiagram.getCanvasState();
        if (bounds == null) {
            state.actualSize();
            markDirty();
            context.setText("Canvas fit: empty diagram");
            redraw();
            return;
        }
        double padding = 48.0;
        double zoomX = (canvas.getWidth() - padding * 2) / Math.max(bounds.width(), 1);
        double zoomY = (canvas.getHeight() - padding * 2) / Math.max(bounds.height(), 1);
        state.setZoom(Math.min(zoomX, zoomY));
        state.setViewportX(bounds.x() - padding / state.getZoom());
        state.setViewportY(bounds.y() - padding / state.getZoom());
        markDirty();
        context.setText("Canvas fit: " + Math.round(state.getZoom() * 100) + "%");
        redraw();
    }

    private Rect diagramBounds() {
        return activeDiagram.getCanvasState().getBoundsByElementId().values().stream()
                .reduce(null, (acc, r) -> {
                    if (acc == null) return r;
                    double minX = Math.min(acc.x(), r.x());
                    double minY = Math.min(acc.y(), r.y());
                    double maxX = Math.max(acc.x() + acc.width(), r.x() + r.width());
                    double maxY = Math.max(acc.y() + acc.height(), r.y() + r.height());
                    return new Rect(minX, minY, maxX - minX, maxY - minY);
                });
    }

    private void drawViewportStatus(GraphicsContext g) {
        CanvasState state = activeDiagram.getCanvasState();
        g.setFill(Color.web(TEXT_SECONDARY));
        g.setFont(uiFont(11));
        g.fillText("zoom " + Math.round(state.getZoom() * 100) + "% · view " + Math.round(state.getViewportX()) + "," + Math.round(state.getViewportY()), 12, canvas.getHeight() - 12);
    }

    private void drawCanvasFocusRing(GraphicsContext g) {
        if (!canvas.isFocused()) return;
        g.setStroke(Color.web(SELECTION));
        g.setLineWidth(3);
        g.strokeRect(1.5, 1.5, canvas.getWidth() - 3, canvas.getHeight() - 3);
    }

    private void drawDatabase(GraphicsContext g) {
        ValidationResult validation = validator.validate(activeDiagram);
        Set<String> errorElementIds = validation.issues().stream()
                .filter(issue -> issue.severity() == ValidationIssue.Severity.ERROR)
                .map(ValidationIssue::elementId)
                .collect(Collectors.toSet());
        Set<String> warningElementIds = validation.issues().stream()
                .filter(issue -> issue.severity() == ValidationIssue.Severity.WARNING)
                .map(ValidationIssue::elementId)
                .collect(Collectors.toSet());
        Set<String> warningTableIds = activeDiagram.getForeignKeys().stream()
                .filter(fk -> warningElementIds.contains(fk.getId()) || errorElementIds.contains(fk.getId()))
                .flatMap(fk -> java.util.stream.Stream.of(fk.getSourceTableId(), fk.getTargetTableId()))
                .collect(Collectors.toSet());

        for (ForeignKey fk : activeDiagram.getForeignKeys()) {
            Rect a = activeDiagram.getCanvasState().getBoundsByElementId().get(fk.getSourceTableId());
            Rect b = activeDiagram.getCanvasState().getBoundsByElementId().get(fk.getTargetTableId());
            if (a != null && b != null) {
                boolean error = errorElementIds.contains(fk.getId());
                boolean warning = warningElementIds.contains(fk.getId());
                boolean selectedFk = fk.getId().equals(selectedForeignKeyId);
                Color edgeColor = Color.web(selectedFk ? SELECTION : error ? ERROR : warning ? WARNING : TEXT_SECONDARY);
                drawDirectedEdge(g, a.center().x(), a.center().y(), b.center().x(), b.center().y(), edgeColor, selectedFk ? "FK selected" : error || warning ? "FK !" : "FK");
                if (error || warning) {
                    g.setFill(edgeColor);
                    g.fillOval((a.center().x() + b.center().x()) / 2 - 4, (a.center().y() + b.center().y()) / 2 - 4, 8, 8);
                }
            }
        }
        for (DbTable table : activeDiagram.getTables()) {
            Rect r = activeDiagram.getCanvasState().getBoundsByElementId().getOrDefault(table.getId(), new Rect(64,64,220,120));
            boolean selected = table.getId().equals(selectedTableId);
            boolean pinnedTable = relationPin.matchesTable(table.getId());
            boolean columnDepth = selected && selectedColumnId != null;
            boolean hasValidationWarning = warningTableIds.contains(table.getId());
            g.setFill(Color.web(selected ? CARD_HEADER : CARD_BG));
            g.fillRoundRect(r.x(), r.y(), r.width(), r.height(), 16, 16);
            g.setStroke(Color.web(columnDepth ? SELECTION : selected ? PIN_COLOR : hasValidationWarning ? WARNING : FK_COLOR));
            g.setLineWidth(columnDepth || selected ? 2.5 : 1.0);
            g.strokeRoundRect(r.x(), r.y(), r.width(), r.height(), 16, 16);
            g.setLineWidth(1.0);
            g.setFill(Color.web(TEXT_PRIMARY));
            g.fillText((hasValidationWarning ? "⚠ " : "") + table.getName(), r.x() + 12, r.y() + 24);
            if (pinnedTable) drawRelationPinBadge(g, r.x() + r.width() - 54, r.y() + 8, "PIN");
            int row = 0;
            for (DbColumn column : table.getColumns()) {
                double rowY = r.y() + 58 + row * 24;
                boolean pinnedColumn = relationPin.matchesColumn(table.getId(), column.getId());
                if (column.getId().equals(selectedColumnId)) {
                    g.setFill(Color.web(PANEL_ELEVATED));
                    g.fillRoundRect(r.x() + 10, rowY - 16, r.width() - 20, 22, 8, 8);
                    g.setStroke(Color.web(SELECTION));
                    g.strokeRoundRect(r.x() + 10, rowY - 16, r.width() - 20, 22, 8, 8);
                    g.setFill(Color.web(TEXT_PRIMARY));
                }
                g.fillText((column.isPrimaryKey() ? "◆ " : "") + column.getName() + ": " + column.getType(), r.x() + 16, rowY);
                if (pinnedColumn) drawRelationPinBadge(g, r.x() + r.width() - 54, rowY - 15, "PIN");
                row++;
            }
        }
        drawFkPreview(g);
    }

    private void drawDirectedEdge(GraphicsContext g, double x1, double y1, double x2, double y2, Color color, String label) {
        g.save();
        g.setStroke(color);
        g.setFill(color);
        g.setLineWidth(2.0);
        g.strokeLine(x1, y1, x2, y2);
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double arrowLength = 12.0;
        double arrowWidth = 7.0;
        double ax = x2 - Math.cos(angle) * 18.0;
        double ay = y2 - Math.sin(angle) * 18.0;
        double leftX = ax - Math.cos(angle) * arrowLength + Math.sin(angle) * arrowWidth;
        double leftY = ay - Math.sin(angle) * arrowLength - Math.cos(angle) * arrowWidth;
        double rightX = ax - Math.cos(angle) * arrowLength - Math.sin(angle) * arrowWidth;
        double rightY = ay - Math.sin(angle) * arrowLength + Math.cos(angle) * arrowWidth;
        g.fillPolygon(new double[]{ax, leftX, rightX}, new double[]{ay, leftY, rightY}, 3);
        double midX = (x1 + x2) / 2.0;
        double midY = (y1 + y2) / 2.0;
        g.setFont(uiFont(10));
        g.fillText(label, midX + 6, midY - 6);
        g.restore();
    }

    private void drawRelationPinBadge(GraphicsContext g, double x, double y, String text) {
        g.setFill(Color.web(WARNING));
        g.fillRoundRect(x, y, 42, 18, 8, 8);
        g.setStroke(Color.web(PK_COLOR));
        g.strokeRoundRect(x, y, 42, 18, 8, 8);
        g.setFill(Color.web(APP_BG));
        g.setFont(uiFont(10));
        g.fillText(text, x + 10, y + 13);
    }

    private void drawSequence(GraphicsContext g) {
        if (activeDiagram.getParticipants().isEmpty()) {
            g.setFill(Color.web(TEXT_SECONDARY));
            g.fillText("Sequence diagram is empty. Use command palette: New/Add participant/message.", 64, 64);
            return;
        }

        for (SequenceParticipant participant : activeDiagram.getParticipants()) {
            Rect r = activeDiagram.getCanvasState().getBoundsByElementId().getOrDefault(participant.getId(), new Rect(64, 64, 140, 48));
            boolean selected = participant.getId().equals(selectedParticipantId);
            g.setFill(Color.web(selected ? CARD_HEADER : CARD_BG));
            g.fillRoundRect(r.x(), r.y(), r.width(), r.height(), 14, 14);
            g.setStroke(Color.web(selected ? PIN_COLOR : FK_COLOR));
            g.setLineWidth(selected ? 2.5 : 1.5);
            g.strokeRoundRect(r.x(), r.y(), r.width(), r.height(), 14, 14);
            g.setFill(Color.web(TEXT_PRIMARY));
            g.fillText(participant.getName(), r.x() + 12, r.y() + 29);
            g.setStroke(Color.web(TEXT_SECONDARY));
            g.setLineDashes(8, 6);
            g.strokeLine(r.center().x(), r.y() + r.height(), r.center().x(), 760);
            g.setLineDashes();
        }

        int index = 0;
        for (SequenceMessage message : activeDiagram.getMessages()) {
            Rect from = sequenceParticipantBounds(message.getFromParticipantId()).orElse(null);
            Rect to = sequenceParticipantBounds(message.getToParticipantId()).orElse(null);
            if (from == null || to == null) continue;
            double y = sequenceMessageY(index);
            double x1 = from.center().x();
            double x2 = to.center().x();
            boolean selected = message.getId().equals(selectedMessageId);
            drawDirectedEdge(g, x1, y, x2, y, Color.web(selected ? SELECTION : TEXT_SECONDARY), message.getLabel());
            if (selected) {
                g.setStroke(Color.web(SELECTION));
                g.setLineWidth(2.0);
                g.strokeRoundRect(Math.min(x1, x2) + 6, y - 18, Math.abs(x2 - x1) - 12, 30, 8, 8);
            }
            if (message.isActivation()) {
                double activationX = x2 - 6;
                g.setFill(Color.web(PANEL_ELEVATED));
                g.fillRoundRect(activationX, y, 12, 52, 6, 6);
                g.setStroke(Color.web(SELECTION));
                g.strokeRoundRect(activationX, y, 12, 52, 6, 6);
            }
            index++;
        }
    }

    private Optional<Rect> sequenceParticipantBounds(String participantId) {
        return activeDiagram.getParticipants().stream()
                .filter(participant -> participant.getId().equals(participantId))
                .findFirst()
                .map(participant -> activeDiagram.getCanvasState().getBoundsByElementId().getOrDefault(participant.getId(), new Rect(64, 64, 140, 48)));
    }

    private Label sectionLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-title");
        return label;
    }

    private void updateInspector(ValidationResult validation) {
        if (inspectorFields == null) return;
        inspectorFields.getChildren().clear();
        InspectorPresenter.Selection selection = InspectorPresenter.Selection.of(selectedTableId, selectedColumnId, selectedParticipantId, selectedMessageId);
        Optional<InspectorPresenter.FkPreviewModel> fkPreview = activeFkPreview == null
                ? Optional.empty()
                : Optional.of(new InspectorPresenter.FkPreviewModel(
                tableName(activeFkPreview.getSourceTableId()) + "." + columnName(activeFkPreview.getSourceTableId(), activeFkPreview.getSourceColumnId()),
                tableName(activeFkPreview.getTargetTableId()) + "." + columnName(activeFkPreview.getTargetTableId(), activeFkPreview.getTargetColumnId()),
                activeFkPreview.isKeepTargetPinnedAfterCreate(),
                fkPreviewValidationText(activeFkPreview)));

        InspectorPresenter.InspectorModel model = inspectorPresenter.inspect(activeDiagram, selection, fkPreview);
        for (InspectorPresenter.Section section : model.sections()) {
            inspectorFields.getChildren().add(sectionLabel(section.title()));
            if ("Table".equals(section.title())) {
                addTableInspectorFields(section);
            } else if ("Sequence participant".equals(section.title())) {
                addSequenceParticipantInspectorFields(section);
            } else if ("Sequence message".equals(section.title())) {
                addSequenceMessageInspectorFields(section);
            } else if ("FK preview".equals(section.title())) {
                inspectorFields.getChildren().add(fkPreviewInspector(activeFkPreview));
            } else {
                addReadOnlyFields(section);
            }
        }

        StringBuilder text = new StringBuilder();
        appendValidationSummary(text, validation);
        TextArea validationText = new TextArea(text.toString());
        validationText.setEditable(false);
        validationText.setWrapText(true);
        validationText.setPrefRowCount(Math.min(8, Math.max(3, validation.issues().size() + 2)));
        inspectorFields.getChildren().add(new Separator());
        inspectorFields.getChildren().add(validationText);
    }

    private void addTableInspectorFields(InspectorPresenter.Section section) {
        Optional<DbTable> selected = selectedTableId == null ? Optional.empty() : activeDiagram.findTable(selectedTableId);
        if (selected.isEmpty()) {
            addReadOnlyFields(section);
            return;
        }
        DbTable table = selected.get();
        section.fields().stream()
                .filter(field -> field.kind() == InspectorPresenter.FieldKind.TEXT && field.editField() == InspectorPresenter.EditField.TABLE_NAME)
                .findFirst()
                .ifPresent(field -> inspectorFields.getChildren().add(textEditor(field.label(), field.value(), field.editField(), value -> commitTableName(table, value))));
        inspectorFields.getChildren().add(new Separator());
        inspectorFields.getChildren().add(sectionLabel("Columns"));
        for (DbColumn column : table.getColumns()) {
            inspectorFields.getChildren().add(columnEditor(table, column, section));
        }
    }

    private void addSequenceParticipantInspectorFields(InspectorPresenter.Section section) {
        Optional<SequenceParticipant> selected = selectedParticipantId == null ? Optional.empty() : activeDiagram.getParticipants().stream().filter(participant -> participant.getId().equals(selectedParticipantId)).findFirst();
        if (selected.isEmpty()) {
            addReadOnlyFields(section);
            return;
        }
        SequenceParticipant participant = selected.get();
        for (InspectorPresenter.Field field : section.fields()) {
            if (field.kind() == InspectorPresenter.FieldKind.TEXT && field.editField() == InspectorPresenter.EditField.SEQUENCE_PARTICIPANT_NAME) {
                inspectorFields.getChildren().add(textEditor(field.label(), field.value(), field.editField(), value -> commitSequenceParticipantName(participant, value)));
            } else if (field.kind() == InspectorPresenter.FieldKind.ACTION_HINT) {
                inspectorFields.getChildren().add(new Label(field.label() + ": " + field.value()));
            }
        }
    }

    private void addSequenceMessageInspectorFields(InspectorPresenter.Section section) {
        Optional<SequenceMessage> selected = selectedMessageId == null ? Optional.empty() : activeDiagram.getMessages().stream().filter(message -> message.getId().equals(selectedMessageId)).findFirst();
        if (selected.isEmpty()) {
            addReadOnlyFields(section);
            return;
        }
        SequenceMessage message = selected.get();
        for (InspectorPresenter.Field field : section.fields()) {
            if (field.kind() == InspectorPresenter.FieldKind.TEXT && field.editField() == InspectorPresenter.EditField.SEQUENCE_MESSAGE_LABEL) {
                inspectorFields.getChildren().add(textEditor(field.label(), field.value(), field.editField(), value -> commitSequenceMessageLabel(message, value)));
            } else if (field.kind() == InspectorPresenter.FieldKind.CHECKBOX && field.editField() == InspectorPresenter.EditField.SEQUENCE_MESSAGE_ACTIVATION) {
                inspectorFields.getChildren().add(checkBox(field.label(), field.booleanValue(), field.editField(), value -> commitSequenceMessageActivation(message, value)));
            } else if (field.kind() == InspectorPresenter.FieldKind.READ_ONLY || field.kind() == InspectorPresenter.FieldKind.ACTION_HINT) {
                inspectorFields.getChildren().add(new Label(field.label() + ": " + field.value()));
            }
        }
    }

    private void addReadOnlyFields(InspectorPresenter.Section section) {
        for (InspectorPresenter.Field field : section.fields()) {
            inspectorFields.getChildren().add(new Label(field.label() + ": " + field.value()));
        }
    }

    private Node columnEditor(DbTable table, DbColumn column, InspectorPresenter.Section section) {
        VBox box = new VBox(4);
        box.setPadding(new Insets(4));
        boolean selected = column.getId().equals(selectedColumnId);
        box.setStyle(selected ? "-fx-border-color: " + SELECTION + "; -fx-border-radius: 5; -fx-padding: 4; -fx-background-color: " + PANEL_ELEVATED + "; -fx-background-radius: 5;" : "-fx-border-color: " + BORDER + "; -fx-border-radius: 5; -fx-padding: 4; -fx-background-color: " + PANEL_BG + "; -fx-background-radius: 5;");
        Label title = new Label((selected ? "Selected column: " : "Column: ") + table.getName() + "." + column.getName());
        TextField name = textField(column.getName(), InspectorPresenter.EditField.COLUMN_NAME, value -> commitColumnName(column, value));
        TextField type = textField(column.getType(), InspectorPresenter.EditField.COLUMN_TYPE, value -> commitColumnType(column, value));
        CheckBox nullable = checkBox("Nullable", column.isNullable(), InspectorPresenter.EditField.COLUMN_NULLABLE, value -> commitColumnFlag(column, "db.column.nullable", "Set nullable", DbColumn::isNullable, DbColumn::setNullable, value));
        CheckBox pk = checkBox("Primary key", column.isPrimaryKey(), InspectorPresenter.EditField.COLUMN_PRIMARY_KEY, value -> commitColumnFlag(column, "db.column.pk", "Set primary key", DbColumn::isPrimaryKey, DbColumn::setPrimaryKey, value));
        CheckBox unique = checkBox("Unique", column.isUnique(), InspectorPresenter.EditField.COLUMN_UNIQUE, value -> commitColumnFlag(column, "db.column.unique", "Set unique", DbColumn::isUnique, DbColumn::setUnique, value));
        box.getChildren().addAll(title, labelled("Name", name), labelled("Type", type), nullable, pk, unique);
        return box;
    }

    private Node textEditor(String label, String value, InspectorPresenter.EditField editField, java.util.function.Consumer<String> commit) {
        return labelled(label, textField(value, editField, commit));
    }

    private Node labelled(String label, Node input) {
        return new VBox(1, new Label(label), input);
    }

    private TextField textField(String original, InspectorPresenter.EditField editField, java.util.function.Consumer<String> commit) {
        TextField field = new TextField(original);
        final boolean[] cancelled = {false};
        field.setOnAction(event -> commitTextField(field, editField, original, commit));
        field.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) {
                if (cancelled[0]) cancelled[0] = false;
                else commitTextField(field, editField, original, commit);
            }
        });
        field.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelled[0] = true;
                field.setText(original);
                inspectorPanel.requestFocus();
                context.setText("Inspector edit cancelled");
                event.consume();
            }
        });
        return field;
    }

    private void commitTextField(TextField field, InspectorPresenter.EditField editField, String original, java.util.function.Consumer<String> commit) {
        Optional<InspectorPresenter.EditDecision> decision = inspectorPresenter.textEdit(editField, original, field.getText());
        if (decision.isEmpty()) {
            field.setText(original);
            return;
        }
        commit.accept(decision.get().value());
    }

    private CheckBox checkBox(String label, boolean original, InspectorPresenter.EditField editField, java.util.function.Consumer<Boolean> commit) {
        CheckBox box = new CheckBox(label);
        box.setSelected(original);
        box.setOnAction(event -> {
            inspectorPresenter.booleanEdit(editField, original, box.isSelected()).ifPresent(decision -> commit.accept(decision.booleanValue()));
        });
        return box;
    }

    private void commitTableName(DbTable table, String value) {
        runMutating(new RenameTableCommand(table, value));
        redraw();
        context.setText("Renamed table: " + value);
    }

    private void commitColumnName(DbColumn column, String value) {
        runMutating(new EditColumnCommand<>(column, "db.column.rename", "Rename column", DbColumn::getName, DbColumn::setName, value));
        redraw();
        context.setText("Renamed column: " + value);
    }

    private void commitColumnType(DbColumn column, String value) {
        runMutating(new EditColumnCommand<>(column, "db.column.type", "Change column type", DbColumn::getType, DbColumn::setType, value));
        redraw();
        context.setText("Changed column type: " + value);
    }

    private void commitColumnFlag(DbColumn column, String id, String title, java.util.function.Function<DbColumn, Boolean> getter, java.util.function.BiConsumer<DbColumn, Boolean> setter, boolean value) {
        runMutating(new EditColumnCommand<>(column, id, title, getter, setter, value));
        redraw();
        context.setText(title + ": " + (value ? "on" : "off"));
    }

    private void commitSequenceParticipantName(SequenceParticipant participant, String value) {
        runMutating(new EditValueCommand<>(participant, "sequence.participant.rename", "Rename participant", SequenceParticipant::getName, SequenceParticipant::setName, value));
        redraw();
        context.setText("Renamed participant: " + value);
    }

    private void commitSequenceMessageLabel(SequenceMessage message, String value) {
        runMutating(new EditValueCommand<>(message, "sequence.message.label", "Edit message label", SequenceMessage::getLabel, SequenceMessage::setLabel, value));
        redraw();
        context.setText("Changed message label: " + value);
    }

    private void commitSequenceMessageActivation(SequenceMessage message, boolean value) {
        runMutating(new EditValueCommand<>(message, "sequence.message.activation", "Set message activation", SequenceMessage::isActivation, SequenceMessage::setActivation, value));
        redraw();
        context.setText("Message activation: " + (value ? "on" : "off"));
    }

    private String participantName(String participantId) {
        return activeDiagram.getParticipants().stream()
                .filter(participant -> participant.getId().equals(participantId))
                .map(SequenceParticipant::getName)
                .findFirst()
                .orElse(participantId);
    }


    private Node fkPreviewInspector(FkPreview preview) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(8));
        box.setStyle("-fx-border-color: " + WARNING + "; -fx-border-radius: 6; -fx-background-color: " + PANEL_ELEVATED + "; -fx-background-radius: 6;");
        Label title = new Label("FK preview");
        Label source = chip("Source", tableName(preview.getSourceTableId()) + "." + columnName(preview.getSourceTableId(), preview.getSourceColumnId()));
        Label target = chip("Target", tableName(preview.getTargetTableId()) + "." + columnName(preview.getTargetTableId(), preview.getTargetColumnId()));
        Label direction = new Label("Creates: source column references target column");
        CheckBox keepPinned = new CheckBox("Keep target pinned after create");
        keepPinned.setSelected(preview.isKeepTargetPinnedAfterCreate());
        keepPinned.setOnAction(e -> {
            preview.setKeepTargetPinnedAfterCreate(keepPinned.isSelected());
            context.setText(fkPreviewContextLine(preview));
        });
        TextArea validation = new TextArea(fkPreviewValidationText(preview));
        validation.setEditable(false);
        validation.setWrapText(true);
        validation.setPrefRowCount(Math.min(5, Math.max(2, fkPreviewErrors(preview).size() + fkPreviewWarnings(preview).size() + 1)));
        HBox buttons = new HBox(6);
        Button swap = new Button("X Swap");
        swap.setOnAction(e -> swapFkPreview());
        Button create = new Button("Enter Create");
        create.setOnAction(e -> confirmFkPreview());
        Button cancel = new Button("Esc Cancel");
        cancel.setOnAction(e -> cancelFkPreview());
        buttons.getChildren().addAll(swap, create, cancel);
        box.getChildren().addAll(title, source, target, direction, keepPinned, validation, buttons);
        return box;
    }

    private Label chip(String role, String value) {
        Label label = new Label(role + ": " + value);
        label.setStyle("-fx-background-color: " + PANEL_ELEVATED + "; -fx-background-radius: 10; -fx-padding: 3 7; -fx-text-fill: " + TEXT_PRIMARY + ";");
        return label;
    }

    private void drawFkPreview(GraphicsContext g) {
        if (activeFkPreview == null) return;
        Rect source = activeDiagram.getCanvasState().getBoundsByElementId().get(activeFkPreview.getSourceTableId());
        Rect target = activeDiagram.getCanvasState().getBoundsByElementId().get(activeFkPreview.getTargetTableId());
        if (source == null || target == null) return;
        g.save();
        g.setStroke(Color.web(WARNING));
        g.setLineWidth(2.5);
        g.setLineDashes(8, 6);
        g.strokeLine(source.center().x(), source.center().y(), target.center().x(), target.center().y());
        g.setLineDashes(0);
        drawPreviewChip(g, source.center().x() - 38, source.center().y() - 24, "SOURCE");
        drawPreviewChip(g, target.center().x() - 38, target.center().y() - 24, "TARGET");
        g.restore();
    }

    private void drawPreviewChip(GraphicsContext g, double x, double y, String text) {
        g.setFill(Color.web(WARNING));
        g.fillRoundRect(x, y, 76, 22, 10, 10);
        g.setStroke(Color.web(PK_COLOR));
        g.strokeRoundRect(x, y, 76, 22, 10, 10);
        g.setFill(Color.web(APP_BG));
        g.setFont(uiFont(10));
        g.fillText(text, x + 14, y + 15);
    }

    private String fkPreviewContextLine(FkPreview preview) {
        List<String> errors = fkPreviewErrors(preview);
        if (!errors.isEmpty()) return "FK preview blocked: " + errors.getFirst() + " • Esc Cancel";
        List<String> warnings = fkPreviewWarnings(preview);
        String suffix = warnings.isEmpty() ? "" : " • Warning: " + warnings.getFirst();
        String keep = preview.isKeepTargetPinnedAfterCreate() ? " • Keep target pinned" : "";
        return "FK preview: Source " + tableName(preview.getSourceTableId()) + "." + columnName(preview.getSourceTableId(), preview.getSourceColumnId())
                + " → Target " + tableName(preview.getTargetTableId()) + "." + columnName(preview.getTargetTableId(), preview.getTargetColumnId())
                + " • X Swap • Enter Create • Esc Cancel" + keep + suffix;
    }

    private String fkTargetSelectionContextLine(FkPreview preview) {
        return "FK target select: Source " + tableName(preview.getSourceTableId()) + "." + columnName(preview.getSourceTableId(), preview.getSourceColumnId())
                + " → Target " + tableName(preview.getTargetTableId()) + "." + columnName(preview.getTargetTableId(), preview.getTargetColumnId())
                + " • Arrows/Home/End choose target • Enter Create • Esc Cancel";
    }

    private String fkPreviewValidationText(FkPreview preview) {
        List<String> errors = fkPreviewErrors(preview);
        List<String> warnings = fkPreviewWarnings(preview);
        if (errors.isEmpty() && warnings.isEmpty()) return "✓ FK preview is valid";
        StringBuilder text = new StringBuilder();
        errors.forEach(error -> text.append("✕ ").append(error).append('\n'));
        warnings.forEach(warning -> text.append("⚠ ").append(warning).append('\n'));
        if (!warnings.isEmpty() && errors.isEmpty()) text.append("Enter still creates the FK; Esc cancels.");
        return text.toString().trim();
    }

    private List<String> fkPreviewErrors(FkPreview preview) {
        Optional<DbTable> source = activeDiagram.findTable(preview.getSourceTableId());
        Optional<DbTable> target = activeDiagram.findTable(preview.getTargetTableId());
        if (source.isEmpty() || target.isEmpty()) return List.of("source or target table is missing");
        Optional<DbColumn> sourceColumn = source.get().findColumn(preview.getSourceColumnId());
        Optional<DbColumn> targetColumn = target.get().findColumn(preview.getTargetColumnId());
        if (sourceColumn.isEmpty() || targetColumn.isEmpty()) return List.of("source or target column is missing");
        if (preview.getSourceTableId().equals(preview.getTargetTableId()) && preview.getSourceColumnId().equals(preview.getTargetColumnId())) {
            return List.of("source and target are the same column");
        }
        return List.of();
    }

    private List<String> fkPreviewWarnings(FkPreview preview) {
        if (!fkPreviewErrors(preview).isEmpty()) return List.of();
        DbTable source = activeDiagram.findTable(preview.getSourceTableId()).orElseThrow();
        DbTable target = activeDiagram.findTable(preview.getTargetTableId()).orElseThrow();
        DbColumn sourceColumn = source.findColumn(preview.getSourceColumnId()).orElseThrow();
        DbColumn targetColumn = target.findColumn(preview.getTargetColumnId()).orElseThrow();
        java.util.ArrayList<String> warnings = new java.util.ArrayList<>();
        if (!sourceColumn.getType().equalsIgnoreCase(targetColumn.getType())) {
            warnings.add("source type " + sourceColumn.getType() + " differs from target type " + targetColumn.getType());
        }
        if (!targetColumn.isPrimaryKey() && !targetColumn.isUnique()) {
            warnings.add("target column is not PK or unique; relation meaning may be ambiguous");
        }
        return warnings;
    }

    private String tableName(String tableId) {
        return activeDiagram == null ? tableId : activeDiagram.findTable(tableId).map(DbTable::getName).orElse(tableId);
    }

    private String columnName(String tableId, String columnId) {
        if (activeDiagram == null) return columnId;
        return activeDiagram.findTable(tableId).flatMap(table -> table.findColumn(columnId)).map(DbColumn::getName).orElse(columnId);
    }

    private void appendValidationSummary(StringBuilder text, ValidationResult validation) {
        text.append("\nValidation:\n");
        if (validation.issues().isEmpty()) {
            text.append(" ✓ No issues\n");
            return;
        }
        for (ValidationIssue issue : validation.issues()) {
            text.append(" ").append(issue.severity() == ValidationIssue.Severity.ERROR ? "✕" : "⚠")
                    .append(" [").append(issue.code()).append("] ")
                    .append(issue.message())
                    .append(" (").append(issue.elementId()).append(")\n");
        }
    }

    private void updateContextAfterRedraw(ValidationResult validation) {
        if (context == null || !isContextSelectionTip()) return;
        if (activeFkPreview != null) {
            context.setText(fkPreviewContextLine(activeFkPreview));
            return;
        }
        if (relationPin.isPinned()) {
            context.setText(relationPinContextLine());
            return;
        }
        context.setText(contextLineResolver.resolve(
                Optional.empty(),
                Optional.empty(),
                relationPin,
                Optional.empty(),
                Optional.empty(),
                Optional.of(validation),
                "Ready"
        ));
    }

    private boolean isContextSelectionTip() {
        String value = context.getText();
        return value == null || value.isBlank()
                || value.equals("Ready")
                || value.startsWith("Focus:")
                || value.startsWith("Validation:")
                || value.startsWith("Relation pin:")
                || value.startsWith("FK preview:")
                || value.startsWith("Selected ")
                || value.startsWith("Column depth:")
                || value.startsWith("Table depth:");
    }

    private String relationPinContextLine() {
        if (activeDiagram == null || !relationPin.isPinned()) return "Ready";
        Optional<DbTable> table = relationPin.targetTableId().flatMap(activeDiagram::findTable);
        String tableName = table.map(DbTable::getName).orElse(relationPin.targetTableId().orElse("?"));
        String object = relationPin.targetColumnId()
                .flatMap(columnId -> table.flatMap(t -> t.findColumn(columnId)).map(column -> tableName + "." + column.getName()))
                .orElse(tableName);
        return "Relation pin: " + object + " • Space A F create FK • Space U clear";
    }

    private void pruneMissingRelationPin() {
        if (!relationPin.isPinned() || activeDiagram == null) return;
        Optional<DbTable> table = relationPin.targetTableId().flatMap(activeDiagram::findTable);
        if (table.isEmpty()) {
            relationPin.clear();
            if (context != null) context.setText("Relation pin cleared: pinned table was removed");
            return;
        }
        if (relationPin.targetColumnId().isPresent() && table.get().findColumn(relationPin.targetColumnId().get()).isEmpty()) {
            relationPin.clear();
            if (context != null) context.setText("Relation pin cleared: pinned column was removed");
        }
    }

    private Optional<String> firstTableId() {
        return activeDiagram.getTables().stream().findFirst().map(DbTable::getId);
    }

    private Optional<String> firstParticipantId() {
        return activeDiagram.getParticipants().stream().findFirst().map(SequenceParticipant::getId);
    }

    private String factoryId(String prefix) {
        return prefix + "-" + System.nanoTime();
    }
}
