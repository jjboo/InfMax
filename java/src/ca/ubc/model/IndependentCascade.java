package ca.ubc.model;

import ca.ubc.util.Config;
import ca.ubc.util.Graph;
import ca.ubc.util.InfMaxUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;


/**
 * Implement spread computation for the IC model
 *
 * TODO For better OOP practice, we need to create a parent abstract class or interface called Model, and let concrect model classes such as IC (this one) or LT inherit it
 */
public class IndependentCascade {
  private final Random _random;

  public IndependentCascade(long seed) {
    _random = new Random(seed);
  }

  /**
   * Simulate influence spread under IC model
   * @param queue Already activated nodes (typically seeds)
   * @param graph Graph data
   * @param active A boolean array specifying which nodes are activated (and not)
   * @return Number of activated nodes in this particular simulation
   */
  private int bfs(Graph graph, Queue<Integer> queue, boolean[] active) {
    int count = 0;

    while (!queue.isEmpty()) {
      int w = queue.poll(); // this also removes w from BFS queue
      List<Integer> neighborList = graph.neighbors[w];
      List<Double> edgeProbs = graph.probs[w];

      // try to activate neighbours of w
      for (int j = 0; j < neighborList.size(); ++j) {
        int v = neighborList.get(j);
        // only look at inactive out-neighbours
        if (!active[v]) {
          if (_random.nextDouble() <= edgeProbs.get(j)) {
            active[v] = true;
            queue.add(v);
            count++;
          }
        }
      }
    }

    return count;
  }

  /**
   * @param seeds Current seed set
   * @param candidate Seed candidate
   * @param curBest current best node
   * @return An double array {u.mg1, u.mg2}
   */
  public double[] estimateSpreadPlus(Graph graph, Config config,
      Set<Integer> seeds, int candidate, int curBest) {

    boolean[] active = new boolean[graph.n];
    Queue<Integer> bfsQueue = new LinkedList<>();

    // ret[0] = sigma(S+u), ret[1] = sigma(S+u+curBest), both initialized to 0
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
      countActive += bfs(graph, bfsQueue, active);
      ret[0] += countActive;
      ret[1] += countActive;

      // compute mg2 = MG(u | S + prevBest)
      if (curBest >= 0 && !active[curBest]) {
        active[curBest] = true;
        countActive = 1;
        bfsQueue.add(curBest);

        countActive += bfs(graph, bfsQueue, active);
        ret[1] += countActive;
      }
    }

    ret[0] = InfMaxUtils.round(ret[0] / (double) config.mcRuns, config.rounding);
    ret[1] = InfMaxUtils.round(ret[1] / (double) config.mcRuns, config.rounding);
    return ret;
  }

  /**
   * Given a seed set, compute spread under IC model
   * @param seeds Current seed set
   * @param candidate Seed candidate id
   */
  public double estimateSpread(Graph graph, Config config,
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

      countActive += bfs(graph, bfsQueue, active);
      ret += countActive;
    }

    return InfMaxUtils.round(ret / (double) config.mcRuns, config.rounding);
  }

  /**
   * Compute spread, mean, and std dev of each PW
   */
  public double[] spreadStats(Graph graph, Config config, Set<Integer> seeds) {

    double spread = 0;
    double[] countActive = new double[config.mcRuns];
    Arrays.fill(countActive, 0.0);

    boolean[] active = new boolean[graph.n];
    Queue<Integer> bfsQueue = new LinkedList<>();

    for (int i = 0; i < config.mcRuns; ++i) {
      Arrays.fill(active, false);
      bfsQueue.clear();

      // activate all seeds
      for (int s : seeds) {
        countActive[i]++;
        active[s] = true;
        bfsQueue.add(s);
      }

      countActive[i] += bfs(graph, bfsQueue, active);
      spread += countActive[i];
    }

    double[] stats = new double[3]; // expected spread, mean, std dev
    stats[0] = spread;
    stats[1] = computeMean(countActive);
    stats[2] = computeStdDev(countActive);
    return stats;
  }

  /**
   * Return mean value
   */
  private double computeMean(double[] arr) {
    double sum = 0.0;
    for (double d : arr) {
      sum += d;
    }
    return sum / arr.length;
  }

  /**
   * Return standard deviation
   */
  private double computeStdDev(double[] arr) {
    double mean = computeMean(arr);
    double var = 0.0;
    for (double d : arr) {
      var += Math.pow((d - mean), 2) / arr.length;
    }
    return Math.sqrt(var);
  }
}
