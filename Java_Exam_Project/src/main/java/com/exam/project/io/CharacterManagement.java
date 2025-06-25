package com.exam.project.io;

import com.exam.project.factory.Character;
import com.exam.project.factory.CharacterFactory;
import com.exam.project.factory.Warrior;
import com.exam.project.factory.Mage;
import com.exam.project.logger.GameLogger;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Simple save/load for characters
 * Basic I/O implementation for exam requirements
 */
public class CharacterManagement {

    private static final String SAVE_DIR = "saves";
    private static final Logger logger = GameLogger.getLogger();

    /**
     * Save character to file
     */
    public static boolean saveCharacter(Character character, String filename) {
        logger.info("Attempting to save character: " + (character != null ? character.getName() : "null"));

        if (character == null || filename == null) {
            logger.warning("Save failed: null character or filename");
            System.out.println("Error saving!");
            return false;
        }

        try {
            // Create saves directory if needed
            File dir = new File(SAVE_DIR);
            if (!dir.exists()) {
                boolean created = dir.mkdir();
                logger.info("Save directory created: " + created);
            }

            Properties props = new Properties();

            // Save basic info
            props.setProperty("name", character.getName());
            props.setProperty("type", character.getClass().getSimpleName());
            props.setProperty("health", String.valueOf(character.getHealth()));
            props.setProperty("stamina", String.valueOf(character.getStamina()));
            props.setProperty("damage", String.valueOf(character.getBaseDamage()));
            props.setProperty("money", String.valueOf(character.getMoney()));

            // Save mage mana if needed
            if (character instanceof Mage) {
                Mage mage = (Mage) character;
                props.setProperty("mana", String.valueOf(mage.getMana()));
                logger.info("Saved mage-specific data for: " + character.getName());
            }

            // Write to file
            try {
                FileOutputStream out = new FileOutputStream(SAVE_DIR + "/" + filename + ".save");
                props.store(out, "Character Save");
                out.close();
                logger.info("Character saved successfully: " + character.getName());
                System.out.println("Character saved!");
                return true;
            } catch (IOException e) {
                logger.severe("File write error: " + e.getMessage());
                System.out.println("Save failed!");
                return false;
            }

        } catch (Exception e) {
            logger.severe("Unexpected error during save: " + e.getMessage());
            System.out.println("Save failed!");
            return false;
        }
    }

    /**
     * Load character from file
     */
    public static Character loadCharacter(String filename) {
        logger.info("Attempting to load character from: " + filename);

        if (filename == null || filename.trim().isEmpty()) {
            logger.warning("Load failed: invalid filename");
            System.out.println("Invalid filename!");
            return null;
        }

        Properties props = new Properties();

        try {
            FileInputStream in = new FileInputStream(SAVE_DIR + "/" + filename + ".save");
            props.load(in);
            in.close();
            logger.info("File loaded successfully: " + filename);
        } catch (FileNotFoundException e) {
            logger.warning("Save file not found: " + filename);
            System.out.println("Save file not found!");
            return null;
        } catch (IOException e) {
            logger.severe("File read error: " + e.getMessage());
            System.out.println("Load failed!");
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
            Character character = factory.createCharacter(charType, name);

            if (character != null) {
                logger.info("Character recreated successfully: " + name);
                System.out.println("Character loaded: " + name);
            } else {
                logger.warning("Character factory returned null");
            }

            return character;

        } catch (Exception e) {
            logger.severe("Error recreating character: " + e.getMessage());
            System.out.println("Load failed!");
            return null;
        }
    }

    /**
     * Check if save exists
     */
    public static boolean saveExists(String filename) {
        try {
            if (filename == null) {
                logger.warning("saveExists called with null filename");
                return false;
            }

            File file = new File(SAVE_DIR + "/" + filename + ".save");
            boolean exists = file.exists();
            logger.info("Save file check for " + filename + ": " + exists);
            return exists;
        } catch (Exception e) {
            logger.warning("Error checking save file existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * List save files
     */
    public static String[] listSaveFiles() {
        logger.info("Listing save files");

        try {
            File dir = new File(SAVE_DIR);
            if (!dir.exists()) {
                logger.info("Save directory doesn't exist");
                return new String[0];
            }

            File[] files = dir.listFiles();
            if (files == null) {
                logger.warning("Could not list files in save directory");
                return new String[0];
            }

            String[] names = new String[files.length];
            int count = 0;
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                if (name.endsWith(".save")) {
                    names[count] = name.replace(".save", "");
                    count++;
                }
            }

            // Resize array to actual count
            String[] result = new String[count];
            System.arraycopy(names, 0, result, 0, count);

            logger.info("Found " + count + " save files");
            return result;

        } catch (Exception e) {
            logger.severe("Error listing save files: " + e.getMessage());
            return new String[0];
        }
    }

    /**
     * Delete save file
     */
    public static boolean deleteSave(String filename) {
        logger.info("Attempting to delete save: " + filename);

        try {
            if (filename == null) {
                logger.warning("Delete called with null filename");
                return false;
            }

            File file = new File(SAVE_DIR + "/" + filename + ".save");
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    logger.info("Save file deleted successfully: " + filename);
                    System.out.println("Save deleted!");
                } else {
                    logger.warning("Failed to delete save file: " + filename);
                    System.out.println("Delete failed!");
                }
                return deleted;
            } else {
                logger.info("Save file not found for deletion: " + filename);
                System.out.println("Save not found!");
                return false;
            }
        } catch (Exception e) {
            logger.severe("Error deleting save file: " + e.getMessage());
            System.out.println("Delete failed!");
            return false;
        }
    }
}
