/*
	---------------------------------------------------------------------------
	Copyright © 2015-2016 ADP, LLC.   
	
	Licensed under the Apache License, Version 2.0 (the “License”); 
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software 
	distributed under the License is distributed on an “AS IS” BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
	implied.  See the License for the specific language governing 
	permissions and limitations under the License.
	---------------------------------------------------------------------------
*/
package com.bap.ADBridge.restService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.adp.marketplace.connection.configuration.ClientCredentialsConfiguration;


/**
 * <p>
 * ClientUtils is a singleton utility class that provides convenience methods 
 * to obtain read properties file and map them to the ADPConnection configuration 
 * 
 * @author tallaprs
 *
 */
public class ClientUtils {
	
	private static ClientUtils INSTANCE = null;
	
	/**
	 * constructor
	 */
	public ClientUtils() {}
	
	/**
	 * returns a singleton instance of this class
	 * 
	 * @return ClientUtils instance of this class
	 */
	public static ClientUtils getInstance() {
		
		if ( INSTANCE == null ) {		
			synchronized (ClientUtils.class) {
	            if ( INSTANCE == null ) {
	                INSTANCE = new ClientUtils();
	            }
	        }
	    }
		
	    return INSTANCE;
	}

	/**
	 * 
	 * Returns the properties associated with this application by reading 
	 * from the specified location
	 * 
	 * @return Properties   Properties file for acquiring client credentials connection
	 * @throws IOException  Exception in reading the properties file
	 */
	public Properties getConfigProperties() throws IOException {
	
		final String CONFIG_PROPERTIES_FILE_PATH = "/src/main/resources/clientConfig.properties";
		
		String FILE_PATH = null;
		Properties properties = null;
		InputStream inputStream = null;
		
		try {
			
			properties = new Properties();
			FILE_PATH = (new java.io.File( "." ).getCanonicalPath()).concat(CONFIG_PROPERTIES_FILE_PATH);
			
			inputStream = new FileInputStream(FILE_PATH);
			
			if ( inputStream != null ) {
				properties.load(inputStream);
				printProperties(properties);
			}
			
		} catch (IOException e) {
			System.out.println("Exception loading properties file...");
		} finally {
			if ( inputStream != null ) {
				inputStream.close();
			}
		}
		
		return properties;
	}
	
	
	/**
	 * Populates AuthorizationCodeConfiguration by reading the properties files
	 * 
	 * @param properties 					  file with key value pairs for 
	 *                                        client credentials connection details
	 * @param clientCredentialsConfiguration  mapped with configuration properties
	 */
	public void mapPropertiesToClientCredentialsConfiguration(Properties properties, 
			ClientCredentialsConfiguration clientCredentialsConfiguration) throws Exception {
		
				
		String clientID = (String) properties.get("clientID");
		String clientSecret = (String) properties.get("clientSecret");
		String grantType = (String) properties.get("grantType");
		
		String sslCertPath = (String) properties.get("sslCertPath");
		String concatSSLCertPath = getFormattedSSLCertPath(sslCertPath);
		
		String keyPassword = (String) properties.get("keyPassword");
		String storePassword = (String) properties.get("storePassword");
		
		String apiRequestURL = (String) properties.get("apiRequestURL");
		String tokenServerURL = (String) properties.get("tokenServerURL");
		
			
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
	}
	
	
	/**
	 *  Identifies current directory and appends Java keystore file path and returns the 
	 *  absolute path 
	 * 
	 * @param sslCertPath
	 * @return
	 * @throws Exception 
	 */
	private String getFormattedSSLCertPath(String sslCertPath) throws Exception {
		
		String currentDirectory = null;
		String concatSSLCertPath = null;
		
		try {
			
			currentDirectory = (new java.io.File( "." ).getCanonicalPath());
			concatSSLCertPath = currentDirectory.concat(sslCertPath);
			
			System.out.println("\nFormatted SSL Cert Path: "+ concatSSLCertPath );
			
		} catch (IOException e) {
			throw new Exception ("Exception while processing SSLCertPath..." + e);
		}
		
		return concatSSLCertPath;
	}

	/**
	 * Prints current properties to console
	 *  
	 * @param properties properties to be printed
	 */
	private void printProperties(Properties properties) {
		
		Object entry = null;
		
		if ( properties != null) {
			
			System.out.println("----------------Client Credentials Connection - Config Properties------------------"  );
			
			Iterator<String> iterator = (Iterator) properties.entrySet().iterator();				
			while ( iterator.hasNext() ) {
				entry = iterator.next();
				System.out.println( "" + entry.toString() );
			}
			
			System.out.println("----------------------------------------------------------------------------------"  );
		}
	}
}