package mk.smarthome.homeprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mk.smarthome.homeprocessor.model.Apartment;
import mk.smarthome.homeprocessor.model.Role;
import mk.smarthome.homeprocessor.model.User;
import mk.smarthome.homeprocessor.repository.ApartmentRepository;
import mk.smarthome.homeprocessor.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApartmentSeeder implements CommandLineRunner {

    private final ApartmentRepository apartmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (apartmentRepository.count() == 0) {
            for (int i = 1; i <= 8; i++) {
                Apartment apartment = Apartment.builder()
                        .apartmentId("apt-" + i)
                        .apartmentNumber(String.valueOf(i))
                        .floor((i - 1) / 2 + 1)
                        .ownerUsername("tenant" + i)
                        .build();
                apartmentRepository.save(apartment);
            }
            log.info("Seeded 8 apartments.");
        }

        if (userRepository.count() == 0) {
            for (int i = 1; i <= 8; i++) {
                User tenant = User.builder()
                        .username("tenant" + i)
                        .password(passwordEncoder.encode("password" + i))
                        .role(Role.TENANT)
                        .apartmentId("apt-" + i)
                        .build();
                userRepository.save(tenant);
            }

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .apartmentId(null)
                    .build();
            userRepository.save(admin);

            log.info("Seeded 8 tenant users + 1 admin user.");
        }
    }

}