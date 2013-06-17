package org.grails.gorm

import org.grails.gorm.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional

import spock.lang.Specification

@ContextConfiguration("classpath:/org/grails/gorm/test-gorm-context.xml")
@TransactionConfiguration(defaultRollback = false)
@Transactional
class GormSpec extends Specification {

	@Autowired
	JdbcTemplate jdbcTemplate

	User user

	def setup() {
		user = new User(username: 'John', email: 'test@mycompany.com', password: 'secret')
	}

	def "save user"() {
		when:
		user.save(flush: true)

		then:
		noExceptionThrown()
		jdbcTemplate.queryForInt("select count(*) from test_user") == 1
		def userMap = jdbcTemplate.queryForMap("select * from test_user where username = ?", user.username)
		userMap['username'] == user.username
		userMap['email'] == user.email
		!userMap['password']
		userMap['password_hash'] == user.password.reverse()
		userMap['date_created'] == user.dateCreated
		userMap['last_updated'] == user.lastUpdated
	}

	def "find user by username"() {
		when:
		User found = User.findByUsername(user.username)

		then:
		noExceptionThrown()
		found
		found.username == user.username
		found.email == user.email
		found.passwordHash == user.password.reverse()
		found.dateCreated
		found.lastUpdated
	}
}

