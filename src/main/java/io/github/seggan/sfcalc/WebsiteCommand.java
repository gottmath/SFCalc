package io.github.seggan.sfcalc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class WebsiteCommand extends SubCommand {

    public WebsiteCommand() {
        super("website", "Gives the SFCalc website");
    }

    @Override
    void execute(@Nonnull CommandSender commandSender, @Nonnull String[] strings) {
        if (commandSender instanceof Player p) {
            // bungee-chat is gone in Paper 26.2; send an adventure component instead
            p.sendMessage(net.kyori.adventure.text.Component.text(
                "Click this message to go to the SFCalc website",
                net.kyori.adventure.text.format.NamedTextColor.YELLOW)
                .clickEvent(net.kyori.adventure.text.event.ClickEvent.openUrl("https://sfcalc-online.pages.dev")));
        } else {
            commandSender.sendMessage("https://sfcalc-online.pages.dev");
        }
    }

}
