package mk.smarthome.homeprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.smarthome.homeprocessor.model.Severity;
import mk.smarthome.homeprocessor.model.SensorType;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse {

    private String alertId;
    private SensorType deviceType;
    private String apartmentId;
    private String roomId;
    private Severity severity;
    private String message;
    private Instant timestamp;

}