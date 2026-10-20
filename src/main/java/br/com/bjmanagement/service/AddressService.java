
package br.com.bjmanagement.service;

import br.com.bjmanagement.model.Address;
import br.com.bjmanagement.model.Person;
import br.com.bjmanagement.repository.AddressRepository;
import br.com.bjmanagement.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final PersonRepository personRepository;

    //Injeção de dependências: o service precisa scessar endereços e person
    public AddressService(
            AddressRepository addressRepository,
            PersonRepository personRepository) {
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
    }

    //Cria um endereço atrelando a um Person existente
    public Address create(Address address) {
        address.setPerson(findPerson(address));
        return addressRepository.save(address);
    }

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Address findById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Not Found by ID: " + id));
    }

    public Address update(Long id, Address address) {
        Address exist = findById(id);

        //Altera os campos editaveis pelos valores recebidos.
        exist.setStreet(address.getStreet());
        exist.setCity(address.getCity());
        exist.setState(address.getState());
        exist.setZipCode(address.getZipCode());
        exist.setCountry(address.getCountry());
        exist.setPerson(findPerson(address));

        return addressRepository.save(exist);
    }

    public Address patch(Long id, Address address) {
        Address exist = findById(id);

        //altera somente os campos enviados no body request
        if (address.getStreet() != null) {
            exist.setStreet(address.getStreet());
        }
        if (address.getCity() != null) {
            exist.setCity(address.getCity());
        }
        if (address.getState() != null) {
            exist.setState(address.getState());
        }
        if (address.getZipCode() != null) {
            exist.setZipCode(address.getZipCode());
        }
        if (address.getCountry() != null) {
            exist.setCountry(address.getCountry());
        }
        if (address.getPerson() != null) {
            exist.setPerson(findPerson(address));
        }

        return addressRepository.save(exist);
    }

    public void delete(Long id) {
        //Só deleta se existir
        findById(id);
        addressRepository.deleteById(id);
    }

    private Person findPerson(Address address) {
        //O endereço precisa estar vinculado a uma person
        if (address.getPerson() == null
                || address.getPerson().getId() == null) {
            throw new IllegalArgumentException(
                    "É necessário informar o ID da pessoa.");
        }
        //Busca uma person real no banco antes de estabelecer o relacionamento
        return personRepository.findById(address.getPerson().getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Pessoa não encontrada com o ID: "
                                        + address.getPerson().getId()));
    }
}
