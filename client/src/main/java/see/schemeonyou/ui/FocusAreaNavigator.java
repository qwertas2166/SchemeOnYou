package see.schemeonyou.ui;

import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class FocusAreaNavigator {
    private FocusAreaNavigator() { }

    public static Optional<Node> firstFocusableInArea(Node areaRoot) {
        if (areaRoot == null || !isAvailable(areaRoot)) return Optional.empty();

        List<Node> descendants = new ArrayList<>();
        if (areaRoot instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                collectFocusableNodes(child, descendants);
            }
        }
        if (!descendants.isEmpty()) return Optional.of(descendants.getFirst());
        return areaRoot.isFocusTraversable() ? Optional.of(areaRoot) : Optional.empty();
    }

    public static List<Node> focusableNodes(Node root) {
        List<Node> nodes = new ArrayList<>();
        collectFocusableNodes(root, nodes);
        return nodes;
    }

    private static void collectFocusableNodes(Node node, List<Node> nodes) {
        if (!isAvailable(node)) return;
        if (node.isFocusTraversable()) nodes.add(node);
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                collectFocusableNodes(child, nodes);
            }
        }
    }

    private static boolean isAvailable(Node node) {
        return node != null && node.isVisible() && !node.isDisabled();
    }
}
