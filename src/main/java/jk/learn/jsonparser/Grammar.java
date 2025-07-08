package jk.learn.jsonparser;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.ListIterator;

public class Grammar {
  public static void check(List<Token> tokens) {
    if (tokens.isEmpty()) {
      return;
    }
    isJson(tokens.listIterator());
    validateSingleRoot(tokens.listIterator());
  }

  private static void validateSingleRoot(ListIterator<Token> it) {
    if (it.next().isBasic() && !it.hasNext()) {
      return;
    }
    it.previous();

    Deque<Context> cs = new ArrayDeque<>();
    while (it.hasNext()) {
      if (cs.isEmpty() && it.hasPrevious()) {
        ut(it.next());
      }
      Token t = it.next();
      if (t.isOs()) {
        cs.push(Context.object);
      } else if (t.isOe()) {
        if (cs.pop() != Context.object) {
          ut(t);
        }
      } else if (t.isAs()) {
        cs.push(Context.array);
      } else if (t.isAe()) {
        if (cs.pop() != Context.array) {
          ut(t);
        }
      }
    }

    if (!cs.isEmpty()) {
      ut(it.previous());
    }
  }


  enum Context {
    object, array
  }

  private static void isJson(ListIterator<Token> it) {
    if (!it.hasNext()) {
      ueof(it.previous());
    }

    Token token = it.next();
    if (token.isBasic()) {
      return;
    }

    if (token.isOs()) {
      validateObject(it);
    }
    else if (token.isAs()) {
      validateArray(it);
    }
    else {
      ut(token);
    }
  }

  static void validateObject(ListIterator<Token> it) {
    if (!it.hasNext()) {
      ueof(it.previous());
    }
    if (it.next().isOe()) {
      return;
    }
    it.previous();

    validateKv(it);
    while (it.hasNext()) {
      if (it.next().isComma()) {
        validateKv(it);
      } else {
        it.previous();
        break;
      }
    }

    if (!it.hasNext()) {
      ueof(it.previous());
    }
    if (!it.next().isOe()) {
      ut(it.previous());
    }
  }

  static void validateKv(ListIterator<Token> it) {
    if (!it.hasNext()) {
      ueof(it.previous());
    }

    if (!it.next().isString()) {
      ut(it.previous());
    }
    if (!it.hasNext()) {
      ueof(it.previous());
    }
    if (!it.next().isColon()) {
      ut(it.previous());
    }
    if (!it.hasNext()) {
      ueof(it.previous());
    }

    isJson(it);
  }

  static void validateArray(ListIterator<Token> it) {
    if (!it.hasNext()) {
      ueof(it.previous());
    }
    if (it.next().isAe()) {
      return;
    }
    it.previous();

    isJson(it);
    while (it.hasNext()) {
      if (it.next().isComma()) {
        isJson(it);
      } else {
        it.previous();
        break;
      }
    }

    if (!it.hasNext()) {
      ueof(it.previous());
    }
    if (!it.next().isAe()) {
      ut(it.previous());
    }
  }

  static void ut(Token token) {
    throw new JsonParsingException(
        "Unexpected "
        + stringFor(token.type)
        + " at line "
        + token.line
        + " and column "
        + token.colStart);
  }

  static void ueof(Token last) {
    throw new JsonParsingException(
        "Unexpected end of file after " + stringFor(last.type));
  }

  static String stringFor(TokenType type) {
    return switch (type) {
      case string0 -> "string";
      case number0 -> "number";
      case null0 -> "null";
      case true0 -> "true";
      case false0 -> "false";
      case os0 -> "'{'";
      case oe0 -> "'}'";
      case as0 -> "'['";
      case ae0 -> "']'";
      case colon0 -> "':'";
      case comma0 -> "','";
    };
  }
}
