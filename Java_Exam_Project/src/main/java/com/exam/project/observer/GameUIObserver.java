package com.exam.project.observer;

import com.exam.project.factory.Character;
import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;

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

    // Flag to control if we show stamina recovery messages to player
    private boolean showRecoveryMessages;

    /**
     * Constructor
     * @param showRecoveryMessages Whether to display recovery messages to player
     */
    public GameUIObserver(boolean showRecoveryMessages) {
        this.showRecoveryMessages = showRecoveryMessages;
        logger.info("GameUIObserver created");
    }

    /**
     * Called when character stamina changes
     * Shows appropriate message to the player
     */
    @Override
    public void onStaminaChanged(Character character, int oldStamina, int newStamina) {
        try {
            // Log the change for debugging
            logger.fine(String.format("Stamina changed for %s: %d -> %d",
                    character.getName(), oldStamina, newStamina));

            // Show different messages based on what happened
            if (newStamina > oldStamina) {
                // Stamina increased (recovery or rest)
                onStaminaIncreased(character, oldStamina, newStamina);
            } else if (newStamina < oldStamina) {
                // Stamina decreased (used for actions)
                onStaminaDecreased(character, oldStamina, newStamina);
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
     * Handles stamina increase events
     */
    private void onStaminaIncreased(Character character, int oldStamina, int newStamina) {
        try {
            int increase = newStamina - oldStamina;

            // Show message for significant stamina gains (like from rest)
            if (increase >= 10) {
                System.out.println("✨ " + character.getName() +
                        " feels refreshed! (+" + increase + " stamina)");
            }

        } catch (Exception e) {
            logger.warning("Error handling stamina increase: " + e.getMessage());
        }
    }

    /**
     * Handles stamina decrease events
     */
    private void onStaminaDecreased(Character character, int oldStamina, int newStamina) {
        try {
            int decrease = oldStamina - newStamina;

            // Warn player if stamina is getting low
            if (newStamina <= 10 && decrease > 0) {
                System.out.println("⚠️  " + character.getName() +
                        " is getting tired! (Stamina: " + newStamina + ")");
            }

            // Warn if completely exhausted
            if (newStamina == 0) {
                System.out.println("😴 " + character.getName() +
                        " is completely exhausted and needs rest!");
            }

        } catch (Exception e) {
            logger.warning("Error handling stamina decrease: " + e.getMessage());
        }
    }

    /**
     * Shows current character status (useful for debugging)
     * @param character The character to show status for
     */
    public void showCharacterStatus(Character character) {
        try {
            if (character != null) {
                int stamina = character.getStamina();
                int maxStamina = character.getMaxStamina();
                double percentage = (double) stamina / maxStamina * 100;

                String staminaBar = createStaminaBar(stamina, maxStamina);

                System.out.println(String.format("\n%s Status:", character.getName()));
                System.out.println(String.format("Stamina: %s (%.1f%%)",
                        staminaBar, percentage));
                System.out.println(String.format("Health: %d/%d",
                        character.getHealth(), character.getMaxHealth()));
            }
        } catch (Exception e) {
            logger.warning("Error showing character status: " + e.getMessage());
            System.out.println("Unable to show character status");
        }
    }

    /**
     * Creates a simple text-based stamina bar
     * @param current Current stamina
     * @param max Maximum stamina
     * @return String representation of stamina bar
     */
    private String createStaminaBar(int current, int max) {
        try {
            int barLength = 10;
            int filled = (int) ((double) current / max * barLength);

            StringBuilder bar = new StringBuilder("[");
            for (int i = 0; i < barLength; i++) {
                if (i < filled) {
                    bar.append("█");
                } else {
                    bar.append("░");
                }
            }
            bar.append("] ").append(current).append("/").append(max);

            return bar.toString();
        } catch (Exception e) {
            // Fallback to simple format if bar creation fails
            return current + "/" + max;
        }
    }

    /**
     * Enables or disables recovery message display
     * @param show Whether to show recovery messages
     */
    public void setShowRecoveryMessages(boolean show) {
        this.showRecoveryMessages = show;
        logger.info("Recovery messages " + (show ? "enabled" : "disabled"));
    }
}
