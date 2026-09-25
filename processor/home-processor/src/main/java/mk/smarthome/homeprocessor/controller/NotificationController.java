package mk.smarthome.homeprocessor.controller;

import lombok.RequiredArgsConstructor;
import mk.smarthome.homeprocessor.dto.NotificationResponse;
import mk.smarthome.homeprocessor.model.Notification;
import mk.smarthome.homeprocessor.repository.NotificationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping
    public List<NotificationResponse> getAll(Authentication authentication) {
        String username = authentication.getName();
        return notificationRepository.findAllByRecipientUsernameOrderByTimestampDesc(username)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/unread")
    public List<NotificationResponse> getUnread(Authentication authentication) {
        String username = authentication.getName();
        return notificationRepository.findAllByRecipientUsernameAndReadFalseOrderByTimestampDesc(username)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCount(Authentication authentication) {
        String username = authentication.getName();
        long count = notificationRepository.countByRecipientUsernameAndReadFalse(username);
        return Map.of("count", count);
    }

    @PatchMapping("/{notificationId}/read")
    public void markAsRead(@PathVariable String notificationId) {
        notificationRepository.findAll().stream()
                .filter(n -> n.getNotificationId().equals(notificationId))
                .findFirst()
                .ifPresent(n -> {
                    n.setRead(true);
                    notificationRepository.save(n);
                });
    }

    private NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .notificationId(n.getNotificationId())
                .deviceType(n.getDeviceType())
                .apartmentId(n.getApartmentId())
                .roomId(n.getRoomId())
                .severity(n.getSeverity())
                .message(n.getMessage())
                .timestamp(n.getTimestamp())
                .read(n.isRead())
                .build();
    }

}