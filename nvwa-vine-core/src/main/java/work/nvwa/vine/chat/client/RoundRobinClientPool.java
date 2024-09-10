package work.nvwa.vine.chat.client;

import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Geng Rong
 */
public class RoundRobinClientPool {

    private final AtomicInteger nextClientCyclicCounter = new AtomicInteger(0);
    private final List<SingletonVineChatClient> clients;
    private final int modulo;

    public RoundRobinClientPool(List<SingletonVineChatClient> clients) {
        Assert.notEmpty(clients, "Chat model clients must not be empty");
        this.clients = clients;
        this.modulo = clients.size();
    }

    public SingletonVineChatClient next() {
        return clients.get(incrementAndGetModulo());
    }

    private int incrementAndGetModulo() {
        while (true) {
            int current = nextClientCyclicCounter.get();
            int next = (current + 1) % this.modulo;
            if (nextClientCyclicCounter.compareAndSet(current, next)) {
                return next;
            }
        }
    }
}
