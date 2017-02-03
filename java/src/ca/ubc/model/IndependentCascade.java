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

  /**
   * Given a seed set, compute spread under IC model
   */
  public static double estimateSpread(Graph graph, Config config, Set<Integer> seeds, Integer candidate) {
    boolean[] active = new boolean[graph.n];
    Queue<Integer> bfsQueue = new LinkedList<>();
    double ret = 0;

    for (int i = 0; i < config.mcRuns; ++i) {
      Arrays.fill(active, false);
      bfsQueue.clear();
      int countActive = 0;

      if (candidate != null) {
        // activate the candidate
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

      while (!bfsQueue.isEmpty()) {
        int w = bfsQueue.poll(); // this also removes w from BFS queue
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
              countActive++;
              bfsQueue.add(v);
            }
          }
        }
      }

      ret += (1.0 * countActive) / (1.0 * config.mcRuns);
    }

    return ret;
  }

}
