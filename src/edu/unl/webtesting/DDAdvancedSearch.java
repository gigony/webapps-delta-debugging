package edu.unl.webtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class DDAdvancedSearch extends DD {
  Main testScript;
  String url;
  ArrayList<String> prefixEvents;
  ArrayList<String> events;
  Map<HashCode, Integer> cache = Maps.newHashMap();

  HashFunction hf = Hashing.md5();

  int totalTest = 0;
  int totalCachedTest = 0;
  int totalPass = 0;
  int totalCachedPass = 0;
  int totalFail = 0;
  int totalCachedFail = 0;
  int totalUnresolved = 0;
  int totalCachedUnresolved = 0;

  public DDAdvancedSearch(Main testScript, String url, ArrayList<String> prefixEvents, ArrayList<String> events) {
    this.testScript = testScript;
    this.url = url;
    this.prefixEvents = prefixEvents;
    this.events = events;
  }

  public int test(List config) {
    int outcome = DD.UNRESOLVED;

    System.out.println("test(" + config + ")...");
    HashCode hashCode = hash(config);
    boolean isCached = false;
    if (cache.containsKey(hashCode)) {
      isCached = true;
      outcome = cache.get(hashCode);

      totalCachedTest++;
      if (outcome == DD.PASS)
        totalCachedPass++;
      else if (outcome == DD.FAIL)
        totalCachedFail++;
      else if (outcome == DD.UNRESOLVED)
        totalCachedUnresolved++;
    } else {
      outcome = testScript.executeEvents(url, prefixEvents, events, (List<Integer>) config, false);
      cache.put(hashCode, outcome);
    }

    if (isCached)
      System.out.println("test(" + config + ") = " + pretty_outcome(outcome) + "[cached]");
    else
      System.out.println("test(" + config + ") = " + pretty_outcome(outcome));

    totalTest++;
    if (outcome == DD.PASS)
      totalPass++;
    else if (outcome == DD.FAIL)
      totalFail++;
    else if (outcome == DD.UNRESOLVED)
      totalUnresolved++;

    return outcome;
  }

  private HashCode hash(List config) {

    Hasher hc = hf.newHasher();
    for (Integer v : (List<Integer>) config) {
      hc.putInt(v);
    }
    return hc.hash();
  }

  public List ddmin() {
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

  public int getTotalCachedTest() {
    return totalCachedTest;
  }

  public int getTotalCachedPass() {
    return totalCachedPass;
  }

  public int getTotalCachedFail() {
    return totalCachedFail;
  }

  public int getTotalCachedUnresolved() {
    return totalCachedUnresolved;
  }

}
