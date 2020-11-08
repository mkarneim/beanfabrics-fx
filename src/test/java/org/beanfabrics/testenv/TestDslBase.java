package org.beanfabrics.testenv;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multimap;
import com.google.common.collect.Range;
import org.assertj.core.api.Assertions;


/**
 * This is the domain-agnostic basis for the domain-specific language of tests.
 */
public class TestDslBase extends Assertions {
  @CheckReturnValue
  public static int few() {
    return 3;
  }

  @CheckReturnValue
  public static int several() {
    return 10;
  }

  @CheckReturnValue
  public static int many() {
    return 100;
  }


  @SafeVarargs
  @CheckReturnValue
  public static <P> P[] arrayOf(P... elements) {
    return elements;
  }

  @SafeVarargs
  @CheckReturnValue
  public static <P> List<P> listOf(P... elements) {
    return Arrays.asList(elements);
  }

  @SafeVarargs
  @CheckReturnValue
  public static <P> Set<P> setOf(P... elements) {
    return new HashSet<>(Arrays.asList(elements));
  }

  @SafeVarargs
  @CheckReturnValue
  public static <E extends Enum<E>> EnumSet<E> enumSetOf(E... elems) {
    return EnumSet.copyOf(listOf(elems));
  }

  @SafeVarargs
  @CheckReturnValue
  public static <K, V> Map<K, V> mapOf(Map.Entry<? extends K, ? extends V>... entries) {
    return mapOf(Arrays.asList(entries));
  }



  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public static <P> P[] arrayOf(Collection<P> elements) {
    elements = new ArrayList<>(elements); // copy elements to get rid of some internal Guava classes
    P[] result = null;
    if (!elements.isEmpty()) {
      Class<?> elemType = elements.iterator().next().getClass();
      result = elements.toArray((P[]) Array.newInstance(elemType, elements.size()));
    }
    return result;
  }

  @CheckReturnValue
  public static <P> List<P> listOf(Collection<? extends P> col) {
    return new ArrayList<>(col);
  }

  @CheckReturnValue
  public static <P> Set<P> setOf(Collection<? extends P> col) {
    return new HashSet<>(col);
  }

  @CheckReturnValue
  public static <E extends Enum<E>> EnumSet<E> enumSetOf(Collection<E> col) {
    return EnumSet.copyOf(col);
  }

  @CheckReturnValue
  public static <K, V> Map<K, V> mapOf(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
    Map<K, V> result = new HashMap<>();
    for (Entry<? extends K, ? extends V> entry : entries) {
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
  }



  @SafeVarargs
  @CheckReturnValue
  public static <P> List<P> listOf(Collection<? extends P>... cols) {
    List<P> result = new ArrayList<P>();
    for (Collection<? extends P> col : cols) {
      result.addAll(col);
    }
    return result;
  }

  @SafeVarargs
  @CheckReturnValue
  public static <P> Set<P> setOf(Collection<? extends P>... cols) {
    Set<P> result = new HashSet<P>();
    for (Collection<? extends P> col : cols) {
      result.addAll(col);
    }
    return result;
  }



  @SafeVarargs
  @CheckReturnValue
  public static <P> List<P> listOf(Collection<? extends P> col, P... elements) {
    List<P> result = new ArrayList<P>(col);
    for (P elem : elements) {
      result.add(elem);
    }
    return result;
  }

  @SafeVarargs
  @CheckReturnValue
  public static <P> Set<P> setOf(Collection<? extends P> col, P... elements) {
    Set<P> result = new HashSet<P>(col);
    for (P elem : elements) {
      result.add(elem);
    }
    return result;
  }



  @CheckReturnValue
  public static <E> List<E> emptyList() {
    return Collections.emptyList();
  }

  @CheckReturnValue
  public static <E> Set<E> emptySet() {
    return Collections.emptySet();
  }

  @CheckReturnValue
  public static <K, V> Map<K, V> emptyMap() {
    return Collections.emptyMap();
  }

  @CheckReturnValue
  public static <K, V> Multimap<K, V> emptyMultimap() {
    return ArrayListMultimap.create();
  }

  @SuppressWarnings("unchecked")
  @CheckReturnValue
  public static <E> E[] emptyArray(Class<E> cls) {
    return (E[]) Array.newInstance(cls, 0);
  }

  @CheckReturnValue
  public static <E> List<E> emptyList(Class<E> clazz) {
    return emptyList();
  }

  @CheckReturnValue
  public static <E> Set<E> emptySet(Class<E> clazz) {
    return emptySet();
  }

  @CheckReturnValue
  public static <K, V> Map<K, V> emptyMap(Class<K> keyClass, Class<V> valueClass) {
    return emptyMap();
  }

  @CheckReturnValue
  public static <K, V> Multimap<K, V> emptyMultimap(Class<K> key, Class<V> value) {
    return emptyMultimap();
  }


  @CheckReturnValue
  public static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
    return entry(key, value);
  }



  @CheckReturnValue
  public static <E extends Enum<E>> EnumSet<E> enumSetAllOf(Class<E> enumType) {
    return EnumSet.allOf(enumType);
  }


  @CheckReturnValue
  private static <E> Collection<E> shuffle(Collection<E> elements) {
    List<E> result = new ArrayList<>(elements);
    // Zufällige aber deterministische Reihenfolge!
    Collections.shuffle(result, new Random(12345));
    return result;
  }


  @CheckReturnValue
  public static <T> T firstOf(Iterable<? extends T> it) {
    return it.iterator().next();
  }

  @CheckReturnValue
  public static <T> T secondOf(Iterable<? extends T> it) {
    Iterator<? extends T> iterator = it.iterator();
    iterator.next();
    return iterator.next();
  }

  @CheckReturnValue
  public static <T> T lastOf(Iterable<? extends T> it) {
    return Iterables.getLast(it);
  }

  @CheckReturnValue
  public static <T> T as(Class<T> cls, Object entity) {
    return cls.cast(entity);
  }



  @CheckReturnValue
  public static ContiguousSet<Integer> range(int min, int max) {
    return ContiguousSet.create(Range.closed(min, max), DiscreteDomain.integers());
  }

  @CheckReturnValue
  public static ContiguousSet<Long> range(long min, long max) {
    return ContiguousSet.create(Range.closed(min, max), DiscreteDomain.longs());
  }

  @CheckReturnValue
  public static ContiguousSet<BigInteger> range(BigInteger min, BigInteger max) {
    return ContiguousSet.create(Range.closed(min, max), DiscreteDomain.bigIntegers());
  }

  public static <T extends Number> double sumOf(Iterable<? extends T> it) {
    double result = 0.0;
    for (T value : it) {
      if (value != null) {
        result += value.doubleValue();
      }
    }
    return result;
  }

  @CheckReturnValue
  public static int countNonNulls(Object... elements) {
    return countNonNulls(Arrays.asList(elements));
  }

  @CheckReturnValue
  public static int countNonNulls(Iterable<?> elements) {
    return Iterators.size(filterNotNull(elements).iterator());
  }

  @CheckReturnValue
  public static int countNonAbsents(Optional<?>... elements) {
    return countNonAbsents(Arrays.asList(elements));
  }

  @CheckReturnValue
  public static int countNonAbsents(Iterable<? extends Optional<?>> elements) {
    return Iterators.size(filterNotAbsent(elements).iterator());
  }

  @CheckReturnValue
  public static <T> Iterable<T> filterNotNull(Iterable<T> unfiltered) {
    return Iterables.filter(unfiltered, Predicates.notNull());
  }

  @CheckReturnValue
  public static <T> Collection<T> filterNotNull(Collection<T> unfiltered) {
    return Collections2.filter(unfiltered, Predicates.notNull());
  }

  @CheckReturnValue
  public static <T extends Optional<?>> Iterable<T> filterNotAbsent(Iterable<T> unfiltered) {
    return Iterables.filter(unfiltered, new Predicate<Optional<?>>() {
      @Override
      public boolean apply(@Nullable Optional<?> input) {
        return input == null || input.isPresent();
      }
    });
  }
}
