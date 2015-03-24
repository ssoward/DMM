
package com.soward.servlet;



import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.soward.object.Barcode;
import com.soward.object.Barcode128;
import com.soward.object.Product;
import com.soward.util.BarcodeGenerator;
import com.soward.util.ProductUtils;
import com.soward.util.Utils;



public class BarcodeServlet extends HttpServlet {

    // Default barcode heigth in pixels

    private static final int DEFAULT_BARCODE_HEIGTH = 50;


    protected void doGet(HttpServletRequest request, HttpServletResponse res) {

        try {
            String prodNum = request.getParameter( "prodNum" );
            String printer = request.getParameter( "printer" );
            String printCount = request.getParameter( "printCount" );
            String location = request.getParameter( "location" );

            printer = printer!=null?printer:"receivingbc";
            printCount = printCount!=null?printCount:"1";
            res.setContentType("image/jpeg");


            Product prod = ProductUtils.fetchProductForNum( prodNum, location ); 

            byte[] imageData = null;
            Barcode barCode = null;
            imageData = BarcodeGenerator.printBarCode(prod, printCount, printer); 

            res.setContentLength(imageData.length);
            try {
                OutputStream os = res.getOutputStream();
                os.write(imageData);
                os.flush();
                os.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    /**

     * 

     * @param rew

     *            javax.servlet.http.HttpServletRequest

     * @param res

     *            javax.servlet.http.HttpServletResponse

     */

    protected void doPost(HttpServletRequest req, HttpServletResponse res) {

        doGet(req, res);

    }

}

