package jk.learn.demo;

import jk.learn.jsonparser.Grammar;
import jk.learn.jsonparser.Tokenizer;

public class JsonParsingDemo {
  public static void main(String[] args) {
    char[] json = """
        {
          "name": "ram",
          "city": "ayodhya",
          "place": nuLL,
          "arrows": 29,
          "water": 0.5,
          "angry": False,
          "loving": TRUE,
          "attributes": {
            "complexion": "fair-blue",
            "height": "very-good",
            "weight": "almost-perfect",
            "arrowTypes": ["sarkanda", "agni"]
          }
        }\
        """.toCharArray();

    System.out.println(json);
    System.out.println("--------");

    var tokens = Tokenizer.tokenize(json);
    for (var t : tokens) {
      System.out.println(t);
    }
    Grammar.check(tokens);
  }
}
