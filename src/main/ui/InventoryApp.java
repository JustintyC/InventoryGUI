package ui;

import exceptions.InvalidItemIDException;
import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.uiexceptions.InvalidSaveSlotException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

// inventory text UI that handles inputs and produces corresponding information
public class InventoryApp extends JFrame implements WindowListener {
    private Scanner scanner = new Scanner(System.in); // Scanner from B04-SimpleCalculatorStaterLecLab
    private Inventory inventory;
    private Hand hand;
    private Map<Integer, JButton> buttonMap;

    // Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private static final String JSON_STORE1 = "./data/inventory1.json";
    private static final String JSON_STORE2 = "./data/inventory2.json";
    private static final String JSON_STORE3 = "./data/inventory3.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private Boolean isSaving;
    JDesktopPane gameScreen;
    JInternalFrame inventoryGUI;
    JInternalFrame saveOrLoad;
    JInternalFrame saveLoadScreen;
    JMenuBar menuBar;


    // EFFECTS: Constructs an instance of inventory and runs application
    public InventoryApp() {
        inventory = new Inventory();
        hand = new Hand();
        buttonMap = new HashMap();
        isSaving = true;
        initializeItemBank();

        jsonWriter = new JsonWriter(JSON_STORE1);
        jsonReader = new JsonReader(JSON_STORE1);

        runGUI();
        //runInventoryApp();
    }

    // EFFECTS: Initializes GUI
    // Based on https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
    private void runGUI() {
        gameScreenSetup();
        setContentPane(gameScreen);
        setTitle("A game with inventory");
        setSize(WIDTH, HEIGHT);

        pauseScreenSetup();
        inventoryGuiSetup();
        buttonsSetup();
        menuBarSetup();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        centerOnScreen();
        setVisible(true);

        addWindowListener(this);

        inventoryGUI.pack();
    }

    // EFFECTS: sets up game screen (not the inventory GUI)
    private void gameScreenSetup() {
        gameScreen = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage img = ImageIO.read(new File("data/background.png"));
                    // background source: the incredible artistic skills of ChatGPT
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }
        };
        gameScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    hand.clearHand();
                }
            }
        });
    }

    // EFFECTS: sets up pause screen for saving and loading
    public void pauseScreenSetup() {
        saveOrLoad = new JInternalFrame();
        saveOrLoad.setSize(375, 150);
        saveOrLoad.setLayout(new GridLayout(2, 0));
        saveOrLoad.setLocation(WIDTH / 4, HEIGHT / 4);
        saveOrLoad.setVisible(false);
        gameScreen.add(saveOrLoad);

        JPanel saveOrLoadPanel = new JPanel();
        saveOrLoadPanel.setLayout(new GridLayout());
        saveOrLoad.add(saveOrLoadPanel, BorderLayout.NORTH);

        JButton saveButton = saveLoadButtonSetup("Save");
        JButton loadButton = saveLoadButtonSetup("Load");

        saveOrLoadPanel.add(saveButton);
        saveOrLoadPanel.add(loadButton);

        cancelButtonSetup();
    }

    // REQUIRES: name is either "Save" or "Load"
    // EFFECTS: sets up a button on the saving/loading option screen
    private JButton saveLoadButtonSetup(String name) {
        JButton thisButton = new JButton(name);
        thisButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (Objects.equals(name, "Load")) {
                        if (isSaving) {
                            isSaving = false;
                        }
                        saveLoadScreenSetup();
                    } else if (Objects.equals(name, "Save")) {
                        if (!isSaving) {
                            isSaving = true;
                        }
                        saveLoadScreenSetup();
                    }
                    saveLoadScreen.setVisible(true);
                    saveOrLoad.setVisible(false);
                }
            }
        });
        return thisButton;
    }

    // EFFECTS: sets up saving/loading screen where user can choose which slot to save/load into
    private void saveLoadScreenSetup() {
        saveLoadScreen = new JInternalFrame();
        saveLoadScreen.setLayout(new GridLayout());
        saveLoadScreen.setLocation(WIDTH / 4, HEIGHT / 4);
        saveLoadScreen.setSize(375, 150);
        JButton slot1 = new JButton("1");
        JButton slot2 = new JButton("2");
        JButton slot3 = new JButton("3");
        if (isSaving) {
            makeButtonsSave(slot1, slot2, slot3);
        } else {
            makeButtonsLoad(slot1, slot2, slot3);
        }
        saveLoadScreen.add(slot1);
        saveLoadScreen.add(slot2);
        saveLoadScreen.add(slot3);
        gameScreen.add(saveLoadScreen);
    }

    // EFFECTS: makes the 3 buttons on the saving/loading screen save current instance
    private void makeButtonsSave(JButton slot1, JButton slot2, JButton slot3) {
        slot1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buttonSave(JSON_STORE1);
            }
        });
        slot2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buttonSave(JSON_STORE2);
            }
        });
        slot3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buttonSave(JSON_STORE3);
            }
        });
    }

    // EFFECTS: saving function of a save slot button
    private void buttonSave(String jsonStore1) {
        jsonWriter = new JsonWriter(jsonStore1);
        try {
            jsonWriter.open();
            jsonWriter.write(inventory);
            jsonWriter.close();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException();
        }
        saveLoadScreen.setVisible(false);
        inventoryGUI.setVisible(true);
        menuBar.setVisible(true);
    }

    // EFFECTS: makes the 3 buttons on the saving/loading screen load requested instance
    private void makeButtonsLoad(JButton slot1, JButton slot2, JButton slot3) {
        slot1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buttonLoad(JSON_STORE1);
            }
        });
        slot2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buttonLoad(JSON_STORE2);
            }
        });
        slot3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buttonLoad(JSON_STORE3);
            }
        });
    }

    // EFFECTS: loading function of a load slot button
    private void buttonLoad(String jsonStore1) {
        jsonReader = new JsonReader(jsonStore1);
        try {
            inventory = jsonReader.read();
        } catch (IOException ex) {
            throw new RuntimeException();
        }
        saveLoadScreen.setVisible(false);
        inventoryGUI.setVisible(true);
        menuBar.setVisible(true);
        updateInventoryGUI();
    }

    // EFFECTS: sets up cancel button in save/load options screen
    private void cancelButtonSetup() {
        JPanel saveOrLoadCancel = new JPanel();
        saveOrLoadCancel.setLayout(new BorderLayout());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    saveOrLoad.setVisible(false);
                    inventoryGUI.setVisible(true);
                    menuBar.setVisible(true);
                }
            }
        });
        saveOrLoadCancel.add(cancelButton);
        saveOrLoad.add(saveOrLoadCancel, BorderLayout.SOUTH);
    }

    // EFFECTS: sets up menu bar
    public void menuBarSetup() {
        menuBar = new JMenuBar();
        JMenu options = optionsSetUp();
        JMenu addItemGUI = new JMenu("Spawn Item");

        addItemGuiSetup(addItemGUI);

        menuBar.add(options);
        menuBar.add(addItemGUI);
        menuBar.setVisible(true);
        setJMenuBar(menuBar);
    }

    // EFFECTS: sets up the window that prompts user to enter an item ID to add an item to inventory
    private void addItemGuiSetup(JMenu addItemGUI) {
        addItemGUI.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JInternalFrame addItemFrame = new JInternalFrame("Add Item");
                addItemFrame.setLayout(new BorderLayout());

                JTextField inputBox = inputPanelSetup(addItemFrame);

                JPanel buttonPanel = new JPanel();
                JButton addButton = new JButton("Add");
                addItemButtonAction(addItemFrame, inputBox, addButton);
                JButton cancelButton = new JButton("Close");
                cancelButton.addActionListener(e12 -> addItemFrame.dispose());
                buttonPanel.add(addButton);
                buttonPanel.add(cancelButton);
                addItemFrame.add(buttonPanel, BorderLayout.SOUTH);

                addItemFrame.pack();
                addItemFrame.setVisible(true);
                gameScreen.add(addItemFrame);
            }
        });
    }

    // EFFECTS: setups input panel in the addItem GUI
    private JTextField inputPanelSetup(JInternalFrame addItemFrame) {
        JPanel inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(200, 50));
        JLabel inputLabel = new JLabel("Enter an item ID:");
        JTextField inputBox = new JTextField(10);
        inputPanel.add(inputLabel);
        inputPanel.add(inputBox);
        addItemFrame.add(inputPanel, BorderLayout.CENTER);
        return inputBox;
    }

    // EFFECTS: functionality of the Add button in the addItem GUI; opens error window if input is not a positive
    //          nonzero integer
    private void addItemButtonAction(JInternalFrame addItemFrame, JTextField inputField, JButton addButton) {
        addButton.addActionListener(e1 -> {
            String input = inputField.getText();
            try {
                int inputInt = Integer.parseInt(input);
                if (inputInt > 0) {
                    doAddItemGUI(inputInt, addItemFrame);
                } else {
                    JOptionPane.showMessageDialog(addItemFrame, "Please enter a positive nonzero integer.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addItemFrame, "Please enter an integer.");
            }
        });
    }

    // EFFECTS: adds requested item in inventory; opens error window if inventory is full or given ID doesn't exist
    private void doAddItemGUI(int id, JInternalFrame addItemFrame) {
        ItemBank itemBank = inventory.getItemBank();

        try {
            Item item = itemBank.findItem(id);
            Boolean inserted = false;
            for (int i = 0; i < inventory.getListSize(); i++) {
                if (inventory.insertItem(i, item)) {
                    inserted = true;
                    updateSlotGUI(i);
                    break;
                }
            }
            if (!inserted) {
                JOptionPane.showMessageDialog(addItemFrame, "Inventory full.");
            }
        } catch (InvalidItemIDException e) {
            JOptionPane.showMessageDialog(addItemFrame, "Cannot find item with given item ID.");
        }
    }

    // EFFECTS: sets up the save/load options screen
    private JMenu optionsSetUp() {
        JMenu options = new JMenu("Options");
        options.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inventoryGUI.setVisible(false);
                saveOrLoad.setVisible(true);
                menuBar.setVisible(false);
            }
        });
        return options;
    }

    // EFFECTS: sets up buttons ONLY on the inventoryGUI JInternalFrame
    private void buttonsSetup() {
        JPanel buttonPanel = initializeInventoryGUI();
        inventoryGUI.add(buttonPanel, BorderLayout.SOUTH);

        JPanel extraInventoryButtonsPanel = new JPanel();
        extraInventoryButtonsPanel.setLayout(new GridLayout());
        organizeButtonSetup(extraInventoryButtonsPanel);
        inventoryGUI.add(extraInventoryButtonsPanel, BorderLayout.NORTH);

    }

    // EFFECTS: sets up the organize button so that it organizes the inventory
    private void organizeButtonSetup(JPanel extraInventoryButtonsPanel) {
        JButton organizeButton = new InventoryToolsButton("Organize", 20, 20);
        organizeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    inventory.organize();
                    updateInventoryGUI();
                }
            }
        });
        extraInventoryButtonsPanel.add(organizeButton);
    }

    // EFFECTS: sets up the inventory GUI
    private void inventoryGuiSetup() {
        inventoryGUI = new JInternalFrame("Inventory", false, false, false, false);
        inventoryGUI.setLayout(new BorderLayout());
        inventoryGUI.setSize(375, 320);
        inventoryGUI.setBackground(Color.DARK_GRAY);
        inventoryGUI.setForeground(Color.LIGHT_GRAY);
        inventoryGUI.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.LIGHT_GRAY));
        inventoryGUI.setLocation(WIDTH / 4, HEIGHT / 4 - 30);
        inventoryGUI.setVisible(true);
        gameScreen.add(inventoryGUI);
    }

    // EFFECTS: Initiates grid with 20 slots for inventory GUI
    private JPanel initializeInventoryGUI() {
        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new GridLayout(0,5));
        buttonPanel.setVisible(true);
        buttonPanel.setSize(40, 70);

        for (int i = 0; i < inventory.getListSize(); i++) {
            JButton thisButton = new SlotButton(this, i);
            buttonPanel.add(thisButton);
            buttonMap.put(i, thisButton);
        }

        updateInventoryGUI();
        return buttonPanel;
    }

    // EFFECTS: updates the GUI with its proper icons and stack numbers
    private void updateInventoryGUI() {
        for (int i = 0; i < inventory.getListSize(); i++) {
            updateSlotGUI(i);
        }
    }

    // EFFECTS: updates icon of specific slot
    private void updateSlotGUI(int i) {
        JButton buttonAtI = buttonMap.get(i);
        String iconURL = getSlotImgUrl(i);

        BufferedImage image;
        try {
            image = ImageIO.read(new File(iconURL));
        } catch (IOException e) {
            try {
                image = ImageIO.read(new File("data/gregor.jpg"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        assert image != null;

        ImageIcon icon = new ImageIcon(image);
        buttonAtI.setIcon(icon);
        buttonAtI.setText(String.valueOf(inventory.getNthSlot(i).getStackCount()));
    }

    // EFFECTS: handles case where user left clicks on slot
    //          empty hand -> holds full stack
    //          occupied hand -> drops full stack
    public void handleLeftClick(int slotNum) {
        Boolean isTargetEmpty = (inventory.getNthSlot(slotNum) instanceof EmptySlot);
        int stackCountAtNthSlot = inventory.getNthSlot(slotNum).getStackCount();

        if (hand.isEmpty()) {
            if (!isTargetEmpty) {
                hand.hold(inventory, slotNum, stackCountAtNthSlot);
            }
        } else {
            int stackCountHand = hand.getHeldAmount();
            hand.drop(inventory, slotNum, stackCountHand);
        }

        updateSlotGUI(slotNum);
    }

    // EFFECTS: handles case where user right clicks on slot
    //          empty hand -> holds half stack (1 if target stack = 1)
    //          occupied hand -> attempts to drop 1 item from hand (nothing will happen if target is occupied)
    public void handleRightClick(int slotNum) {
        int stackCountAtNthSlot = inventory.getNthSlot(slotNum).getStackCount();

        if (hand.isEmpty()) {
            if (stackCountAtNthSlot == 1) {
                hand.hold(inventory, slotNum, 1);
            } else {
                hand.hold(inventory, slotNum, (int) Math.floor(stackCountAtNthSlot / 2));
            }
        } else {
            hand.drop(inventory, slotNum, 1);
        }

        updateSlotGUI(slotNum);
    }

    // EFFECTS: returns image URL of given slot
    private String getSlotImgUrl(int i) {
        int slotID = inventory.getNthSlot(i).getItemID();
        String url = "data/id" + slotID + "_icon.png";
        return url;
    }



    // EFFECTS: Helper to center main application window on desktop
    // Based on https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
    private void centerOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }


    // EFFECTS: Initializes preset items for user
    private void initializeItemBank() {
        inventory.getItemBank().add(new Item("UBC Card", 1, 1));
        inventory.getItemBank().add(new Item("Pencil", 2, 20));
        inventory.getItemBank().add(new Item("Ball", 3, 10));
        inventory.getItemBank().add(new Item("Sensible Footwear", 4, 1));
        inventory.getItemBank().add(new Item("Laptop", 5, 1));
        inventory.getItemBank().add(new Item("Phone", 6, 10));
        inventory.getItemBank().add(new Item("Credit Card", 7, 5));
        inventory.getItemBank().add(new Item("Vodka", 8, 1));
        inventory.getItemBank().add(new Item("Keys", 9, 5));
        inventory.getItemBank().add(new Item("Suspicious Papers", 10, 20));
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        for (Event event : EventLog.getInstance()) {
            System.out.println(event.getDescription());
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

//    // EFFECTS: Initiates an inventory from a video game with console-based user interaction
//    private void runInventoryApp() {
//        String commandList = "AddItem, RemoveItem, CreateItem, Hold, Drop, SlotInfo, Organize, ViewInventory, Save, "
//                + "Load, Quit";
//        System.out.println("Available commands: " + commandList);
//        while (true) {
//            updateInventoryGUI();
//            System.out.println("-------------------------------------");
//            System.out.println(inventory.textView());
//            System.out.println(hand.handTextView());
//            System.out.println("What would you like to do?");
//            String instruction = scanner.next();
//
//            if (instruction.equals("Quit")) {
//                System.out.println("Quitting...");
//                break;
//            } else {
//                handleInput(instruction, inventory, hand, commandList);
//            }
//        }
//    }
//
//    // EFFECTS: Produces results according to inputs.
//    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
//    private void handleInput(String input, Inventory inventory, Hand hand, String commandList) {
//        switch (input) {
//            case "AddItem":
//                doAddItem(inventory);
//                break;
//            case "RemoveItem":
//                doRemoveItem(inventory);
//                break;
//            case "Hold":
//                doHoldItem(inventory, hand);
//                break;
//            case "Drop":
//                doDropItem(inventory, hand);
//                break;
//            case "CreateItem":
//                doCreateItem(inventory);
//                break;
//            case "SlotInfo":
//                doSlotInfo(inventory);
//                break;
//            case "Organize":
//                inventory.organize();
//                System.out.println("Inventory organized.");
//                break;
//            case "ViewInventory":
//                System.out.println(inventory.textView());
//                break;
//            case "Save":
//                try {
//                    doSave();
//                } catch (InvalidSaveSlotException e) {
//                    System.out.println("Invalid save slot.");
//                }
//                break;
//            case "Load":
//                try {
//                    doLoad();
//                } catch (InvalidSaveSlotException e) {
//                    System.out.println("Invalid save slot.");
//                }
//                break;
//            default:
//                System.out.println("Unknown command. Please enter one of: " + commandList);
//                break;
//        }
//    }
//
//    // MODIFIES: inventory
//    // EFFECTS: prompts user and adds an item to inventory
//    private void doAddItem(Inventory inventory) {
//        ItemBank itemBank = inventory.getItemBank();
//
//        System.out.println("Please enter slot number to add item into: ");
//        int slot = parseInt(scanner.next()) - 1;
//        System.out.println("Please enter item ID of the item you wish to add: ");
//        int id = parseInt(scanner.next());
//        try {
//            Item item = itemBank.findItem(id);
//            if (!inventory.insertItem(slot, item)) {
//                System.out.println("Cannot add item to given slot.");
//            } else {
//                System.out.println("Item successfully added.");
//            }
//        } catch (InvalidItemIDException e) {
//            System.out.println("Cannot find item with given item ID.");
//        } catch (IndexOutOfBoundsException e) {
//            System.out.println("Invalid slot number.");
//        }
//    }
//
//    // MODIFIES: inventory
//    // EFFECTS: prompts user and removes an item from inventory
//    private void doRemoveItem(Inventory inventory) {
//        try {
//            System.out.println("Please enter slot number to remove item from: ");
//            int slotNum = parseInt(scanner.next()) - 1;
//            if (inventory.getNthSlot(slotNum) instanceof EmptySlot) {
//                System.out.println("This slot is empty!");
//            } else {
//                Slot slot = inventory.getNthSlot(slotNum);
//                System.out.println("This slot has " + slot.getStackCount() + " of " + slot.getName());
//                System.out.println("Please enter amount of this item to remove: ");
//                int amount = parseInt(scanner.next());
//                if (!inventory.removeItem(slotNum, amount)) {
//                    System.out.println("Not enough items to remove!");
//                } else {
//                    System.out.println("Item successfully removed.");
//                }
//            }
//        } catch (IndexOutOfBoundsException e) {
//            System.out.println("Invalid slot number.");
//        }
//    }
//
//    // MODIFIES: inventory, hand
//    // EFFECTS: prompts user and picks up an item from inventory
//    private void doHoldItem(Inventory inventory, Hand hand) {
//        try {
//            System.out.println("Please enter target slot number: ");
//            int slotNum = parseInt(scanner.next()) - 1;
//            System.out.println("Please enter amount to remove: ");
//            int amount = parseInt(scanner.next());
//            if (!hand.hold(inventory, slotNum, amount)) {
//                System.out.println("Cannot hold items from targeted slot.");
//            }
//        } catch (IndexOutOfBoundsException e) {
//            System.out.println("Invalid slot number.");
//        }
//    }
//
//    // MODIFIES: inventory, hand
//    // EFFECTS: prompts user and drops an item from hand into inventory
//    private void doDropItem(Inventory inventory, Hand hand) {
//
//        try {
//            System.out.println("Please enter target slot number: ");
//            int slotNum = parseInt(scanner.next()) - 1;
//            System.out.println("Please enter amount to drop: ");
//            int amount = parseInt(scanner.next());
//            if (!hand.drop(inventory, slotNum, amount)) {
//                System.out.println("Cannot drop items into targeted slot.");
//            } else {
//                System.out.println("Items successfully dropped.");
//            }
//        } catch (IndexOutOfBoundsException e) {
//            System.out.println("Invalid slot number.");
//        }
//    }
//
//    // EFFECTS: provides slot info of given slot
//    private void doSlotInfo(Inventory inventory) {
//        try {
//            System.out.println("Please enter slot number: ");
//            int slotNum = parseInt(scanner.next()) - 1;
//            Slot slot = inventory.getNthSlot(slotNum);
//            if (slot instanceof EmptySlot) {
//                System.out.println("This slot is empty.");
//            } else {
//                System.out.println("Item name: " + slot.getName());
//                System.out.println("Item ID: " + slot.getItemID());
//                System.out.println("Item amount: " + slot.getStackCount());
//                System.out.println("Max stack size: " + slot.getMaxStackSize());
//            }
//        } catch (IndexOutOfBoundsException e) {
//            System.out.println("Invalid slot number.");
//        }
//    }
//
//    // MODIFIES: inventory.itemBank
//    // EFFECTS: prompts user and creates a new item
//    private void doCreateItem(Inventory inventory) {
//        System.out.println("Please enter the item's name: ");
//        String name = scanner.next();
//        System.out.println("Please enter the item's maximum stack count: ");
//        int maxStack = parseInt(scanner.next());
//        System.out.println(inventory.createItem(name, maxStack));
//        System.out.println("Item successfully created.");
//    }
//
//    // EFFECTS: saves current inventory to a save slot
//    // Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo in WorkRoomApp.saveWorkRoom
//    private void doSave() throws InvalidSaveSlotException {
//        System.out.println("Select save slot: [1] [2] [3]");
//        int saveSlotNum = parseInt(scanner.next());
//        switch (saveSlotNum) {
//            case 1:
//                this.jsonWriter = new JsonWriter(JSON_STORE1);
//                break;
//            case 2:
//                this.jsonWriter = new JsonWriter(JSON_STORE2);
//                break;
//            case 3:
//                this.jsonWriter = new JsonWriter(JSON_STORE3);
//                break;
//            default:
//                throw new InvalidSaveSlotException();
//        }
//        try {
//            jsonWriter.open();
//            jsonWriter.write(inventory);
//            jsonWriter.close();
//            System.out.println("Saved current inventory to slot " + saveSlotNum + ".");
//        } catch (FileNotFoundException e) {
//            System.out.println("Unable to write to file to slot " + saveSlotNum + ".");
//        }
//    }
//
//    // EFFECTS: load inventory from a save slot
//    // Based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo in WorkRoomApp.loadWorkRoom
//    private void doLoad() throws InvalidSaveSlotException {
//        System.out.println("Select save slot: [1] [2] [3]");
//        int saveSlotNum = parseInt(scanner.next());
//        switch (saveSlotNum) {
//            case 1:
//                this.jsonReader = new JsonReader(JSON_STORE1);
//                break;
//            case 2:
//                this.jsonReader = new JsonReader(JSON_STORE2);
//                break;
//            case 3:
//                this.jsonReader = new JsonReader(JSON_STORE3);
//                break;
//            default:
//                throw new InvalidSaveSlotException();
//        }
//        try {
//            inventory = jsonReader.read();
//            System.out.println("Loaded inventory from slot " + saveSlotNum + ".");
//        } catch (IOException e) {
//            System.out.println("Unable to read from slot " + saveSlotNum + ".");
//        }
//    }
}
