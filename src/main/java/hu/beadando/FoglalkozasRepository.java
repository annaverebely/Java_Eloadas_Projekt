package hu.beadando;

import org.springframework.data.repository.CrudRepository;

// A metódusokat (findAll, findById, save, deleteById, count...) a Spring készíti el
interface FoglalkozasRepository extends CrudRepository<Foglalkozas, Integer> {
}
