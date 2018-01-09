package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomerService {


    /**
     * Find all customer entries.
     *
     * @return page of customer entries
     * @throws DataAccessException in case something went wrong
     */
    Page<CustomerDTO> findAll(Pageable request) throws DataAccessException;

    /**
     * Find customer entries by name
     *
     * @param name of Customer entry
     * @param request for pagination

     * @return list of customer entries
     * @throws DataAccessException in case something went wrong
     * @throws SearchNoMatchException if nothing is found
     */
    Page<CustomerDTO> findByName(String name, Pageable request) throws DataAccessException, SearchNoMatchException;

    /**
     * Find specific customer by customerNumber
     *
     * @param customerNumber of Customer entry
     * @return CustomerDTO
     * @throws DataAccessException in case something went wrong
     * @throws SearchNoMatchException if nothing is found
     */
    CustomerDTO findByNumber(Long customerNumber) throws DataAccessException, SearchNoMatchException;

    void saveCustomer(CustomerDTO customer) throws DataAccessException;

    void updateCustomer(CustomerDTO customer) throws DataAccessException;

    boolean checkIfCustomerValid(CustomerDTO customer);

    boolean checkIfCustomerNameValid(String name);

    boolean checkIfCustomerEmailValid(String email);

    boolean checkIfCustomerBirthdateValid(LocalDate birthdate);
}
