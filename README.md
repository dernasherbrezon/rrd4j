rrd4j-light
=====

This is hard fork of the original [rrd4j](https://github.com/rrd4j/rrd4j). This library supports only reading and writing rrd files.

Original rrd4j library has support for:
- use XML for backend (org.w3c.dom.*)
- render graphs (java.awt.*)

Unfortunately these APIs are not included into the Java 8 Embedded compact1 profile.

### Using with Maven

Add this dependency to your project's POM file:

```xml
<dependency>
	<groupId>com.aerse</groupId>
	<artifactId>rrd4j-light</artifactId>
	<version>3.2</version>
</dependency>
```

### Why RRD4J?

  * Portable files, RRDTool files are not
  * Simple API
  * Supports the same data source types as RRDTool (`COUNTER`, `ABSOLUTE`, `DERIVE`, `GAUGE`)
  * Supports the same consolidation functions as RRDTool (`AVERAGE`, `MIN`, `MAX`, `LAST`) and adds `TOTAL`, `FIRST`
  * Supports almost all RRDTool RPN functions (wiki/see [RPNFuncs](RPNFuncs))
  * Multiple backends, e.g. use MongoDB as data store

### Usage Example

```java
import org.rrd4j.core.*;
import static org.rrd4j.DsType.*;
import static org.rrd4j.ConsolFun.*;
...
// first, define the RRD
RrdDef rrdDef = new RrdDef(rrdPath, 300);
rrdDef.addArchive(AVERAGE, 0.5, 1, 600); // 1 step, 600 rows
rrdDef.addArchive(AVERAGE, 0.5, 6, 700); // 6 steps, 700 rows
rrdDef.addArchive(MAX, 0.5, 1, 600);

// then, create a RrdDb from the definition and start adding data
RrdDb rrdDb = new RrdDb(rrdDef);
Sample sample = rrdDb.createSample();
while (...) {
    sample.setTime(t);
    sample.setValue("inbytes", ...);
    sample.setValue("outbytes", ...);
    sample.update();
}
rrdDb.close();
```

Go through the source of [Demo](https://github.com/dernasherbrezon/rrd4j/blob/master/src/main/java/org/rrd4j/demo/Demo.java) for more examples.

### Supported Backends

Next to memory and file storage, RRD4J supports the following backends (using byte array storage):

  * [MongoDB](http://www.mongodb.org/) - a scalable, high-performance, open source, document-oriented database.
  * [Oracle Berkeley DB](http://www.oracle.com/technetwork/database/berkeleydb/overview/index-093405.html) Java Edition - an open source, embeddable database providing developers with fast, reliable, local persistence with zero administration.

### Clojure

Thanks to the [rrd4clj](https://github.com/maoe/rrd4clj) project Clojure now has a RRD API (using RRD4J). Check out their [examples](https://github.com/maoe/rrd4clj/blob/master/src/clj/rrd4clj/examples.clj).

### Contributing

If you are interested in contributing to RRD4J, start by posting pull requests to issues that are important to you. Subscribe to the [discussion
group](https://groups.google.com/forum/#!forum/rrd4j-discuss) and introduce yourself.

If you can't contribute, please let us know about your RRD4J use case. Always good to hear your stories!

### Graph Examples (from the [JRDS](http://jrds.fr/) project)

![http://jrds.fr/_media/myssqlops.png](http://jrds.fr/_media/myssqlops.png)

![http://jrds.fr/_media/screenshots/meminforam.png](http://jrds.fr/_media/screenshots/meminforam.png)

### License

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
