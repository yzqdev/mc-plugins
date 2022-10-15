package com.yzqdev.mcplug.tutor.demo

import org.bukkit.plugin.java.JavaPlugin

class Demo : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        println("好了咯")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}