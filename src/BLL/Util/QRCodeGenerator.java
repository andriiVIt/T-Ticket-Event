package BLL.Util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;

public class QRCodeGenerator {

    public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 120, 120);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
