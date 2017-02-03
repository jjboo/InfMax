package ca.ubc;

/**
 * For testing purposes only
 */
public class TestMain {
  public static void main(String[] args){
    Graph graph = new Graph("./java/graph/sample0.txt");
    graph.print(0);
    graph.print(1);
    graph.print(15232);
  }
}
