# JQF+AFL with MAVEN

다음은 [Tutorial: Fuzzing with AFL](https://github.com/rohanpadhye/jqf/wiki/Fuzzing-with-AFL)을 참고로 하여, 다양한 프로젝트에 대해 maven을 기반으로 JQF+AFL을 사용하기 위한 과정을 더 구체적으로 적은 것이다. 

이해를 위해 DateFormatter 예제를 사용하였으며, 대부분의 예제는 [JQF repository](https://github.com/rohanpadhye/JQF)에 제공되어 있는 것을 사용하였다.

## Maven project 준비하기
### Maven project 생성
* VSCode를 사용하여 쉽게 Maven project를 생성하고 설정할 수 있다
* Extension Pack for Java가 VSCode에 설치되어 있는 경우, `Java: Create Java Project` 커맨드를 사용하여 프로젝트를 생성한다
* 참고: [https://kogle.tistory.com/225](https://kogle.tistory.com/225)

### JQF+AFL에 맞춰 fuzz target 작성

* class는 `@RunWith(JQF.class)`으로 annotation 한다
* method는 `@Fuzz`로 annotation 한다
* method는 `public void` 타입이어야 한다
* method는 딱 하나의 `InputStream` 타입 파라미터를 가져야 한다

```java
package kr.ac.handong;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.Assume;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * @author Rohan Padhye
 */
@RunWith(JQF.class)
public class DateFormatterTest {
    @Fuzz
    public void fuzzWithInputStream (InputStream input) throws IllegalArgumentException  {  
        try {
            String format = IOUtils.toString(input, "UTF-8");
            DateFormat df = new SimpleDateFormat(format);
            df.format(new Date());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
### (optional) Reproducer 작성

JQF+AFL을 사용해 얻은 input이 실제로 어떤 crash/hang을 발생시키는지 확인해보기 위한 코드를 함께 작성하였다

```java
package kr.ac.handong;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;

public class Reproducer {

    public static void fuzzWithInputStream (InputStream input) throws IllegalArgumentException  {  
        try {
            String format = IOUtils.toString(input, "UTF-8");
            DateFormat df = new SimpleDateFormat(format);
            df.format(new Date());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IllegalArgumentException {
        if (args.length == 0) {
            System.err.println("usage: java Reproducer [input]");
            System.exit(1);
        }
        
        try {
            File file = new File(args[0]);
            InputStream in = new FileInputStream(file);
            fuzzWithInputStream(in);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

### seed 준비
initial seed corpus를 적절히 준비한다

### pom.xml
기본적인 pom.xml에서 수정 혹은 추가해야 하는 부분은 다음과 같다.

* JQF 사용을 위한 dependency 추가
    ```xml
    <dependency>
        <groupId>edu.berkeley.cs.jqf</groupId>
        <artifactId>jqf-fuzz</artifactId>
        <version>1.8</version>
    </dependency>
    ```
* JQF 사용을 위한 plugin 추가
    ```xml
    <plugin>
        <groupId>edu.berkeley.cs.jqf</groupId>
        <artifactId>jqf-maven-plugin</artifactId>
        <version>1.8</version>
    </plugin>
    ```
* target project의 dependency : buggy version으로 설정
* dependency copy를 위한 플러그인 추가
    ```xml
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <executions>
            <execution>
                <?m2e execute onConfiguration?>
                <id>copy-dependencies</id>
                <phase>compile</phase>
                <goals>
                    <goal>copy-dependencies</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    ```
* (optional) seed copy를 위한 플러그인 추가
    ```xml
    <plugin>
        <artifactId>maven-resources-plugin</artifactId>
        <version>3.0.2</version>
        <executions>
            <execution>
                <id>copy-seeds</id>
                <phase>compile</phase>
                <goals>
                    <goal>copy-resources</goal>
                </goals>
                <configuration>
                    <outputDirectory>${basedir}/target/seeds</outputDirectory>
                    <resources>
                        <resource>
                            <!-- path for the prepared seeds -->
                            <directory>src/main/seeds</directory>
                        </resource>
                    </resources>
                </configuration>
            </execution>
        </executions>
    </plugin>
    ```

### classpath 지정을 위한 스크립트 준비
JQF 사용을 위해 필요한 경로, maven을 사용해 프로젝트를 빌드한 target class의 경로와 copy된 dependency들을 classpath로 지정해 주기 위한 작업이다.

```bash
#!/bin/bash

JQF_DIR=~/git/jqf
PROJ_DIR=~/git/jqf-afl-targets/dateformat

# Create classpath
# [path for jqf-related-classes]:[path for target class]
cp="$JQF_DIR/fuzz/target/classes:$PROJ_DIR/target/classes"

# concatenate paths for dependency files
for jar in $PROJ_DIR/target/dependency/*.jar; do
  cp="$cp:$jar"
done

echo $cp
```

## JQF+AFL 실행

### 실행 예시
```bash
# [path-for-jqf]/bin/jqf-afl-fuzz -t 10000 -c $([path-for-classpath-script]) -i [path-for-seeds] -o [path-for-out-dir] -v [fully-qualified-class-name] [method-name]
$JQF_PATH/bin/jqf-afl-fuzz -t 10000 -c $(./scripts/classpath.sh) -i ./target/seeds/random -o out -v kr.ac.handong.DateFormatterTest fuzzWithInputStream 
```
* -t : time limit
* -c : configure classpath
* -i : directory path for initial seed corpus
* -o : directory path for output (result)
* -v : (optional) verbose; create a log file

### (optional) Reproducer 사용 예시
```bash
# java -cp $([path-for-classpath-script]) [fully-qualified-class-name] [path-for-input]
java -cp $(./script/classpath.sh) kr.ac.handong.Reproducer ./out/crashes/id:000000...
```