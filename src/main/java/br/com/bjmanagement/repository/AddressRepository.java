
package br.com.bjmanagement.repository;

import br.com.bjmanagement.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

//O JpaRepository já fornece os métodos básicos de CRUD
public interface AddressRepository extends JpaRepository<Address, Long> {
}
