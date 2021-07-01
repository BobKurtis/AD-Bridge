package com.bap.ADBridge;

import com.adp.marketplace.connection.configuration.AuthorizationCodeConfiguration;
import com.adp.marketplace.connection.configuration.ClientCredentialsConfiguration;
import com.adp.marketplace.connection.configuration.ConnectionConfiguration;
import com.adp.marketplace.connection.core.ADPAPIConnection;
import com.adp.marketplace.connection.core.ADPAPIConnectionFactory;
import com.adp.marketplace.connection.core.AuthorizationCodeConnection;
import com.adp.marketplace.connection.core.ClientCredentialsConnection;
import com.adp.marketplace.connection.exception.ConnectionException;
import com.adp.marketplace.connection.vo.Token;
import org.apache.commons.lang3.StringUtils;
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
//import sun.net.ftp.FtpClient;

import java.util.Properties;

@SpringBootApplication
public class AdBridgeApplication {

	private static final Object CLIENT_UTILS_INSTANCE = ;
	private static final ADPAPIConnectionFactory CONNECTION_FACTORY_INSTANCE = ;

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

	public void clientCredentials() throws ConnectionException {

		ClientCredentialsConfiguration clientCredentialsConfiguration = new ClientCredentialsConfiguration();
		//CONFIGURATION FOR AUTHORIZATION CODE
			//clientConfig.properties
				String clientID = "5cab3a80-b3fd-415f-955f-4f868596ff43";
				String clientSecret = "4a26db08-2885-4766-b6bb-ad8d0eac7c22";
			//absolute path file path, file path has to follow patterns based of Windows or Unix
			String sslCertPath = "${CurrentDirectory}/src/main/resources/certs/keystor.jks";
			String storePassword = "adpadp10";
			String	keyPassword = "adpadp10";
			String	grantType = "authorization_code";
			String	responseType = "code";
			String	scope="openid";
			String	baseAuthorizationURL = "https://iat-accounts.adp.com/auth/oauth/v2/authorize";
			String	disconnectURL = "https://iat-accounts.adp.com/auth/oauth/v2/logout";
			String	redirectURL = "http://localhost:8889/marketplace/callback";
			String	tokenServerURL = "https://uat-accounts.adp.com/auth/oauth/v2/token";
			String	apiRequestURL = "https://iat-api.adp.com/core/v1/userinfo";


		if ( !StringUtils.isBlank( clientID) ) {
			clientCredentialsConfiguration.setClientID(clientID);
		}

		if ( !StringUtils.isBlank( clientSecret)  )  {
			clientCredentialsConfiguration.setClientSecret(clientSecret);
		}

		if ( !StringUtils.isBlank( concatSSLCertPath)) {
			clientCredentialsConfiguration.setSslCertPath(concatSSLCertPath);
		}

		if ( !StringUtils.isBlank( keyPassword)) {
			clientCredentialsConfiguration.setKeyPassword(keyPassword);
		}

		if ( !StringUtils.isBlank( storePassword)) {
			clientCredentialsConfiguration.setStorePassword(storePassword);
		}

		if ( !StringUtils.isBlank( tokenServerURL)) {
			clientCredentialsConfiguration.setTokenServerUrl(tokenServerURL);
		}

		if ( !StringUtils.isBlank( apiRequestURL)) {
			clientCredentialsConfiguration.setApiRequestUrl(apiRequestURL);
		}

		if ( !StringUtils.isBlank( grantType)) {
			// do nothing  - the configuration object is mapped with grant type = client_credentials
		}

		System.out.println("\nClient Credentials Connection Config: " + clientCredentialsConfiguration.toString());
		// login page credentials to use on login screen
				//UserId = MKPLDEMO
				//Password = marketplace1

		//ACQUIRING CONNECTION FOR AUTHORIZATION CODE
			//create authorization code configuration object
					AuthorizationCodeConfiguration authorizationCodeConfiguration = new AuthorizationCodeConfiguration();

			//get authorization code configuration properties
					Properties properties = CLIENT_UTILS_INSTANCE.getConfigProperties();

			//populate authorization code configuration
					CLIENT_UTILS_INSTANCE.mapPropertiesToAuthCodeConfiguration(properties, authorizationCodeConfiguration);

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
