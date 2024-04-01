package com.test.backendapirest.controller;

import com.test.backendapirest.model.entity.Cliente;
import com.test.backendapirest.model.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {
@Autowired
private IClienteService clienteService;
@GetMapping("/clientes")
    public List<Cliente> index(){
        return clienteService.findAll();
    }

    @GetMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> show(@PathVariable Long id){

    Cliente cliente = null;
        Map<String, Object> response = new HashMap<>();
    try{
        cliente = clienteService.findById(id);
    } catch(DataAccessException e){
        response.put("mensaje", "Error al realizar la consulta en la base de datos");
        response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    if(cliente == null){
        response.put("mensaje", "El cliente ID: ".concat(id.toString()).concat(" No existe en la base de datos!"));
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }



    @PostMapping("/clientes")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente create(@RequestBody Cliente cliente){
    return clienteService.save(cliente);
    }

    @PutMapping("/clientes/{id}")
public Cliente update(@RequestBody Cliente cliente, @PathVariable long id){
    Cliente clienteActual = clienteService.findById(id);

    clienteActual.setApellido(cliente.getApellido());
    clienteActual.setNombre(cliente.getNombre());
    clienteActual.setEmail(cliente.getEmail());

    return clienteService.save(clienteActual);
    }

    @DeleteMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
    clienteService.delete(id);
    }

}
