package jk.learn.jsonparser;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexHi {
  public static void main(String[] args) {
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
