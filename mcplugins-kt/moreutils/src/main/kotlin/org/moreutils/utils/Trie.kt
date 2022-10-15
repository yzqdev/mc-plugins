package org.moreutils.utils

import java.util.*

class Trie {
    private val root: Node
    private var size = 0

    private class Node() {
        var isWord = false
        var next: MutableMap<Char, Node>

        init {
            next = TreeMap()
        }

        constructor(isWord: Boolean) : this() {
            this.isWord = isWord
        }
    }

    init {
        root = Node()
    }

    fun size(): Int {
        return size
    }

    val isEmpty: Boolean
        get() = size == 0

    /**
     * 插入操作
     *
     * @param word 单词
     */
    fun add(word: String) {
        var current: Node? = root
        val cs = word.toCharArray()
        for (c in cs) {
            val next = current!!.next[c]
            if (next == null) {
                current.next[c] = Node()
            }
            current = current.next[c]
        }
        //如果当前的node已经是一个word，则不需要添加
        if (!current!!.isWord) {
            size++
            current.isWord = true
        }
    }

    /**
     * 是否包含某个单词
     *
     * @param word 单词
     * @return 存在返回true，反之false
     */
    operator fun contains(word: String): Boolean {
        var current = root
        for (i in 0 until word.length) {
            val c = word[i]
            val node = current.next[c] ?: return false
            current = node
        }
        //如果只存在 panda这个词，查询 pan，虽然有这3个字母，但是并不存在该单词
        return current.isWord
    }

    /**
     * Trie是否包含某个前缀
     *
     * @param prefix 前缀
     * @return
     */
    fun containsPrefix(prefix: String): Boolean {
        var current = root
        for (i in 0 until prefix.length) {
            val c = prefix[i]
            val node = current.next[c] ?: return false
            current = node
        }
        return true
    }
    /*
     * 1，如果单词是另一个单词的前缀，只需要把该word的最后一个节点的isWord的改成false
     * 2，如果单词的所有字母的都没有多个分支，删除整个单词
     * 3，如果单词的除了最后一个字母，其他的字母有多个分支，
     */
    /**
     * 删除操作
     *
     * @param word
     * @return
     */
    fun remove(word: String): Boolean {
        var multiChildNode: Node? = null
        var multiChildNodeIndex = -1
        var current = root
        for (i in 0 until word.length) {
            val child = current.next[word[i]] ?: return false
            //如果Trie中没有这个单词
            //当前节点的子节点大于1个
            if (child.next.size > 1) {
                multiChildNodeIndex = i
                multiChildNode = child
            }
            current = child
        }
        //如果单词后面还有子节点
        if (current.next.size > 0) {
            if (current.isWord) {
                current.isWord = false
                size--
                return true
            }
            //不存在该单词，该单词只是前缀
            return false
        }
        //如果单词的所有字母的都没有多个分支，删除整个单词
        if (multiChildNodeIndex == -1) {
            root.next.remove(word[0])
            size--
            return true
        }
        //如果单词的除了最后一个字母，其他的字母有分支
        if (multiChildNodeIndex != word.length - 1) {
            multiChildNode!!.next.remove(word[multiChildNodeIndex + 1])
            size--
            return true
        }
        return false
    }
}