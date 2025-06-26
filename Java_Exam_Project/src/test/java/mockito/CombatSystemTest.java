package mockito;

import rpg.combat.CombatSystem;
import rpg.factory.Character;
import rpg.factoryMonster.AbstractMonster;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * CombatSystemTest - Unit tests for the CombatSystem class using Mockito
 * <p>
 * This test class demonstrates the use of Mockito to isolate the CombatSystem class
 * from its dependencies during testing. By mocking the Character and AbstractMonster
 * classes, we can test the CombatSystem's behavior without relying on the actual
 * implementations of these dependencies.
 * </p>
 * <p>
 * The tests focus on verifying that:
 * <ul>
 *   <li>The CombatSystem correctly delegates attack calculations to characters and monsters</li>
 *   <li>Damage is properly applied to the target of attacks</li>
 *   <li>The system handles edge cases like null inputs appropriately</li>
 * </ul>
 * </p>
 */
public class CombatSystemTest {

    @Mock
    private Character mockCharacter;
    
    @Mock
    private AbstractMonster mockMonster;
    
    private CombatSystem combatSystem;

    /**
     * Sets up the test environment before each test
     * <p>
     * This method initializes all mock objects and creates a fresh
     * CombatSystem instance for each test to ensure test isolation.
     * </p>
     */
    @Before
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        combatSystem = new CombatSystem();
    }

    /**
     * Tests that character attacks are properly executed
     * <p>
     * This test verifies that when a character attacks a monster:
     * <ul>
     *   <li>The character's attack method is called to calculate damage</li>
     *   <li>The monster's takeDamage method is called with the correct damage amount</li>
     * </ul>
     * </p>
     */
    @Test
    public void testExecuteAttack() {
        // Arrange
        when(mockCharacter.attack()).thenReturn(15);
        
        // Act
        combatSystem.executeAttack(mockCharacter, mockMonster);
        
        // Assert
        verify(mockCharacter, times(1)).attack();
        verify(mockMonster, times(1)).takeDamage(15);
    }

    /**
     * Tests that monster attacks are properly executed
     * <p>
     * This test verifies that when a monster attacks a character:
     * <ul>
     *   <li>The monster's attack method is called to calculate damage</li>
     *   <li>The character's takeDamage method is called with the correct damage amount</li>
     * </ul>
     * </p>
     */
    @Test
    public void testExecuteMonsterAttack() {
        // Arrange
        when(mockMonster.attack()).thenReturn(10);
        
        // Act
        combatSystem.executeMonsterAttack(mockMonster, mockCharacter);
        
        // Assert
        verify(mockMonster, times(1)).attack();
        verify(mockCharacter, times(1)).takeDamage(10);
    }

    /**
     * Tests that the combat system properly handles null inputs
     * <p>
     * This test verifies that when null inputs are provided to the combat system:
     * <ul>
     *   <li>No damage is applied to any entity</li>
     *   <li>No attack methods are called</li>
     *   <li>The system gracefully handles the invalid inputs without exceptions</li>
     * </ul>
     * </p>
     */
    @Test
    public void testExecuteAttackWithNullInputs() {
        // Act
        combatSystem.executeAttack(null, mockMonster);
        combatSystem.executeAttack(mockCharacter, null);
        
        // Assert
        verify(mockMonster, never()).takeDamage(anyInt());
        verify(mockCharacter, never()).attack();
    }
}