package mk.smarthome.sensorproducer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApartmentInfo {
    private String apartmentId;
    private String apartmentNumber;
    private int floor;
}