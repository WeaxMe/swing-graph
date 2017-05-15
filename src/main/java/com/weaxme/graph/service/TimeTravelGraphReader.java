package com.weaxme.graph.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class TimeTravelGraphReader {
    private static final Logger LOG = LoggerFactory.getLogger(TimeTravelGraphReader.class);

    public static String readFunctionFromFile(Path path) {
        StringBuilder builder = new StringBuilder("");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path)));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            LOG.error("Can't read function from file: {}", path.toAbsolutePath());
            if (LOG.isDebugEnabled()) e.printStackTrace();
        }
        return builder.toString();
    }
}
