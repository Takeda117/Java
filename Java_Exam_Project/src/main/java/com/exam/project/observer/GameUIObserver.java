package com.exam.project.observer;

import com.exam.project.factory.Character;
import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;

/**
 * GameUIObserver - Implementazione concreta di StaminaObserver
 * Rappresenta l'interfaccia utente che reagisce ai cambiamenti di stamina
 */
public class GameUIObserver implements StaminaObserver {

    private static final Logger logger = GameLogger.getLogger();
    private final List<Character> observedCharacters = new ArrayList<>();

    /**
     * Costruttore
     */
    public GameUIObserver() {
        logger.info("GameUIObserver creato");
    }

    /**
     * Chiamato quando la stamina di un personaggio cambia
     */
    @Override
    public void onStaminaChanged(Character character, int oldStamina, int newStamina) {
        if (!observedCharacters.contains(character)) {
            observedCharacters.add(character);
        }
        
        int diff = newStamina - oldStamina;
        String change = diff > 0 ? "aumentata" : "diminuita";
        
        System.out.println("[UI] " + character.getName() + " stamina " + 
                change + " di " + Math.abs(diff));
        
        logger.info(character.getName() + " stamina " + change + ": " + oldStamina + " -> " + newStamina);
    }

    /**
     * Chiamato quando la stamina viene recuperata automaticamente
     */
    @Override
    public void onStaminaRecovered(Character character, int recoveredAmount) {
        if (!observedCharacters.contains(character)) {
            observedCharacters.add(character);
        }
        
        System.out.println("[UI] " + character.getName() + " recupera " + recoveredAmount + " stamina");
        logger.info(character.getName() + " recupera " + recoveredAmount + " stamina");
    }
}
