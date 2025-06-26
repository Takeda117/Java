package java.menu;

import java.composite.GameMenu;
import java.composite.MenuItem;
import java.factory.Character;
import java.rpgIO.CharacterManagement;
import java.rpgSecurity.InputValidator;
import java.logger.GameLogger;

import java.rpgSecurity.ExceptionHandler;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * CharacterMenu - Manages character-specific menu
 * <p>
 * This class is responsible for building and executing the menu for a specific character.
 * It provides options for training, resting, accessing inventory, exploring dungeons,
 * saving the character, and returning to the main menu. The class uses the Composite pattern
 * through the GameMenu and MenuItem classes to structure the menu hierarchy.
 * </p>
 * <p>
 * The CharacterMenu also includes methods for handling character actions like training
 * and resting, and manages the flow between different game menus.
 * </p>
 */
public class CharacterMenu {
    private static final Logger logger = GameLogger.getLogger();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Shows character menu
     * <p>
     * Displays and executes the menu for a specific character, handling
     * character state validation and menu navigation. If the character is
     * dead, it returns to the main menu.
     * </p>
     * 
     * @param character The character for which to show the menu
     * @throws ReturnToMainMenuException if the user chooses to return to the main menu
     */
    public static void showCharacterMenu(Character character) {
        if (character == null) {
            logger.warning("Cannot show character menu: null character");
            return;
        }

        if (!character.isAlive()) {
            logger.warning("Character is dead, returning to main menu: " + character.getName());
            System.out.println("Il tuo personaggio è morto! Ritorno al menu principale.");
            throw new ReturnToMainMenuException();
        }

        try {
            GameMenu characterMenu = buildCharacterMenu(character);
            characterMenu.execute();
        } catch (ReturnToMainMenuException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error in character menu: " + e.getMessage());
            ExceptionHandler.handleException(e, "Errore nel menu personaggio.");
        }
    }

    /**
     * Builds character menu
     * <p>
     * Creates a GameMenu instance with options specific to the character,
     * including training, resting, inventory access, dungeon exploration,
     * saving, and returning to the main menu.
     * </p>
     * 
     * @param character The character for which to build the menu
     * @return A configured GameMenu instance for the character
     */
    private static GameMenu buildCharacterMenu(Character character) {
        GameMenu menu = new GameMenu("Menu Personaggio - " + character.getName());
        menu.add(new MenuItem("Allenati", () -> trainCharacter(character)));
        menu.add(new MenuItem("Riposa", () -> restCharacter(character)));
        menu.add(new MenuItem("Accedi inventario", () -> InventoryMenu.showInventoryMenu(character)));
        menu.add(new MenuItem("Esplora Dungeon", () -> DungeonMenu.showDungeonMenu(character)));
        menu.add(new MenuItem("Salva", () -> saveAndReturnToMain(character)));
        menu.add(new MenuItem("Torna al menu principale", CharacterMenu::exitToMain));
        return menu;
    }

    /**
     * Trains character
     * <p>
     * Handles the character training action, which increases the character's
     * base damage at the cost of stamina. If the character doesn't have enough
     * stamina, the training is canceled.
     * </p>
     * 
     * @param character The character to train
     */
    private static void trainCharacter(Character character) {
        System.out.println("\n=== ALLENAMENTO ===");
        System.out.println("Personaggio: " + character);

        if (character.getStamina() < 10) {
            System.out.println("Non hai abbastanza stamina per allenarti! (Serve almeno 10)");
            return;
        }

        int oldDamage = character.getBaseDamage();
        int oldStamina = character.getStamina();
        
        character.restoreStamina(-10);
        
        if (character.getStamina() >= oldStamina) {
            reduceStaminaViaReflection(character);
        }
        
        character.train();

        System.out.println("Allenamento completato!");
        System.out.println("Danno aumentato da " + oldDamage + " a " + character.getBaseDamage());
        System.out.println("Stamina consumata: -10 (Stamina attuale: " + character.getStamina() + ")");

        logger.info("Character trained: " + character.getName());
    }

    /**
     * Reduces stamina using reflection as a fallback method
     * <p>
     * This method is used as a fallback when the normal method to reduce
     * stamina fails. It uses reflection to directly access and modify the
     * stamina field in the character class.
     * </p>
     * 
     * @param character The character whose stamina should be reduced
     */
    private static void reduceStaminaViaReflection(Character character) {
        try {
            java.lang.reflect.Field staminaField = character.getClass().getSuperclass().getDeclaredField("stamina");
            staminaField.setAccessible(true);
            int currentStamina = (int) staminaField.get(character);
            staminaField.set(character, currentStamina - 10);
            logger.warning("Used reflection to reduce stamina as normal method failed");
        } catch (Exception e) {
            logger.severe("Failed to reduce stamina via reflection: " + e.getMessage());
        }
    }

    /**
     * Rests character
     * <p>
     * Handles the character resting action, which recovers health and stamina.
     * If the character is already at full health and stamina, the rest is canceled.
     * </p>
     * 
     * @param character The character to rest
     */
    private static void restCharacter(Character character) {
        System.out.println("\n=== RIPOSO ===");
        System.out.println("Stato attuale: " + character);

        if (character.getHealth() == character.getMaxHealth() &&
                character.getStamina() == character.getMaxStamina()) {
            System.out.println("Sei già completamente riposato!");
            return;
        }

        int oldHealth = character.getHealth();
        int oldStamina = character.getStamina();

        character.rest();

        int healthRecovered = character.getHealth() - oldHealth;
        int staminaRecovered = character.getStamina() - oldStamina;

        System.out.println("Riposo completato!");
        if (healthRecovered > 0) {
            System.out.println("Vita recuperata: +" + healthRecovered);
        }
        if (staminaRecovered > 0) {
            System.out.println("Stamina recuperata: +" + staminaRecovered);
        }

        logger.info("Character rested: " + character.getName());
    }

    /**
     * Saves character and returns to main menu
     * <p>
     * Prompts the user for a save file name, saves the character to that file,
     * and returns to the main menu if the save is successful.
     * </p>
     * 
     * @param character The character to save
     */
    private static void saveAndReturnToMain(Character character) {
        System.out.println("\n=== SALVATAGGIO ===");
        System.out.print("Nome del salvataggio: ");
        String filename = InputValidator.sanitizeInput(scanner.nextLine());

        if (!filename.isEmpty()) {
            boolean success = CharacterManagement.saveCharacter(character, filename);
            if (success) {
                System.out.println("Personaggio salvato!");
                logger.info("Character saved: " + character.getName());
                throw new ReturnToMainMenuException();
            } else {
                System.out.println("Errore nel salvataggio!");
            }
        } else {
            System.out.println("Nome non valido!");
        }
    }

    /**
     * Exits to main menu
     * <p>
     * Logs the action and throws a ReturnToMainMenuException to signal
     * that the application should return to the main menu.
     * </p>
     * 
     * @throws ReturnToMainMenuException to signal return to main menu
     */
    private static void exitToMain() {
        logger.info("User returned to main menu");
        throw new ReturnToMainMenuException();
    }

    /**
     * Custom exception to signal return to main menu
     * <p>
     * This exception is used to signal that the application should
     * return to the main menu from any point in the character menu
     * or its submenus.
     * </p>
     */
    public static class ReturnToMainMenuException extends RuntimeException {
    }
}