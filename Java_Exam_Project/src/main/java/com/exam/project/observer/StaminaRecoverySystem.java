package com.exam.project.observer;

import com.exam.project.factory.Character;
import com.exam.project.factory.Warrior;
import com.exam.project.factory.Mage;
import com.exam.project.logger.GameLogger;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Stamina Recovery System using Observer Pattern
 *
 * This class manages automatic stamina recovery for characters.
 * It uses a Timer to periodically recover stamina and notifies
 * observers when stamina changes occur.
 *
 * Observer Pattern benefits:
 * - Characters don't need to know who's watching their stamina
 * - Easy to add new features that react to stamina changes
 * - Clean separation between stamina logic and UI/logging
 */
public class StaminaRecoverySystem {

    private static final Logger logger = GameLogger.getLogger();

    // Characters being monitored for stamina recovery
    private List<Character> characters;

    // Observers that want to know about stamina changes
    private List<StaminaObserver> observers;

    // Timer for automatic recovery
    private Timer recoveryTimer;

    // Recovery happens every 3 seconds
    private static final int RECOVERY_INTERVAL = 3000;

    /**
     * Constructor initializes the recovery system
     */
    public StaminaRecoverySystem() {
        try {
            this.characters = new ArrayList<>();
            this.observers = new ArrayList<>();
            this.recoveryTimer = new Timer("StaminaRecovery", true); // daemon thread
            logger.info("StaminaRecoverySystem initialized");
        } catch (Exception e) {
            logger.severe("Failed to initialize StaminaRecoverySystem: " + e.getMessage());
            throw new RuntimeException("Cannot create stamina recovery system", e);
        }
    }

    /**
     * Adds a character to be monitored for stamina recovery
     * @param character The character to monitor
     */
    public void addCharacter(Character character) {
        try {
            if (character != null && !characters.contains(character)) {
                characters.add(character);
                logger.info("Added character to stamina recovery: " + character.getName());
            }
        } catch (Exception e) {
            logger.warning("Error adding character to recovery: " + e.getMessage());
        }
    }

    /**
     * Removes a character from monitoring
     * @param character The character to stop monitoring
     */
    public void removeCharacter(Character character) {
        try {
            if (characters.remove(character)) {
                logger.info("Removed character from stamina recovery: " +
                        (character != null ? character.getName() : "null"));
            }
        } catch (Exception e) {
            logger.warning("Error removing character from recovery: " + e.getMessage());
        }
    }

    /**
     * Adds an observer to be notified of stamina changes
     * @param observer The observer to add
     */
    public void addObserver(StaminaObserver observer) {
        try {
            if (observer != null && !observers.contains(observer)) {
                observers.add(observer);
                logger.info("Added stamina observer: " + observer.getClass().getSimpleName());
            }
        } catch (Exception e) {
            logger.warning("Error adding observer: " + e.getMessage());
        }
    }

    /**
     * Removes an observer
     * @param observer The observer to remove
     */
    public void removeObserver(StaminaObserver observer) {
        try {
            if (observers.remove(observer)) {
                logger.info("Removed stamina observer");
            }
        } catch (Exception e) {
            logger.warning("Error removing observer: " + e.getMessage());
        }
    }

    /**
     * Starts the automatic stamina recovery timer
     */
    public void startRecovery() {
        try {
            recoveryTimer.scheduleAtFixedRate(new RecoveryTask(),
                    RECOVERY_INTERVAL, RECOVERY_INTERVAL);
            logger.info("Stamina recovery timer started");
        } catch (Exception e) {
            logger.severe("Failed to start recovery timer: " + e.getMessage());
        }
    }

    /**
     * Stops the recovery timer
     */
    public void stopRecovery() {
        try {
            if (recoveryTimer != null) {
                recoveryTimer.cancel();
                logger.info("Stamina recovery timer stopped");
            }
        } catch (Exception e) {
            logger.warning("Error stopping recovery timer: " + e.getMessage());
        }
    }

    /**
     * Notifies all observers about a stamina change
     * @param character The character whose stamina changed
     * @param oldStamina Previous stamina value
     * @param newStamina Current stamina value
     */
    private void notifyStaminaChanged(Character character, int oldStamina, int newStamina) {
        try {
            if (character == null) {
                logger.warning("Null character in notifyStaminaChanged");
                return;
            }
            
            if (observers == null || observers.isEmpty()) {
                logger.fine("No observers to notify");
                return;
            }
            
            for (StaminaObserver observer : observers) {
                if (observer != null) {
                    observer.onStaminaChanged(character, oldStamina, newStamina);
                }
            }
        } catch (Exception e) {
            logger.warning("Error notifying observers: " + e.getMessage());
        }
    }

    /**
     * Notifies observers about stamina recovery
     * @param character The character that recovered stamina
     * @param recoveredAmount Amount of stamina recovered
     */
    private void notifyStaminaRecovered(Character character, int recoveredAmount) {
        try {
            for (StaminaObserver observer : observers) {
                observer.onStaminaRecovered(character, recoveredAmount);
            }
        } catch (Exception e) {
            logger.warning("Error notifying recovery observers: " + e.getMessage());
        }
    }

    /**
     * Gets the recovery rate for a character based on their class
     * @param character The character
     * @return Base recovery amount
     */
    private int getRecoveryAmount(Character character) {
        try {
            if (character instanceof Warrior) {
                return 2; // Warriors recover slowly
            } else if (character instanceof Mage) {
                return 3; // Mages recover faster
            } else {
                return 2; // Default recovery
            }
        } catch (Exception e) {
            logger.warning("Error calculating recovery amount: " + e.getMessage());
            return 1; // Safe fallback
        }
    }

    /**
     * Inner class for the recovery timer task
     */
    private class RecoveryTask extends TimerTask {
        @Override
        public void run() {
            try {
                // Make a copy to avoid concurrent modification
                List<Character> currentCharacters = new ArrayList<>(characters);

                for (Character character : currentCharacters) {
                    if (character != null && character.isAlive()) {
                        recoverStaminaForCharacter(character);
                    }
                }
            } catch (Exception e) {
                logger.warning("Error in recovery task: " + e.getMessage());
            }
        }

        /**
         * Recovers stamina for a single character
         * @param character The character to recover stamina for
         */
        private void recoverStaminaForCharacter(Character character) {
            try {
                int currentStamina = character.getStamina();
                int maxStamina = character.getMaxStamina();

                // Only recover if not at max stamina
                if (currentStamina < maxStamina) {
                    int recoveryAmount = getRecoveryAmount(character);
                    int newStamina = Math.min(maxStamina, currentStamina + recoveryAmount);

                    // Calculate actual recovery
                    int actualRecovery = newStamina - currentStamina;

                    if (actualRecovery > 0) {
                        // Restore the stamina
                        character.restoreStamina(actualRecovery);

                        // Notify observers
                        notifyStaminaChanged(character, currentStamina, newStamina);
                        notifyStaminaRecovered(character, actualRecovery);

                        logger.fine(character.getName() + " recovered " + actualRecovery + " stamina");
                    }
                }
            } catch (Exception e) {
                logger.warning("Error recovering stamina for character: " + e.getMessage());
            }
        }
    }
}
