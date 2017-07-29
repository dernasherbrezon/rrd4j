package com.aerse.core;

import java.io.IOException;

interface RrdUpdater {
    /**
     * <p>getRrdBackend.</p>
     *
     * @return a {@link com.aerse.core.RrdBackend} object.
     */
    RrdBackend getRrdBackend();

    /**
     * <p>copyStateTo.</p>
     *
     * @param updater a {@link com.aerse.core.RrdUpdater} object.
     * @throws java.io.IOException if any.
     */
    void copyStateTo(RrdUpdater updater) throws IOException;

    /**
     * <p>getRrdAllocator.</p>
     *
     * @return a {@link com.aerse.core.RrdAllocator} object.
     */
    RrdAllocator getRrdAllocator();
}
