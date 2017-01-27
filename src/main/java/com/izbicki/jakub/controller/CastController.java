package com.izbicki.jakub.controller;

import com.izbicki.jakub.service.CastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CastController {

    @Autowired @Qualifier("CastService")

    private CastService cs;

    @RequestMapping(value = "/admin/casts", method = GET)
    private ResponseEntity selectAllCast(){

        return cs.selectAll();
    }
}
