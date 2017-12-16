package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)

public class CustomerMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class CustomerMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private CustomerMapper customerMapper;

    private static final long CUSTOMER_ID = 1L;
    private static final long CUSTOMER_NUMBER = 1L;
    private static final String CUSTOMER_NAME = "Max";
    private static final String CUSTOMER_SURNAME = "Mustermann";
    private static final String CUSTOMER_EMAIL = "Maxmustermann@gmail.com";
    private static final LocalDate CUSTOMER_BIRTHDATE = LocalDate.of(1950, 1, 1);

    @Test
    public void shouldMapCustomerToCustomerDTO() {
        Customer customer = Customer.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name(CUSTOMER_NAME)
            .surname(CUSTOMER_SURNAME)
            .mail(CUSTOMER_EMAIL)
            .birthDate(CUSTOMER_BIRTHDATE)
            .build();
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        assertThat(customerDTO).isNotNull();
        assertThat(customerDTO.getId() == (1L));
        assertThat(customerDTO.getKnr() == (CUSTOMER_NUMBER));
        assertThat(customerDTO.getName()).isEqualTo(CUSTOMER_NAME);
        assertThat(customerDTO.getSurname()).isEqualTo(CUSTOMER_SURNAME);
        assertThat(customerDTO.getEmail()).isEqualTo(CUSTOMER_EMAIL);
        assertThat(customerDTO.getBirthDate()).isEqualTo(CUSTOMER_BIRTHDATE);
    }

    @Test
    public void shouldMapDetailedUserDTOToUser() {
        CustomerDTO customerDTO = CustomerDTO.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name(CUSTOMER_NAME)
            .surname(CUSTOMER_SURNAME)
            .mail(CUSTOMER_EMAIL)
            .birthDate(CUSTOMER_BIRTHDATE)
            .build();
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        assertThat(customer).isNotNull();
        assertThat(customer.getId() == (1L));
        assertThat(customer.getKnr() == (CUSTOMER_NUMBER));
        assertThat(customer.getName()).isEqualTo(CUSTOMER_NAME);
        assertThat(customer.getSurname()).isEqualTo(CUSTOMER_SURNAME);
        assertThat(customer.getEmail()).isEqualTo(CUSTOMER_EMAIL);
        assertThat(customer.getBirthDate()).isEqualTo(CUSTOMER_BIRTHDATE);
    }
}
