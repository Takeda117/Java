package com.exam.project;

import com.exam.project.factory.Character;
import com.exam.project.factory.CharacterFactory;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Chiama il metodo di test
        testFactoryInteractive();
    }

    public static void testFactoryInteractive() {
        Scanner scanner = new Scanner(System.in);
        CharacterFactory factory = new CharacterFactory();
        List<Character> characters = new ArrayList<>();

        System.out.println("=== INTERACTIVE FACTORY TEST ===\n");

        while (true) {
            System.out.println("Choose an action:");
            System.out.println("1. Show available character types");
            System.out.println("2. Create a new character");
            System.out.println("3. List created characters");
            System.out.println("4. Test character actions");
            System.out.println("5. Battle test");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            int choice = getIntInput(scanner);

            switch (choice) {
                case 1 -> showCharacterTypes(factory);
                case 2 -> createCharacterInteractive(scanner, factory, characters);
                case 3 -> listCharacters(characters);
                case 4 -> testCharacterActions(scanner, characters);
                case 5 -> battleTest(scanner, characters);
                case 0 -> {
                    System.out.println("Thanks for testing the Factory Pattern!");
                    return;
                }
                default -> System.out.println("Invalid choice! Please try again.\n");
            }
        }
    }

    /**
     * Mostra i tipi di personaggio disponibili
     */
    private static void showCharacterTypes(CharacterFactory factory) {
        System.out.println("\n=== AVAILABLE CHARACTER TYPES ===");
        factory.showAvailableTypes();
        System.out.println();
    }

    /**
     * Crea un personaggio tramite input utente
     */
    private static void createCharacterInteractive(Scanner scanner, CharacterFactory factory, List<Character> characters) {
        System.out.println("\n=== CREATE NEW CHARACTER ===");

        // Input tipo personaggio
        System.out.print("Enter character type (warrior/w, mage/m): ");
        String type = scanner.nextLine().trim();

        // Input nome personaggio
        System.out.print("Enter character name: ");
        String name = scanner.nextLine().trim();

        // Crea personaggio usando il factory
        Character character = factory.createCharacter(type, name);

        if (character != null) {
            characters.add(character);
            System.out.printf("✅ Successfully created: %s%n", character);
            System.out.printf("Total characters created: %d%n", characters.size());
        } else {
            System.out.println("❌ Character creation failed!");
        }
        System.out.println();
    }

    /**
     * Mostra tutti i personaggi creati
     */
    private static void listCharacters(List<Character> characters) {
        System.out.println("\n=== CREATED CHARACTERS ===");

        if (characters.isEmpty()) {
            System.out.println("No characters created yet.");
        } else {
            for (int i = 0; i < characters.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, characters.get(i));
            }
        }
        System.out.println();
    }

    /**
     * Testa le azioni dei personaggi
     */
    private static void testCharacterActions(Scanner scanner, List<Character> characters) {
        if (characters.isEmpty()) {
            System.out.println("\nNo characters to test! Create some first.\n");
            return;
        }

        System.out.println("\n=== CHARACTER ACTIONS TEST ===");

        // Scegli personaggio
        Character character = selectCharacter(scanner, characters);
        if (character == null) return;

        System.out.printf("Testing actions for: %s%n", character.getName());

        while (true) {
            System.out.println("\nWhat should " + character.getName() + " do?");
            System.out.println("1. Attack");
            System.out.println("2. Train");
            System.out.println("3. Rest");
            System.out.println("4. Show stats");
            System.out.println("0. Back to main menu");
            System.out.print("Action: ");

            int action = getIntInput(scanner);

            switch (action) {
                case 1 -> {
                    System.out.println("\n--- ATTACK ---");
                    if (character.getStamina() >= 3) {
                        int damage = character.attack();
                        System.out.printf("Attack dealt %d damage!%n", damage);
                    } else {
                        System.out.println("Not enough stamina to attack!");
                    }
                }
                case 2 -> {
                    System.out.println("\n--- TRAINING ---");
                    if (character.getStamina() >= 10) {
                        character.train();
                    } else {
                        System.out.println("Not enough stamina to train!");
                    }
                }
                case 3 -> {
                    System.out.println("\n--- REST ---");
                    character.rest();
                }
                case 4 -> {
                    System.out.println("\n--- STATS ---");
                    System.out.println(character);
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid action!");
            }
        }
    }

    /**
     * Test di battaglia tra due personaggi
     */
    private static void battleTest(Scanner scanner, List<Character> characters) {
        if (characters.size() < 2) {
            System.out.println("\nNeed at least 2 characters for battle! Create more first.\n");
            return;
        }

        System.out.println("\n=== BATTLE TEST ===");

        // Scegli primo combattente
        System.out.println("Choose first fighter:");
        Character fighter1 = selectCharacter(scanner, characters);
        if (fighter1 == null) return;

        // Scegli secondo combattente
        System.out.println("Choose second fighter:");
        Character fighter2 = selectCharacter(scanner, characters);
        if (fighter2 == null || fighter2 == fighter1) {
            System.out.println("Invalid selection!");
            return;
        }

        // Simula battaglia
        System.out.printf("\n🥊 BATTLE: %s vs %s%n", fighter1.getName(), fighter2.getName());
        System.out.println("Before battle:");
        System.out.printf("  %s: %s%n", fighter1.getName(), fighter1);
        System.out.printf("  %s: %s%n", fighter2.getName(), fighter2);

        // Round di battaglia
        System.out.println("\n--- BATTLE ROUND ---");

        if (fighter1.getStamina() >= 3) {
            int damage1 = fighter1.attack();
            fighter2.takeDamage(damage1);
        } else {
            System.out.printf("%s is too tired to attack!%n", fighter1.getName());
        }

        if (fighter2.isAlive() && fighter2.getStamina() >= 3) {
            int damage2 = fighter2.attack();
            fighter1.takeDamage(damage2);
        } else if (fighter2.isAlive()) {
            System.out.printf("%s is too tired to attack!%n", fighter2.getName());
        }

        // Risultato battaglia
        System.out.println("\nAfter battle:");
        System.out.printf("  %s: %s%n", fighter1.getName(), fighter1);
        System.out.printf("  %s: %s%n", fighter2.getName(), fighter2);

        if (!fighter1.isAlive()) {
            System.out.printf("🏆 %s wins!%n", fighter2.getName());
        } else if (!fighter2.isAlive()) {
            System.out.printf("🏆 %s wins!%n", fighter1.getName());
        } else {
            System.out.println("🤝 Both fighters survive!");
        }

        System.out.println();
    }

    /**
     * Helper: seleziona un personaggio dalla lista
     */
    private static Character selectCharacter(Scanner scanner, List<Character> characters) {
        System.out.println("Available characters:");
        for (int i = 0; i < characters.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, characters.get(i).getName());
        }

        System.out.print("Choose character (number): ");
        int choice = getIntInput(scanner);

        if (choice >= 1 && choice <= characters.size()) {
            return characters.get(choice - 1);
        } else {
            System.out.println("Invalid selection!");
            return null;
        }
    }

    /**
     * Helper: ottiene input intero sicuro
     */
    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
