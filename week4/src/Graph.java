/**
 * Created by smithg08 on 18/10/2016.
 */
import java.io.*;
import java.util.*;

public class Graph {
    private ArrayList<ArrayList<Integer>> vertices; // graph
    private ArrayList<ArrayList<Integer>> reverse; // graph with all arcs reversed
    private int[] labels; // ordering
    private int[] leader; // count how many nodes are of the same leader
    private int time; // DFS finishing time
    private int source; // leader
    private boolean[] explored; // track which nodes have been explored in DFS

    /**
     * Read graph from input file.
     *
     * @param inputFileName
     * @throws FileNotFoundException
     */

    public Graph(String inputFileName) throws FileNotFoundException {
        System.out.println(" in graph constructor ");
        vertices = new ArrayList<ArrayList<Integer>>();
        reverse = new ArrayList<ArrayList<Integer>>();
        Scanner in = new Scanner(new File(inputFileName));
        //add all vertices
        while (in.hasNextInt()) {
            int tail = in.nextInt();
            int head = in.nextInt();
            int max = Math.max(tail, head);
            System.out.println(" tail: " + tail + " head: " + head + " max: " + max + " vsize: " + vertices.size());
            while (vertices.size() < max) {
                System.out.println(" in while loop ");
                vertices.add(new ArrayList<Integer>());
                reverse.add(new ArrayList<Integer>());
            }
            vertices.get(tail - 1).add(head - 1);
            reverse.get(head - 1).add(tail - 1);
            System.out.println("Added " + tail + "->" + head);
        }
        System.out.print( "\n vertices: ");
        for (int i = 0; i < vertices.size(); i++) {
            System.out.println(" i: " + i + " size " + vertices.get(i).size() );

            for (int j = 0; j < vertices.get(i).size(); j++) {
                System.out.print(i + "->" + vertices.get(i).get(j) + " ");
            }
            System.out.println();
        }
        for (int j = 0; j < vertices.size(); j++ ) {
            System.out.print(vertices.get(j));
         }
        System.out.print("\n reverse: ");
        for (int i = 0; i < reverse.size(); i++) {
            System.out.println(" i: " + i + " size " + reverse.get(i).size());

            for (int j = 0; j < reverse.get(i).size(); j++) {
                System.out.print(i + "->" + reverse.get(i).get(j) + " ");
            }
            System.out.println();
        }

        for ( int j = 0; j < reverse.size(); j++ ){
            System.out.print( reverse.get(j) );
        }
        System.out.println(" ");


    }

    /**
     * Computes SCCs.
     *
     * @return top5 an integer array of size 5, containing the sizes of the 5
     * largest SCCs in the given graph, in decreasing order of sizes.
     */

    public int[] computeSCC() {
        int[] top5 = new int[5];
        DFSLoop1();
        DFSLoop2();
        Arrays.sort(leader);
        System.out.println("leader size is: " + leader.length );
        for (int i = 0; i < leader.length; i++ ){
            System.out.print(leader[i] + " ");
        }
        System.out.println("" );
        for (int i = 0; i < 5; i++) {
            top5[i] = leader[leader.length - i - 1];
        }
        return top5;
    }

    /**
     * The first DFS loop will DFS the reversed graph and labeling each nodes
     * by the finishing time.
     */

    public void DFSLoop1() {
        time = 0;
        explored = new boolean[reverse.size()];
        labels = new int[vertices.size()];
        for (int i = reverse.size() - 1; i >= 0; i--) {
            if (explored[i] == false) {
                DFS1(i);
            }
        }
    }

    /**
     * The second DFS loop will DFS the original graph. At the beginning of
     * each loop, it will choose the largest label to begin and mark all the
     * explored nodes' leader with this label.
     */

    public void DFSLoop2() {
        explored = new boolean[vertices.size()];
        leader = new int[vertices.size()];
        for (int i = labels.length - 1; i >= 0; i--) {
            int node = labels[i];
            if (explored[node] == false) {
                source = node;
                DFS2(node);
            }
        }
    }

    /**
     * Part of the first DFS loop.
     *
     * @param node
     */

    public void DFS1(int node) {
        explored[node] = true;
        for (int head : reverse.get(node)) {
            System.out.println("DFS1:node  " + node + " head: " + head + " explored: " + explored[head]);
            if (explored[head] == false) {
                DFS1(head);
            }
        }
        labels[time] = node;
        System.out.println("labels[time]: " + labels[time] + " time: " + time );
        time++;
    }

    /**
     * Part of the second DFS loop.
     *
     * @param node
     */

    public void DFS2(int node) {
        System.out.print("DFS2: node " + node + " explored[node]: " + explored[node] + " source: " + source
                + " leader[source] " + leader[source] );
        explored[node] = true;
        leader[source]++;
        System.out.println(" after: leader[source]++: " + leader[source] );
        for (int head : vertices.get(node)) {
            System.out.println("explored[head]: " + explored[head] + " head " + head );
            if (explored[head] == false) {
                DFS2(head);
            }
        }
    }


    public void printGraph() {
        for (int i = 0; i < vertices.size(); i++) {
            System.out.println(i + ": ");
            System.out.print("outgoings: ");
            for (int j = 0; j < vertices.get(i).size(); j++) {
                System.out.print(i + "->" + vertices.get(i).get(j) + " ");
            }
            System.out.println();
            System.out.print("incommings: ");
            for (int j = 0; j < reverse.get(i).size(); j++) {
                System.out.print(i + "<-" + reverse.get(i).get(j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


    public static void main(String[] args) throws FileNotFoundException {
        Graph g = new Graph("test1.txt");
        // Graph g = new Graph("SCCInput.txt");
        int[] topSCCs = g.computeSCC();
        for (int n : topSCCs) {
            System.out.print(n + " ");


        }
        g.printGraph();

    }
}

