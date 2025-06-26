package java.rpgIO;

import java.factory.Character;
import java.factory.CharacterFactory;
import java.factory.Mage;
import java.io.*;
import java.logger.GameLogger;
import java.rpgSecurity.InputValidator;
import java.rpgSecurity.ExceptionHandler;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * CharacterManagement - Handles character save/load operations
 * <p>
 * This class provides functionality for saving and loading character data
 * to and from files. It implements basic I/O operations required for the
 * game's persistence system, allowing players to save their progress and
 * continue later.
 * </p>
 * <p>
 * The implementation uses Properties to store character data in a simple
 * text-based format, with appropriate error handling and security measures.
 * </p>
 */
public class CharacterManagement {

  // Use external configuration instead of hardcoded values
  private static final String SAVE_DIR = System.getProperty("game.save.dir", "saves");
  private static final String FILE_EXT = System.getProperty("game.save.ext", ".save");
  private static final Logger logger = GameLogger.getLogger();

  /**
   * Saves a character to a file
   *
   * @param character The character to save
   * @param filename The name of the save file (without extension)
   * @return true if save was successful, false otherwise
   */
  public static boolean saveCharacter(Character character, String filename) {
    logger.info(
        "Attempting to save character: " + (character != null ? character.getName() : "null"));

    if (character == null || filename == null) {
      logger.warning("Save failed: null character or filename");
      System.out.println("Error saving!");
      return false;
    }

    // Sanitize filename
    String safeFilename = InputValidator.sanitizeFilename(filename);
    if (safeFilename.isEmpty()) {
      logger.warning("Save failed: invalid filename after sanitization");
      System.out.println("Invalid filename!");
      return false;
    }

    try {
      // Create saves directory if needed
      File dir = new File(SAVE_DIR);
      if (!dir.exists()) {
        boolean created =
            dir.mkdirs(); // Use mkdirs instead of mkdir to create parent directories if needed
        logger.info("Save directory created: " + created);
        if (!created) {
          logger.warning("Failed to create save directory");
          System.out.println("Failed to create save directory!");
          return false;
        }
      }

      Properties props = new Properties();

      // Save basic info with null checks
      props.setProperty("name", character.getName() != null ? character.getName() : "Unknown");
      props.setProperty("type", character.getClass().getSimpleName());
      props.setProperty("health", String.valueOf(Math.max(0, character.getHealth())));
      props.setProperty("maxHealth", String.valueOf(Math.max(1, character.getMaxHealth())));
      props.setProperty("stamina", String.valueOf(Math.max(0, character.getStamina())));
      props.setProperty("maxStamina", String.valueOf(Math.max(1, character.getMaxStamina())));
      props.setProperty("damage", String.valueOf(Math.max(0, character.getBaseDamage())));
      props.setProperty("money", String.valueOf(Math.max(0, character.getMoney())));
      props.setProperty("level", String.valueOf(Math.max(1, character.getLevel())));

      // Save mage mana if needed
      if (character instanceof Mage) {
        Mage mage = (Mage) character;
        props.setProperty("mana", String.valueOf(Math.max(0, mage.getMana())));
        props.setProperty("maxMana", String.valueOf(Math.max(1, mage.getMaxMana())));
        logger.info("Saved mage-specific data for: " + character.getName());
      }

      // Write to file - add extension if not already present
      String fullFilename = safeFilename;
      if (!fullFilename.endsWith(FILE_EXT)) {
        fullFilename += FILE_EXT;
      }

      File saveFile = new File(SAVE_DIR + "/" + fullFilename);
      try (FileOutputStream out = new FileOutputStream(saveFile)) {
        props.store(out, "Character Save");
        logger.info(
            "Character saved successfully: "
                + character.getName()
                + " to "
                + saveFile.getAbsolutePath());
        System.out.println("Character saved to " + saveFile.getAbsolutePath());
        return true;
      } catch (IOException e) {
        logger.severe("File write error: " + e.getMessage());
        ExceptionHandler.handleSaveLoadError(e);
        return false;
      } catch (Exception e) {
        logger.severe("Unexpected error during save: " + e.getMessage());
        ExceptionHandler.handleException(e, "Save failed!");
        return false;
      }
    } catch (Exception e) {
      logger.severe("Unexpected error preparing save: " + e.getMessage());
      ExceptionHandler.handleException(e, "Save failed!");
      return false;
    }
  }

  /**
   * Loads a character from a file
   *
   * @param filename The name of the save file (without extension)
   * @return The loaded character, or null if loading failed
   */
  public static Character loadCharacter(String filename) {
    logger.info("Attempting to load character from: " + filename);

    if (filename == null || filename.trim().isEmpty()) {
      logger.warning("Load failed: invalid filename");
      System.out.println("Invalid filename!");
      return null;
    }

    // Sanitize filename
    String safeFilename = InputValidator.sanitizeFilename(filename.trim());
    if (safeFilename.isEmpty()) {
      logger.warning("Load failed: invalid filename");
      System.out.println("Invalid filename!");
      return null;
    }

    Properties props = new Properties();
    File saveFile = new File(SAVE_DIR + "/" + safeFilename + FILE_EXT);

    // Verify the file exists before attempting to load it
    if (!saveFile.exists()) {
      logger.warning("Save file not found: " + safeFilename);
      System.out.println("Save file not found!");
      return null;
    }

    try (FileInputStream in = new FileInputStream(saveFile)) {
      props.load(in);
      logger.info("File loaded successfully: " + safeFilename);
    } catch (FileNotFoundException e) {
      logger.warning("Save file not found: " + safeFilename);
      ExceptionHandler.handleSaveLoadError(e);
      return null;
    } catch (IOException e) {
      logger.severe("File read error: " + e.getMessage());
      ExceptionHandler.handleSaveLoadError(e);
      return null;
    }

    try {
      String name = props.getProperty("name");
      String type = props.getProperty("type");

      if (name == null || type == null) {
        logger.warning("Corrupted save file - missing name or type");
        System.out.println("Bad save file!");
        return null;
      }

      // Create character
      CharacterFactory factory = new CharacterFactory();
      String charType = type.equals("Warrior") ? "warrior" : "mage";

      // First create the base character
      Character character = factory.createCharacter(charType, name);

      if (character == null) {
        logger.warning("Character factory returned null");
        System.out.println("Failed to create character!");
        return null;
      }

      // Load base values
      try {
        // Read values from file
        int health = Integer.parseInt(props.getProperty("health", "0"));
        int maxHealth = Integer.parseInt(props.getProperty("maxHealth", "0"));
        int stamina = Integer.parseInt(props.getProperty("stamina", "0"));
        int maxStamina = Integer.parseInt(props.getProperty("maxStamina", "0"));
        int damage = Integer.parseInt(props.getProperty("damage", "0"));
        int money = Integer.parseInt(props.getProperty("money", "0"));
        int level = Integer.parseInt(props.getProperty("level", "1"));

        // Instead of using setters, recreate the character with loaded values
        // This approach requires the factory to support creation with custom values
        character =
            factory.createCustomCharacter(
                charType, name, health, maxHealth, stamina, maxStamina, damage, money, level);

        // If the character is a mage, also load mana
        if (character instanceof Mage
            && props.containsKey("mana")
            && props.containsKey("maxMana")) {
          int mana = Integer.parseInt(props.getProperty("mana", "0"));
          int maxMana = Integer.parseInt(props.getProperty("maxMana", "0"));

          // Recreate the mage with mana values
          character =
              factory.createCustomMage(
                  name,
                  health,
                  maxHealth,
                  stamina,
                  maxStamina,
                  damage,
                  money,
                  level,
                  mana,
                  maxMana);
        }
      } catch (NumberFormatException e) {
        logger.warning("Error parsing numeric values: " + e.getMessage());
        // Continue with default values
      }

      logger.info("Character loaded successfully: " + name);
      System.out.println("Character loaded: " + name);
      return character;

    } catch (Exception e) {
      logger.severe("Error recreating character: " + e.getMessage());
      System.out.println("Load failed!");
      return null;
    }
  }

  /**
   * Lists all save files in the save directory
   *
   * @return Array of save file names (without extensions)
   */
  public static String[] listSaveFiles() {
    logger.info("Listing save files");

    try {
      File dir = new File(SAVE_DIR);
      if (!dir.exists()) {
        logger.info("Save directory doesn't exist");
        return new String[0];
      }

      File[] files = dir.listFiles((d, name) -> name.endsWith(FILE_EXT));
      if (files == null) {
        logger.warning("Could not list files in save directory");
        return new String[0];
      }

      String[] names = new String[files.length];
      for (int i = 0; i < files.length; i++) {
        names[i] = files[i].getName().replace(FILE_EXT, "");
      }

      logger.info("Found " + names.length + " save files");
      return names;
    } catch (Exception e) {
      logger.severe("Error listing save files: " + e.getMessage());
      return new String[0];
    }
  }
    }

