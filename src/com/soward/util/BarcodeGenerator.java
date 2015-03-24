
package com.soward.util;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;

import com.soward.object.Barcode;
import com.soward.object.Barcode128;
import com.soward.object.Product;


public class BarcodeGenerator {

    public BarcodeGenerator() {
        super();
    }

    public static void main(String args[]) {
        Product prod  = ProductUtils.fetchProductForNum( "112540", "MURRAY" ); 
        BarcodeGenerator.printBarCode(prod, "1", "");
    }
    public static byte[] printBarCode(Product prod, String printCount, String printer){

        String fileName = prod.getProductSKU()+".jpg";
        String code = prod.getProductSKU();
        int sizeY = 90;
        int sizeX = 500;
        String fontName = "helvetica";
        String fontSize = "11";
        int fontStyle = Font.BOLD;
        boolean transparent = true;
        String rotation = "0";
        String barColor = "10";
        String textColor = "50";
        boolean showText = true;
        String imageFormat = Barcode.JPG;//Barcode.PNG;
        String quietZone = null;

        // Create Barcode
        Barcode barcode = null;
        try{
            barcode = new Barcode128();
        }catch(Exception e){
            e.printStackTrace();
        }
        barcode.setTextAlignment(Barcode.ALIGN_CENTER);
        barcode.setShowText(showText);
        if (quietZone != null){
            barcode.setQuietZone(true);
            barcode.setQuietZoneX(Integer.parseInt(quietZone));
        }
        barcode.setCode(code);
        barcode.setTransparent(transparent);
        barcode.setFontName(fontName);
        barcode.setFontSize(Integer.parseInt(fontSize));
        barcode.setFontStyle(fontStyle);

        Color barColorColor;
        if (barColor != null) {
            barColorColor = new Color(Integer.decode(barColor).intValue());
        } else {
            barColorColor = Color.black;
        }
        Color textColorColor;
        if (textColor != null) {
            textColorColor = new Color(Integer.decode(textColor).intValue());
        } else {
            textColorColor = Color.black;
        }

        double angle = 0;
        byte[] file=null;
        try {
            file = barcode.createJPG(sizeY,barColorColor, textColorColor, angle, prod);
            File tempFile = new File(fileName);
            FileOutputStream os = new FileOutputStream(tempFile);
            os.write(file);
            os.flush();
            os.close();
            Utils.printImage( tempFile.getCanonicalPath(), printCount, printer );
            //Utils.printImage( "/home/ssoward/tools/cat/webapps/DMM/"+tempFile.getCanonicalPath(), printCount, printer );
        } catch (Exception e) {
            System.out.println("Error:" + e);
            e.printStackTrace();
        }
        return file;
    }   
}

