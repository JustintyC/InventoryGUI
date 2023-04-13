# **Gaming Inventory GUI**

## Description
This application provides a GUI that can potentially be 
implemented into a game that requires an inventory storage 
system. As someone who has plenty of experience in video games,
their development has given me great interest. This project 
will aim to provide me with experience in the development of
GUI, as well as potentially providing others with inspiration
on how to develop their own inventory system.

## User Stories
### As a user, I want to be able to:

- Clearly view the inventory
- Add and remove items from inventory
- Create new items that can be added into the inventory
- Manually re-order items in inventory
- Have stackable and non-stackable items
- Automatically organize inventory 
- Load save slots or create a new one
- Manually save data
- Option to save or not save data when quitting

TAs please grade the user stories marked with a *

### Phase 3: Instructions for Grader

- You can generate the first required action related to adding Xs to a Y by pressing the "Add item" button on the 
menubar, entering a number between 1 and 10 for the item ID, and pressing add
- You can generate the second required action related to adding Xs to a Y by left/right clicking on any added item (this
picks up the item) and left/right clicking on any open spot (or spot with the same item) to drop them item in
- You can locate my visual component by looking at the background and the item icons
- You can save the state of my application by pressing options, pressing save, and selecting a save slot
- You can reload the state of my application by pressing options, pressing load, and selecting a save slot


### Phase 4: Task 2

Hand cleared

Stack count at slot 0 increased by 1

Stack count at slot 0 increased by 1

Stack count at slot 0 increased by 1

Stack count at slot 0 increased by 1

Stack count at slot 1 increased by 1

Stack count at slot 1 increased by 1

Stack count at slot 1 increased by 1

Stack count at slot 2 increased by 1

Stack count at slot 3 increased by 1

Stack count at slot 1 decreased by 3

3 item(s) picked up at slot 1

Stack count at slot 6 increased by 3

3 item(s) taken from hand

Stack count at slot 0 decreased by 4

4 item(s) picked up at slot 0

Stack count at slot 10 increased by 4

4 item(s) taken from hand

Stack count at slot 10 decreased by 2

2 item(s) picked up at slot 10

Stack count at slot 11 increased by 2

2 item(s) taken from hand

Stack count at slot 11 decreased by 1

1 item(s) picked up at slot 11

Stack count at slot 5 increased by 1

1 item(s) taken from hand

Stack count at slot 10 decreased by 1

1 item(s) picked up at slot 10

Stack count at slot 0 increased by 1

1 item(s) taken from hand

Stack count at slot 6 decreased by 3

3 item(s) picked up at slot 6

Stack in hand swapped with stack in slot 10

Stack in hand swapped with stack in slot 2

Stack count at slot 19 increased by 1

1 item(s) taken from hand

Stack count at slot 0 decreased by 1

Stack count at slot 0 increased by 1

Stack count at slot 2 decreased by 1

Stack count at slot 0 increased by 1

Stack count at slot 3 decreased by 1

Stack count at slot 1 increased by 1

Stack count at slot 5 decreased by 1

Stack count at slot 0 increased by 1

Stack count at slot 10 decreased by 3

Stack count at slot 2 increased by 3

Stack count at slot 11 decreased by 1

Stack count at slot 0 increased by 1

Stack count at slot 19 decreased by 1

Stack count at slot 3 increased by 1

Inventory organized

Stack count at slot 2 decreased by 3

3 item(s) picked up at slot 2

Hand cleared

Stack count at slot 0 decreased by 4

4 item(s) picked up at slot 0

Hand cleared

### Phase 4: Task 3

- Refactoring portions of my GUI into different classes would make it easier to read, debug, and improve. This would
also allow me and others to understand how the GUI works just by looking at the UML diagram. For example, I can have
different menus be separated into different classes such as InventoryGUI, SaveLoadScreen, and MenuBar.
- I could define an abstract class/interface that is extended/implemented by Inventory and Hand to reduce
code duplication. Methods that place/remove items from either object may be duplicates and can therefore be refactored
out.
- I could refactor out unnecessary code that checks for equality of two items by redefining equals for the Slot and 
Item classes. This would remove the need for me to extract the item's ID from the item via a getter before performing 
comparisons.
- Applying design patterns learned in class (singleton and iterable in particular) would remove the need of extra pieces 
of data that I made to compensate for the lack of such patterns. For example, being able to iterate over an Inventory
object instead of needing a getter to return a list would greatly simplify code. Having a universal Inventory object 
would also remove the need of passing the inventory I am working with into every class as a parameter.


