package com.weaxme.graph.component;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.weaxme.graph.application.graph.IGraph;
import com.weaxme.graph.application.IGraphApplication;
import com.weaxme.graph.application.graph.DefaultGodographAxisGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Vitaliy Gonchar
 */
@Singleton
public class TravelTimeGraphCommandWindow extends JFrame implements IGraphCommandWindow {

    private JPanel rootPanel;
    private JFormattedTextField graphFunctionField;
    private JFormattedTextField minField;
    private JFormattedTextField maxField;
    private JFormattedTextField stepField;
    private JButton buildGraphButton;
    private JFormattedTextField delayField;
    private JFormattedTextField pixelStepField;
    private JComboBox lineWidthBox;
    private JFormattedTextField multiplierField;

    @Inject
    public TravelTimeGraphCommandWindow(final IGraphApplication app) {
        super("Travel time graph command component");
        configure(app);
        buildGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String function = (String) graphFunctionField.getValue();
                double min = (Double) minField.getValue();
                double max = (Double) maxField.getValue();
                double step = (Double) stepField.getValue();
                if (step <= 0) step = app.getGraph().getStep();
                app.setGraph(new DefaultGodographAxisGraph(function, min, max, step));
                app.setGraphDelay((Long) delayField.getValue());
                app.repaintGraph(0);
            }
        });
        setContentPane(rootPanel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    private void configure(final IGraphApplication app) {
        IGraph graph = app.getGraph();
        if (graph != null) {
            graphFunctionField.setValue(graph.getGraphFunction());
            minField.setValue(graph.getMin());
            maxField.setValue(graph.getMax());
            stepField.setValue(graph.getStep());
        }
        delayField.setValue(app.getGraphDelay());
        pixelStepField.setValue(app.getMarkPixelStep());
        multiplierField.setValue(app.getPointMultiplier());
        multiplierField.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                Double multiplier = (Double) multiplierField.getValue();
                if (multiplier > 0) {
                    app.setPointMultiplier(multiplier);
                    app.repaintGraphWithoutDelay();
                }
            }
        });
        configureWidthBox(app);
    }

    @SuppressWarnings("unchecked")
    private void configureWidthBox(final IGraphApplication app) {
        for (Integer width : app.getPossibleWidth()) {
            lineWidthBox.addItem(width);
        }
        lineWidthBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Integer width = (Integer) lineWidthBox.getSelectedItem();
                app.setGraphLineWidth(width);
                app.repaintGraphWithoutDelay();
            }
        });
    }

    private void createUIComponents() {
        rootPanel = new JPanel();
    }

    @Override
    public void windowEnable() {
        setVisible(true);
        setEnabled(true);
    }

    @Override
    public void windowDisable() {
        setVisible(false);
        setEnabled(false);
    }

    @Override
    public boolean isWindowEnable() {
        return isVisible() && isEnabled();
    }
}
