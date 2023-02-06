# JDK-8190511 ([link](https://bugs.openjdk.org/browse/JDK-8190511))

### Description
IHDR 섹션이 WIDE x HEIGHT가 integer.MAX_VALUE보다 작고 VM의 maximum array size limit보다는 크게 치수를 지정하는 잘못된 형식의 이미지 파일을 디코딩하려고 할 때 OOM 발생

### Fuzz target?
issue에 달려있는 reproduce 코드 기반으로 작성 (String을 InputStream으로 변환하는 과정을 제외하면, 단순히 ImageIO.read()를 호출하는 코드였다. 이 fuzz target을 사용했을 때 reproduce가 잘 될지는 모르겠지만 일단 시도해 보았다.)

### Reporduce
* java.lang.IllegalArgumentException: Invalid scanline stride 발생
  * 정상적인 png 파일에서는 발생하지 않는 것을 확인했지만, report된 이슈인 OOM은 아님
  * Trophie에 같은 프로젝트에 대한 다른 issue들이 있었는데, 혹시 그 중에 비슷한 에러가 있는지 확인해 봄
    * [PngReader throws IllegalArgumentException because ScanlineStride calculation logic is not proper](https://bugs.openjdk.org/browse/JDK-8191174) : 비슷하긴 한데 같은 이슈는 아닌 것 같다