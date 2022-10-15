package com.zettelnet.levelhearts.configuration;

import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.zettelnet.levelhearts.LevelHearts;
import com.zettelnet.levelhearts.LevelHeartsPermissions;
import com.zettelnet.levelhearts.LevelHeartsPlugin;
import com.zettelnet.levelhearts.event.PlayerHealthChangeEvent;
import com.zettelnet.levelhearts.event.PlayerMaxHealthChangeEvent;
import com.zettelnet.levelhearts.health.HealthFormat;
import com.zettelnet.levelhearts.lib.net.gravitydevelopment.updater.Updater;
import com.zettelnet.levelhearts.zet.chat.ChatMessage;
import com.zettelnet.levelhearts.zet.chat.FormatOption;
import com.zettelnet.levelhearts.zet.chat.MessageValueMap;
import com.zettelnet.levelhearts.zet.configuration.LanguageConfigurationFile;
import com.zettelnet.levelhearts.zet.event.EventNotifier;

/**
 * @author yanni
 */
public class LanguageConfiguration extends LanguageConfigurationFile {

    private LevelHeartsPlugin plugin;
    private Logger log;
    private LevelHeartsPermissions perm;

    private MainConfiguration config;
    private boolean changeMessages;
    private boolean commandMessages;

    private String lang;

    // command.error
    public ChatMessage CommandErrorSyntax;
    public ChatMessage CommandErrorInvalidArgument;
    public ChatMessage CommandErrorInvalidArgumentType;
    public ChatMessage CommandErrorMissingArgument;
    public ChatMessage CommandErrorNotPlayer;
    public ChatMessage CommandErrorNotNumber;
    public ChatMessage CommandErrorNotPositiveNumber;
    public ChatMessage CommandErrorNoPermission;
    public ChatMessage CommandErrorDisabled;
    // command.argument
    public ChatMessage CommandArgumentAmount;
    public ChatMessage CommandArgumentPlayer;
    public ChatMessage CommandArgumentHealth;
    public ChatMessage CommandArgumentMaxHealth;
    // command.help
    public ChatMessage CommandHelpCommand;
    // command.info.self
    public ChatMessage CommandInfoSelfGet;
    public ChatMessage CommandInfoSelfIncrease;
    public ChatMessage CommandInfoSelfNoIncrease;
    // command.info.other
    public ChatMessage CommandInfoOtherGet;
    public ChatMessage CommandInfoOtherIncrease;
    public ChatMessage CommandInfoOtherNoIncrease;
    // command.health
    public ChatMessage CommandHealthSet;
    public ChatMessage CommandHealthGive;
    public ChatMessage CommandHealthTake;
    public ChatMessage CommandHealthRestore;
    // command.maxHealth
    public ChatMessage CommandMaxHealthSet;
    public ChatMessage CommandMaxHealthGive;
    public ChatMessage CommandMaxHealthTake;
    public ChatMessage CommandMaxHealthReset;
    // command.syntax
    public FormatOption CommandSyntaxHealthGeneral;
    public FormatOption CommandSyntaxHealthHelp;
    public FormatOption CommandSyntaxHealthGet;
    public FormatOption CommandSyntaxHealthSet;
    public FormatOption CommandSyntaxHealthGive;
    public FormatOption CommandSyntaxHealthTake;
    public FormatOption CommandSyntaxHealthRestore;
    public FormatOption CommandSyntaxMaxHealthGeneral;
    public FormatOption CommandSyntaxMaxHealthHelp;
    public FormatOption CommandSyntaxMaxHealthGet;
    public FormatOption CommandSyntaxMaxHealthSet;
    public FormatOption CommandSyntaxMaxHealthGive;
    public FormatOption CommandSyntaxMaxHealthTake;
    public FormatOption CommandSyntaxMaxHealthReset;
    public FormatOption CommandSyntaxPluginGeneral;
    public FormatOption CommandSyntaxPluginHelp;
    public FormatOption CommandSyntaxPluginReload;

    // health
    public ChatMessage HealthDisplay;
    public ChatMessage HealthStatus;
    // health.change.command
    public ChatMessage HealthChangeCommandSet;
    public ChatMessage HealthChangeCommandGive;
    public ChatMessage HealthChangeCommandTake;
    public ChatMessage HealthChangeCommandRestore;
    // health.help
    public ChatMessage HealthHelpGeneral;
    public ChatMessage HealthHelpGet;
    public ChatMessage HealthHelpSet;
    public ChatMessage HealthHelpGive;
    public ChatMessage HealthHelpTake;
    public ChatMessage HealthHelpRestore;

    // maxHealth
    public ChatMessage MaxHealthDisplay;
    public ChatMessage MaxHealthStatus;
    // maxHealth.change
    public ChatMessage MaxHealthChangeLevelUp;
    public ChatMessage MaxHealthChangeDeath;
    public ChatMessage MaxHealthChangeLogin;
    // maxHealth.change.command
    public ChatMessage MaxHealthChangeCommandSet;
    public ChatMessage MaxHealthChangeCommandGive;
    public ChatMessage MaxHealthChangeCommandTake;
    public ChatMessage MaxHealthChangeCommandReset;
    // maxHealth.help
    public ChatMessage MaxHealthHelpGeneral;
    public ChatMessage MaxHealthHelpGet;
    public ChatMessage MaxHealthHelpSet;
    public ChatMessage MaxHealthHelpGive;
    public ChatMessage MaxHealthHelpTake;
    public ChatMessage MaxHealthHelpReset;

    // plugin
    public ChatMessage PluginPrefix;
    public ChatMessage PluginInfo;
    public ChatMessage PluginWebsite;
    public ChatMessage PluginReload;
    public ChatMessage PluginUpdateAvailable;
    // plugin.help
    public ChatMessage PluginHelpReload;

    public LanguageConfiguration() {
        super(LevelHearts.getPlugin(), "lang.yml", "lang.yml");

        plugin = LevelHearts.getPlugin();
        log = LevelHearts.getLog();
        perm = LevelHearts.getPermissionManager();

        config = LevelHearts.getConfiguration();
        changeMessages = config.chatChangeMessagesEnabled();
        commandMessages = config.chatCommandMessagesEnabled();

        lang = LevelHearts.getConfiguration().chatLanguage();

        MessageValueMap formatOptions = getFormatOptions();

        // Umlauts
        formatOptions.put("Auml", new FormatOption("\u00C4", "Ae", "Ae"));
        formatOptions.put("Ouml", new FormatOption("\u00D6", "Oe", "Oe"));
        formatOptions.put("Uuml", new FormatOption("\u00DC", "Ue", "Ue"));
        formatOptions.put("auml", new FormatOption("\u00E4", "ae", "ae"));
        formatOptions.put("ouml", new FormatOption("\u00F6", "oe", "oe"));
        formatOptions.put("uuml", new FormatOption("\u00FC", "ue", "ue"));

        // Cyrillic letters
        formatOptions.put("Acyrl", new FormatOption("\u0410", "A", "A"));
        formatOptions.put("BEcyrl", new FormatOption("\u0411", "BE", "BE"));
        formatOptions.put("VEcyrl", new FormatOption("\u0412", "VE", "VE"));
        formatOptions.put("GHEcyrl", new FormatOption("\u0413", "GHE", "GHE"));
        formatOptions.put("DEcyrl", new FormatOption("\u0414", "DE", "DE"));
        formatOptions.put("IEcyrl", new FormatOption("\u0415", "IE", "IE"));
        formatOptions.put("ZHEcyrl", new FormatOption("\u0416", "ZHE", "ZHE"));
        formatOptions.put("ZEcyrl", new FormatOption("\u0417", "ZE", "ZE"));
        formatOptions.put("Icyrl", new FormatOption("\u0418", "I", "I"));
        formatOptions.put("Icyrls", new FormatOption("\u0419", "I", "I"));
        formatOptions.put("KAcyrl", new FormatOption("\u041A", "KA", "KA"));
        formatOptions.put("ELcyrl", new FormatOption("\u041B", "EL", "EL"));
        formatOptions.put("EMcyrl", new FormatOption("\u041C", "EM", "EM"));
        formatOptions.put("ENcyrl", new FormatOption("\u041D", "EN", "EN"));
        formatOptions.put("Ocyrl", new FormatOption("\u041E", "O", "O"));
        formatOptions.put("PEcyrl", new FormatOption("\u041F", "PE", "PE"));
        formatOptions.put("ERcyrl", new FormatOption("\u0420", "ER", "ER"));
        formatOptions.put("EScyrl", new FormatOption("\u0421", "ES", "ES"));
        formatOptions.put("TEcyrl", new FormatOption("\u0422", "TE", "TE"));
        formatOptions.put("Ucyrl", new FormatOption("\u0423", "U", "U"));
        formatOptions.put("EFcyrl", new FormatOption("\u0424", "EF", "EF"));
        formatOptions.put("HAcyrl", new FormatOption("\u0425", "HA", "HA"));
        formatOptions.put("TSEcyrl", new FormatOption("\u0426", "TSE", "TSE"));
        formatOptions.put("CHEcyrl", new FormatOption("\u0427", "CHE", "CHE"));
        formatOptions.put("SHAcyrl", new FormatOption("\u0428", "SHA", "SHA"));
        formatOptions.put("SHCHAcyrl", new FormatOption("\u0429", "SHCHA", "SHCHA"));
        formatOptions.put("SIGNcyrlh", new FormatOption("\u042A", "SIGN", "SIGN"));
        formatOptions.put("YERUcyrl", new FormatOption("\u042B", "YERU", "YERU"));
        formatOptions.put("SIGNcyrls", new FormatOption("\u042C", "SIGN", "SIGN"));
        formatOptions.put("Ecyrl", new FormatOption("\u042D", "E", "E"));
        formatOptions.put("YUcyrl", new FormatOption("\u042E", "YU", "YU"));
        formatOptions.put("YAcyrl", new FormatOption("\u042F", "YA", "YA"));
        formatOptions.put("acyrl", new FormatOption("\u0430", "a", "a"));
        formatOptions.put("becyrl", new FormatOption("\u0431", "be", "be"));
        formatOptions.put("vecyrl", new FormatOption("\u0432", "ve", "ve"));
        formatOptions.put("ghecyrl", new FormatOption("\u0433", "ghe", "ghe"));
        formatOptions.put("decyrl", new FormatOption("\u0434", "de", "de"));
        formatOptions.put("iecyrl", new FormatOption("\u0435", "ie", "ie"));
        formatOptions.put("zhecyrl", new FormatOption("\u0436", "zhe", "zhe"));
        formatOptions.put("zecyrl", new FormatOption("\u0437", "ze", "ze"));
        formatOptions.put("icyrl", new FormatOption("\u0438", "i", "i"));
        formatOptions.put("icyrls", new FormatOption("\u0439", "i", "i"));
        formatOptions.put("kacyrl", new FormatOption("\u043A", "ka", "ka"));
        formatOptions.put("elcyrl", new FormatOption("\u043B", "el", "el"));
        formatOptions.put("emcyrl", new FormatOption("\u043C", "em", "em"));
        formatOptions.put("encyrl", new FormatOption("\u043D", "en", "en"));
        formatOptions.put("ocyrl", new FormatOption("\u043E", "o", "o"));
        formatOptions.put("pecyrl", new FormatOption("\u043F", "pe", "pe"));
        formatOptions.put("ercyrl", new FormatOption("\u0440", "er", "er"));
        formatOptions.put("escyrl", new FormatOption("\u0441", "es", "es"));
        formatOptions.put("tecyrl", new FormatOption("\u0442", "te", "te"));
        formatOptions.put("ucyrl", new FormatOption("\u0443", "u", "u"));
        formatOptions.put("efcyrl", new FormatOption("\u0444", "ef", "ef"));
        formatOptions.put("hacyrl", new FormatOption("\u0445", "ha", "ha"));
        formatOptions.put("tsecyrl", new FormatOption("\u0446", "tse", "tse"));
        formatOptions.put("checyrl", new FormatOption("\u0447", "che", "che"));
        formatOptions.put("shacyrl", new FormatOption("\u0448", "sha", "sha"));
        formatOptions.put("shchacyrl", new FormatOption("\u0449", "shcha", "shcha"));
        formatOptions.put("signcyrlh", new FormatOption("\u044A", "sign", "sign"));
        formatOptions.put("yerucyrl", new FormatOption("\u044B", "yeru", "yeru"));
        formatOptions.put("signcyrls", new FormatOption("\u044C", "sign", "sign"));
        formatOptions.put("ecyrl", new FormatOption("\u044D", "e", "e"));
        formatOptions.put("yucyrl", new FormatOption("\u044E", "yu", "yu"));
        formatOptions.put("yacyrl", new FormatOption("\u044F", "ya", "ya"));
    }

    @Override
    protected void loadValues(FileConfiguration config, FileConfiguration defaults) {
        update(config);

        // plugin.prefix
        PluginPrefix = load("plugin.prefix", "&(gray)[&(darkRed)LevelHearts&(gray)] ");
        addFormatOption("prefix", PluginPrefix.toFormatOption());

        // command.error
        CommandErrorSyntax = load("command.error.syntax", "&(red)Syntax: &(yellow)%(syntax)");
        CommandErrorInvalidArgument = load("command.error.invalidArgument", "&(red)Argument \"%(arg)\" invalid");
        CommandErrorInvalidArgumentType = load("command.error.invalidArgumentType", "&(red)Argument %(argType) \"%(arg)\" invalid");
        CommandErrorMissingArgument = load("command.error.missingArgument", "&(red)Missing argument %(argType)");
        CommandErrorNotPlayer = load("command.error.notPlayer", "&(red)The player %(player) is currently not online");
        CommandErrorNotNumber = load("command.error.notNumber", "&(red)%(number) is not a valid number");
        CommandErrorNotPositiveNumber = load("command.error.notPositiveNumber", "&(red)%(number) is not a positive number");
        CommandErrorNoPermission = load("command.error.noPermission", "&(red)You don't have permission to do this");
        CommandErrorDisabled = load("command.error.disabled", "&(red)This command is disabled");
        // command.argument
        CommandArgumentAmount = load("command.argument.amount", "amount");
        CommandArgumentPlayer = load("command.argument.player", "player");
        CommandArgumentHealth = load("command.argument.health", "health amount");
        CommandArgumentMaxHealth = load("command.argument.maxHealth", "maximum health amount");
        // command.help
        CommandHelpCommand = load("command.help.command", "&(yellow)/%(syntax)&(gray) - &(white)%(description)");
        // command.info.self
        CommandInfoSelfGet = load("command.info.self.get", "&(prefix)&(gold)You have %(health)&(gold) of %(maxHealth)&(gold) health!");
        CommandInfoSelfIncrease = load("command.info.self.increase",
                "&(yellow)Your maximum health will increase on level &(gold)%(maxHealthIncreaseLevel)&(yellow)!");
        CommandInfoSelfNoIncrease = load("command.info.self.noIncrease", "&(yellow)You can no longer increase your maximum health.");
        // command.info.other
        CommandInfoOtherGet = load("command.info.other.get", "&(prefix)&(gold)%(player) has %(health)&(gold) of %(maxHealth)&(gold) health!");
        CommandInfoOtherIncrease = load("command.info.other.increase",
                "&(yellow)His maximum health will increase on level &(gold)%(maxHealthIncreaseLevel)&(yellow)!");
        CommandInfoOtherNoIncrease = load("command.info.other.noIncrease", "&(yellow)He can no longer increase his maximum health.");
        // command.health
        CommandHealthSet = load("command.health.set", "&(yellow)You have changed %(player)'s health to %(health)&(yellow).");
        CommandHealthGive = load("command.health.give", "&(yellow)You have given %(player) %(healthChange)&(yellow) health.&(br)%(status)");
        CommandHealthTake = load("command.health.take", "&(yellow)You have taken %(healthChange) health from %(player).&(br)%(status)");
        CommandHealthRestore = load("command.health.restore", "&(yellow)You have restored %(player)'s health.&(br)%(status)");
        // command.maxHealth
        CommandMaxHealthSet = load("command.maxHealth.set", "&(yellow)You have changed %(player)'s maximum health to %(maxHealth)&(yellow).");
        CommandMaxHealthGive = load("command.maxHealth.give", "&(yellow)You have given %(player) %(maxHealthChange)&(yellow) maximum health.&(br)%(status)");
        CommandMaxHealthTake = load("command.maxHealth.take",
                "&(yellow)You have taken %(maxHealthChange)&(yellow) maximum health from %(player).&(br)%(status)");
        CommandMaxHealthReset = load("command.maxHealth.reset", "&(yellow)You have reset %(player)'s maximum health.&(br)%(status)");
        // command.syntax
        CommandSyntaxHealthGeneral = new FormatOption(String.format("health [%s]", CommandArgumentPlayer), String.format("health <%s>", CommandArgumentPlayer));
        CommandSyntaxHealthHelp = new FormatOption("health help");
        CommandSyntaxHealthGet = new FormatOption(String.format("health get [%s]", CommandArgumentPlayer), String.format("health get <%s>",
                CommandArgumentPlayer));
        CommandSyntaxHealthSet = new FormatOption(String.format("health set [%s] <%s>", CommandArgumentPlayer, CommandArgumentHealth), String.format(
                "health set <%s> <%s>", CommandArgumentPlayer, CommandArgumentAmount));
        CommandSyntaxHealthGive = new FormatOption(String.format("health give [%s] <%s>", CommandArgumentPlayer, CommandArgumentHealth), String.format(
                "health give <%s> <%s>", CommandArgumentPlayer, CommandArgumentAmount));
        CommandSyntaxHealthTake = new FormatOption(String.format("health take [%s] <%s>", CommandArgumentPlayer, CommandArgumentHealth), String.format(
                "health take <%s> <%s>", CommandArgumentPlayer, CommandArgumentAmount));
        CommandSyntaxHealthRestore = new FormatOption(String.format("health restore [%s]", CommandArgumentPlayer), String.format("health restore <%s>",
                CommandArgumentPlayer));
        CommandSyntaxMaxHealthGeneral = new FormatOption(String.format("maxhealth [%s]", CommandArgumentPlayer), String.format("maxhealth <%s>",
                CommandArgumentPlayer));
        CommandSyntaxMaxHealthHelp = new FormatOption("maxhealth help");
        CommandSyntaxMaxHealthGet = new FormatOption(String.format("maxhealth get [%s]", CommandArgumentPlayer), String.format("maxhealth get <%s>",
                CommandArgumentPlayer));
        CommandSyntaxMaxHealthSet = new FormatOption(String.format("maxhealth set [%s] <%s>", CommandArgumentPlayer, CommandArgumentAmount), String.format(
                "maxhealth set <%s> <%s>", CommandArgumentPlayer, CommandArgumentAmount));
        CommandSyntaxMaxHealthGive = new FormatOption(String.format("maxhealth give [%s] <%s>", CommandArgumentPlayer, CommandArgumentAmount), String.format(
                "maxhealth give <%s> <%s>", CommandArgumentPlayer, CommandArgumentAmount));
        CommandSyntaxMaxHealthTake = new FormatOption(String.format("maxhealth take [%s] <%s>", CommandArgumentPlayer, CommandArgumentAmount), String.format(
                "maxhealth take <%s> <%s>", CommandArgumentPlayer, CommandArgumentAmount));
        CommandSyntaxMaxHealthReset = new FormatOption(String.format("maxhealth reset [%s]", CommandArgumentPlayer), String.format("maxhealth reset <%s>",
                CommandArgumentPlayer));
        CommandSyntaxPluginGeneral = new FormatOption("levelhearts");
        CommandSyntaxPluginHelp = new FormatOption("levelhearts help");
        CommandSyntaxPluginReload = new FormatOption("levelhearts reload");

        // health
        HealthDisplay = load("health.display", "&(red)%(health) &(heart)");
        HealthStatus = load("health.status", "&(white) - &(yellow)Current health: %(health)&(yellow).");
        // health.change.command
        HealthChangeCommandSet = load("health.change.command.set", "&(prefix)&(yellow)Your health has been set to %(health).");
        HealthChangeCommandGive = load("health.change.command.give", "&(prefix)&(yellow)Your health has been increased by %(healthChange).");
        HealthChangeCommandTake = load("health.change.command.take", "&(prefix)&(yellow)Your health has been decreased by %(healthChange).");
        HealthChangeCommandRestore = load("health.change.command.restore", "&(prefix)&(yellow)Your health has been restored.");
        // health.help
        HealthHelpGeneral = load("health.help.general", "Health specific commands");
        HealthHelpGet = load("health.help.get", "Display the health of a player");
        HealthHelpSet = load("health.help.set", "Change the health of a player");
        HealthHelpGive = load("health.help.give", "Give health to a player");
        HealthHelpTake = load("health.help.take", "Take health from a player");
        HealthHelpRestore = load("health.help.restore", "Restore the health of a player");

        // maxHealth
        MaxHealthDisplay = load("maxHealth.display", "&(red)%(maxHealth) &(heart)");
        MaxHealthStatus = load("maxHealth.status", "&(white) - &(yellow)Current maximum health: %(maxHealth)&(yellow).");
        // maxHealth.change
        MaxHealthChangeLevelUp = load("maxHealth.change.levelUp", "&(prefix)&(gold)You leveled up and your maximum health has increased!");
        MaxHealthChangeDeath = load("maxHealth.change.death", "&(prefix)&(yellow)Your maximum health has been reset because you died.");
        MaxHealthChangeLogin = load("maxHealth.change.login", "&(prefix)&(yellow)Your maximum health has been reset because you left the server.");
        // maxHealth.change.command
        MaxHealthChangeCommandSet = load("maxHealth.change.command.set", "&(prefix)&(yellow)Your maximum health has been set to %(maxHealth).");
        MaxHealthChangeCommandGive = load("maxHealth.change.command.give", "&(prefix)&(yellow)Your maximum health has been increased by %(maxHealthChange).");
        MaxHealthChangeCommandTake = load("maxHealth.change.command.take", "&(prefix)&(yellow)Your maximum health has been decreased by %(maxHealthChange).");
        MaxHealthChangeCommandReset = load("maxHealth.change.command.reset", "&(prefix)&(yellow)Your maximum health has been reset.");
        // maxHealth.help
        MaxHealthHelpGeneral = load("maxHealth.help.general", "Maximum health specific commands");
        MaxHealthHelpGet = load("maxHealth.help.get", "Display the maximum health of a player");
        MaxHealthHelpSet = load("maxHealth.help.set", "Change the maximum health of a player");
        MaxHealthHelpGive = load("maxHealth.help.give", "Give maximum health to a player");
        MaxHealthHelpTake = load("maxHealth.help.take", "Take maximum health from a player");
        MaxHealthHelpReset = load("maxHealth.help.reset", "Resets the maximum health of a player");

        // plugin
        PluginInfo = load("plugin.info", "&(prefix)&(yellow)This server is running &(red)LevelHearts&(yellow) v%(version) by %(creator)!");
        PluginWebsite = load("plugin.website", "&(yellow)Website: &(gray)%(website)");
        PluginReload = load("plugin.reload", "&(prefix)&(white)Reloaded plugin configurations &(...)");
        PluginUpdateAvailable = load("plugin.updateAvailable",
                "&(prefix)&(yellow)An update is available &(white)(%(updateName) for %(updateGameVersion))&(yellow)!&(br)&(white)Download it at &(gray)%(updateLink).");
        // plugin.help
        PluginHelpReload = load("plugin.help.reload", "Reloads the plugin");
    }

    @Override
    public void update(FileConfiguration config) {
        if (!config.getBoolean("config.autoUpdate", true)) {
            return;
        }
        String version = config.getString("config.version", "unknown");
        switch (version) {
            default:
            case "unknown":
            case "0.1":
            case "0.1.0":
            case "1.7.2-R0.1":
            case "0.1.1":
            case "1.7.2-R0.2":
            case "1.0":
            case "1.0.0":
            case "1.7.9-R0.1":
            case "1.1":
            case "1.1.0":
            case "1.7.9-R0.2":
                updateStart("1.0");
                log.warning("Language config lang.yml is outdated! LevelHearts will fallback to defaults for values it cannot find.");
                log.warning("If you want to disable this warning change 'config.version' in lang.yml to the current version of LevelHearts.");
                setIfNotExists("config.version", "1.1");
                setIfNotExists("config.autoUpdate", true);
            case "1.2":
            case "1.2.0":
            case "1.2.1":
            case "1.2.2":
            case "1.2.3":
            case "1.2.4":
            case "1.3":
            case "1.3.1":
            case "1.3.2":
            case "1.4":
            case "1.4.1":
            case "1.4.2":
            case "1.4.3":
            case "1.4.4":
            case "1.4.5":
            case "1.4.6":
            case "1.4.7":
            case "1.4.8":
                updateDone("1.4.8");
        }
    }

    @Override
    public String getLanguage() {
        return lang;
    }

    public void sendHealthInfoSelf(CommandSender sender, Player player, double health, double maxHealth, int nextMaxHealthIncreaseLevel) {
        HealthFormat healthFormat = LevelHearts.getHealthFormat();
        CommandInfoSelfGet.send(sender, "player", player.getName(), "health", healthFormat.formatHealth(health, sender), "maxHealth", healthFormat.formatHealth(maxHealth, sender));
        if (nextMaxHealthIncreaseLevel > 0) {
            CommandInfoSelfIncrease.send(sender, "player", player.getName(), "maxHealthIncreaseLevel", nextMaxHealthIncreaseLevel);
        } else {
            CommandInfoSelfNoIncrease.send(sender, "player", player.getName());
        }
    }

    public void sendHealthInfoOther(CommandSender sender, Player player, double health, double maxHealth, int nextMaxHealthIncreaseLevel) {
        HealthFormat healthFormat = LevelHearts.getHealthFormat();
        CommandInfoOtherGet.send(sender, "player", player.getName(), "health", healthFormat.formatHealth(health, sender), "maxHealth", healthFormat.formatHealth(maxHealth, sender));
        if (nextMaxHealthIncreaseLevel > 0) {
            CommandInfoOtherIncrease.send(sender, "player", player.getName(), "maxHealthIncreaseLevel", nextMaxHealthIncreaseLevel);
        } else {
            CommandInfoOtherNoIncrease.send(sender, "player", player.getName());
        }
    }

    // health.change.command
    public void sendCommandHealthSet(PlayerHealthChangeEvent event, CommandSender sender, boolean self) {
        if (self)
            return;
        if (commandMessages)
            event.addMonitor(new PlayerHealthChangeEvent.Notifier(CommandHealthSet, sender, EventNotifier.MODE_ALWAYS));
        if (changeMessages)
            event.addMonitor(new PlayerHealthChangeEvent.Notifier(HealthChangeCommandSet, EventNotifier.MODE_ONLY_SUCCESS));
    }

    public void sendCommandHealthGive(PlayerHealthChangeEvent event, CommandSender sender, boolean self) {
        if (self)
            return;
        if (commandMessages)
            event.addMonitor(new PlayerHealthChangeEvent.Notifier(CommandHealthGive, sender, EventNotifier.MODE_ALWAYS));
        if (changeMessages)
            event.addMonitor(new PlayerHealthChangeEvent.Notifier(HealthChangeCommandGive, EventNotifier.MODE_ONLY_SUCCESS));
    }

    public void sendCommandHealthTake(PlayerHealthChangeEvent event, CommandSender sender, boolean self) {
        if (self)
            return;
        if (commandMessages)
            event.addMonitor(new PlayerHealthChangeEvent.Notifier(CommandHealthTake, sender, EventNotifier.MODE_ALWAYS));
        if (changeMessages)
            event.addMonitor(new PlayerHealthChangeEvent.Notifier(HealthChangeCommandTake, EventNotifier.MODE_ONLY_SUCCESS));
    }

    public void sendCommandHealthRestore(PlayerHealthChangeEvent event, CommandSender sender, boolean self) {
        if (self)
            return;
        if (commandMessages)
            event.addMonitor(new PlayerHealthChangeEvent.Notifier(CommandHealthRestore, sender, EventNotifier.MODE_ALWAYS));
        if (changeMessages)
            event.addMonitor(new PlayerHealthChangeEvent.Notifier(HealthChangeCommandRestore, EventNotifier.MODE_ONLY_SUCCESS));
    }

    // maxHealth.change.command
    public void sendCommandMaxHealthSet(PlayerMaxHealthChangeEvent event, CommandSender sender, boolean self) {
        if (self)
            return;
        if (commandMessages)
            event.addMonitor(new PlayerMaxHealthChangeEvent.Notifier(CommandMaxHealthSet, sender, EventNotifier.MODE_ALWAYS));
        if (changeMessages)
            event.addMonitor(new PlayerMaxHealthChangeEvent.Notifier(MaxHealthChangeCommandSet, EventNotifier.MODE_ONLY_SUCCESS));
    }

    public void sendCommandMaxHealthGive(PlayerMaxHealthChangeEvent event, CommandSender sender, boolean self) {
        if (self)
            return;
        if (commandMessages)
            event.addMonitor(new PlayerMaxHealthChangeEvent.Notifier(CommandMaxHealthGive, sender, EventNotifier.MODE_ALWAYS));
        if (changeMessages)
            event.addMonitor(new PlayerMaxHealthChangeEvent.Notifier(MaxHealthChangeCommandGive, EventNotifier.MODE_ONLY_SUCCESS));
    }

    public void sendCommandMaxHealthTake(PlayerMaxHealthChangeEvent event, CommandSender sender, boolean self) {
        if (self)
            return;
        if (commandMessages)
            event.addMonitor(new PlayerMaxHealthChangeEvent.Notifier(CommandMaxHealthTake, sender, EventNotifier.MODE_ALWAYS));
        if (changeMessages)
            event.addMonitor(new PlayerMaxHealthChangeEvent.Notifier(MaxHealthChangeCommandTake, EventNotifier.MODE_ONLY_SUCCESS));
    }

    public void sendCommandMaxHealthReset(PlayerMaxHealthChangeEvent event, CommandSender sender, boolean self) {
        if (self)
            return;
        if (commandMessages)
            event.addMonitor(new PlayerMaxHealthChangeEvent.Notifier(CommandMaxHealthReset, sender, EventNotifier.MODE_ALWAYS));
        if (changeMessages)
            event.addMonitor(new PlayerMaxHealthChangeEvent.Notifier(MaxHealthChangeCommandReset, EventNotifier.MODE_ONLY_SUCCESS));
    }

    public void sendCommandHelp(CommandSender sender, FormatOption syntax, ChatMessage description) {
        CommandHelpCommand.send(sender, "syntax", syntax, "description", description.format(sender));
    }

    public void sendPluginInformation(CommandSender sender) {
        PluginInfo.send(sender, "version", LevelHearts.getVersion(), "creator", "Zettelkasten");
        PluginWebsite.send(sender, "website", LevelHearts.getWebsite());
    }

    public void sendHealthCommandHelp(CommandSender sender) {
        if (!perm.hasCommandHealthEnabled(sender)) {
            CommandErrorNoPermission.send(sender);
            return;
        }
        sendCommandHelp(sender, CommandSyntaxHealthGeneral, HealthHelpGet);
        if (perm.hasCommandHealthSetOwn(sender)) {
            sendCommandHelp(sender, CommandSyntaxHealthSet, HealthHelpSet);
            sendCommandHelp(sender, CommandSyntaxHealthGive, HealthHelpGive);
            sendCommandHelp(sender, CommandSyntaxHealthTake, HealthHelpTake);
            sendCommandHelp(sender, CommandSyntaxHealthRestore, HealthHelpRestore);
        }
    }

    public void sendMaxHealthCommandHelp(CommandSender sender) {
        if (!perm.hasCommandMaxHealthEnabled(sender)) {
            CommandErrorNoPermission.send(sender);
            return;
        }
        sendCommandHelp(sender, CommandSyntaxMaxHealthGeneral, MaxHealthHelpGet);
        if (perm.hasCommandMaxHealthSetOwn(sender)) {
            sendCommandHelp(sender, CommandSyntaxMaxHealthSet, MaxHealthHelpSet);
            sendCommandHelp(sender, CommandSyntaxMaxHealthGive, MaxHealthHelpGive);
            sendCommandHelp(sender, CommandSyntaxMaxHealthTake, MaxHealthHelpTake);
            sendCommandHelp(sender, CommandSyntaxMaxHealthReset, MaxHealthHelpReset);
        }
    }

    public void sendPluginCommandHelp(CommandSender sender) {
        boolean noPerm = false;
        if (perm.hasCommandInfoReload(sender)) {
            sendCommandHelp(sender, CommandSyntaxPluginReload, PluginHelpReload);
            noPerm = true;
        }
        if (perm.hasCommandHealthEnabled(sender)) {
            sendCommandHelp(sender, CommandSyntaxHealthGeneral, HealthHelpGeneral);
            noPerm = true;
        }
        if (perm.hasCommandMaxHealthEnabled(sender)) {
            sendCommandHelp(sender, CommandSyntaxMaxHealthGeneral, MaxHealthHelpGeneral);
            noPerm = true;
        }
        if (!noPerm) {
            CommandErrorNoPermission.send(sender);
        }
    }

    public void sendPluginReload(CommandSender sender) {
        PluginReload.send(sender);
    }

    public void sendDisabledCommand(CommandSender sender) {
        CommandErrorDisabled.send(sender);
    }

    public void sendUpdateAvailable(CommandSender sender) {
        Updater updater = plugin.getUpdater();
        PluginUpdateAvailable.send(sender, "updateName", updater.getLatestName(), "updateGameVersion", updater.getLatestGameVersion(), "updateType", updater.getLatestType(), "updateLink", updater.getLatestFileLink());
    }
}
