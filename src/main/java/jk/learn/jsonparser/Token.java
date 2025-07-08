package jk.learn.jsonparser;

public class Token {
  final TokenType type;
  final String content;
  final int line;
  final int colStart;
  final int colEnd;

  public Token(TokenType type, int line, int colStart) {
    this.type = type;
    this.line = line;
    this.colStart = colStart;
    this.colEnd = colStart;
    this.content = null;
  }

  public Token(TokenType type, int line, int colStart, int colEnd, String content) {
    this.type = type;
    this.line = line;
    this.colStart = colStart;
    this.colEnd = colEnd;
    this.content = content;
  }

  @Override
  public String toString() {
    String s = ", line: " + line + ", colStart: " + colStart + ", colEnd: " + colEnd;
    return "Token(" + type + s + contentToString() + ")";
  }

  private String contentToString() {
    if (content == null) {
      return "";
    }
    String val;
    if (type == TokenType.string0) {
      val = "\"" + content + "\"";
    } else {
      val = content;
    }
    return ", " + val;
  }

  public boolean isOs() {
    return this.type == TokenType.os0;
  }

  public boolean isOe() {
    return this.type == TokenType.oe0;
  }

  public boolean isAs() {
    return this.type == TokenType.as0;
  }

  public boolean isAe() {
    return this.type == TokenType.ae0;
  }

  public boolean isBasic() {
    return TokenType.basic_types.contains(this.type);
  }

  public boolean isString() {
    return this.type == TokenType.string0;
  }

  public boolean isColon() {
    return this.type == TokenType.colon0;
  }

  public boolean isComma() {
    return this.type == TokenType.comma0;
  }

  static Token os(int line, int col) {
    return new Token(TokenType.os0, line, col);
  }

  static Token oe(int line, int col) {
    return new Token(TokenType.oe0, line, col);
  }

  static Token as(int line, int col) {
    return new Token(TokenType.as0, line, col);
  }

  static Token ae(int line, int col) {
    return new Token(TokenType.ae0, line, col);
  }

  static Token colon(int line, int col) {
    return new Token(TokenType.colon0, line, col);
  }

  static Token comma(int line, int col) {
    return new Token(TokenType.comma0, line, col);
  }

  static Token string(int line, int col, String content) {
    return new Token(TokenType.string0, line, col, col + content.length() + 1, content);
  }

  static Token _null(int line, int col, String content) {
    return new Token(TokenType.null0, line, col, col + content.length(), content);
  }

  static Token number(int line, int col, String content) {
    return new Token(TokenType.number0, line, col, col + content.length(), content);
  }

  static Token _true(int line, int col, String content) {
    return new Token(TokenType.true0, line, col, col + content.length(), content);
  }

  static Token _false(int line, int col, String content) {
    return new Token(TokenType.false0, line, col, col + content.length(), content);
  }
}
