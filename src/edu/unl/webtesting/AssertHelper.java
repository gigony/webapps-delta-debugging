package edu.unl.webtesting;


public class AssertHelper {
  public void assertEquals(Object a, Object b) {
    if (!a.equals(b))
      throw new RuntimeException(String.format("assertion failure!(expected:[%s], actual:[%s]", a, b));
    // Assert.assertEquals(a, b);
  }
}
