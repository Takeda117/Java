package com.exam.project.observer;

import com.exam.project.factory.Character;
import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;

/**
 * GameUIObserver - Concrete implementation of StaminaObserver
 *
 * This class represents the "UI" part of our game that wants to know
 * when stamina changes happen. In a real game, this might update
 * health bars, show notifications, or trigger visual effects.
 *
 * Observer Pattern allows this UI class to react to stamina changes
 * without the Character classes needing to know about UI details.
 */
public class GameUIObserver implements StaminaObserver {

    private static final Logger logger = GameLogger.getLogger();
    private boolean showRecoveryMessages = true;
    private List<Character> observedCharacters = new ArrayList<>();

    /**
     * Constructor
     */
    public GameUIObserver() {
        logger.info("GameUIObserver created");
    }

    /**
     * Called when a character's stamina changes
     */
    @Override
    public void onStaminaChanged(Character character, int oldStamina, int newStamina) {
        try {
            if (!observedCharacters.contains(character)) {
                observedCharacters.add(character);
            }
            
            int diff = newStamina - oldStamina;
            String change = diff > 0 ? "increased" : "decreased";
            
            logger.info(String.format("%s's stamina %s: %d -> %d",
                    character.getName(), change, oldStamina, newStamina));

            // In a real game, this would update UI elements
            if (Math.abs(diff) > 5) {
                System.out.println("[UI] " + character.getName() + "'s stamina " + 
                        change + " by " + Math.abs(diff));
            }
        } catch (Exception e) {
            logger.warning("Error in onStaminaChanged: " + e.getMessage());
        }
    }

    /**
     * Called when automatic stamina recovery occurs
     */
    @Override
    public void onStaminaRecovered(Character character, int recoveredAmount) {
        try {
            if (!observedCharacters.contains(character)) {
                observedCharacters.add(character);
            }
            
            if (showRecoveryMessages && recoveredAmount > 0) {
                // Only show recovery messages if enabled and something was recovered
                System.out.println("💚 " + character.getName() +
                        " naturally recovers " + recoveredAmount + " stamina");
            }

            // Always log for debugging
            logger.info(String.format("Auto-recovery: %s gained %d stamina",
                    character.getName(), recoveredAmount));

        } catch (Exception e) {
            logger.warning("Error in onStaminaRecovered: " + e.getMessage());
        }
    }

    /**
     * Enable or disable recovery messages
     */
    public void setShowRecoveryMessages(boolean show) {
        this.showRecoveryMessages = show;
        logger.info("Recovery messages " + (show ? "enabled" : "disabled"));
    }
    
    /**
     * Notifica tutti i personaggi osservati di un evento
     * Implementazione del pattern Observer
     */
    public void notifyObservers(String message) {
        logger.info("Notifying all observers: " + message);
        
        for (Character character : observedCharacters) {
            try {
                System.out.println("[UI Notification] " + character.getName() + ": " + message);
            } catch (Exception e) {
                logger.warning("Error notifying character: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifica un personaggio specifico
     */
    public void notifyObserver(Character character, String message) {
        if (character != null && observedCharacters.contains(character)) {
            logger.info("Notifying observer " + character.getName() + ": " + message);
            System.out.println("[UI Notification] " + character.getName() + ": " + message);
        }
    }
}