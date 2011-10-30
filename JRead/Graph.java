import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.io.*;

public class Graph {

  private List<Node> nodes = new ArrayList<Node>();

  Pixel[][] pixels;  //= new Pixel[regions.length][regions[0].length];

  private double alpha = 0;  

  public Graph() {

  } 

  /* Adds the initial nodes */
  public void createNodes(int number) {
    for(int i = 0; i <= number; i++  ) {
      Node newNode = new Node(i);
      nodes.add(newNode);
    }
  }

  /* Debug function to print nodes and adjacencies */
  public void listNodes() {
    System.out.println("Nodes: ");
    for(Node node : nodes) {
      node.print();
    }
    System.out.println("");
    System.out.println("");
  }

  public int numNodes() {
    return nodes.size();
  }

  /* Adjacency / edge calculation */
  public void setupNodes(int[][] regions, int[][] intensities) {

//    regions = seg;

    // The following two searches should be rewritten to follow the pixel matrix below, as it should be more efficient

    // Row search
    for(int i = 0; i < regions.length; i++) {
      for(int j = 0; j < regions[0].length-1; j++) {
        if(regions[i][j] != regions[i][j+1]) {
          Node node = nodes.get(regions[i][j]);
          Node adj = nodes.get(regions[i][j+1]);

          node.adjacent.add(adj);
          adj.adjacent.add(node);
        }
      }
    }

    // Column search
    for(int j = 0; j < regions[0].length; j++) {
      for(int i = 0; i < regions.length-1; i++) {
        if(regions[i][j] != regions[i+1][j]) {
          Node node = nodes.get(regions[i][j]);
          Node adj = nodes.get(regions[i+1][j]);

          node.adjacent.add(adj);
          adj.adjacent.add(node);
        }
      }
    }

    /*Pixel[][]*/ pixels  = new Pixel[regions.length][regions[0].length];

    // Set area and create Pixel objects for each region
    for(int i = 0; i < regions.length; i++) {
      for(int j = 0; j < regions[0].length; j++) {
        Node node = nodes.get(regions[i][j]);
        Pixel p = new Pixel(i,j,regions[i][j],intensities[i][j]);
        node.regions.add(p);
        pixels[i][j] = p;
      }
    }

    // Calculate adjacencies for each pixel
    for(int i = 0; i < pixels.length; i++ ) {
      for(int j = 0; j < pixels[0].length; j++) {
        if(i!=0) pixels[i][j].above = pixels[i-1][j];
        if(i<pixels.length -1) pixels[i][j].below = pixels[i+1][j];
        if(j!=0) pixels[i][j].left = pixels[i][j-1];
        if(j<pixels[0].length -1) pixels[i][j].right = pixels[i][j+1];
      }
    }

  }

  public void calculateWeights(double alpha) {
    this.alpha = alpha;
    for(Node node : nodes) {
      node.calculateWeights(alpha);
      if(node.adjacent.size() != node.weights.size()) {
        System.out.println("Error: edge-region number mismatch.");
      }
    }
  }

  /* Node1 is merged to node2, removing node1 */
  private void mergeNodes(Node node1, Node node2) {
    if(!node1.adjacent.contains(node2) || !(node2.adjacent.contains(node1)) || node1.equals(node2) ) {
        System.out.println("Improper merge.");
        return;
    }
    // Transfer pixels to new region
    for(Pixel p : node1.regions) {
      p.setReg(node2.getRegion());
      if(!node2.regions.add(p)) System.out.println("Already contained ("+p.getX()+","+p.getY()+")");
    }
    node2.adjacent.addAll(node1.adjacent);
    // Don't let it add itself or keep the old node around
    node2.adjacent.remove(node2);
    node2.adjacent.remove(node1);


    nodes.remove(node1);
    
    for(Node node : node2.adjacent) {
      if(node.adjacent.contains(node1)) {
        node.adjacent.remove(node1);
        node.adjacent.add(node2);
      }
      node.recalculateWeight(node2,alpha);
//      node.calculateWeights(alpha);
    }
    node2.calculateWeights(alpha);

  }


  /* Node1 is merged to node2, removing node1 */
/*  private void mergeNodesOld(Node node1, Node node2) {

    // Sanity check--should be adjacent
    if(!node1.adjacent.contains(node2) || !(node2.adjacent.contains(node1))) {
        System.out.println("Improper merge.");
        return;
    }

    //  Instead of removing the node we no longer want, we instead
    //  "take" its slot and give it to the item on the end of the
    //  list, removing it after this move is done.
    Node last = nodes.get(nodes.size()-1);

    // We must handle special cases where the end of the list is
    // also one of the nodes we are merging to or from
    if(last.equals(node1) || last.equals(node2)) {
      System.out.println("Correcting");

      if(last.equals(node2)) {
        node2 = node1;
        node1 = last;
      }

      for(Pixel p : node1.regions) {
        p.setReg(node2.getRegion());
        if(!node2.regions.add(p)) System.out.println("Already contained ("+p.getX()+","+p.getY()+")");
      }
      node2.adjacent.addAll(node1.adjacent);
      // Don't let it add itself!
      node2.adjacent.remove(node2);
      for(Node node : node2.adjacent) {
        if(node.adjacent.contains(node1)) {
          node.adjacent.remove(node1);
          node.adjacent.add(node2);
        }
        node.calculateWeights(alpha);
      }
      node2.calculateWeights(alpha);
      nodes.remove(node1);

    }
    // Don't let us merge to the end of the list. Instead, just
    // reverse the merge to fall into the previous if
//    else if(last.equals(node2)) {
//      mergeNodes(node2,node1);
//      return;    
//    } 
    else {
      // I've made a huge mistake.
      if(last.equals(node1) || last.equals(node2)) System.out.println("AAAAAAAAAAHHHHHHHHHHHHHHHH!!!!!!!!!!!!!!!!");

//      for(Node node : last.adjacent) {
//        for(Edge edj : node.weights) {
//          if(edj.getRegion() == last.getRegion()) 
//            edj.setRegion(node1.getRegion());
//        }
//      }

      nodes.set(node1.getRegion(),last);
      last.setRegion(node1.getRegion());
      nodes.remove(nodes.size()-1);

      // Transfer pixels to new region
      for(Pixel p : node1.regions) {
        p.setReg(node2.getRegion());
        if(!node2.regions.add(p)) System.out.println("Already contained ("+p.getX()+","+p.getY()+")");
      }

      node2.adjacent.addAll(node1.adjacent);
      // Don't let it add itself!
      node2.adjacent.remove(node2);

      for(Node node : node2.adjacent) {
        if(node.adjacent.contains(node1)) {
          node.adjacent.remove(node1);
          node.adjacent.add(node2);
        }
//        node.recalculateWeight(node2,alpha);
        node.calculateWeights(alpha);
      }
      node2.calculateWeights(alpha);

      last.calculateWeights(alpha);

    }
    // Anyone linking to us still?
    for(Node node : nodes) {
      if(node.adjacent.contains(node1)) System.out.println("I sense a disturbance in the force.");
    }

    // Update other node regions and adjacencies
//    for(Node node : nodes) {
//      updateRegion(node.getRegion(),nodes.indexOf(node));
//      node.setRegion(nodes.indexOf(node));
//
//      if(node.adjacent.contains(node1)){
//        node.adjacent.remove(node1);
//        node.adjacent.add(node2);
//        node.calculateWeights(alpha);
//      }
//    }
//
    // Update new node regions and adjacencies
//    node2.adjacent.addAll(node1.adjacent);
//    node2.calculateWeights(alpha);

    // Recalculate all edges
    // Optimize this for efficiency later
//    calculateWeights(alpha);

 }
*/
  public void edgeThresholdMerge(int threshold) {
    boolean continueLooping = false;
    do {
      continueLooping = false;
      Node mergeNode = null;
      Node mergeToNode = null;
      double maxCutRatio = -1;

      for(Node node : nodes) {
        for(Edge edj : node.weights) {
          if(edj.cutRatio() > maxCutRatio || maxCutRatio == -1) {
            mergeNode = node;
            mergeToNode = edj.destination; 
            maxCutRatio = edj.cutRatio();
          }
        }
      }

      if(Math.floor(maxCutRatio) >= threshold) {
        if(Params.verbose) {
          System.out.print("Max: "+maxCutRatio+">"+threshold+"\t");
          System.out.print("Nodes: "+nodes.size()+" \t");
          System.out.println("\t\tMerging("+mergeNode.getRegion()+"->"+mergeToNode.getRegion()+")"); 
        }

        mergeNodes(mergeNode,mergeToNode);
        // Should only have to do this to debug
//        calculateWeights(alpha);
        continueLooping = true;
      }

    }while(continueLooping);

    // Update region #s to their respective indices
    int counter = 0;
    for(Node node : nodes) {
      node.setRegion(counter);
      counter++;
    }

    calculateWeights(alpha);

//    outputWeights();

    if(Params.debug) System.out.println("\tNumber of nodes: "+nodes.size());

  }

  public void edgeNumberMerge(int number) {
    boolean continueLooping = false;
    while(nodes.size() > number) {
      continueLooping = false;
      Node mergeNode = null;
      Node mergeToNode = null;
      double maxCutRatio = -1;

      for(Node node : nodes) {
        for(Edge edj : node.weights) {
          if(edj.cutRatio() > maxCutRatio || maxCutRatio == -1) {
            mergeNode = node;
            mergeToNode = edj.destination; 
            maxCutRatio = edj.cutRatio();
          }
        }
      }

      if(Params.verbose) {
        System.out.print("Nodes: "+nodes.size()+" \t");
        System.out.println("\t\tMerging("+mergeNode.getRegion()+"->"+mergeToNode.getRegion()+")"); 
      }

      mergeNodes(mergeNode,mergeToNode);
      // Should only have to do this to debug
//      calculateWeights(alpha);
    }

    // Update region #s to their respective indices
    int counter = 0;
    for(Node node : nodes) {
      node.setRegion(counter);
      counter++;
    }

    calculateWeights(alpha);

//    outputWeights();

    if(Params.debug) System.out.println("\tNumber of nodes: "+nodes.size());

  }

  public void areaMergeMax(int threshold) {
    boolean continueLooping = false;
//    System.out.println("First count is " + pixelCount() );
    do {
      continueLooping = false;

/*      for(Node node : nodes) {
        for(Node adj : node.adjacent) {
          boolean corresponding = false;
          for(Edge edj : node.weights) {
            if(edj.getRegion() == adj.getRegion()) corresponding = true;
          }
          if(!corresponding) System.out.println("FAIL on: "+adj.getRegion());
        }
      }
*/
      // Select node to merge
      Node mergeNode = null;
      int minArea = -1;
      for(Node node : nodes) {
        if(node.getArea() < minArea || minArea == -1) {
          minArea=node.getArea();
          mergeNode = node;
        }
      }

//      for(Node node : nodes) {
        // If node is a candidate for merging
        if(mergeNode.getArea() < threshold) {
          // Find optimal adjacent region to merge to
          double maxCutRatio = 0;
          int mergeToRegion = -1; 
          for(Edge edj : mergeNode.weights) {
            double cutRatio = edj.cutRatio(); //768-(edj.getW1() / edj.getW2());
            if(maxCutRatio < cutRatio) {
              maxCutRatio = cutRatio;
              mergeToRegion = edj.getRegion();
            }
          }
//          if(mergeToRegion < 0) System.out.println("A very bad thing has happened");
          // Locate corresponding node
          Node mergeToNode = null;
          for(Node adj : mergeNode.adjacent) {
            if(adj.getRegion() == mergeToRegion) {
              mergeToNode = adj;
              break;
            }
          }

          if(mergeToNode == null || mergeToRegion == -1) System.out.println("No candidate merge node found.");

          // Perform merge
          if(Params.verbose) {
            System.out.print("Nodes: "+nodes.size()+" \t");
            System.out.println("\t\tMerging("+mergeNode.getRegion()+"->"+mergeToNode.getRegion()+")");
          }
          mergeNodes(mergeNode, mergeToNode);
          continueLooping = true;
//          break;
        }
//      }
    } while(continueLooping);

    //Update regions to new values
    int counter = 0;
    for(Node node : nodes) {
      node.setRegion(counter);
      counter++;
      if(node.getArea() < threshold) System.out.println("Threshold violated");
    }

    if(Params.debug) System.out.println("\tNumber of nodes: "+nodes.size());
//    System.out.println("Last count is " + pixelCount() );
  } 

  /* Change a region's number */
/*  public void updateRegion(int oldRegion, int newRegion) {
    // Do nothing if regions are the same
    if(oldRegion==newRegion) return;
    // Otherwise, loop through the matrix updating the values as they are found
    for(int i = 0; i < regions.length; i++) {
      for(int j = 0; j < regions[0].length; j++) {
        if(regions[i][j] == oldRegion) regions[i][j] = newRegion;
      } 
    }
  }
*/
  /* Debug segfile writer */
/*  public void writeSEG(String fileName) {
    try{
      FileWriter fstream = new FileWriter(fileName);
      PrintWriter out = new PrintWriter(fstream);
      out.print("#(");
      for(int i = 0; i < pixels[0].length; i++) {
        out.print( (i!=0 ? "   " : "")  + "#(");
        for(int j = 0; j < pixels.length; j++) {
          out.print( (j!=0 ?  "      " : "") + pixels[j][i].getReg());
          out.println( (j!=pixels.length-1 ? "" : ")" + (i!=pixels[0].length-1 ? "" : ")" )  ) );
        } 
      }
      out.close();
    }catch (Exception e){
      System.err.println("Error: " + e.getMessage());
    }

  }
*/
/* public void areaMergeMin(int threshold) {
    boolean continueLooping = false;
    System.out.println("First count is " + pixelCount() );
    do {
      continueLooping = false;

      for(Node node : nodes) {
        for(Node adj : node.adjacent) {
          boolean corresponding = false;
          for(Edge edj : node.weights) {
            if(edj.getRegion() == adj.getRegion()) corresponding = true;
          }
          if(!corresponding) System.out.println("FAIL on: "+adj.getRegion());
        }
      }

      for(Node node : nodes) {
        // If node is a candidate for merging
        if(node.getArea() < threshold) {
          // Find optimal adjacent region to merge to
          double minCutRatio = 9999999;
          int mergeToRegion = -1; 
          for(Edge edj : node.weights) {
            double cutRatio = edj.getW1() / edj.getW2();
            if(minCutRatio > cutRatio) {
              minCutRatio = cutRatio;
              mergeToRegion = edj.getRegion();
            }
          }
          // Locate corresponding node
          Node mergeToNode = null;
          for(Node adj : node.adjacent) {
            if(adj.getRegion() == mergeToRegion) {
              mergeToNode = adj;
              break;
            }
          }
          // Perform merge
//          System.out.print("Nodes: "+nodes.size()+" \t");
//          System.out.println("\t\tMerging("+node.getRegion()+"->"+mergeToNode.getRegion()+")");
          mergeNodes(node, mergeToNode);
          continueLooping = true;
          break;
        }
      }
    } while(continueLooping);

    //Update regions to new values
    int counter = 0;
    for(Node node : nodes) {
      node.setRegion(counter);
      counter++;
      if(node.getArea() < threshold) System.out.println("Threshold violated");
    }

    System.out.println("Number of nodes: "+nodes.size());
  }
*/ 
public boolean duplicateCheck() {
  for(Node node : nodes) {
    int count = 0;
    for(Node nodeOther: nodes) {
      if(node.getRegion() == nodeOther.getRegion()) count ++;
    }
    if(count > 1) return false; 
  }
  return true;
}

public boolean inOrderCheck() {
  for(Node node: nodes) {
    if(node.getRegion() != nodes.indexOf(node)) return false;
  }
  return true;
}

public int pixelCount() {
  int count = 0;
  for(Node node : nodes) {
    count = count + node.regions.size();
  }
  return count; 
}

public void outputWeights() {
    for(Node node : nodes)
      for(Edge edj : node.weights)
        System.out.println(edj.cutRatio()+" ");
}

public void writePGM(String fileName,int[][] pgm) {
    try{
      FileWriter fstream = new FileWriter(fileName);
      PrintWriter out = new PrintWriter(fstream);
      out.println("P3");
      out.println(pgm[0].length + " " + pgm.length);
      out.println("255");

      for(int i = 0; i < pgm.length; i++) {
        for(int j = 0; j < pgm[0].length; j++) {
//          out.println(pgm[i][j]);
          out.print( (pixels[i][j].isInternal() ? pgm[i][j] : "255") + " " );
//          out.print( (pixels[j][i].isInternal() ? pgm[j][i] : "0") + " " );
          out.print(pgm[i][j] + " " );
//          out.println( pixels[j][i].isInternal() ? pgm[j][i] : "0" );
          out.println( pgm[i][j] );
        } 
      }
      out.close();
    }catch (Exception e){
      System.err.println("Error: " + e.getMessage());
    }
}

/*public void writePGMRegion(String fileName,int[][] pgm,int region) {
    try{
      FileWriter fstream = new FileWriter(fileName);
      PrintWriter out = new PrintWriter(fstream);
      out.println("P3");
      out.println(pgm[0].length + " " + pgm.length);
      out.println("255");

      for(int i = 0; i < pgm[0].length; i++) {
        for(int j = 0; j < pgm.length; j++) {
//          out.println(pgm[i][j]);
          out.print( (pixels[j][i].getReg() != region ? pgm[j][i] : "255") + " " );
//          out.print( (pixels[j][i].isInternal() ? pgm[j][i] : "0") + " " );
          out.print(pgm[j][i] + " " );
//          out.println( pixels[j][i].isInternal() ? pgm[j][i] : "0" );
          out.println( pgm[j][i] );
        } 
      }
      out.close();
    }catch (Exception e){
      System.err.println("Error: " + e.getMessage());
    }
} */

public void writeSEG(String fileName) {
    try{
      FileWriter fstream = new FileWriter(fileName);
      PrintWriter out = new PrintWriter(fstream);
      out.print("#(");
      for(int i = 0; i < pixels.length; i++) {
        out.print( (i!=0 ? "   " : "")  + "#(");
        for(int j = 0; j < pixels[0].length; j++) {
          out.print( (j!=0 ?  "      " : "") + pixels[i][j].getReg());
          out.println( (j!=pixels[0].length-1 ? "" : ")" + (i!=pixels.length-1 ? "" : ")" )  ) );
        } 
      }
      out.close();
    }catch (Exception e){
      System.err.println("Error: " + e.getMessage());
    }

  }

}
