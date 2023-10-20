package com.platzi.pizza.web.controller;

import com.platzi.pizza.persitence.entity.PizzaEntity;
import com.platzi.pizza.service.PizzaService;
import com.platzi.pizza.service.dto.UpdatePizzaPriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {

    private final PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public ResponseEntity<Page<PizzaEntity>> getAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "8") int elements){
        return ResponseEntity.ok(this.pizzaService.getAll(page, elements));
    }

    @GetMapping("/{idPizza}")
    public ResponseEntity<PizzaEntity> get(@PathVariable int idPizza){
        return ResponseEntity.ok(this.pizzaService.get(idPizza));
    }

    @GetMapping("/available")
    public ResponseEntity<Page<PizzaEntity>> getAvailable(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "8") int elements,
                                                          @RequestParam(defaultValue = "price") String sortBy,
                                                          @RequestParam(defaultValue = "ASC") String sortDirection){
        return ResponseEntity.ok(this.pizzaService.getAvailable(page,elements,sortBy,sortDirection));
    }
    @GetMapping("/vegan")
    public ResponseEntity<Integer> getVegan(){
        return ResponseEntity.ok(this.pizzaService.getVegan());
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<PizzaEntity> getByName(@PathVariable String name){
        return ResponseEntity.ok(this.pizzaService.getByName(name));
    }

    @GetMapping("/ingredient/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> getWhit(@PathVariable String ingredient){
        return ResponseEntity.ok(this.pizzaService.getWith(ingredient));
    }
    @GetMapping("/whitoutIngredient/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> getWhitout(@PathVariable String ingredient){
        return ResponseEntity.ok(this.pizzaService.getWithout(ingredient));
    }
    @GetMapping("/pricecheapest/{price}")
    public ResponseEntity<List<PizzaEntity>> getpricecheapest(@PathVariable double price){
        return ResponseEntity.ok(this.pizzaService.getCheaperst(price));
    }


    @PostMapping("/save")
    public ResponseEntity<PizzaEntity> add(@RequestBody PizzaEntity pizza){
        if (pizza.getIdPizza()==null || this.pizzaService.exist(pizza.getIdPizza())){

            return ResponseEntity.ok(this.pizzaService.save(pizza));
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<PizzaEntity> update(@RequestBody PizzaEntity pizza){
        if (pizza.getIdPizza()!=null && this.pizzaService.exist(pizza.getIdPizza())){

            return ResponseEntity.ok(this.pizzaService.save(pizza));
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/updatePrice")
    public ResponseEntity<Void> updatePrice(@RequestBody UpdatePizzaPriceDto dto){
        if ( this.pizzaService.exist(dto.getPizzaId())){
            this.pizzaService.updatePrice(dto);
        return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }




    @DeleteMapping("/delete/{idPizza}")
    public  ResponseEntity<Void> delete(@PathVariable int idPizza){
        if (pizzaService.exist(idPizza)){
            this.pizzaService.delete(idPizza);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }






}
