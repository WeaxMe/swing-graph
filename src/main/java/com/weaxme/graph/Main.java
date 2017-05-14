package com.weaxme.graph;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.weaxme.graph.service.GraphInitModule;
import com.weaxme.graph.application.IGraphApplication;
import com.weaxme.graph.component.IGraphCommandWindow;
import com.weaxme.graph.component.panel.IGraphPanel;
import com.weaxme.graph.application.graph.DefaultGodographAxisGraph;
import com.weaxme.graph.component.GraphWindow;
import com.weaxme.graph.component.TravelTimeGraphCommandWindow;
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
        LOG.info("Start running Swing Graph program");
        final Injector injector = Guice.createInjector(new GraphInitModule());
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Dimension dimension = new Dimension(1280, 800);
                IGraphPanel graphPanel = injector.getInstance(IGraphPanel.class);

                IGraphApplication app = injector.getInstance(IGraphApplication.class)
                        .setGraphPanel(graphPanel)
                        .setGraph(new DefaultGodographAxisGraph("6 13 9 2", 0, 100, 0.01));
                TravelTimeGraphCommandWindow travel = injector.getInstance(TravelTimeGraphCommandWindow.class);
                app.addGraphUpdateListener(travel);
                JFrame frame = new GraphWindow(graphPanel, dimension, travel);
                frame.setVisible(true);
            }
        });
    }
}
