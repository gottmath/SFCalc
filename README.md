# SFCalc
A calculator for the plugin Slimefun. This is based off of john000708's SlimeCalculator.

**Unofficial port for Slimefun5 (5.3.3) / Paper 26.2.**
Based on the current master of Seggan/SFCalc (archived 10/2025), with the
`/sfcalc needed` command (and the website command) restored on top of the
current master logic (recursive recipes, hidden-item NPE fix, contains-tab-completion).

## Usage

This addon for Slimefun adds a new command: /sfcalc. This command can calculate how much basic slimefun resources you need to make the specified item.

Usage: `/sfcalc calc [required: item] [optional: amount]` (or just `/sfcalc <item> [amount]`)

Item is the id of the item you want to craft. An item id is like this: "electric_motor", "carbonado", "solar_generator" (basic solar generator), etc. For tiered machines that are not lowest tier, add the tier number after the id. For example: "solar_generator_3" (carbonado solar generator), "lava_generator_2" (advanced lava generator), "carbon_press" (carbon press [tier 1]), etc.

Amount is the number of items you want to craft. It defaults to 1 if you don't specify any amount.

Using `/sfcalc needed <item> [amount]` instead will show the items you still need to craft the item, taking the contents of your inventory into account.

Using `/sfcalc website` will print the SFCalc online calculator link.

## Build
Maven project (JDK 25+/Java 17 target): `mvn clean package`

Minecraft version: 26.x (Paper API 26.2.build.121-stable)
Slimefun version: 5.3.3 (fork Slimefun5/Slimefun5)