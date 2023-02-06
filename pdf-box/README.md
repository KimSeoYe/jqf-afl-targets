# PDFBox

### Description
* [PDFBOX-4333](https://issues.apache.org/jira/browse/PDFBOX-4333): ClassCastException when loading PDF
* [PDFBOX-4338](https://issues.apache.org/jira/browse/PDFBOX-4338): ArrayIndexOutOfBoundsException in COSParser
* [PDFBOX-4339](https://issues.apache.org/jira/browse/PDFBOX-4339): NullPointerException in COSParser  

=> buggy versin: 2.0.12

### Fuzz target?
fuzzing에 사용한 코드가 다음과 같이 공개되어 있었다
```java
PDDocument.load(inputStream)
```

### Reproduce
3가지 종류의 오류 모두 발견