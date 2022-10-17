package kr.ac.handong;

import org.junit.Assert;
import org.junit.Assume;

import org.junit.Test;

import java.util.Map;
import java.util.HashMap;
import org.apache.commons.collections4.trie.PatriciaTrie;

/**
 * Unit test
 */
public class ReproduceTest 
{
    /*
     * This is a test to reproduce a bug
     * It would be failed if the bug has been fixed.
     */
    @Test
    public void testNullTerminatedKey1() 
    {
        Map<String, Integer> map = new HashMap<>();
        map.put("x", 0);         // key of length 1
        map.put("x\u0000", 1);   // key of length 2
        map.put("x\u0000y", 2);  // key of length 3
        Assert.assertTrue(map.size() == 3);  // ok, 3 distinct keys
    
        PatriciaTrie<Integer> trie = new PatriciaTrie<>(map);
        Assert.assertFalse(trie.size() == 3);  // fail; actual=2
    }

    /*
     * This is a test to reproduce a bug
     * It would be failed if the bug has been fixed.
     */
    @Test
    public void testNullTerminatedKey2() 
    {
        PatriciaTrie<Integer> trie = new PatriciaTrie<>();
        trie.put("x", 0);
        Assert.assertTrue(trie.containsKey("x")); // ok
        trie.put("x\u0000", 1);
        // Assert.assertTrue(trie.containsKey("x")); // fail
        Assert.assertFalse(trie.containsKey("x")); 
    }
}
