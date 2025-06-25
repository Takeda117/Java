package com.exam.project.factoryMonster;

import com.exam.project.iterator.Item;

/**
 * Goblin - Mostro base della Goblin Cave
 *
 * I Goblin rappresentano i nemici più comuni e deboli del gioco.
 * Sono veloci ma fragili, con attacchi poco potenti ma frequenti.
 * Perfetti come nemici introduttivi per i giocatori principianti.
 *
 * Caratteristiche distintive:
 * - Vita bassa ma attacchi rapidi
 * - Drop comuni (pozioni di cura piccole)
 * - Poco oro ma facili da sconfiggere
 * - Attacchi con variazione alta (imprevedibili)
 */
public class Goblin extends AbstractMonster {

    // Nomi casuali per varietà nei Goblin generati
    private static final String[] GOBLIN_NAMES = {
            "Gruk", "Snarl", "Grik", "Zog", "Mog",
            "Brak", "Skrim", "Nix", "Grot", "Vex"
    };

    /**
     * Costruttore per creare un Goblin con nome casuale
     * Utilizza statistiche bilanciate per nemici di livello base
     *
     * @param difficulty fattore di scala per le statistiche (tipicamente 1-3)
     */
    public Goblin(int difficulty) {
        super(
                generateRandomName(),           // Nome casuale dalla lista
                "Goblin",                      // Tipo fisso
                15 + (difficulty * 3),         // Vita: 18-24 punti
                3 + difficulty,                // Danno: 4-6 punti
                8 + (difficulty * 2),          // Oro: 10-14 monete
                40                             // 40% di probabilità di drop
        );

        // Inizializza i possibili drop del Goblin
        initializeDrops();
    }

    /**
     * Costruttore alternativo con nome specifico
     * Utile per creare Goblin boss o con nomi particolari
     */
    public Goblin(String specificName, int difficulty) {
        super(
                specificName,
                "Goblin",
                15 + (difficulty * 3),
                3 + difficulty,
                8 + (difficulty * 2),
                40
        );

        initializeDrops();
    }

    /**
     * Genera un nome casuale dalla lista dei nomi Goblin
     */
    private static String generateRandomName() {
        return GOBLIN_NAMES[random.nextInt(GOBLIN_NAMES.length)];
    }

    /**
     * Inizializza gli oggetti che il Goblin può droppare
     * I Goblin tipicamente droppano oggetti di basso valore
     */
    private void initializeDrops() {
        // Pozione di cura piccola - drop comune dei Goblin
        addPossibleDrop(new Item("Piccola Pozione di Cura", Item.ItemType.POTION, 15, 0));

        // Raramente possono droppare una daga arrugginita
        addPossibleDrop(new Item("Daga Arrugginita", Item.ItemType.WEAPON, 25, 1));

        // Oggetti vari che i Goblin raccolgono
        addPossibleDrop(new Item("Dente di Goblin", Item.ItemType.MISC, 5, 0));
    }

    /**
     * Attacco specializzato del Goblin
     * I Goblin sono imprevedibili nei loro attacchi
     */
    @Override
    public int attack() {
        // 20% di possibilità di attacco critico (furioso)
        if (random.nextInt(100) < 20) {
            int criticalDamage = calculateDamage() + 2;
            System.out.printf("%s %s diventa furioso e attacca selvaggiamente per %d danni!%n",
                    type, name, criticalDamage);
            return criticalDamage;
        }

        // 10% di possibilità di attacco mancato (sono goffi)
        if (random.nextInt(100) < 10) {
            System.out.printf("%s %s inciampa durante l'attacco e manca il bersaglio!%n", type, name);
            return 0;
        }

        // Attacco normale
        return super.attack();
    }

    /**
     * Calcolo del danno personalizzato per i Goblin
     * Hanno alta variazione nel danno (sono imprevedibili)
     */
    @Override
    protected int calculateDamage() {
        // I Goblin hanno variazione del ±50% invece del ±20% standard
        int variance = (int)(baseDamage * 0.5);
        int variation = random.nextInt(variance * 2 + 1) - variance;

        return Math.max(1, baseDamage + variation);
    }

    /**
     * Comportamento speciale quando il Goblin viene sconfitto
     */
    @Override
    protected void onDefeat() {
        super.onDefeat();

        // 30% di possibilità che il Goblin urli prima di morire
        if (random.nextInt(100) < 30) {
            String[] deathCries = {
                    "emette un urlo acuto prima di crollare!",
                    "balbetta qualcosa in goblinese prima di arrendersi!",
                    "guarda terrorizzato prima di fuggire zoppicando!",
                    "lascia cadere tutto quello che aveva in mano!"
            };
            String cry = deathCries[random.nextInt(deathCries.length)];
            System.out.printf("%s %s %s%n", type, name, cry);
        }
    }

    /**
     * Crash-safe toString implementation for Goblin
     * @return Formatted string with goblin-specific information
     */
    @Override
    public String toString() {
        try {
            String safeName = (name != null) ? name : "Unknown";
            int safeHealth = Math.max(0, health);
            int safeMaxHealth = Math.max(1, maxHealth);
            int safeBaseDamage = Math.max(0, baseDamage);
            int safeGoldDrop = Math.max(0, goldDrop);

            return String.format("Goblin %s [Vita: %d/%d, Danno: %d (variabile), Oro: %d]",
                    safeName, safeHealth, safeMaxHealth, safeBaseDamage, safeGoldDrop);
        } catch (Exception e) {
            return "Goblin [Error displaying stats]";
        }
    }
}
