package java.observer;

import java.factory.Character;


/**
 * Observer Pattern - Interface for stamina recovery system
 * <p>
 * This interface defines the Observer component in the Observer Pattern
 * implementation for the stamina system. It allows different parts of the game
 * to be notified when a character's stamina changes.
 * </p>
 * <p>
 * The Observer Pattern enables loose coupling between the character (subject)
 * and the systems that need to know about stamina changes, such as:
 * <ul>
 *   <li>UI updates showing stamina bars</li>
 *   <li>Logging stamina changes</li>
 *   <li>Triggering events when stamina reaches certain levels</li>
 * </ul>
 * </p>
 */
public interface StaminaObserver {

    /**
     * Called when a character's stamina changes
     * <p>
     * This method is invoked by the subject (character or stamina system)
     * whenever a character's stamina value is modified. It provides both
     * the old and new stamina values to allow observers to react appropriately
     * to the change.
     * </p>
     * 
     * @param character The character whose stamina changed
     * @param oldStamina Previous stamina value
     * @param newStamina Current stamina value
     */
    void onStaminaChanged(Character character, int oldStamina, int newStamina);

    /**
     * Called when stamina recovery occurs automatically
     * <p>
     * This method is invoked specifically when stamina is recovered through
     * the automatic recovery system, rather than through other means like
     * items or abilities. This allows observers to distinguish between
     * different types of stamina changes.
     * </p>
     * 
     * @param character The character recovering stamina
     * @param recoveredAmount How much stamina was recovered
     */
    void onStaminaRecovered(Character character, int recoveredAmount);
}
