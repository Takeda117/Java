package com.exam.project.factoryMonster;

import com.exam.project.iterator.Item;

/**
 * Troll - Mostro avanzato della Palude dei Troll
 *
 * I Troll sono creature massicce e potenti che abitano le paludi.
 * Sono molto più forti dei Goblin ma anche più lenti e prevedibili.
 * Rappresentano una sfida maggiore per i giocatori più esperti.
 *
 * Caratteristiche distintive:
 * - Vita molto alta e attacchi devastanti
 * - Rigenerazione naturale durante il combattimento
 * - Drop di valore elevato (armi pesanti, armature)
 * - Attacchi lenti ma estremamente dannosi
 * - Resistenza naturale ai danni
 */
public class Troll extends AbstractMonster {

    // Nomi caratteristici dei Troll della palude
    private static final String[] TROLL_NAMES = {
            "Mossback", "Swampfist", "Mudcrusher", "Thornhide", "Bogstomper",
            "Slimeclaw", "Marshbane", "Rotgut", "Murkwater", "Mireking"
    };

    // Statistiche speciali del Troll
    private int regenerationAmount;
    private boolean hasRegenerated;  // Per limitare la rigenerazione a una volta per combattimento

    /**
     * Costruttore per creare un Troll con nome casuale
     * I Troll hanno statistiche molto superiori ai Goblin
     *
     * @param difficulty fattore di scala per le statistiche (tipicamente 2-4)
     */
    public Troll(int difficulty) {
        super(
                generateRandomName(),           // Nome casuale dalla lista
                "Troll",                       // Tipo fisso
                35 + (difficulty * 8),         // Vita: 51-67 punti (molto alta!)
                8 + (difficulty * 2),          // Danno: 12-16 punti (forte!)
                25 + (difficulty * 10),        // Oro: 45-65 monete (ricco!)
                60                             // 60% di probabilità di drop (generosi)
        );

        // Capacità di rigenerazione del Troll
        this.regenerationAmount = 5 + difficulty;
        this.hasRegenerated = false;

        // Inizializza i drop preziosi del Troll
        initializeDrops();
    }

    /**
     * Costruttore alternativo con nome specifico
     * Utile per creare Troll boss della palude
     */
    public Troll(String specificName, int difficulty) {
        super(
                specificName,
                "Troll",
                35 + (difficulty * 8),
                8 + (difficulty * 2),
                25 + (difficulty * 10),
                60
        );

        this.regenerationAmount = 5 + difficulty;
        this.hasRegenerated = false;

        initializeDrops();
    }

    /**
     * Genera un nome casuale dalla lista dei nomi Troll
     */
    private static String generateRandomName() {
        return TROLL_NAMES[random.nextInt(TROLL_NAMES.length)];
    }

    /**
     * Inizializza gli oggetti preziosi che il Troll può droppare
     * I Troll possiedono equipaggiamento di qualità superiore
     */
    private void initializeDrops() {
        // Armi pesanti caratteristiche dei Troll
        addPossibleDrop(new Item("Clava di Ferro", Item.ItemType.WEAPON, 80, 4));
        addPossibleDrop(new Item("Martello della Palude", Item.ItemType.WEAPON, 120, 6));

        // Armature robuste fatte di materiali della palude
        addPossibleDrop(new Item("Corazza di Cuoio Troll", Item.ItemType.ARMOR, 100, 3));
        addPossibleDrop(new Item("Elmo di Ossa", Item.ItemType.ARMOR, 60, 2));

        // Pozioni più potenti
        addPossibleDrop(new Item("Pozione di Cura Media", Item.ItemType.POTION, 50, 0));

        // Materiali rari della palude
        addPossibleDrop(new Item("Muschio Magico", Item.ItemType.MISC, 40, 0));
        addPossibleDrop(new Item("Zanna di Troll", Item.ItemType.MISC, 75, 0));
    }

    /**
     * Attacco specializzato del Troll
     * I Troll hanno attacchi potenti ma prevedibili
     */
    @Override
    public int attack() {
        // I Troll hanno il 15% di possibilità di attacco devastante
        if (random.nextInt(100) < 15) {
            int devastatingDamage = calculateDamage() * 2;
            System.out.printf("%s %s solleva le sue enormi braccia e colpisce devastantemente per %d danni!%n",
                    type, name, devastatingDamage);
            return devastatingDamage;
        }

        // 10% di possibilità di attacco che stordisce (danno normale ma messaggio speciale)
        if (random.nextInt(100) < 10) {
            int damage = calculateDamage();
            System.out.printf("%s %s colpisce con tale forza che il terreno trema! %d danni!%n",
                    type, name, damage);
            return damage;
        }

        // Attacco normale con messaggio caratteristico
        int damage = calculateDamage();
        System.out.printf("%s %s colpisce pesantemente per %d danni!%n", type, name, damage);
        return damage;
    }

    /**
     * Calcolo del danno del Troll
     * Hanno meno variazione dei Goblin ma danno base molto più alto
     */
    @Override
    protected int calculateDamage() {
        // I Troll hanno variazione del ±10% (sono più consistenti dei Goblin)
        int variance = (int)(baseDamage * 0.1);
        int variation = random.nextInt(variance * 2 + 1) - variance;

        return Math.max(1, baseDamage + variation);
    }

    /**
     * Override del metodo takeDamage per includere la rigenerazione
     */
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);

        // Se il Troll è ancora vivo e ha perso più del 50% della vita, si rigenera (una volta sola)
        if (isAlive() && !hasRegenerated && health < (maxHealth / 2)) {
            regenerate();
        }
    }

    /**
     * Capacità speciale di rigenerazione del Troll
     * Può rigenerarsi una volta durante il combattimento quando è gravemente ferito
     */
    private void regenerate() {
        hasRegenerated = true;  // Può rigenerarsi solo una volta

        int oldHealth = health;
        health = Math.min(maxHealth, health + regenerationAmount);
        int healedAmount = health - oldHealth;

        System.out.printf("%s %s attinge alla forza della palude e rigenera %d punti vita! Vita: %d/%d%n",
                type, name, healedAmount, health, maxHealth);

        // Messaggio aggiuntivo per enfatizzare l'evento
        System.out.printf("La pelle di %s si ispessisce e le ferite si rimarginano rapidamente!%n", name);
    }

    /**
     * Comportamento speciale quando il Troll viene sconfitto
     */
    @Override
    protected void onDefeat() {
        super.onDefeat();

        // I Troll hanno sempre un messaggio di sconfitta drammatico
        String[] deathMessages = {
                "crolla come un albero abbattuto, facendo tremare la palude!",
                "emette un ultimo ruggito che echeggia nella nebbia della palude!",
                "si dissolve lentamente tornando al fango da cui era nato!",
                "sussurra antiche parole trollesche prima di chiudere gli occhi!"
        };

        String message = deathMessages[random.nextInt(deathMessages.length)];
        System.out.printf("%s %s %s%n", type, name, message);

        // I Troll lasciano sempre qualcosa in più quando muoiono
        System.out.printf("La morte di %s rilascia energia magica nell'aria...%n", name);
    }

    // Getter per le statistiche speciali del Troll
    public int getRegenerationAmount() {
        return regenerationAmount;
    }

    public boolean hasAlreadyRegenerated() {
        return hasRegenerated;
    }

    /**
     * Crash-safe toString implementation for Troll
     * @return Formatted string with troll-specific information
     */
    @Override
    public String toString() {
        try {
            String safeName = (name != null) ? name : "Unknown";
            String regenStatus = hasRegenerated ? " (rigenerato)" : " (può rigenerarsi)";
            int safeHealth = Math.max(0, health);
            int safeMaxHealth = Math.max(1, maxHealth);
            int safeBaseDamage = Math.max(0, baseDamage);
            int safeGoldDrop = Math.max(0, goldDrop);

            return String.format("Troll %s [Vita: %d/%d, Danno: %d, Oro: %d%s]",
                    safeName, safeHealth, safeMaxHealth, safeBaseDamage, safeGoldDrop, regenStatus);
        } catch (Exception e) {
            return "Troll [Error displaying stats]";
        }
    }
}
