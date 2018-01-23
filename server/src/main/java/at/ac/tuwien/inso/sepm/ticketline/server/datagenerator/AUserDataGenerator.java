package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile("generateData")
@Component
public class AUserDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AUserDataGenerator.class);
    private static final int NUMBER_OF_USER_TO_GENERATE = 3;

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final Faker faker;

    PasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public AUserDataGenerator(UserRepository userRepository, NewsRepository newsRepository) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        faker = new Faker();
    }

    @PostConstruct
    private void generateUser() {
        if (userRepository.count() > 3) {
            LOGGER.info("user already generated");
        } else {
            LOGGER.info("generating {} user entries", NUMBER_OF_USER_TO_GENERATE);

            userRepository.save(
                User.builder()
                    .userName("user")
                    .password(encoder.encode("password"))
                    .notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
                    .role(2)
                    .blocked(false)
                    .build()
            );

            userRepository.save(
                User.builder()
                    .userName("Florian")
                    .password(encoder.encode("Florian"))
                    .notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
                    .role(2)
                    .blocked(false)
                    .build()
            );

            userRepository.save(
                User.builder()
                    .userName("Monika")
                    .password(encoder.encode("Monika"))
                    .notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
                    .role(2)
                    .blocked(false)
                    .build()
            );


        }
    }
    @PostConstruct
    private void generateAdmin() {
        if (userRepository.count() > 3) {
            LOGGER.info("admin already generated");
        } else {
            LOGGER.info("generating {} admin entries");

            userRepository.save(
                User.builder()
                    .userName("admin")
                    .password(encoder.encode("password"))
                    .notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
                    .role(1)
                    .blocked(false)
                    .build()
            );

            userRepository.save(
                User.builder()
                    .userName("Stefan")
                    .password(encoder.encode("Stefan"))
                    .notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
                    .role(1)
                    .blocked(false)
                    .build()
            );

            userRepository.save(
                User.builder()
                    .userName("David")
                    .password(encoder.encode("David"))
                    .notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
                    .role(1)
                    .blocked(false)
                    .build()
            );

            userRepository.save(
                User.builder()
                    .userName("Verena")
                    .password(encoder.encode("Verena"))
                    .notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
                    .role(1)
                    .blocked(false)
                    .build()
            );


        }
    }
}

