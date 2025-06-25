package com.exam.project.combat;

import com.exam.project.factory.Character;
import com.exam.project.factoryMonster.AbstractMonster;

/**
 * Sistema di combattimento semplificato
 */
public class CombatSystem {

    /**
     * Verifica se un personaggio può combattere
     */
    public boolean canFight(Character character) {
        return character != null && character.isAlive() && character.getStamina() > 0;
    }
    
    /**
     * Esegue un attacco del personaggio contro un mostro
     */
    public int executeAttack(Character character, AbstractMonster monster) {
        int damage = character.attack();
        if (damage > 0) {
            monster.takeDamage(damage);
        }
        return damage;
    }
    
    /**
     * Esegue un attacco del mostro contro un personaggio
     */
    public int executeMonsterAttack(AbstractMonster monster, Character character) {
        int damage = monster.attack();
        if (damage > 0) {
            character.takeDamage(damage);
        }
        return damage;
    }
}
