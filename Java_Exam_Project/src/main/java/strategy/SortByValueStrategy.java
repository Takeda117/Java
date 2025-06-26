package strategy;

import iterator.Item;

import java.util.Comparator;
import java.util.List;

/**
 * Concrete strategy for sorting items by value (highest to lowest).
 */
public class SortByValueStrategy implements InventorySortStrategy {

    /**
     * Sorts the list of items by value in descending order.
     *
     * @param items the list of items to sort
     */
    @Override
    public void sort(List<Item> items) {
        items.sort(Comparator.comparing(Item::getValue).reversed());
    }
}