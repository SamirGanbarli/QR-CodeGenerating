package com.company.Qr2;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Controller
public class MainController {

    @Autowired
    public ProductRepository proRepo;

    @GetMapping("/")
    public String getStart(Model model){

        Product p1 = new Product();

        model.addAttribute(p1);

        return "first";
    }

    @PostMapping("/addpro")
    public String addAndGenerate(Product product) throws IOException, WriterException {

        Product p2 = new Product();
        p2.setId(product.getId());
        p2.setName(product.getName());

        proRepo.save(p2);
        createQrCode(p2.getName(),350,350);


        return "second";
    }

    public void createQrCode(String name, int width, int height) throws IOException, WriterException {
        generator.generateQRCodeImage(name, width,height,
                "./src/main/resources/QRCode.png");
    }

}

class generator{

    //this function creates the qr code itself and save it to the directory
    public static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }

    // this function is for showing the qr code using browser
    public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }
}
