package org.grails.gorm

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.orm.hibernate.ConfigurableLocalSessionFactoryBean
import org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin
import org.codehaus.groovy.grails.plugins.orm.hibernate.HibernatePluginSupport
import org.hibernate.EntityMode
import org.hibernate.SessionFactory
import org.hibernate.metadata.ClassMetadata
import org.springframework.beans.BeanInstantiationException
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

public class GormEnhancingBeanPostProcessor implements InitializingBean, ApplicationContextAware {

	SessionFactory sessionFactory

	GrailsApplication application

	ApplicationContext applicationContext

	@Override
	public void afterPropertiesSet() throws Exception {
		sessionFactory.allClassMetadata.each { className, ClassMetadata metadata ->
			Class mappedClass = metadata.getMappedClass(EntityMode.POJO)

			if (!application.getDomainClass(mappedClass.name)) {
				application.addDomainClass(mappedClass)
			}
		}

		try {
			DomainClassGrailsPlugin.enhanceDomainClasses(application, applicationContext)
			HibernatePluginSupport.enhanceSessionFactory(sessionFactory, application, applicationContext)
		} catch (Throwable e) {
			throw new BeanInstantiationException(ConfigurableLocalSessionFactoryBean, "Error configuring GORM dynamic behavior: $e.message", e)
		}
	}
}