package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;

import java.time.LocalDate;
import java.util.List;

public interface CustomerService {


    /**
     * Find all customer entries.
     *
     * @return list of customer entries
     * @throws DataAccessException in case something went wrong
     */
    List<CustomerDTO> findAll(int pageIndex, int customersPerPage) throws DataAccessException;

    /**
     * Find customer entries by name
     *
     * @param name of Customer entry
     * @return list of customer entries
     * @throws DataAccessException in case something went wrong
     * @throws SearchNoMatchException if nothing is found
     */
    List<CustomerDTO> findByName(String name, int pageIndex, int customersPerPage) throws DataAccessException, SearchNoMatchException;

    /**
     * Find specific customer by customerNumber
     *
     * @param customerNumber of Customer entry
     * @return CustomerDTO
     * @throws DataAccessException in case something went wrong
     * @throws SearchNoMatchException if nothing is found
     */
    List<CustomerDTO> findByNumber(Long customerNumber) throws DataAccessException, SearchNoMatchException;

    void saveCustomer(CustomerDTO customer) throws DataAccessException;

    void updateCustomer(CustomerDTO customer) throws DataAccessException;

    boolean checkIfCustomerValid(CustomerDTO customer);

    boolean checkIfCustomerNameValid(String name);

    boolean checkIfCustomerEmailValid(String email);

    boolean checkIfCustomerBirthdateValid(LocalDate birthdate);
}
