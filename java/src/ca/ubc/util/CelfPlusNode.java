package ca.ubc.util;

import ca.ubc.algo.CelfPlusAlgo;


/**
 * Used by {@link CelfPlusAlgo} in its priority queue for seed selection
 */
public class CelfPlusNode extends CelfNode {
  public double mg2;
  public int prevBest;

  public CelfPlusNode(int id, double mg, double mg2, int flag, int prevBest) {
    this.id = id;
    this.mg = mg;
    this.mg2 = mg2;
    this.flag = flag;
    this.prevBest = prevBest;
  }
}
