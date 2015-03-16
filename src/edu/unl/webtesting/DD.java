package edu.unl.webtesting;

//Simple Delta Debugging algorithm
//@author: Andreas Zeller
//$Id: DD.java,v 1.1 2004/12/01 16:36:53 zeller Exp $

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class DD {

 // Outcome
 public static final int FAIL       = -1;
 public static final int PASS       = +1;
 public static final int UNRESOLVED = 0;

 String pretty_outcome(int outcome) {
     if (outcome == PASS)
         return "PASS";
     else if (outcome == FAIL)
         return "FAIL";
     else if (outcome == UNRESOLVED)
         return "UNRESOLVED";
     else {
         assert false;
         return null;
     }
 }
 
 // List utilities
 // Split a configuration C into N subsets
 public static List split(List c, int n) {
     List subSets = new LinkedList();
     int start = 0;

     for (int i = 0; i < n; i++) {
         List subSet = 
             c.subList(start, start + (c.size() - start) / (n - i));
         subSets.add(subSet);
         start += subSet.size();
     }

     // print("split(" + c + ", " + n + ") = " + subSets);

     return subSets;
 }

 // Return a - b
 public static List minus(List a, List b) {
     List c = new LinkedList();

     // Rather inefficient right now
     for (Iterator i = a.iterator(); i.hasNext();) {
         Object element = i.next();
         if (!b.contains(element)) {                 
             c.add(element);
         }
     }

     // print("minus(" + a + ", " + b + ") = " + c);

     return c;
 }

 // Return a \cup b
 public static List union(List a, List b) {
     List c = new LinkedList();

     for (Iterator i = a.iterator(); i.hasNext();) {
         Object element = i.next();
             c.add(element);
     }

     for (Iterator i = b.iterator(); i.hasNext();) {
         Object element = i.next();
             c.add(element);
     }

     // print("union(" + a + ", " + b + ") = " + c);

     return c;
 }

 // test function - to be overloaded in subclasses
 public int test(List config)
 {
     return UNRESOLVED;
 }

 // ddmin algorithm
 // Return a sublist of CIRCUMSTANCES that is a relevant
 // configuration with respect to TEST.
 public List ddmin(List circumstances_)
 {
     print("ddmin(" + circumstances_ + ")...");

     List circumstances = circumstances_;

     assert test(new LinkedList()) == PASS;
     assert test(circumstances) == FAIL;

     int n = 2;

     while (circumstances.size() >= 2)
     {
         print("circumstance [n=" + n + "]: " + circumstances);
         List subsets = split(circumstances, n);
         assert subsets.size() == n;

         print("ddmin: testing subsets");

         boolean some_complement_is_failing = false;
         for (int i = 0; i < subsets.size(); i++)
         {
             List subset = (List)subsets.get(i);
             List complement = minus(circumstances, subset);

             if (test(complement) == FAIL)
             {
                 circumstances = complement;
                 n = Math.max(n - 1, 2);
                 some_complement_is_failing = true;
                 break;
             }
         }

         if (!some_complement_is_failing)
         {
             if (n == circumstances.size())
                 break;

             print("ddmin: increasing granularity");
             n = Math.min(n * 2, circumstances.size());
         }
     }

     print("ddmin(" + circumstances_ + ") = " + circumstances);

     return circumstances;
 }

 
 // ddiso algorithm
 // Return a triple (DELTA, C_PASS', C_FAIL') such that
 // C_PASS subseteq C_PASS' subset C_FAIL' subseteq C_FAIL holds
 // DELTA = C_FAIL' - C_PASS' is a minimal difference relevant for TEST.
 public List ddiso(List c_pass_, List c_fail_)
 {
     print("ddiso(" + c_pass_ + ", " + c_fail_ + ")...");

     List c_pass = c_pass_;
     List c_fail = c_fail_;

     int n = 2;

     while (true) {
         assert test(c_pass) == PASS;
         assert test(c_fail) == FAIL;

         List delta = minus(c_fail, c_pass);
         if (n > delta.size()) {
             List ret = new LinkedList();
             ret.add(delta);
             ret.add(c_pass);
             ret.add(c_fail);

             print("ddiso(" + c_pass_ + ", " + c_fail_ + 
                                ") = " + ret);
             return ret;
         }

         List deltas = split(delta, n);
         assert deltas.size() == n;

         int offset = 0;
         int j = 0;

         while (j < n) {
             int i = (j + offset) % n;
             List next_c_pass = union(c_pass, (List)deltas.get(i));
             List next_c_fail = minus(c_fail, (List)deltas.get(i));

             if (test(next_c_fail) == FAIL && n == 2) {
                 c_fail = next_c_fail;
                 n = 2; offset = 0; break;
             } else if (test(next_c_fail) == PASS) {
                 c_pass = next_c_fail;
                 n = 2; offset = 0; break;
             } else if (test(next_c_pass) == FAIL) {
                 c_fail = next_c_pass;
                 n = 2; offset = 0; break;
             } else if (test(next_c_fail) == FAIL) {
                 c_fail = next_c_fail;
                 n = Math.max(n - 1, 2); offset = i; break;
             } else if (test(next_c_pass) == PASS) {
                 c_pass = next_c_pass;
                 n = Math.max(n - 1, 2); offset = i; break;
             } else {
                 j++;
             }
        }

        if (j >= n) {
            if (n >= delta.size()) {
                List ret = new LinkedList();
                ret.add(delta);
                ret.add(c_pass);
                ret.add(c_fail);

                print("ddiso(" + c_pass_ + ", " + c_fail_ + 
                                   ") = " + ret);
            }
            else {
                print("ddmin: increasing granularity"); 
                n = Math.min(n * 2, delta.size());
            }
        }
     }
 }
 
 public void print(String s){
   System.out.println(s);
 }
}


