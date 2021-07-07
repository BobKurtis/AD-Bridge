package com.bap.ADBridge.ADPtoADbyRest.services;

import com.bap.ADBridge.ADPtoADbyRest.models.Person;
import com.bap.ADBridge.ADPtoADbyRest.mappers.PersonAttributesMapper;
import com.bap.ADBridge.ADPtoADbyRest.utils.DigestSHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Component;

import javax.naming.Name;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class ADService {

    @Autowired
    @Qualifier("readOnly")
    private LdapTemplate ldapTemplateReader;

    @Autowired
    @Qualifier("writing")
    private LdapTemplate ldapTemplateWriter;

    private DigestSHA digestSHA = new DigestSHA();


    public List<String> search(String username) {
        System.out.println("we are in the search function searching for: " + username);

        return ldapTemplateReader
                .search(
                        "",
                        "cn=" + username,
                        (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
    }

    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<Person>();
        try {
            List search = ldapTemplateReader.search("", "(objectClass=person)", new PersonAttributesMapper());
            persons.addAll(search);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return persons;
    }

    public List findUserByCommonName(String commonName) {
        ldapTemplateReader.setIgnorePartialResultException(true);
        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        andFilter.and(new EqualsFilter("cn", commonName));
        return ldapTemplateReader.search("", andFilter.encode(), new PersonAttributesMapper());
    }

    public void create(String username, String password) {
        Name dn = LdapNameBuilder
                .newInstance()
                //.add("ou", "ADPSyncTest")
                //.add("ou", "Accounts")
                .add("cn", username)
                .build();
        DirContextAdapter context = new DirContextAdapter(dn);

        context.setAttributeValues(
                "objectclass",
                new String[]
                        {"top",
                                "person",
                                "organizationalPerson",
                                "user"});
        context.setAttributeValue("cn", username);
        context.setAttributeValue("sn", username);
        context.setAttributeValue("uid", username);
        context.setAttributeValue
                ("userPassword", digestSHA.digestSHA(password));

        ldapTemplateWriter.bind(context);
    }
    public void modify(String username, String password) {
        Name dn = LdapNameBuilder.newInstance()
                //.add("ou", "ADPSyncTest")
                //.add("ou", "Accounts")
                .add("cn", username)
                .build();
        DirContextOperations context
                = ldapTemplateWriter.lookupContext(dn);

        context.setAttributeValues
                ("objectclass",
                        new String[]
                                { "top",
                                        "person",
                                        "organizationalPerson",
                                        "user" });
        context.setAttributeValue("cn", username);
        context.setAttributeValue("sn", "This Was Modified");
        context.setAttributeValue("userPassword",
                digestSHA.digestSHA(password));

        ldapTemplateWriter.modifyAttributes(context);
    }

}
