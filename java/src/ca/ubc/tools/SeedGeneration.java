package ca.ubc.tools;

import java.util.Random;

/**
 * Generate N independent seeds
 */
public class SeedGeneration {
  public static void main(String[] args) {
    final long startTime = System.currentTimeMillis();

    int numSeeds = 20;

    if (args.length > 0) {
      numSeeds = Integer.parseInt(args[0]);
    }

    long[] seeds = new long[numSeeds];
    seeds[0] = startTime;

    Random r = new Random();
    long numMillisInDay = 24 * 3600 * 1000;

    System.out.println(startTime);
    for (int i=1; i<numSeeds; i++) {
      long prevSeed = seeds[i-1];
      long nextLong = r.nextLong();
      long nextLongWithMod = nextLong % numMillisInDay;
      long nextSeed = prevSeed + numMillisInDay + nextLongWithMod;
      seeds[i] = nextSeed;
      System.out.println(nextSeed);
    }
  }
}
