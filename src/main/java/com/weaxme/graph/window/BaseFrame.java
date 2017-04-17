package com.weaxme.graph.window;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Vitaliy Gonchar
 */
public abstract class BaseFrame extends JFrame {

    public BaseFrame(String title) {
        super(title);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        setVisible(true);
    }

    /**
     * Init components
     */
    protected abstract void init();
}
