package rpg.composite;

import rpg.rpgSecurity.InputValidator;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * GameMenu - Composite pattern implementation for game menus
 * <p>
 * This class can contain both menu items and submenus,
 * creating a tree structure of menus. It serves as the composite
 * node in the Composite pattern, allowing a hierarchical menu system.
 * </p>
 */
public class GameMenu implements MenuComponent {

    private String title;
    private List<MenuComponent> menuItems;
    private Scanner scanner;

    /**
     * Constructor for creating a menu
     * 
     * @param title The title of the menu
     */
    public GameMenu(String title) {
        this.title = title;
        this.menuItems = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Adds a menu item or submenu to this menu
     * 
     * @param component The menu component to add
     */
    @Override
    public void add(MenuComponent component) {
        menuItems.add(component);
    }

    /**
     * Removes a menu item or submenu from this menu
     * 
     * @param component The menu component to remove
     */
    @Override
    public void remove(MenuComponent component) {
        menuItems.remove(component);
    }

    /**
     * Displays and executes the menu
     * <p>
     * This method shows the menu options, handles user input,
     * and executes the selected menu item or submenu.
     * </p>
     */
    @Override
    public void execute() {
        boolean continueMenu = true;

        while (continueMenu) {
            // Display menu
            display();

            // Get user choice
            System.out.print("\nYour choice: ");
            String input = scanner.nextLine();

            // Determine the maximum number of options
            int maxOptions = menuItems.size();

            // Check if this menu has a visible zero option
            boolean hasZeroOption = !(title.contains("Menu Personaggio") || 
                                     title.contains("Esplora Dungeon") || 
                                     title.contains("Inventario"));

            Integer choice = InputValidator.validateMenuChoice(input, maxOptions);

            if (choice == null) {
                System.out.println("Invalid choice! Please try again.");
                continue;
            }

            if (choice == 0 && hasZeroOption) {
                // Exit this menu
                continueMenu = false;
                
                // If this is the main menu and user selects 0, exit the game
                if (title.contains("Menu Principale") || title.equals("RPG Adventure Game - Menu Principale")) {
                    exitGame();
                }
            } else if (choice > 0 && choice <= menuItems.size()) {
                // Execute the selected menu item
                MenuComponent selected = menuItems.get(choice - 1);
                selected.execute();

                // If the last option is "Return to..." and the user selected it, exit the menu
                if (choice == menuItems.size() && 
                    selected.getName() != null && 
                    (selected.getName().contains("Torna al") || 
                     selected.getName().contains("Back to"))) {
                    continueMenu = false;
                }
                
                // If it was an action (not a submenu), we might want to pause
                if (!(selected instanceof GameMenu)) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }
            }
        }
    }

    /**
     * Displays the menu options
     * <p>
     * This method shows the menu title and all available options,
     * including a back/exit option appropriate to the menu type.
     * </p>
     */
    @Override
    public void display() {
        System.out.println("\n=== " + title.toUpperCase() + " ===");

        for (int i = 0; i < menuItems.size(); i++) {
            System.out.print((i + 1) + ". ");
            menuItems.get(i).display();
        }

        // Show the appropriate exit option based on the menu type
        if (title.contains("Menu Principale") || title.equals("RPG Adventure Game - Menu Principale")) {
            System.out.println("0. Exit");
        } else if (!(title.contains("Menu Personaggio") || 
                   title.contains("Esplora Dungeon") || 
                   title.contains("Inventario"))) {
            // Only show "0. Back" for menus that don't already have a return option
            System.out.println("0. Back");
        }

    }

    /**
     * Gets the menu title
     * 
     * @return The title of the menu
     */
    @Override
    public String getName() {
        return title;
    }

    /**
     * Helper method to exit the game
     */
    private void exitGame() {
        System.out.println("\nGrazie per aver giocato!");
        System.exit(0);
    }
}
