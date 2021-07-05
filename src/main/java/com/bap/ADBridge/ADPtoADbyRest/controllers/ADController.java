package com.bap.ADBridge.ADPtoADbyRest.controllers;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.bap.ADBridge.ADPtoADbyRest.services.ADService;
import com.bap.ADBridge.ADPtoADbyRest.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ADController {

    @Autowired
    private ADService adService;

    @GetMapping("/lookfor")
    public List<String> search(@RequestParam(value = "name", defaultValue = "") String name) {
        return adService.search(name);
    }

    @GetMapping("/searchAll")
    public List<Person> searchAll() {
        //http://localhost:8080/searchAll
        return adService.getAllPersons();
    }

    @GetMapping("/findUserByCommonName")
    public List findUserByCommonName(@RequestParam(value = "name", defaultValue = "Administrator") String name) {
        //https://localhost:8443/findUserByCommonName?name=Bob%20Admire
        return adService.findUserByCommonName(name);
    }

    @GetMapping("/createUser")
    public void createUser(@RequestParam(value = "name", defaultValue = "Smitty") String name, @RequestParam(value = "password", defaultValue = "password") String password) {
        //https://localhost:8443/createUser?name=Test&password=1abcdefg!
        adService.create(name, password);
    }

    @GetMapping("/")
    public String index() {
        return "Home page";
    }

}