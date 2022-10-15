package org.moreutils.utils

import org.bukkit.Bukkit
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.moreutils.MoreUtils
import java.io.File
import java.io.IOException

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/30 1:01
 * @Modified By:
 */
object Config {
    var config: YamlConfiguration? = null
        private set
    var FILE: File = File(MoreUtils.get().dataFolder.path + File.separator + "config.yml")
    fun createConfig() {
        configFile()
    }

    fun createDefaultConfig() {
        if (FILE.exists()) {
            FILE.delete()
        }
        configFile()
    }

    fun configFile() {
        Bukkit.getConsoleSender().sendMessage("[" + MoreUtils.get().getName() + "] §cCreate Config.yml")
        config = YamlConfiguration()
        config!!["player-max-health"] = 20
        config!!["to-egg-chance"] = 80
        try {
            config!!.save(FILE)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadConfig() {
        if (!FILE.exists()) {
            createConfig()
            return
        }
        Bukkit.getConsoleSender().sendMessage("[" + MoreUtils.get().getName() + "] §aFind config.yml")
        config = YamlConfiguration()
        try {
            config!!.load(FILE)
        } catch (e: IOException) {
            e.printStackTrace()
            Bukkit.getConsoleSender().sendMessage("[" + MoreUtils.get().getName() + "] §c读取config时发生错误")
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
            Bukkit.getConsoleSender().sendMessage("[" + MoreUtils.get().getName() + "] §c读取config时发生错误")
        }
    }

    fun getConfig(loc: String?): String? {
        var raw = config!!.getString(loc!!)
        if (raw.isNullOrEmpty()) {
            createConfig()
            raw = config!!.getString(loc)
            return raw
        }
        raw = raw.replace("&", "§")
        return raw
    }

    fun setConfig(label: String?, userVar: String?) {
        config = YamlConfiguration()
        config!![label!!] = userVar
        try {
            config!!.save(FILE)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}