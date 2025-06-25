package com.exam.project;

import com.exam.project.composite.GameMenu;
import com.exam.project.model.GameMenuBuilder;
import com.exam.project.logger.GameLogger;

import java.util.logging.Logger;

public class App {
    private static final Logger logger = GameLogger.getLogger();
    
    public static void main(String[] args) {
        try {
            logger.info("Avvio applicazione");
            System.out.println("=== RPG ADVENTURE GAME ===");
            System.out.println("Un gioco di ruolo testuale in Java\n");
            
            // Costruisci ed esegui il menu di gioco
            GameMenu mainMenu = GameMenuBuilder.buildMainMenu();
            mainMenu.execute();
            
            System.out.println("\nGrazie per aver giocato!");
            logger.info("Applicazione terminata normalmente");
        } catch (Exception e) {
            logger.severe("Errore critico nell'applicazione: " + e.getMessage());
            System.out.println("Si è verificato un errore critico. L'applicazione verrà chiusa.");
        }

    }
}
