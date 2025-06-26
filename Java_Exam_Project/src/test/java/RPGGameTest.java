import rpg.builder.ConcreteDungeonBuilder;
import rpg.builder.Dungeon;
import rpg.builder.DungeonBuilder;
import rpg.factory.Character;
import rpg.factory.CharacterFactory;
import rpg.factory.Warrior;

import rpg.iterator.Inventory;
import rpg.iterator.Item;
import rpg.logger.GameLogger;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.logging.Logger;

/**
 * RPGGameTest - Integration tests for the RPG game components
 * <p>
 * This test class provides integration tests for the core components of the RPG game,
 * verifying that they work together correctly. Unlike the mockito tests that focus on
 * isolated unit testing, these tests use actual implementations to ensure that the
 * components integrate properly.
 * </p>
 * <p>
 * The tests cover several key aspects of the game:
 * <ul>
 *   <li>Character creation and basic properties</li>
 *   <li>Inventory management and item operations</li>
 *   <li>Dungeon creation using the Builder pattern</li>
 * </ul>
 * </p>
 * <p>
 * Each test includes comprehensive logging to track test execution and
 * facilitate debugging if issues occur.
 * </p>
 */
public class RPGGameTest {

    private static final Logger logger = GameLogger.getLogger();
    private CharacterFactory factory;
    private Inventory inventory;

    /**
     * Sets up the test environment before each test
     * <p>
     * This method initializes the common components needed for testing:
     * <ul>
     *   <li>A CharacterFactory for creating game characters</li>
     *   <li>An Inventory with a capacity of 10 items</li>
     * </ul>
     * </p>
     * <p>
     * The setup is wrapped in a try-catch block to ensure that any initialization
     * failures are properly logged and cause the test to fail with a clear message.
     * </p>
     * 
     * @throws Exception If initialization of test components fails
     */
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

    /**
     * Tests warrior character creation
     * <p>
     * This test verifies that the CharacterFactory correctly creates a warrior character
     * with the expected properties. It checks:
     * <ul>
     *   <li>The character is successfully created (not null)</li>
     *   <li>The character has the correct name</li>
     *   <li>The character is of the correct type (Warrior)</li>
     *   <li>The character is alive</li>
     *   <li>The character has positive health</li>
     * </ul>
     * </p>
     * <p>
     * This test ensures that the basic character creation functionality works correctly,
     * which is fundamental to the game's operation.
     * </p>
     */
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

    /**
     * Tests inventory functionality
     * <p>
     * This test verifies the core functionality of the inventory system, including:
     * <ul>
     *   <li>Initial inventory state (empty, size zero)</li>
     *   <li>Adding items to the inventory</li>
     *   <li>Checking inventory size after additions</li>
     *   <li>Equipping items</li>
     *   <li>Verifying equipped status</li>
     *   <li>Calculating stat bonuses from equipped items</li>
     * </ul>
     * </p>
     * <p>
     * This test ensures that the inventory system, which is a critical component
     * of character progression, functions correctly.
     * </p>
     */
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

    /**
     * Tests dungeon builder pattern
     * <p>
     * This test verifies that the Builder pattern implementation for dungeon creation
     * works correctly. It checks:
     * <ul>
     *   <li>Creating a builder and resetting it</li>
     *   <li>Setting various dungeon properties (name, description, rewards, etc.)</li>
     *   <li>Building the final dungeon object</li>
     *   <li>Verifying that the built dungeon has the correct properties</li>
     * </ul>
     * </p>
     * <p>
     * This test ensures that the Builder pattern is correctly implemented and that
     * dungeons can be created with the desired properties, which is essential for
     * the game's content creation.
     * </p>
     */
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