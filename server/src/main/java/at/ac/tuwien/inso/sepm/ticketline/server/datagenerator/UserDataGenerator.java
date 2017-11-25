package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Profile("generateData")
@Component
public class UserDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsDataGenerator.class);
    private static final int NUMBER_OF_USER_TO_GENERATE = 3;

    private final UserRepository userRepository;
    private final Faker faker;

    public UserDataGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
        faker = new Faker();
    }

    @PostConstruct
    private void generateUser() {
        if (userRepository.count() > 0) {
            LOGGER.info("user already generated");
        } else {
            LOGGER.info("generating {} user entries", NUMBER_OF_USER_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_USER_TO_GENERATE; i++) {
                User user = User.builder()
                    .userName(faker.lorem().characters(25, 60))
                    .password(faker.lorem().characters(25, 60))
                    .role(4)
                    .blocked(false)
                    .build();
                LOGGER.debug("saving user {}", user);
                userRepository.save(user);
            }
        }
    }
}

