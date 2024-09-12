package work.nvwa.vine.invocation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * @author Geng Rong
 */
public class ChatServiceFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {
    private final Class<T> chatServiceInterface;
    private ApplicationContext applicationContext;

    public ChatServiceFactoryBean(Class<T> chatServiceInterface) {
        this.chatServiceInterface = chatServiceInterface;
    }

    @Override
    public T getObject() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(chatServiceInterface);
        enhancer.setCallback(new VineServiceInvocationHandler(chatServiceInterface, applicationContext));
        return (T) enhancer.create();
    }

    @Override
    public Class<T> getObjectType() {
        return chatServiceInterface;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
