package jk.learn.jsonparser;

import java.util.NoSuchElementException;
import java.util.Objects;

public class CharIterator {
  private final char[] chars;
  private int pos = 0;

  public CharIterator(char[] chars) {
    Objects.requireNonNull(chars);
    this.chars = chars;
  }

  public boolean hasNext() {
    return pos < chars.length;
  }

  public char next() {
    if (hasNext()) {
      return chars[pos++];
    }
    throw new NoSuchElementException();
  }

  public boolean hasPrevious() {
    return pos > 0;
  }

  public char previous() {
    if (hasPrevious()) {
      return chars[--pos];
    }
    throw new NoSuchElementException();
  }
}
