package com.soward.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.ColorSupported;
import javax.print.attribute.standard.PrinterName;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;

import com.soward.db.DB;

public class Utils {

    /**
     * @param
     */

    public static String getP(HttpServletRequest request, String param ){
        return StringUtils.trimToEmpty( request.getParameter( param ) );
    }

    public static String p(String s){
        return parseStringDateTo_MMDASHddDASHyyyy_withTimeInAMPM(s);
    }

    /**
     * String toFormat   = "MM-dd-yyyy";
     * @param d
     * @return
     */
    public static String d(Date d){
        return dateFormat(d);
    }

    public static String parseStringDateTo_MMDASHddDASHyyyy_withTimeInAMPM(String oldDate){
        Date date=null;
        String defaultDate = "1999-01-01 12:00";
        if(oldDate==null||oldDate.length()<1){
            return "";
        }
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            date=df.parse(oldDate);
        }
        catch (Exception err) {
            //unparsable date
            try {
                date=df.parse(defaultDate);
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }
        return dateFormat(date);
    }

    private static String dateFormat(Date date){
        String newDate = "";
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");// h:mm a");
        try{
            newDate = df.format(date);
        }catch(Exception e){
            //null value
        }
        return newDate;
    }

    //check to see if date range is X wide
    public static boolean validateDateRange( String startDate, String endDate, int numberOfDays, String simpleDateFormat ) {
        boolean goodDates = false;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat);
            Date stDte = sdf.parse( startDate );
            Date edDte = sdf.parse( endDate );
            Calendar start = Calendar.getInstance();
            Calendar end   = Calendar.getInstance();
            start.setTime( stDte );
            end  .setTime( edDte );
            start.add( Calendar.DAY_OF_YEAR, numberOfDays );
            int compare = start.compareTo( end );
            goodDates = compare<0?false:true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return goodDates;
    }
    //check to see if date range is X wide
    public static boolean validateDate( String startDate, String simpleDateFormat ) {
        boolean goodDates = false;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat);
            sdf.setLenient( false );
            Date dtee = sdf.parse( startDate );
            return true;
        }catch(Exception e){
            //bad date
        }
        return goodDates;
    }

    public static void printImage(String fileName, String printCount, String printer) {
        try {

            //          Look up all services
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

            // Look up the default print service
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();

            // Find all services that can support a particular
            // input format; in this case, a GIF
            DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;
            services = PrintServiceLookup.lookupPrintServices(flavor, null);

            // Find a particular service by name;
            // in this case "HP LaserJet 6MP PS"
            AttributeSet aset = new HashAttributeSet();
            //"receivingbc"
            //"regsouthbc"
            aset.add(new PrinterName(printer, null));
            services = PrintServiceLookup.lookupPrintServices(null, aset);

            // Find all services that support a set of print job capabilities;
            // in this case, color
            //          aset = new HashAttributeSet();
            //          aset.add(ColorSupported.SUPPORTED);
            //          services = PrintServiceLookup.lookupPrintServices(null, aset);

            // Open the image file
            InputStream is = new BufferedInputStream(
                    new FileInputStream(fileName));

            // Find the default service
            //PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            // Create the print job
            DocPrintJob job = service.createPrintJob();
            Doc doc = new SimpleDoc(is, flavor, null);
            // Print it
            for(int i = 0; i<Integer.parseInt(printCount); i++){
                PrintJobWatcher pjDone = new PrintJobWatcher(job);
                job.print(doc, null);

                // Wait for the print job to be done
                pjDone.waitForDone();
            }
            // It is now safe to close the input stream
            is.close();
        } catch (PrintException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int toInt(String str) {
        if(!StringUtils.isBlank(str)){
            if(StringUtils.isNumeric(str.replace( "-", "" ).replace( ",", "" ))){
                return Integer.parseInt(str);
            }
        }
        return 0;
    }
    public static String breakString(String str){
        String pnm = str;
        String prdNm = "";
        while(pnm.length()>10){
            prdNm += pnm.substring(0,10)+"<br/>";
            pnm = pnm.substring(10, pnm.length());
        }
        prdNm += pnm;
        return prdNm;
    }
    public static String pS(String str){
        return StringUtils.isBlank(str)?"":str;
    }
    public static String formatDt(String date, String fromFormat, String toFormat){
        if(StringUtils.isBlank(date)){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
        SimpleDateFormat sdft = new SimpleDateFormat(toFormat);
        try{
            return sdft.format(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public static double parseDouble( String number ) {
        if ( StringUtils.isBlank( number ))
            return 0;
        return Double.valueOf( number.replaceAll( ",", "" ) );
    }
    public static int parseInt( String number ) {
        if ( StringUtils.isBlank( number ))
            return 0;
        return Integer.valueOf( number.replaceAll( ",", "" ) );
    }
    public static HashMap<String,String> getStates(){
        HashMap<String, String> al = new HashMap<String, String>();

        al.put("AL","Alabama");
        al.put("AK","Alaska");
        al.put("AZ","Arizona");
        al.put("AR","Arkansas");
        al.put("CA","California");
        al.put("CO","Colorado");
        al.put("CT","Connecticut");
        al.put("DE","Delaware");
        al.put("DC","District Of Columbia");
        al.put("FL","Florida");
        al.put("GA","Georgia");
        al.put("HI","Hawaii");
        al.put("ID","Idaho");
        al.put("IL","Illinois");
        al.put("IN","Indiana");
        al.put("IA","Iowa");
        al.put("KS","Kansas");
        al.put("KY","Kentucky");
        al.put("LA","Louisiana");
        al.put("ME","Maine");
        al.put("MD","Maryland");
        al.put("MA","Massachusetts");
        al.put("MI","Michigan");
        al.put("MN","Minnesota");
        al.put("MS","Mississippi");
        al.put("MO","Missouri");
        al.put("MT","Montana");
        al.put("NE","Nebraska");
        al.put("NV","Nevada");
        al.put("NH","New Hampshire");
        al.put("NJ","New Jersey");
        al.put("NM","New Mexico");
        al.put("NY","New York");
        al.put("NC","North Carolina");
        al.put("ND","North Dakota");
        al.put("OH","Ohio");
        al.put("OK","Oklahoma");
        al.put("OR","Oregon");
        al.put("PA","Pennsylvania");
        al.put("RI","Rhode Island");
        al.put("SC","South Carolina");
        al.put("SD","South Dakota");
        al.put("TN","Tennessee");
        al.put("TX","Texas");
        al.put("UT","Utah");
        al.put("VT","Vermont");
        al.put("VA","Virginia");
        al.put("WA","Washington");
        al.put("WV","West Virginia");
        al.put("WI","Wisconsin");
        al.put("WY","Wyoming");
        return al;
    }

    public static long getDaysDiff(Calendar cal1, Calendar cal2){// main(String[] args)
        Calendar date = (Calendar) cal1.clone();
        long daysBetween = 0;
        while (date.before(cal2)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }
//
//        // Get the represented date in milliseconds
//        long milis1 = cal1.getTimeInMillis();
//        long milis2 = cal2.getTimeInMillis();
//
//        // Calculate difference in milliseconds
//        long diff = milis2 - milis1;
//
//        // Calculate difference in seconds
//        long diffSeconds = diff / 1000;
//
//        // Calculate difference in minutes
//        long diffMinutes = diff / (60 * 1000);
//
//        // Calculate difference in hours
//        long diffHours = diff / (60 * 60 * 1000);
//
//        // Calculate difference in days
//        long diffDays = diff / (24 * 60 * 60 * 1000);
//
//        return diffDays;

    public static String fD(Object input) {
        Double d = null;
        try{
            if(input instanceof String){
                d = Double.parseDouble((String)input);
            }else{
                d = (Double)input;
            }
            return new java.text.DecimalFormat( "$0.00" ).format(d);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main (String args[]){
//        Calendar cal1 = Calendar.getInstance();
//        Calendar cal2 = Calendar.getInstance();
//        cal2.add(Calendar.YEAR, 1);
//        long l = Utils.getDaysDiff(cal1, cal2);
//        System.out.println(l);
//        System.out.println(Utils.fD("7.95000"));
//        double dblTest = 10.100000000000014;
//        System.out.println(Utils.fP(dblTest));
        System.out.println(Utils.roundUp(5.3550));

    }

    /**
     * String toFormat   = "MM-dd-yyyy hh:mm:ss";
     * @param date
     * @return
     */
    public static String dp(String date){
        try{
            SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dp(df.parse(date));
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public static String dp(Date date){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            return sdf.format(date);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static double roundUp(double v) {
        return v;//+0.001;
    }

    public static long fP(double discount) {
        return Math.round( discount );
    }

    public static double roundDown(double v) {
        return v;//-0.003;
    }
}
