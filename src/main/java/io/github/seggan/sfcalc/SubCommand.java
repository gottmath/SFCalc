package io.github.seggan.sfcalc;

import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * A single sub command of /sfcalc. Replaces the InfinityLib SubCommand that was
 * removed upstream together with NeededCommand and WebsiteCommand.
 */
abstract class SubCommand {

    private final String name;
    private final String description;

    SubCommand(@Nonnull String name, @Nonnull String description) {
        this.name = name.toLowerCase(Locale.ROOT);
        this.description = description;
    }

    @Nonnull
    String getName() {
        return name;
    }

    @Nonnull
    String getDescription() {
        return description;
    }

    abstract void execute(@Nonnull CommandSender sender, @Nonnull String[] args);

    @Nonnull
    List<String> complete(@Nonnull CommandSender sender, @Nonnull String[] args) {
        return Collections.emptyList();
    }

}
