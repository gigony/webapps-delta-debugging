package edu.unl.webtesting;

public class SearchResult {
  boolean succeed;
  int minStart;
  int minEnd;
  public SearchResult(boolean result, int minStart, int minEnd) {
    this.succeed = result;
    this.minStart = minStart;
    this.minEnd = minEnd;
  }

}
