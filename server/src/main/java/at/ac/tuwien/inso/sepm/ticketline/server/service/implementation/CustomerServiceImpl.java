package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidIdException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository repository){
        customerRepository = repository;
    }

    @Override
    public List<Customer> findAll(Pageable request) {
        Page<Customer> p = customerRepository.findAll(request);
        return p.getContent();
    }

    @Override
    public Customer findOneById(Long id) throws InvalidIdException {
        if(!validate(id)){
            throw new InvalidIdException("Given id was not valid.");
        }
        return customerRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }


    private boolean validate(Long id){
        if(id < 0){
            return false;
        }
        return true;
    }
}
