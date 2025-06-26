package java.rpgSecurity;

import java.logger.GameLogger;
import java.util.logging.Logger;

/**
 * InputValidator - Provides input validation and sanitization
 * <p>
 * This class contains utility methods for validating and sanitizing
 * various types of user input. It helps ensure that all data entering
 * the system meets the required format and security constraints.
 * </p>
 * <p>
 * The validator provides methods for common input types in the game:
 * <ul>
 *   <li>Character names</li>
 *   <li>Menu selections</li>
 *   <li>Yes/no responses</li>
 *   <li>Filenames (with security measures against path traversal)</li>
 * </ul>
 * </p>
 * <p>
 * All validation methods log their activity and provide user-friendly
 * error messages when validation fails.
 * </p>
 */
public class InputValidator {

  private static final Logger logger = GameLogger.getLogger();

  /**
   * Validates a character name
   * <p>
   * Ensures that character names meet the game's requirements:
   * <ul>
   *   <li>Not null or empty</li>
   *   <li>At least 2 characters long</li>
   *   <li>No more than 20 characters long</li>
   * </ul>
   * </p>
   * 
   * @param input The character name to validate
   * @return The validated name, or null if validation fails
   */
  public static String validateCharacterName(String input) {
    logger.info("Validating character name: " + input);

    if (input == null || input.trim().isEmpty()) {
      logger.warning("Character name validation failed: empty name");
      System.out.println("Name cannot be empty!");
      return null;
    }

    try {
      String name = input.trim();

      if (name.length() < 2) {
        logger.warning("Character name validation failed: too short");
        System.out.println("Name too short!");
        return null;
      }

      if (name.length() > 20) {
        logger.warning("Character name validation failed: too long");
        System.out.println("Name too long!");
        return null;
      }

      logger.info("Character name validated successfully: " + name);
      return name;
    } catch (Exception e) {
      logger.severe("Error validating character name: " + e.getMessage());
      System.out.println("Name validation error!");
      return null;
    }
  }

  /**
   * Validates a menu choice
   * <p>
   * Ensures that menu selections are valid integers within the
   * acceptable range for the current menu.
   * </p>
   * 
   * @param input The user's input string
   * @param max The maximum valid menu option number
   * @return The validated menu choice as an Integer, or null if validation fails
   */
  public static Integer validateMenuChoice(String input, int max) {
    logger.info("Validating menu choice: " + input + " (max: " + max + ")");

    if (input == null || input.trim().isEmpty()) {
      logger.warning("Menu choice validation failed: empty input");
      System.out.println("Please enter a number!");
      return null;
    }

    try {
      int choice = Integer.parseInt(input.trim());

      if (choice < 0 || choice > max) {
        logger.warning("Menu choice validation failed: out of range (" + choice + ")");
        System.out.println("Choose between 0 and " + max + "!");
        return null;
      }

      logger.info("Menu choice validated: " + choice);
      return choice;
    } catch (NumberFormatException e) {
      logger.warning("Menu choice validation failed: not a number");
      System.out.println("That's not a number!");
      return null;
    } catch (Exception e) {
      logger.severe("Error validating menu choice: " + e.getMessage());
      System.out.println("Input validation error!");
      return null;
    }
  }

  /**
   * Validates a yes/no response
   * <p>
   * Interprets various forms of affirmative responses (y, yes, s, si)
   * as true, and any other input as false.
   * </p>
   * 
   * @param input The user's input string
   * @return true for affirmative responses, false otherwise
   */
  public static boolean validateYesNo(String input) {
    logger.info("Validating yes/no input: " + input);

    try {
      if (input == null) {
        logger.info("Yes/no validation: null input = false");
        return false;
      }

      String clean = input.trim().toLowerCase();
      boolean result =
          clean.equals("y") || clean.equals("yes") || clean.equals("s") || clean.equals("si");

      logger.info("Yes/no validation result: " + result);
      return result;
    } catch (Exception e) {
      logger.warning("Error validating yes/no: " + e.getMessage());
      return false;
    }
  }

  /**
   * Validates a filename
   * <p>
   * Ensures that filenames meet the system's requirements and security constraints:
   * <ul>
   *   <li>Not null or empty</li>
   *   <li>No more than 30 characters long</li>
   *   <li>Contains only safe characters (via sanitization)</li>
   * </ul>
   * The method automatically appends the ".save" extension to valid filenames.
   * </p>
   * 
   * @param input The filename to validate
   * @return The validated filename with extension, or null if validation fails
   */
  public static String validateFilename(String input) {
    logger.info("Validating filename: " + input);

    if (input == null || input.trim().isEmpty()) {
      logger.warning("Filename validation failed: empty filename");
      System.out.println("Filename cannot be empty!");
      return null;
    }

    try {
      String name = input.trim();

      if (name.length() > 30) {
        logger.warning("Filename validation failed: too long");
        System.out.println("Filename too long!");
        return null;
      }

      // Sanitize filename
      name = sanitizeFilename(name);
      if (name.isEmpty()) {
        logger.warning("Filename validation failed: invalid characters");
        System.out.println("Invalid filename!");
        return null;
      }

      String result = name + ".save";
      logger.info("Filename validated: " + result);
      return result;
    } catch (Exception e) {
      logger.severe("Error validating filename: " + e.getMessage());
      System.out.println("Filename validation error!");
      return null;
    }
  }

  /**
   * Sanitizes a filename to prevent path traversal attacks
   * <p>
   * Removes dangerous characters and patterns that could be used for
   * directory traversal or other file system attacks.
   * </p>
   * 
   * @param input The filename to sanitize
   * @return The sanitized filename, or an empty string if the input is invalid
   */
  public static String sanitizeFilename(String input) {
    if (input == null) {
      return "";
    }

    String name = input.trim();

    if (name.length() > 30) {
      return "";
    }

    // Remove dangerous characters and path traversal
    name = name.replaceAll("[^a-zA-Z0-9_\\-]", "_");

    // Prevent path traversal
    if (name.contains("..")
        || name.startsWith(".")
        || name.startsWith("/")
        || name.startsWith("\\")) {
      return "";
    }

    return name;
  }

  /**
   * Sanitizes general input by trimming whitespace
   * <p>
   * Provides a simple way to clean up user input by removing
   * leading and trailing whitespace.
   * </p>
   * 
   * @param string The input string to sanitize
   * @return The sanitized string, or an empty string if input is null
   */
  public static String sanitizeInput(String string) {
    if (string == null) {
      return "";
    }
    return string.trim();
  }
}