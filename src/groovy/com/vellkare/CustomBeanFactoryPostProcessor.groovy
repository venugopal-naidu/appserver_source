package com.vellkare

import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.DefaultListableBeanFactory

//https://github.com/bluesliverx/grails-spring-security-oauth2-provider/issues/86
class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor{

    @Override
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory readWriteBeanFactory = beanFactory as DefaultListableBeanFactory

//        readWriteBeanFactory.removeAlias('tokenStore')
//        readWriteBeanFactory.registerAlias('appTokenStore', 'tokenStore')

        def ep = readWriteBeanFactory.getBean("authenticationEntryPoint")
        def apiRequestMatcher = readWriteBeanFactory.getBean("apiRequestMatcher")
        def apiEntryPoint = readWriteBeanFactory.getBean("apiEntryPoint")
        ep.entryPoints.put(apiRequestMatcher,apiEntryPoint)

    }
}
