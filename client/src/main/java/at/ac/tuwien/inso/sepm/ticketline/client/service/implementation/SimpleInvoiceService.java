package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InvoiceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    @Override
    public File invoiceToPdf(InvoiceDTO invoiceDTO, Window window) throws DataAccessException {

        URL formTemplate = getClass().getResource("/invoice_template/Invoice_Template_new.pdf") ;
           try (PDDocument pdfDocument = PDDocument.load(new File(formTemplate.getPath()))) {

               PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

               if (acroForm != null) {
                   // Retrieve an individual field and set its value.

                   SimpleEventDTO event= invoiceDTO.getTickets().get(0).getEvent();
                   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


                   PDTextField field = (PDTextField) acroForm.getFields().get(0);
                   field.setValue("Ticketline 3 - "+(invoiceDTO.isStorno()?"REVERSAL":"INVOICE"));


                   field = (PDTextField) acroForm.getFields().get(1);
                   field.setValue("Ticketline 3");
                   field = (PDTextField) acroForm.getFields().get(2);
                   field.setValue("");
                   field = (PDTextField) acroForm.getFields().get(3);
                   field.setValue("");
                   field = (PDTextField) acroForm.getFields().get(4);
                   field.setValue("");

                   field = (PDTextField) acroForm.getFields().get(5);
                   field.setValue(invoiceDTO.getCustomer().getName()+" "+ invoiceDTO.getCustomer().getSurname());

                   field = (PDTextField) acroForm.getFields().get(7);
                   field.setValue("");
                   field = (PDTextField) acroForm.getFields().get(9);
                   field.setValue("");
                   field = (PDTextField) acroForm.getFields().get(11);
                   field.setValue("");

                   //todo: insert id to 6

                   field = (PDTextField) acroForm.getFields().get(8);
                   field.setValue(invoiceDTO.getInvoiceDate().format(formatter));
                   field = (PDTextField) acroForm.getFields().get(10);
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
                       if((invoiceDTO.isStorno()&&ticketDTO.isDeleted()) || (!invoiceDTO.isStorno() && !ticketDTO.isDeleted())) {
                           switch (ticketDTO.getSeat().getSector()) {
                               case 'a':
                                   secA.add(ticketDTO);
                                   break;
                               case 'b':
                                   secB.add(ticketDTO);
                                   break;
                               case 'c':
                                   secC.add(ticketDTO);
                                   break;
                               case 'd':
                                   secD.add(ticketDTO);
                                   break;
                               case 'e':
                                   secE.add(ticketDTO);
                                   break;
                           }
                       }
                   }

                   int line=0;
                   for(List<TicketDTO> sec:allSectors) {

                       if (!sec.isEmpty() && !invoiceDTO.isStorno()) {
                           field = (PDTextField) acroForm.getFields().get(14+(line*4));
                           field.setValue(invoiceDTO.isStorno()?"-":""+event.getTitle() + " Ticket(s) in Sector \""+sec.get(0).getSeat().getSector()+"\"");
                           field = (PDTextField) acroForm.getFields().get(15+(line*4));
                           field.setValue(sec.size() + "");
                           field = (PDTextField) acroForm.getFields().get(16+(line*4));
                           field.setValue(invoiceDTO.isStorno()?"-":""+sec.get(0).getPriceInEuro() + " \u20ac");
                           field = (PDTextField) acroForm.getFields().get(17+(line*4));
                           field.setValue(invoiceDTO.isStorno()?"-":""+Math.round((100D*sec.get(0).getPriceInEuro()) * sec.size())/100D + " \u20ac");
                           if(line==3 || line==4){
                               field = (PDTextField) acroForm.getFields().get(17+(line*4));
                               field.setValue(invoiceDTO.isStorno()?"-":""+event.getTitle() + " Ticket(s) in Sector \""+sec.get(0).getSeat().getSector()+"\"");
                               field = (PDTextField) acroForm.getFields().get(14+(line*4));
                               field.setValue(sec.size() + "");
                               field = (PDTextField) acroForm.getFields().get(15+(line*4));
                               field.setValue(invoiceDTO.isStorno()?"-":""+sec.get(0).getPriceInEuro() + " \u20ac");
                               field = (PDTextField) acroForm.getFields().get(16+(line*4));
                               field.setValue(invoiceDTO.isStorno()?"-":""+Math.round((100D*sec.get(0).getPriceInEuro()) * sec.size())/100D + " \u20ac");
                           }
                           line++;
                       }
                   }

                   field = (PDTextField) acroForm.getFields().get(38);
                   field.setValue(invoiceDTO.isStorno()?"-":""+invoiceDTO.getTotalPriceInEuro()+"");
                   field = (PDTextField) acroForm.getFields().get(40);
                   field.setValue(invoiceDTO.isStorno()?"-":""+invoiceDTO.getTotalPriceInEuro()+"\u20ac");

               }

               // Save the filled out form.
/*
               File template =  new File(getClass().getResource("/invoice_template/Invoice_Template.pdf").getPath()) ;
               File parent= template.getParentFile();
               File doc = new File(parent.getPath()+"/Invoice"+invoiceDTO.getInvoiceNumber()+".pdf");
               pdfDocument.save(doc);

               invoiceRestClient.saveInvoicePdf(doc);

               //open pdf
               try {
                   Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + doc);
               } catch (IOException e) {
                   //todo: new exception
                   e.printStackTrace();
               }*/

               FileChooser fileChooser = new FileChooser();
               fileChooser.setInitialFileName("Invoice"+invoiceDTO.getInvoiceNumber()+".pdf");
               File file= fileChooser.showSaveDialog(window);
               pdfDocument.save(file);

               return file;

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
