package java.builder;

import java.combat.CombatSystem;
import java.factory.Character;
import java.factoryMonster.AbstractMonster;
import java.factoryMonster.MonsterFactory;
import java.iterator.Item;
import java.observer.StaminaRecoverySystem;
import java.logger.GameLogger;
import java.security.ExceptionHandler;
import java.util.logging.Logger;
import java.util.Scanner;
import java.util.List;

/**
 * DungeonExplorer - Manages the exploration of a dungeon
 * <p>
 * This class uses a fluent interface (a variation of the Builder pattern) to configure
 * and execute a dungeon exploration. It handles the combat between the character and
 * monsters found in the dungeon.
 * </p>
 */
public class DungeonExplorer {
    
    private static final Logger logger = GameLogger.getLogger();
    
    private Character character;
    private Dungeon dungeon;
    private final Scanner scanner;
    private final CombatSystem combatSystem;
    private final MonsterFactory monsterFactory;
    
    /**
     * Constructor that initializes the explorer with required components
     */
    public DungeonExplorer() {
        this.scanner = new Scanner(System.in);
        this.combatSystem = new CombatSystem();
        this.monsterFactory = new MonsterFactory();
    }
    
    /**
     * Sets the character that will explore the dungeon
     * 
     * @param character The character to use for exploration
     * @return The explorer instance for method chaining
     */
    public DungeonExplorer withCharacter(Character character) {
        this.character = character;
        return this;
    }
    
    /**
     * Sets the dungeon to be explored
     * 
     * @param dungeon The dungeon to explore
     * @return The explorer instance for method chaining
     */
    public DungeonExplorer withDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
        return this;
    }
    
    /**
     * Starts the dungeon exploration
     * <p>
     * This method creates a monster based on the dungeon's monster type,
     * initiates combat, and handles rewards if the character is victorious.
     * </p>
     * 
     * @return true if the exploration was successful (character survived), false otherwise
     */
    public boolean build() {
        if (character == null || dungeon == null) {
            logger.warning("Cannot explore dungeon: character or dungeon is null");
            return false;
        }
        
        logger.info(character.getName() + " explores " + dungeon.getName());
        System.out.println("\nYou are exploring " + dungeon.getName());
        System.out.println(dungeon.getDescription());
        
        // Create a monster
        AbstractMonster monster = monsterFactory.createMonster(dungeon.getMonsterType());
        if (monster == null) {
            logger.warning("No monster created for type: " + dungeon.getMonsterType());
            System.out.println("There are no monsters here.");
            return false;
        }
        
        System.out.println("\nYou encountered a " + monster.getType() + "!");
        
        // Fight
        boolean victory = combat(monster);
        
        // Recover stamina after the dungeon
        if (victory) {
            StaminaRecoverySystem.recoverStamina(character);
        }
        
        return victory;
    }
    
    /**
     * Handles the combat system between the character and a monster
     * <p>
     * This method manages the turn-based combat, allowing the player to attack
     * and handling the monster's counterattacks until one of them is defeated.
     * </p>
     * 
     * @param monster The monster to fight against
     * @return true if the character won the combat, false otherwise
     */
    private boolean combat(AbstractMonster monster) {
        try {
            while (monster.isAlive() && character.isAlive()) {
                // Show status
                System.out.println("\nHP: " + character.getHealth() + "/" + character.getMaxHealth());
                System.out.println("Enemy: " + monster.getHealth() + " HP");
                
                // Player's turn
                System.out.println("\n1. Attack");
                System.out.print("What do you do? ");
                scanner.nextLine();
                
                combatSystem.executeAttack(character, monster);
                
                // Check if the monster is dead
                if (!monster.isAlive()) {
                    logger.info(character.getName() + " defeated " + monster.getType());
                    System.out.println("\nYou won!");
                    System.out.println("You earned " + dungeon.getGoldReward() + " gold!");
                    
                    // Add dropped items to the character's inventory
                    List<Item> droppedItems = monster.getDroppedItems();
                    if (!droppedItems.isEmpty()) {
                        System.out.println("\nYou found:");
                        for (Item item : droppedItems) {
                            character.addItem(item);
                            // Confirmation message is already shown in the addItem method
                        }
                    }
                    
                    return true;
                }
                
                // Monster's turn
                combatSystem.executeMonsterAttack(monster, character);
                
                // Check if the character is dead
                if (!character.isAlive()) {
                    logger.info(character.getName() + " was defeated by " + monster.getType());
                    System.out.println("\nYou have been defeated!");
                    return false;
                }
            }
            
            return character.isAlive();
        } catch (Exception e) {
            logger.severe("Error during combat: " + e.getMessage());
            ExceptionHandler.handleException(e, "An error occurred during combat.");
            return false;
        }
    }
}