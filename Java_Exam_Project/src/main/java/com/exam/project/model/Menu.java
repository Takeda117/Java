package com.exam.project.model;


import com.exam.project.composite.*;
import com.exam.project.factory.Character;
import com.exam.project.builder.*;
import com.exam.project.factory.*;
import com.exam.project.factoryMonster.MonsterFactory;
import com.exam.project.iterator.*;
import com.exam.project.security.InputValidator;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Menu {

    // Game state
    private static Character currentCharacter = null;
    private static final List<Character> allCharacters = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    // Factories and utilities
    private static final CharacterFactory characterFactory = new CharacterFactory();
    private static final DungeonFactory dungeonFactory = new DungeonFactory();
    private static final DungeonExplorer dungeonExplorer = new DungeonExplorer();

    public static void main(String[] args) {
        System.out.println("=== WELCOME TO THE RPG ADVENTURE GAME ===");
        System.out.println("A text-based role-playing game built with Java\n");

        // Build and execute the main menu using Composite pattern
        GameMenu mainMenu = buildMainMenu();
        mainMenu.execute();

        System.out.println("\nThank you for playing! Goodbye!");
    }

    /**
     * Build the main menu structure using the Composite pattern
     *
     * This method demonstrates how we can create complex menu hierarchies
     * by composing GameMenu and MenuItem objects. Each menu can contain
     * other menus (submenus) or individual actions (menu items).
     */
    private static GameMenu buildMainMenu() {
        // Create the main menu
        GameMenu mainMenu = new GameMenu("Main Menu");

        // Add menu items - each with a name and an action (lambda expression)
        mainMenu.add(new MenuItem("Create New Character", Menu::createCharacter));
        mainMenu.add(new MenuItem("Select Character", Menu::selectCharacter));
        mainMenu.add(new MenuItem("Show Current Character", Menu::showCurrentCharacter));

        // Create and add the Play submenu
        GameMenu playMenu = buildPlayMenu();
        mainMenu.add(playMenu);

        // Add character management submenu
        GameMenu characterMenu = buildCharacterMenu();
        mainMenu.add(characterMenu);

        // Add test menu for development
        GameMenu testMenu = buildTestMenu();
        mainMenu.add(testMenu);

        return mainMenu;
    }

    /**
     * Build the Play menu - available when a character is selected
     */
    private static GameMenu buildPlayMenu() {
        GameMenu playMenu = new GameMenu("Play Game");

        // This menu item checks if a character is selected before allowing actions
        playMenu.add(new MenuItem("Explore Dungeon", () -> {
            if (checkCharacterSelected()) {
                exploreDungeonMenu();
            }
        }));

        playMenu.add(new MenuItem("Train Character", () -> {
            if (checkCharacterSelected()) {
                trainCharacter();
            }
        }));

        playMenu.add(new MenuItem("Visit Shop", () -> {
            if (checkCharacterSelected()) {
                System.out.println("Shop feature coming soon!");
            }
        }));

        playMenu.add(new MenuItem("Manage Inventory", () -> {
            if (checkCharacterSelected()) {
                manageInventory();
            }
        }));

        return playMenu;
    }

    /**
     * Build the Character Management menu
     */
    private static GameMenu buildCharacterMenu() {
        GameMenu characterMenu = new GameMenu("Character Management");

        characterMenu.add(new MenuItem("List All Characters", Menu::listAllCharacters));
        characterMenu.add(new MenuItem("Delete Character", Menu::deleteCharacter));
        characterMenu.add(new MenuItem("Save Character", () -> System.out.println("Save feature coming soon!")));
        characterMenu.add(new MenuItem("Load Character", () -> System.out.println("Load feature coming soon!")));

        return characterMenu;
    }

    /**
     * Build the Test menu for development
     */
    private static GameMenu buildTestMenu() {
        GameMenu testMenu = new GameMenu("Test Features");

        testMenu.add(new MenuItem("Test Battle System", Menu::testBattle));
        testMenu.add(new MenuItem("Test Item System", Menu::testItems));
        testMenu.add(new MenuItem("Show Factory Types", () -> {
            characterFactory.showAvailableTypes();
            System.out.println();
            new MonsterFactory().showAvailableTypes();
        }));

        return testMenu;
    }

    /**
     * Create a new character
     */
    private static void createCharacter() {
        System.out.println("\n=== CREATE NEW CHARACTER ===");

        // Show available types
        characterFactory.showAvailableTypes();

        // Get character type
        System.out.print("\nEnter character type: ");
        String type = scanner.nextLine();

        // Get character name
        System.out.print("Enter character name: ");
        String name = scanner.nextLine();

        // Create the character
        Character newCharacter = characterFactory.createCharacter(type, name);

        if (newCharacter != null) {
            allCharacters.add(newCharacter);
            currentCharacter = newCharacter;
            System.out.println("\nCharacter created successfully!");
            System.out.println("Current character set to: " + newCharacter.getName());

            // Give starting items
            giveStartingItems(newCharacter);
        }
    }

    /**
     * Give starting items to a new character
     */
    private static void giveStartingItems(Character character) {
        System.out.println("\nGiving starting items...");

        // Add a basic weapon based on class
        if (character instanceof Warrior) {
            Item sword = new Item("Training Sword", Item.ItemType.WEAPON, 30, 2);
            character.addItem(sword);
            character.equipItem(sword);
        } else if (character instanceof Mage) {
            Item staff = new Item("Apprentice Staff", Item.ItemType.WEAPON, 30, 2);
            character.addItem(staff);
            character.equipItem(staff);
        }

        // Add some potions
        character.addItem(new Item("Small Health Potion", Item.ItemType.POTION, 25, 0));
        character.addItem(new Item("Small Health Potion", Item.ItemType.POTION, 25, 0));

        System.out.println("Starting items added to inventory!");
    }

    /**
     * Select a character from the list
     */
    private static void selectCharacter() {
        if (allCharacters.isEmpty()) {
            System.out.println("\nNo characters available! Create one first.");
            return;
        }

        System.out.println("\n=== SELECT CHARACTER ===");
        for (int i = 0; i < allCharacters.size(); i++) {
            Character c = allCharacters.get(i);
            String current = (c == currentCharacter) ? " [CURRENT]" : "";
            System.out.println((i + 1) + ". " + c.getName() + " - " + c + current);
        }

        System.out.print("\nSelect character number: ");
        String input = scanner.nextLine();
        Integer choice = InputValidator.validateMenuChoice(input, allCharacters.size());

        if (choice != null && choice > 0) {
            currentCharacter = allCharacters.get(choice - 1);
            System.out.println("Selected: " + currentCharacter.getName());
        }
    }

    /**
     * Show current character details
     */
    private static void showCurrentCharacter() {
        if (currentCharacter == null) {
            System.out.println("\nNo character selected!");
            return;
        }

        System.out.println("\n=== CURRENT CHARACTER ===");
        System.out.println(currentCharacter);
        System.out.println("\nInventory:");
        currentCharacter.showInventory();
    }

    /**
     * Explore dungeon menu
     */
    private static void exploreDungeonMenu() {
        System.out.println("\n=== DUNGEON SELECTION ===");

        // Show available dungeons
        dungeonFactory.showAvailableDungeons();

        System.out.println("\nSelect dungeon:");
        System.out.println("1. Goblin Cave");
        System.out.println("2. Orc Stronghold");
        System.out.println("3. Ancient Crypt");
        System.out.println("4. Custom Dungeon");
        System.out.println("0. Cancel");

        System.out.print("\nYour choice: ");
        String input = scanner.nextLine();
        Integer choice = InputValidator.validateMenuChoice(input, 4);

        if (choice == null || choice == 0) {
            return;
        }

        Dungeon selectedDungeon = null;

        switch (choice) {
            case 1:
                selectedDungeon = dungeonFactory.createGoblinCave();
                break;
            case 2:
                selectedDungeon = dungeonFactory.createOrcStronghold();
                break;
            case 3:
                selectedDungeon = dungeonFactory.createAncientCrypt();
                break;
            case 4:
                // Custom dungeon
                System.out.print("Enter dungeon name: ");
                String dungeonName = scanner.nextLine();
                System.out.print("Enter difficulty (1-10): ");
                String diffInput = scanner.nextLine();
                Integer difficulty = InputValidator.validateMenuChoice(diffInput, 10);
                if (difficulty != null && difficulty > 0) {
                    selectedDungeon = dungeonFactory.createCustomDungeon(dungeonName, difficulty);
                }
                break;
        }

        if (selectedDungeon != null) {
            // Start dungeon exploration
            boolean completed = dungeonExplorer.exploreDungeon(currentCharacter, selectedDungeon);

            if (completed) {
                System.out.println("\nCongratulations on completing the dungeon!");
            } else {
                System.out.println("\nBetter luck next time!");
            }
        }
    }

    /**
     * Train the current character
     */
    private static void trainCharacter() {
        System.out.println("\n=== TRAINING ===");
        System.out.println("Training will increase your damage but reduce max stamina.");
        System.out.println("Current stats: " + currentCharacter);

        System.out.print("\nProceed with training? (y/n): ");
        String input = scanner.nextLine();

        if (InputValidator.validateYesNo(input)) {
            currentCharacter.train();
        }
    }

    /**
     * Manage character inventory
     */
    private static void manageInventory() {
        System.out.println("\n=== INVENTORY MANAGEMENT ===");
        currentCharacter.showInventory();

        // This would be expanded with more inventory options
        System.out.println("\nInventory management features coming soon!");
    }

    /**
     * List all created characters
     */
    private static void listAllCharacters() {
        System.out.println("\n=== ALL CHARACTERS ===");

        if (allCharacters.isEmpty()) {
            System.out.println("No characters created yet.");
            return;
        }

        for (int i = 0; i < allCharacters.size(); i++) {
            Character c = allCharacters.get(i);
            String current = (c == currentCharacter) ? " [CURRENT]" : "";
            System.out.println((i + 1) + ". " + c + current);
        }
    }

    /**
     * Delete a character
     */
    private static void deleteCharacter() {
        if (allCharacters.isEmpty()) {
            System.out.println("\nNo characters to delete!");
            return;
        }

        selectCharacter();
        if (currentCharacter == null) {
            return;
        }

        System.out.print("\nDelete " + currentCharacter.getName() + "? This cannot be undone! (y/n): ");
        String input = scanner.nextLine();

        if (InputValidator.validateYesNo(input)) {
            allCharacters.remove(currentCharacter);
            System.out.println(currentCharacter.getName() + " has been deleted.");
            currentCharacter = null;
        }
    }

    /**
     * Test battle system
     */
    private static void testBattle() {
        System.out.println("\n=== BATTLE TEST ===");

        if (allCharacters.size() < 2) {
            System.out.println("Need at least 2 characters for battle test!");
            return;
        }

        // Simple PvP test (would not be in final game)
        System.out.println("Select two characters to battle...");
        // Implementation similar to previous battle test
        System.out.println("Battle test feature simplified - use Explore Dungeon for real combat!");
    }

    /**
     * Test item system
     */
    private static void testItems() {
        if (!checkCharacterSelected()) {
            return;
        }

        System.out.println("\n=== ITEM SYSTEM TEST ===");

        // Create some test items
        Item testSword = new Item("Test Sword", Item.ItemType.WEAPON, 100, 5);
        Item testArmor = new Item("Test Armor", Item.ItemType.ARMOR, 150, 3);
        Item testPotion = new Item("Test Potion", Item.ItemType.POTION, 50, 0);

        // Add to character
        System.out.println("Adding test items to " + currentCharacter.getName() + "...");
        currentCharacter.addItem(testSword);
        currentCharacter.addItem(testArmor);
        currentCharacter.addItem(testPotion);

        // Show inventory
        currentCharacter.showInventory();
    }

    /**
     * Check if a character is selected
     */
    private static boolean checkCharacterSelected() {
        if (currentCharacter == null) {
            System.out.println("\nNo character selected! Please create or select a character first.");
            return false;
        }
        return true;
    }
}