# COLLECTIONS-714 ([link](https://issues.apache.org/jira/browse/COLLECTIONS-714))

### Description
Java에서 string은 null terminated되지 않으므로 “x”와 “x\u0000”은 다른데, PatricaTrie가 그것을 구분하지 못한다.

### 어떤 fuzz target을 사용해야 할까?
* Bug report에 있는 reproduce를 위한 코드들
    ```java
    // #1
    public void testNullTerminatedKey1() {
        Map<String, Integer> map = new HashMap<>();
        map.put("x", 0);         // key of length 1
        map.put("x\u0000", 1);   // key of length 2
        map.put("x\u0000y", 2);  // key of length 3
        Assert.assertEquals(3, map.size());  // ok, 3 distinct keys
        PatriciaTrie<Integer> trie = new PatriciaTrie<>(map);
        Assert.assertEquals(3, trie.size());  // fail; actual=2
    }

    // #2
    public void testNullTerminatedKey2() {
        PatriciaTrie<Integer> trie = new PatriciaTrie<>();
        trie.put("x", 0);
        Assert.assertTrue(trie.containsKey("x")); // ok
        trie.put("x\u0000", 1);
        Assert.assertTrue(trie.containsKey("x")); // fail 
    } // “x\u0000”을 put하면 기존에 있던 “x”가 사라짐
    ```
* JQF examples에 있는 fuzz target ([link](https://github.com/rohanpadhye/JQF/blob/master/examples/src/test/java/edu/berkeley/cs/jqf/examples/commons/PatriciaTrieTest.java))
    ```java
    @Fuzz
    public void testCopy(Map<String, Integer> map, String key) {
        assumeTrue(map.containsKey(key));
        // Create new trie with input `map`
        Trie trie = new PatriciaTrie(map);
        // The key should exist in the trie as well
        assertTrue(trie.containsKey(key));
    }
    ```
    => 3개의 method 중 가장 가능성이 있어보여서, AFL 사용을 위해 InputStream을 argument로 받도록 수정해 보았다.

* Cycle이 비정상적으로 오르는 문제 발생했었음

# COMPRESS-424 ([link](https://issues.apache.org/jira/browse/COMPRESS-424))

### Description
malformed data에 대해 ArrayIndexOutOfBoundsException 발생

### Fuzz target?
JQF에 example로 붙어있던 DecmpressTest.java를 JQF+AFL에 맞게 수정

### Reproduce
같은 종류의 오류 발생 (ArrayIndexOutOfBoundsException)

# Commons Lang ([link](https://issues.apache.org/jira/browse/LANG-1385))

### Description
* 특정 테스트 케이스에 대해 StingIndexOutOfBoundsException 발생 
* version : 4.3
* test case : `NumberUtils.createNumber("L")`
* expected result : 
    ```bash
    NumberFormatException
    actual result : 
    java.lang.StringIndexOutOfBoundsException: String index out of range: 0
    at java.lang.String.charAt(String.java:658)
    at org.apache.commons.lang3.math.NumberUtils.createNumber(NumberUtils.java:528)
    ```

### Fuzz target?
```java
@Fuzz
public void fuzzCreateNumber(@From(ArbitraryLengthStringGenerator.class) 
String input) throws NumberFormatException {
	  NumberUtils.createNumber(input);
}

```

### Reproduce

* crash를 일으킨 인풋들
  * l
  * L	<= report된 input과 정확히 일치
  * E@e.11
  *  EeE..
* 빠른 시간 내에 발견, 모두 report된 StringIndexOutOfBound 발생