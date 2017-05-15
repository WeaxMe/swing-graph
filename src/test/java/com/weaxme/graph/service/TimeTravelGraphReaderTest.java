package com.weaxme.graph.service;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;


public class TimeTravelGraphReaderTest {

    @Test
    public void readFunctionFromLineInFile() throws Exception {
        String function = "1 2 3 4 5 6 7 8 9 0 -34 456 2345 221 123";
        Path path = Paths.get("src/test/java/com/weaxme/graph/service/test-line.txt");
        Assert.assertEquals(TimeTravelGraphReader.readFunctionFromFile(path), function);
    }

    @Test
    public void readFunctionFromTextInFile() throws Exception {
        String function = "1 2 3 4 5 6 7 8 9 0 -34 456 2345 221 123 12 34 55 13";
        Path path = Paths.get("src/test/java/com/weaxme/graph/service/test-text.txt");
        Assert.assertEquals(TimeTravelGraphReader.readFunctionFromFile(path), function);
    }
}
