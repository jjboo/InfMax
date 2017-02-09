package ca.ubc.util;

import ca.ubc.InfluenceMaximization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * A directed graph with edge probabilities
 */
public class Graph {

  private static final Logger LOGGER = Logger.getLogger(InfluenceMaximization.class.getCanonicalName());
  private static final String SEPARATOR = " ";

  // TODO we should make those fields private
  public int n;  // num of nodes
  public List<Integer>[] neighbors;
  public List<Double>[] probs;

  @SuppressWarnings("unchecked")
  public Graph(String graphFilePath) {
    BufferedReader br = null;
    int numEdges = 0;

    try  {
      br = new BufferedReader(new FileReader(graphFilePath));
      String line;
      boolean isFirstLine = true;
      LOGGER.info("Reading graph file from " + graphFilePath);

      while ((line = br.readLine()) != null) {
        String[] data = line.split(SEPARATOR);

        if (isFirstLine) {
          // first line tells num of nodes and edges
          isFirstLine = false;
          n = Integer.parseInt(data[0]);
          LOGGER.info("Number of nodes = " + n);

          neighbors = (ArrayList<Integer>[]) new ArrayList[n];
          probs = (ArrayList<Double>[]) new ArrayList[n];
          for (int i = 0; i < n; ++i) {
            neighbors[i] = new ArrayList<>();
            probs[i] = new ArrayList<>();
          }

        } else {
          // each line contains one directed edge: (u, v, p_uv)
          int u = Integer.parseInt(data[0]);
          int v = Integer.parseInt(data[1]);
          double p = Double.parseDouble(data[2]);

          neighbors[u].add(v);
          probs[u].add(p);
          numEdges++;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    } finally {
      try {
        if (br != null) {
          br.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(-1);
      }
    }

    LOGGER.info("Number of edges = " + numEdges);
    LOGGER.info("Finished reading graph file!");
  }

  /**
   * Print the neighbors of u and out-degree of u in the graph
   */
  public void print(int u) {

    if (u >= n) {
      throw new RuntimeException("Node index out of bounds!");
    }

    System.out.println("Printing stats for node " + u);
    List<Integer> f = neighbors[u];
    List<Double> p = probs[u];
    for (int i = 0; i < f.size(); ++i) {
      StringBuilder sb = new StringBuilder();
      sb.append('(');
      sb.append(u);
      sb.append(", ");
      sb.append(f.get(i));
      sb.append(", ");
      sb.append(p.get(i));
      sb.append(" )");
      System.out.println(sb.toString());
    }
  }
}
