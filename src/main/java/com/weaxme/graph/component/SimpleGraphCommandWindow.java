package com.weaxme.graph.component;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.weaxme.graph.application.IGraphApplication;
import com.weaxme.graph.service.IGraphCommandWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Vitaliy Gonchar
 */
@Singleton
public class SimpleGraphCommandWindow extends JFrame implements IGraphCommandWindow {
    private JPanel rootPanel;
    private JButton buildGraphButton;
    private JComboBox delayBox;
    private JFormattedTextField startField;
    private JFormattedTextField endField;
    private JFormattedTextField graphFunctionField;
    private JFormattedTextField stepField;

    private static final Logger LOG = LoggerFactory.getLogger(SimpleGraphCommandWindow.class);

    @Inject
    public SimpleGraphCommandWindow(final IGraphApplication app) {
        super("Simple graph command component");
        buildGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                app.repaintGraph();
            }
        });

        setContentPane(rootPanel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
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
