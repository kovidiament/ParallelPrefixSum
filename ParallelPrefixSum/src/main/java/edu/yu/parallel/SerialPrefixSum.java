package edu.yu.parallel;

/** Serial implementation of prefix sum.
 *
 */

public class SerialPrefixSum implements PrefixSum {

  /** No-arg constructor
   */
  public SerialPrefixSum() {}

  @Override
  public int[] prefixSum(int[] input)
  {
    if(input == null)
    {
      throw new NullPointerException();
    }
      for(int i = 1; i < input.length; i++)
      {
        input[i] += input[i-1];
      }
      return input;
  }
}
