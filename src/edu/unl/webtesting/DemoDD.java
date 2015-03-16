package edu.unl.webtesting;

import java.util.LinkedList;
import java.util.List;

public class DemoDD extends DD {

  public static final Integer ONE   = new Integer(1);
  public static final Integer TWO   = new Integer(2);
  public static final Integer THREE = new Integer(3);
  public static final Integer FOUR  = new Integer(4);

  public int _test(List config)
  {
      if (config.contains(ONE) && config.contains(THREE))
          return FAIL;
      return PASS;
  }

  public int test(List config)
  {
      System.out.println("test(" + config + ")...");
      int outcome = _test(config);

      System.out.println("test(" + config + ") = " + 
                         pretty_outcome(outcome));

      return outcome;
  }

  public static void main(String[] args) {
      LinkedList config = new LinkedList();

      config.add(ONE);
      config.add(TWO);
      config.add(THREE);
      config.add(FOUR);
      System.out.println(config);

      DD mydd = new DemoDD();

      System.out.println("Running ddmin");    
      List c = mydd.ddmin(config);

      System.out.println("");
      System.out.println("Running ddiso");    
      List ret = mydd.ddiso(new LinkedList(), config);
  }
 }
