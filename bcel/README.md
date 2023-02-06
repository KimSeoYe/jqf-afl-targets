# BCEL

### Description
* [BCEL-303](https://issues.apache.org/jira/browse/BCEL-303): AssertionViolatedException in Pass 3A Verification of invoke instructions
* [BCEL-307](https://issues.apache.org/jira/browse/BCEL-307): ClassFormatException thrown in Pass 3A verification 
* [BCEL-308](https://issues.apache.org/jira/browse/BCEL-308): NullPointerException in Verifier Pass 3A 
* [BCEL-309](https://issues.apache.org/jira/browse/BCEL-309): NegativeArraySizeException when Code attribute length is negative 
* [BCEL-310](https://issues.apache.org/jira/browse/BCEL-310): ArrayIndexOutOfBounds in Verifier Pass 3A
* [BCEL-311](https://issues.apache.org/jira/browse/BCEL-311): ClassCastException in Verifier Pass 2 
* [BCEL-312](https://issues.apache.org/jira/browse/BCEL-312): AssertionViolation: INTERNAL ERROR Please adapt StringRepresentation to deal with ConstantPackage in Verifier Pass 2
* [BCEL-313](https://issues.apache.org/jira/browse/BCEL-313): ClassFormatException: Invalid signature: Ljava/lang/String)V in Verifier Pass 3A

### Fuzz target?
* example에 붙어있는 것 사용

### Reproduce
* 303과 312 제외, 같은 오류들을 reproduce하는 데 성공함