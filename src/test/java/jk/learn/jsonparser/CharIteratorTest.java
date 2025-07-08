package jk.learn.jsonparser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class CharIteratorTest {
  @Test
  void instantiates() {
    assertThat(new CharIterator(new char[0])).isNotNull();
  }

  @Test
  void callingHasNextWithoutElementsReturnsFalse() {
    var ci = new CharIterator(new char[0]);
    assertThat(ci.hasNext()).isFalse();
  }

  @Test
  void callingHasPreviousWithoutElementsReturnsFalse() {
    var ci = new CharIterator(new char[0]);
    assertThat(ci.hasPrevious()).isFalse();
  }

  @Test
  void callingNextWithoutElementsThrowsEx() {
    var ci = new CharIterator(new char[0]);
    assertThatThrownBy(() -> ci.next()).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void callingPreviousWithoutElementsThrowsEx() {
    var ci = new CharIterator(new char[0]);
    assertThatThrownBy(() -> ci.previous()).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void callingHasNextWithOneElementReturnsTrue() {
    var ci = new CharIterator(new char[]{'a'});
    assertThat(ci.hasNext()).isTrue();
  }

  @Test
  void callingHasPreviousWithOneElementReturnsFalse() {
    var ci = new CharIterator(new char[]{'a'});
    assertThat(ci.hasPrevious()).isFalse();
  }

  @Test
  void callingNextWithOneElementReturnsElement() {
    var ci = new CharIterator(new char[]{'a'});
    assertThat(ci.next()).isEqualTo('a');
  }

  @Test
  void callingNextSecondTimeWithOneElementThrowsEx() {
    var ci = new CharIterator(new char[]{'a'});
    ci.next();
    assertThatThrownBy(() -> ci.next()).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void callingHasPreviousAfterNextWithOneElementReturnsTrue() {
    var ci = new CharIterator(new char[]{'a'});
    ci.next();
    assertThat(ci.hasPrevious()).isTrue();
  }

  @Test
  void callingPreviousAfterNextWithOneElementReturnsElement() {
    var ci = new CharIterator(new char[]{'a'});
    ci.next();
    assertThat(ci.previous()).isEqualTo('a');
  }
}
