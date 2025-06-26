package java.composite;

/**
 * MenuComponent - Base interface for the Composite pattern
 * <p>
 * This interface defines the common operations for both
 * individual menu items and composite menus. It serves as the
 * component interface in the Composite pattern, allowing clients
 * to treat individual objects and compositions uniformly.
 * </p>
 */
public interface MenuComponent {

    /**
     * Executes the action associated with this menu component
     * <p>
     * For leaf nodes (MenuItem), this executes the specific action.
     * For composite nodes (GameMenu), this displays the menu and handles user input.
     * </p>
     */
    void execute();

    /**
     * Displays the menu component
     * <p>
     * For leaf nodes (MenuItem), this displays the menu item name.
     * For composite nodes (GameMenu), this displays the entire menu structure.
     * </p>
     */
    void display();
    
    /**
     * Gets the name of this menu component
     * 
     * @return The name of the menu component
     */
    String getName();
    
    /**
     * Adds a component to this menu component
     * <p>
     * Default implementation for leaf nodes throws an exception
     * since leaf nodes cannot contain other components.
     * </p>
     * 
     * @param component The component to add
     * @throws UnsupportedOperationException If called on a leaf node
     */
    default void add(MenuComponent component) {
        throw new UnsupportedOperationException("Cannot add to a leaf");
    }
    
    /**
     * Removes a component from this menu component
     * <p>
     * Default implementation for leaf nodes throws an exception
     * since leaf nodes cannot contain other components.
     * </p>
     * 
     * @param component The component to remove
     * @throws UnsupportedOperationException If called on a leaf node
     */
    default void remove(MenuComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a leaf");
    }
}
