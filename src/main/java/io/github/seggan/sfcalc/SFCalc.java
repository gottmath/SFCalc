package io.github.seggan.sfcalc;

import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class SFCalc extends JavaPlugin implements Listener {

    private static SFCalc instance;
    private final Set<RecipeType> blacklistedRecipes = new HashSet<>();
    private final Set<String> blacklistedIds = new HashSet<>();
    private Calculator calculator;
    private StringRegistry stringRegistry;

    @Override
    public void onEnable() {
        instance = this;

        // auto updates are not possible with this unofficial port
        // (version is not a blob build); deliberately not calling the updater

        saveDefaultConfig();
        healConfigDefaults(getConfig());

        stringRegistry = new StringRegistry(getConfig(), new File(getDataFolder(), "config.yml"));
        calculator = new Calculator(this);

        blacklistedRecipes.add(RecipeType.ORE_WASHER);
        blacklistedRecipes.add(RecipeType.GEO_MINER);
        blacklistedRecipes.add(RecipeType.GOLD_PAN);
        blacklistedRecipes.add(RecipeType.MOB_DROP);
        blacklistedRecipes.add(RecipeType.BARTER_DROP);
        blacklistedRecipes.add(RecipeType.ORE_CRUSHER);
        blacklistedRecipes.add(RecipeType.NULL);

        blacklistedIds.add("UU_MATTER");
        blacklistedIds.add("SILICON");
        blacklistedIds.add("FALLEN_METEOR");
        blacklistedIds.add("RUBBER");
        blacklistedIds.add("VOID_BIT");
        if (getConfig().getBoolean("options.use-carbon-instead-of-coal", true)) {
            blacklistedIds.add("CARBON");
        }

        new SFCalcMetrics(this);

        SFCalcCommand executor = new SFCalcCommand();
        PluginCommand command = getCommand("sfcalc");
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    /**
     * Fills in defaults for any string key missing from the config, so that a
     * config.yml from an older version cannot produce null messages (NPE in
     * StringRegistry.reformat or Validate.notNull in format). The defaults are
     * then written back to the file by StringRegistry, healing old configs.
     */
    private void healConfigDefaults(FileConfiguration config) {
        config.addDefault("header-string", "&e&nRecipe for %1:");
        config.addDefault("header-amount-string", "&e&nRecipe for %2 %1:");
        config.addDefault("stack-string", "&e%1 (%2 x%3 + %4)");
        config.addDefault("amount-string", "&e%2 of %1");
        config.addDefault("needed-string", "&e%2 more %1 needed");
        config.addDefault("no-item-string", "&cThat item was not found!");
        config.addDefault("not-a-number-string", "&cThat's not a number!");
        config.addDefault("category-error-string", "&cThat many categories is not supported yet. Please use the command form of the calculator.");
        config.addDefault("item-error-string", "&cThat many items is not supported yet. Please use the command form of the calculator.");
        config.addDefault("not-a-player-string", "&cYou must be a player to send this message!");
        config.addDefault("invalid-number-string", "&cInvalid number!");
        config.addDefault("options.use-carbon-instead-of-coal", true);
    }

    @Nonnull
    static SFCalc inst() {
        return instance;
    }

    public Calculator getCalc() {
        return calculator;
    }

    public StringRegistry getStringRegistry() {
        return stringRegistry;
    }

    public Set<RecipeType> getBlacklistedRecipes() {
        return blacklistedRecipes;
    }

    public Set<String> getBlacklistedIds() {
        return blacklistedIds;
    }

}
