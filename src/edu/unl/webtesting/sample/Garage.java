package edu.unl.webtesting.sample;

public class Garage {
  // URL[] urls = new URL[] {};
  // ClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
  //
  //
  // GroovyScriptEngine gse;
  // try {
  // gse = new GroovyScriptEngine(urls, loader); //, Thread.currentThread().getClass().getClassLoader());
  //
  //
  // // Binding binding = new Binding();
  // // binding.setVariable("input", "world");
  // // gse.run("hello.groovy", binding);
  // // System.out.println(binding.getVariable("output"));
  //
  // } catch (IOException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // } catch (ResourceException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // } catch (groovy.util.ScriptException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  //

  // String[] roots = new String[] { "scripts" };
  // GroovyScriptEngine gse;
  // try {
  // gse = new GroovyScriptEngine(roots); //, Thread.currentThread().getClass().getClassLoader());
  // Binding binding = new Binding();
  // binding.setVariable("input", "world");
  // gse.run("hello.groovy", binding);
  // System.out.println(binding.getVariable("output"));
  //
  // } catch (IOException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // } catch (ResourceException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // } catch (groovy.util.ScriptException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }

  // ScriptEngineManager factory = new ScriptEngineManager();
  // ScriptEngine engine = factory.getEngineByName("groovy");
  // String fact = "def factorial(n) { n == 1 ? 1 : n * factorial(n - 1) }";
  // try {
  // engine.eval(fact);
  // Invocable inv = (Invocable) engine;
  // Object[] params = { new Integer(5) };
  // Object result = inv.invokeFunction("factorial", params);
  // System.out.println(result);
  //
  // } catch (ScriptException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // } catch (NoSuchMethodException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }



//int mid = (start + end) / 2;
//
//boolean result = executeEvents(url, events, start, end);
//
//
//String response = readInput("Have a failure?(yes/no)").toLowerCase();
//
//if (response.equals("yes")) {
////boolean leftResult = executeEvents(url, events, start, mid);
//boolean leftResult = binarySearch(url,events,start,mid);
//if (leftResult) {
//info(String.format("## Minimal events: event %d to event %d (size: %d)",start,end,(end-start) +1));
//return true;
//} else {
//
//boolean rightResult = binarySearch(url,events,mid + 1, end);
//if(rightResult) {
//info(String.format("## Minimal events: event %d to event %d (size: %d)",start,end,(end-start) +1));
//
//}
//} else {
//
//}
//
//return false;
//
//
//
//
//String leftResponse = readInput("Have a failure?(yes/no)").toLowerCase();
//if (leftResponse.equals("yes")) {
//binarySearch(url, events, start, mid);
//} else {
//boolean rightResult = executeEvents(url, events, mid + 1, end);
//String rightResponse = readInput("Have a failure?(yes/no)").toLowerCase();
//if (rightResponse.equals("yes")) {
//binarySearch(url, events, mid + 1, end);
//} else {
//info("# there is no failure!");
//}
//}
//} else {
//info("# there is no failure!");
//}

//
//if(leftFailure) {
//return true;
//} else {
//boolean rightFailure = binarySearch(events,mid+1,end);
//
//if(rightFailure) {
//return true;
//}
//}
//} else {
//info("# there is no failure!");
//}
}
