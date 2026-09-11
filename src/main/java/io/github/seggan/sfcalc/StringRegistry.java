package io.github.seggan.sfcalc;

import io.github.thebusybiscuit.slimefun5.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun5.libraries.dough.common.Validate;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Loads, caches and formats the configurable strings of SFCalc.
 */
public final class StringRegistry {

    private final Pattern percentPattern = Pattern.compile("(%s|%d)");
    private final String headerString;
    private final String headerAmountString;
    private final String stackString;
    private final String amountString;
    private final String neededString;
    private final String noItemString;
    private final String notANumberString;
    private final String tooManyCategoriesString;
    private final String tooManyItemsString;
    private final String notAPlayerString;
    private final String invalidNumberString;

    StringRegistry(FileConfiguration config, File file) {
        headerString = reformat(config, "header-string", 1);
        headerAmountString = reformat(config, "header-amount-string", 2, 1);
        stackString = reformat(config, "stack-string", 1, 2, 3, 4);
        amountString = reformat(config, "amount-string", 2, 1);
        neededString = reformat(config, "needed-string", 2, 1);
        noItemString = config.getString("no-item-string");
        notANumberString = config.getString("not-a-number-string");
        tooManyCategoriesString = config.getString("category-error-string");
        tooManyItemsString = config.getString("item-error-string");
        notAPlayerString = config.getString("not-a-player-string");
        invalidNumberString = config.getString("invalid-number-string");

        try {
            config.save(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String getHeaderString() {
        return headerString;
    }

    public String getHeaderAmountString() {
        return headerAmountString;
    }

    public String getStackString() {
        return stackString;
    }

    public String getAmountString() {
        return amountString;
    }

    public String getNeededString() {
        return neededString;
    }

    public String getNoItemString() {
        return noItemString;
    }

    public String getNotANumberString() {
        return notANumberString;
    }

    public String getTooManyCategoriesString() {
        return tooManyCategoriesString;
    }

    public String getTooManyItemsString() {
        return tooManyItemsString;
    }

    public String getNotAPlayerString() {
        return notAPlayerString;
    }

    public String getInvalidNumberString() {
        return invalidNumberString;
    }

    @Nonnull
    public static String format(@Nonnull String formatString, @Nonnull Object... objects) {
        Validate.notNull(formatString, "The format string must not be null!");
        Validate.noNullElements(objects, "The objects must not contain null elements!");

        String finalString = formatString;

        for (int i = 0; i < objects.length; i++) {
            finalString = finalString.replace("%" + (i + 1), objects[i].toString());
        }

        return ChatColors.color(finalString);
    }

    private String reformat(FileConfiguration config, String key, int... numbers) {
        String val = config.getString(key);

        AtomicInteger i = new AtomicInteger();
        String formatted = this.percentPattern.matcher(val)
            .replaceAll(matchResult -> "%" + numbers[i.getAndIncrement()]);

        if (i.get() == numbers.length) {
            config.set(key, formatted);
            return formatted;
        }

        return val;
    }

}
