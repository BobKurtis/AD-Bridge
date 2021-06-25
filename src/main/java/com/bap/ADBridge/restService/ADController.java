package com.bap.ADBridge.restService;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ADController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

/*    @Autowired
    private UserService userService;*/

    @Autowired
    private ADService adService;

   /* @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }*/

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
        //http://localhost:8080/findUserByCommonName?name=Bob%20Admire
        return adService.findUserByCommonName(name);
    }

    @GetMapping("/createUser")
    public void createUser(@RequestParam(value = "name", defaultValue = "Smitty") String name, @RequestParam(value="password", defaultValue="password") String password) {
       adService.create(name, password);
    }

    @GetMapping("/")
    public String index(){
        return "Home page";
    }

}