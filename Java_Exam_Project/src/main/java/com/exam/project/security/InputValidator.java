package com.exam.project.security;

/**
 * Enhanced input validator with comprehensive error handling
 */
public class InputValidator {

    private static final int MAX_NAME_LENGTH = 20;
    private static final int MIN_NAME_LENGTH = 2;

    /**
     * Validates and cleans character names with error protection
     * @param input The input name to validate
     * @return Clean name or null if invalid
     */
    public static String validateCharacterName(String input) {
        try {
            if (input == null) {
                System.out.println("Name cannot be null!");
                return null;
            }

            String cleanName = input.trim();

            if (cleanName.isEmpty()) {
                System.out.println("Name cannot be empty!");
                return null;
            }

            if (cleanName.length() < MIN_NAME_LENGTH) {
                System.out.println("Name must be at least " + MIN_NAME_LENGTH + " characters!");
                return null;
            }

            if (cleanName.length() > MAX_NAME_LENGTH) {
                System.out.println("Name cannot exceed " + MAX_NAME_LENGTH + " characters!");
                return null;
            }

            for (char c : cleanName.toCharArray()) {
                if (!Character.isLetter(c) && c != ' ') {
                    System.out.println("Name can only contain letters and spaces!");
                    return null;
                }
            }

            cleanName = cleanName.replaceAll(" +", " ");
            return cleanName;

        } catch (Exception e) {
            System.out.println("Error validating name!");
            return null;
        }
    }

    /**
     * Validates menu choices with bounds checking
     * @param input The input string to validate
     * @param maxChoice The maximum valid choice
     * @return Valid choice or null if invalid
     */
    public static Integer validateMenuChoice(String input, int maxChoice) {
        try {
            if (input == null) {
                System.out.println("Input cannot be null!");
                return null;
            }

            String cleanInput = input.trim();
            if (cleanInput.isEmpty()) {
                System.out.println("Please enter a number!");
                return null;
            }

            int choice = Integer.parseInt(cleanInput);

            if (choice < 0 || choice > maxChoice) {
                System.out.println("Choose a number between 0 and " + maxChoice + "!");
                return null;
            }

            return choice;

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
            return null;
        } catch (Exception e) {
            System.out.println("Invalid input!");
            return null;
        }
    }

    /**
     * Validates yes/no input safely
     * @param input The input to validate
     * @return true for yes, false for no or invalid input
     */
    public static boolean validateYesNo(String input) {
        try {
            if (input == null) {
                return false;
            }

            String clean = input.trim().toLowerCase();
            return clean.equals("s") || clean.equals("si") || clean.equals("y") || clean.equals("yes");

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates filename input safely
     * @param input The filename to validate
     * @return Clean filename or null if invalid
     */
    public static String validateFilename(String input) {
        try {
            if (input == null || input.trim().isEmpty()) {
                System.out.println("Filename cannot be empty!");
                return null;
            }

            String cleanName = input.trim();

            if (cleanName.endsWith(".save")) {
                cleanName = cleanName.substring(0, cleanName.length() - 5);
            }

            if (cleanName.length() > 30) {
                System.out.println("Filename too long (max 30 characters)!");
                return null;
            }

            for (char c : cleanName.toCharArray()) {
                if (!Character.isLetterOrDigit(c) && c != '_') {
                    System.out.println("Filename can only contain letters, numbers and underscore!");
                    return null;
                }
            }

            return cleanName + ".save";

        } catch (Exception e) {
            System.out.println("Error validating filename!");
            return null;
        }
    }

    /**
     * Validates money amounts safely
     * @param input The input to validate
     * @return Valid amount or null if invalid
     */
    public static Integer validateMoney(String input) {
        try {
            if (input == null || input.trim().isEmpty()) {
                System.out.println("Amount cannot be empty!");
                return null;
            }

            int amount = Integer.parseInt(input.trim());

            if (amount < 0) {
                System.out.println("Amount cannot be negative!");
                return null;
            }

            return amount;

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
            return null;
        } catch (Exception e) {
            System.out.println("Invalid input!");
            return null;
        }
    }
}