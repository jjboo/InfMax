package ca.ubc;

import java.util.Comparator;

public abstract class Node {
  int id;
}

class CelfNode extends Node {
  double mg;
  int flag;

  public CelfNode() {
    this.id = -1;
    this.mg = -Double.MAX_VALUE;
    this.flag = -1;
  }

  public CelfNode(int id, double mg, int flag) {
    this.id = id;
    this.mg = mg;
    this.flag = flag;
  }
}

class CelfPlusNode extends CelfNode {
  double mg2;
  int curBest;

  public CelfPlusNode(int id, double mg, double mg2, int flag, int curBest) {
    this.id = id;
    this.mg = mg;
    this.mg2 = mg2;
    this.flag = flag;
    this.curBest = curBest;
  }
}

class NodeComparator implements Comparator<CelfNode> {
  @Override
  public int compare(CelfNode n1, CelfNode n2){
    return Double.compare(n2.mg, n1.mg);
  }
}
