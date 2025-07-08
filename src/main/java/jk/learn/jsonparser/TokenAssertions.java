package jk.learn.jsonparser;

import java.util.*;

public class TokenAssertions {
  private final ListIterator<Token> it;
  private Token token;

  public TokenAssertions(ListIterator<Token> it) {
    Objects.requireNonNull(it);
    this.it = it;
  }

  public TokenAssertions next() {
    this.token = it.next();
    return this;
  }

  public TokenAssertions previous() {
    this.token = it.previous();
    return this;
  }

  public boolean hasNext() {
    return it.hasNext();
  }

  public boolean hasPrevious() {
    return it.hasPrevious();
  }

  public void mustBeJson() {
    if (token == null) {
      throw new JsonParsingException("Expecting JSON");
    }
    if (token.type != TokenType.STRING && token.type != TokenType.NULL) {
      throw new JsonParsingException("Expecting JSON at line " + token.line + " and column " + token.colStart);
    }
  }

  public void mustBeString() {
    mustBe(TokenType.STRING);
  }

  public void mustBeObjectStart() {
    mustBe(TokenType.O_START);
  }

  public void mustBeObjectEnd() {
    mustBe(TokenType.O_END);
  }

  public void mustBeColon() {
    mustBe(TokenType.COLON);
  }

  public void mustBeComma() {
    mustBe(TokenType.COMMA);
  }

  private void mustBe(TokenType type) {
    if (token == null) {
      throw unexpectedEof(type);
    }
    if (token.type != type) {
      throw unexpectedToken(type, token);
    }
  }

  public boolean isString() {
    return token != null && token.type == TokenType.STRING;
  }

  public boolean isObjectStart() {
    return token != null && token.type == TokenType.O_START;
  }

  public boolean isObjectEnd() {
    return token != null && token.type == TokenType.O_END;
  }

  public boolean isColon() {
    return token != null && token.type == TokenType.COLON;
  }

  public boolean isComma() {
    return token != null && token.type == TokenType.COMMA;
  }

  static RuntimeException unexpectedToken(TokenType expected, Token token) {
    throw new JsonParsingException(
        "Expected "
        + stringFor(expected)
        + " but found "
        + stringFor(token.type)
        + " at line "
        + token.line
        + " and column "
        + token.colStart);
  }

  static RuntimeException unexpectedEof(TokenType expectedType) {
    return new JsonParsingException(
        "Unexpected end of file, expecting a " + stringFor(expectedType));
  }

  static String stringFor(TokenType type) {
    return switch (type) {
      case STRING -> "string";
      case NUMBER -> "number";
      case NULL -> "null";
      case O_START -> "'{'";
      case O_END -> "'}'";
      case COLON -> "':'";
      case COMMA -> "','";
    };
  }
}
