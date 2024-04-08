package BLL.Util;

// imports

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import java.awt.image.BufferedImage;

/**
 *
 * @author tomdra01, mrtng1
 */
public class BarcodeGenerator {

    public static BufferedImage generateBarcodeImage(String barcodeText) throws WriterException {
        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, 400, 100);
        bitMatrix.rotate(90);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
