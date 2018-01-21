package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.CustomerNotValidException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidIdException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository repository) {
        customerRepository = repository;
    }

    @Override
    public Page<Customer> findAll(Pageable request) {
        List<Customer> customer = customerRepository.findAll();
        int start = request.getOffset();
        int end = (start + request.getPageSize()) > customer.size() ? customer.size() : (start + request.getPageSize());
        Page<Customer> e = new PageImpl<>(customer.subList(start, end), request, customer.size());
        System.out.println( " IN SERVIC SERver custom: _______________ " + e.getContent().size());

        return new PageImpl<>(customer.subList(start, end), request, customer.size());
    }

    @Override
    public Customer findOneById(Long id) throws InvalidIdException, CustomerNotValidException {
        if (!validateIdOrKnr(id)) {
            throw new InvalidIdException("Given id was not valid.");
        }

        Optional<Customer> customer = customerRepository.findOneById(id);

        if (customer == null) {
            throw new NotFoundException("knr " + id);
        }

        if (!validateCustomer(customer.get())) {
            throw new CustomerNotValidException("Customer not valid");
        }
        return customer.get();
    }

    @Override
    public Customer createCustomer(Customer customer) throws CustomerNotValidException {
        if (!validateCustomer(customer)) {
            throw new CustomerNotValidException("Customer was not valid!");
        }
        OffsetTime now = OffsetTime.now(ZoneOffset.UTC);
        String format = now.format(DateTimeFormatter.ISO_LOCAL_TIME);
        format.replace("-", "");
        format.replace(".", "");
        format.replace("'", "");
        format.replace(":", "");
        System.out.println(format);
        char[] myChar = format.toCharArray();

        String seq = "" + myChar[myChar.length - 1] + myChar[1] + myChar[0] + myChar[myChar.length - 3] + myChar[myChar.length - 2] + myChar[myChar.length - 4];
        Long knr = Long.valueOf(seq);
        System.out.println(knr);
        customer.setKnr(knr);
        Customer c = customerRepository.save(customer);
        return c;
    }

    @Override
    public void updateCustomer(Customer customer) throws CustomerNotValidException, InvalidIdException {
        if (!validateCustomer(customer)) {
            throw new CustomerNotValidException("Customer was not valid!");
        }

        customerRepository.setCustomerInfoByKnr(customer.getName(), customer.getSurname(), customer.getEmail(), Timestamp.valueOf(customer.getBirthDate().atStartOfDay()), customer.getKnr(),customer.getVersion());
    }

    @Override
    public Customer findByKnr(Long knr) throws InvalidIdException, CustomerNotValidException {
        if (!validateIdOrKnr(knr)) {
            throw new InvalidIdException("No valid knr!");
        }
        Customer customer = customerRepository.findOneByKnr(knr);
        return customer;
    }

    @Override
    public Page<Customer> findByName(String name, Pageable request) {
        List<Customer> customer = customerRepository.findByNameStartingWithIgnoreCaseOrSurnameStartingWithIgnoreCase(name, name);
        int start = request.getOffset();
        int end = (start + request.getPageSize()) > customer.size() ? customer.size() : (start + request.getPageSize());
        return new PageImpl<>(customer.subList(start, end), request, customer.size());
    }

/*
    @Override
    public List<Customer> findBySurname(String surname, Pageable request) {
        Page<Customer> p = customerRepository.readBySurnameStartingWithIgnoreCase(surname, request);
        return p.getContent();
    }
*/


    private boolean validateIdOrKnr(Long id) {
        return id >= 0;
    }

    private boolean validateCustomer(Customer customer) {
        if (customer.getBirthDate().isAfter(LocalDate.now())) {
            return false;
        }
        if (ChronoUnit.DAYS.between(customer.getBirthDate(), LocalDate.now()) < 14 * 365) {
            return false;
        }
        if (customer.getKnr() != null && customer.getKnr() < 0) {
            return false;
        }
        if (customer.getId() != null && customer.getId() < 0) {
            return false;
        }

        if (customer.getEmail() != null && !customer.getEmail().isEmpty() && customer.getEmail().length() <= 100) {

            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(customer.getEmail());

            if (!matcher.matches()) {
                return false;
            }
        }

        if (customer.getName() == null || customer.getName().equals("")) {
            return false;
        }
        if (customer.getSurname() == null || customer.getSurname().equals("")) {
            return false;
        }
        return true;
    }
}
