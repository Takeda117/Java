package com.exam.project.composite;

/**
 * MenuItem - Represents a single menu action (leaf node in Composite pattern)
 *
 * This is the "leaf" component in our Composite pattern structure.
 * It represents an individual menu item that performs a specific action
 * when selected, rather than opening another submenu.
 *
 * The Composite pattern allows us to build complex menu structures
 * where both individual items and entire submenus can be treated
 * the same way through the MenuComponent interface.
 */
public class MenuItem implements MenuComponent {

    private String name;
    private Runnable action;

    /**
     * Constructor for creating a menu item
     *
     * @param name The display name of the menu item
     * @param action The action to perform when selected (using Java 8 lambda)
     */
    public MenuItem(String name, Runnable action) {
        this.name = name;
        this.action = action;
    }

    /**
     * Execute the action associated with this menu item
     * This is called when the user selects this option
     */
    @Override
    public void execute() {
        if (action != null) {
            action.run();
        } else {
            System.out.println("No action defined for: " + name);
        }
    }

    /**
     * Display the menu item name
     * This is called when the parent menu is showing its options
     */
    @Override
    public void display() {
        System.out.println(name);
    }

    /**
     * Get the name of this menu item
     */
    public String getName() {
        return name;
    }
}
