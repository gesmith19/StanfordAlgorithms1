
/*
 * Week 3 programming exercise
 * kargerMinCut.txt
 * The file contains the adjacency list representation of a simple undirected graph. There are 200 vertices labeled
 * 1 to 200. The first column in the file represents the vertex label, and the particular row (other entries except
 * the first column) tells all the vertices that the vertex is adjacent to. So for example, the 6th row looks like :
 * "6	155	56	52	120	......". This just means that the vertex with label 6 is adjacent to (i.e., shares an edge
 * with) the vertices with labels 155,56,52,120,......,etc
 *
 * Your task is to code up and run the randomized contraction algorithm for the min cut problem and use it on the
 * above graph to compute the min cut. (HINT: Note that you'll have to figure out an implementation of edge
 * contractions. Initially, you might want to do this naively, creating a new graph from the old every time there's
 * an edge contraction. But you should also think about more efficient implementations.)
 * (WARNING: As per the video lectures, please make sure to run the algorithm many times with different
 * random seeds, and remember the smallest cut that you ever find.) Write your numeric answer in the space
 * provided. So e.g., if your answer is 5, just type 5 in the space provided.
 *
 *  filename         num of min cuts        cuts are
 *  test1.txt                2              [(1,7),(4,5)]
 *  test2.txt                2              [(1,7), (4,5)]
 *  test3.txt                1              [(4,5)]
 *  test4.txt                3
 *  kargerMinCut.txt         17              I ran it on 1000 trials
 */

package com.stanford.algorithm1.week3;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Random;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;


public class MinCut {

    /*
     * reads in the input graph data in the format specified at the top of the file
     * returns a hashmap that contains each vertex with a list of its linked vertices
     */
    private static HashMap<Integer, ArrayList<Integer>> readInputFile( String fname ){

        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<Integer, ArrayList<Integer>>();

        try (Scanner inputFile = new Scanner( new File( fname ))){
            while ( inputFile.hasNextLine() ) {

                // first item is the vertex
                int vertex = Integer.parseInt( inputFile.next() );

                // format the rest of the line
                String[] line = inputFile.nextLine().trim().split( "\\s+" );

                //read in the links for this vertex
                ArrayList<Integer> links = new ArrayList<Integer>();
                for (int i = 0; i < line.length; i++ ) {
                    int lnk1 = Integer.parseInt( line[i] );
                    links.add( lnk1 );
                }
                graph.put( vertex, links );
            }
            inputFile.close();
        } catch ( FileNotFoundException e) {
            e.printStackTrace();
        }
        return graph;
    }

    /*
     * print out the graph
     */
    private static void printGraph( HashMap<Integer, ArrayList<Integer>> graph ) {
        System.out.println( " printing graph data");

        for ( Map.Entry<Integer, ArrayList<Integer>> entry : graph.entrySet() ){
            Integer key = entry.getKey();
            System.out.print( " key: " + key );
            ArrayList<Integer> links = entry.getValue();

            for ( int j : links ) {
                System.out.print( " " + j );
            }
            System.out.println( " ");
        }
    }

    /*
     * copy the graph and return the copy
     */
    private static HashMap<Integer, ArrayList<Integer>> copyGraph( HashMap<Integer, ArrayList<Integer>> sourceGraph) {

        HashMap<Integer, ArrayList<Integer>> targetGraph = new HashMap<Integer, ArrayList<Integer>>();

        Iterator sourceIt = sourceGraph.keySet().iterator();

        while ( sourceIt.hasNext() ) {
            Integer key = ( Integer ) sourceIt.next();
            ArrayList<Integer> links = sourceGraph.get( key );

            targetGraph.put( key, new ArrayList<Integer>( links ) );
        }
        return targetGraph;
    }
    /*
     * kargerAlgorithm
     */
    private static int kargerAlgorithm( HashMap<Integer, ArrayList<Integer>> graph ){

        // Iterate until only 2 vertices left
        while ( graph.size() > 2 ) {
            fuse( graph );
        }

        //return the number of links between the two vertices - the minimum cut
        return graph.get( ( Integer ) graph.keySet().toArray()[0] ).size();
    }

    /*
     * selects at random a node or vertex and then selects at random a link of that node (the link
     * is itself another node).  It returns a list containing both of these values
     *
     */
    private static Integer getRandomNodeOrLinks( HashMap<Integer, ArrayList<Integer>> graph, int type, Integer node ) {

        Integer returnValue;

        if ( type == 0 ) {
            // return random node
            int nodeIndex = (int) ( Math.random() * graph.keySet().size() );
            returnValue =  ( Integer ) (graph.keySet().toArray()[nodeIndex]);
        }
        else {
            // return a random link from the parameter node
            int linkIndex = (int) ( Math.random() * graph.get( node ).size() );
            returnValue = (Integer) graph.get( node ).get( linkIndex );
        }
        return returnValue;
    }



    private static void fuse( HashMap<Integer, ArrayList<Integer>> graph ){

        // get a random node
        Integer node1 = getRandomNodeOrLinks( graph, 0, 0);
        // get a random link associated with the random node selected
        Integer node2 = getRandomNodeOrLinks( graph, 1, node1 );

        // get the links associated with the 2 selected nodes
        ArrayList<Integer> node1Links = graph.get( node1 );
        ArrayList<Integer> node2Links = graph.get( node2 );

        // add the links from node2 to node1
        node1Links.addAll( node2Links );

        // remove the the node2 entry from the graph
        graph.remove( node2 );

        // in the graph find all links to node2 and replace them with links to node1
        Iterator graphIt = graph.keySet().iterator();

        while ( graphIt.hasNext() ) {
            Integer graphKey = (Integer) graphIt.next();
            ArrayList<Integer> graphLinks = graph.get( graphKey );

            for ( Integer j : graphLinks ) {
                if ( j.intValue() == node2.intValue() ) {
                    graphLinks.set( graphLinks.indexOf( j ), node1 );
                }
            }
        } // end of while loop

        // remove self-loops
        ArrayList<Integer> linksToRemove = new ArrayList<Integer>();

        for ( Integer j : node1Links ) {
            if ( j.intValue() == node1.intValue() ) {
                linksToRemove.add( j );
            }
        }
        node1Links.removeAll( linksToRemove );

        return;
    }

    /*
     * start parameters: input filename, number of iterations to run
    */
    public static void main( String[] args ) {

        //very basic check that the start parameters are correct
        if ( args.length != 2 ){
            System.out.println(" Usage mincut <input filename> <number of iterations>" );
            System.exit( -1 );
        }

        // read the input file
        HashMap<Integer, ArrayList<Integer>> graph = readInputFile( args[0] );
        // printGraph( graph );

        // keep the results of each trial - for testing purposes
        int numOfTrials = Integer.parseInt( args[1] );

        if ( numOfTrials < 10 )
            numOfTrials = 10;

        int[] trialResults = new int[numOfTrials];

        for ( int i = 0; i < numOfTrials; i++ ) {
            // make a copy of the graph for this trial
            HashMap<Integer, ArrayList<Integer>> trialGraph = copyGraph( graph );
            // printGraph ( trialGraph );
            // i = 10;

            // store the results of each trial
            trialResults[i] = kargerAlgorithm( trialGraph );
            // printGraph( trialGraph );
        }
        Arrays.sort( trialResults );

        System.out.println( " min cut is " + trialResults[0] );
    }
}
