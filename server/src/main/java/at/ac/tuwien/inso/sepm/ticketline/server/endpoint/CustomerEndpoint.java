package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.CustomerNotValidException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidIdException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.matcher.NullMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import java.util.List;

@RestController
@RequestMapping(value = "/customer")
@Api(value = "customer")
public class CustomerEndpoint {
    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    public CustomerEndpoint(CustomerMapper mapper, CustomerService service){
        this.customerMapper = mapper;
        this.customerService = service;
    }

    @RequestMapping(value= "/pageIndex}/{customerPerPage}", method = RequestMethod.GET)
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
        try {
            return customerMapper.customerToCustomerDTO(customerService.findByKnr(knr));
        } catch (InvalidIdException | CustomerNotValidException e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value="/create", method = RequestMethod.POST)
    @ApiOperation(value = "Create and save the given customer")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customer){
        try {
            return customerMapper.customerToCustomerDTO(customerService.createCustomer(customerMapper.customerDTOToCustomer(customer)));
        } catch (CustomerNotValidException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value="/create", method = RequestMethod.POST)
    @ApiOperation(value = "Update and save the given customer")
    public void updateCustomer(@RequestBody CustomerDTO customerDTO){
        try {
           customerService.updateCustomer(customerMapper.customerDTOToCustomer(customerDTO));
        } catch (CustomerNotValidException e) {
            e.printStackTrace();
        }

    }



}
