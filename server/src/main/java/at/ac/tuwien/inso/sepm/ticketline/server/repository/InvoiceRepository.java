package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long>{

    /**
     *find all invoices
     *
     * @param request defienes how to read paged from the database
     * @return a list of invoices, though the size of the list is dependent of the pageable object
     */
    Page<Invoice> findAll( Pageable request);

    /**
     * Find a single invoice entry by id.
     *
     * @param id the is of the invoice entry
     * @return Optional containing the invoice entry
     */
    Optional<Invoice> findOneById( Long id);


}
