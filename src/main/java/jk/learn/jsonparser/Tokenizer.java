package jk.learn.jsonparser;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Tokenizer {
  static final Pattern NULL_INTERMEDIATE = Pattern.compile("(n|nu|nul|null)", Pattern.CASE_INSENSITIVE);
  static final Pattern NULL_FINAL = Pattern.compile("null", Pattern.CASE_INSENSITIVE);

  static List<Token> tokenize(char[] json) {
    // trying to parse a simple object with key values, all values are strings
    // {
    //   "key": "value"
    // }
    List<Token> tokens = new LinkedList<>();
    final Matcher nullIntermediateMatcher = NULL_INTERMEDIATE.matcher("");
    final Matcher nullFinalMatcher = NULL_FINAL.matcher("");
    int line = 1;
    int column = 0;
    StringBuilder buffer = null;
    int sLine = 0;
    int sCol = 0;

    BasicContext ctxt = new BasicContext();

    for (char ch : json) {
      column++;

      if (ctxt.isNone() && ch == '"') {
        ctxt.inToString();
        sLine = line;
        sCol = column;
        buffer = new StringBuilder();
      }
      else if (ctxt.isString() && ch == '"') {
        ctxt.outOfString();
        tokens.add(Token.string(sLine, sCol, buffer.toString()));
        sLine = 0;
        sCol = 0;
        buffer = null;
      }
      else if (ctxt.isString()) {
        buffer.append(ch);
      }
      else if (ctxt.isNone() && isNullBegin(ch)) {
        ctxt.inToNull();
        buffer = new StringBuilder(4);
        sLine = line;
        sCol = column;
        buffer.append(ch);
      }
      else if (ctxt.isNull()) {
        buffer.append(ch);
        nullFinalMatcher.reset(buffer);
        if (nullFinalMatcher.matches()) {
          tokens.add(Token._null(sLine, sCol, buffer.toString()));
          buffer = null;
          sLine = 0;
          sCol = 0;
          ctxt.outOfNull();
          continue;
        }
        nullIntermediateMatcher.reset(buffer);
        if (!nullIntermediateMatcher.matches()) {
          throw new JsonParsingException("Expecting 'null' at line " + sLine + " and column " + sCol);
        }
      }
      else if (ch == '\n') {
        column = 0;
        line++;
      }
      else if (ch == ' ' || ch == '\t') {
        continue;
      }
      else if (ch == '{') {
        tokens.add(Token.oStart(line, column));
      }
      else if (ch == '}') {
        tokens.add(Token.oEnd(line, column));
      }
      else if (ch == ':') {
        tokens.add(Token.colon(line, column));
      }
      else if (ch == ',') {
        tokens.add(Token.comma(line, column));
      }
      else {
        throw new JsonParsingException(
            "Unrecognized character '" + ch + "' at line " + line + " and column " + column);
      }
    }

    if (buffer != null) {
      throw new JsonParsingException("Invalid JSON");
    }

    return tokens;
  }

  static boolean isNullBegin(char ch) {
    return ch == 'n' || ch == 'N';
  }

  static class BasicContext {
    boolean _string;
    boolean _null;

    void inToString() {
      _string = !_string;
    }

    void outOfString() {
      _string = !_string;
    }

    void inToNull() {
      _null = !_null;
    }

    void outOfNull() {
      _null = !_null;
    }

    boolean isString() {
      return _string && !_null;
    }

    boolean isNull() {
      return !_string && _null;
    }

    boolean isNone() {
      return !_string && !_null;
    }
  }
}
