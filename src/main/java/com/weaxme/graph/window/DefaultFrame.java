package com.weaxme.graph.window;

import javax.swing.*;

/**
 * @author Vitaliy Gonchar
 */
public class DefaultFrame extends BaseFrame {
    public DefaultFrame(String title) {
        super(title);
    }

    @Override
    protected void init() {
        JLabel jLabel = new JLabel("Hello, World!");
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Default frame"));
        getContentPane().add(panel);
        add(jLabel);
        pack();
    }
}