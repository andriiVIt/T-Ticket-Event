package gui.util;

import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;

public class BlurEffectUtil {public static void applyBlurEffect(Node node, double radius) {
    node.setEffect(new GaussianBlur(radius));
}

    public static void removeBlurEffect(Node node) {
        node.setEffect(null);
    }
}
