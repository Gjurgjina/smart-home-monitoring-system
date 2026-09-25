package mk.smarthome.sensorproducer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorEvent {

    private String eventId;
    private String deviceId;
    private SensorType deviceType;
    private String apartmentId;
    private String roomId;
    private Boolean value;
    private Double numericValue;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;

}