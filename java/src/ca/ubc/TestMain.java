package ca.ubc;

/**
 * For testing purposes only
 */
public class TestMain {
  public static void main(String[] args){
    Graph graph = new Graph("/Users/wlu/IdeaProjects/InfMax2017/java/graph/hep_WC.inf.0");
    graph.print(0);
    graph.print(1);
    graph.print(15232);
  }
}
