package ca.ubc.algo;

import ca.ubc.InfluenceMaximization;
import ca.ubc.model.IndependentCascade;
import ca.ubc.util.CelfNode;
import ca.ubc.util.Config;
import ca.ubc.util.Graph;
import ca.ubc.util.InfMaxUtils;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Celf
 */
public class CelfAlgo {

  private static final Logger LOGGER = Logger.getLogger(InfluenceMaximization.class.getCanonicalName());

  private PriorityQueue<CelfNode> _covQueue;
  private Graph _graph;
  private Config _config;
  private Set<Integer> _seedSet;
  private BufferedWriter _bufferedWriter;

  private static final int INITIAL_FLAG = 0;

  /**
   * Constructor
   */
  public CelfAlgo(Graph graph, Config config, BufferedWriter bufferedWriter) {
    _graph = graph;
    _config = config;
    _covQueue = new PriorityQueue<>(new CelfNode.NodeComparator());
    _seedSet = new HashSet<>();
    _bufferedWriter = bufferedWriter;
  }

  /**
   * Mine seeds
   * @return total spread of the chosen seed set
   */
  public double run() throws IOException {
    final long startTime = System.currentTimeMillis();

    double totalSpread = 0;
    int lookUps = 0;
    _seedSet.clear();

    // first iteration
    for (int nodeId = 0; nodeId < _graph.n; ++nodeId) {
      CelfNode node = new CelfNode(nodeId, IndependentCascade.estimateSpread(_graph, _config, _seedSet, nodeId), INITIAL_FLAG);
      _covQueue.add(node);
      if (nodeId % 1000 == 0) {
        LOGGER.info(node.id + "\t" + String.format("%.5f", node.mg));
      }
      lookUps++;
    }

    // Select k seeds
    while (_seedSet.size() < _config.numSeeds) {
      CelfNode bestNode = _covQueue.peek();

      // flag = current seed set size: found a seed
      if (bestNode.flag == _seedSet.size()) {
        _seedSet.add(bestNode.id);
        totalSpread += bestNode.mg;
        String msg = InfMaxUtils.logSeed(LOGGER, _seedSet.size(), bestNode.id, bestNode.mg, totalSpread,
                lookUps, 0, InfMaxUtils.runningTimeMin(startTime));
        _bufferedWriter.write(msg);
        _covQueue.poll();
        lookUps = 0; // reset
      } else {
        // re-compute MG
        double newMg = IndependentCascade.estimateSpread(_graph, _config, _seedSet, bestNode.id) - totalSpread;
        lookUps++;
        // update flag and re-heapify
        int id = bestNode.id;
        _covQueue.poll();
        _covQueue.add(new CelfNode(id, newMg, _seedSet.size()));
      }
    }

    return totalSpread;
  }
}
