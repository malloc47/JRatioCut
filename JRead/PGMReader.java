import java.io.*;
import java.util.*;

public class PGMReader {

  public int[][] image;
  private int x = 0;
  private int y = 0;
  private int maxVal = 0;

  public PGMReader(String fileName) {
    FileInputStream file;
    Reader reader;
    StreamTokenizer parser;

    try {
      file = new FileInputStream(fileName);
      reader = new BufferedReader(new InputStreamReader(file));
      parser = new StreamTokenizer(reader);
      parser.commentChar('#');
      parser.parseNumbers();

      int i = 0;
      int j = 0;

      parser.nextToken();
//      System.out.println(parser.sval);
      if(!parser.sval.equals("P2")) {
        System.out.println("Not valid .pgm file, or not grayscale.");
        return;
      }

      parser.nextToken();
      this.x = (int)parser.nval;
      parser.nextToken();
      this.y = (int)parser.nval;

      if(Params.debug) System.out.println("\tPGM Dimensions: " + x + "," + y);

      parser.nextToken();
      this.maxVal = (int)parser.nval;

//      System.out.println("Max Value: " + maxVal);


      image = new int[this.y][this.x];

      while(parser.nextToken() != StreamTokenizer.TT_EOF) {
        switch(parser.ttype) {
          // Fill in the numbers in the array
          case StreamTokenizer.TT_NUMBER:
            image[j][i] = (int)Math.round(parser.nval);
            i++;
            if(i >= x) { i = 0; j++; }
            break;

/*          case StreamTokenizer.TT_WORD:
            System.out.println("Possible error in seg file");
            break;  */
        }

      }

      file.close(); 

    } catch(IOException e) {
      System.out.println(fileName + "not found.");
    } 

  }

  public void print() {
    System.out.println("");
    for(int i = 0; i<x; i++) {
      for(int j = 0; j<y; j++) {
        System.out.print(image[i][j] + " ");
      }
      System.out.println("");
    }
  }

  public void writePGM(String fileName) {
    try{
      FileWriter fstream = new FileWriter(fileName);
      PrintWriter out = new PrintWriter(fstream);
      out.println("P2");
      out.println(image[0].length + " " + image.length);
      out.println("255");

      for(int i = 0; i < image[0].length; i++) {
        for(int j = 0; j < image.length; j++) {
          out.println(image[j][i]);
//          out.println( pixels[i][j].isInternal() ? pgm[i][j] : "255" );
        } 
      }
      out.close();
    }catch (Exception e){
      System.err.println("Error: " + e.getMessage());
    }

  }




}
