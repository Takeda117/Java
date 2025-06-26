package rpg.strategy;
import rpg.iterator.Item;
import java.util.List;

/**
 * Strategy interface for sorting a list of items in the inventory.
 */
public interface InventorySortStrategy {
    /**
     * Sorts the provided list of items in-place.
     *
     * @param items the list of items to sort
     */
    void sort(List<Item> items);
}


