package com.exam.project.menu;

import com.exam.project.composite.*;
import com.exam.project.factory.Character;
import com.exam.project.builder.ConcreteDungeonBuilder;
import com.exam.project.builder.Dungeon;
import com.exam.project.builder.DungeonBuilder;
import com.exam.project.builder.DungeonExplorer;
import com.exam.project.menu.CharacterMenu.ReturnToMainMenuException;
import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;

/**
 * DungeonMenu - Manages dungeon exploration menu
 */
public class DungeonMenu {
    private static final Logger logger = GameLogger.getLogger();
    private static final DungeonBuilder dungeonBuilder = new ConcreteDungeonBuilder();
    
    /**
     * Shows dungeon menu
     */
    public static void showDungeonMenu(Character character) {
        if (character == null || !character.isAlive()) {
            logger.warning("Impossibile mostrare menu dungeon: personaggio nullo o morto");
            return;
        }
        
        try {
            logger.info("Mostrando menu dungeon per " + character.getName());
            GameMenu menu = new GameMenu("Esplora Dungeon");
            
            menu.add(new MenuItem("Goblin Cave", () -> enterGoblinCave(character)));
            menu.add(new MenuItem("Swamp of Trolls", () -> enterSwamp(character)));
            menu.add(new MenuItem("Torna al menu personaggio", () -> {}));
            
            menu.execute();
        } catch (ReturnToMainMenuException e) {
            logger.info("Ritorno al menu principale");
            throw e;
        } catch (Exception e) {
            logger.severe("Errore nel menu dungeon: " + e.getMessage());
            System.out.println("Errore nel menu dungeon.");
        }
    }
    
    /**
     * Enters Goblin Cave dungeon
     */
    private static void enterGoblinCave(Character character) {
        System.out.println("\n=== GOBLIN CAVE ===");
        logger.info(character.getName() + " entra nella Goblin Cave");
        
        Dungeon goblinCave = buildDungeon("Goblin Cave", "Una grotta piena di goblin.", 100, "goblin");
        exploreDungeon(character, goblinCave);
    }
    
    /**
     * Enters Swamp of Trolls dungeon
     */
    private static void enterSwamp(Character character) {
        System.out.println("\n=== SWAMP OF TROLLS ===");
        logger.info(character.getName() + " entra nella Swamp of Trolls");
        
        Dungeon swamp = buildDungeon("Swamp of Trolls", "Una palude pericolosa con troll.", 200, "troll");
        exploreDungeon(character, swamp);
    }
    
    /**
     * Builds a dungeon with specified parameters
     */
    private static Dungeon buildDungeon(String name, String description, int goldReward, String monsterType) {
        return dungeonBuilder.reset()
                .setName(name)
                .setDescription(description)
                .setGoldReward(goldReward)
                .setMonsterType(monsterType)
                .build();
    }
    
    /**
     * Explores a dungeon with a character
     */
    private static void exploreDungeon(Character character, Dungeon dungeon) {
        new DungeonExplorer()
                .withCharacter(character)
                .withDungeon(dungeon)
                .build();
    }
}