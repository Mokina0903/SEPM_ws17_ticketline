package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import org.springframework.stereotype.Service;

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
    public List<CustomerDTO> findByName(String name, int pageIndex, int customersPerPage) throws DataAccessException {
        return customerRestClient.findByName(name, pageIndex, customersPerPage);
    }

    @Override
    public List<CustomerDTO> findByNumber(Long customerNumber) throws DataAccessException, SearchNoMatchException {
        return customerRestClient.findByNumber(customerNumber);
    }

    @Override
    public void saveCustomer(CustomerDTO customer) throws DataAccessException {
        customerRestClient.saveCustomer(customer);
    }
}
