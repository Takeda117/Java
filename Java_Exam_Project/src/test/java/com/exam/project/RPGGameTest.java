package com.exam.project;

import com.exam.project.factory.Character;
import com.exam.project.factory.CharacterFactory;
import com.exam.project.factory.Warrior;
import com.exam.project.factory.Mage;
import com.exam.project.iterator.*;

import com.exam.project.builder.*;
import com.exam.project.factoryMonster.*;
import com.exam.project.security.InputValidator;
import com.exam.project.strategy.*;
import com.exam.project.logger.GameLogger;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Basic tests for RPG game
 */
public class RPGGameTest {

    private static final Logger logger = GameLogger.getLogger();
    private CharacterFactory factory;
    private MonsterFactory monsterFactory;
    private Inventory inventory;

    @Before
    public void setUp() {
        logger.info("Setting up test environment");
        try {
            factory = new CharacterFactory();
            monsterFactory = new MonsterFactory();
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

    @Test
    public void testCreateMage() {
        logger.info("Testing mage creation");
        try {
            Character mage = factory.createCharacter("mage", "TestMage");

            assertNotNull(mage);
            assertTrue(mage instanceof Mage);
            assertTrue(mage.isAlive());

            Mage m = (Mage) mage;
            assertTrue(m.getMana() > 0);
            logger.info("Mage creation test passed");
        } catch (Exception e) {
            logger.severe("Mage creation test failed: " + e.getMessage());
            fail("Mage creation failed");
        }
    }

    @Test
    public void testBadCharacterType() {
        logger.info("Testing invalid character type");
        try {
            factory.createCharacter("badtype", "Test");
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            logger.info("Correctly caught exception for bad character type");
            // Good, expected this
        } catch (Exception e) {
            logger.warning("Unexpected exception: " + e.getMessage());
            fail("Unexpected exception type");
        }
    }

    // Test monsters
    @Test
    public void testCreateMonsters() {
        logger.info("Testing monster creation");
        try {
            AbstractMonster goblin = monsterFactory.createMonster("goblin", 1);
            AbstractMonster troll = monsterFactory.createMonster("troll", 2);

            assertNotNull(goblin);
            assertNotNull(troll);
            assertTrue(goblin instanceof Goblin);
            assertTrue(troll instanceof Troll);
            assertTrue(goblin.isAlive());
            assertTrue(troll.isAlive());
            logger.info("Monster creation test passed");
        } catch (Exception e) {
            logger.severe("Monster creation test failed: " + e.getMessage());
            fail("Monster creation failed");
        }
    }

    // Test inventory iterator
    @Test
    public void testInventoryIterator() {
        logger.info("Testing inventory iterator");
        try {
            Item sword = new Item("Sword", Item.ItemType.WEAPON, 50, 3);
            Item potion = new Item("Potion", Item.ItemType.POTION, 25, 0);

            inventory.addItem(sword);
            inventory.addItem(potion);

            Iterator<Item> iter = inventory.iterator();
            assertTrue(iter.hasNext());

            Item first = iter.next();
            assertEquals("Sword", first.getName());

            assertTrue(iter.hasNext());
            Item second = iter.next();
            assertEquals("Potion", second.getName());

            assertFalse(iter.hasNext());
            logger.info("Inventory iterator test passed");
        } catch (Exception e) {
            logger.severe("Inventory iterator test failed: " + e.getMessage());
            fail("Iterator test failed");
        }
    }

    // Test inventory for-each
    @Test
    public void testInventoryForEach() {
        Item item = new Item("Test", Item.ItemType.MISC, 10, 0);
        inventory.addItem(item);

        int count = 0;
        for (Item i : inventory) {
            assertNotNull(i);
            count++;
        }
        assertEquals(1, count);
    }

    // Test collections
    @Test
    public void testInventoryCollections() {
        assertTrue(inventory.isEmpty());
        assertEquals(0, inventory.getSize());

        Item weapon = new Item("Axe", Item.ItemType.WEAPON, 30, 2);
        Item armor = new Item("Shield", Item.ItemType.ARMOR, 40, 1);

        inventory.addItem(weapon);
        inventory.addItem(armor);

        assertEquals(2, inventory.getSize());
        assertFalse(inventory.isEmpty());

        // Test filtering by type
        var weapons = inventory.getItemsByType(Item.ItemType.WEAPON);
        assertEquals(1, weapons.size());
        assertEquals("Axe", weapons.get(0).getName());
    }

    // Test equipment
    @Test
    public void testEquipment() {
        Item sword = new Item("Magic Sword", Item.ItemType.WEAPON, 100, 5);
        inventory.addItem(sword);

        assertTrue(inventory.equipItem(sword));
        assertTrue(inventory.isEquipped(sword));
        assertEquals(5, inventory.getTotalStatBonus());
    }

    // Test builder pattern
    @Test
    public void testDungeonBuilder() {
        Dungeon cave = DungeonFactory.createGoblinCave();
        assertNotNull(cave);
        assertEquals("Goblin Cave", cave.getName());
        assertEquals(4, cave.getNumberOfRooms());

        Dungeon swamp = DungeonFactory.createSwampOfTrolls();
        assertNotNull(swamp);
        assertEquals("Swamp of Trolls", swamp.getName());
        assertEquals(6, swamp.getNumberOfRooms());
    }

    // Test combat strategy
    @Test
    public void testCombatStrategy() {
        Character warrior = factory.createCharacter("warrior", "Fighter");
        WarriorStrategy strategy = new WarriorStrategy();

        int damage = strategy.calculateDamage(warrior);
        assertTrue(damage > 0);
        assertEquals(5, strategy.getStaminaCost());
    }

    // Test combat system
    @Test
    public void testCombatSystem() {
        logger.info("Testing combat system");
        try {
            Character warrior = factory.createCharacter("warrior", "Hero");
            AbstractMonster goblin = monsterFactory.createMonster("goblin", 1);
            CombatSystem combat = new CombatSystem();

            assertTrue(combat.canFight(warrior));
            assertTrue(combat.canFight(goblin));

            int startHealth = goblin.getHealth();
            int damage = combat.executeAttack(warrior, goblin);

            if (damage > 0) {
                assertTrue(goblin.getHealth() < startHealth);
            }
            logger.info("Combat system test passed");
        } catch (Exception e) {
            logger.severe("Combat system test failed: " + e.getMessage());
            fail("Combat test failed");
        }
    }

    // Test input validation
    @Test
    public void testInputValidation() {
        assertEquals("TestName", InputValidator.validateCharacterName("TestName"));
        assertNull(InputValidator.validateCharacterName(""));

        assertEquals(Integer.valueOf(2), InputValidator.validateMenuChoice("2", 5));
        assertNull(InputValidator.validateMenuChoice("10", 5));

        assertTrue(InputValidator.validateYesNo("y"));
        assertFalse(InputValidator.validateYesNo("n"));
    }

    // Test character combat
    @Test
    public void testCharacterFight() {
        logger.info("Testing character combat actions");
        try {
            Character warrior = factory.createCharacter("warrior", "TestWarrior");

            int startStamina = warrior.getStamina();
            int damage = warrior.attack();
            assertTrue(damage > 0);
            assertTrue(warrior.getStamina() < startStamina);

            int startHealth = warrior.getHealth();
            warrior.takeDamage(10);
            assertTrue(warrior.getHealth() < startHealth);
            assertTrue(warrior.isAlive());
            logger.info("Character combat test passed");
        } catch (Exception e) {
            logger.severe("Character combat test failed: " + e.getMessage());
            fail("Character combat failed");
        }
    }

    // Test training
    @Test
    public void testTraining() {
        logger.info("Testing character training");
        try {
            Character mage = factory.createCharacter("mage", "Student");

            int startDamage = mage.getBaseDamage();
            int startMaxStamina = mage.getMaxStamina();

            mage.train();

            assertTrue(mage.getBaseDamage() > startDamage);
            assertTrue(mage.getMaxStamina() > startMaxStamina);

            logger.info("Training test passed");
        } catch (Exception e) {
            logger.severe("Training test failed: " + e.getMessage());
            fail("Training failed");
        }
    }

    // Test rest
    @Test
    public void testRest() {
        logger.info("Testing character rest");
        try {
            Character character = factory.createCharacter("warrior", "Tired");

            character.takeDamage(20);
            character.restoreStamina(-30);

            int lowHealth = character.getHealth();
            int lowStamina = character.getStamina();

            character.rest();

            assertTrue(character.getHealth() > lowHealth);
            assertTrue(character.getStamina() > lowStamina);
            assertEquals(character.getMaxHealth(), character.getHealth());
            assertEquals(character.getMaxStamina(), character.getStamina());
            logger.info("Rest test passed");
        } catch (Exception e) {
            logger.severe("Rest test failed: " + e.getMessage());
            fail("Rest failed");
        }
    }
}