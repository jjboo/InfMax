package ca.ubc;

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