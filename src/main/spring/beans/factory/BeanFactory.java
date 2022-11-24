package main.spring.beans.factory;

import java.util.Set;

public interface BeanFactory {
    Set<Class<?>> getBeanClasses();

    <T> T getBean(Class<T> clazz);

    void clear();

}
