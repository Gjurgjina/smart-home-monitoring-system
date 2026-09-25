package mk.smarthome.homeprocessor.dto;

import mk.smarthome.homeprocessor.model.Alert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertMapper {

    public AlertResponse toResponse(Alert alert) {
        return AlertResponse.builder()
                .alertId(alert.getAlertId())
                .deviceType(alert.getDeviceType())
                .apartmentId(alert.getApartmentId())
                .roomId(alert.getRoomId())
                .severity(alert.getSeverity())
                .message(alert.getMessage())
                .timestamp(alert.getTimestamp())
                .build();
    }

    public List<AlertResponse> toResponseList(List<Alert> alerts) {
        return alerts.stream()
                .map(this::toResponse)
                .toList();
    }

}