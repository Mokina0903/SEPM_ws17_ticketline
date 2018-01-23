package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SimpleCustomerService implements CustomerService{
    private final CustomerRestClient customerRestClient;

    public SimpleCustomerService(CustomerRestClient customerRestClient) {
        this.customerRestClient = customerRestClient;
    }
    @Override
    public Page<CustomerDTO> findAll(Pageable request) throws DataAccessException {
        return customerRestClient.findAll(request);
    }

    @Override
    public Page<CustomerDTO> findByName(String name, Pageable request) throws DataAccessException, SearchNoMatchException {
        Page<CustomerDTO> customer = customerRestClient.findByName(name, request);
        if (customer.getContent().isEmpty()) {
            throw new SearchNoMatchException();
        }
        return customer;
    }

    @Override
    public CustomerDTO findByNumber(Long customerNumber) throws DataAccessException, SearchNoMatchException {
        CustomerDTO customer =  customerRestClient.findByNumber(customerNumber);
        if (customer == null) {
            throw new SearchNoMatchException();
        }
        return customer;
    }

    @Override
    public void saveCustomer(CustomerDTO customer) throws DataAccessException {
        customerRestClient.saveCustomer(customer);
    }

    @Override
    public void updateCustomer(CustomerDTO customer) throws DataAccessException {
        customerRestClient.updateCustomer(customer);
    }

    @Override
    public boolean checkIfCustomerValid(CustomerDTO customer) {

        return (checkIfCustomerNameValid(customer.getName()) && checkIfCustomerNameValid(customer.getSurname())
        && checkIfCustomerEmailValid(customer.getEmail()) && checkIfCustomerBirthdateValid(customer.getBirthDate()));
    }

    @Override
    public boolean checkIfCustomerNameValid(String name) {
        return !(name.equals("") || name.length() > 50 || name.isEmpty());
    }

    @Override
    public boolean checkIfCustomerEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean checkIfCustomerBirthdateValid(LocalDate birthdate) {
        return (ChronoUnit.DAYS.between(birthdate, LocalDate.now()) >= 14*365);
    }
}
