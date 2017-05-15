package com.weaxme.graph.application;

public interface IGraphUpdater extends Runnable {
    public IGraphUpdater setDelayForBuild(long delay);
    public IGraphUpdater setStop(boolean stop);
    public IGraphUpdater setPause(boolean pause);
    public boolean isUpdated();
}
