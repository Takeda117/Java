package com.exam.project.observer;

import com.exam.project.factory.Character;
import java.util.List;
import java.util.ArrayList;

/**
 * Observer Pattern - Interface for stamina recovery system
 *
 * This interface allows different parts of the game to be notified
 * when a character's stamina changes. It's useful for:
 * - UI updates showing stamina bars
 * - Logging stamina changes
 * - Triggering events when stamina reaches certain levels
 *
 * Observer Pattern allows loose coupling between the character
 * (subject) and the systems that need to know about stamina changes.
 */
public interface StaminaObserver {

    /**
     * Called when a character's stamina changes
     * @param character The character whose stamina changed
     * @param oldStamina Previous stamina value
     * @param newStamina Current stamina value
     */
    void onStaminaChanged(Character character, int oldStamina, int newStamina);

    /**
     * Called when stamina recovery occurs automatically
     * @param character The character recovering stamina
     * @param recoveredAmount How much stamina was recovered
     */
    void onStaminaRecovered(Character character, int recoveredAmount);
}
