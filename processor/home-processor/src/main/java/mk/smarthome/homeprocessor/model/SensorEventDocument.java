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
@Document(collection = "sensor_events")
public class SensorEventDocument {

    @Id
    private String id;

    private String eventId;
    private String deviceId;
    private SensorType deviceType;
    private String apartmentId;
    private String roomId;
    private Boolean value;
    private Double numericValue;
    private Instant timestamp;

}