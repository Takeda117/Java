package com.exam.project.security;

/**
 * InputValidator - Controlla che gli input dell'utente siano validi
 *
 * Questa classe serve per:
 * - Verificare che i nomi dei personaggi siano corretti
 * - Controllare che i numeri siano nel range giusto
 * - Pulire gli input da spazi extra
 */
public class InputValidator {

    // Costanti per i limiti
    private static final int MAX_NAME_LENGTH = 20;
    private static final int MIN_NAME_LENGTH = 2;

    /**
     * Controlla e pulisce il nome del personaggio
     *
     * Regole:
     * - Deve essere tra 2 e 20 caratteri
     * - Solo lettere e spazi
     * - Non può iniziare o finire con spazi
     */
    public static String validateCharacterName(String input) {
        // Controlla se l'input è nullo o vuoto
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Il nome non può essere vuoto!");
            return null;
        }

        // Rimuovi spazi all'inizio e alla fine
        String cleanName = input.trim();

        // Controlla la lunghezza
        if (cleanName.length() < MIN_NAME_LENGTH) {
            System.out.println("Il nome deve avere almeno " + MIN_NAME_LENGTH + " caratteri!");
            return null;
        }

        if (cleanName.length() > MAX_NAME_LENGTH) {
            System.out.println("Il nome può avere massimo " + MAX_NAME_LENGTH + " caratteri!");
            return null;
        }

        // Controlla che contenga solo lettere e spazi
        for (char c : cleanName.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                System.out.println("Il nome può contenere solo lettere e spazi!");
                return null;
            }
        }

        // Controlla che non ci siano troppi spazi consecutivi
        if (cleanName.contains("  ")) {
            cleanName = cleanName.replaceAll(" +", " ");
        }

        return cleanName;
    }

    /**
     * Controlla che un numero sia valido per le scelte del menu
     */
    public static Integer validateMenuChoice(String input, int maxChoice) {
        // Controlla se l'input è vuoto
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        // Prova a convertire in numero
        try {
            int choice = Integer.parseInt(input.trim());

            // Controlla che sia nel range valido
            if (choice < 0 || choice > maxChoice) {
                System.out.println("Scegli un numero tra 0 e " + maxChoice);
                return null;
            }

            return choice;

        } catch (NumberFormatException e) {
            // Se non è un numero valido
            System.out.println("Inserisci un numero valido!");
            return null;
        }
    }

    /**
     * Controlla il nome del file di salvataggio
     *
     * Regole:
     * - Solo lettere, numeri e underscore
     * - Massimo 30 caratteri
     */
    public static String validateFilename(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Il nome del file non può essere vuoto!");
            return null;
        }

        String cleanName = input.trim();

        // Rimuovi l'estensione se l'utente l'ha messa
        if (cleanName.endsWith(".save")) {
            cleanName = cleanName.substring(0, cleanName.length() - 5);
        }

        // Controlla lunghezza
        if (cleanName.length() > 30) {
            System.out.println("Il nome del file è troppo lungo (max 30 caratteri)!");
            return null;
        }

        // Controlla caratteri validi
        for (char c : cleanName.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '_') {
                System.out.println("Il nome del file può contenere solo lettere, numeri e underscore!");
                return null;
            }
        }

        // Aggiungi l'estensione .save
        return cleanName + ".save";
    }

    /**
     * Controlla input si/no
     */
    public static boolean validateYesNo(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        String clean = input.trim().toLowerCase();

        // Accetta varie forme di si
        if (clean.equals("s") || clean.equals("si") || clean.equals("y") || clean.equals("yes")) {
            return true;
        }

        return false;
    }

    /**
     * Valida un importo di denaro (deve essere positivo)
     */
    public static Integer validateMoney(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        try {
            int amount = Integer.parseInt(input.trim());

            if (amount < 0) {
                System.out.println("L'importo non può essere negativo!");
                return null;
            }

            return amount;

        } catch (NumberFormatException e) {
            System.out.println("Inserisci un numero valido!");
            return null;
        }
    }
}