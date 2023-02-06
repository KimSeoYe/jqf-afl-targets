# Rhino#405~#407, #409~#410

### Description
* [mozilla/rhino#405](https://github.com/mozilla/rhino/issues/405): FAILED ASSERTION due to malformed destructuring syntax
* [mozilla/rhino#406](https://github.com/mozilla/rhino/issues/406): ClassCastException when compiling malformed destructuring expression
* [mozilla/rhino#407](https://github.com/mozilla/rhino/issues/407): java.lang.VerifyError in bytecode produced by CodeGen : closed
* [mozilla/rhino#409](https://github.com/mozilla/rhino/issues/409): ArrayIndexOutOfBoundsException when parsing '<!-'
* [mozilla/rhino#410](https://github.com/mozilla/rhino/issues/410): NullPointerException in BodyCodeGen

close된 #407 리포트를 제외하고는, “특정 인풋에 대해 어떤 오류가 발생한다” 이외의 내용이 없다.
#407에서는 긴 discussion이 오가고 패치가 된 것으로 보인다.

### Fuzz target?
* fuzz target이 example에 있어서, 일단 해당 target의 여러 메소드들을 모두 실행해 보기로 했다.
* JQF+AFL 대상이 아닌 메소드들도 String 인풋을 받게 되어 있어서, 간단하게 수정 후 실행했다.

### Reproduce
* mozilla/rhino#405 와 비슷한 케이스 몇 가지가 발생하였다.