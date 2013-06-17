package org.grails.gorm.domain

import grails.persistence.Entity

@Entity
class Group  {

	String name

	Date dateCreated
	Date lastUpdated

	Set<User> users

	static hasMany = [users: User]

	static mapping = {
		table 'test_group'
		users cascade: 'all-delete-orphan'
	}

	static constraints = {
		name(blank: false, unique: true, minSize:4, maxSize: 100)

		dateCreated(nullable: true) // must be true to enable grails
		lastUpdated(nullable: true) // auto-inject to be useful which occurs post validation
	}
}
