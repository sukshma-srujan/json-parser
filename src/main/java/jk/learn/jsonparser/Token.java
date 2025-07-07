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
    return "Token(" + type + contentToString() + ")";
  }

  private String contentToString() {
    if (content == null || type == TokenType.NULL) {
      return "";
    }
    String val = "";
    if (type == TokenType.STRING) {
      val = "\"" + content + "\"";
    }
    return ", " + val;
  }

  static Token oStart(int line, int col) {
    return new Token(TokenType.O_START, line, col);
  }

  static Token oEnd(int line, int col) {
    return new Token(TokenType.O_END, line, col);
  }

  static Token colon(int line, int col) {
    return new Token(TokenType.COLON, line, col);
  }

  static Token comma(int line, int col) {
    return new Token(TokenType.COMMA, line, col);
  }

  static Token string(int line, int col, String content) {
    return new Token(TokenType.STRING, line, col, col + content.length() + 1, content);
  }

  static Token _null(int line, int col, String content) {
    return new Token(TokenType.NULL, line, col, col + content.length(), content);
  }
}
