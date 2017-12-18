package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.CustomerNotValidException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidIdException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.matcher.NullMatcher;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
    public List<CustomerDTO> findAll(@PathVariable("pageIndex")int pageIndex, @PathVariable("customerPerPage")int customerPerPage){
        PageRequest request = new PageRequest(pageIndex, customerPerPage, Sort.Direction.ASC, "surname");
        return customerMapper.customerToCustomerDTO(customerService.findAll(request));
    }

    @RequestMapping(value= "/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "Get one spezific customer entry by id")
    public CustomerDTO findById(@PathVariable("id") Long id){
        try {
            return customerMapper.customerToCustomerDTO(customerService.findOneById(id));
        } catch (InvalidIdException | CustomerNotValidException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value="/findWithKnr/{knr}", method = RequestMethod.GET)
    @ApiOperation(value = "Get one spezific customer entry by knr")
    public CustomerDTO findByKnr(@PathVariable("knr") long knr){
        LOGGER.info("Finding by KNR in CustomerEndpoint");
        try {
            CustomerDTO customer = (customerMapper.customerToCustomerDTO(customerService.findByKnr(knr)));
            return customer;
        } catch (InvalidIdException | CustomerNotValidException e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value="/create", method = RequestMethod.POST)
    @ApiOperation(value = "Create and save the given customer")
    public void createCustomer(@RequestBody CustomerDTO customer){
        try {
            customerMapper.customerToCustomerDTO(customerService.createCustomer(customerMapper.customerDTOToCustomer(customer)));
        } catch (CustomerNotValidException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    @ApiOperation(value = "Update and save the given customer")
    public void updateCustomer(@RequestBody CustomerDTO customerDTO){
        try {
           customerService.updateCustomer(customerMapper.customerDTOToCustomer(customerDTO));
        } catch (CustomerNotValidException | InvalidIdException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value="/findName/{pageIndex}/{customerPerPage}/{name}", method = RequestMethod.GET)
    @ApiOperation(value = "Gets all customers with the given name")
    public List<CustomerDTO> findByName(@PathVariable("pageIndex")int pageIndex, @PathVariable("customerPerPage")int customerPerPage, @PathVariable("name") String name){
        PageRequest request = new PageRequest(pageIndex, customerPerPage, Sort.Direction.ASC, "surname");
        return customerMapper.customerToCustomerDTO(customerService.findByName(name, request));
    }

    @RequestMapping(value="/findSurename/{pageIndex}/{customerPerPage}/{surname}", method = RequestMethod.GET)
    @ApiOperation(value = "Gets all customers with the given name")
    public List<CustomerDTO> findBySurname(@PathVariable("pageIndex")int pageIndex, @PathVariable("customerPerPage")int customerPerPage, @PathVariable("surname") String surname){
        PageRequest request = new PageRequest(pageIndex, customerPerPage, Sort.Direction.ASC, "surname");
        return customerMapper.customerToCustomerDTO(customerService.findBySurname(surname, request));
    }



}
