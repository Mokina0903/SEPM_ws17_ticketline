package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.CustomerNotValidException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidIdException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    /**
     * @param request pageRequest, defining which page and how many items per page will be loaded
     * @return List of costumers, as defined in request
     */
    Page<Customer> findAll(Pageable request);

    /**
     * @param id of the wanted customer
     * @return the customer with the given id
     * @throws InvalidIdException        throwen when id  <0
     * @throws CustomerNotValidException throwen when the returend customer is not valid
     */
    Customer findOneById(Long id) throws InvalidIdException, CustomerNotValidException;

    /**
     * @param customer the customer to save in the database
     * @return the created customer with its generated id and knr
     */
    Customer createCustomer(Customer customer) throws CustomerNotValidException;

    /**
     * @param customer the customer to be updated
     * @throws CustomerNotValidException when the given customer is not valid
     */
    void updateCustomer(Customer customer) throws CustomerNotValidException, InvalidIdException;

    /**
     * @param knr of the wanted customer
     * @return the customer with the given knr
     * @throws InvalidIdException        throwen when the knr <0
     * @throws CustomerNotValidException throwen when the found customer is not valid
     */
    Customer findByKnr(Long knr) throws InvalidIdException, CustomerNotValidException;

    /**
     * @param name    firstname of the wanted costumer
     * @param request pageRequest, defining which page and how many items per page will be loaded
     * @return Page of costumers, as defined in request
     */
    Page<Customer> findByName(String name, Pageable request);

}
