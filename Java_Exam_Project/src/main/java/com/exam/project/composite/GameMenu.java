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
     * Get a specific menu item
     */
    @Override
    public MenuComponent getChild(int index) {
        if (index >= 0 && index < menuItems.size()) {
            return menuItems.get(index);
        }
        return null;
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
            Integer choice = InputValidator.validateMenuChoice(input, menuItems.size());

            if (choice == null) {
                continue;
            }

            if (choice == 0) {
                // Exit this menu
                continueMenu = false;
            } else if (choice > 0 && choice <= menuItems.size()) {
                // Execute the selected menu item
                MenuComponent selected = menuItems.get(choice - 1);
                selected.execute();

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

        System.out.println("0. Back/Exit");
    }

    /**
     * Get the menu title
     */
    public String getTitle() {
        return title;
    }
}