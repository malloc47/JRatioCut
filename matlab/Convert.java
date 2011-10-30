import java.io.*;

public class Convert {

  public static void main(String args[]) {

    if(args.length > 0) {
      // Loop through the files given
      String baseName = args[0];
      System.out.println("Reading: " + baseName + ".seg");
      SEGReader segParse = new SEGReader(baseName+".seg");
      segParse.writeCDF(baseName+".csv");
    }
  }

}
