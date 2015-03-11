package edu.unl.webtesting;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TestScript {
  StaticWebServer server;
  // GroovyScriptEngine gse;
  GroovyShell shell;
  Binding binding;
  AssertHelper assertHelper;
  boolean isWebDriver = false;
  
  private boolean doAutomation = false;  
//  private int minStart;
//  private int minEnd;

  String logFile;


  public TestScript() {
    URL[] urls = new URL[] {};
    ClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
    this.binding = new Binding();
    this.shell = new GroovyShell(loader, binding);
    this.assertHelper = new AssertHelper();
    binding.setVariable("assertHelper", this.assertHelper);
  }

  public void open(String url) {

    binding.setVariable("url", url);
    if (isWebDriver)
      shell.evaluate("driver.get(url);");
    else
      shell.evaluate("selenium.open(url);");
  }

  public void stop() {
    if (isWebDriver)
      shell.evaluate("driver.quit();");
    else
      shell.evaluate("selenium.stop();");
  }

  public void init() {
    init(false);
  }

  public void init(boolean isWebDriver) {
    this.isWebDriver = isWebDriver;
    try {
      binding.setVariable("isWebDriver", isWebDriver);
      String initScript = new String(Files.readAllBytes(FileSystems.getDefault().getPath("scripts", "import.groovy")));
      shell.evaluate(initScript);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void finish() {
    stop();
  }

  public void run(String testCasePath) throws IOException {
    run(testCasePath, false);
  }

  public void run(String testCasePath, boolean preservePreState) throws IOException {

    StaticWebServer.start("experiment/code");
    setLogFile(testCasePath);

    minimize(testCasePath, preservePreState);

  }

  private void setLogFile(String testCasePath) {
    logFile = testCasePath.replaceAll("\\.txt", "") + ".log";
  }

  private void minimize(String testCasePath) throws IOException {
    minimize(testCasePath, false);
  }

  private void minimize(String testCasePath, boolean preservePreState) throws IOException {
    List<String> lines = Files.readAllLines(FileSystems.getDefault().getPath(testCasePath), StandardCharsets.UTF_8);
    // get url from the first line
    String url = lines.get(0);
    info(String.format("# url: %s", url));

    lines.remove(0);
    // open("http://localhost:8080/Airport.html");

    // format event texts to be used in Groovy script engine.
    ArrayList<String> events = new ArrayList<String>();
    for (String line : lines) {
      line = line.replaceAll("\\(\"", "('''").replaceAll("\",", "''',").replaceAll("\" ,", "''' ,")
          .replaceAll(", \"", ", '''").replaceAll(",\"", ",'''").replaceAll("\"\\)", "''')")
          .replaceAll("assertEquals\\(", "assertHelper.assertEquals(").trim();
      if (!line.equals(""))
        events.add(line);
    }

    info(String.format("There are %d events", events.size()));

    // do binary search
//    minStart = 0;
//    minEnd = events.size() - 1;
//    binarySearch(url, events, 0, events.size() - 1, preservePreState);
//    info(String.format("##Minimun range of the test case:[%d, %d], size: %d/%d", minStart, minEnd, minEnd - minStart
//        + 1, events.size()));
    SearchResult result = binarySearch(url, events, 0, events.size() - 1, preservePreState, 0, events.size() - 1);
    info(String.format("##Minimun range of the test case:[%d, %d], size: %d/%d", result.minStart, result.minEnd, result.minEnd - result.minStart
        + 1, events.size()));

//    binarySearch(url, events, 0, events.size() - 1);
  }


//  private boolean binarySearch(String url, ArrayList<String> events, int start, int end, boolean preservePreState) {
//    if (start > end)
//      return false;
//
//    boolean exeResult = executeEvents(url, events, start, end, preservePreState);
//
//    
//    String response = readInput("Have a failure?(yes/no)", exeResult).toLowerCase();
//
//    if (response.equals("yes")) {
//      minStart = start;
//      minEnd = end;
//      if (start == end)
//        return true;
//      int mid = (start + end) / 2;
//      boolean result = binarySearch(url, events, start, mid, preservePreState);
//      if (!result) {
//        binarySearch(url, events, mid + 1, end, preservePreState);
//      }
//      return true;
//    } else {
//      return false;
//    }
//  }
  
//  private SearchResult binarySearch(String url, ArrayList<String> events, int start, int end, boolean preservePreState, int minStart, int minEnd) {
//    boolean exeResult = executeEvents(url, events, start, end, preservePreState);
//    
//    String response = readInput("Have a failure?(yes/no)", exeResult).toLowerCase();
//
//    if (response.equals("yes")) {
//      if (start == end)
//        return new SearchResult(true, start, end);
//      int mid = (start + end) / 2;
//      SearchResult result = binarySearch(url, events, start, mid, preservePreState, start, end);
//      if (!result.result) {
//        result = binarySearch(url, events, mid + 1, end, preservePreState, start, end);
//      }
//      return new SearchResult(true, result.minStart,result.minEnd);
//    } else {
//      return new SearchResult(false, minStart, minEnd);
//    }
//  }
  private SearchResult binarySearch(String url, ArrayList<String> events, int start, int end, boolean preservePreState, int minStart, int minEnd) {
    SearchResult result = new SearchResult(false, minStart, minEnd);    
    boolean exeResult = executeEvents(url, events, start, end, preservePreState);    
    String response = readInput("Have a failure?(yes/no)", exeResult).toLowerCase();

    if (response.equals("yes")) {
      if (start == end) {
        result.minStart = start;
        result.minEnd = end;
      } else {
        int mid = (start + end) / 2;
        result = binarySearch(url, events, start, mid, preservePreState, start, end);
        if (!result.succeed) {
          result = binarySearch(url, events, mid + 1, end, preservePreState, start, end);
        }
      }
    }
    return result;
  }
  
  
  private boolean executeEvents(String url, ArrayList<String> events, int start, int end, boolean preservePreState) {
    if (start > end) {
      return true;
    }
    info(String.format("# run events from index %d to index %d", start, end));
    open(url);

    // String scriptText = Joiner.on("\n").join(events.subList(start, end + 1));
    List<String> subList = events.subList(start, end + 1);

    boolean preStateMade = true;

    int index = 0;
    if (preservePreState) {
      try {
        if (start != 0) {
          info(String.format("  - making a state : executing event %d to %d", 0, start - 1));
          for (int i = 0; i < start; i++) {
            shell.evaluate(events.get(i));
            index++;
          }
        }
      } catch (Exception e) {
        info(String.format("\t@A failure occurs while making a state (event %d)", index));
        info("\t---");
        info(String.format("\t%s", e.getMessage()));
        info("\t---");
        preStateMade = false;
        return false;
      }
    }

    if (preStateMade) {
      index = start;
      try {
        for (String event : subList) {
          info(String.format("  - execute event %d : %s", index, event));
          shell.evaluate(event);
          index++;
        }
      } catch (Exception e) {
        info(String.format("\t@A failure occurs at event %d", index));
        info("\t---");
        info(String.format("\t%s", e.getMessage()));
        info("\t---");
        return false;
      }
    }

    return true;
  }

  private String readInput(String msg, boolean executionResult) {
    info(msg);
    if (doAutomation) {
      if (executionResult) {
        info("no");
        return "no";
      }
      else {
        info("yes");
        return "yes";
      }
    }
    Scanner scanner = new Scanner(System.in);
    String result = scanner.nextLine().trim();
    info(result);

    if (!result.toLowerCase().equals("yes") && !result.toLowerCase().equals("no"))
      return readInput(msg, executionResult);
    return result;
  }

  private void info(String msg) {
    System.out.println(msg);

    if (logFile == null)
      return;
    try {
      PrintWriter pw = new PrintWriter(new FileWriter(new File(logFile), true), true);
      pw.println(msg);
      pw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    TestScript testScript = new TestScript();
    try {
      testScript.init();
      testScript.setAutomate(true);
      
//      testScript.run("experiment/tests/CarRentalRecording.txt");
//    testScript.run("experiment/tests/SoastaStore.txt");

//       testScript.run("experiment/tests/BestCarsRecording.txt");
      // testScript.run("experiment/tests/BmatchesadminRecording.txt");
       testScript.run("experiment/tests/BpoolAdminRecording.txt");
//       testScript.run("experiment/tests/1.txt");
      testScript.finish();
    } catch (Exception e) {
      testScript.stop();
      e.printStackTrace();
    }

  }

  private void setAutomate(boolean b) {
    this.doAutomation = b;
  }
}


//private void binarySearch(String url, ArrayList<String> events, int start, int end) {
//  if (!replayReveals(url, events, start, end)) {
//    report(events,start,end);
//    return;
//  }
//  
//  int mid = (start + end) / 2;
//  
//  if(replayReveals(url,events,start,mid)) {
//    binarySearch(url,events,start,mid);
//  } else {
//    binarySearch(url,events,mid + 1, end);
//  }
//}
//
//private void report(ArrayList<String> events, int start, int end) {
//  info(String.format("##Minimun range of the test case:[%d, %d], size: %d/%d", start,end, end- start +1, events.size()));
//}
//
//private boolean replayReveals(String url, ArrayList<String> events, int start, int end) {
//  if (start>end)
//    return false;
//  executeEvents(url, events, start, end, false);
//  String response = readInput("Have a failure?(yes/no)").toLowerCase();
//  return response.equals("yes");
//}
