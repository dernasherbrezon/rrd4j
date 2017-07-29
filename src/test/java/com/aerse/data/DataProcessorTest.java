package com.aerse.data;

import static com.aerse.ConsolFun.AVERAGE;
import static com.aerse.DsType.GAUGE;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.aerse.ConsolFun;
import com.aerse.core.FetchRequest;
import com.aerse.core.RrdBackendFactory;
import com.aerse.core.RrdDb;
import com.aerse.core.RrdDef;
import com.aerse.data.DataProcessor;

public class DataProcessorTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testMemoryDataprocess() throws IOException {
        RrdDef rrdDef = new RrdDef(testFolder.newFile("testBuild.rrd").getCanonicalPath());
        rrdDef.addDatasource("sun", GAUGE, 600, 0, Double.NaN);
        rrdDef.addArchive(AVERAGE, 0.5, 1, 600);
        try (RrdDb rrdDb = new RrdDb(rrdDef, RrdBackendFactory.getFactory("MEMORY"))){
            FetchRequest fr = rrdDb.createFetchRequest(ConsolFun.AVERAGE, 10000, 20000);
            DataProcessor dp = new DataProcessor(10000, 20000);
            dp.addDatasource("sun", fr.fetchData());
            dp.processData();
        }
    }

}
