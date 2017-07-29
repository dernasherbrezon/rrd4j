package com.aerse.demo;

import static com.aerse.ConsolFun.AVERAGE;
import static com.aerse.ConsolFun.MAX;
import static com.aerse.ConsolFun.TOTAL;
import static com.aerse.DsType.GAUGE;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import com.aerse.core.FetchData;
import com.aerse.core.FetchRequest;
import com.aerse.core.RrdDb;
import com.aerse.core.RrdDef;
import com.aerse.core.RrdSafeFileBackend;
import com.aerse.core.Sample;
import com.aerse.core.Util;

/**
 * Simple demo just to check that everything is OK with this library. Creates two files in your
 * $HOME/rrd4j-demo directory: demo.rrd and demo.png.
 */
public class Demo {
    static final long SEED = 1909752002L;
    static final Random RANDOM = new Random(SEED);
    static final String FILE = "demo";

    static final long START = Util.getTimestamp(2010, 4, 1);
    static final long END = Util.getTimestamp(2010, 6, 1);
    static final int MAX_STEP = 300;

    static final int IMG_WIDTH = 500;
    static final int IMG_HEIGHT = 300;
    private static final String SHADE = "shade";
    private static final String SUNMAX = "sunmax";
    private static final String SUNAVERAGE = "sunaverage";
    private static final String SHADEMAX = "shademax";
    private static final String SHADEVERAGE = "shadeverage";

    private Demo() {}

    /**
     * <p>To start the demo, use the following command:</p>
     * <pre>
     * java -cp rrd4j-{version}.jar org.rrd4j.demo.Demo
     * </pre>
     *
     * @param args the name of the backend factory to use (optional)
     * @throws java.io.IOException Thrown
     */
    public static void main(String[] args) throws IOException {
        println("== Starting demo");
        long startMillis = System.currentTimeMillis();
        if (args.length > 0) {
            println("Setting default backend factory to " + args[0]);
            RrdDb.setDefaultFactory(args[0]);
        }
        long start = START;
        long end = END;
        String rrdPath = Util.getRrd4jDemoPath(FILE + ".rrd");
        String rrdRestoredPath = Util.getRrd4jDemoPath(FILE + "_restored.rrd");
        String logPath = Util.getRrd4jDemoPath(FILE + ".log");
        PrintWriter log = new PrintWriter(new BufferedOutputStream(new FileOutputStream(logPath, false)));
        // creation
        println("== Creating RRD file " + rrdPath);
        RrdDef rrdDef = new RrdDef(rrdPath, start - 1, 300);
        rrdDef.setVersion(2);
        rrdDef.addDatasource("sun", GAUGE, 600, 0, Double.NaN);
        rrdDef.addDatasource(SHADE, GAUGE, 600, 0, Double.NaN);
        rrdDef.addArchive(AVERAGE, 0.5, 1, 600);
        rrdDef.addArchive(AVERAGE, 0.5, 6, 700);
        rrdDef.addArchive(AVERAGE, 0.5, 24, 775);
        rrdDef.addArchive(AVERAGE, 0.5, 288, 797);
        rrdDef.addArchive(TOTAL, 0.5, 1, 600);
        rrdDef.addArchive(TOTAL, 0.5, 6, 700);
        rrdDef.addArchive(TOTAL, 0.5, 24, 775);
        rrdDef.addArchive(TOTAL, 0.5, 288, 797);
        rrdDef.addArchive(MAX, 0.5, 1, 600);
        rrdDef.addArchive(MAX, 0.5, 6, 700);
        rrdDef.addArchive(MAX, 0.5, 24, 775);
        rrdDef.addArchive(MAX, 0.5, 288, 797);
        println(rrdDef.dump());
        log.println(rrdDef.dump());
        println("Estimated file size: " + rrdDef.getEstimatedSize());
        try (RrdDb rrdDb = new RrdDb(rrdDef)){
            println("== RRD file created.");
            if (rrdDb.getRrdDef().equals(rrdDef)) {
                println("Checking RRD file structure... OK");
            } else {
                println("Invalid RRD file created. This is a serious bug, bailing out");
                log.close();
                return;
            }
        }
        println("== RRD file closed.");

        // update database
        GaugeSource sunSource = new GaugeSource(1200, 20);
        GaugeSource shadeSource = new GaugeSource(300, 10);
        println("== Simulating one month of RRD file updates with step not larger than " +
                MAX_STEP + " seconds (* denotes 1000 updates)");
        long t = start;
        int n = 0;
        try (RrdDb rrdDb = new RrdDb(rrdPath)){
            Sample sample = rrdDb.createSample();
            while (t <= end + 172800L) {
                sample.setTime(t);
                sample.setValue("sun", sunSource.getValue());
                sample.setValue(SHADE, shadeSource.getValue());
                log.println(sample.dump());
                sample.update();
                t += RANDOM.nextDouble() * MAX_STEP + 1;
                if (((++n) % 1000) == 0) {
                    System.out.print("*");
                }
            } 
        }

        println("");
        println("== Finished. RRD file updated " + n + " times");

        // test read-only access!
        RrdDb rrdDb = new RrdDb(rrdPath, true);
        println("File reopen in read-only mode");
        println("== Last update time was: " + rrdDb.getLastUpdateTime());
        println("== Last info was: " + rrdDb.getInfo());

        // fetch data
        println("== Fetching data for the whole month");
        FetchRequest request = rrdDb.createFetchRequest(AVERAGE, start, end);
        println(request.dump());
        log.println(request.dump());
        FetchData fetchData = request.fetchData();
        println("== Data fetched. " + fetchData.getRowCount() + " points obtained");
        println(fetchData.toString());
        println("== Fetch completed");

        // close files
        println("== Closing both RRD files");
        rrdDb.close();
        println("== First file closed");

       
        println("== Locks info ==");
        println(RrdSafeFileBackend.getLockInfo());
        // demo ends
        log.close();
        println("== Demo completed in " +
                ((System.currentTimeMillis() - startMillis) / 1000.0) + " sec");
    }

    static void println(String msg) {
        //System.out.println(msg + " " + Util.getLapTime());
        System.out.println(msg);
    }

    static void print(String msg) {
        System.out.print(msg);
    }

    static class GaugeSource {
        private double value;
        private double step;

        GaugeSource(double value, double step) {
            this.value = value;
            this.step = step;
        }

        long getValue() {
            double oldValue = value;
            double increment = RANDOM.nextDouble() * step;
            if (RANDOM.nextDouble() > 0.5) {
                increment *= -1;
            }
            value += increment;
            if (value <= 0) {
                value = 0;
            }
            return Math.round(oldValue);
        }
    }

}

