package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SlotButton extends JButton {

    private int associatedSlotNum;
    private InventoryApp inventoryApp;

    public SlotButton(InventoryApp inventoryApp, int associatedSlotNum) {
        super();
        this.associatedSlotNum = associatedSlotNum;
        this.inventoryApp = inventoryApp;
        setPreferredSize(new Dimension(75, 75));
        setBackground(Color.GRAY);
        setHorizontalTextPosition(SwingConstants.RIGHT);
        setVerticalTextPosition(SwingConstants.BOTTOM);

        setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.DARK_GRAY));
        setBorderPainted(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    inventoryApp.handleLeftClick(associatedSlotNum);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    inventoryApp.handleRightClick(associatedSlotNum);
                }
            }

        });
    }



}
