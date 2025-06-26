package observer;

import factory.Character;
import logger.GameLogger;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * Sistema semplificato di recupero stamina con Observer Pattern
 */
public class StaminaRecoverySystem {
    
    private static final Logger logger = GameLogger.getLogger();
    private static final List<StaminaObserver> observers = new ArrayList<>();
    
    /**
     * Aggiunge un observer
     */
    public static void addObserver(StaminaObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            logger.info("Observer aggiunto al sistema di recupero stamina");
        }
    }
    
    /**
     * Recupera stamina per un personaggio
     */
    public static void recoverStamina(Character character) {
        try {
            if (character != null && character.isAlive()) {
                int recoveryAmount = 10; // Recupero fisso di 10 stamina
                character.restoreStamina(recoveryAmount);
                
                // Notifica gli observer
                for (StaminaObserver observer : observers) {
                    observer.onStaminaRecovered(character, recoveryAmount);
                }
                
                logger.info(character.getName() + " ha recuperato " + recoveryAmount + " stamina");
            }
        } catch (Exception e) {
            logger.warning("Errore nel recupero stamina: " + e.getMessage());
        }
    }
}