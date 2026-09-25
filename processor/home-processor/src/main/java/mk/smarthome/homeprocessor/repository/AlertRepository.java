package mk.smarthome.homeprocessor.repository;

import mk.smarthome.homeprocessor.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AlertRepository extends MongoRepository<Alert, String> {

    List<Alert> findAllByOrderByTimestampDesc();

    List<Alert> findAllByApartmentIdOrderByTimestampDesc(String apartmentId);

}