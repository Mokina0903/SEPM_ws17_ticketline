package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidIdException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.OldVersionException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/customer")
@Api(value = "customer")
public class CustomerEndpoint {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CustomerEndpoint.class);

    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    public CustomerEndpoint(CustomerMapper mapper, CustomerService service) {
        this.customerMapper = mapper;
        this.customerService = service;
    }

    @RequestMapping(value = "/{pageIndex}/{customerPerPage}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of customer entries")
    public Page<CustomerDTO> findAll(@PathVariable("pageIndex") int pageIndex, @PathVariable("customerPerPage") int customerPerPage) {
        Pageable request = new PageRequest(pageIndex, customerPerPage, Sort.Direction.ASC, "knr");
        Page<Customer> customerPage = customerService.findAll(request);
        List<CustomerDTO> dtos = customerMapper.customerToCustomerDTO(customerPage.getContent());
        return new PageImpl<>(dtos, request, customerPage.getTotalElements());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get one spezific customer entry by id")
    public CustomerDTO findById(@PathVariable("id") Long id) {
        return customerMapper.customerToCustomerDTO(customerService.findOneById(id));
    }

    @RequestMapping(value = "/findWithKnr/{knr}", method = RequestMethod.GET)
    @ApiOperation(value = "Get one spezific customer entry by knr")
    public CustomerDTO findByKnr(@PathVariable("knr") long knr) {
        LOGGER.info("Finding by KNR in CustomerEndpoint");
        CustomerDTO customer = (customerMapper.customerToCustomerDTO(customerService.findByKnr(knr)));
        return customer;
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "Create and save the given customer")
    public void createCustomer(@RequestBody CustomerDTO customer) {
        customerService.createCustomer(customerMapper.customerDTOToCustomer(customer));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "Update and save the given customer")
    public void updateCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerService.findByKnr(customerDTO.getKnr());
        if (customer == null)
            throw new InvalidIdException("No valid knr!");

        if (!customer.correctVersion(customerDTO.getVersion())) {
            if (!customer.equalsUpdate(customerMapper.customerDTOToCustomer(customerDTO))) {
                throw new OldVersionException();
            }
        }
        customerDTO.setVersion(customerDTO.getVersion() + 1);
        customerService.updateCustomer(customerMapper.customerDTOToCustomer(customerDTO));
    }

    @RequestMapping(value = "/findName/{pageIndex}/{customerPerPage}/{name}", method = RequestMethod.GET)
    @ApiOperation(value = "Gets all customers with the given name")
    public Page<CustomerDTO> findByName(@PathVariable("pageIndex") int pageIndex, @PathVariable("customerPerPage") int customerPerPage, @PathVariable("name") String name) {
        Pageable request = new PageRequest(pageIndex, customerPerPage, Sort.Direction.ASC, "knr");
        Page<Customer> customerPage = customerService.findByName(name, request);
        List<CustomerDTO> dtos = customerMapper.customerToCustomerDTO(customerPage.getContent());
        return new PageImpl<>(dtos, request, customerPage.getTotalElements());
    }


}
