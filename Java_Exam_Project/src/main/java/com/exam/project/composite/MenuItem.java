package com.exam.project.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * MenuItem - Leaf nel Composite Pattern
 * Rappresenta un'azione singola nel menu
 */
public class MenuItem implements MenuComponent {
    
    private String name;
    private MenuAction action;
    private List<MenuComponent> children; // Per supportare il composite pattern

    /**
     * Costruttore per un item di menu semplice
     */
    public MenuItem(String name, MenuAction action) {
        this.name = name;
        this.action = action;
        this.children = new ArrayList<>(); // Inizializza la lista anche se normalmente vuota
    }

    @Override
    public void display() {
        System.out.println("- " + name);
    }

    @Override
    public void execute() {
        if (action != null) {
            action.execute();
        }
    }

    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Implementazione di add per supportare il Composite Pattern
     * Anche se MenuItem è una foglia, implementiamo questo metodo
     * per mantenere l'uniformità dell'interfaccia
     */
    @Override
    public void add(MenuComponent component) {
        // In un MenuItem semplice, questo potrebbe trasformarlo in un sottomenu
        children.add(component);
    }
    
    /**
     * Implementazione di remove per supportare il Composite Pattern
     */
    @Override
    public void remove(MenuComponent component) {
        children.remove(component);
    }
    
    /**
     * Verifica se questo MenuItem ha figli (è diventato un composite)
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }
    
    /**
     * Ottiene i figli di questo MenuItem
     */
    public List<MenuComponent> getChildren() {
        return new ArrayList<>(children);
    }
}
