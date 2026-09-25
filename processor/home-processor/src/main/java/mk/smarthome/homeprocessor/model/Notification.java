package mk.smarthome.homeprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String notificationId;
    private String recipientUsername;
    private String alertId;
    private SensorType deviceType;
    private String apartmentId;
    private String roomId;
    private Severity severity;
    private String message;
    private Instant timestamp;
    private boolean read;

}