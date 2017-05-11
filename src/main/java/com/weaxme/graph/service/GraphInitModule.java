package com.weaxme.graph.service;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.weaxme.graph.component.SimpleGraphCommandWindow;
import com.weaxme.graph.component.TravelTimeGraphCommandWindow;
import com.weaxme.graph.component.panel.GraphPanel;

/**
 * @author Vitaliy Gonchar
 */
public class GraphInitModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IGraphPanel.class).to(GraphPanel.class).in(Singleton.class);
        bind(SimpleGraphCommandWindow.class).in(Singleton.class);
        bind(TravelTimeGraphCommandWindow.class).in(Singleton.class);
    }
}
