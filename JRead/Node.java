import java.util.HashSet;
import java.util.Set;

public class Node {

  public Set<Node> adjacent = new HashSet<Node>();
  public Set<Edge> weights = new HashSet<Edge>();
  public Set<Pixel> regions = new HashSet<Pixel>();

  private int region = -1;
  private double avgIntensity = -1;

  public Node() {

  }

  public Node(int region) {
    this.region = region;
  }

  public int getRegion() {
    return this.region;
  }

  public void setRegion(int region) {
    this.region = region;
    for(Pixel p : regions) {
      p.setReg(region);
    }
  }

  public int getArea() {
    return regions.size();
  }

  public double getAvgInt() {
    return this.avgIntensity;
  }

  public void calcAvgInt() {
    this.avgIntensity = 0;
    for(Pixel p : regions) {
      this.avgIntensity = this.avgIntensity + p.getInt();
    }

    this.avgIntensity = this.avgIntensity / regions.size();
  }

  public void calculateWeights(double alpha) {
    // Discard old weights
    weights.clear();

    this.calcAvgInt();
    
    for(Node adj : adjacent) {
/*      double c1=0; // Intensity difference along length
      int c2=0; // Length
      double h; // Averge intensity difference between regions      

      // Save some computation if the adjacency has already been calculated
      adj.calcAvgInt();

      // Calculate intensity difference
      h = Math.abs(this.getAvgInt() - adj.getAvgInt());

      // Loop through pixels
      for(Pixel p : regions) {
        // Loop through the surrounding pixels
        for(Pixel q : p.getAdjacent()) {
          // What do we do with nulls(e.g. edges of the image)?
          if(p.getReg() == this.getRegion() && q.getReg() == adj.getRegion()) {
            // Add to length and sum additional weight
            c2++;
            c1=c1+Math.abs(p.getInt() - q.getInt());
          }
        }
      }

      // Formula for w1
      double w1 = (alpha*h*c2) + ((1-alpha)*c1);

      if(w1==0.0 || c2==0.0) System.out.println("Edge calculation error");
      
      // w2=c2
      weights.add(new Edge(adj,w1,c2));
*/
      weights.add(calculateWeight(adj,alpha));
    }
  }

  private Edge calculateWeight(Node destNode, double alpha) {
      double c1=0; // Intensity difference along length
      int c2=0; // Length
      double h; // Averge intensity difference between regions      

      destNode.calcAvgInt();
      this.calcAvgInt();

      // Calculate intensity difference
      h = Math.abs(this.getAvgInt() - destNode.getAvgInt());

      // Loop through pixels
      for(Pixel p : regions) {
        // Loop through the surrounding pixels
        for(Pixel q : p.getAdjacent()) {
          // What do we do with nulls(e.g. edges of the image)?
          if(p.getReg() == this.getRegion() && q.getReg() == destNode.getRegion()) {
            // Add to length and sum additional weight
            c2++;
            c1=c1+(765-3*Math.abs(p.getInt() - q.getInt()));
          }
        }
      }

      // Formula for w1
      double w1 = (alpha*(765-3*h)*c2) + ((1-alpha)*c1);

      if(w1==0.0 || c2==0.0) System.out.println("Edge calculation error");
      
      // w2=c2
      return new Edge(destNode,w1,c2);

  } 

  private void reconcileEdges() {
    Set<Edge> removeEdges = new HashSet<Edge>();
    // Discrepancy in the number of edges to nodes, so some node or nodes have to be removed
    for(Edge edj : weights) 
      if(!adjacent.contains(edj.destination)) 
        removeEdges.add(edj);
    weights.removeAll(removeEdges);
  }

  public void recalculateWeight(Node updated, double alpha) {
    // Remove any extra edges
/*    if(adjacent.size() < weights.size()) */ reconcileEdges();

    Edge toUpdate = null;

    // Find edge to update
    for(Edge edj : weights) {
      if(edj.destination.equals(updated)) {
        toUpdate = edj;
        break;
      }
    }

    Edge newValues = calculateWeight(updated, alpha);

    if(toUpdate==null) {
      weights.add(newValues);
    }
    else {
      toUpdate.setW1(newValues.getW1());
      toUpdate.setW2(newValues.getW2());
    }

  }

  public void print() {
    System.out.println(this.toString());
  }

  public String toString() {
    String output = "Node "+this.getRegion()+", "+this.getArea()+": ";
    for(Node node : adjacent)
      output = output + node.getRegion() + " ";
    output = output + "\n";
    for(Edge edj : weights){
      output = output + edj.getW1() + "," + edj.getW2() + " ";
//      output = output + (768-(edj.getW1() / edj.getW2())) + " ";
    }
    return output;
  }

  public boolean equals(Node other) {
    return (this.getRegion() == other.getRegion() );
  }

} 
