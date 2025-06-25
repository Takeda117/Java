package com.exam.project.security;

import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;/**
 * Simple input validation
 */
public class InputValidator {

    private static final Logger logger = GameLogger.getLogger();

    /**
     * Check character name
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
     * Check menu choice
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
     * Check yes/no
     */
    public static boolean validateYesNo(String input) {
        logger.info("Validating yes/no input: " + input);

        try {
            if (input == null) {
                logger.info("Yes/no validation: null input = false");
                return false;
            }

            String clean = input.trim().toLowerCase();
            boolean result = clean.equals("y") || clean.equals("yes") || clean.equals("s") || clean.equals("si");

            logger.info("Yes/no validation result: " + result);
            return result;
        } catch (Exception e) {
            logger.warning("Error validating yes/no: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check filename
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

            // Remove bad characters
            name = name.replaceAll("[^a-zA-Z0-9_]", "_");
            String result = name + ".save";

            logger.info("Filename validated: " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Error validating filename: " + e.getMessage());
            System.out.println("Filename validation error!");
            return null;
        }
    }
}