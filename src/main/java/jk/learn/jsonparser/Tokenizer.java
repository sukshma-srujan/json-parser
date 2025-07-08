package jk.learn.jsonparser;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
  static final Matcher NUMBER_START = Pattern.compile("[+-]|\\d").matcher("");
  static final Matcher PARTIAL_NUMBER =
      Pattern.compile("[+-]?\\d+|[+-]?\\d+\\.|[+-]?\\d+\\.\\d+").matcher("");
  static final Matcher COMPLETE_NUMBER = Pattern.compile("([+-]?\\d+)(\\.\\d+)?").matcher("");

  static final Matcher PARTIAL_NULL = Pattern.compile("n|nu|nul", Pattern.CASE_INSENSITIVE).matcher("");
  static final Matcher COMPLETE_NULL = Pattern.compile("null", Pattern.CASE_INSENSITIVE).matcher("");

  static final Matcher PARTIAL_FALSE = Pattern.compile("f|fa|fal|fals|false", Pattern.CASE_INSENSITIVE).matcher("");
  static final Matcher COMPLETE_FALSE = Pattern.compile("false", Pattern.CASE_INSENSITIVE).matcher("");

  static final Matcher PARTIAL_TRUE = Pattern.compile("t|tr|tru|true", Pattern.CASE_INSENSITIVE).matcher("");
  static final Matcher COMPLETE_TRUE = Pattern.compile("true", Pattern.CASE_INSENSITIVE).matcher("");

  public static List<Token> tokenize(char[] json) {
    List<Token> tokens = new LinkedList<>();
    int line = 1;
    int column = 0;
    StringBuilder buffer = null;
    int sLine;
    int sCol;

    CharIterator ci = new CharIterator(json);
    while (ci.hasNext()) {
      column++;
      char ch = ci.next();

      if (ch == '\n') {
        column = 0;
        line++;
      }
      else if (ch == ' ' || ch == '\t') {
        // ignored
      }
      else if (ch == '{') {
        tokens.add(Token.os(line, column));
      }
      else if (ch == '}') {
        tokens.add(Token.oe(line, column));
      }
      else if (ch == '[') {
        tokens.add(Token.as(line, column));
      }
      else if (ch == ']') {
        tokens.add(Token.ae(line, column));
      }
      else if (ch == ':') {
        tokens.add(Token.colon(line, column));
      }
      else if (ch == ',') {
        tokens.add(Token.comma(line, column));
      }
      else if (ch == '"') {
        sLine = line;
        sCol = column;
        buffer = new StringBuilder();

        while (ci.hasNext()) {
          ch = ci.next();
          if (ch == '"') {
            tokens.add(Token.string(sLine, sCol, buffer.toString()));
            buffer = null;
            break;
          } else {
            buffer.append(ch);
          }
        }
      }
      else if (isNullBegins(ch)) {
        buffer = new StringBuilder(4);
        sLine = line;
        sCol = column;
        buffer.append(ch);

        while (ci.hasNext()) {
          ch = ci.next();
          buffer.append(ch);
          COMPLETE_NULL.reset(buffer);
          PARTIAL_NULL.reset(buffer);
          if (COMPLETE_NULL.matches()) {
            tokens.add(Token._null(sLine, sCol, buffer.toString()));
            buffer = null;
            break;
          }
          else if (!PARTIAL_NULL.matches()) {
            ch = buffer.charAt(buffer.length() - 1);
            throw new JsonParsingException("Unexpected character '" + ch + "' at line " + sLine + " and column " + sCol);
          }
        }
      }
      else if (isNumberBegins(ch)) {
        buffer = new StringBuilder(10);
        sLine = line;
        sCol = column;
        buffer.append(ch);

        while (ci.hasNext()) {
          ch = ci.next();
          buffer.append(ch);
          PARTIAL_NUMBER.reset(buffer);

          if (!PARTIAL_NUMBER.matches()) {
            break;
          }
        }

        if (!PARTIAL_NUMBER.matches()) {
          buffer.deleteCharAt(buffer.length() - 1);
          ci.previous();
        }

        COMPLETE_NUMBER.reset(buffer);

        if (COMPLETE_NUMBER.matches()) {
          tokens.add(Token.number(sLine, sCol, buffer.toString()));
          buffer = null;
        } else {
          ch = buffer.charAt(buffer.length() - 1);
          throw new JsonParsingException(
              "Unexpected character '" + ch + "' at line " + line + " and column " + column);
        }
      } else if (isTrueBegins(ch)) {
        buffer = new StringBuilder(4);
        sLine = line;
        sCol = column;
        buffer.append(ch);

        while (ci.hasNext()) {
          ch = ci.next();
          buffer.append(ch);
          COMPLETE_TRUE.reset(buffer);
          PARTIAL_TRUE.reset(buffer);
          if (COMPLETE_TRUE.matches()) {
            tokens.add(Token._true(sLine, sCol, buffer.toString()));
            buffer = null;
            break;
          }
          else if (!PARTIAL_TRUE.matches()) {
            ch = buffer.charAt(buffer.length() - 1);
            throw new JsonParsingException("Unexpected character '" + ch + "' at line " + sLine + " and column " + sCol);
          }
        }
      } else if (isFalseBegins(ch)) {
        buffer = new StringBuilder(5);
        sLine = line;
        sCol = column;
        buffer.append(ch);

        while (ci.hasNext()) {
          ch = ci.next();
          buffer.append(ch);
          COMPLETE_FALSE.reset(buffer);
          PARTIAL_FALSE.reset(buffer);
          if (COMPLETE_FALSE.matches()) {
            tokens.add(Token._false(sLine, sCol, buffer.toString()));
            buffer = null;
            break;
          }
          else if (!PARTIAL_FALSE.matches()) {
            ch = buffer.charAt(buffer.length() - 1);
            throw new JsonParsingException("Unexpected character '" + ch + "' at line " + sLine + " and column " + sCol);
          }
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

  static boolean isNullBegins(char ch) {
    return ch == 'n' || ch == 'N';
  }

  static boolean isNumberBegins(char ch) {
    NUMBER_START.reset(ch + "");
    return NUMBER_START.matches();
  }

  static boolean isTrueBegins(char ch) {
    return ch == 't' || ch == 'T';
  }

  static boolean isFalseBegins(char ch) {
    return ch == 'f' || ch == 'F';
  }
}
