package main.spring.beans.factory.config;

import com.google.common.collect.Sets;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import main.spring.beans.factory.support.InjectType;

public class DefaultBeanDefinition implements BeanDefinition {
    private final Class<?> beanClazz;
    private final Constructor<?> injectConstructor;
    private final Set<Field> injectFields;

    public DefaultBeanDefinition(Class<?> clazz) {
        this.beanClazz = clazz;
        this.injectConstructor = getInjectConstructor(clazz);
        this.injectFields = getInjectFields(clazz, injectConstructor);
    }

    private static Constructor<?> getInjectConstructor(Class<?> clazz) {
        return BeanFactoryUtils.getInjectedConstructor(clazz);
    }

    private static Set<Class<?>> getInjectPropertiesType(Class<?> clazz) {
        Set<Class<?>> injectProperties = Sets.newHashSet();
        Set<Method> injectMethod = BeanFactoryUtils.getInjectedMethods(clazz);
        for (Method method : injectMethod) {
            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != 1) {
                throw new IllegalStateException("DI할 메소드 인자는 하나여야 합니다.");
            }
        }

        Set<Field> injectField = BeanFactoryUtils.getInjectFields(clazz);
        for (Field field : injectField) {
            injectProperties.add(field.getType());
        }
        return injectProperties;
    }

    private Set<Field> getInjectFields(Class<?> clazz, Constructor<?> constructor) {
        if (constructor != null) {
            return Sets.newHashSet();
        }

        Set<Field> injectFields = Sets.newHashSet();
        Set<Class<?>> injectProperties = getInjectPropertiesType(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (injectProperties.contains(field.getType())) {
                injectFields.add(field);
            }
        }
        return injectFields;
    }

    @Override
    public Constructor<?> getInjectConstructor() {
        return injectConstructor;
    }

    @Override
    public Set<Field> getInjectFields() {
        return this.injectFields;
    }

    @Override
    public Class<?> getBeanClass() {
        return this.beanClazz;
    }

    @Override
    public InjectType getResolvedInjectMode() {
        if (injectConstructor != null) {
            return InjectType.INJECT_CONSTRUCTOR;
        }

        if (!injectFields.isEmpty()) {
            return InjectType.INJECT_FIELD;
        }
        return InjectType.INJECT_NO;
    }
}
