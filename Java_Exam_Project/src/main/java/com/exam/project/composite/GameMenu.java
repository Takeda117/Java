package com.exam.project.composite;

import com.exam.project.security.InputValidator;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * GameMenu - Composite pattern implementation for game menus
 *
 * This class can contain both menu items and submenus,
 * creating a tree structure of menus
 */
public class GameMenu implements MenuComponent {

    private String title;
    private List<MenuComponent> menuItems;
    private Scanner scanner;

    /**
     * Constructor for creating a menu
     */
    public GameMenu(String title) {
        this.title = title;
        this.menuItems = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Add a menu item or submenu
     */
    @Override
    public void add(MenuComponent component) {
        menuItems.add(component);
    }

    /**
     * Remove a menu item
     */
    @Override
    public void remove(MenuComponent component) {
        menuItems.remove(component);
    }

    /**
     * Display and execute the menu
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

            // Determina il numero massimo di opzioni
            int maxOptions = menuItems.size();

            // Controlla se questo menu ha l'opzione 0 visibile
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
            } else if (choice > 0 && choice <= menuItems.size()) {
                // Execute the selected menu item
                MenuComponent selected = menuItems.get(choice - 1);
                selected.execute();

                // Se l'ultima opzione è "Torna al..." e l'utente l'ha selezionata, esci dal menu
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
     * Display the menu options
     */
    @Override
    public void display() {
        System.out.println("\n=== " + title.toUpperCase() + " ===");

        for (int i = 0; i < menuItems.size(); i++) {
            System.out.print((i + 1) + ". ");
            menuItems.get(i).display();
        }

        // Mostra l'opzione di uscita appropriata in base al tipo di menu
        if (title.contains("Menu Principale") || title.equals("RPG Adventure Game - Menu Principale")) {
            System.out.println("0. Exit");
        } else if (title.contains("Menu Personaggio") || 
                   title.contains("Esplora Dungeon") || 
                   title.contains("Inventario")) {
            // Non mostrare l'opzione 0 per questi menu, dato che hanno già opzioni per tornare al menu precedente
        } else {
            System.out.println("0. Indietro");
        }
    }

    /**
     * Get the menu title
     */
    @Override
    public String getName() {
        return title;
    }
}