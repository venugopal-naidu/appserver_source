package com.vellkare

import groovy.util.logging.Log4j
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.hibernate.HibernateException
import org.hibernate.Session
import org.hibernate.SessionFactory

//http://forum.spring.io/forum/other-spring-related/grails/89137-controllers-and-transactions
//https://developer.jboss.org/wiki/SessionHandlingWithAOP
@Log4j
class TransactionLogger implements MethodInterceptor {

    SessionFactory sessionFactory
    def flushOnMethods

    @Override
    Object invoke(MethodInvocation invocation) throws Throwable {
        String className = invocation.getThis().getClass().getSimpleName()
        String methodName = invocation.getMethod().getName()
        Session session = null
        if( methodName != "getMetaClass" ) {
            session = logSession(className,methodName)
        }

        Object o = invocation.proceed()

        if( "${className}.${methodName}".toString() in flushOnMethods ) {
            session.flush()
            log.debug("${methodName}.${className}: flushing changes -------> ")
        }
        return o
    }

    protected Session logSession(String className, String methodName) {
        Session session = null;
        String invocationDescription = getInvocationDescription(className,methodName);
        try {
            session = sessionFactory.getCurrentSession()
            boolean isActive = session.getTransaction().isActive();
            log.info("${invocationDescription}: Session: ${System.identityHashCode(session)}, txnActive:${isActive}")
        } catch(HibernateException x) {
            log.warn("${invocationDescription}: ***** ${x.getMessage()} ******")
        }
        return session
    }

    protected wrapTxn(MethodInvocation invocation) {
        Session session = sessionFactory.getCurrentSession()

        boolean isActive = session.getTransaction().isActive();
        if ( !isActive) {
            log.debug("Starting a database transaction");
            session.beginTransaction();
        }

        log.debug("Invoking the aspectized service method");
        Object result = invocation.proceed()

        // Commit and cleanup
        if (!isActive) {
            log.debug("Committing the database transaction");
            session.getTransaction().commit();
        }

        return result;
    }

    protected String getInvocationDescription(String className, String methodName) {
        return "${methodName}.${className}: ";
    }
}
