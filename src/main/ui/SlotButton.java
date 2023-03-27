package ui;

import javax.swing.*;
import java.awt.*;

public class SlotButton extends JButton {

    public SlotButton() {
        super();
        setText("pls");
        setPreferredSize(new Dimension(100, 100));
        setBackground(Color.GRAY);
        setHorizontalTextPosition(SwingConstants.RIGHT);
        setVerticalTextPosition(SwingConstants.BOTTOM);

        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.DARK_GRAY));
        setBorderPainted(true);
    }



}
