package com.aerse.data;

import com.aerse.ConsolFun;
import com.aerse.core.Util;

/**
 * Simple class which holds aggregated values (MIN, MAX, FIRST, LAST, AVERAGE and TOTAL). You
 * don't need to create objects of this class directly. Objects of this class are returned from
 * <code>getAggregates()</code> method in
 * {@link com.aerse.core.FetchData#getAggregates(String) FetchData} and
 * {@link com.aerse.data.DataProcessor#getAggregates(String)} DataProcessor classes.
 *
 */
public class Aggregates {
    double min = Double.NaN, max = Double.NaN;
    double first = Double.NaN, last = Double.NaN;
    double average = Double.NaN, total = Double.NaN;

    /**
     * Returns the minimal value
     *
     * @return Minimal value
     */
    public double getMin() {
        return min;
    }

    /**
     * Returns the maximum value
     *
     * @return Maximum value
     */
    public double getMax() {
        return max;
    }

    /**
     * Returns the first value
     *
     * @return First value
     */
    public double getFirst() {
        return first;
    }

    /**
     * Returns the last value
     *
     * @return Last value
     */
    public double getLast() {
        return last;
    }

    /**
     * Returns average
     *
     * @return Average value
     */
    public double getAverage() {
        return average;
    }

    /**
     * Returns total value
     *
     * @return Total value
     */
    public double getTotal() {
        return total;
    }


    /**
     * Returns single aggregated value for the give consolidation function
     *
     * @param consolFun Consolidation function: MIN, MAX, FIRST, LAST, AVERAGE, TOTAL. These constants
     *                  are conveniently defined in the {@link com.aerse.ConsolFun ConsolFun} interface.
     * @return Aggregated value
     * @throws java.lang.IllegalArgumentException Thrown if unsupported consolidation function is supplied
     */
    public double getAggregate(ConsolFun consolFun) {
        switch (consolFun) {
        case AVERAGE:
            return average;
        case FIRST:
            return first;
        case LAST:
            return last;
        case MAX:
            return max;
        case MIN:
            return min;
        case TOTAL:
            return total;
        }
        throw new IllegalArgumentException("Unknown consolidation function: " + consolFun);
    }

    /**
     * Returns String representing all aggregated values. Just for debugging purposes.
     *
     * @return String containing all aggregated values
     */
    public String dump() {
        StringBuilder bl = new StringBuilder();
        for(ConsolFun cf: ConsolFun.values()) {
            bl.append(cf.name() + '=' + Util.formatDouble(this.getAggregate(cf)));
        }
        return bl.toString();
    }
}
