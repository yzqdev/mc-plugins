package org.moreutils.utils;

/**
 * @Author: Yangzhengqian
 * @Description:
 * @Date:Created time 2019/12/30 14:31
 * @Modified By:
 */
public class TryTrie {
    public static void main(String[] args) {
        Trie trie=new Trie();
        trie.add("apple");
        System.out.println(trie.containsPrefix("app"));
    }
}
