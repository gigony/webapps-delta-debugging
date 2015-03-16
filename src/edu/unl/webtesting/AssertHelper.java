package edu.unl.webtesting;


public class AssertHelper {
  public void assertEquals(Object a, Object b) {
    System.out.println("assertEquals("+a+ ","+ b+")");
    if (!a.equals(b))
      throw new RuntimeException(String.format("assertion failure!(expected:[%s], actual:[%s]", a, b));
  }  
  public void assertNotEquals(Object a, Object b) {
    System.out.println("assertNotEquals("+a+ ","+ b+")");
    if (a.equals(b))
      throw new RuntimeException(String.format("assertion not equal failure!(not expected:[%s], actual:[%s]", a, b));
  }  
  public void assertTrue(boolean a) {
    if (!a)
      throw new RuntimeException(String.format("assertion true failure!(actual:[%s]", a));
  }
  public void assertFalse(boolean a) {
    assertTrue(!a);
  }
  public void print(Object a) {
    System.out.println(a);
  }
}
