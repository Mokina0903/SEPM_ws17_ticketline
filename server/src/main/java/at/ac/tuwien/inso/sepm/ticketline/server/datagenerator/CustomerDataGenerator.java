package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Profile("generateData")
@Component
public class CustomerDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsDataGenerator.class);
    private static final int NUMBER_OF_CUSTOMER_TO_GENERATE = 50;

    private final CustomerRepository customerRepository;
    private final Faker faker;

    public CustomerDataGenerator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        faker = new Faker();
    }

    @PostConstruct
    private void generateCustomer() {
        if (customerRepository.count() > 0) {
            LOGGER.info("customer already generated");
        } else {
            LOGGER.info("generating {} news entries", NUMBER_OF_CUSTOMER_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_CUSTOMER_TO_GENERATE; i++) {
                Customer customer = Customer.builder()
                    .knr((long)i)
                    .surname(faker.name().lastName())
                    .name(faker.name().firstName())
                    .mail(faker.pokemon().name()+"@" + faker.internet().domainName())
                    .birthDate(
                            faker.date()
                                .birthday(14, 100).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        )
                    .build();
                LOGGER.debug("saving customer {}", customer);
                customerRepository.save(customer);
            }
        }
    }

}
