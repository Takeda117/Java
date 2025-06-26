package composite;

/**
 * MenuItem - Leaf node in the Composite pattern
 *
 * Represents a single menu item that performs an action
 */
public class MenuItem implements MenuComponent {
    
    private String name;
    private MenuAction action;
    
    /**
     * Constructor for creating a menu item
     */
    public MenuItem(String name, MenuAction action) {
        this.name = name;
        this.action = action;
    }
    
    /**
     * Execute the action associated with this menu item
     */
    @Override
    public void execute() {
        if (action != null) {
            action.execute();
        }
    }
    
    /**
     * Display the menu item
     */
    @Override
    public void display() {
        System.out.println(name);
    }
    
    /**
     * Get the name of this menu item
     */
    @Override
    public String getName() {
        return name;
    }
}
