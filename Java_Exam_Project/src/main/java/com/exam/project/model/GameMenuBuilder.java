package com.exam.project.model;

import com.exam.project.composite.*;
import com.exam.project.factory.Character;
import com.exam.project.factory.CharacterFactory;
import com.exam.project.factoryMonster.MonsterFactory;
import com.exam.project.combat.CombatSystem;
import com.exam.project.security.InputValidator;
import com.exam.project.logger.GameLogger;
import com.exam.project.io.CharacterManagement;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Builder per il menu di gioco usando il pattern Composite
 */
public class GameMenuBuilder {
    
    private static final Logger logger = GameLogger.getLogger();
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Character> characters = new ArrayList<>();
    private static Character currentCharacter = null;
    
    /**
     * Costruisce il menu principale del gioco
     */
    public static GameMenu buildMainMenu() {
        try {
            GameMenu mainMenu = new GameMenu("Menu Principale");
            
            // Aggiungi voci di menu principali
            mainMenu.add(new MenuItem("Crea Personaggio", GameMenuBuilder::createCharacter));
            mainMenu.add(new MenuItem("Seleziona Personaggio", GameMenuBuilder::selectCharacter));
            mainMenu.add(new MenuItem("Mostra Personaggio", GameMenuBuilder::showCharacter));
            
            // Crea sottomenu Gioca
            GameMenu playMenu = new GameMenu("Gioca");
            playMenu.add(new MenuItem("Esplora Dungeon", GameMenuBuilder::exploreDungeon));
            playMenu.add(new MenuItem("Addestra Personaggio", GameMenuBuilder::trainCharacter));
            mainMenu.add(playMenu);
            
            // Crea sottomenu Gestione
            GameMenu managementMenu = new GameMenu("Gestione");
            managementMenu.add(new MenuItem("Mostra Tutti i Personaggi", GameMenuBuilder::showAllCharacters));
            mainMenu.add(managementMenu);
            
            return mainMenu;
        } catch (Exception e) {
            logger.severe("Errore nella creazione del menu: " + e.getMessage());
            // Fallback menu di emergenza
            GameMenu emergencyMenu = new GameMenu("Menu");
            emergencyMenu.add(new MenuItem("Esci", () -> {}));
            return emergencyMenu;
        }
    }
    
    /**
     * Crea un nuovo personaggio
     */
    private static void createCharacter() {
        try {
            System.out.println("\n=== CREA PERSONAGGIO ===");
            
            CharacterFactory factory = new CharacterFactory();
            factory.showAvailableTypes();
            
            // Input tipo
            System.out.print("\nTipo (warrior/mage): ");
            String type = InputValidator.sanitizeInput(scanner.nextLine());
            
            // Input nome
            System.out.print("Nome: ");
            String name = InputValidator.sanitizeInput(scanner.nextLine());
            
            if (name.isEmpty()) {
                System.out.println("Nome non valido!");
                return;
            }
            
            // Crea personaggio
            Character character = factory.createCharacter(type, name);
            
            if (character != null) {
                characters.add(character);
                currentCharacter = character;
                System.out.println("Personaggio creato: " + character.getName());
            } else {
                System.out.println("Creazione personaggio fallita!");
            }
        } catch (Exception e) {
            logger.warning("Errore nella creazione del personaggio: " + e.getMessage());
            System.out.println("Si è verificato un errore. Riprova.");
        }
    }
    
    /**
     * Seleziona un personaggio esistente
     */
    private static void selectCharacter() {
        try {
            if (characters.isEmpty()) {
                System.out.println("\nNessun personaggio disponibile!");
                return;
            }
            
            System.out.println("\n=== SELEZIONA PERSONAGGIO ===");
            
            // Mostra personaggi
            for (int i = 0; i < characters.size(); i++) {
                Character c = characters.get(i);
                String current = (c == currentCharacter) ? " [ATTUALE]" : "";
                System.out.println((i+1) + ". " + c.getName() + current);
            }
            
            // Input selezione
            System.out.print("\nNumero: ");
            String input = scanner.nextLine();
            
            try {
                int choice = Integer.parseInt(input.trim());
                if (choice > 0 && choice <= characters.size()) {
                    currentCharacter = characters.get(choice-1);
                    System.out.println("Selezionato: " + currentCharacter.getName());
                } else {
                    System.out.println("Selezione non valida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Inserisci un numero valido!");
            }
        } catch (Exception e) {
            logger.warning("Errore nella selezione del personaggio: " + e.getMessage());
            System.out.println("Si è verificato un errore. Riprova.");
        }
    }
    
    /**
     * Mostra il personaggio attuale
     */
    private static void showCharacter() {
        try {
            if (currentCharacter == null) {
                System.out.println("\nNessun personaggio selezionato!");
                return;
            }
            
            System.out.println("\n=== PERSONAGGIO ATTUALE ===");
            System.out.println(currentCharacter);
        } catch (Exception e) {
            logger.warning("Errore nella visualizzazione del personaggio: " + e.getMessage());
            System.out.println("Si è verificato un errore. Riprova.");
        }
    }
    
    /**
     * Esplora un dungeon e combatti
     */
    private static void exploreDungeon() {
        try {
            if (currentCharacter == null) {
                System.out.println("\nSeleziona prima un personaggio!");
                return;
            }
            
            System.out.println("\n=== ESPLORA DUNGEON ===");
            
            // Scegli dungeon
            System.out.println("1. Grotta dei Goblin");
            System.out.println("2. Palude dei Troll");
            System.out.print("Scelta: ");
            
            String input = scanner.nextLine();
            int choice;
            
            try {
                choice = Integer.parseInt(input.trim());
                if (choice < 1 || choice > 2) {
                    System.out.println("Scelta non valida!");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Inserisci un numero valido!");
                return;
            }
            
            // Crea mostro
            MonsterFactory monsterFactory = new MonsterFactory();
            String monsterType = (choice == 1) ? "goblin" : "troll";
            
            System.out.println("\nHai incontrato un " + monsterType + "!");
            
            // Combatti
            CombatSystem combat = new CombatSystem();
            currentCharacter = combat.doCombat(currentCharacter, 
                                              monsterFactory.createMonster(monsterType, 1));
        } catch (Exception e) {
            logger.warning("Errore nell'esplorazione del dungeon: " + e.getMessage());
            System.out.println("Si è verificato un errore. Riprova.");
        }
    }
    
    /**
     * Addestra il personaggio
     */
    private static void trainCharacter() {
        try {
            if (currentCharacter == null) {
                System.out.println("\nSeleziona prima un personaggio!");
                return;
            }
            
            System.out.println("\n=== ADDESTRAMENTO ===");
            System.out.println("Statistiche attuali: " + currentCharacter);
            
            System.out.print("\nProcedere? (s/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("s")) {
                currentCharacter.train();
                System.out.println("Addestramento completato!");
                System.out.println("Nuove statistiche: " + currentCharacter);
            }
        } catch (Exception e) {
            logger.warning("Errore nell'addestramento: " + e.getMessage());
            System.out.println("Si è verificato un errore. Riprova.");
        }
    }
    
    /**
     * Mostra tutti i personaggi
     */
    private static void showAllCharacters() {
        try {
            System.out.println("\n=== TUTTI I PERSONAGGI ===");
            
            if (characters.isEmpty()) {
                System.out.println("Nessun personaggio disponibile!");
                return;
            }
            
            for (int i = 0; i < characters.size(); i++) {
                Character c = characters.get(i);
                String current = (c == currentCharacter) ? " [ATTUALE]" : "";
                System.out.println((i+1) + ". " + c.getName() + current);
                System.out.println("   " + c);
                System.out.println();
            }
        } catch (Exception e) {
            logger.warning("Errore nella visualizzazione dei personaggi: " + e.getMessage());
            System.out.println("Si è verificato un errore. Riprova.");
        }
    }
    
    /**
     * Ottieni il personaggio attuale
     */
    public static Character getCurrentCharacter() {
        return currentCharacter;
    }
    
    /**
     * Ottieni tutti i personaggi
     */
    public static List<Character> getCharacters() {
        return new ArrayList<>(characters);
    }
}