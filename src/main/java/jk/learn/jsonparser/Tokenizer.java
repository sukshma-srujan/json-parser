package jk.learn.jsonparser;

import java.util.LinkedList;
import java.util.List;

public class Tokenizer {
  static List<Token> tokenize(char[] json) {
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
