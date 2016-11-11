
 * /******************************************************************************
 * Programming Assignment 6 question 2
 *
 * Download the following text file:
 * Median.txt
 *
 * The goal of this problem is to implement the "Median Maintenance" algorithm
 * (covered in the Week 5 lecture on heap applications). The text file contains
 * a list of the integers from 1 to 10000 in unsorted order; you should treat
 * this as a stream of numbers, arriving one by one. Letting xi denote the ith
 * number of the file, the kth median mk is defined as the median of the numbers
 * x1,…,xk. (So, if k is odd, then mk is ((k+1)/2)th smallest number among x1,…,xk;
 * if k is even, then mk is the (k/2)th smallest number among x1,…,xk.)
 *
 * In the box below you should type the sum of these 10000 medians, modulo 10000
 * (i.e., only the last 4 digits). That is, you should compute
 * (m1+m2+m3+...+m10000)mod10000.
 *
 * OPTIONAL EXERCISE: Compare the performance achieved by heap-based and
 * search-tree-based implementations of the algorithm.
 *
 *****************************************************************************/
 /*
 * Created by Gillian Smith on 02/11/2016.
 *  For Medians.txt  the assignment Answer 1213
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

public class RunningMedians {

    static int N = 10000;
    private PriorityQueue<Integer> lowMaxHeap;
    private PriorityQueue<Integer> highMinHeap;


    public RunningMedians(){
        highMinHeap = new PriorityQueue<Integer>();  // head of queue will be smallest entry
        lowMaxHeap = new PriorityQueue<Integer>(Collections.reverseOrder());  // head of queue will be largest entry

    }

    public int calcMedians( String fname ) {

        try ( Scanner input = new Scanner( new File( fname ) ) ) {

            int x = Integer.parseInt( input.next() );
            int currentMedian = x;
            int total = x;

            // add first entry to low number heap which is ordered largest-to-smallest numbers
            lowMaxHeap.add( x );

            // read in an integer at a time
            while ( input.hasNext() ) {
                x = Integer.parseInt( input.next() );
                int maxLow = lowMaxHeap.peek();


                // add to the lownumbers heap if less than its largest value
                // if not add to the highnumbers heap
                if ( x < maxLow ) {
                    lowMaxHeap.add( x );
                }
                else {
                    highMinHeap.add( x );
                }

                // balance the 2 heaps. They can differ in size by 1. If more than 1, then move an element
                // from one heap to another

                if ( lowMaxHeap.size() - highMinHeap.size() > 1 ) {
                    highMinHeap.add( lowMaxHeap.poll() );
                }
                else if ( lowMaxHeap.size() - highMinHeap.size() < -1 ) {
                    lowMaxHeap.add( highMinHeap.poll() );
                }

                // calculate the current median and then add it to the running total
                // if odd number of numbers read (k), the median is (k + 1)/2 smallest number
                // if even number of numbers read (k), the median is k/2 smallest number

                if ( lowMaxHeap.size() == highMinHeap.size() ) {
                    currentMedian = lowMaxHeap.peek();
                }
                else
                {
                    currentMedian = (lowMaxHeap.size() > highMinHeap.size() ? lowMaxHeap.peek() : highMinHeap.peek() );
                }

                total += currentMedian;

            }
            input.close();
            return total;
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();

        }
        return -1;

    }

    public static void main( String[] args ) {

        RunningMedians rm = new RunningMedians();

        int total  = rm.calcMedians ( "Median.txt" );

        System.out.println( " total: " + total + " answer: " + total % N );
    }


}
