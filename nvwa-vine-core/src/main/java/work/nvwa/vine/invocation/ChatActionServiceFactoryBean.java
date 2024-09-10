package work.nvwa.vine.invocation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * @author Geng Rong
 */
public class ChatActionServiceFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {
    private final Class<T> chatActionServiceInterface;
    private ApplicationContext applicationContext;

    public ChatActionServiceFactoryBean(Class<T> chatActionServiceInterface) {
        this.chatActionServiceInterface = chatActionServiceInterface;
    }

    @Override
    public T getObject() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(chatActionServiceInterface);
        enhancer.setCallback(new ChatActionInvocationHandler(chatActionServiceInterface, applicationContext));
        return (T) enhancer.create();
    }

    @Override
    public Class<T> getObjectType() {
        return chatActionServiceInterface;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
