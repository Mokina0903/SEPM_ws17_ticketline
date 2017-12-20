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

        if (!customer.getEmail().contains("@") || !customer.getEmail().contains(".") || customer.getEmail().length()>100) {
         return false;
        }
        if (customer.getName() == null || customer.getName().length() >50 || customer.getSurname().length() >50 || customer.getName().isEmpty() || customer.getSurname() == null || customer.getSurname().isEmpty()) {
            return false;
        }
        if(customer.getBirthDate() == null || customer.getBirthDate().isAfter(LocalDate.now())){
            return false;
        }
        if(ChronoUnit.DAYS.between(customer.getBirthDate(),LocalDate.now())<14*365){
            return false;
        }
        return true;
    }
}
