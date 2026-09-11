package io.github.seggan.sfcalc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The main /sfcalc command. Dispatches to the calc, needed and website subcommands.
 * NeededCommand was removed upstream after the InfinityLib rewrite; it is restored
 * here as requested.
 */
public class SFCalcCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommands = new HashMap<>();
    private final CalcCommand calcCommand = new CalcCommand();

    public SFCalcCommand() {
        registerSubCommand(calcCommand);
        registerSubCommand(new NeededCommand());
        registerSubCommand(new WebsiteCommand());
    }

    private void registerSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(Locale.ROOT), subCommand);
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length == 0) {
            sendUsage(sender, label);
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase(Locale.ROOT));
        if (subCommand == null) {
            // no sub command given: the master branch treats the first argument as
            // the item id, so /sfcalc <item> [amount] keeps working like /sfcalc calc <item> [amount]
            calcCommand.execute(sender, args);
            return true;
        }

        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);

        subCommand.execute(sender, subArgs);
        return true;
    }

    private void sendUsage(CommandSender sender, String label) {
        sender.sendMessage(ChatColor.YELLOW + "SFCalc " + ChatColor.WHITE + "- commands:");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " calc <item> [amount] " + ChatColor.GRAY + "- calculates the resources needed");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " needed <item> [amount] " + ChatColor.GRAY + "- shows how much more you need (inventory-aware)");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " website " + ChatColor.GRAY + "- opens the SFCalc website");
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        List<String> tabs = new ArrayList<>();

        if (args.length == 1) {
            // when no sub command was typed yet, suggest item ids as well, so
            // bare mode /sfcalc <item> is tab-completable just like in upstream
            for (String name : subCommands.keySet()) {
                if (name.contains(args[0].toLowerCase(Locale.ROOT))) {
                    tabs.add(name);
                }
            }
            tabs.addAll(calcCommand.complete(sender, args));
            return tabs;
        }

        if (args.length >= 2) {
            SubCommand subCommand = subCommands.get(args[0].toLowerCase(Locale.ROOT));
            if (subCommand != null) {
                String[] subArgs = new String[args.length - 1];
                System.arraycopy(args, 1, subArgs, 0, subArgs.length);
                tabs.addAll(subCommand.complete(sender, subArgs));
            }
        }

        return tabs;
    }

}
