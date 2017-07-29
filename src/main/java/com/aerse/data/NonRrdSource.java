package com.aerse.data;

interface NonRrdSource {
    /**
     * <p>calculate.</p>
     *
     * @param tStart a long.
     * @param tEnd a long.
     * @param dataProcessor a {@link com.aerse.data.DataProcessor} object.
     */
    void calculate(long tStart, long tEnd, DataProcessor dataProcessor);
}
