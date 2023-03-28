package ui;

import javax.swing.*;
import java.awt.*;

public class InventoryToolsButton extends JButton {

    public InventoryToolsButton(String text, int width, int height) {
        super();
        setText(text);
        setForeground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.DARK_GRAY);
        setBorderPainted(false);
    }

}
