package mk.smarthome.homeprocessor.controller;

import lombok.RequiredArgsConstructor;
import mk.smarthome.homeprocessor.dto.AlertMapper;
import mk.smarthome.homeprocessor.dto.AlertResponse;
import mk.smarthome.homeprocessor.model.Alert;
import mk.smarthome.homeprocessor.model.Severity;
import mk.smarthome.homeprocessor.repository.AlertRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    @GetMapping("/recent")
    public List<AlertResponse> getRecentAlerts(Authentication authentication) {
        List<Alert> alerts = isAdmin(authentication)
                ? alertRepository.findAllByOrderByTimestampDesc()
                : alertRepository.findAllByApartmentIdOrderByTimestampDesc(getApartmentId(authentication));
        return alertMapper.toResponseList(alerts);
    }

    @GetMapping
    public List<AlertResponse> getAlertsFiltered(
            @RequestParam(required = false) Severity severity,
            Authentication authentication
    ) {
        List<Alert> alerts = isAdmin(authentication)
                ? alertRepository.findAllByOrderByTimestampDesc()
                : alertRepository.findAllByApartmentIdOrderByTimestampDesc(getApartmentId(authentication));

        if (severity != null) {
            alerts = alerts.stream().filter(a -> a.getSeverity() == severity).toList();
        }
        return alertMapper.toResponseList(alerts);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private String getApartmentId(Authentication authentication) {
        return (String) ((UsernamePasswordAuthenticationToken) authentication).getDetails();
    }

}