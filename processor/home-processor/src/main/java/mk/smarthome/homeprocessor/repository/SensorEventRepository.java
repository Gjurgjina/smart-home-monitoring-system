package mk.smarthome.homeprocessor.repository;

import mk.smarthome.homeprocessor.model.SensorEventDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SensorEventRepository extends MongoRepository<SensorEventDocument, String> {

    Page<SensorEventDocument> findAllByApartmentId(String apartmentId, Pageable pageable);

}