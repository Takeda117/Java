package rpg.composite;

/**
 * MenuItem - Leaf node in the Composite pattern
 * <p>
 * Represents a single menu item that performs an action.
 * This class implements the MenuComponent interface and serves
 * as a leaf node in the Composite pattern hierarchy.
 * </p>
 */
public class MenuItem implements MenuComponent {
    
    private String name;
    private MenuAction action;
    
    /**
     * Constructor for creating a menu item
     * 
     * @param name The name of the menu item
     * @param action The action to be executed when this menu item is selected
     */
    public MenuItem(String name, MenuAction action) {
        this.name = name;
        this.action = action;
    }
    
    /**
     * Executes the action associated with this menu item
     * <p>
     * If an action is defined, it will be executed.
     * </p>
     */
    @Override
    public void execute() {
        if (action != null) {
            action.execute();
        }
    }
    
    /**
     * Displays the menu item
     * <p>
     * Prints the name of the menu item to the console.
     * </p>
     */
    @Override
    public void display() {
        System.out.println(name);
    }
    
    /**
     * Gets the name of this menu item
     * 
     * @return The name of the menu item
     */
    @Override
    public String getName() {
        return name;
    }
}
