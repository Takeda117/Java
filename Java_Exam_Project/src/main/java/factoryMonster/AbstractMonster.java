package factoryMonster;

import iterator.Item;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import logger.GameLogger;
import java.util.logging.Logger;

/**
 * AbstractMonster - Classe base per tutti i mostri del gioco
 *
 * Questa classe implementa il comportamento comune a tutti i mostri,
 * mentre le sottoclassi definiscono caratteristiche specifiche.
 *
 * La struttura è simile a quella dei personaggi ma più semplice,
 * dato che i mostri non hanno inventari complessi o progressione.
 */
public abstract class AbstractMonster {

    private static final Logger logger = GameLogger.getLogger();

    // Statistiche base comuni a tutti i mostri
    protected String name;
    protected String type;
    protected int health;
    protected int maxHealth;
    protected int baseDamage;
    protected int goldDrop;

    // Sistema di drop degli oggetti
    protected List<Item> possibleDrops;
    protected int dropChance; // Percentuale di probabilità di drop (0-100)

    // Generatore di numeri casuali per variazioni di danno e drop
    protected static final Random random = new Random();

    /**
     * Costruttore protetto - solo le sottoclassi possono creare mostri
     *
     * @param name Nome specifico del mostro (es. "Gruk")
     * @param type Tipo del mostro (es. "Goblin")
     * @param health Punti vita del mostro
     * @param baseDamage Danno base del mostro
     * @param goldDrop Oro che lascia quando sconfitto
     * @param dropChance Probabilità di lasciare oggetti (0-100)
     */
    protected AbstractMonster(String name, String type, int health, int baseDamage, int goldDrop, int dropChance) {
        // Validazione degli input
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del mostro non può essere vuoto");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Il tipo del mostro non può essere vuoto");
        }
        if (health <= 0 || baseDamage < 0 || goldDrop < 0) {
            throw new IllegalArgumentException("Le statistiche del mostro devono essere positive");
        }

        this.name = name.trim();
        this.type = type.trim();
        this.health = health;
        this.maxHealth = health;
        this.baseDamage = baseDamage;
        this.goldDrop = goldDrop;
        this.dropChance = Math.max(0, Math.min(100, dropChance)); // Assicura che sia tra 0 e 100
        this.possibleDrops = new ArrayList<>();
    }

    /**
     * Attacco del mostro - comportamento base
     * Le sottoclassi possono sovrascrivere questo metodo per comportamenti speciali
     *
     * @return il danno inflitto dall'attacco
     */
    public int attack() {
        // Calcola il danno con una piccola variazione casuale
        int damage = calculateDamage();

        // Messaggio di attacco - le sottoclassi possono personalizzarlo
        System.out.printf("%s %s attacca per %d danni!%n", type, name, damage);

        return damage;
    }

    /**
     * Calcola il danno dell'attacco con variazione casuale
     * Metodo protetto che le sottoclassi possono sovrascrivere
     */
    protected int calculateDamage() {
        // Variazione casuale del ±20% del danno base
        int variance = (int)(baseDamage * 0.2); // 20% del danno base
        int variation = random.nextInt(variance * 2 + 1) - variance; // Da -variance a +variance

        return Math.max(1, baseDamage + variation); // Almeno 1 danno
    }

    /**
     * Il mostro subisce danno da un attacco del giocatore
     *
     * @param damage la quantità di danno ricevuto
     */
    public void takeDamage(int damage) {
        if (damage < 0) {
            logger.warning("Invalid negative damage attempted: " + damage + " on " + type + " " + name);
            System.out.println("Danno non valido ignorato");
            return;
        }

        int oldHealth = this.health;
        this.health = Math.max(0, this.health - damage);
        
        logger.info(type + " " + name + " took " + damage + " damage. Health: " + oldHealth + " -> " + health);

        System.out.printf("%s %s subisce %d danni! Vita: %d/%d%n",
                type, name, damage, health, maxHealth);

        if (!isAlive()) {
            logger.info(type + " " + name + " was defeated");
            System.out.printf("%s %s è stato sconfitto!%n", type, name);
            onDefeat(); // Chiamata al metodo di sconfitta
        }
    }

    /**
     * Controlla se il mostro è ancora vivo
     *
     * @return true se ha punti vita > 0, false altrimenti
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Metodo chiamato quando il mostro viene sconfitto
     * Le sottoclassi possono sovrascriverlo per comportamenti speciali
     */
    protected void onDefeat() {
        // Comportamento base: messaggio di sconfitta
        // Le sottoclassi possono aggiungere effetti speciali
    }

    /**
     * Aggiunge un possibile oggetto al drop del mostro
     *
     * @param item l'oggetto da aggiungere alla lista dei possibili drop
     */
    public void addPossibleDrop(Item item) {
        if (item != null) {
            possibleDrops.add(item);
        }
    }

    /**
     * Calcola gli oggetti lasciati dal mostro quando viene sconfitto
     * Utilizza il sistema probabilistico basato su dropChance
     *
     * @return lista degli oggetti effettivamente droppati
     */
    public List<Item> getDroppedItems() {
        List<Item> actualDrops = new ArrayList<>();

        // Se non ci sono possibili drop, ritorna lista vuota
        if (possibleDrops.isEmpty()) {
            return actualDrops;
        }

        // Per ogni possibile drop, controlla se viene effettivamente lasciato
        for (Item item : possibleDrops) {
            if (random.nextInt(100) < dropChance) {
                actualDrops.add(item);
            }
        }

        return actualDrops;
    }

    // === Metodi getter per accedere alle proprietà del mostro ===

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getGoldDrop() {
        return goldDrop;
    }

    public int getDropChance() {
        return dropChance;
    }

    /**
     * Restituisce una copia sicura della lista dei possibili drop
     */
    public List<Item> getPossibleDrops() {
        return new ArrayList<>(possibleDrops);
    }

    /**
     * Crash-safe toString implementation
     * @return Formatted string with monster stats
     */
    @Override
    public String toString() {
        try {
            String safeName = (name != null) ? name : "Unknown";
            String safeType = (type != null) ? type : "Monster";
            int safeHealth = Math.max(0, health);
            int safeMaxHealth = Math.max(1, maxHealth);
            int safeBaseDamage = Math.max(0, baseDamage);
            int safeGoldDrop = Math.max(0, goldDrop);
            int safeDropChance = Math.max(0, Math.min(100, dropChance));

            return String.format("%s %s [Vita: %d/%d, Danno: %d, Oro: %d, Drop: %d%%]",
                    safeType, safeName, safeHealth, safeMaxHealth, safeBaseDamage, safeGoldDrop, safeDropChance);
        } catch (Exception e) {
            return "Monster [Error displaying stats]";
        }
    }

    /**
     * Confronto tra mostri basato su nome e tipo
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        AbstractMonster monster = (AbstractMonster) obj;
        return name.equals(monster.name) && type.equals(monster.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}