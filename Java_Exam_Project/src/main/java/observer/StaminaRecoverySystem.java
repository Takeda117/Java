package java.observer;

import java.factory.Character;
import java.logger.GameLogger;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * StaminaRecoverySystem - Implements the stamina recovery mechanism
 * <p>
 * This class serves as the Subject in the Observer Pattern implementation
 * for the stamina system. It manages a list of observers that are notified
 * when stamina changes occur, and provides methods for recovering stamina
 * for characters.
 * </p>
 * <p>
 * The system uses a simplified approach with a fixed recovery amount,
 * but could be extended to support variable recovery rates based on
 * character attributes or game conditions.
 * </p>
 */
public class StaminaRecoverySystem {
    
    private static final Logger logger = GameLogger.getLogger();
    private static final List<StaminaObserver> observers = new ArrayList<>();
    
    /**
     * Adds an observer to the stamina recovery system
     * <p>
     * Registers a new observer to be notified of stamina changes.
     * Prevents duplicate registrations of the same observer.
     * </p>
     * 
     * @param observer The observer to add
     */
    public static void addObserver(GameUIObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            logger.info("Observer added to stamina recovery system");
        }
    }
    
    /**
     * Recovers stamina for a character
     * <p>
     * Increases a character's stamina by a fixed amount and notifies
     * all registered observers about the recovery. Only works for
     * living characters.
     * </p>
     * 
     * @param character The character to recover stamina for
     */
    public static void recoverStamina(Character character) {
        try {
            if (character != null && character.isAlive()) {
                int recoveryAmount = 10; // Fixed recovery of 10 stamina
                character.restoreStamina(recoveryAmount);
                
                // Notify observers
                for (StaminaObserver observer : observers) {
                    observer.onStaminaRecovered(character, recoveryAmount);
                }
                
                logger.info(character.getName() + " recovered " + recoveryAmount + " stamina");
            }
        } catch (Exception e) {
            logger.warning("Error in stamina recovery: " + e.getMessage());
        }
    }
}