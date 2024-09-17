package work.nvwa.vine.context;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.cglib.proxy.MethodInterceptor;
import work.nvwa.vine.SerializationType;
import work.nvwa.vine.VineFunctionExample;
import work.nvwa.vine.annotation.VineFunction;
import work.nvwa.vine.annotation.VineService;
import work.nvwa.vine.chat.client.VineChatClient;
import work.nvwa.vine.config.VineConfig;
import work.nvwa.vine.invocation.VineServiceInvocationHandler;
import work.nvwa.vine.metadata.VineFunctionMetadata;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class InvocationContext {

    private final VineConfig vineConfig;
    private final VineChatClient chatClient;
    private final SchemaContext schemaContext;

    public InvocationContext(VineConfig vineConfig, VineChatClient chatClient, SchemaContext schemaContext) {
        this.vineConfig = vineConfig;
        this.chatClient = chatClient;
        this.schemaContext = schemaContext;
    }

    public MethodInterceptor buildInterceptor(Class<?> vineServiceInterface) {
        Map<Method, VineFunctionMetadata> methodMetadataMap = new HashMap<>();
        VineService vineService = vineServiceInterface.getAnnotation(VineService.class);
        for (Method method : vineServiceInterface.getMethods()) {
            VineFunctionMetadata metadata = buildChatActionMetadata(vineService, method);
            methodMetadataMap.put(method, metadata);
        }
        return new VineServiceInvocationHandler(schemaContext, chatClient, methodMetadataMap);
    }

    private VineFunctionMetadata buildChatActionMetadata(VineService vineService, Method method) {
        String systemPrompt = vineService.systemPromptPrefix();
        String userPrompt = vineService.userPromptPrefix();
        String clientLevel = vineService.clientLevel();
        VineFunction vineFunction = method.getAnnotation(VineFunction.class);
        String mission;
        String schemaPrompt;
        SerializationType serializationType;
        boolean enableThought;
        Class<? extends VineFunctionExample>[] examples = null;
        if (vineFunction != null) {
            clientLevel = vineFunction.clientLevel();
            enableThought = vineFunction.enableThought();
            serializationType = vineFunction.serializationType();
            examples = vineFunction.examples();
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
        } else {
            serializationType = vineService.serializationType();
            mission = method.getName();
            enableThought = false;
        }
        if (serializationType == SerializationType.Default) {
            serializationType = vineConfig.defaultSerializationType();
        }
        TypeReference<?> returnTypeRef = new TypeReference<>() {
            @Override
            public Type getType() {
                return method.getGenericReturnType();
            }
        };
        schemaPrompt = schemaContext.buildSchemaPrompt(method, serializationType, examples);
        return new VineFunctionMetadata(userPrompt, systemPrompt, schemaPrompt, mission, clientLevel, enableThought, serializationType, returnTypeRef);
    }
}
