package gui.controller;

import BE.Customer;
import BE.Event;
import BE.Ticket;
import BLL.Util.BarcodeGenerator;
import BLL.Util.QRCodeGenerator;
import gui.model.TicketModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.PDPage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
public class TicketController implements Initializable {
    // instance variable with @FXML
    @FXML
    private AnchorPane ticketAnchor;
    @FXML
    private Line ticketLine;
    @FXML
    private Label ticketEvent, ticketLocation, ticketDate, ticketTime, ticketParticipantName;
    @FXML
    private Button printButton;
    @FXML
    private ImageView imgQRCode, imgBarcode;

    // instance variables
    private TicketModel ticketModel;
    private Customer customer;

    public void setModel(TicketModel ticketModel) {
        this.ticketModel = ticketModel;
    }

    public void setDetails(Event event, Customer customer) {
        this.customer = customer;
        try {ticketModel.fetchAllTickets(customer, event);} catch (SQLException e) {throw new RuntimeException(e);}
        ticketInitialize();
    }

    private void ticketInitialize() {
        List<Ticket> tickets = ticketModel.getTickets();

        for (Ticket ticket : tickets) {
            if (ticket.getCustomer().getId() == customer.getId()) {
                ticketEvent.setText(ticket.getEvent().getName());
                ticketLocation.setText(ticket.getEvent().getLocation());
                ticketDate.setText(String.valueOf(ticket.getEvent().getDate()));

                double decimalNumber = ticket.getEvent().getTime();
                double roundedNumber = Math.round(decimalNumber * 100.0) / 100.0;
                String roundedString = String.format("%.2f", roundedNumber);
                ticketTime.setText(roundedString);

                ticketParticipantName.setText(ticket.getCustomer().getName());

                try {
                    Image qrCode = SwingFXUtils.toFXImage(QRCodeGenerator.generateQRCodeImage(ticket.getUuid().toString()), null);
                    imgQRCode.setImage(qrCode);

                    Image barCode = SwingFXUtils.toFXImage(BarcodeGenerator.generateBarcodeImage(ticket.getUuid().toString()), null);
                    imgBarcode.setImage(barCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void printTicket(ActionEvent actionEvent) throws IOException {
        printButton.setVisible(false);

        Scene scene = ticketAnchor.getScene();
        float aspectRatio = (float) scene.getWidth() / (float) scene.getHeight();
        float pdfWidth = 595;
        float pdfHeight = pdfWidth / aspectRatio;

        // Create a PDF document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(new PDRectangle(pdfWidth, pdfHeight));
        document.addPage(page);

        // Convert the scene to an image
        WritableImage fxImage = new WritableImage((int) scene.getWidth(), (int) scene.getHeight());
        scene.snapshot(fxImage);
        BufferedImage image = SwingFXUtils.fromFXImage(fxImage, null);

        // Convert the BufferedImage to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        // Add the image to the PDF document
        PDImageXObject xImage = PDImageXObject.createFromByteArray(document, imageBytes, "ticket");
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.drawImage(xImage, 0, 0, pdfWidth, pdfHeight);
        contentStream.close();

        // Save the image to a file at the specified path
        String event = ticketEvent.getText();
        String name = ticketParticipantName.getText();
        File outputFile = new File("tickets/Ticket" + event + "_" + name + ".pdf");
        document.save(outputFile);
        document.close();

        // Opens the PDF document
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(outputFile);
            }
        }
        Stage stage = (Stage) ticketAnchor.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ticketLine.setStrokeWidth(2);
        ticketLine.setStrokeType(StrokeType.CENTERED);
        ticketLine.setStrokeLineCap(StrokeLineCap.ROUND);
        ticketLine.setStrokeLineJoin(StrokeLineJoin.ROUND);
        ticketLine.getStrokeDashArray().addAll(15d, 10d);

    }
}
