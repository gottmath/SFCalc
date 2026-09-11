package io.github.seggan.sfcalc;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.dough.common.CommonPatterns;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static io.github.seggan.sfcalc.StringRegistry.format;

/**
 * Tells the player how much more of each resource they still need, taking their
 * inventory into account. This command was removed from the upstream master branch
 * when InfinityLib was dropped (commit 9090993, 2024); it is restored here on top
 * of the current master logic as requested.
 */
public class NeededCommand extends SubCommand {

    private static final List<String> ids = new ArrayList<>();
    private final SFCalc plugin = SFCalc.inst();

    public NeededCommand() {
        super("needed", "Tells you how much more resources are needed");
    }

    @Override
    void execute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        long amount;
        String reqItem;
        SlimefunItem item;

        StringRegistry registry = plugin.getStringRegistry();

        if (!(sender instanceof Player)) {
            sender.sendMessage(format(registry.getNotAPlayerString()));
            return;
        }

        if (args.length > 2 || args.length == 0) {
            return;
        }

        reqItem = args[0];

        if (args.length == 1) {
            amount = 1;
        } else if (!CommonPatterns.NUMERIC.matcher(args[1]).matches()) {
            sender.sendMessage(format(registry.getNotANumberString()));
            return;
        } else {
            try {
                amount = Long.parseLong(args[1]);
                if (amount == 0 || amount > Integer.MAX_VALUE) {
                    sender.sendMessage(format(registry.getInvalidNumberString()));
                    return;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(format(registry.getInvalidNumberString()));
                return;
            }
        }

        item = SlimefunItem.getById(reqItem.toUpperCase(Locale.ROOT));

        if (item == null) {
            sender.sendMessage(format(registry.getNoItemString()));
            return;
        }

        SFCalcMetrics.addItemSearched(item.getItemName());

        plugin.getCalc().printResults(sender, item, amount, true);
    }

    @Override
    @Nonnull
    List<String> complete(@Nonnull CommandSender sender, @Nonnull String[] args) {
        List<String> tabs = new ArrayList<>();
        if (ids.isEmpty()) {
            for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
                if (!item.isHidden()) {
                    ids.add(item.getId().toLowerCase(Locale.ROOT));
                }
            }
        }

        if (args.length == 1) {
            for (String id : ids) {
                if (id.contains(args[0].toLowerCase(Locale.ROOT))) {
                    tabs.add(id);
                }
            }
        }

        return tabs;
    }

}
