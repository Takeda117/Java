package rpg.composite;

/**
 * MenuAction - Functional interface for menu actions
 * <p>
 * This interface defines a single method that represents
 * an action to be executed when a menu item is selected.
 * It follows the Command pattern and allows for lambda expressions
 * to be used when creating menu items.
 * </p>
 */
@FunctionalInterface
public interface MenuAction {
    
    /**
     * Executes the action associated with a menu item
     */
    void execute();
}