package com.exam.project.composite;

/**
 * MenuComponent - Base interface for the Composite pattern
 *
 * This interface defines the common operations for both
 * individual menu items and composite menus
 */
public interface MenuComponent {

    /**
     * Execute the action associated with this menu component
     */
    void execute();

    /**
     * Display the menu component
     */
    void display();
    
    /**
     * Add a component to this menu component
     * Default implementation for leaf nodes
     */
    default void add(MenuComponent component) {
        throw new UnsupportedOperationException("Cannot add to a leaf");
    }
    
    /**
     * Remove a component from this menu component
     * Default implementation for leaf nodes
     */
    default void remove(MenuComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a leaf");
    }
    
    /**
     * Get a child component at the specified index
     * Default implementation for leaf nodes
     */
    default MenuComponent getChild(int index) {
        throw new UnsupportedOperationException("Cannot get child from a leaf");
    }
    
    /**
     * Get the name of this menu component
     */
    String getName();
}
