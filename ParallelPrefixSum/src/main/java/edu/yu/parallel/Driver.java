package edu.yu.parallel;

import java.util.Arrays;
import java.util.Random;

public class Driver
{
    public static void main(String[] args)
    {
        Driver drive = new Driver();
        drive.meVersusJDK();
        drive.testCorrectness();
    }
    public void meVersusJDK()
    {
        long jdkavg = 0;
        long serialavg = 0;
        long meavg = 0;

        for(int i = 0; i < 30; i++)
        {
            int[] numbers = new int[500000000];

            Random random = new Random();
            for(int x = 0; x < numbers.length; x++)
            {
                numbers[x] = random.nextInt();
            }
            int[] numbersForMe = Arrays.copyOf(numbers, numbers.length);
            int[] numbersForJDK = Arrays.copyOf(numbers, numbers.length);
            int[] numbersForSerial = Arrays.copyOf(numbers, numbers.length);
            ParallelPrefixSum mySum = new ParallelPrefixSum();
            JDKParallelPrefixSum jdkSum = new JDKParallelPrefixSum();
            SerialPrefixSum serialSum = new SerialPrefixSum();
            long meStart = System.nanoTime();
            numbersForMe = mySum.prefixSum(numbersForMe);
            long meEnd = System.nanoTime();
            meavg += meEnd-meStart;
            long jdkStart = System.nanoTime();
            numbersForJDK = jdkSum.prefixSum(numbersForJDK);
            long jdkEnd = System.nanoTime();
            jdkavg += jdkEnd-jdkStart;
            long serialStart = System.nanoTime();
            numbersForSerial = serialSum.prefixSum(numbersForSerial);
            long serialEnd = System.nanoTime();
            serialavg += serialEnd-serialStart;
        }
        jdkavg = jdkavg/20;
        serialavg = serialavg/20;
        meavg = meavg/20;
        System.out.println("test done on array of random ints of size five hundred million.");
        System.out.println("ParallelPrefix time: "+(meavg)+" JDK time: "+(jdkavg)+ " Serial Time: "+(serialavg));
        System.out.println("jdk time over my time = "+(double)(jdkavg)/(meavg));
        System.out.println("my time over serial time = "+(double)(meavg)/(serialavg));
        System.out.println("jdk time over serial time = "+(double)(jdkavg)/(serialavg));
    }
    public void testCorrectness()
    {
        int[] numbers = new int[1000000];

        Random random = new Random();
        for(int x = 0; x < numbers.length; x++)
        {
            numbers[x] = random.nextInt();
        }
        int[] numbersForMe = Arrays.copyOf(numbers, numbers.length);
        int[] numbersForJDK = Arrays.copyOf(numbers, numbers.length);
        int[] numbersForSerial = Arrays.copyOf(numbers, numbers.length);
        SerialPrefixSum serial = new SerialPrefixSum();
        ParallelPrefixSum parallel = new ParallelPrefixSum();
        JDKParallelPrefixSum jdk = new JDKParallelPrefixSum();
        numbersForJDK = jdk.prefixSum(numbersForJDK);
        numbersForMe = parallel.prefixSum(numbersForMe);
        numbersForSerial = serial.prefixSum(numbersForSerial);
        System.out.println("Making sure all versions actually have same result :" + (Arrays.equals(numbersForJDK, numbersForMe) && Arrays.equals(numbersForMe, numbersForSerial)));
    }
}
