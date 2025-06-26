package strategy;

import iterator.Item;

import java.util.Comparator;
import java.util.List;

/**
 * Concrete strategy for sorting items by type and then by name.
 * Assumes Item provides getType() and getName() methods.
 */
public class SortByTypeStrategy implements InventorySortStrategy {

    /**
     * Sorts the list of items first by type, then by name.
     *
     * @param items the list of items to sort
     */
    @Override
    public void sort(List<Item> items) {
        items.sort(Comparator
                .comparing(Item::getType)
                .thenComparing(Item::getName));
    }
}


