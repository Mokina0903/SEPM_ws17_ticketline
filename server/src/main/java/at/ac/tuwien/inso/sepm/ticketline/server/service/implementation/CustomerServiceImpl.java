package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.CustomerNotValidException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidIdException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public Customer findOneById(Long id) throws InvalidIdException, CustomerNotValidException {
        if(!validateIdOrKnr(id)){
            throw new InvalidIdException("Given id was not valid.");
        }
        Customer customer =customerRepository.findOneById(id).orElseThrow(NotFoundException::new);
        if(!validateCustomer(customer)){
            throw new CustomerNotValidException("Customer not valid");
        }
        return customer;
    }

    @Override
    public Customer createCustomer(Customer customer) throws CustomerNotValidException {
        if(!validateCustomer(customer)){
        throw new CustomerNotValidException("Customer was not valid!"); }
        Customer c = customerRepository.save(customer);
        return c;
    }

    @Override
    public void updateCustomer(Customer customer) throws CustomerNotValidException {
        if(!validateCustomer(customer)){
        throw new CustomerNotValidException("Customer was not valid!"); }
        customerRepository.save(customer);
    }

    @Override
    public Customer findByKnr(Long knr) throws InvalidIdException, CustomerNotValidException {
        if(!validateIdOrKnr(knr)){
            throw new InvalidIdException("No valid knr!");
        }
        Customer c = customerRepository.findOneByKnr(knr).orElseThrow(NotFoundException::new);
        if(!validateCustomer(c)){
            throw new CustomerNotValidException("Found Customer not valid!");
        }
        return c;
    }

    @Override
    public List<Customer> findByName(String name) {

        return null;
    }

    @Override
    public List<Customer> findBySurname(String surename) {
        return null;
    }


    private boolean validateIdOrKnr(Long id){
        return id >= 0;
    }

    private boolean validateCustomer(Customer customer){
        if(customer.getBirthDate().isAfter(LocalDate.now())){
            return false;
        }
        if(customer.getKnr()<0){
            return false;
        }
        if(customer.getId()<0){
            return false;
        }
        if(customer.getEmail()!= null && !customer.getEmail().isEmpty()){
            if(!customer.getEmail().contains("@")){
                return false;
            }
        }

        if(customer.getName() == null || customer.getName().equals("")){
            return false;
        }
        if(customer.getSurname() == null || customer.getSurname().equals("")){
            return false;
        }
        return true;
    }
}
