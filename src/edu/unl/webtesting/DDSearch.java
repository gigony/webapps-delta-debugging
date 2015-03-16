package edu.unl.webtesting;

import java.util.ArrayList;
import java.util.List;

public class DDSearch extends DD {
  TestScript testScript;
  String url;
  ArrayList<String> prefixEvents;
  ArrayList<String> events;
  
  int totalTest = 0;
  int totalPass = 0;
  int totalFail = 0;
  int totalUnresolved = 0;
  
  public DDSearch(TestScript testScript, String url, ArrayList<String> prefixEvents, ArrayList<String> events) {
    this.testScript = testScript;
    this.url = url;
    this.prefixEvents = prefixEvents;
    this.events = events;
  }
  
  public int test(List config)
  {
      System.out.println("test(" + config + ")...");
      int outcome = testScript.executeEvents(url, prefixEvents, events, (List<Integer>)config, false);
      System.out.println("test(" + config + ") = " + 
                         pretty_outcome(outcome));
      
      totalTest++;
      if (outcome == DD.PASS)
        totalPass++;
      else if (outcome == DD.FAIL)
        totalFail++;
      else if (outcome == DD.UNRESOLVED) 
        totalUnresolved++;

      return outcome;
  }
  

  public  List ddmin() {
    int size = events.size();
    List<Integer> indexList = new ArrayList<Integer>();
    for (int i = 0; i < size; i++) {
      indexList.add(i);
    }
    return ddmin(indexList);
  }
  
  @Override
  public void print(String s) {
    testScript.info(s);
  }
  
  public int getTotalTest() {
    return totalTest;
  }
  public int getTotalPass() {
    return totalPass;
  }
  public int getTotalFail() {
    return totalFail;
  }
  public int getTotalUnresolved() {
    return totalUnresolved;
  }
  
}
