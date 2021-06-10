package com.quickshare.framework.core.annotation;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * UniqueNameGenerator
 * @author liu_ke
 */
@Component
public class UniqueNameGenerator extends AnnotationBeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        if (definition instanceof AnnotatedBeanDefinition){
            String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
            if (StringUtils.hasText(beanName)){
                return beanName;
            }
        }

        //全限定类名
        String beanName = definition.getBeanClassName();
        return beanName;
    }
}