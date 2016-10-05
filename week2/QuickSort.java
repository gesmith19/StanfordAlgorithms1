/*
 * Week 2 Programming Question
 * 
 * Download the following text file:QuickSort.txt
 * 
 * The file contains all of the integers between 1 and 10,000 (inclusive, with no repeats) in unsorted order. 
 * The integer in the ith row of the file gives you the ith entry of an input array.
 * 
 * Your task is to compute the total number of comparisons used to sort the given input file by QuickSort. 
 * As you know, the number of comparisons depends on which elements are chosen as pivots, so we'll ask you to 
 * explore three different pivoting rules.
 * 
 * You should not count comparisons one-by-one. Rather, when there is a recursive call on a subarray of length m, 
 * you should simply add m−1 to your running total of comparisons. (This is because the pivot element is compared 
 * to each of the other m−1 elements in the subarray in this recursive call.)
 *
 * WARNING: The Partition subroutine can be implemented in several different ways, and different implementations 
 * can give you differing numbers of comparisons. For this problem, you should implement the Partition subroutine 
 * exactly as it is described in the video lectures (otherwise you might get the wrong answer).
 * 
 * 3 Questions
 * 
 * Question 1: use first element in the array
 * Question 2: use the final element in the array
 * Question 3: Compute the number of comparisons (as in Problem 1), using the "median-of-three" pivot rule. 
 * [The primary motivation behind this rule is to do a little bit of extra work to get much better performance on 
 * input arrays that are nearly sorted or reverse sorted.] In more detail, you should choose the pivot as follows. 
 * Consider the first, middle, and final elements of the given array. (If the array has odd length it should be 
 * clear what the "middle" element is; for an array with even length 2k, use the kth element as the "middle" 
 * element. So for the array 4 5 6 7, the "middle" element is the second one ---- 5 and not 6!) Identify which of 
 * these three elements is the median (i.e., the one whose value is in between the other two), and use this as your 
 * pivot. As discussed in the first and second parts of this programming assignment, be sure to implement Partition 
 * exactly as described in the video lectures (including exchanging the pivot element with the first element just 
 * before the main Partition subroutine).
 *
 * EXAMPLE: For the input array 8 2 4 5 7 1 you would consider the first (8), middle (4), and last (1) elements; 
 * since 4 is the median of the set {1,4,8}, you would use 4 as your pivot element.
 *
 * SUBTLE POINT: A careful analysis would keep track of the comparisons made in identifying the median of the 
 * three candidate elements. You should NOT do this. That is, as in the previous two problems, you should simply 
 * add m−1 to your running total of comparisons every time you recurse on a subarray with length m.
 * 
 * Test files and answers
 * 
 * Name			first		final		median
 * 10.txt		25			29			21
 * 100.txt		615         587			518
 * 1000.txt		10297		10184		8921
 * 
 * Assignment answers
 * QuickSort.txt 162085     164123		138382
 */

package quicksort;

import java.io.*;
import java.util.*;

public class QuickSort {
	private static boolean debug = false;
	private static int comparisons = 0;

	private static int[] readInputFile( String name ) 
	{
		try ( Scanner inputFile = new Scanner( new File( name ) ) ) 
		{
			List<Integer> source = new ArrayList<Integer>();
	    	while ( inputFile.hasNextInt() ) 
	    	{
	    		source.add( inputFile.nextInt() );
	    	}
	    	inputFile.close();
	    	
	    	if ( debug )
	    	{
	    	    System.out.println(" list is: " + source);
	    	}
	    	return convertListToIntegers( source );
		} catch ( FileNotFoundException e ) 
		{
			e.printStackTrace();
			return null;
		}
	} // end of readInputFile
	
	private static int[] convertListToIntegers(List<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    for ( int i = 0; i < ret.length; i++ )
	    {
	        ret[i] = iterator.next().intValue();
	    }
	   
	    return ret;
	} // return convertListToIntegers

	/*
	 * parameters:  1 - inputfile name (mandatory)
	 *              2 - pivot option
	 *                  1 = pivot is first element
	 *                  2 = pivot is last element
	 *                  3 = pivot is median-of-three pivot
	 */
	
	public static void main(String[] args)   
	{
    	int result;
    	boolean flag = false;
    	
		// if ( args.length  > 1 )
		//	debug = true;
		
		int pivotType = 0;
		
		if ( args.length == 2 ) {
			pivotType = Integer.parseInt( args[1] );
		}
		
		if (pivotType > 0 && pivotType < 4) {
			flag = true;
		}
    	int [] input = readInputFile( args[0] );
    	if ( input.length > 0  && flag )
    	{
    		result = quicksort( input, pivotType );
    	    System.out.println ( args[0] + " made " + result + " comparisons.");
    	    System.out.println( "comparisons: " + comparisons );
    	    // System.out.println( "sorted list: " + Arrays.toString( input ) );
    	}
    	else
    	{
    		// System.out.println( "invalid input: " + args[0] + " " + args[1] );
    		System.out.println( "invalid input" );
    	}
	} // end of main
	
	/*
	 * 
	 */
	public static int quicksort( int[] arr, int pivotType ) { 
		
		int result = quicksort( arr, 0, arr.length - 1, pivotType );
		return result;
		
	}
	
	
	private static int quicksort( int[] arr, int startIndex, int endIndex, int pivotOption ) {
		
		if ( debug ) {
			System.out.println( " quicksort: array: " + Arrays.toString( arr ) );
			System.out.println( "quicksort: startIndex " + startIndex + " endIndex " + endIndex );
		}
	
		// base case
		if ( startIndex >= endIndex ) {
			return 0;
		}
		
		//work out if using first element, last element or median element as the the pivot
		int pivotType = choosePivot( arr, startIndex, endIndex, pivotOption );
		
		// get the correct index of the array given the pivot type
		int pivotIndex = partition( arr, startIndex, endIndex, pivotType );
		
		// count the comparisons (in two ways!) 
		int count = endIndex - startIndex;
		comparisons = comparisons + endIndex - startIndex;
		
		if ( debug ) {
			System.out.println( "pivotIndex: " + pivotIndex );
			System.out.println( "count: " + count + " comparisons: " + comparisons);
		}
		
		// sort the lefthand side of the array - from beginning to right most entry below the pivot
		int left = quicksort( arr, startIndex, pivotIndex - 1, pivotOption );
		// sort the righthand side of the array - from the entry just above the pivot to the end of the array
		int right = quicksort( arr, pivotIndex+1, endIndex, pivotOption );
				
		//return the comparison count
		return count + left + right;
		
	} // end of quicksort which is called recursively
	
	/*
	 * return the correct index into the array given the pivot option selected
	 * option can be 1 for the start index, 2 for the end index and 3 for the median index
	 */
	private static int choosePivot( int[] arr, int startIndex, int endIndex, int option ) {
		
		int pivotPosition = startIndex;
		if ( arr.length < 3)
		{
			pivotPosition = startIndex;
		}
		else
		{
		
			switch ( option ) {
			case 1: // first element
				pivotPosition = startIndex;
				break;
			case 2: // last element
				pivotPosition = endIndex;
				break;
			case 3: // median element
				pivotPosition = calculateMedian( arr, startIndex, endIndex );
				break;
			default:
				break;
			}	
		}
		return pivotPosition;
	} // end of choosePivot
	
	/*
	 *  Find the middle index given the lowend and highend indexes
	 *  Find the middle value of these 3 values and return the index of that value
	 */
	private static int calculateMedian( int[] arr, int low, int high ){
		
		int mid =  (low + high) / 2;
		//if ( arr[low] > arr[mid] )
		//	swap( arr, low, mid);
		//if ( arr[low] > arr[high] )
		//	swap( arr, low, high );
		//if ( arr[mid] > arr[high] )
		 //  swap( arr, mid, high );
		// return mid
		
		if ( debug ) {
			System.out.println("low, mid, high " + low + " " + mid + " " + high );
			System.out.println( arr[low] + " " + arr[mid] + " " + arr[high] );
		}
		
		if (arr[low] > arr[mid]) 
		{
			  if (arr[mid] > arr[high]) 
			  {
			     // System.out.println( "mid is the middle value");
			     return mid;
			  }  else if (arr[low] > arr[high]) 
			  {
			    // System.out.println( "high is the middle value" );
			    return high;
			  } else 
			  {
			    // System.out.println( "low is the middle value" );
			    return low;
			  }
		}
		else 
		{
			  if (arr[low] > arr[high]) 
			  {
			     // System.out.println( "low is the middle value" );
			     return low;
			  } else if (arr [mid] > arr [high]) 
			  {
			    // System.out.println( "high is the middle value" );
			    return high;
			  } 
			  else 
			  {
			    // System.out.println(  "mid is the middle value" );
			    return mid;
			  }
		}
	    
	}
	
	/*
	 * partitions array according to an arbitrary pivot index
	 * arr - an array of integers to be partitions
	 * startIndex - the start of the subset of the array to be partitioned
	 * endIndex - the end of the subset of the array to be partitioned
	 * returns index of pivot element
	 */
     private static int partition( int[] arr, int startIndex, int endIndex, int pivotIndex ) {
    	 
    	 if ( debug ) {
    		 System.out.println( "\npartition: start: " + startIndex + " end: " + endIndex + " pivot: " + pivotIndex);
    	 }
    	 // swap the first element of the array with pivot, so the pivot is the first element
    	 swap( arr, startIndex, pivotIndex );
    	 
    	 // border for elements <= pivot starts at startIndex + 1
    	 int i = startIndex + 1;
  
    	 int pivot = arr[startIndex];
    	 
    	 // step through array partitioning the array into elements which are < than pivot and
    	 // elements which are > pivot
    	 
    	 for ( int j = startIndex + 1; j <= endIndex; j++ ) {
    		 if ( arr[j] < pivot ) {
    			 swap( arr, j, i);
    			 i++;   // increment the border
       		 } 		 
    	 }
    	 
    	 // swap the pivot and the i-1 index to finish partition.  Pivot now in correct place in the array
    	 swap( arr, startIndex, i - 1 );
    	 
    	 // return the new index of the pivot
    	 return i - 1;
     } // end of partition
     
     private static void swap( int[] arr, int first, int second ) {
    	 int  temp = arr[first];
    	 arr[first] = arr[second];
    	 arr[second] = temp;
     } // end of swap

} // end of QuickSort class