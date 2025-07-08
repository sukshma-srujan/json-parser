package jk.learn.jsonparser;

public class JsonParsingDemo {
  public static void main(String[] args) {
    char[] json = """
        {
          "name": "ram",
          "city": "ayodhya",
          "place": null,
          "arrows": 29,
          "water": 0.5
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
