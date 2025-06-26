package com.exam.project;

import factory.Character;
import factory.CharacterFactory;
import factory.Warrior;
import iterator.*;
import builder.*;
import logger.GameLogger;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.logging.Logger;

/**
 * Basic tests for RPG game
 */
public class RPGGameTest {

    private static final Logger logger = GameLogger.getLogger();
    private CharacterFactory factory;
    private Inventory inventory;

    @Before
    public void setUp() {
        logger.info("Setting up test environment");
        try {
            factory = new CharacterFactory();
            inventory = new Inventory(10);
            logger.info("Test setup completed successfully");
        } catch (Exception e) {
            logger.severe("Test setup failed: " + e.getMessage());
            fail("Test setup failed");
        }
    }

    // Test character creation
    @Test
    public void testCreateWarrior() {
        logger.info("Testing warrior creation");
        try {
            Character warrior = factory.createCharacter("warrior", "TestGuy");

            assertNotNull(warrior);
            assertEquals("TestGuy", warrior.getName());
            assertTrue(warrior instanceof Warrior);
            assertTrue(warrior.isAlive());
            assertTrue(warrior.getHealth() > 0);
            logger.info("Warrior creation test passed");
        } catch (Exception e) {
            logger.severe("Warrior creation test failed: " + e.getMessage());
            fail("Warrior creation failed");
        }
    }

    // Test inventory functionality
    @Test
    public void testInventoryOperations() {
        logger.info("Testing inventory operations");
        try {
            // Test basic inventory operations
            assertTrue(inventory.isEmpty());
            assertEquals(0, inventory.getSize());

            Item weapon = new Item("Axe", Item.ItemType.WEAPON, 30, 2);
            Item armor = new Item("Shield", Item.ItemType.ARMOR, 40, 1);

            inventory.addItem(weapon);
            inventory.addItem(armor);

            assertEquals(2, inventory.getSize());
            assertFalse(inventory.isEmpty());

            // Test equipment functionality
            assertTrue(inventory.equipItem(weapon));
            assertTrue(inventory.isEquipped(weapon));
            assertEquals(2, inventory.getTotalStatBonus());
            
            logger.info("Inventory operations test passed");
        } catch (Exception e) {
            logger.severe("Inventory operations test failed: " + e.getMessage());
            fail("Inventory operations failed");
        }
    }

    // Test dungeon builder pattern
    @Test
    public void testDungeonBuilder() {
        logger.info("Testing dungeon builder");
        try {
            DungeonBuilder builder = new ConcreteDungeonBuilder();
            
            Dungeon cave = builder.reset()
                    .setName("Goblin Cave")
                    .setDescription("Una grotta oscura piena di goblin.")
                    .setGoldReward(100)
                    .setMonsterType("goblin")
                    .build();
                    
            assertNotNull(cave);
            assertEquals("Goblin Cave", cave.getName());
            assertEquals("goblin", cave.getMonsterType());
            assertEquals(100, cave.getGoldReward());
            
            logger.info("Dungeon builder test passed");
        } catch (Exception e) {
            logger.severe("Dungeon builder test failed: " + e.getMessage());
            fail("Dungeon builder failed");
        }
    }
}
