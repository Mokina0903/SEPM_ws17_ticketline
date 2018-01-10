package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.time.LocalDate;
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
     * @return a list of customer, though the size of the list is dependent of the pageable object
     */
    List<Customer> findAll();

    /**
     *
     * @param knr of the wanted customer
     * @return the customer with the given knr
     */
    Customer findOneByKnr(Long knr);

    /**
     *
     * @param name substring of firstname of the wanted costumers
     * @param surname  substring of surname of the wanted costumers
     * @return a list of customer,  though the size of the list is dependent of the pageable object
     */
    List<Customer> findByNameStartingWithIgnoreCaseOrSurnameStartingWithIgnoreCase(String name, String surname);


/*    *//**
     *
     * @param surname surname of the wanted costumers
     * @param request  defienes how to read paged from the database
     * @return a list of customer,  though the size of the list is dependent of the pageable object
     *//*
    Page<Customer> readBySurnameStartingWithIgnoreCase(String surname, Pageable request);*/

    /**
     *
     * @param name to be updated
     * @param surname to be updated
     * @param email to be updated
     * @param birthDate to be upated
     * @param knr of the customer, wohms data needs to be edited
     */
    @Modifying
    @Transactional
    @Query(value = "update Customer u set u.name = ?1, u.surname = ?2, u.email = ?3, u.birthdate = ?4 where u.knr = ?5", nativeQuery = true)
    void setCustomerInfoByKnr(String name, String surname, String email, Timestamp birthDate, Long knr);

}
