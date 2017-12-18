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


import javax.swing.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;

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
        Customer customer = customerRepository.findOneById(id).orElseThrow(NotFoundException::new);
        if(!validateCustomer(customer)){
            throw new CustomerNotValidException("Customer not valid");
        }
        return customer;
    }

    @Override
    public Customer createCustomer(Customer customer) throws CustomerNotValidException {
        if(!validateCustomer(customer)){
        throw new CustomerNotValidException("Customer was not valid!"); }
        OffsetTime now = OffsetTime.now(ZoneOffset.UTC);
        String format = now.format(DateTimeFormatter.ISO_LOCAL_TIME);
        format.replace("-", "");
        format.replace(".", "");
        format.replace("'", "");
        format.replace(":", "");
        System.out.println(format);
        char[] myChar = format.toCharArray();

        String seq = ""+myChar[myChar.length-1]+myChar[1]+myChar[0]+myChar[myChar.length-3]+myChar[myChar.length-2]+myChar[myChar.length-4];
        Long knr =Long.valueOf(seq);
        System.out.println(knr);
        customer.setKnr(knr);
        Customer c = customerRepository.save(customer);
        return c;
    }

    @Override
    public void updateCustomer(Customer customer) throws CustomerNotValidException, InvalidIdException {
        if(!validateCustomer(customer)){
        throw new CustomerNotValidException("Customer was not valid!"); }

        customerRepository.setCustomerInfoByKnr(customer.getName(), customer.getSurname(), customer.getEmail(), Timestamp.valueOf(customer.getBirthDate().atStartOfDay()), customer.getKnr());
    }

    @Override
    public Customer findByKnr(Long knr) throws InvalidIdException, CustomerNotValidException {
        if(!validateIdOrKnr(knr)){
            throw new InvalidIdException("No valid knr!");
        }
        Customer customer= customerRepository.findOneByKnr(knr);
        return customer;
    }

    @Override
    public List<Customer> findByName(String name, Pageable request) {
        Page<Customer> p = customerRepository.findByNameStartingWithIgnoreCaseOrSurnameStartingWithIgnoreCase(name, name, request);
        return p.getContent();
    }

    @Override
    public List<Customer> findBySurname(String surname, Pageable request) {
        Page<Customer> p = customerRepository.readBySurnameStartingWithIgnoreCase(surname, request);
        return p.getContent();
    }


    private boolean validateIdOrKnr(Long id){
        return id >= 0;
    }

    private boolean validateCustomer(Customer customer){
        if(customer.getBirthDate().isAfter(LocalDate.now())){
            return false;
        }
        if(ChronoUnit.DAYS.between(customer.getBirthDate(),LocalDate.now())<14*365){
            return false;
        }
        if(customer.getKnr()!= null && customer.getKnr()<0){
            return false;
        }
        if(customer.getId() != null && customer.getId()<0){
            return false;
        }
        if(customer.getEmail()!= null && !customer.getEmail().isEmpty()){
            if(!customer.getEmail().contains("@") || !customer.getEmail().contains(".")){
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
