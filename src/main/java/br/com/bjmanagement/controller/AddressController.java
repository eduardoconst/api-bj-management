
package br.com.bjmanagement.controller;

import br.com.bjmanagement.model.Address;
import br.com.bjmanagement.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Address create(@RequestBody Address address) {
        return addressService.create(address);
    }

    @GetMapping
    public List<Address> findAll() {
        return addressService.findAll();
    }

    @GetMapping("/{id}")
    public Address findById(@PathVariable Long id) {
        return addressService.findById(id);
    }

    @PutMapping("/{id}")
    public Address update(
            @PathVariable Long id,
            @RequestBody Address address) {
        return addressService.update(id, address);
    }

    @PatchMapping("/{id}")
    public Address patch(
            @PathVariable Long id,
            @RequestBody Address address) {
        return addressService.patch(id, address);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        addressService.delete(id);
    }
}
