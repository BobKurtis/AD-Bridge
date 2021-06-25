package com.bap.ADBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertyResolver;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class AdBridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdBridgeApplication.class, args);
	}


	@Bean
	public LdapContextSource contextSource() {
		LdapContextSource contextSource = new LdapContextSource();

		//PropertyResolver env = null;
		/*contextSource.setUrl(env.getRequiredProperty("ldap.url"));
		contextSource.setBase(env.getRequiredProperty("ldap.partitionSuffix"));
		contextSource.setUserDn(env.getRequiredProperty("ldap.principal"));
		contextSource.setPassword(env.getRequiredProperty("ldap.password"));*/
		/*contextSource.setUrl("ldap://ldap.forumsys.com:389");
		contextSource.setBase("dc=example,dc=com");
		contextSource.setUserDn("cn=read-only-admin,dc=example,dc=com");
		contextSource.setPassword("password");*/

		/*contextSource.setUrl("ldap://db.debian.org:389");
		contextSource.setBase("dc=debian,dc=org");
		contextSource.setUserDn("");
		contextSource.setPassword("");*/

		contextSource.setUrl("LDAP://clc.loc:389");
		contextSource.setBase("dc=clc,dc=loc");
		contextSource.setUserDn("SA_ADPSyncRO");
		contextSource.setPassword("pok-N-round");

		//contextSource.set

		return contextSource;
	}

	@Bean
	public LdapTemplate ldapTemplate() {
		return new LdapTemplate(contextSource());
	}


	@Configuration
	@EnableWebSecurity
	public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception{
			http.authorizeRequests().antMatchers("/").permitAll();
		}
	}
}
