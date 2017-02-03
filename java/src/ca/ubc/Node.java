package ca.ubc;

import java.util.Comparator;

public class Node {
  long id;
}

class CelfNode extends Node {
  double mg;
  int flag;
}

class CelfppNode extends CelfNode {
  double mg2;
  long curBest;
}

class NodeComparator implements Comparator<CelfNode> {
  @Override
  public int compare(CelfNode n1,CelfNode n2){
    return Double.compare(n2.mg,n1.mg);
  }
}
