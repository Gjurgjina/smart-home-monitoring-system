package mk.smarthome.homeprocessor.repository;

import mk.smarthome.homeprocessor.model.Apartment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ApartmentRepository extends MongoRepository<Apartment, String> {

    Optional<Apartment> findByApartmentId(String apartmentId);

}