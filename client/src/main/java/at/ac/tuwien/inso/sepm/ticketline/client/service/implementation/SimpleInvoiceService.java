package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InvoiceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SimpleInvoiceService implements InvoiceService{

    private final InvoiceRestClient invoiceRestClient;

    public SimpleInvoiceService( InvoiceRestClient invoiceRestClient ) {
        this.invoiceRestClient = invoiceRestClient;
    }

    @Override
    public Page<InvoiceDTO> findAll( Pageable request ) throws DataAccessException {
        return invoiceRestClient.findAll(request);
    }

    @Override
    public InvoiceDTO findOneById( Long id ) throws DataAccessException {
        return invoiceRestClient.findOneById(id);
    }

    @Override
    public InvoiceDTO create( InvoiceDTO invoice ) throws DataAccessException {
        return invoiceRestClient.create(invoice);
    }

    public PDDocument invoiceToPdf(InvoiceDTO invoiceDTO){
        PDDocument doc = new PDDocument();

        PDPage page = new PDPage();
        doc.addPage(page);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
            contents.beginText();
            contents.setFont(font, 12);
            contents.newLineAtOffset(100, 700);
            contents.showText("Invoice NR:" +invoiceDTO.getInvoiceNumber());
            contents.endText();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            doc.save("Invoice"+invoiceDTO.getInvoiceNumber()+".pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;

    }
}
