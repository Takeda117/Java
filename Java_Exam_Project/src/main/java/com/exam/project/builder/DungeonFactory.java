package com.exam.project.builder;

import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;

/**
 * DungeonFactory - Usa il Builder Pattern per creare i 2 dungeon
 *
 * Versione semplificata: usa ConcreteDungeonBuilder
 */
public class DungeonFactory {

    private static final Logger logger = GameLogger.getLogger();
    private static final DungeonBuilder builder = new ConcreteDungeonBuilder();

    /**
     * Crea Goblin Cave usando Builder Pattern
     */
    public static Dungeon createGoblinCave() {
        try {
            return builder.reset()
                    .setName("Goblin Cave")
                    .setDescription("Una grotta oscura piena di goblin. Perfetta per principianti.")
                    .setGoldReward(100)
                    .setMonsterType("goblin")
                    .build();

        } catch (Exception e) {
            logger.severe("Error creating Goblin Cave: " + e.getMessage());
            // Fallback di emergenza
            return builder.reset()
                    .setName("Emergency Cave")
                    .setMonsterType("goblin")
                    .build();
        }
    }

    /**
     * Crea Swamp of Trolls usando Builder Pattern
     */
    public static Dungeon createSwampOfTrolls() {
        try {
            return builder.reset()
                    .setName("Swamp of Trolls")
                    .setDescription("Una palude pericolosa dove si nascondono potenti troll.")
                    .setGoldReward(200)
                    .setMonsterType("troll")
                    .build();

        } catch (Exception e) {
            logger.severe("Error creating Swamp of Trolls: " + e.getMessage());
            // Fallback di emergenza
            return builder.reset()
                    .setName("Emergency Swamp")
                    .setMonsterType("troll")
                    .build();
        }
    }

    /**
     * Mostra i 2 dungeon disponibili
     */
    public static void showAvailableDungeons() {
        try {
            System.out.println("\n=== DUNGEON DISPONIBILI ===\n");

            Dungeon goblinCave = createGoblinCave();
            System.out.println("1. " + goblinCave.getName());
            System.out.println("   " + goblinCave.toString());
            System.out.println("   Difficoltà: ★☆☆ (Principiante)");

            System.out.println();

            Dungeon trollSwamp = createSwampOfTrolls();
            System.out.println("2. " + trollSwamp.getName());
            System.out.println("   " + trollSwamp.toString());
            System.out.println("   Difficoltà: ★★★ (Avanzato)");

            System.out.println("\nScegli in base al tuo livello di esperienza!");

        } catch (Exception e) {
            logger.severe("Error showing dungeons: " + e.getMessage());
            System.out.println("Errore nel mostrare i dungeon.");
        }
    }

    /**
     * Crea dungeon per scelta - SOLO 1 o 2
     */
    public static Dungeon createDungeonByChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    System.out.println("\nHai scelto la Goblin Cave!");
                    return createGoblinCave();

                case 2:
                    System.out.println("\nHai scelto la Swamp of Trolls!");
                    return createSwampOfTrolls();

                default:
                    System.out.println("Scelta non valida! Usando Goblin Cave.");
                    return createGoblinCave();
            }
        } catch (Exception e) {
            logger.severe("Error creating dungeon by choice: " + e.getMessage());
            return createGoblinCave();
        }
    }

    /**
     * Metodo per testare il Builder Pattern
     */
    public static void testBuilderPattern() {
        try {
            System.out.println("\n=== TEST BUILDER PATTERN ===");

            // Test creazione custom
            DungeonBuilder testBuilder = new ConcreteDungeonBuilder();

            Dungeon customDungeon = testBuilder
                    .setName("Test Dungeon")
                    .setDescription("Dungeon di test")
                    .setGoldReward(75)
                    .setMonsterType("goblin")
                    .build();

            System.out.println("Custom dungeon creato: " + customDungeon);

            // Test reset e riutilizzo
            Dungeon anotherDungeon = testBuilder.reset()
                    .setName("Another Test")
                    .setGoldReward(150)
                    .setMonsterType("troll")
                    .build();

            System.out.println("Secondo dungeon: " + anotherDungeon);

        } catch (Exception e) {
            logger.severe("Error testing builder pattern: " + e.getMessage());
            System.out.println("Errore nel test del Builder Pattern!");
        }
    }
}