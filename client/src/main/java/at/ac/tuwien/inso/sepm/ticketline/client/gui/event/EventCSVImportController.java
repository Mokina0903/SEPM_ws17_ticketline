package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.ErrorDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventCSVImportController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController.class);
    @FXML
    public Button btnAddFile;
    @FXML
    public Button btnImport;
    @FXML
    public TextArea TAlogOutput;
    @FXML
    public Button btnFinish;
    @FXML
    public Label lblChooseFile;
    @FXML
    public Label lblChoosenFile;

    @FXML
    private TabHeaderController tabHeaderController;

    @Autowired
    private LocalizationSubject localizationSubject;

    @Autowired
    private MainController mainController;

    @Autowired
    private EventService eventService;

    private Node oldContent;
    private EventController c;
    private File choosenFile = null;

    @FXML
    void initialize(){
        tabHeaderController.setIcon(FontAwesome.Glyph.FILM);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("event.addEvent"));
        localizationSubject.attach(this);
        lblChooseFile.setFont(Font.font(16));


    }

   void initializeData(EventController c, Node oldContent){
        this.oldContent = oldContent;
       this.c = c;
   }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("event.addEvent"));
        btnAddFile.setText(BundleManager.getBundle().getString("event.chooseYourInputFile"));
        btnImport.setText(BundleManager.getBundle().getString("event.import"));
        btnFinish.setText(BundleManager.getBundle().getString("event.finish"));
        setChoosenFile(null);
    }


    @FXML
    public void goBack(ActionEvent actionEvent) {
        c.getEventTab().setContent(oldContent);
    }

    public void chooseFile(ActionEvent actionEvent) {
        String home = System.getProperty("user.home");
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(home));
        FileChooser.ExtensionFilter f1 = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        fc.getExtensionFilters().addAll(f1);
        File file = fc.showOpenDialog(null);

        if (file == null) {
            LOGGER.warn("Tried to upload empty csv.");
            mainController.showGeneralError("File was empty. Choose a file before upload.");
            return;
        }

        setChoosenFile(file);
    }

    private void setChoosenFile(File file) {
        this.choosenFile = file;
        lblChoosenFile.setText(file.getPath());
    }

    public void importCSVFile(ActionEvent actionEvent) {
        mainController.showGeneralError("");
        if (choosenFile == null) {
            LOGGER.warn("Tried to upload empty csv.");
            mainController.showGeneralError("File was empty. Choose a file before upload.");
            return;
        }

        TAlogOutput.setText("");
        TAlogOutput.setDisable(true);
        btnAddFile.setDisable(true);
        btnImport.setDisable(true);
        Task<Void> workerTask = new Task<Void>() {
            @Override
            protected Void call() {
                String csvFile = choosenFile.getPath();
                String cvsSplitBy = ";";
                BufferedReader br = null;
                try {
                    String line = "";
                    br = new BufferedReader(new FileReader(csvFile));
                    int cnt = 1;
                    outer: while ((line = br.readLine()) != null) {
                        //0     1               2       3       4       5       6               7           8       9       10
                        //Titel	Beschreibung	Start	Ende	Price	Sektor	LocationName	HallName	Artist1	Artist2	...
                        String[] column = line.split(cvsSplitBy);

                        if (column.length < 8) {
                            TAlogOutput.setText(TAlogOutput.getText() + "Error: Column not complete < 8" + "\n");
                            continue;
                        }

                        for (int i = 0; i < column.length; i++) {
                            if (column[i].isEmpty()) {
                                TAlogOutput.setText(TAlogOutput.getText() + "Error: Column not complete empty (" + i + ")\n");
                                continue outer;
                            }
                        }

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                        String title = column[0];
                        String description = column[1];
                        LocalDateTime start = LocalDateTime.parse(column[2], formatter);
                        LocalDateTime end = LocalDateTime.parse(column[3], formatter);
                        Long price = Long.valueOf(column[4]);
                        boolean sector = column[5].toUpperCase().equals("TRUE") ? true : false;

                        DetailedLocationDTO location = DetailedLocationDTO.builder()
                            .id(0L)
                            .description(column[6])
                            .build();

                        DetailedHallDTO hall = DetailedHallDTO.builder()
                            .id(0L)
                            .description(column[7])
                            .location(location)
                            .build();

                        List<SimpleArtistDTO> artists = new ArrayList<>();

                        for (int i = 8; i < column.length; i++) {
                            String[] artistSplit = column[i].split(":");
                            String firstName = artistSplit[0];
                            String lastName = artistSplit[1];

                            SimpleArtistDTO artistDTO = SimpleArtistDTO.builder()
                                .id(0L)
                                .artistFirstname(firstName)
                                .artistLastName(lastName)
                                .build();

                            artists.add(artistDTO);
                        }

                        DetailedEventDTO detailedEventDTO = DetailedEventDTO.builder()
                            .title(title)
                            .description(description)
                            .startOfEvent(start)
                            .endOfEvent(end)
                            .price(price)
                            .seatSelection(sector)
                            .hall(hall)
                            .artists(artists)
                            .build();

                        try {
                            detailedEventDTO = eventService.publishEvent(detailedEventDTO);
                            TAlogOutput.setText(TAlogOutput.getText() + "OK: " + detailedEventDTO.getTitle() +"\n");
                        } catch (DataAccessException e) {
                            mainController.showGeneralError(e.toString());
                        } catch (ErrorDTO errorDTO) {
                            TAlogOutput.setText(TAlogOutput.getText() + "Error: " + title + "->" + errorDTO.getStatus() + ": " + errorDTO.getMessage() + "\n");
                        }
                    }
                // TODO: (David) Exception handling
                } catch (FileNotFoundException e) {
                    mainController.showGeneralError("File not found " + choosenFile.getPath());
                } catch (IOException e) {
                    mainController.showGeneralError("Error reading file " + choosenFile.getPath());
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            mainController.showGeneralError("Error reading file " + choosenFile.getPath());
                        }
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                TAlogOutput.setDisable(false);
                btnAddFile.setDisable(false);
                btnImport.setDisable(false);
            }

            @Override
            protected void failed() {
                super.failed();
                mainController.showGeneralError("Failure at PublishEvents: " + getException().getMessage());
                TAlogOutput.setDisable(false);
                btnAddFile.setDisable(false);
                btnImport.setDisable(false);
            }
        };

        workerTask.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(workerTask).start();

    }
}
