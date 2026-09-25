package mk.smarthome.homeprocessor.repository;

import mk.smarthome.homeprocessor.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findAllByRecipientUsernameOrderByTimestampDesc(String recipientUsername);

    List<Notification> findAllByRecipientUsernameAndReadFalseOrderByTimestampDesc(String recipientUsername);

    long countByRecipientUsernameAndReadFalse(String recipientUsername);

}