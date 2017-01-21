package com.izbicki.jakub.Controller;

import com.izbicki.jakub.Entity.Cast;
import com.izbicki.jakub.Service.CastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class CastController {

    @Autowired @Qualifier("CastService")
    private CastService cs;

    @RequestMapping(value = "/admin/casts", method = GET)
    private List<Cast> selectAllCast(){

        return cs.selectAll();
    }
}
