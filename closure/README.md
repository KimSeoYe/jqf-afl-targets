# closure-compiler ([link](https://github.com/google/closure-compiler/issues/2842))

### Description
특정 인풋에 대해 java.lang.IllegalStateException 발생
* closure-compiler version : v20180204

### Fuzz target?
* JQF examples에 붙어있는 closure-compiler fuzz target 개발자가 보고한 이슈인 것으로 보아, 해당 타겟을 사용한 것 같다
* JQF+Zest, JQF+AFL 중 무엇을 사용했는지는 밝히지 않았고 단서도 충분하지 않아 잘 모르겠다. 일단 JQF+AFL 대상으로 만들어진 타겟을 실행시켜보고 있는 중

### Reproduce
* crash를 발생시킨 input : a|(') e i');
* java.lang.NullPointerException 발생
* Trophie에 등록된 다른 issue와 같은 유형의 exception ([link](https://github.com/google/closure-compiler/issues/2843))
  * input의 형태는 다른 것으로 보임


---
### 아직 살펴보지 못한 Trophie
* [google/closure-compiler#2843](https://github.com/google/closure-compiler/issues/2843): NullPointerException when using Arrow Functions in dead code
* [google/closure-compiler#3173](https://github.com/google/closure-compiler/issues/3173): Algorithmic complexity / performance issue on fuzzed input
* [google/closure-compiler#3220](https://github.com/google/closure-compiler/issues/3220): ExpressionDecomposer throws IllegalStateException: Object method calls

