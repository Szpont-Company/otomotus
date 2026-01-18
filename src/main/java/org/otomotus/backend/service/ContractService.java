package org.otomotus.backend.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.otomotus.backend.entity.AuctionEntity;
import org.otomotus.backend.entity.UserEntity;
import org.otomotus.backend.exception.ResourceNotFoundException;
import org.otomotus.backend.repository.AuctionRepository;
import org.otomotus.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public byte[] generateCarSaleContract(UUID auctionId, String buyerUsername) {
        AuctionEntity auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        UserEntity seller = auction.getSeller();

        UserEntity buyer = userRepository.findByUsername(buyerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font titleFont = new Font(helvetica, 18, Font.BOLD);
            Font normalFont = new Font(helvetica, 11, Font.NORMAL);
            Font boldFont = new Font(helvetica, 11, Font.BOLD);

            Paragraph title = new Paragraph("UMOWA KUPNA - SPRZEDAŻY SAMOCHODU", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            Paragraph datePlace = new Paragraph("Miejscowość: " + auction.getLocation() + ", Data: " + LocalDate.now(), normalFont);
            datePlace.setAlignment(Element.ALIGN_RIGHT);
            datePlace.setSpacingAfter(20);
            document.add(datePlace);

            addSectionTitle(document, "§1. Strony umowy", boldFont);

            document.add(new Paragraph("Sprzedający:", boldFont));
            document.add(new Paragraph("Imię i nazwisko: " + seller.getFirstName() + " " + seller.getLastName(), normalFont));
            document.add(new Paragraph("PESEL: ____________________", normalFont)); // Brak w bazie
            document.add(new Paragraph("Adres: ___________________________________", normalFont)); // Brak w bazie
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Kupujący:", boldFont));
            document.add(new Paragraph("Imię i nazwisko: " + buyer.getFirstName() + " " + buyer.getLastName(), normalFont));
            document.add(new Paragraph("PESEL: ____________________", normalFont)); // Brak w bazie
            document.add(new Paragraph("Adres: ___________________________________", normalFont)); // Brak w bazie
            document.add(Chunk.NEWLINE);

            addSectionTitle(document, "§2. Przedmiot umowy", boldFont);
            document.add(new Paragraph("Sprzedający oświadcza, że jest właścicielem pojazdu marki:", normalFont));

            com.lowagie.text.List carDetails = new com.lowagie.text.List(com.lowagie.text.List.UNORDERED);
            carDetails.setSymbolIndent(12);
            carDetails.add(new ListItem("Marka: " + auction.getCar().getBrand(), normalFont));
            carDetails.add(new ListItem("Model: " + auction.getCar().getModel(), normalFont));
            carDetails.add(new ListItem("Rok produkcji: " + auction.getCar().getProductionYear(), normalFont));
            carDetails.add(new ListItem("VIN: " + auction.getCar().getVin(), normalFont));
            carDetails.add(new ListItem("Przebieg: " + auction.getCar().getMileage() + " km", normalFont));
            carDetails.add(new ListItem("Nr rejestracyjny: _________________", normalFont)); // Brak w bazie
            document.add(carDetails);
            document.add(Chunk.NEWLINE);

            addSectionTitle(document, "§3. Warunki sprzedaży", boldFont);
            String priceText = "Strony ustalają cenę sprzedaży na kwotę: " + auction.getPrice() + " PLN";
            document.add(new Paragraph(priceText, normalFont));
            document.add(new Paragraph("Słownie: _______________________________________________________", normalFont));
            document.add(Chunk.NEWLINE);

            addSectionTitle(document, "§4. Oświadczenia", boldFont);
            document.add(new Paragraph("1. Sprzedający oświadcza, że pojazd jest wolny od wad prawnych i obciążeń na rzecz osób trzecich.", normalFont));
            document.add(new Paragraph("2. Kupujący oświadcza, że stan techniczny pojazdu jest mu znany i nie wnosi do niego zastrzeżeń.", normalFont));
            document.add(Chunk.NEWLINE);

            Paragraph signatures = new Paragraph("\n\n\n__________________________                  __________________________\nSprzedający                                                  Kupujący", normalFont);
            signatures.setAlignment(Element.ALIGN_CENTER);
            document.add(signatures);

            document.close();
            return out.toByteArray();
        } catch (IOException | DocumentException e) {
            throw new RuntimeException("Błąd podczas generowania PDF", e);
        }
    }

    private void addSectionTitle(Document doc, String text, Font font) throws DocumentException {
        Paragraph p = new Paragraph(text, font);
        p.setSpacingAfter(5);
        doc.add(p);
    }
}