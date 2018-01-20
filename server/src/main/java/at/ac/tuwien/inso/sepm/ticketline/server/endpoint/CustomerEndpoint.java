package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.CustomerNotValidException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidIdException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.OldVersionException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: (Verena) Warum Exception handling

@RestController
@RequestMapping(value = "/customer")
@Api(value = "customer")
public class CustomerEndpoint {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CustomerEndpoint.class);

    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    public CustomerEndpoint(CustomerMapper mapper, CustomerService service){
        this.customerMapper = mapper;
        this.customerService = service;
    }

    @RequestMapping(value= "/{pageIndex}/{customerPerPage}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of customer entries")
    public Page<CustomerDTO> findAll(@PathVariable("pageIndex")int pageIndex, @PathVariable("customerPerPage")int customerPerPage){
        Pageable request = new PageRequest(pageIndex, customerPerPage, Sort.Direction.ASC, "start_of_event");
        Page<Customer> customerPage = customerService.findAll(request);
        List<CustomerDTO> dtos = customerMapper.customerToCustomerDTO(customerPage.getContent());
        return new PageImpl<>(dtos, request, customerPage.getTotalElements());
    }

    @RequestMapping(value= "/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "Get one spezific customer entry by id")
    public CustomerDTO findById(@PathVariable("id") Long id){
        return customerMapper.customerToCustomerDTO(customerService.findOneById(id));
    }

    @RequestMapping(value="/findWithKnr/{knr}", method = RequestMethod.GET)
    @ApiOperation(value = "Get one spezific customer entry by knr")
    public CustomerDTO findByKnr(@PathVariable("knr") long knr){
        LOGGER.info("Finding by KNR in CustomerEndpoint");
        try {
            CustomerDTO customer = (customerMapper.customerToCustomerDTO(customerService.findByKnr(knr)));
            return customer;
        } catch (InvalidIdException | CustomerNotValidException e) {
          //  e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value="/create", method = RequestMethod.POST)
    @ApiOperation(value = "Create and save the given customer")
    public void createCustomer(@RequestBody CustomerDTO customer){
        // TODO: (Verena) Mapper notwendig?
        customerMapper.customerToCustomerDTO(customerService.createCustomer(customerMapper.customerDTOToCustomer(customer)));
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    @ApiOperation(value = "Update and save the given customer")
    public void updateCustomer(@RequestBody CustomerDTO customerDTO){
        // TODO: (Verena) Was passiert bei unbekannten Benutzer?
        Customer customer = customerService.findByKnr(customerDTO.getKnr());
        if (customer == null)
            throw new InvalidIdException("No valid knr!");

        if (!customer.correctVersion(customerDTO.getVersion())) {
            System.out.println(customer);
            System.out.println("This is new");
            System.out.println(customerMapper.customerDTOToCustomer(customerDTO));
            if (!customer.equalsUpdate(customerMapper.customerDTOToCustomer(customerDTO))) {
                throw new OldVersionException();
            }
        }
        customerDTO.setVersion(customerDTO.getVersion() + 1);
        customerService.updateCustomer(customerMapper.customerDTOToCustomer(customerDTO));
    }

    @RequestMapping(value="/findName/{pageIndex}/{customerPerPage}/{name}", method = RequestMethod.GET)
    @ApiOperation(value = "Gets all customers with the given name")
    public Page<CustomerDTO> findByName(@PathVariable("pageIndex")int pageIndex, @PathVariable("customerPerPage")int customerPerPage, @PathVariable("name") String name){
        Pageable request = new PageRequest(pageIndex, customerPerPage, Sort.Direction.ASC, "start_of_event");
        Page<Customer> customerPage = customerService.findByName(name, request);
        List<CustomerDTO> dtos = customerMapper.customerToCustomerDTO(customerPage.getContent());
        return new PageImpl<>(dtos, request, customerPage.getTotalElements());
    }


    /*@RequestMapping(value="/findSurename/{pageIndex}/{customerPerPage}/{surname}", method = RequestMethod.GET)
    @ApiOperation(value = "Gets all customers with the given name")
    public List<CustomerDTO> findBySurname(@PathVariable("pageIndex")int pageIndex, @PathVariable("customerPerPage")int customerPerPage, @PathVariable("surname") String surname){
        PageRequest request = new PageRequest(pageIndex, customerPerPage, Sort.Direction.ASC, "surname");
        return customerMapper.customerToCustomerDTO(customerService.findBySurname(surname, request));
    }*/



}
