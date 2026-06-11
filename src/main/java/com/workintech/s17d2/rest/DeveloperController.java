package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class DeveloperController {

    public Map<Integer, Developer> developers;

    private final Taxable taxable;

    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
    }

    @GetMapping("/developers")
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/developers/{id}")
    public Developer getDeveloperById(@PathVariable int id) {
        return developers.get(id);
    }

    @PostMapping("/developers")
    @ResponseStatus(HttpStatus.CREATED)
    public Developer addDeveloper(@RequestBody Developer developer) {

        Developer newDeveloper;

        if (developer.getExperience() == Experience.JUNIOR) {

            double salary =
                    developer.getSalary()
                            - (developer.getSalary() * taxable.getSimpleTaxRate() / 100);

            newDeveloper = new JuniorDeveloper(
                    developer.getId(),
                    developer.getName(),
                    salary
            );

        } else if (developer.getExperience() == Experience.MID) {

            double salary =
                    developer.getSalary()
                            - (developer.getSalary() * taxable.getMiddleTaxRate() / 100);

            newDeveloper = new MidDeveloper(
                    developer.getId(),
                    developer.getName(),
                    salary
            );

        } else {

            double salary =
                    developer.getSalary()
                            - (developer.getSalary() * taxable.getUpperTaxRate() / 100);

            newDeveloper = new SeniorDeveloper(
                    developer.getId(),
                    developer.getName(),
                    salary
            );
        }

        developers.put(newDeveloper.getId(), newDeveloper);

        return newDeveloper;
    }

    @PutMapping("/developers/{id}")
    public Developer updateDeveloper(@PathVariable int id,
                                     @RequestBody Developer developer) {

        developers.put(id, developer);

        return developer;
    }

    @DeleteMapping("/developers/{id}")
    public Developer deleteDeveloper(@PathVariable int id) {

        return developers.remove(id);
    }
}