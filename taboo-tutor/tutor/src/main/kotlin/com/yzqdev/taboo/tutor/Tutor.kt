package com.yzqdev.taboo.tutor

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info

object Tutor : Plugin() {

    override fun onEnable() {
        info("Successfully running ExamplePlugin!")
    }
}