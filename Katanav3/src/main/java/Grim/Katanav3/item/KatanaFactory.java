package Grim.Katanav3.item;

import Grim.Katanav3.KatanasPlugin;
import Grim.Katanav3.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Level;

public class KatanaFactory {

    private final KatanasPlugin plugin;
    private final Map<String, Katana> katanaRegistry = new HashMap<>();

    public KatanaFactory(KatanasPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadKatanas() {
        katanaRegistry.clear();
        Bukkit.resetRecipes(); // Simple way to handle reloads

        ConfigurationSection katanasSection = plugin.getConfig().getConfigurationSection("katanas");
        if (katanasSection == null) {
            plugin.getLogger().warning("No 'katanas' section found in config.yml!");
            return;
        }

        for (String katanaId : katanasSection.getKeys(false)) {
            ConfigurationSection section = katanasSection.getConfigurationSection(katanaId);
            if (section == null || !section.getBoolean("enabled", false)) {
                continue;
            }

            try {
                // Build the ItemStack
                ItemStack item = buildItemStack(katanaId, section);

                // Build the Recipe
                Recipe recipe = buildRecipe(katanaId, section, item);
                if (recipe != null) {
                    Bukkit.addRecipe(recipe);
                }

                // Register the final Katana object
                katanaRegistry.put(katanaId.toLowerCase(), new Katana(katanaId, item, recipe));
                plugin.getLogger().info("Loaded katana: " + katanaId);

            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load katana: " + katanaId, e);
            }
        }
    }

    private ItemStack buildItemStack(String katanaId, ConfigurationSection section) {
        Material material = Material.matchMaterial(section.getString("material", ""));
        if (material == null) {
            plugin.getLogger().warning("Invalid material for katana: " + katanaId);
            return null;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatUtils.color(section.getString("display-name")));
        meta.setCustomModelData(section.getInt("custom-model-data"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        // Add Attributes
        AttributeModifier damage = new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", section.getDouble("stats.damage"), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        AttributeModifier speed = new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", section.getDouble("stats.attack-speed") - 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damage);
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, speed);

        // Add Lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatUtils.color("&7A powerful, custom-crafted katana."));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private Recipe buildRecipe(String katanaId, ConfigurationSection section, ItemStack result) {
        if (!section.getBoolean("recipe.enabled")) {
            return null;
        }

        NamespacedKey key = new NamespacedKey(plugin, katanaId);
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(section.getStringList("recipe.shape").toArray(new String[0]));

        ConfigurationSection ingredients = section.getConfigurationSection("recipe.ingredients");
        if (ingredients != null) {
            for (String ingKey : ingredients.getKeys(false)) {
                String ingredientValue = ingredients.getString(ingKey);
                Katana customItem = getKatana(ingredientValue); // Check if it's a custom item

                if (customItem != null) {
                    recipe.setIngredient(ingKey.charAt(0), new RecipeChoice.ExactChoice(customItem.getItemStack()));
                } else {
                    Material ingMat = Material.matchMaterial(ingredientValue);
                    if (ingMat != null) {
                        recipe.setIngredient(ingKey.charAt(0), ingMat);
                    }
                }
            }
        }
        return recipe;
    }

    public Katana getKatana(String id) {
        return katanaRegistry.get(id.toLowerCase());
    }

    public Set<String> getAllKatanaIds() {
        return katanaRegistry.keySet();
    }
}