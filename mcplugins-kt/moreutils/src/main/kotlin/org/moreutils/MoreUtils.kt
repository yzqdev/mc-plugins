package org.moreutils

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.moreutils.commands.AmazeEgg
import org.moreutils.commands.Hp
import org.moreutils.commands.LoadConf
import org.moreutils.eventListeners.EventListener
import org.moreutils.utils.Config
import java.io.File
import java.util.*

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/29 19:45
 * @Modified By:
 */
class MoreUtils  : JavaPlugin() {

    override fun onEnable() {
        instance = this
        Config.loadConfig()
        registerCommands()
        val version = Properties()
        try {
            version.load(getResource("version.properties"))
        } catch (ex: Exception) {
            logger.info("载入失败")
        }
        logger.info("Lores " + description.version + " (Build " + version.getProperty("Build") + ") is enabled!")
        EventListener(this)
    }

    private fun registerCommands() {
        getCommand("hp")?.setExecutor(Hp(this))
        getCommand("hp")?.tabCompleter = Hp(this)
        getCommand("mu")?.setExecutor(LoadConf(this))
        getCommand("mu")?.tabCompleter = LoadConf(this)
        getCommand("toegg")?.setExecutor(AmazeEgg(this))
        getCommand("toegg")?.tabCompleter = AmazeEgg(this)
    }

    private fun loadConfigurationFiles() {}
    fun createConfig() {
        if (!File(dataFolder.toString() + File.separator + "config.yml").exists()) {
            saveDefaultConfig()
            saveResource("test/test.yml", true)
            say(ChatColor.YELLOW.toString() + "无法找到config.yml,正在创建")
            /**
             * 读取
             * this.getDataFolder()方法返回插件配置文件夹的File对象
             * File biuConfigFile = new File(this.getDataFolder(), "biu.yml");
             * 对于在插件配置文件夹创建一个新的文件夹存放配置文件
             * File biuConfigFile = new File(this.getDataFolder(), "test/biu.yml");
             * FileConfiguration biuConfig = YamlConfiguration.loadConfiguration(biuConfigFile);
             * biuConfigFile.get.......
             * biuConfigFile.set....... //set完了记得保存!
             * //保存
             * biuConfig.save(biuConfigFile);
             */
        }
        try {
            reloadConfig()
            say(ChatColor.YELLOW.toString() + "成功加载config" + dataFolder)
        } catch (e: Exception) {
            e.printStackTrace()
            server.pluginManager.disablePlugin(this)
            say(ChatColor.RED.toString() + "无法读取config")
        }
    }

    fun say(s: String?) {
        val sender = Bukkit.getConsoleSender()
        sender.sendMessage(s)
    }



    companion object {
        private var instance: MoreUtils? = null
            get() {
                if (field == null) {
                    field =  MoreUtils()
                }
                return field
            }
        fun get():  MoreUtils{
            //细心的小伙伴肯定发现了，这里不用getInstance作为为方法名，是因为在伴生对象声明时，内部已有getInstance方法，所以只能取其他名字
            return instance!!
        }
    }

}