import java.io.*;
import java.util.*;

public class SEGReader {

  public int[][] regions;
  private int x = 0;
  private int y = 0;

  public SEGReader() {

  }

  public SEGReader(String fileName) {
    dimensions(fileName);
    if(Params.debug) System.out.println("\tSEG Dimensions: " + x + "," + y);
    read(fileName);
  }

  private void dimensions(String fileName) {
    FileInputStream file;
    Reader reader;
    StreamTokenizer parser;

    try {
      file = new FileInputStream(fileName);
      reader = new BufferedReader(new InputStreamReader(file));
      parser = new StreamTokenizer(reader);
      parser.wordChars('#','#');
      parser.whitespaceChars('(','(');
      parser.whitespaceChars(')',')');
      parser.eolIsSignificant(true);
      parser.parseNumbers();

      int lineCount = 0;
      boolean firstDimensionFound = false;

      parser.nextToken();
      parser.nextToken();
      if(!parser.sval.equals("#")) {
        System.out.println("Not valid .seg file.");
        return;
      }


      while(parser.nextToken() != StreamTokenizer.TT_EOF) {
       if(!firstDimensionFound) {
          if(parser.sval != null && parser.sval.equals("#")) {
            firstDimensionFound = true;
            this.x = lineCount;
          }
        }

        if(parser.ttype == StreamTokenizer.TT_EOL)
          lineCount++;

      }

//      System.out.println("lineCount: " + lineCount);
//      System.out.println("x : " + this.x);

      this.y = (int)(lineCount / this.x);

      file.close();

    } catch(IOException e) {
      System.out.println(fileName + "not found.");
    } 

  }

  private void read(String fileName) {
    FileInputStream file;
    Reader reader;
    StreamTokenizer parser;

    try {
      file = new FileInputStream(fileName);
      reader = new BufferedReader(new InputStreamReader(file));
      parser = new StreamTokenizer(reader);
      parser.wordChars('#','#');
      parser.whitespaceChars('(','(');
      parser.whitespaceChars(')',')');
      parser.parseNumbers();
      regions = new int[this.y][this.x];

      int i = 0;
      int j = -1;

      parser.nextToken();
      if(!parser.sval.equals("#")) {
        System.out.println("Not valid .seg file.");
        return;
      }

      while(parser.nextToken() != StreamTokenizer.TT_EOF) {
        switch(parser.ttype) {
          // Fill in the numbers in the array
          case StreamTokenizer.TT_NUMBER:
            regions[j][i] = (int)Math.round(parser.nval);
            i++;
            break;

          // This must represent a #
          case StreamTokenizer.TT_WORD:
            i = 0;
            j++;
            break;
        }
      }

      file.close(); 

    } catch(IOException e) {
      System.out.println(fileName + "not found.");
    } 

  }

  public int getRegions () {
    int max = 0;
    for(int i = 0; i < this.y; i++) {
      for(int j = 0; j < this.x; j++) {
        if(regions[i][j] > max) max = regions[i][j];
      }
    }
    return max;
  }

  public String toString() {
    String s = "";
    for(int i = 0; i<x; i++) {
      for(int j = 0; j<y; j++) {
        s = s + regions[i][j] + " ";
      }
      s = s + "\n";
    }
    return s;
  }

  public void print() {
    System.out.println("");
    for(int i = 0; i<x; i++) {
      for(int j = 0; j<y; j++) {
        System.out.print(regions[j][i] + " ");
      }
      System.out.println("");
    }
  }

  public void writeCDF(String fileName) {
    FileOutputStream out;
    PrintStream p;

    try {
      out = new FileOutputStream(fileName);
      p = new PrintStream(out);
      for(int i = 0; i<x; i++) {
        for(int j = 0; j<y; j++) {
          p.print(regions[j][i] + ",");
        }
        p.println("");
      }
    
    }
    catch (Exception c) {
      System.err.println("Error writing file");
    }
   
  }

}
