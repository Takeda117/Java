package com.exam.project.strategy;

import com.exam.project.iterator.Item;

import java.util.Comparator;
import java.util.List;

/**
 * Concrete strategy for sorting items alphabetically by name.
 */
public class SortByNameStrategy implements InventorySortStrategy {

    /**
     * Sorts the list of items alphabetically by name.
     *
     * @param items the list of items to sort
     */
    @Override
    public void sort(List<Item> items) {
        items.sort(Comparator.comparing(Item::getName));
    }
}