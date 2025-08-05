package Grim.Katanav3.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class Katana {
    private final String id;
    private final ItemStack itemStack;
    private final Recipe recipe;

    public Katana(String id, ItemStack itemStack, Recipe recipe) {
        this.id = id;
        this.itemStack = itemStack;
        this.recipe = recipe;
    }

    public String getId() {
        return id;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public Recipe getRecipe() {
        return recipe;
    }
}