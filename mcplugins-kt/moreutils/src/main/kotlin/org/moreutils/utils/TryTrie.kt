package org.moreutils.utils

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/30 14:31
 * @Modified By:
 */
object TryTrie {
    @JvmStatic
    fun main(args: Array<String>) {
        val trie = Trie()
        trie.add("apple")
        println(trie.containsPrefix("app"))
    }
}