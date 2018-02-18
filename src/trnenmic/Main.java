package trnenmic;

import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * MINIMAL SPANNING TREE WITH ADDITIONAL RULES
 * You are given a graph. Find the graph that meets these criteria:
 * 1) The graph has to be a tree.
 * 2) The graph should contain maximum possible number of connections of some particular length,
 * regardless of the cost associated with those connections.
 * 3) The total length of a graph respecting the first and the second rule should be minimized.
 *
 * @author Michal Trněný
 */

public class Main {

    private static final String PATH = "." + File.separator + "data" + File.separator + "in" + File.separator;
    private static final String FILE_NAME = "pub09.in";
    private static final boolean USE_DATA_FILE = true;
    private static final boolean MEASURE_TIME = true;

    private BufferedReader reader;

    private long result;
    private int maximizedEdgeCount;

    private IntIntMap map;

    private int groupIteration;
    private int groupRealEdgeCount;
    private int groupTheoreticalEdgeCount;
    private long groupTotalPrice;
    private int groupSinglePrice;

    private int[] nodeId;
    private int[] size;
    private int[][] edges;
    private int[][] frequencies;

    private int N_NODES;
    private int M_EDGES;


    private static double start;

    public static void main(String[] args) throws IOException {
        start = System.nanoTime();
        Main main = new Main();
        main.start();
        if (MEASURE_TIME) {
            System.out.println("Time: " + (System.nanoTime() - start) / 1000000000);
        }
    }

    private void start() throws IOException {
        if (USE_DATA_FILE) {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH + FILE_NAME)));
        } else {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
        String line = reader.readLine();
        StringTokenizer tok = new StringTokenizer(line);
        N_NODES = Integer.parseInt(tok.nextToken());
        M_EDGES = Integer.parseInt(tok.nextToken());

        initDataStructure();

        sortByCost();
        sortByFrequencies();

        search();

        //Printing result
        System.out.println(result);
    }

    private void printCosts() {
        for (int i = 0; i < M_EDGES; i++) {
            System.out.println("EG|FROM: " + edges[i][0] + " TO: " + edges[i][1] + " PRICE: " + edges[i][2]);
        }
    }

    private void printFrequencies() {
        for (int i = 0; i < M_EDGES; i++) {
            System.out.println("FR|FROM: " + frequencies[i][0] + " TO: " + frequencies[i][1] + " PRICE: " + frequencies[i][2]
                    + " COUNT: " + frequencies[i][3]);
        }
    }

    private void printArray(int[] array) {
        for (int i : array) {
            System.out.println(array[i]);
        }
    }

    private void initDataStructure() throws IOException {
        nodeId = new int[N_NODES];
        size = new int[N_NODES];
        for (int i = 0; i < N_NODES; i++) {
            nodeId[i] = i;
            size[i] = 1;
        }

        map = new IntIntMap(100 + (M_EDGES / 2));

        frequencies = new int[M_EDGES][4];
        edges = new int[M_EDGES][3];

        for (int i = 0; i < M_EDGES; i++) {
            String line = reader.readLine();
            StringTokenizer tok = new StringTokenizer(line);
            edges[i][0] = Integer.parseInt(tok.nextToken()) - 1;
            edges[i][1] = Integer.parseInt(tok.nextToken()) - 1;
            edges[i][2] = Integer.parseInt(tok.nextToken());
            frequencies[i][0] = edges[i][0];
            frequencies[i][1] = edges[i][1];
            frequencies[i][2] = edges[i][2];

            //Hash of groups
            int freq = map.get(edges[i][2]);
            if (freq == Integer.MIN_VALUE) {
                freq = 1;
                map.put(edges[i][2], freq);
            } else {
                map.put(edges[i][2], (freq + 1));
            }
        }

        for (int i = 0; i < M_EDGES; i++) {
            //adding frequencies length
            frequencies[i][3] = map.get(frequencies[i][2]);
        }

        maximizedEdgeCount = 0;
    }

    private void search() {
        result = Long.MAX_VALUE;
        groupIteration = 0;
        while (groupIteration < M_EDGES) {
            groupSinglePrice = frequencies[groupIteration][2];
            groupTheoreticalEdgeCount = frequencies[groupIteration][3];

            if (maximizedEdgeCount > groupTheoreticalEdgeCount) {
                break;
            }

            prepareGraph();
            if (groupRealEdgeCount > maximizedEdgeCount) {
                maximizedEdgeCount = groupRealEdgeCount;
                result = Long.MAX_VALUE;
                searchIteration();
            } else if (groupRealEdgeCount == maximizedEdgeCount) {
                searchIteration();
            }
            resetGraph();
        }

    }

    private void prepareGraph() {
        groupRealEdgeCount = 0;
        groupTotalPrice = 0;
        int i = 0;
        while (i < groupTheoreticalEdgeCount) {
            if (maximizedEdgeCount == groupTheoreticalEdgeCount && groupTotalPrice > result) {
                i = groupTheoreticalEdgeCount;
                break;
            }
            boolean isSameGroup = find(frequencies[groupIteration + i][0], frequencies[groupIteration + i][1]);
            if (!isSameGroup) {
                unite(frequencies[groupIteration + i][0], frequencies[groupIteration + i][1]);
                groupRealEdgeCount++;
                groupTotalPrice += groupSinglePrice;
            }
            i++;
        }
        groupIteration += i;
    }

    private void resetGraph() {
        for (int i = 0; i < N_NODES; i++) {
            nodeId[i] = i;
            size[i] = 1;
        }
    }

    private void searchIteration() {
        long tmpResult = groupTotalPrice;
        int i = 0;
        while (i < M_EDGES) {
            if (edges[i][2] == groupSinglePrice) { //Skip already included edges
                i++;
                continue;
            }
            boolean isSameGroup = find(edges[i][0], edges[i][1]);
            if (!isSameGroup) {
                unite(edges[i][0], edges[i][1]);
                //Calculate result
                tmpResult += edges[i][2];
                if (tmpResult > result) { //Compare with the so far lowest found result
                    return;
                }
            }
            i++;
        }
        result = tmpResult;
    }

    private int root(int i) {
        while (i != nodeId[i]) {
            i = nodeId[i];
        }
        return i;
    }

    private boolean find(int b, int a) {
        return root(b) == root(a);
    }

    private void unite(int b, int a) {
        int j = root(a);
        int i = root(b);
        if (size[i] < size[j]) {
            nodeId[i] = j;
            size[j] += size[i];
        } else {
            nodeId[j] = i;
            size[i] += size[j];
        }
    }

    private void sortByCost() {
        Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));
    }

    private void sortByFrequencies() {
        Arrays.sort(frequencies, (a, b) -> {
            int result = -Integer.compare(a[3], b[3]);
            if (result == 0) {
                result = Integer.compare(a[2], b[2]);
            }
            return result;
        });
    }

}
