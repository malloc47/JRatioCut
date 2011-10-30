import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class JRead {

  private static boolean debug = true;
  private static final double defaultAlpha = 0.5;

  public static void main(String args[]) {

    // Setup the argument parser 
    OptionParser parser = new OptionParser("qvA:N:T:G:r:");
    parser.accepts("help");
    OptionSpec<Integer> area = parser.accepts("A").withRequiredArg().ofType( Integer.class );
    OptionSpec<Integer> number = parser.accepts("N").withRequiredArg().ofType( Integer.class );
    OptionSpec<Integer> cutThresh = parser.accepts("T").withRequiredArg().ofType( Integer.class );
    OptionSpec<Integer> sigma = parser.accepts("G").withRequiredArg().ofType( Integer.class );
    OptionSpec<Double> alpha = parser.accepts("r").withRequiredArg().ofType( Double.class );

    OptionSet options = parser.parse(args);

    if (options.has("help")) {
      System.out.println(helpMsg);
    }
    // If no filename(s) is/are given, then report this
    else if(options.nonOptionArguments().isEmpty()){
      System.err.println("jmrc-read: You must specify an input-pathname");
    }

    debug = !options.has("q");
    Params.debug = debug;
    Params.verbose = options.has("v");

    // Loop through the files given
    for(int i = 0; i < options.nonOptionArguments().size(); i++) {
      String baseName = options.nonOptionArguments().get(i);
      if(debug) System.out.println("Reading: " + baseName + ".pgm");
      PGMReader pgmParse = new PGMReader(baseName+".pgm");
      if(debug) System.out.println("Reading: " + baseName + ".seg");
      SEGReader segParse = new SEGReader(baseName+".seg");
      int numRegions = segParse.getRegions();
      if(debug) System.out.println("\t" + numRegions + " regions found.");
//      if(debug) System.out.println("Reading: " + baseName + ".w");
//      SEGReader wParse = new SEGReader(baseName+".w");

      if(debug) System.out.println("Creating graph.");
      Graph graph = new Graph();
      if(debug) System.out.println("\tCreating nodes.");
      graph.createNodes(numRegions);
      if(debug) System.out.println("\tCalculating adjacencies.");
      graph.setupNodes(segParse.regions,pgmParse.image);
      if(debug) System.out.println("\tSetting up initial edge weights");

      // Default to alpha=0.5 
      if(options.has(alpha) && options.hasArgument(alpha)) {
        System.out.println("\t\tUsing alpha="+options.valueOf(alpha));
        graph.calculateWeights(options.valueOf(alpha));
      }
      else {
        graph.calculateWeights(0.5);
      }

      if(debug) System.out.println("Merging regions.");

      if(options.has(area) && options.hasArgument(area)) {
        if(debug) System.out.println("\tMerging on area.");
        graph.areaMergeMax(options.valueOf(area));
      }

      if(options.has(cutThresh) && options.hasArgument(cutThresh)) {
        if(debug) System.out.println("\tMerging on cut ratio.");
        graph.edgeThresholdMerge(options.valueOf(cutThresh));
      }

      if(options.has(number) && options.hasArgument(number)) {
        if(debug) System.out.println("\tMerging on target number.");
        graph.edgeNumberMerge(options.valueOf(number));
      }

      graph.writePGM(baseName + "-all.ppm",pgmParse.image);
/*      for(int j = 0; j < graph.numNodes() ; j++) {
        graph.writePGMRegion("test/"+baseName + "-"+j+".ppm",pgmParse.image,j);
      }
*/
      graph.writeSEG(baseName+"-raw.seg");

    }
    
  }

  private static final String helpMsg = "The mrc-read command reads and displays segmentations produced by minimum\nratio cut.\n\nParameters:\n  -help\n    Print this message.\n  -q\n    Quiet mode. Disable diagnostic printing.\n  -A area-threshold\n    Specify the threshold for eliminating small regions. Regions whose area\n    is less than this threshold are merged with the neighbor with /* smallest */ largest\n    cut ratio. By default, disable this process.\n  -N num-regions\n    Specify a target number of regions. The merging process will continue\n    until there are this many regions or fewer. By default disable this\n    process. (not reimplemented)\n  -T threshold\n    Specify a cut-ratio threshold for the merging process. The merging process\n    will continue until the maximal cut ratio between any two regions is less\n    than this threshold. By default, disable this process.\n  Note that merging due to -A is performed first, followed by merging due to\n    -N, followed by merging due to -T.\n  -G sigma\n    Use a Gaussian edge-weight function with parameter sigma for merging.\n    By default, use a linear edge-weight function. (not reimplemented)\n  -r alpha\n    The blending ratio used to control the influence of edge weights from the\n    first iteration for merging. The valid range is [0,1]. The default is 0.5.\n    If alpha=1, the edge weights from the first iteration are used.\n\nAuthors: Jarrell Waggoner, Song Wang, and Jeffrey Mark Siskind\nDate: January 2009";

}
