package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value= "/{id}/{pageIndex}/{customerPerPage}",method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple news entries")
    public List<CustomerDTO> findAll(@PathVariable("id") Long id, @PathVariable("pageIndex")int pageIndex, @PathVariable("customerPerPage")int customerPerPage){
        PageRequest request = new PageRequest(pageIndex, customerPerPage, Sort.Direction.ASC, "surname");
        return customerMapper.customerToCustomerDTO(customerService.findAll(request));
    }
}
