package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InvoiceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

        String formTemplate = "C:\\Users\\stefa\\Documents\\Informatik\\sepm\\Gruppenphase\\ticketline\\client\\src\\main\\resources\\invoice_template\\Invoice_Template.pdf";
           try (PDDocument pdfDocument = PDDocument.load(new File(formTemplate))) {

               PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

               if (acroForm != null) {
                   // Retrieve an individual field and set its value.

                   SimpleEventDTO event= invoiceDTO.getTickets().get(0).getEvent();
                   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                   PDTextField field = (PDTextField) acroForm.getFields().get(0);
                   field.setValue("TicketLine 3");

                   field = (PDTextField) acroForm.getFields().get(5);
                   field.setValue(invoiceDTO.getCustomer().getName()+" "+ invoiceDTO.getCustomer().getSurname());

                   field = (PDTextField) acroForm.getFields().get(6);
                   field.setValue("");
                   field = (PDTextField) acroForm.getFields().get(7);
                   field.setValue("");
                   field = (PDTextField) acroForm.getFields().get(8);
                   field.setValue("");

                   field = (PDTextField) acroForm.getFields().get(10);
                   field.setValue(invoiceDTO.getInvoiceDate().format(formatter));
                   field = (PDTextField) acroForm.getFields().get(11);
                   field.setValue(invoiceDTO.getInvoiceNumber()+"");
                   field = (PDTextField) acroForm.getFields().get(12);
                   field.setValue(event.getEndOfEvent().format(formatter));

                   field = (PDTextField) acroForm.getFields().get(13);
                   field.setValue(invoiceDTO.isStorno()?"Ticket-Reversal(Storno)":
                       "Tickets bought for  "+event.getTitle());

                   List<TicketDTO> secA=new ArrayList<>();
                   List<TicketDTO> secB=new ArrayList<>();
                   List<TicketDTO> secC=new ArrayList<>();
                   List<TicketDTO> secD=new ArrayList<>();
                   List<TicketDTO> secE=new ArrayList<>();

                   List<List<TicketDTO>> allSectors = new ArrayList<>();
                   allSectors.add(secA);
                   allSectors.add(secB);
                   allSectors.add(secC);
                   allSectors.add(secD);
                   allSectors.add(secE);

                   for(TicketDTO ticketDTO:invoiceDTO.getTickets()){
                       switch(ticketDTO.getSeat().getSector()){
                           case 'a':secA.add(ticketDTO);break;
                           case 'b':secB.add(ticketDTO);break;
                           case 'c':secC.add(ticketDTO);break;
                           case 'd':secD.add(ticketDTO);break;
                           case 'e':secE.add(ticketDTO);break;
                       }
                   }

                   int line=0;
                   for(List<TicketDTO> sec:allSectors) {

                       if (!sec.isEmpty()) {
                           field = (PDTextField) acroForm.getFields().get(14+(line*4));
                           field.setValue(event.getTitle() + " Ticket(s) in Sector \""+sec.get(0).getSeat().getSector()+"\"");
                           field = (PDTextField) acroForm.getFields().get(15+(line*4));
                           field.setValue(sec.size() + "");
                           field = (PDTextField) acroForm.getFields().get(16+(line*4));
                           field.setValue(sec.get(0).getPriceInEuro() + " \u20ac");
                           field = (PDTextField) acroForm.getFields().get(17+(line*4));
                           field.setValue(Math.round((100D*sec.get(0).getPriceInEuro()) * sec.size())/100D + " \u20ac");
                           line++;
                       }
                   }

                   field = (PDTextField) acroForm.getFields().get(38);
                   field.setValue(invoiceDTO.getTotalPriceInEuro()+"");
                   field = (PDTextField) acroForm.getFields().get(40);
                   field.setValue(invoiceDTO.getTotalPriceInEuro()+"\u20ac");
                   // If a field is nested within the form tree a fully qualified name
                   // might be provided to access the field.
                   //field = (PDTextField) acroForm.getField("fieldsContainer.nestedSampleField");
                   //field.setValue("Text Entry");
               }

               // Save and close the filled out form.
               pdfDocument.save("C:\\Users\\stefa\\Documents\\Informatik\\sepm\\Gruppenphase\\ticketline\\client\\src\\main\\resources\\invoice_template\\Invoice"+invoiceDTO.getInvoiceNumber()+".pdf");



               return pdfDocument;

           } catch (InvalidPasswordException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }

/*
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
        */
        return null;
    }
}
