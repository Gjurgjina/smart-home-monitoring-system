package mk.smarthome.homeprocessor.controller;

import lombok.RequiredArgsConstructor;
import mk.smarthome.homeprocessor.model.SensorEventDocument;
import mk.smarthome.homeprocessor.repository.SensorEventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sensor-events")
@RequiredArgsConstructor
public class SensorEventController {

    private final SensorEventRepository sensorEventRepository;

    @GetMapping("/recent")
    public List<SensorEventDocument> getRecentEvents(Authentication authentication) {
        PageRequest pageRequest = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "timestamp"));

        if (isAdmin(authentication)) {
            return sensorEventRepository.findAll(pageRequest).getContent();
        }

        String apartmentId = getApartmentId(authentication);
        return sensorEventRepository.findAllByApartmentId(apartmentId, pageRequest).getContent();
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private String getApartmentId(Authentication authentication) {
        return (String) ((UsernamePasswordAuthenticationToken) authentication).getDetails();
    }

}