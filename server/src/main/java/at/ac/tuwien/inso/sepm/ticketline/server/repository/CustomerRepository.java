package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return Optional containing the news entry
     */
    Optional<Customer> findOneById(Long id);

    /**
     *
     * @param request defienes how to read paged from the database
     * @return a list of customer, though the size of the list is dependent of the pageable object
     */
    Page<Customer> findAll(Pageable request);

    /**
     *
     * @param knr of the wanted customer
     * @return the customer with the given knr
     */
    Customer findOneByKnr(Long knr);

    /**
     *
     * @param name substring of firstname of the wanted costumers
     * @param surname substring of surname of the wanted costumers
     * @param request  defienes how to read paged from the database
     * @return a list of customer,  though the size of the list is dependent of the pageable object
     */
    Page<Customer> findByNameStartingWithIgnoreCaseOrSurnameStartingWithIgnoreCase(String name, String surname, Pageable request);


    /**
     *
     * @param surname surname of the wanted costumers
     * @param request  defienes how to read paged from the database
     * @return a list of customer,  though the size of the list is dependent of the pageable object
     */
    Page<Customer> readBySurnameStartingWithIgnoreCase(String surname, Pageable request);



}
