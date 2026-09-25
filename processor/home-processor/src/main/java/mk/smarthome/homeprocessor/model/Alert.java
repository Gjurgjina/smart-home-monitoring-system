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
@Document(collection = "alerts")
public class Alert {

    @Id
    private String id;

    private String alertId;
    private String sourceEventId;
    private SensorType deviceType;
    private String apartmentId;
    private String roomId;
    private Severity severity;
    private String message;
    private Instant timestamp;

}