package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
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
    public List<CustomerDTO> findAll(int pageIndex, int customersPerPage) throws DataAccessException {
        return customerRestClient.findAll(pageIndex, customersPerPage);
    }

    @Override
    public List<CustomerDTO> findByName(String name, int pageIndex, int customersPerPage) throws DataAccessException, SearchNoMatchException {
        List<CustomerDTO> customer = customerRestClient.findByName(name, pageIndex, customersPerPage);
        if (customer.isEmpty()) {
            throw new SearchNoMatchException();
        }
        return customer;
    }

    @Override
    public List<CustomerDTO> findByNumber(Long customerNumber) throws DataAccessException, SearchNoMatchException {
        CustomerDTO customer =  customerRestClient.findByNumber(customerNumber);
        if (customer == null) {
            throw new SearchNoMatchException();
        }
        List<CustomerDTO> list = new ArrayList<>();
        list.add(customer);
        return list;
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
