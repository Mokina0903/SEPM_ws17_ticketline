package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerRestClient {


    /**
     * Find all customer entries.
     *
     * @return list of customer entries
     * @throws DataAccessException in case something went wrong
     */
    Page<CustomerDTO> findAll(Pageable request) throws DataAccessException;

    /**
     * Find customer entries by name
     *
     * @param name of Customer entry
     * @param request for pagination
     * @return page of customer entries
     * @throws DataAccessException in case something went wrong
     */
    Page<CustomerDTO> findByName(String name, Pageable request) throws DataAccessException;

    /**
     * Find specific customer by customerNumber
     *
     * @param customerNumber of Customer entry
     * @return CustomerDTO
     * @throws DataAccessException in case something went wrong
     */
    CustomerDTO findByNumber(Long customerNumber) throws DataAccessException, SearchNoMatchException;

    void saveCustomer(CustomerDTO customerDTO) throws DataAccessException;
    void updateCustomer(CustomerDTO customer) throws DataAccessException;
}
