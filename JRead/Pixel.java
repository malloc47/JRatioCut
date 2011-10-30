import java.util.List;
import java.util.ArrayList;

public class Pixel {
  private int x = -1;
  private int y = -1;

  private int region = -1;
  private int intensity = -1;

  public Pixel above = null;
  public Pixel below = null;
  public Pixel left = null;
  public Pixel right = null; 

  public Pixel(int x, int y) {
    this.setX(x);
    this.setY(y);
  }

  public Pixel(int x, int y, int region, int intensity) {
    this.setX(x);
    this.setY(y);
    this.setReg(region);
    this.setInt(intensity);
  }

  public Pixel() {}

  public void setX(int x) {
    if(x >= 0) this.x = x;
  }
  public void setY(int y) {
    if(y >= 0) this.y = y;
  }

  public void setReg(int region) {
    this.region = region;
  }

  public void setInt(int intensity) {
    this.intensity = intensity;
  }

  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }

  public int getReg() {
    return this.region;
  }

  public int getInt() {
    return this.intensity;
  }
  
  public List<Pixel> getAdjacent() {
    List<Pixel> adjacent = new ArrayList<Pixel>();
    if(above!=null) adjacent.add(above);
    if(below!=null) adjacent.add(below);
    if(left!=null) adjacent.add(left);
    if(right!=null) adjacent.add(right);
    return adjacent;
  }

  public boolean isInternal() {
    return ( (above==null || above.getReg()==getReg()) && 
        (below==null || below.getReg()==getReg()) &&  
        (left==null  || left.getReg()==getReg()) &&
        (right==null || right.getReg()==getReg()) );
  }

  public boolean isBorder() {
    return (left==null || right==null || above==null || below==null);
  }

  public boolean equals(Pixel other) {
    return ( (this.getX() == other.getX()) &&
/*        (this.getInt() == other.getInt()) && 
        (this.getReg() == other.getReg()) &&
*/        (this.getY() == other.getY()) );
  }

}
