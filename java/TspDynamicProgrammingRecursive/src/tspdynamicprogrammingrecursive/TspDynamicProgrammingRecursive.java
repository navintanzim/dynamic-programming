
package tspdynamicprogrammingrecursive;
import java.util.*;

public class TspDynamicProgrammingRecursive {


   private final int N;
  private final int START_NODE;
  private final int FINISHED_STATE;

  private double[][] distance;
  private double minTourCost = Double.POSITIVE_INFINITY;

  private List<Integer> tour = new ArrayList<>();
  private boolean ranSolver = false;

  public TspDynamicProgrammingRecursive(double[][] distance) {
    this(0, distance);
  }

  public TspDynamicProgrammingRecursive(int startNode, double[][] distance) {
    
    this.distance = distance;
    N = distance.length;
    START_NODE = startNode;
    
    // Validate inputs.
    if (N <= 2) throw new IllegalStateException("TSP on 0, 1 or 2 nodes doesn't make sense.");
    if (N != distance[0].length) throw new IllegalArgumentException("Matrix must be square (N x N)");
    if (START_NODE < 0 || START_NODE >= N) throw new IllegalArgumentException("Starting node must be: 0 <= startNode < N");
    if (N > 32) throw new IllegalArgumentException("Matrix too large! A matrix that size for the DP TSP problem with a time complexity of" +
                                                   "O(n^2*2^n) requires way too much computation for any modern home computer to handle");

    
    FINISHED_STATE = (1 << N) - 1;// all nodes have been visited
  }

  
  public List<Integer> getTour() {
    if (!ranSolver) solve();
    return tour; // optimal tour
  }

  
  public double getTourCost() {
    if (!ranSolver) solve();
    return minTourCost; //minimum tour
  }

  public void solve() {   // the solver
      
    int state = 1 << START_NODE;
    Double[][] memo = new Double[N][1 << N];
    Integer[][] prev = new Integer[N][1 << N];
    minTourCost = tsp(START_NODE, state, memo, prev);
    
    int index = START_NODE;
    while (true) {
      tour.add(index);
      Integer nextIndex = prev[index][state];
      if (nextIndex == null) break;
      int nextState = state | (1 << nextIndex);
      state = nextState;
      index = nextIndex;
    }
    tour.add(START_NODE);
    ranSolver = true;
  }

  private double tsp(int i, int state, Double[][] memo, Integer[][] prev) {
    
    
    if (state == FINISHED_STATE) return distance[i][START_NODE]; // 1 tour over. return to first city
    
    if (memo[i][state] != null) return memo[i][state];
    
    double minCost = Double.POSITIVE_INFINITY;
    int index = -1;
    for (int next = 0; next < N; next++) {
      
      
      if ((state & (1 << next)) != 0) continue;  // Skip tour if the next node has been visited.
      
      int nextState = state | (1 << next);
      double newCost = distance[i][next] + tsp(next, nextState, memo, prev);
      if (newCost < minCost) {
        minCost = newCost;
        index = next;
      }
    }
    
    prev[i][state] = index;
    return memo[i][state] = minCost;
  }

  
  public static void main(String[] args) {
long startTime, endTime;
startTime = System.currentTimeMillis();
    // adjacency matrix
    int n = 5;
    double[][] distanceMatrix = new double[n][n];
    for (double[] row : distanceMatrix) java.util.Arrays.fill(row, 10000);
    distanceMatrix[0][2] = distanceMatrix[2][0] = 5;
     distanceMatrix[0][1] = distanceMatrix[1][0] = 3; 
    distanceMatrix[1][4] = distanceMatrix[4][1] = 2;
    distanceMatrix[4][2] = distanceMatrix[2][4] = 4;
    distanceMatrix[2][3] = distanceMatrix[3][2] = 6;
    distanceMatrix[3][0] = distanceMatrix[0][3] = 8;





   
    TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive(distanceMatrix);

 
    System.out.println("Tour: " + solver.getTour());

    
    System.out.println("Tour cost: " + solver.getTourCost());
     endTime = System.currentTimeMillis(); 

System.out.println("It took " + (endTime - startTime) + " milliseconds"); 
double currentMemory = ( (double)((double)(Runtime.getRuntime().totalMemory()/1024)/1024))- ((double)((double)(Runtime.getRuntime().freeMemory()/1024)/1024));
System.out.println("It took " + currentMemory + " mbs");

  }
   
}






