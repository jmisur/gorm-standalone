package org.grails.gorm

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.orm.hibernate.HibernateDatastore
import org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor
import org.grails.datastore.mapping.model.MappingContext
import org.hibernate.SessionFactory
import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

public class HibernateDatastoreFactoryBean implements FactoryBean<HibernateDatastore>, ApplicationContextAware {

	SessionFactory sessionFactory

	MappingContext mappingContext

	GrailsApplication grailsApplication

	ClosureEventTriggeringInterceptor interceptor

	ApplicationContext applicationContext

	@Override
	public HibernateDatastore getObject() throws Exception {
		def hibernateDatastore = new HibernateDatastore(mappingContext, sessionFactory, grailsApplication.getConfig())
		hibernateDatastore.applicationContext = applicationContext
		interceptor.setDatastores([(sessionFactory): hibernateDatastore])
		hibernateDatastore
	}

	@Override
	public Class<?> getObjectType() {
		return HibernateDatastore.class
	}

	@Override
	public boolean isSingleton() {
		return true
	}
}
