package edu.yu.parallel;

import java.util.Arrays;
import java.util.function.IntBinaryOperator;

/** Wrapper implementation of the JDK's parallel prefix sum.
 *
 */

public class JDKParallelPrefixSum implements PrefixSum {

  /** No-arg constructor
   */
  public JDKParallelPrefixSum() {}

  IntBinaryOperator operator = (a, b) -> a + b;
  @Override
  public int[] prefixSum(int[] input)
  {
    Arrays.parallelPrefix(input, operator);
    return input;
  }
}
