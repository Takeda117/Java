package com.exam.project.security;

import com.exam.project.logger.GameLogger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * ExceptionHandler - Gestisce le eccezioni del gioco in modo sicuro
 *
 * Questa classe serve per:
 * - Non mostrare errori tecnici agli utenti
 * - Registrare gli errori nel log per debugging
 * - Mostrare messaggi comprensibili agli utenti
 */
public class ExceptionHandler {

    private static final Logger logger = GameLogger.getLogger();

    /**
     * Gestisce un'eccezione generica
     * Registra l'errore nel log e mostra un messaggio sicuro all'utente
     */
    public static void handleException(Exception e, String userMessage) {
        // Registra l'errore completo nel log (per noi sviluppatori)
        logger.log(Level.SEVERE, "Errore: " + e.getMessage(), e);

        // Mostra all'utente solo un messaggio sicuro
        System.out.println(userMessage);
    }

    /**
     * Gestisce errori durante il salvataggio/caricamento
     */
    public static void handleSaveLoadError(Exception e) {
        logger.log(Level.SEVERE, "Errore save/load: " + e.getMessage(), e);
        
        // Fornisci messaggi più specifici in base al tipo di eccezione
        if (e instanceof FileNotFoundException) {
            System.out.println("File non trovato. Controlla il nome del file.");
        } else if (e instanceof IOException) {
            System.out.println("Problema di accesso al file. Controlla i permessi.");
        } else {
            System.out.println("Errore durante il salvataggio/caricamento. Riprova.");
        }
    }

    /**
     * Gestisce errori durante la creazione del personaggio
     */
    public static void handleCharacterError(Exception e) {
        logger.log(Level.WARNING, "Errore personaggio: " + e.getMessage(), e);
        System.out.println("Errore nella creazione del personaggio. Controlla i dati inseriti.");
    }

    /**
     * Gestisce errori nell'inventario
     */
    public static void handleInventoryError(Exception e) {
        logger.log(Level.WARNING, "Errore inventario: " + e.getMessage(), e);
        System.out.println("Operazione inventario fallita.");
    }

    /**
     * Registra un'azione importante nel log
     */
    public static void logAction(String action) {
        logger.log(Level.INFO, "Azione: " + action);
    }
}