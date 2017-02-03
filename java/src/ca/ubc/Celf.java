package ca.ubc;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Celf {
  PriorityQueue<CelfNode> covQueue;
  Graph G;
  Config config;

  public Celf(Graph G, Config config) {
    this.G = G;
    this.config = config;
    covQueue = new PriorityQueue<>(new NodeComparator());
  }

  public void run(Graph G, Config config) {



  }

  double ICCov(List<Long> seeds) {
    double cov = 0;

    for (int i=0; i<config.mcRuns; i++) {
        
    }

    return 0;
  }
}
