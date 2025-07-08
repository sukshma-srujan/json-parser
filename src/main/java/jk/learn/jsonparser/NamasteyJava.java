package jk.learn.jsonparser;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class NamasteyJava {
  public static void main(String[] args) {
    ListIterator<Integer> li = List.of(1,2,3).listIterator();
    System.out.println("next : " + li.next());
    System.out.println("next : " + li.next());
    System.out.println("prev : " + li.previous());
    System.out.println("next : " + li.next());
    System.out.println("next : " + li.next());

    CharIterator ci = new CharIterator(new char[] {'a', 'b', 'c'});
    System.out.println("next : " + ci.next());
    System.out.println("next : " + ci.next());
    System.out.println("prev : " + ci.previous());
    System.out.println("next : " + ci.next());
    System.out.println("next : " + ci.next());
  }

  static void test1() {
    Pattern p = Pattern.compile("null");
    Matcher matcher = p.matcher("");

    matcher.reset("null");
    if (matcher.matches()) {
      onMatch(matcher);
      onNoMatch(matcher);
    }

    matcher.reset("nul");
    if (matcher.matches()) {
      onNoMatch(matcher);
    }
  }

  static void onNoMatch(Matcher matcher) {
    System.out.println(matcher);
    System.out.println("start: " + matcher.regionStart());
    System.out.println("end  : " + matcher.regionEnd());
  }

  static void onMatch(Matcher matcher) {
    System.out.println(matcher);
    System.out.println("start: " + matcher.start());
    System.out.println("end  : " + matcher.end());
  }
}
