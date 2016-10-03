



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

    // parameters:  1 - inputfile name (mandatory)
	//              2 - debug or not (optional)
	public static void main(String[] args)   
	{
    	int result;
    	
		if ( args.length  > 1 )
			debug = true;
		
    	int [] input = readInputFile( args[0] );
    	if ( input.length > 0 )
    	{
    		result = quicksort( input, 0, input.length - 1 );
    	    System.out.println ( args[0] + " made " + result + " comparisons.");
    	}
    	else
    	{
    		System.out.println( "invalid input" );
    	}
	}
	
	public static int quicksort( int[] arr, int startIndex, int endIndex ) {
	
		int count = 0;
		
		
		
		return count;
		
	}
	
	/*
	 * partitions array according to an arbitrary pivot index
	 * arr - an array of integers to be partitions
	 * startIndex - the start of the subset of the array to be partitioned
	 * endIndex - the end of the subset of the array to be partitioned
	 * returns index of pivot element
	 */
     private static int partition( int[] arr, int startIndex, int endIndex, int pivotIndex ) {
    	 
    	 // swap the first element of the array with pivot, so the pivot is the first element
    	 swap( arr, startIndex, pivotIndex );
    	 
    	 // border for elements <= pivot starts at startIndex + 1
    	 int i = startIndex + 1;
    	 
    	 int pivot = arr[pivotIndex];
    	 
    	 // step through array partitioning the array into elements which are < than pivot and
    	 // elements which are > pivot
    	 
    	 for ( int j = startIndex + 1; j <= endIndex; j++ ) {
    		 if ( arr[j] < pivot ) {
    			 swap( arr, j, i);
    			 i++;   // increment the border
       		 } 		 
    	 }
    	 
    	 // swap the pivot and the i-1 index to finish partition
    	 swap( arr, startIndex, i - 1 );
    	 
    	 // return the new index of the pivot
    	 return i - 1;
     }
     
     private static void swap( int[] arr, int first, int second ) {
    	 int  temp = arr[first];
    	 arr[first] = arr[second];
    	 arr[second] = temp;
     }

}
