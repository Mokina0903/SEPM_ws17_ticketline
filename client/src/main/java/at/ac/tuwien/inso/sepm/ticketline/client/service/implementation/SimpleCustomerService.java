package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleCustomerService implements CustomerService{
    @Override
    public List<CustomerDTO> findAll(int pageIndex, int customersPerPage) throws DataAccessException {
        return null;
    }

    @Override
    public List<CustomerDTO> findByName(String name) throws DataAccessException {
        return null;
    }

    @Override
    public List<CustomerDTO> findByNumber(Long customerNumber) throws DataAccessException {
        return null;
    }
}
