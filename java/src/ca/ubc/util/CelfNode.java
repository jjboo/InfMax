package ca.ubc.util;

import ca.ubc.algo.CelfAlgo;
import java.util.Comparator;


/**
 * Used by {@link CelfAlgo} in its priority queue for seed selection
 */
public class CelfNode extends Node {

  public static class NodeComparator implements Comparator<CelfNode> {
    @Override
    public int compare(CelfNode n1, CelfNode n2){
      return Double.compare(n2.mg, n1.mg); // we want sorting in DESC order
    }
  }

  public double mg;
  public int flag;

  /**
   * Default constructor, should not be called
   */
  protected CelfNode() {
    this.id = -1;
    this.mg = -Double.MAX_VALUE;
    this.flag = -1;
  }

  /**
   * Public constructor
   */
  public CelfNode(int id, double mg, int flag) {
    this.id = id;
    this.mg = mg;
    this.flag = flag;
  }
}
