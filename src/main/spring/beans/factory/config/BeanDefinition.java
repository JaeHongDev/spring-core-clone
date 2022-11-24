package main.spring.beans.factory.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;
import main.spring.beans.factory.support.InjectType;

public interface BeanDefinition {

    Constructor<?> getInjectConstructor();

    Set<Field> getInjectFields();

    Class<?> getBeanClass();

    InjectType getResolvedInjectMode();

}
