package com.cognizant.labs.controller;

import com.cognizant.labs.models.MobOrder;
import com.cognizant.labs.service.MobOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/moborder")
public class MobOrderController {

    @Autowired
    MobOrderService mobOrderService;

    @GetMapping("/{moborderId")
    public ResponseEntity<MobOrder> getById(@PathVariable Long id) {
        MobOrder result = mobOrderService.findById(id);
        if(result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MobOrder>> getAll(){
        List<MobOrder> result = mobOrderService.findAll();
        if(result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<MobOrder> saveNewMobOrder(@RequestBody String difficulty) {
        return new ResponseEntity<>(mobOrderService.createMobOrder(difficulty), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<MobOrder> updateMobOrder(@RequestBody MobOrder mobOrder){
        MobOrder mobOrder1 = mobOrderService.updateMobOrder(mobOrder);
        if(mobOrder1 == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mobOrder1, HttpStatus.OK);
    }
}
