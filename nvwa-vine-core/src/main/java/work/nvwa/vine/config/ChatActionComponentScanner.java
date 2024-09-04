package work.nvwa.vine.config;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import work.nvwa.vine.annotation.ChatActionService;
import work.nvwa.vine.annotation.NoChatModelService;
import work.nvwa.vine.invocation.ChatActionServiceFactoryBean;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gr@fastball.dev
 * @since 2024/8/31
 */
public class ChatActionComponentScanner extends ClassPathBeanDefinitionScanner {

    private static final Class<? extends Annotation>[] INCLUDED_ANNOTATION_TYPES = new Class[]{};

    private final BeanNameGenerator beanNameGenerator = FullyQualifiedAnnotationBeanNameGenerator.INSTANCE;
    private final BeanDefinitionRegistry registry;

    public ChatActionComponentScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        addIncludeFilter(new AnnotationTypeFilter(ChatActionService.class));
        this.registry = registry;
    }

    public void scan(ResourceLoader resourceLoader) {
        setResourceLoader(resourceLoader);
        String[] packages = findBasePackages(registry);
        scan(packages);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                BeanDefinitionHolder definitionHolder = registerBeanDefinition(candidate);
                if (definitionHolder != null) {
                    beanDefinitions.add(definitionHolder);
                }
            }
        }
        return beanDefinitions;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isInterface() && metadata.hasAnnotation(ChatActionService.class.getName()) && !metadata.hasAnnotation(NoChatModelService.class.getName());
    }

    private String[] findBasePackages(BeanDefinitionRegistry registry) {
        Set<String> packages = new LinkedHashSet<>();
        for (String beanDefinitionName : registry.getBeanDefinitionNames()) {
            BeanDefinition definition = registry.getBeanDefinition(beanDefinitionName);
            packages.addAll(parseDefinition(definition));
        }
        return packages.toArray(new String[]{});
    }

    private BeanDefinitionHolder registerBeanDefinition(BeanDefinition candidate) {
        String className = candidate.getBeanClassName();
        String beanName = beanNameGenerator.generateBeanName(candidate, this.registry);
        try {
            Class<?> repositoryInterface = Class.forName(className);
            ChatActionServiceFactoryBean<?> factoryBean = new ChatActionServiceFactoryBean<>(repositoryInterface);
            RootBeanDefinition beanDefinition = new RootBeanDefinition(repositoryInterface);
            beanDefinition.setInstanceSupplier(() -> factoryBean);
            BeanDefinitionHolder definitionHolder = new BeanComponentDefinition(beanDefinition, beanName);
            registerBeanDefinition(definitionHolder, this.registry);
            return definitionHolder;
        } catch (ClassNotFoundException e) {
            logger.error("Failed to register bean definition for " + className, e);
        }
        return null;
    }

    private Set<String> parseDefinition(BeanDefinition definition) {
        if (definition instanceof AnnotatedBeanDefinition) {
            AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) definition;
            return parseComponentScan(annotatedBeanDefinition.getMetadata());
        }
        return Collections.emptySet();
    }

    private Set<String> parseComponentScan(AnnotationMetadata metadata) {
        Set<String> packages = new LinkedHashSet<>();

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(ComponentScan.class.getName(), true));
        if (attributes == null) {
            packages.add(ClassUtils.getPackageName(metadata.getClassName()));
            return packages;
        }

        packages.addAll(Arrays.asList(attributes.getStringArray("value")));
        packages.addAll(Arrays.asList(attributes.getStringArray("basePackages")));
        packages.addAll(classToPackage(attributes.getStringArray("basePackageClasses")));
        if (packages.isEmpty()) {
            packages.add(ClassUtils.getPackageName(metadata.getClassName()));
        }
        return packages;
    }

    private Collection<String> classToPackage(String[] values) {
        if (values == null || values.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.stream(values).map(ClassUtils::getPackageName).collect(Collectors.toList());
    }
}
