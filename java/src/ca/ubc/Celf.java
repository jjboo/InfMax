package ca.ubc;

import java.util.PriorityQueue;
import java.util.Set;

public class Celf {
  PriorityQueue<CelfNode> Q;
  Graph G;
  Config config;

  public Celf(Graph G, Config config) {
    this.G = G;
    this.config = config;
    Q = new PriorityQueue<>(new NodeComparator());
  }

  public void run(Graph G, Config config) {
    Q.clear(); // just a safety check



  }

  double ICCov(Set<Long> seeds) {
    for ()
  }
}
