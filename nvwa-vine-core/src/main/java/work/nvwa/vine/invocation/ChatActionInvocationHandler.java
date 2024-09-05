package work.nvwa.vine.invocation;


import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;
import work.nvwa.vine.SerializationType;
import work.nvwa.vine.annotation.ChatAction;
import work.nvwa.vine.annotation.ChatActionService;
import work.nvwa.vine.chat.ChatMessage;
import work.nvwa.vine.chat.client.VineChatClient;
import work.nvwa.vine.context.SchemaContext;
import work.nvwa.vine.metadata.ChatActionMetadata;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActionInvocationHandler implements MethodInterceptor {

    private final Map<Method, ChatActionMetadata> methodMetadataMap = new HashMap<>();
    private final ApplicationContext applicationContext;

    private SchemaContext schemaContext;
    private VineChatClient chatClient;

    public ChatActionInvocationHandler(Class<?> chatActionServiceInterface, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        ChatActionService chatActionService = chatActionServiceInterface.getAnnotation(ChatActionService.class);
        for (Method method : chatActionServiceInterface.getMethods()) {
            ChatActionMetadata metadata = buildChatActionMetadata(chatActionService, method);
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
        ChatActionMetadata metadata = methodMetadataMap.get(method);
        List<ChatMessage> messages = new ArrayList<>();
        if (!metadata.getSystemPrompt().isBlank()) {
            messages.add(ChatMessage.systemMessage(metadata.getSystemPrompt()));
        }
        messages.add(ChatMessage.userMessage(schemaContext.buildUserMessage(metadata, argumentMap)));
        return getChatClient().call(messages, metadata);
    }

    private ChatActionMetadata buildChatActionMetadata(ChatActionService chatActionService, Method method) {
        String systemPrompt = chatActionService.systemPrompt();
        String userPrompt = chatActionService.userPromptPrefix();
        String clientLevel = chatActionService.clientLevel();
        ChatAction chatAction = method.getAnnotation(ChatAction.class);
        String task = null;
        SerializationType serializationType;
        if (chatAction != null) {
            task = chatAction.task();
            clientLevel = chatAction.clientLevel();
            serializationType = chatAction.serializationType();
            systemPrompt = getSchemaContext().buildSystemPrompt(method, systemPrompt, serializationType, chatAction.examples());
        } else {
            serializationType = SerializationType.Yaml;
            systemPrompt = getSchemaContext().buildSystemPrompt(method, systemPrompt, serializationType, null);
        }
        if (task == null || task.isEmpty()) {
            task = method.getName();
        }


        if (userPrompt.isEmpty()) {
            userPrompt = task;
        } else {
            userPrompt += "\n" + task;
        }

        TypeReference<?> returnTypeRef = new TypeReference<>() {
            @Override
            public Type getType() {
                return method.getGenericReturnType();
            }
        };

        return new ChatActionMetadata(userPrompt, systemPrompt, clientLevel, serializationType, returnTypeRef);
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
