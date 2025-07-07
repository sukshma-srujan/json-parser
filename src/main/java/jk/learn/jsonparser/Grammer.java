package jk.learn.jsonparser;

import java.util.List;
import java.util.ListIterator;

public class Grammer {
  static void check(List<Token> tokens) {
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
}
