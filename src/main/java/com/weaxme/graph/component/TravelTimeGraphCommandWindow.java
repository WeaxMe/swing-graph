package com.weaxme.graph.component;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.weaxme.graph.application.IGraphUpdateListener;
import com.weaxme.graph.application.graph.IGraph;
import com.weaxme.graph.application.IGraphApplication;
import com.weaxme.graph.application.graph.DefaultGodographAxisGraph;
import com.weaxme.graph.component.util.GraphPaintUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Vitaliy Gonchar
 */
@Singleton
public class TravelTimeGraphCommandWindow extends JFrame
        implements IGraphCommandWindow, IGraphUpdateListener {

    private static final Logger LOG = LoggerFactory.getLogger(TravelTimeGraphCommandWindow.class);

    private JPanel rootPanel;
    private JFormattedTextField graphFunctionField;
    private JFormattedTextField minField;
    private JFormattedTextField maxField;
    private JComboBox lineWidthBox;
    private JPanel commandPanel;
    private JButton startButton;
    private JButton stopButton;
    private JComboBox stepComboBox;
    private JComboBox timeComboBox;

    private static final String START  = "Start";
    private static final String PAUSE  = "Pause";
    private static final String RESUME = "Resume";
    private static final String STOP   = "Stop";


    @Inject
    public TravelTimeGraphCommandWindow(final IGraphApplication app) {
        super("Travel time graph command component");
        configure(app);
        configureButtons(app);
        setContentPane(rootPanel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    private void configureButtons(final IGraphApplication app) {
        startButton.setText(START);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (startButton.getText().equals(START)) {
                    String function = (String) graphFunctionField.getValue();
                    double min = (Double) minField.getValue();
                    double max = (Double) maxField.getValue();
                    double step = Double.valueOf(((String) stepComboBox.getSelectedItem())
                            .replace(",", "."));
                    if (step <= 0) step = app.getGraph().getStep();
                    IGraph currentGraph = app.getGraph();
                    if (!currentGraph.equals(function, min, max, step)) {
                        app.setGraph(new DefaultGodographAxisGraph(function, min, max, step));
                    }
                    app.setGraphDelay((Integer) timeComboBox.getSelectedItem());
                    app.repaintGraph(0);
                    startButton.setText(PAUSE);
                } else if (startButton.getText().equals(PAUSE)) {
                    startButton.setText(RESUME);
                } else if (startButton.getText().equals(RESUME)) {
                    startButton.setText(PAUSE);
                }
                pack();
            }
        });
        stopButton.setText(STOP);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!startButton.getText().equals(START)) {
                    startButton.setText(START);
                }
            }
        });
    }


    private void configure(final IGraphApplication app) {
        IGraph graph = app.getGraph();
        if (graph != null) {
            graphFunctionField.setValue(graph.getGraphFunction());
            minField.setValue(graph.getMin());
            maxField.setValue(graph.getMax());
            initStepComboBox(graph);
        }
        initTimeComboBox(app);
        initWidthBox(app);
    }

    @SuppressWarnings("unchecked")
    private void initWidthBox(final IGraphApplication app) {
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

    @SuppressWarnings("unchecked")
    private void initStepComboBox(IGraph graph) {
        for (double i = IGraphApplication.MIN_STEP; i <= IGraphApplication.MAX_STEP; i += IGraphApplication.MIN_STEP) {
            stepComboBox.addItem(GraphPaintUtil.getDoubleString(i));
        }
        stepComboBox.setSelectedItem(GraphPaintUtil.getDoubleString(graph.getStep()));
        LOG.info("stepComboBox: {}", stepComboBox.getSelectedItem());
        LOG.info("GraphPaintUtil.getDoubleFormat(graph.getStep()): {}", GraphPaintUtil.getDoubleString(graph.getStep()));
        LOG.info("step: {}", graph.getStep());
    }

    @SuppressWarnings("unchecked")
    private void initTimeComboBox(IGraphApplication app) {
        for (int i = app.MIN_SECONDS_DELAY; i <= app.MAX_SECONDS_DELAY; i++) {
            timeComboBox.addItem(i);
        }
        timeComboBox.setSelectedItem(app.getGraphDelay());
    }

    private void createUIComponents() {
        rootPanel = new JPanel();
        commandPanel = new JPanel();
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

    @Override
    public void graphUpdate(IGraphApplication app) {
        if (!startButton.equals(START)) {
            startButton.setText(START);
        }
    }
}
