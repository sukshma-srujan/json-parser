package jk.learn.jsonparser;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
  static final Pattern NUMBER_START = Pattern.compile("[+-]|\\d");
  static final Pattern PARTIAL_NUMBER =
      Pattern.compile("[+-]?\\d+|[+-]?\\d+\\.|[+-]?\\d+\\.\\d+");
  static final Pattern COMPLETE_NUMBER = Pattern.compile("([+-]?\\d+)(\\.\\d+)?");
  static final Pattern PARTIAL_NULL = Pattern.compile("n|nu|nul", Pattern.CASE_INSENSITIVE);
  static final Pattern COMPLETE_NULL = Pattern.compile("null", Pattern.CASE_INSENSITIVE);

  static final Matcher NUMBER_START_MATCHER = NUMBER_START.matcher("");

  static List<Token> tokenize(char[] json) {
    // trying to parse a simple object with key values, all values are strings
    // {
    //   "key": "value"
    // }
    List<Token> tokens = new LinkedList<>();
    final Matcher partialNumberMatcher = PARTIAL_NUMBER.matcher("");
    final Matcher completeNumberMatcher = COMPLETE_NUMBER.matcher("");
    final Matcher partialNullMatcher = PARTIAL_NULL.matcher("");
    final Matcher completeNullMatcher = COMPLETE_NULL.matcher("");
    int line = 1;
    int column = 0;
    StringBuilder buffer = null;
    int sLine = 0;
    int sCol = 0;

    BasicContext ctxt = new BasicContext();

    CharIterator ci = new CharIterator(json);
    while (ci.hasNext()) {
      column++;
      char ch = ci.next();

      if (ch == '\n') {
        column = 0;
        line++;
      }
      else if (ch == ' ' || ch == '\t') {
        // do nothing
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
      else if (ch == '"') {
        ctxt.inToString();
        sLine = line;
        sCol = column;
        buffer = new StringBuilder();

        while (ci.hasNext()) {
          ch = ci.next();
          if (ch == '"') {
            ctxt.outOfString();
            tokens.add(Token.string(sLine, sCol, buffer.toString()));
            buffer = null;
            break;
          } else {
            buffer.append(ch);
          }
        }
      }
      else if (isNullBegin(ch)) {
        ctxt.inToNull();
        buffer = new StringBuilder(4);
        sLine = line;
        sCol = column;
        buffer.append(ch);

        while (ci.hasNext()) {
          ch = ci.next();
          buffer.append(ch);
          completeNullMatcher.reset(buffer);
          partialNullMatcher.reset(buffer);
          if (completeNullMatcher.matches()) {
            tokens.add(Token._null(sLine, sCol, buffer.toString()));
            buffer = null;
            ctxt.outOfNull();
            break;
          }
          else if (!partialNullMatcher.matches()) {
            throw new JsonParsingException("Expecting 'null' at line " + sLine + " and column " + sCol);
          }
        }
      }
      else if (isNumberBegin(ch)) {
        ctxt.inToNumber();
        buffer = new StringBuilder(10);
        sLine = line;
        sCol = column;
        buffer.append(ch);

        while (ci.hasNext()) {
          ch = ci.next();
          buffer.append(ch);
          partialNumberMatcher.reset(buffer);

          if (!partialNumberMatcher.matches()) {
            break;
          }
        }

        if (!partialNumberMatcher.matches()) {
          buffer.deleteCharAt(buffer.length() - 1);
          ci.previous();
        }

        completeNumberMatcher.reset(buffer);

        if (completeNumberMatcher.matches()) {
          tokens.add(Token.number(sLine, sCol, buffer.toString()));
          buffer = null;
          ctxt.outOfNumber();
        } else {
          ch = buffer.charAt(buffer.length() - 1);
          throw new JsonParsingException(
              "Unexpected character '" + ch + "' at line " + line + " and column " + column);
        }
      }
      else {
        throw new JsonParsingException(
            "Unexpected character '" + ch + "' at line " + line + " and column " + column);
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

  static boolean isNumberBegin(char ch) {
    NUMBER_START_MATCHER.reset(ch + "");
    return NUMBER_START_MATCHER.matches();
  }

  static class BasicContext {
    boolean _string;
    boolean _null;
    boolean _number;

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

    void inToNumber() {
      _number = !_number;
    }

    void outOfNumber() {
      _number = !_number;
    }

    boolean isString() {
      return _string && !_null && !_number;
    }

    boolean isNull() {
      return !_string && _null && !_number;
    }

    boolean isNone() {
      return !_string && !_null && _number;
    }
  }
}
