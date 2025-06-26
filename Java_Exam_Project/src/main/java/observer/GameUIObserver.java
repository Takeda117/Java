package java.observer;

import java.factory.Character;
import java.logger.GameLogger;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;

/**
 * GameUIObserver - Concrete implementation of StaminaObserver
 * <p>
 * This class represents the user interface component that reacts to stamina changes.
 * It implements the StaminaObserver interface as part of the Observer Pattern,
 * allowing it to receive notifications when a character's stamina changes.
 * </p>
 * <p>
 * The GameUIObserver maintains a list of characters it is observing and
 * provides visual feedback to the player about stamina changes through
 * console output. In a full game implementation, this would update
 * graphical UI elements like stamina bars.
 * </p>
 */
public class GameUIObserver implements StaminaObserver {

    private static final Logger logger = GameLogger.getLogger();
    private final List<Character> observedCharacters = new ArrayList<>();

    /**
     * Constructor for GameUIObserver
     * <p>
     * Initializes a new UI observer that can be registered with the
     * stamina recovery system to receive notifications about stamina changes.
     * </p>
     */
    public GameUIObserver() {
        logger.info("GameUIObserver created");
    }

    /**
     * Called when a character's stamina changes
     * <p>
     * This method is invoked by the subject (character or stamina system)
     * whenever a character's stamina value is modified. It updates the UI
     * to reflect the change and logs the event.
     * </p>
     * 
     * @param character The character whose stamina changed
     * @param oldStamina Previous stamina value
     * @param newStamina Current stamina value
     */
    @Override
    public void onStaminaChanged(Character character, int oldStamina, int newStamina) {
        if (!observedCharacters.contains(character)) {
            observedCharacters.add(character);
        }
        
        int diff = newStamina - oldStamina;
        String change = diff > 0 ? "increased" : "decreased";
        
        System.out.println("[UI] " + character.getName() + " stamina " + 
                change + " by " + Math.abs(diff));
        
        logger.info(character.getName() + " stamina " + change + ": " + oldStamina + " -> " + newStamina);
    }

    /**
     * Called when stamina recovery occurs automatically
     * <p>
     * This method is invoked specifically when stamina is recovered through
     * the automatic recovery system. It updates the UI to show the recovery
     * and logs the event.
     * </p>
     * 
     * @param character The character recovering stamina
     * @param recoveredAmount How much stamina was recovered
     */
    @Override
    public void onStaminaRecovered(Character character, int recoveredAmount) {
        if (!observedCharacters.contains(character)) {
            observedCharacters.add(character);
        }
        
        System.out.println("[UI] " + character.getName() + " recovers " + recoveredAmount + " stamina");
        logger.info(character.getName() + " recovers " + recoveredAmount + " stamina");
    }
}
