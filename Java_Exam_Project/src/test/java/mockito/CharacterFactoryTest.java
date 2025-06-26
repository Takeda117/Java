package mockito;

import java.factory.CharacterFactory;
import java.factory.Character;
import java.factory.Warrior;
import java.factory.Mage;
import java.security.InputValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * CharacterFactoryTest - Unit tests for the CharacterFactory class using Mockito
 * <p>
 * This test class demonstrates the use of Mockito to isolate the CharacterFactory class
 * from its dependencies during testing. By mocking the InputValidator class, we can test
 * the factory's behavior without relying on the actual validation implementation.
 * </p>
 * <p>
 * The tests focus on verifying that:
 * <ul>
 *   <li>The factory correctly creates different character types based on input</li>
 *   <li>Created characters have the expected properties</li>
 *   <li>The factory handles invalid inputs appropriately</li>
 * </ul>
 * </p>
 * <p>
 * This class uses the MockitoJUnitRunner to automatically initialize mocks,
 * demonstrating an alternative to the explicit MockitoAnnotations.openMocks() approach.
 * </p>
 */
@RunWith(MockitoJUnitRunner.class)
public class CharacterFactoryTest {

    private CharacterFactory factory;
    
    @Mock
    private InputValidator mockValidator;

    /**
     * Sets up the test environment before each test
     * <p>
     * This method initializes all mock objects and creates a fresh
     * CharacterFactory instance for each test to ensure test isolation.
     * </p>
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        factory = new CharacterFactory();
    }

    /**
     * Tests character creation with valid inputs
     * <p>
     * This test verifies that when valid inputs are provided:
     * <ul>
     *   <li>The factory successfully creates a character</li>
     *   <li>The created character is of the correct type (Warrior)</li>
     *   <li>The character has the expected name</li>
     * </ul>
     * </p>
     */
    @Test
    public void testCreateCharacterWithValidInput() {
        Character warrior = factory.createCharacter("warrior", "ValidName");
        
        assertNotNull("Factory should create a warrior", warrior);
        assertTrue("Created character should be a Warrior", warrior instanceof Warrior);
        assertEquals("Character should have the correct name", "ValidName", warrior.getName());
    }

    /**
     * Tests that the factory creates different character types
     * <p>
     * This test verifies that the factory correctly creates different
     * character types based on the type parameter:
     * <ul>
     *   <li>A "warrior" type parameter creates a Warrior instance</li>
     *   <li>A "mage" type parameter creates a Mage instance</li>
     * </ul>
     * </p>
     */
    @Test
    public void testCreateDifferentCharacterTypes() {
        Character warrior = factory.createCharacter("warrior", "WarriorName");
        Character mage = factory.createCharacter("mage", "MageName");
        
        assertNotNull("Factory should create a warrior", warrior);
        assertNotNull("Factory should create a mage", mage);
        
        assertTrue("Created character should be a Warrior", warrior instanceof Warrior);
        assertTrue("Created character should be a Mage", mage instanceof Mage);
    }

    /**
     * Tests that the factory handles invalid character types
     * <p>
     * This test verifies that when an invalid character type is provided:
     * <ul>
     *   <li>The factory returns null</li>
     *   <li>No character is created</li>
     * </ul>
     * </p>
     */
    @Test
    public void testCreateCharacterWithInvalidType() {
        Character invalidCharacter = factory.createCharacter("invalid_type", "TestName");
        
        assertNull("Factory should return null for invalid character type", invalidCharacter);
    }
}