package org.grails.gorm.domain

import grails.persistence.Entity

@Entity
class User {

	String username
	String email
	String password
	String passwordHash = "___dummy___" // prevent validation error

	Date dateCreated
	Date lastUpdated

	Set<Group> groups

	static hasMany = [groups: Group]

	static transients = ['password']

	static belongsTo = [Group]

	static mapping = {
		table 'test_user'
		sort username: 'desc'
		groups cascade: 'save-update'
	}

	static constraints = {
		username(blank: false, minSize: 4, maxSize: 100, unique: true)
		email(nullable: false, minSize: 5, maxSize: 100, email: true, unique: true)
		passwordHash(nullable: false, minSize: 3, maxSize: 100)
		dateCreated(nullable: true) // must be true to enable grails
		lastUpdated(nullable: true) // auto-inject to be useful which occurs post validation
	}

	static beforeInsert = {
		if (password != null) {
			passwordHash = password.reverse() // hashing like a pro
		}
	}

	static beforeUpdate = {
		if (password != null) {
			passwordHash = password.reverse() // hashing like a pro
		}
	}
}
