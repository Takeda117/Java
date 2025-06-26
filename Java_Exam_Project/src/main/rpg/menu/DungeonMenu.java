package rpg.menu;


import rpg.composite.GameMenu;
import rpg.composite.MenuItem;
import rpg.factory.Character;
import rpg.builder.ConcreteDungeonBuilder;
import rpg.builder.Dungeon;
import rpg.builder.DungeonBuilder;
import rpg.builder.DungeonExplorer;
import rpg.menu.CharacterMenu.ReturnToMainMenuException;
import rpg.logger.GameLogger;
import rpg.rpgSecurity.ExceptionHandler;
import java.util.logging.Logger;

/**
 * DungeonMenu - Manages dungeon exploration menu
 * <p>
 * This class is responsible for building and executing the dungeon exploration menu.
 * It provides options for exploring different dungeons and handles the creation
 * of dungeon instances using the Builder pattern. The class uses the Composite pattern
 * through the GameMenu and MenuItem classes to structure the menu hierarchy.
 * </p>
 * <p>
 * The DungeonMenu integrates with the DungeonExplorer to manage the actual
 * exploration process and combat encounters within dungeons.
 * </p>
 */
public class DungeonMenu {
    private static final Logger logger = GameLogger.getLogger();
    private static final DungeonBuilder dungeonBuilder = new ConcreteDungeonBuilder();
    
    /**
     * Shows dungeon menu
     * <p>
     * Displays and executes the dungeon exploration menu for a specific character,
     * handling character state validation and menu navigation.
     * </p>
     * 
     * @param character The character for which to show the dungeon menu
     * @throws ReturnToMainMenuException if the user chooses to return to the main menu
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
            ExceptionHandler.handleException(e, "Errore nel menu dungeon.");
        }
    }
    
    /**
     * Enters Goblin Cave dungeon
     * <p>
     * Creates a Goblin Cave dungeon instance and initiates the exploration
     * process for the specified character.
     * </p>
     * 
     * @param character The character that will explore the dungeon
     */
    private static void enterGoblinCave(Character character) {
        System.out.println("\n=== GOBLIN CAVE ===");
        logger.info(character.getName() + " entra nella Goblin Cave");
        
        Dungeon goblinCave = buildDungeon("Goblin Cave", "Una grotta piena di goblin.", 100, "goblin");
        exploreDungeon(character, goblinCave);
    }
    
    /**
     * Enters Swamp of Trolls dungeon
     * <p>
     * Creates a Swamp of Trolls dungeon instance and initiates the exploration
     * process for the specified character.
     * </p>
     * 
     * @param character The character that will explore the dungeon
     */
    private static void enterSwamp(Character character) {
        System.out.println("\n=== SWAMP OF TROLLS ===");
        logger.info(character.getName() + " entra nella Swamp of Trolls");
        
        Dungeon swamp = buildDungeon("Swamp of Trolls", "Una palude pericolosa con troll.", 200, "troll");
        exploreDungeon(character, swamp);
    }
    
    /**
     * Builds a dungeon with specified parameters
     * <p>
     * Uses the DungeonBuilder to create a dungeon with the specified name,
     * description, gold reward, and monster type.
     * </p>
     * 
     * @param name The name of the dungeon
     * @param description The description of the dungeon
     * @param goldReward The gold reward for completing the dungeon
     * @param monsterType The type of monsters in the dungeon
     * @return A configured Dungeon instance
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
     * <p>
     * Uses the DungeonExplorer to manage the exploration process for a character
     * in a specific dungeon, including combat encounters and rewards.
     * </p>
     * 
     * @param character The character that will explore the dungeon
     * @param dungeon The dungeon to explore
     */
    private static void exploreDungeon(Character character, Dungeon dungeon) {
        new DungeonExplorer()
                .withCharacter(character)
                .withDungeon(dungeon)
                .build();
    }
}