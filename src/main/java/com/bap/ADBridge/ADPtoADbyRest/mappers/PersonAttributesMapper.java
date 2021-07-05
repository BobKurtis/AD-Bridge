package com.bap.ADBridge.ADPtoADbyRest.mappers;

import com.bap.ADBridge.ADPtoADbyRest.models.Person;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;


public class PersonAttributesMapper implements AttributesMapper {

    @Override
    public Object mapFromAttributes(Attributes attributes) throws NamingException {
        Person person = new Person();

        Attribute name = attributes.get("name");
        if (name != null) {
            person.setName((String) name.get());
        }

        Attribute displayname = attributes.get("displayname");
        if (displayname != null) {
            person.setDisplayName((String) displayname.get());
        }

        Attribute lastname = attributes.get("sn");
        if (lastname != null) {
            person.setLastName((String) lastname.get());
        }

        Attribute firstname = attributes.get("givenname");
        if (firstname != null) {
            person.setFirstName((String) firstname.get());
        }

        Attribute mail = attributes.get("mail");
        if (mail != null) {
            person.setMail((String) mail.get());
        }

        Attribute userid = attributes.get("uid");
        if (userid != null) {
            person.setUserID((String) userid.get());
        }

        System.out.println(person.toString());

        return person;
    }
}