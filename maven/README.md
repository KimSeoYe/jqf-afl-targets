# MAVEN

### Description
* incomplete xml tag에 대해 NullPointerException 발생 ([link](https://issues.apache.org/jira/browse/MNG-6375))
  * maven version : 3.5.2, 3.5.3
* malformed pom.xml에 대해 ModelBuilder에서 hang 발생 ([link](https://issues.apache.org/jira/browse/MNG-6374))
  * maven version :  3.5.2, 3.5.3
* unicode entity reference를 parsing할 때 Uncaught IllegalArgumentException 발생 ([link](https://issues.apache.org/jira/browse/MNG-6577))
  * maven version : 3.5.4, 3.6.0

### Fuzz target?
* JQF repo에 ModelReader 관련 fuzz target이 있었다
* ModelBuilder나 다른 report된 버그와 어떻게 관련이 있는지는 잘 모르겠지만, 일단 실행해 보았다
* 소스코드에서 찾아보려고 했으나 실패…

### Reproduce
* report된 input인 `<p` 와 비슷한 input (`<z`) 발생 ([link](https://issues.apache.org/jira/browse/MNG-6375))
* 4일 정도 실행해 본 결과 hang 발생 (31개) ([link](https://issues.apache.org/jira/browse/MNG-6374))
* Uncaught IllegalArgumentException([link](https://issues.apache.org/jira/browse/MNG-6577)) 은 아직 발견하지 못함 