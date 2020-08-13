package edu.yu.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/** Implements PrefixSum interface using two-pass fork-join algorithm
 */
public class ParallelPrefixSum implements PrefixSum {
    private static final ForkJoinPool myPool = ForkJoinPool.commonPool();
    int processes = Runtime.getRuntime().availableProcessors();
    /** No-arg constructor.
     */

    public ParallelPrefixSum() {}

    class Node {
        int value;
        int fromLeft;
        int rangeStart;
        int rangeEnd;
        Node left;
        Node right;

        Node(int rangeStart, int rangeEnd) {
            right = null;
            left = null;
            this.rangeStart = rangeStart;
            this.rangeEnd = rangeEnd;
        }
    }

    class FirstPassExecutor extends RecursiveTask<Integer>
    {
        int[] array;
        Node node;
        int processors;
        int cutoff;
        public FirstPassExecutor(int [] array, Node node, int processors)
        {
            this.array = array;
            this.node = node;
            this.processors = processors;
            this.cutoff = array.length/processors;
        }

        @Override
        protected Integer compute()
        {
            if(node.rangeEnd - node.rangeStart <= cutoff)
            {
                int sum = 0;
                for(int i = node.rangeStart; i < node.rangeEnd; i++)
                {
                    sum += array[i];
                }
                node.value = sum;
                return sum;
            }
            else
            {
                int mid = node.rangeStart + (node.rangeEnd - node.rangeStart)/2;
                Node left = new Node(node.rangeStart, mid);
                Node right = new Node(mid, node.rangeEnd);
                node.left = left;
                node.right = right;
                FirstPassExecutor leftExec = new FirstPassExecutor(array, left, processors);
                FirstPassExecutor rightExec = new FirstPassExecutor(array, right, processors);
                leftExec.fork();
                int rightResult = rightExec.compute();
                int leftResult = leftExec.join();
                int total = leftResult + rightResult;
                node.value = total;
                return total;
            }
        }
    }
    class SecondPassExecutor extends RecursiveAction
    {
        int[] array;
        Node head;
        public SecondPassExecutor (int [] array, Node head)
        {
            this.array = array;
            this.head = head;
        }

        @Override
        protected void compute()
        {
            if(head.left == null)
            {
                array[head.rangeStart] += head.fromLeft;
                for(int i = head.rangeStart + 1; i < head.rangeEnd; i++)
                {
                    array[i] += array[i-1];
                }
            }
            else
            {
                int mid = head.rangeStart + (head.rangeEnd - head.rangeStart)/2;
                head.left.fromLeft = head.fromLeft;
                head.right.fromLeft = head.fromLeft + head.left.value;
                SecondPassExecutor left = new SecondPassExecutor(array, head.left);
                SecondPassExecutor right = new SecondPassExecutor(array, head.right);
                ForkJoinTask.invokeAll(left, right);
            }
        }
    }



    @Override
    public int[] prefixSum(int[] input)
    {

        Node head = new Node(0, input.length);
        FirstPassExecutor firstTask = new FirstPassExecutor(input, head, processes);
        myPool.invoke(firstTask);
        SecondPassExecutor secondTask = new SecondPassExecutor(input, head);
        myPool.invoke(secondTask);
        return input;
    }
}
