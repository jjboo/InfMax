package ca.ubc.tools;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Simplify node ids from 0 : N-1
 */
public class SimplifyNodeIds {

  // internal Node class
  class Node {
    long oldNodeId; // input node id
    int newNodeId; // new node id, from 0 : N-1
    Map<Node, Double> neighbors; // <neighbor, prob_on_edge>

    Node(long oldNodeId, int newNodeId) {
      this.oldNodeId = oldNodeId;
      this.newNodeId = newNodeId;
      neighbors = new HashMap<>();
    }
  }

  private static final Logger LOGGER = Logger.getLogger(SimplifyNodeIds.class.getCanonicalName());
  private static final String SEPARATOR = " ";

  Map<Long, Node> oldIdsToNodesMap;
  int numEdges;

  private void readInputFile(String inputGraphFile) {
    oldIdsToNodesMap = new HashMap<>();

    BufferedReader br = null;

    int nextNodeId = 0;
    numEdges = 0;

    try  {
      br = new BufferedReader(new FileReader(inputGraphFile));
      String line;
      LOGGER.info("Reading input graph file from " + inputGraphFile);

      while ((line = br.readLine()) != null) {
        String[] data = line.split(SEPARATOR);

        // each line contains one directed edge: (u, v, p_uv)
        long u = Long.parseLong(data[0]);
        long v = Long.parseLong(data[1]);
        double p_uv = Double.parseDouble(data[2]);

        // see if u and v already exists
        Node uNode = oldIdsToNodesMap.get(u);
        Node vNode = oldIdsToNodesMap.get(v);

        if (uNode == null) {
          uNode = new Node(u, nextNodeId);
          oldIdsToNodesMap.put(u, uNode);
          nextNodeId++;
        }

        // TODO: perhaps, factor this out in a function?
        if (vNode == null) {
          vNode = new Node(v, nextNodeId);
          oldIdsToNodesMap.put(v, vNode);
          nextNodeId++;
        }

        // add edge
        uNode.neighbors.put(vNode, p_uv);
        numEdges++;
      }

      LOGGER.info("Done reading input graph file. Number of nodes: " + nextNodeId + ". Edges: " + numEdges);

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null) {
          br.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }


  }

  private void writeOutputGraphFile(String outputFileName) {
    try {
      PrintWriter writer = new PrintWriter(outputFileName);

      // write first line: numNodes numEdges
      int numNodes = oldIdsToNodesMap.size();
      writer.println(numNodes + " " + numEdges);

      // write edges now
      for (Node uNode : oldIdsToNodesMap.values()) {
        int uNewId = uNode.newNodeId;

        for (Map.Entry<Node, Double> entry : uNode.neighbors.entrySet()) {
          Node vNode = entry.getKey();
          long vNewId = vNode.newNodeId;
          double p_uv = entry.getValue();

          writer.println(uNewId + " " + vNewId + " " + p_uv);
        }
      }

      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    LOGGER.info("Written new graph file to " + outputFileName);
  }

  public static void main(String[] args) {
    SimplifyNodeIds s = new SimplifyNodeIds();
    s.readInputFile("test_graph.txt");
    s.writeOutputGraphFile("test_graph_simplified.txt");
  }

}
