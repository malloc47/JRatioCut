public class Edge {
  private double w1 = 0;
  private double w2 = 0;
//  private int region = 0;
  public Node destination = null;

  public Edge(Node destination, double w1, double w2) {
    this.destination = destination;
    this.w1 = w1;
    this.w2 = w2;
  }

  public Edge(Node destination) {
    this.destination = destination;
  }
  
  public Edge() {

  }

  public double getW1() {
    return this.w1;
  }

  public void setW1(double w1) {
    this.w1 = w1;
  }

  public double getW2() {
    return this.w2;
  }

  public void setW2(double w2) {
    this.w2 = w2;
  }

  public double cutRatio() {
    return ((this.getW1() / this.getW2()));
  }

  public int getRegion() {
//    return this.region;
    return destination.getRegion();
  }

/*  public void setRegion(int region) {
    this.region = region;
  }
*/
}
