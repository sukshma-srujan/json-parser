package jk.learn.jsonparser;

import java.util.Set;

public enum TokenType {
  string0 ("string"),
  null0 ("null"),
  number0 ("number"),
  true0 ("true"),
  false0 ("false"),
  os0 ("object start"),
  oe0 ("object end"),
  as0 ("array start"),
  ae0 ("array end"),
  colon0 ("colon"),
  comma0 ("comma");

  private final String value;

  TokenType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }


  public static final Set<TokenType> basic_types =
      Set.of(TokenType.string0, TokenType.null0, TokenType.number0, TokenType.true0, TokenType.false0);
}
