package com.aerse.demo;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


class Test {
    /** Constant <code>LOADAVG_FILE="/proc/loadavg"</code> */
    public static final String LOADAVG_FILE = "/proc/loadavg";

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.io.IOException if any.
     */
    public static void main(String[] args) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(LOADAVG_FILE));
        try {
            String line = r.readLine();
            if (line != null) {
                String[] loads = line.split("\\s+");
                if (loads.length >= 3) {
                    String load = loads[0] + " " + loads[1] + " " + loads[2];
                    System.out.println("LOAD = " + load);
                    return;
                }
                System.out.println("Unexpected error while parsing file " + LOADAVG_FILE);
            }
        }
        finally {
            r.close();
        }
    }
}
