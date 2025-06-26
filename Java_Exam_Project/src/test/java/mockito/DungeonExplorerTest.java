package mockito;

import java.builder.Dungeon;
import java.builder.DungeonExplorer;
import java.combat.CombatSystem;
import java.factory.Character;
import java.factoryMonster.AbstractMonster;
import java.factoryMonster.MonsterFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * DungeonExplorerTest - Unit tests for the DungeonExplorer class using Mockito
 * <p>
 * This test class demonstrates the use of Mockito to isolate the DungeonExplorer class
 * from its dependencies during testing. By mocking the Character, Dungeon, MonsterFactory,
 * AbstractMonster, and CombatSystem classes, we can test the DungeonExplorer's behavior
 * without relying on the actual implementations of these dependencies.
 * </p>
 * <p>
 * The tests focus on verifying that:
 * <ul>
 *   <li>The DungeonExplorer correctly interacts with its dependencies</li>
 *   <li>The exploration process follows the expected flow</li>
 *   <li>The DungeonExplorer returns appropriate results based on combat outcomes</li>
 * </ul>
 * </p>
 */
public class DungeonExplorerTest {

    @Mock
    private Character mockCharacter;
    
    @Mock
    private Dungeon mockDungeon;
    
    @Mock
    private MonsterFactory mockMonsterFactory;
    
    @Mock
    private AbstractMonster mockMonster;
    
    @Mock
    private CombatSystem mockCombatSystem;
    
    private DungeonExplorer dungeonExplorer;

    /**
     * Sets up the test environment before each test
     * <p>
     * This method initializes all mock objects and configures their behavior
     * for the tests. It also creates a DungeonExplorer instance and uses
     * reflection to inject the mocked MonsterFactory.
     * </p>
     * <p>
     * The setup configures the mocks to simulate a successful dungeon exploration
     * where the monster is defeated and the character survives.
     * </p>
     * 
     * @throws Exception If mock initialization or reflection fails
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mock behavior
        when(mockDungeon.getMonsterType()).thenReturn("goblin");
        when(mockDungeon.getName()).thenReturn("Test Dungeon");
        when(mockMonsterFactory.createMonster("goblin")).thenReturn(mockMonster);
        when(mockCharacter.isAlive()).thenReturn(true);
        when(mockMonster.isAlive()).thenReturn(false); // Monster dies in combat
        
        // Create dungeonExplorer
        dungeonExplorer = new DungeonExplorer();
        
        // Use reflection to set the monsterFactory field
        try {
            java.lang.reflect.Field field = DungeonExplorer.class.getDeclaredField("monsterFactory");
            field.setAccessible(true);
            field.set(dungeonExplorer, mockMonsterFactory);
        } catch (Exception e) {
            fail("Failed to set monsterFactory field: " + e.getMessage());
        }
    }

    /**
     * Tests a successful dungeon exploration scenario
     * <p>
     * This test verifies that when a character explores a dungeon and defeats
     * the monster, the DungeonExplorer correctly:
     * <ul>
     *   <li>Creates the appropriate monster type based on the dungeon</li>
     *   <li>Checks if the monster is alive after combat</li>
     *   <li>Returns true to indicate successful exploration</li>
     * </ul>
     * </p>
     */
    @Test
    public void testSuccessfulDungeonExploration() {
        // Setup the dungeonExplorer with character and dungeon
        dungeonExplorer.withCharacter(mockCharacter).withDungeon(mockDungeon);
        
        // Execute the build method which starts the exploration
        boolean result = dungeonExplorer.build();
        
        // Verify interactions
        verify(mockMonsterFactory).createMonster("goblin");
        verify(mockMonster).isAlive();
        
        // Verify the result
        assertTrue("Exploration should be successful when monster is defeated", result);
    }
}