package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;

import java.awt.print.Pageable;
import java.util.List;

public interface CustomerService {

    List<Customer> findAll(Pageable request);

    Customer findOneById(Long id);

}
