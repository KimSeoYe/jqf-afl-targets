# JDK-8193444 : SimpleDateFormat ([link](https://bugs.openjdk.org/browse/JDK-8193444))

### Description
SimpleDateFormat#format이 256개 이상의 non-ASCII unicode characters를 포함한 format string을 사용해 formatting할 때 OOB(ArrayIndexOutOfBoundsException)을 발생시킨다

### Fuzz target?
* bug report에 있는 reproduce를 위한 코드
  ```java
    public class DateFormatterTest {
        public static void main(String[] args) {
            char nonAsciiChar = '\u263a'; // ☺
            String format = "";
            for (int i = 0; i < 257; i++) { format += nonAsciiChar; }

            DateFormat df = new SimpleDateFormat(format);
            df.format(new Date()); // throws ArrayIndexOutOfBoundsException
        }
    }
  ```
* reproduce를 위한 코드와 example에 등록된 비슷한 코드([link](https://github.com/rohanpadhye/JQF/blob/master/examples/src/test/java/edu/berkeley/cs/jqf/examples/jdk/DateFormatterTest.java#L53): JQF+Zest)를 활용해, JQF+AFL을 위한 fuzz target 작성
  ```java
  public void fuzzWithInputStream (InputStream input) throws IllegalArgumentException  {  
        try {
            String format = IOUtils.toString(input, "UTF-8");
            DateFormat df = new SimpleDateFormat(format);
            df.format(new Date());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  ```
* formatter라 그런지, IllegalArgumentException이 너무 쉽게 발생
* 기존 fuzz target도 IllegalArgumentException가 발생하면 무시하도록 처리해둠 => IllegalArgumentException은 무시하도록 수정하였음

### Reproduce
* 확인해 본 것은 모두 OOB가 맞았다.
* 256개 이상 연속하는 non-ascii char가 있는지 확인 (C 프로그램 작성: scripts/non-ascii-counter.c)
  * 256개 이상 연속하는 non-ascii char가 있는 것도 있긴 한데, 아닌 것도 많았다
  * non-ascii char가 없는 것도 있었음