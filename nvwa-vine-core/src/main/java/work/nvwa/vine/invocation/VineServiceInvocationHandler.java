package work.nvwa.vine.invocation;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import work.nvwa.vine.chat.ChatMessage;
import work.nvwa.vine.chat.client.VineChatClient;
import work.nvwa.vine.context.SchemaContext;
import work.nvwa.vine.metadata.VineFunctionMetadata;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author Geng Rong
 */
public class VineServiceInvocationHandler implements MethodInterceptor {

    private final SchemaContext schemaContext;
    private final VineChatClient chatClient;
    private final Map<Method, VineFunctionMetadata> methodMetadataMap;

    public VineServiceInvocationHandler(SchemaContext schemaContext, VineChatClient chatClient, Map<Method, VineFunctionMetadata> methodMetadataMap) {
        this.schemaContext = schemaContext;
        this.chatClient = chatClient;
        this.methodMetadataMap = methodMetadataMap;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws InvocationTargetException, IllegalAccessException {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(obj, args);
        }
        Map<String, Object> argumentMap = new TreeMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            String name = parameters[i].getName();
            argumentMap.put(name, args[i]);
        }
        VineFunctionMetadata metadata = methodMetadataMap.get(method);
        if (metadata.enableThought()) {
            return callWithThought(metadata, argumentMap);
        }
        return call(metadata, argumentMap);
    }

    private Object call(VineFunctionMetadata metadata, Map<String, Object> argumentMap) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(schemaContext.buildSystemMessageWithSchema(metadata));
        messages.add(schemaContext.buildUserMessage(metadata, argumentMap));
        return chatClient.call(messages, metadata);
    }

    private Object callWithThought(VineFunctionMetadata metadata, Map<String, Object> argumentMap) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(schemaContext.buildSystemMessageWithThought(metadata));
        messages.add(schemaContext.buildUserMessage(metadata, argumentMap));
        messages.add(chatClient.callMessage(messages, metadata));
        messages.add(schemaContext.buildResultMessage(metadata));
        return chatClient.call(messages, metadata);
    }

}
