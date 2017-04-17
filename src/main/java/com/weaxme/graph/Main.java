package com.weaxme.graph;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.weaxme.graph.service.GraphInitModule;
import com.weaxme.graph.service.IGraphApplication;
import com.weaxme.graph.service.IGraphPanel;
import com.weaxme.graph.service.impl.DefaultGodographAxisGraph;
import com.weaxme.graph.window.SimpleGraphCommandWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * @author Vitaliy Gonchar
 */
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("Start running wing schedule program");
        final Injector injector = Guice.createInjector(new GraphInitModule());
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Graph");
                Dimension dimension = new Dimension(1280, 800);
                frame.setSize(dimension);
                IGraphPanel graphPanel = injector.getInstance(IGraphPanel.class);
                frame.add((Component) graphPanel);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);
                injector.getInstance(IGraphApplication.class)
                        .setGraphPanel(graphPanel)
                        .setGraph(new DefaultGodographAxisGraph("6 13 9 2"), -100, 100, 0.01)
                        .repaintGraph();
                injector.getInstance(SimpleGraphCommandWindow.class).setVisible(true);
            }
        });
    }
}
