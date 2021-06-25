package com.bap.ADBridge.restService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Component;

import javax.naming.Name;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.jar.Attributes;

@Component
public class ADService {

    @Autowired
    private LdapTemplate ldapTemplate;



    public List<String> search(String username) {
        System.out.println("we are in the search function searching for: " + username);

        return ldapTemplate
                .search(
                        "",
                        "cn=" + username,
                        (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
    }

    /*    public List<String> getAllPersonNames() {
            return ldapTemplate.search(
                    query().where("objectclass").is("person"),
                    new AttributesMapper<String>() {
                        public String mapFromAttributes(Attributes attrs)
                                throws NamingException {
                            return attrs.get("cn").get().toString();
                        }
                    });
        }*/
    //@Override
    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<Person>();
        try {
            List search = ldapTemplate.search("", "(objectClass=person)", new PersonAttributesMapper());
            persons.addAll(search);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return persons;
    }

    public List findUserByCommonName(String commonName) {
        ldapTemplate.setIgnorePartialResultException(true);
        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass","person"));
        andFilter.and(new EqualsFilter("cn", commonName));
        return ldapTemplate.search("", andFilter.encode(), new PersonAttributesMapper());
    }

    public void create(String username, String password) {
        Name dn = LdapNameBuilder
                .newInstance()
                .add("ou", "users")
                .add("cn", username)
                .build();
        DirContextAdapter context = new DirContextAdapter(dn);

        context.setAttributeValues(
                "objectclass",
                new String[]
                        {"top",
                                "person",
                                "organizationalPerson",
                                "inetOrgPerson"});
        context.setAttributeValue("cn", username);
        context.setAttributeValue("sn", username);
        context.setAttributeValue
                ("userPassword", digestSHA(password));

        ldapTemplate.bind(context);
    }

    private String digestSHA(final String password) {
        String base64;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(password.getBytes());
            base64 = Base64
                    .getEncoder()
                    .encodeToString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return "{SHA}" + base64;
    }
}
