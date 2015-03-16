package edu.unl.webtesting;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.thoughtworks.selenium.SeleniumException;

public class TestScript {
  StaticWebServer server;
  // GroovyScriptEngine gse;
  GroovyShell shell;
  Binding binding;
  AssertHelper assertHelper;
  boolean isWebDriver = false;

  private boolean doAutomation = false;
  String logFile;
  private String clearDBUrl = "";

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
      shell.evaluate("selenium.deleteAllVisibleCookies();selenium.open(url);selenium.waitForPageToLoad(\"30000\");");
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
    run(testCasePath, "", false, true);
  }

  public void run(String testCasePath, boolean preservePreState) throws IOException {

    run(testCasePath, "", preservePreState, true);

  }

  private void run(String testCasePath, String clearRequest, boolean preservePreState, boolean needStaticServer)
      throws IOException {
    this.clearDBUrl = clearRequest;
    if (needStaticServer)
      StaticWebServer.start("experiment/code");

    setLogFile(testCasePath);
    minimize(testCasePath, preservePreState);
  }

  private void execute(String testCasePath, String clearRequest, boolean needStaticServer) throws IOException {
    this.clearDBUrl = clearRequest;
    if (needStaticServer)
      StaticWebServer.start("experiment/code");

    setLogFile(testCasePath);
    executeScript(testCasePath);
  }

  private void executeScript(String testCasePath) throws IOException {
    List<String> lines = Files.readAllLines(FileSystems.getDefault().getPath(testCasePath), StandardCharsets.UTF_8);
    // get url from the first line
    String url = lines.get(0);
    info(String.format("# url: %s", url));

    lines.remove(0);

    // format event texts to be used in Groovy script engine.
    ArrayList<String> events = new ArrayList<String>();
    for (String line : lines) {
      line = line.replaceAll("\\(\"", "('''").replaceAll("\",", "''',").replaceAll("\" ,", "''' ,")
          .replaceAll(", \"", ", '''").replaceAll(",\"", ",'''").replaceAll("\"\\)", "''')")
          .replaceAll("assertEquals\\(", "assertHelper.assertEquals(")
          .replaceAll("assertNotEquals\\(", "assertHelper.assertNotEquals(")
          .replaceAll("assertTrue\\(", "assertHelper.assertTrue(")
          .replaceAll("assertFalse\\(", "assertHelper.assertFalse(").trim();
      if (!line.equals(""))
        events.add(line);
    }

    ArrayList<String> prefixEvents = extractPrefixEvents(events);

    info(String.format("There are %d events (with %d prefix events)", events.size(), prefixEvents.size()));

    int size = events.size();
    List<Integer> indexList = new ArrayList<Integer>();
    for (int i = 0; i < size; i++) {
      indexList.add(i);
    }
    long time = System.currentTimeMillis();
    info("# execution");
    executeEvents(url, prefixEvents, events, indexList, true);
    info(String.format("Elapsed time: %s seconds", String.valueOf((System.currentTimeMillis() - time) / 1000.0)));

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

    // format event texts to be used in Groovy script engine.
    ArrayList<String> events = new ArrayList<String>();
    for (String line : lines) {
      line = line.replaceAll("\\(\"", "('''").replaceAll("\",", "''',").replaceAll("\" ,", "''' ,")
          .replaceAll(", \"", ", '''").replaceAll(",\"", ",'''").replaceAll("\"\\)", "''')")
          .replaceAll("assertEquals\\(", "assertHelper.assertEquals(")
          .replaceAll("assertNotEquals\\(", "assertHelper.assertNotEquals(")
          .replaceAll("assertTrue\\(", "assertHelper.assertTrue(")
          .replaceAll("assertFalse\\(", "assertHelper.assertFalse(").trim();
      if (!line.equals(""))
        events.add(line);
    }

    ArrayList<String> prefixEvents = extractPrefixEvents(events);

    info(String.format("There are %d events (with %d prefix events)", events.size(), prefixEvents.size()));

    // do binary search
    // SearchResult result = binarySearch(url, prefixEvents, events, 0, events.size() - 1, preservePreState, 0,
    // events.size() - 1);
    // info(String.format("##Minimun range of the test case:[%d, %d], size: %d/%d", result.minStart, result.minEnd,
    // result.minEnd - result.minStart + 1, events.size()));
    deltaDebugging(url, prefixEvents, events);
  }

  private void deltaDebugging(String url, ArrayList<String> prefixEvents, ArrayList<String> events) {

    long time = System.currentTimeMillis();
    DDAdvancedSearch ddSearch = new DDAdvancedSearch(this, url, prefixEvents, events);
    List<Integer> minTest = (List<Integer>) ddSearch.ddmin();
    info(String
        .format("Delta Debugging time: %s seconds", String.valueOf((System.currentTimeMillis() - time) / 1000.0)));

    info("### Validation ###");

    int size = events.size();
    List<Integer> indexList = new ArrayList<Integer>();
    for (int i = 0; i < size; i++) {
      indexList.add(i);
    }
    time = System.currentTimeMillis();
    info("# original");
    executeEvents(url, prefixEvents, events, indexList, true);
    info(String.format("Elapsed time: %s seconds", String.valueOf((System.currentTimeMillis() - time) / 1000.0)));

    time = System.currentTimeMillis();
    info("# reduced");
    executeEvents(url, prefixEvents, events, minTest, true);
    info(String.format("Elapsed time: %s seconds", String.valueOf((System.currentTimeMillis() - time) / 1000.0)));
    info(String.format("Total Test: %d[%d] (Pass: %d[%d], Fail: %d[%d], Unresolved: %d[%d])", ddSearch.getTotalTest(),
        ddSearch.getTotalCachedTest(), ddSearch.getTotalPass(), ddSearch.getTotalCachedPass(), ddSearch.getTotalFail(),
        ddSearch.getTotalCachedFail(), ddSearch.getTotalUnresolved(), ddSearch.getTotalCachedUnresolved()));
    info(String.format("Reduced events: %s (size: %d/%d)", minTest, minTest.size(), events.size()));
  }

  private ArrayList<String> extractPrefixEvents(ArrayList<String> events) {
    ArrayList<String> prefixEvents = new ArrayList<String>();
    while (!events.isEmpty()) {
      String firstLine = events.get(0);
      if (firstLine.trim().startsWith("@")) {
        prefixEvents.add(events.remove(0).trim().substring(1));
      } else {
        break;
      }
    }
    return prefixEvents;
  }

  // private SearchResult binarySearch(String url, ArrayList<String> prefixEvents, ArrayList<String> events, int start,
  // int end, boolean preservePreState, int minStart, int minEnd) {
  // SearchResult result = new SearchResult(false, minStart, minEnd);
  // boolean exeResult = executeEvents(url, prefixEvents, events, start, end, preservePreState);
  // String response = readInput("Have a failure?(yes/no)", exeResult).toLowerCase();
  //
  // if (response.equals("yes")) {
  // if (start == end) {
  // result.minStart = start;
  // result.minEnd = end;
  // } else {
  // int mid = (start + end) / 2;
  // result = binarySearch(url, prefixEvents,events, start, mid, preservePreState, start, end);
  // if (!result.succeed) {
  // result = binarySearch(url, prefixEvents, events, mid + 1, end, preservePreState, start, end);
  // }
  // }
  // result.succeed = true;
  // }
  // return result;
  // }
  private SearchResult binarySearch(String url, ArrayList<String> prefixEvents, ArrayList<String> events, int start,
      int end, boolean preservePreState, int minStart, int minEnd) {
    boolean exeResult = executeEvents(url, prefixEvents, events, start, end, preservePreState, false);

    String response = readInput("Have a failure?(yes/no)", exeResult).toLowerCase();

    if (response.equals("yes")) {
      if (start == end)
        return new SearchResult(true, start, end);
      int mid = (start + end) / 2;
      SearchResult result = binarySearch(url, prefixEvents, events, start, mid, preservePreState, start, end);
      if (!result.succeed) {
        result = binarySearch(url, prefixEvents, events, mid + 1, end, preservePreState, start, end);
      }
      return new SearchResult(true, result.minStart, result.minEnd);
    } else {
      return new SearchResult(false, minStart, minEnd);
    }
  }

  public boolean executeEvents(String url, ArrayList<String> prefixEvents, ArrayList<String> events, int start,
      int end, boolean preservePreState, boolean skipAssertion) {
    if (start > end) {
      return true;
    }
    info(String.format("# run events from index %d to index %d", start, end));
    requestHTTP(this.clearDBUrl);
    open(url);

    // String scriptText = Joiner.on("\n").join(events.subList(start, end + 1));
    List<String> subList = events.subList(start, end + 1);

    boolean preStateMade = true;

    // execute prefix events
    try {
      info(String.format("  - executing prefix events"));
      for (String event : prefixEvents) {
        shell.evaluate(event);
      }
    } catch (Exception e) {
      info(String.format("\t###### A failure occurs when executing prefix events!!!"));
      info("\t---");
      info(String.format("\t%s", e.getMessage()));
      info("\t---");
      return false;
    }

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
          try {
            shell.evaluate(event);
          } catch (SeleniumException e) {
            info(String.format("\t@Selenium cannot find an element at event %d [%s]. Skipped.", index, e.getClass()
                .getName()));
            info("\t---");
            info(String.format("\t%s", e.getMessage()));
            info("\t---");
          } catch (Exception e) {
            info(String.format("\t@A failure occurs at event %d [%s]", index, e.getClass().getName()));
            info("\t---");
            info(String.format("\t%s", e.getMessage()));
            info("\t---");
            if (!skipAssertion) {
              throw e;
            }
          }
          index++;
        }
      } catch (Exception e) {
        info(String.format("\t@A failure occurs at event %d [%s]", index, e.getClass().getName()));
        info("\t---");
        info(String.format("\t%s", e.getMessage()));
        info("\t---");
        return false;
      }
    }

    return true;
  }

  public int executeEvents(String url, ArrayList<String> prefixEvents, ArrayList<String> events, List<Integer> subList,
      boolean skipAssertion) {

    requestHTTP(this.clearDBUrl);
    open(url);

    // execute prefix events
    try {
      info(String.format("  - executing prefix events"));
      for (String event : prefixEvents) {
        shell.evaluate(event);
      }
    } catch (Exception e) {
      info(String.format("\t###### A failure occurs when executing prefix events!!!"));
      info("\t---");
      info(String.format("\t%s", e.getMessage()));
      info("\t---");
      return DD.FAIL;
    }

    for (Integer index : subList) {
      info(String.format("  - execute event %d : %s", index, events.get(index)));
      try {
        shell.evaluate(events.get(index));
      } catch (SeleniumException e) {
        info(String.format("\t@Selenium cannot find an element at event %d [%s]. Skipped.", index, e.getClass()
            .getName()));
        info("\t---");
        info(String.format("\t%s", e.getMessage()));
        info("\t---");
        if (!skipAssertion)
          return DD.UNRESOLVED;
      } catch (Exception e) {
        info(String.format("\t@A failure occurs at event %d [%s]", index, e.getClass().getName()));
        info("\t---");
        info(String.format("\t%s", e.getMessage()));
        info("\t---");
        if (!skipAssertion)
          return DD.FAIL;
      }
    }

    return DD.PASS;
  }

  private String readInput(String msg, boolean executionResult) {
    info(msg);
    if (doAutomation) {
      if (executionResult) {
        info("no");
        return "no";
      } else {
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

  public void info(String msg) {
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

  public void requestHTTP(String urlAddr) {
    if (urlAddr == null || urlAddr.equals(""))
      return;

    // open(urlAddr);

    try {
      URL url = new URL(urlAddr);
      BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
      String strTemp = "";
      while (null != (strTemp = br.readLine())) {
        info(strTemp);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) {
    TestScript testScript = new TestScript();
    try {
      testScript.init();
      testScript.setAutomate(true);

      // testScript.run("experiment/tests/SoastaStore.txt");
      // testScript.run("experiment/tests/BestCarsRecording.txt");
      // testScript.run("experiment/tests/BmatchesadminRecording.txt");
      // testScript.run("experiment/tests/BpoolAdminRecording.txt");
      // testScript.run("experiment/tests/1.txt");
      // testScript.run("experiment/tests/Failure1.txt", "http://faqforge.dev/clear.php", false);

      // testScript.run("experiment/tests/CanadaLong.txt");
      // testScript.run("experiment/tests/CatLong.txt");
      // testScript.run("experiment/tests/TicTacToeLong.txt"); // doesn't work well
      // testScript.run("experiment/tests/OnlineShoppingLong.txt");
      // testScript.run("experiment/tests/18-Appointment.txt");
      // testScript.run("experiment/tests/AccountLong.txt");
      // testScript.run("experiment/tests/AgeLong.txt");
      // testScript.run("experiment/tests/TravellersLong.txt");
      // testScript.run("experiment/tests/Flower.txt");
      // testScript.run("experiment/tests/ReorderLong.txt");
      // testScript.run("experiment/tests/Cook.txt");
      // testScript.run("experiment/tests/MyCart.txt");
      // testScript.run("experiment/tests/TestNumber.txt");
      // testScript.run("experiment/tests/DentistLong.txt");
      // testScript.run("experiment/tests/Financial.txt");
      // testScript.run("experiment/tests/CarRental.txt");
      // testScript.run("experiment/tests/order.txt");
      // testScript.run("experiment/tests/InsuranceLong.txt");
      // testScript.run("experiment/tests/Birthday.txt");
      // testScript.run("experiment/tests/Passport.txt");
      // testScript.run("experiment/tests/WaitingList.txt");
      // testScript.run("experiment/tests/Phone.txt");
      // testScript.run("experiment/tests/Pool.txt");
      // testScript.run("experiment/tests/23.txt");
      // testScript.run("experiment/tests/24.txt");
      // testScript.run("experiment/tests/25.txt");
      // testScript.run("experiment/tests/26.txt");
      // testScript.run("experiment/tests/27.txt");
      // testScript.run("experiment/tests/28.txt");
      // testScript.run("experiment/tests/29.txt");
      // testScript.run("experiment/tests/30.txt");

      // testScript.run("experiment/tests/faqforge1.txt", "http://faqforge.dev.10.211.128.220.xip.io/clear.php", false,
      // false);
      // testScript.run("experiment/tests/faqforge2.txt", "http://faqforge.dev.10.211.128.220.xip.io/clear.php", false,
      // false);
      // testScript.run("experiment/tests/faqforge3.txt", "http://faqforge.dev.10.211.128.220.xip.io/clear.php", false,
      // false);
      // testScript.run("experiment/tests/faqforge4.txt", "http://faqforge.dev.10.211.128.220.xip.io/clear.php", false,
      // false);
      // testScript.execute("experiment/tests/faqforge4test.txt", "http://faqforge.dev.10.211.128.220.xip.io/clear.php",
      // false);
      // testScript.run("experiment/tests/faqforge5.txt", "http://faqforge.dev.10.211.128.220.xip.io/clear.php", false,
      // false);
      // testScript.execute("experiment/tests/faqforge5.txt", "http://faqforge.dev.10.211.128.220.xip.io/clear.php",
      // false);
      // testScript.run("experiment/tests/faqforge6.txt", "http://faqforge.dev.10.211.128.220.xip.io/clear.php", false,
      // false);
      // testScript.execute("experiment/tests/faqforge6.txt", "http://faqforge.dev.10.211.128.220.xip.io/clear.php",
      // false);

      // testScript.run("experiment/tests/schoolmate1.txt", "http://schoolmate.dev.10.211.128.220.xip.io/clear.php",
      // false, false);
      // testScript.run("experiment/tests/schoolmate2.txt", "http://schoolmate.dev.10.211.128.220.xip.io/clear.php",
      // false, false);
      // testScript.run("experiment/tests/schoolmate3.txt", "http://schoolmate.dev.10.211.128.220.xip.io/clear.php",
      // false, false);
      // testScript.run("experiment/tests/schoolmate4.txt", "http://schoolmate.dev.10.211.128.220.xip.io/clear.php",
      // false, false);
      // testScript.execute("experiment/tests/schoolmate4.txt", "http://schoolmate.dev.10.211.128.220.xip.io/clear.php",
      // false);
      // testScript.run("experiment/tests/schoolmate5.txt", "http://schoolmate.dev.10.211.128.220.xip.io/clear.php",
      // false, false);
      // testScript.execute("experiment/tests/schoolmate5.txt", "http://schoolmate.dev.10.211.128.220.xip.io/clear.php",
      // false);
      //
      // testScript.execute("experiment/tests/timeclock1.txt", "http://timeclock.dev.10.211.128.220.xip.io/clear.php",
      // false);
      // testScript.run("experiment/tests/timeclock1.txt", "http://timeclock.dev.10.211.128.220.xip.io/clear.php",
      // false, false);
      // testScript.execute("experiment/tests/timeclock2.txt", "http://timeclock.dev.10.211.128.220.xip.io/clear.php",
      // false);
      // testScript.run("experiment/tests/timeclock2.txt", "http://timeclock.dev.10.211.128.220.xip.io/clear.php",
      // false, false);
      // testScript.execute("experiment/tests/timeclock3.txt", "http://timeclock.dev.10.211.128.220.xip.io/clear.php",
      // false);
      // testScript.run("experiment/tests/timeclock3.txt", "http://timeclock.dev.10.211.128.220.xip.io/clear.php",
      // false,
      // false);

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
