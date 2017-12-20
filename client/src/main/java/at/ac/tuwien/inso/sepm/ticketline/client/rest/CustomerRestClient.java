package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;

import java.util.List;

public interface CustomerRestClient {


    /**
     * Find all customer entries.
     *
     * @return list of customer entries
     * @throws DataAccessException in case something went wrong
     */
    List<CustomerDTO> findAll(int pageIndex, int costumerPerPage) throws DataAccessException;

    /**
     * Find customer entries by name
     *
     * @param name of Customer entry
     * @return list of customer entries
     * @throws DataAccessException in case something went wrong
     */
    List<CustomerDTO> findByName(String name, int pageIndex, int costumerPerPage) throws DataAccessException;

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