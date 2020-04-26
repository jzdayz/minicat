package io.github.jzdayz.minicat.lifecycle;

import org.springframework.context.SmartLifecycle;

public abstract class AbstractLifecycle  implements SmartLifecycle {

    private boolean start = false;

    protected void started(){
        start = true;
    }

    @Override
    public void start() {
        started();
    }

    @Override
    public boolean isRunning() {
        return start;
    }
}
