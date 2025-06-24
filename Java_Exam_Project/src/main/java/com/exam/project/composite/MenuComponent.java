package com.exam.project.composite;

/**
 * MenuComponent - Base interface for the Composite pattern
 *
 * This interface defines the common operations for both
 * individual menu items and composite menus
 *
 * The Composite pattern allows us to treat individual items
 * and collections of items uniformly
 */
public interface MenuComponent {

    /**
     * Execute the action associated with this menu component
     * For menu items, this performs an action
     * For menus, this displays the menu and handles navigation
     */
    void execute();

    /**
     * Display the menu component
     * For menu items, this shows the item name
     * For menus, this shows the menu title
     */
    void display();

    /**
     * Add a child component (only meaningful for composite menus)
     * Default implementation throws exception for leaf nodes
     */
    default void add(MenuComponent component) {
        throw new UnsupportedOperationException("Cannot add to a leaf component");
    }

    /**
     * Remove a child component (only meaningful for composite menus)
     * Default implementation throws exception for leaf nodes
     */
    default void remove(MenuComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a leaf component");
    }

    /**
     * Get a child component (only meaningful for composite menus)
     * Default implementation throws exception for leaf nodes
     */
    default MenuComponent getChild(int index) {
        throw new UnsupportedOperationException("Cannot get child from a leaf component");
    }
}
