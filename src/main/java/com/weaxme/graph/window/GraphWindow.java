package com.weaxme.graph.window;

import com.weaxme.graph.service.IGraphCommandWindow;
import com.weaxme.graph.service.IGraphPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Vitaliy Gonchar
 */
public class GraphWindow extends JFrame {

    public GraphWindow(IGraphPanel graphPanel, Dimension dimension, final IGraphCommandWindow simple, final IGraphCommandWindow travel) {
        super("Graph");
        add((Component) graphPanel);
        JMenuBar menuBar = new JMenuBar();
        Font font = new Font("Verdana", Font.PLAIN, 11);
        JMenu graphMenu = new JMenu("Graph");
        graphMenu.setFont(font);
        menuBar.add(graphMenu);

        JMenuItem simpleGraphItem = new JMenuItem("Simple graph command window");
        simpleGraphItem.setFont(font);
        graphMenu.add(simpleGraphItem);
        simpleGraphItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (travel.isWindowEnable()) {
                    travel.windowDisable();
                }
                simple.windowEnable();
            }
        });

        JMenuItem travelTimeGraphItem = new JMenuItem("Travel time graph command window");
        travelTimeGraphItem.setFont(font);
        graphMenu.add(travelTimeGraphItem);
        travelTimeGraphItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (simple.isWindowEnable()) {
                    simple.windowDisable();
                }
                travel.windowEnable();
            }
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        graphMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        setJMenuBar(menuBar);
        setSize(dimension);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
