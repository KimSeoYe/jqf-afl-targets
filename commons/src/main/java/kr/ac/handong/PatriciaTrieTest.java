/*
 * Copyright (c) 2019, The Regents of the University of California
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package kr.ac.handong;

import java.util.HashMap;
import java.util.Map;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Rohan Padhye
 */
@RunWith(JQF.class)
public class PatriciaTrieTest {

    @Fuzz
    public void testCopy(Map<String, Integer> map, String key) {
        assumeTrue(map.containsKey(key));
        // Create new trie with input `map`
        Trie trie = new PatriciaTrie(map);
        // The key should exist in the trie as well
        assertTrue(trie.containsKey(key));
    }

    @Fuzz
    public void testCopyWithInputStream(InputStream input) {
        Map<String, Integer> map = new HashMap<>();
        String[] keys;
        
        try {
            int split = input.read() % 5 + 1;
            keys = new String[split];

            int len = input.available();
            byte[] buffer = new byte[len];
            len /= split;

            for (int i = 0; i < split; i++) {
                if (i == split - 1) {
                    len = input.available();
                }

                input.read(buffer, 0, len) ;
                map.put(new String(buffer), i);
                Arrays.fill(buffer, (byte)0);
            }

            for (int i = 0; i < split; i++) {
                testCopy(map, keys[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }     
    }
}