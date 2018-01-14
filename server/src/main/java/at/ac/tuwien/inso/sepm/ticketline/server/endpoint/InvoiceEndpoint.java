package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.invoice.InvoiceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/invoice")
@Api(value = "invoice")
public class InvoiceEndpoint {

    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;

    public InvoiceEndpoint( InvoiceService invoiceService, InvoiceMapper invoiceMapper ) {
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }

    @RequestMapping(value= "/{pageIndex}/{invoicesPerPage}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of invoice entries")
    public Page<InvoiceDTO> findAll( @PathVariable("pageIndex")int pageIndex, @PathVariable("invoicesPerPage")int invoicesPerPage){
        Pageable request = new PageRequest(pageIndex, invoicesPerPage, Sort.Direction.ASC, "invoiceDate");
        Page<Invoice> invoicePage = invoiceService.findAll(request);
        List<InvoiceDTO> dtos = invoiceMapper.invoiceToInvoiceDTO(invoicePage.getContent());
        return new PageImpl<>(dtos, request, invoicePage.getTotalElements());
    }

    @RequestMapping(value = "/{invoiceId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about a specific invoice entry")
    public InvoiceDTO findOneById( @PathVariable Long invoiceId) {
        return invoiceMapper.invoiceToInvoiceDTO(invoiceService.findOneById(invoiceId));
    }

    @RequestMapping(value="/create", method = RequestMethod.POST)
    @ApiOperation(value = "Create and save the given invoice")
    public InvoiceDTO createInvoice(@RequestBody InvoiceDTO invoiceDTO){
        return invoiceMapper.invoiceToInvoiceDTO(invoiceService.save(invoiceMapper.invoiceDTOToInvoice(invoiceDTO)));
    }
}
