package jk.learn.jsonparser;

import java.util.*;

public class JsonParser {
  public static void main(String[] args) {
    char[] json = """
        {
          "name": "ram",
          "city": "ayodhya"
        }\
        """.toCharArray();

    System.out.println(json);
    System.out.println("--------");

    var tokens = Tokenizer.tokenize(json);
    Grammer.check(tokens);

    for (var t : tokens) {
      System.out.println(t);
    }
  }
}
