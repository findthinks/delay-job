package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.lib.utils.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class RoundRobinSelector {

    private final AtomicInteger nextServerCyclicCounter = new AtomicInteger(0);

    public <T> T chooseOne(List<T> servers) {
        if (CollectionUtils.isEmpty(servers)) {
            return null;
        }

        if (servers.size() == 1) {
            return servers.get(0);
        }

        T server = null;
        while (server == null) {
            server = servers.get(incrementAndGetModulo(servers.size()));
        }
        return server;
    }

    private int incrementAndGetModulo(int modulo) {
        for (;;) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }
    }
}
