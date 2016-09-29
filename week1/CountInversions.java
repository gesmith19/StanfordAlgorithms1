//
// Week 1 Programming Assignment - variant of sort merge
// Download IntegerArray.txt
// This file contains all of the 100,000 integers between 1 and 100,000 (inclusive) in some order, with no integer 
// repeated.
//
// Your task is to compute the number of inversions in the file given, where the ith row of the file indicates 
// the ith entry of an array.
//
// Because of the large size of this array, you should implement the fast divide-and-conquer algorithm covered in 
// the video lectures.
//
//
// IntegerArray.txt has 2407905288 inversions.

package week1algorithmn1;
import java.io.*;
import java.util.*;

public class CountInversions {
	
	private static boolean debug = false;

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

	//
    // parameters:  1 - inputfile name (mandatory)
	//              2 - debug or not (optional)
	//
	public static void main( String[] args )   
	{
    	long result;
    	
		if ( args.length  > 1 )
			debug = true;
		
    	int [] input = readInputFile( args[0] );
    	if ( input.length > 0 )
    	{
    		result = sortAndCount( input );
    	    System.out.println ( args[0] + " has " + result + " inversions." );
    	}
    	else
    	{
    		System.out.println( "invalid input" );
    	}
	}

	public static long sortAndCount ( int source[] )
	{  
	   if ( debug )
		   System.out.println( " sortAndCount: source: " + Arrays.toString( source ) );
	             
       
	   int mid = source.length/2; 
	   int i = 0;
	   long countLeft, countRight, countMerge;
	            
	   /*  base case  */ 
	   if ( source.length <= 1 ) 
	      return 0;
	             
       //  Split the list into 2 and copy contents 
	               
	   int left[] = new int[mid];
	   int right[] = new int[source.length - mid];

	   for ( i = 0; i < mid; i++ )
	       left[i] = source[i];
	   for ( i = 0; i < source.length - mid; i++ )
	       right[i] = source[mid+i];

	   // Recursively count the inversions in each part. 
	           
	   countLeft = sortAndCount ( left );
	   countRight = sortAndCount ( right );

	   //  merge and count the inversions
	         
	   int result[] = new int[source.length];
	   countMerge = mergeAndCountInversions( left, right, result );
	             
	   if ( debug )
	   {
	       System.out.println( " result: " + Arrays.toString( result ) );
	   }
	  
	   //  copy list back to original array - needed for recursion to work
	          
	   for ( i = 0; i < source.length; i++ )
	       source[i] = result[i];

	   if ( debug )
	   {
	       System.out.println( "left: " + countLeft + " right: " + countRight + " merge: " 
	       + countMerge ); 
	   }
	   
	   // return # of inversions
	   return ( countLeft + countRight + countMerge );  

    } // end


	public static long mergeAndCountInversions ( int left[], int right[], int result[] )
	// Merge the two lists, and count the number of inversions   
	{
	    if ( debug )
	    {
	         System.out.println("\nmergeAndCountInversions\nleft: " + Arrays.toString( left ) );
	         System.out.println(" right: " + Arrays.toString( right ) );
	         System.out.println( "left length: " + left.length + " right length: " + right.length );
	    }
	    
	    // counters

	    int leftIndex = 0;
	    int rightIndex = 0;
	    long count = 0;
	    int i = 0; 
	    int k = 0;

	    while ( ( leftIndex < left.length ) && ( rightIndex < right.length ) )
	    {
	    	if ( debug )
	    	{
	    		System.out.println( "leftIndex: " + leftIndex + " rightIndex: " + rightIndex );
	    	}
            if ( left[leftIndex] <= right[rightIndex] )
	        {
	             result [k] = left[leftIndex];
	             leftIndex++;
	         }
	         else       
	         {
	             // inversion found
	             result [k] = right[rightIndex];
	             rightIndex++;
	                     
	             if ( debug )
	             {
	                System.out.println( "count: " + count + " leftIndex " + leftIndex );
	             }
	                     
	             count += left.length - leftIndex;  // this accounts for the split inversions
	          }
	          k++;
	                 
	          if ( debug )
	          {
	              System.out.println( "count now: " + count );
	        	  System.out.println( "result: "+ Arrays.toString( result ) );
	          }

	       } //end of while loop
	    
	       // copy any entries from the left array that have not been merged
	       if ( leftIndex < left.length )
	       {
	    	  for ( i = leftIndex; i < left.length; i++ )
	    	  {
	    		  result[k] = left[i];
	    		  k++;
	    	  }
	       }
	       
	       // copy any entries from the right array that have not been merged
	       if ( rightIndex < right.length )
	       {
	    	   for ( i = rightIndex; i < right.length; i++ )
	    	   {
	    		   result[k] = right[i];
	    		   k++;
	    	   }
	       }
	      
	       if ( debug )
              System.out.println( "count: " + count );

	        return count;
	    } // end of mergeAndCount
	
} // end of CountInversions

