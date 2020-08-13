# ParallelPrefixSum
Computes a prefix sum from an array using several approaches, for runtime comparison.

**JDKParallelPrefixSum**
Uses Arrays.parallelPrefixSum method.

**ParallelPrefixSum**
Uses a ForkJoinPool and two nested classes, one extending RecursiveTask and one extending RecursiveAction, to compute the prefix sum. 

**SerialPrefixSum**
Computes the prefix sum linearly.

**ParallelPrefixSum**
Specifies the result of comparing the runtimes of these approaches on the same machine, using the Driver class.
