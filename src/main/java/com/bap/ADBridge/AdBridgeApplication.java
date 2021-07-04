package com.bap.ADBridge;

//import com.mashape.unirest.http.Unirest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
//import sun.net.ftp.FtpClient;


@SpringBootApplication
public class AdBridgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdBridgeApplication.class, args);
    }


    @Bean
    public LdapContextSource contextSourceReadOnly() {
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

        return contextSource;
    }

    @Bean
    public LdapContextSource contextSourceWriter() {
        LdapContextSource contextSource = new LdapContextSource();
        //has write access to: OU=ADPSyncTest,OU=Accounts,DC=clc,DC=loc
        contextSource.setUrl("LDAP://clc.loc:389");
        contextSource.setBase("dc=clc,dc=loc");
        contextSource.setUserDn("SA_ADPSync");
        contextSource.setPassword("PADL_Boat!");

        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplateReadOnly() {
        return new LdapTemplate(contextSourceReadOnly());
    }

    @Bean
    public LdapTemplate ldapTemplateWriter() {
        return new LdapTemplate(contextSourceWriter());
    }

    @Bean
    public void clientCredentials() throws Exception {


        Unirest.config().clientCertificateStore("src/main/resources/test.jks", "test");

        try {
            HttpResponse<String> response = Unirest.post("https://uat-accounts.adp.com/auth/oauth/v2/token")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("grant_type", "client_credentials")
                    .field("client_id", "405a7b68-b9ee-4e89-af8b-f106b0fdb425")
                    .field("client_secret", "933d7cc1-9672-4542-aac9-4adde94b86a9")
                    .asString();

            JSONObject myObject = new JSONObject(response.getBody());

            System.out.println("Auth code is: " + myObject.get("access_token"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            HttpResponse<String> response2 = Unirest.get("https://uat-accounts.adp.com/hr/v2/workers?$top=5&preventCache=TIMESTAMP")
                    .header("Authorization", "Bearer " + myObject.get("access_token"))
                    .header("Accept", "application/json;masked=false")
                    .asString();
            JsonElement je = JsonParser.parseString(response2.getBody().toString());
            System.out.println(gson.toJson(je));
        } catch (Exception e) {
            System.out.println("==================REST CLIENT ERROR: " + e.getMessage());
        }


    }

}
