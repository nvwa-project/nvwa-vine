package work.nvwa.vine.invocation;


import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;
import work.nvwa.vine.SerializationType;
import work.nvwa.vine.annotation.VineFunction;
import work.nvwa.vine.annotation.VineService;
import work.nvwa.vine.chat.ChatMessage;
import work.nvwa.vine.chat.client.VineChatClient;
import work.nvwa.vine.context.SchemaContext;
import work.nvwa.vine.metadata.VineFunctionMetadata;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Geng Rong
 */
public class VineServiceInvocationHandler implements MethodInterceptor {

    private final Map<Method, VineFunctionMetadata> methodMetadataMap = new HashMap<>();
    private final ApplicationContext applicationContext;

    private SchemaContext schemaContext;
    private VineChatClient chatClient;

    public VineServiceInvocationHandler(Class<?> vineServiceInterface, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        VineService vineService = vineServiceInterface.getAnnotation(VineService.class);
        for (Method method : vineServiceInterface.getMethods()) {
            VineFunctionMetadata metadata = buildChatActionMetadata(vineService, method);
            methodMetadataMap.put(method, metadata);
        }
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws InvocationTargetException, IllegalAccessException {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(obj, args);
        }
        Map<String, Object> argumentMap = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            String name = parameters[i].getName();
            argumentMap.put(name, args[i]);
        }
        VineFunctionMetadata metadata = methodMetadataMap.get(method);
        List<ChatMessage> messages = new ArrayList<>();
        if (!metadata.systemPrompt().isBlank()) {
            messages.add(ChatMessage.systemMessage(metadata.systemPrompt()));
        }
        messages.add(ChatMessage.userMessage(schemaContext.buildUserMessage(metadata, argumentMap)));
        return getChatClient().call(messages, metadata);
    }

    private VineFunctionMetadata buildChatActionMetadata(VineService vineService, Method method) {
        String systemPrompt = vineService.systemPromptPrefix();
        String userPrompt = vineService.userPromptPrefix();
        String clientLevel = vineService.clientLevel();
        VineFunction vineFunction = method.getAnnotation(VineFunction.class);
        String mission;
        SerializationType serializationType;
        if (vineFunction != null) {
            clientLevel = vineFunction.clientLevel();
            serializationType = vineFunction.serializationType();
            if (!vineFunction.systemPromptPrefix().isEmpty()) {
                systemPrompt = vineFunction.systemPromptPrefix();
            }
            if (!vineFunction.userPromptPrefix().isEmpty()) {
                systemPrompt = vineFunction.userPromptPrefix();
            }
            if (vineFunction.mission().isEmpty()) {
                mission = method.getName();
            } else {
                mission = vineFunction.mission();
            }
            // append examples
            systemPrompt = getSchemaContext().buildSystemPrompt(method, systemPrompt, mission, serializationType, vineFunction.examples());
        } else {
            serializationType = SerializationType.Json;
            mission = method.getName();
            systemPrompt = getSchemaContext().buildSystemPrompt(method, systemPrompt, mission, serializationType, null);
        }

        TypeReference<?> returnTypeRef = new TypeReference<>() {
            @Override
            public Type getType() {
                return method.getGenericReturnType();
            }
        };

        return new VineFunctionMetadata(userPrompt, systemPrompt, clientLevel, serializationType, returnTypeRef);
    }

    private VineChatClient getChatClient() {
        if (chatClient == null) {
            this.chatClient = applicationContext.getBean(VineChatClient.class);
        }
        return chatClient;
    }

    private SchemaContext getSchemaContext() {
        if (schemaContext == null) {
            this.schemaContext = applicationContext.getBean(SchemaContext.class);
        }
        return schemaContext;
    }
}
