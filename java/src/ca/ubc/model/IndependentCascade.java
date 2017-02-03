package ca.ubc.model;

import ca.ubc.util.Config;
import ca.ubc.util.Graph;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;


/**
 * Implement spread computation for the IC model
 */
public class IndependentCascade {

  private static final Random RANDOM = new Random();

  private static int bfs(Queue<Integer> queue, Graph graph, boolean[] active) {
    int ret = 0;

    while (!queue.isEmpty()) {
      int w = queue.poll(); // this also removes w from BFS queue
      List<Integer> neighborList = graph.neighbors[w];
      List<Double> edgeProbs = graph.probs[w];

      // try to activate neighbours of w
      for (int j = 0; j < neighborList.size(); ++j) {
        int v = neighborList.get(j);
        // only look at inactive out-neighbours
        if (!active[v]) {
          double prob = edgeProbs.get(j);
          if (RANDOM.nextDouble() <= prob) {
            active[v] = true;
            ret++;
            queue.add(v);
          }
        }
      }
    }

    return ret;
  }

  /**
   * @param seeds Current seed set
   * @param candidate Seed candidate
   * @param curBest current best node
   * @param isCelfPlusOn if celfplus is executed in this call
   * @return An double array {mg, mg2}
   */
  public static double[] estimateSpreadPlus(Graph graph, Config config,
      Set<Integer> seeds, int candidate, int curBest, boolean isCelfPlusOn) {

    boolean[] active = new boolean[graph.n];
    Queue<Integer> bfsQueue = new LinkedList<>();
    //double ret = 0;

    //candidateNode.mg = 0;
    //candidateNode.mg2 = 0;

    double[] ret = new double[2];

    for (int i = 0; i < config.mcRuns; ++i) {
      // reset relevant data structures
      Arrays.fill(active, false);
      bfsQueue.clear();
      int countActive = 0;

      // activate the candidate
      active[candidate] = true;
      bfsQueue.add(candidate);
      countActive++;

      // activate all seeds
      for (int s : seeds) {
        countActive++;
        active[s] = true;
        bfsQueue.add(s);
      }

      // compute MG(u | S)
      countActive += bfs(bfsQueue, graph, active);
      ret[0] += (1.0 * countActive) / (1.0 * config.mcRuns);

      // compute mg2 = MG(u | S + curBest)
      if (isCelfPlusOn && !active[curBest]) {
        active[curBest] = true;
        countActive++;
        bfsQueue.clear();
        bfsQueue.add(curBest);

        countActive += bfs(bfsQueue, graph, active);
        ret[1] += (1.0 * countActive) / (1.0 * config.mcRuns);
      }
    }

    return ret;
  }

  /**
   * Given a seed set, compute spread under IC model
   * @param seeds Current seed set
   * @param candidate Seed candidate id
   */
  public static double estimateSpread(Graph graph, Config config,
      Set<Integer> seeds, Integer candidate) {

    boolean[] active = new boolean[graph.n];
    Queue<Integer> bfsQueue = new LinkedList<>();
    double ret = 0;

    for (int i = 0; i < config.mcRuns; ++i) {
      Arrays.fill(active, false);
      bfsQueue.clear();
      int countActive = 0;

      // activate the candidate
      if (candidate != null) {
        active[candidate] = true;
        bfsQueue.add(candidate);
        countActive++;
      }

      // activate all seeds
      for (int s : seeds) {
        countActive++;
        active[s] = true;
        bfsQueue.add(s);
      }

      countActive += bfs(bfsQueue, graph, active);
      ret += (1.0 * countActive) / (1.0 * config.mcRuns);
    }

    return ret;
  }
}
