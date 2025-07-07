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

    var tokens = parse(json);
    System.out.println(json);
    System.out.println("--------");
    validate(tokens);
    tokens = new ArrayList<>(tokens);
    for (var t : tokens) {
      System.out.println(t);
    }
  }

  static void validate(List<Token> tokens) {
    if (tokens.isEmpty()) {
      return;
    }

    ListIterator<Token> it = tokens.listIterator();
    TokenAssertions ta = new TokenAssertions(it);
    assertObject(ta);
  }

  static void assertObject(TokenAssertions ta) {
    ta.next().mustBeObjectStart();

    // empty object
    if (ta.next().isObjectEnd()) {
      return;
    }
    ta.previous();

    assertKeyValuePair(ta);
    while (ta.next().isComma()) {
      assertKeyValuePair(ta);
    }
    ta.previous();

    ta.next().mustBeObjectEnd();
  }

  static void assertKeyValuePair(TokenAssertions ta) {
    ta.next().mustBeString();
    ta.next().mustBeColon();
    ta.next().mustBeString();
  }

  static List<Token> parse(char[] json) {
    // trying to parse a simple object with key values, all values are strings
    // {
    //   "key": "value"
    // }
    List<Token> tokens = new LinkedList<>();
    int line = 1;
    int column = 0;
    boolean isString = false;
    StringBuilder buffer = null;
    int sLine = 0;
    int sCol = 0;

    for (char ch : json) {
      column++;
      if (ch == '"') {
        isString = !isString;
        if (isString) {
          sLine = line;
          sCol = column;
          buffer = new StringBuilder();
        } else {
          tokens.add(Token.string(sLine, sCol, buffer.toString()));
          sLine = 0;
          sCol = 0;
          buffer = null;
        }
      } else if (isString) {
        buffer.append(ch);
      } else if (ch == '\n') {
        column = 0;
        line++;
      } else if (ch == ' ' || ch == '\t') {
        continue;
      } else if (ch == '{') {
        tokens.add(Token.oStart(line, column));
      } else if (ch == '}') {
        tokens.add(Token.oEnd(line, column));
      } else if (ch == ':') {
        tokens.add(Token.colon(line, column));
      } else if (ch == ',') {
        tokens.add(Token.comma(line, column));
      } else {
        throw new JsonParsingException(
            "Unrecognized character '" + ch + "' at line " + line + " and column " + column);
      }
    }

    if (isString) {
      throw new JsonParsingException("Invalid JSON, reached EOF without closing string");
    }

    return tokens;
  }
}
