/**
 * Title:        Reports.java
 * Description:  Reports.java 
 * Copyright:    Copyright (c)  2007
 * Company:      
 * @author       Scott Soward     
 */
package com.soward.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import com.soward.reports.ReportCollection;

public class ReportsUtil extends HttpServlet {
    public static final int OUTPUT_HTML = 1;
    public static final int OUTPUT_PDF  = 2;
    public static final int OUTPUT_CSV  = 3;
    public static final int OUTPUT_XLS  = 4;
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            String Uid = (String) session.getAttribute( "Uid" );
            if ( Uid == null ) {
                request.getSession().invalidate();
                if ( session != null ) {
                    session = null;
                }
                //response.sendRedirect( "home.jsp?message=Please Login" );
            } else {
                doPost( request, response );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        HttpSession session = request.getSession();
        try {
            /* ***************************** */
            Map reportParameters = null;

            Map servletParameters = new HashMap();
            Enumeration paramNames = request.getParameterNames();
            while(paramNames.hasMoreElements()) {
                String paramName = (String)paramNames.nextElement();
                //System.out.println("ParamName: "+paramName);
                String paramValue = request.getParameter(paramName);
                servletParameters.put(paramName, paramValue);
            }

            String reportCollection = request.getParameter( "reportCollection" );
            if (reportCollection==null){
                System.out.println("Missing report class name.");
                return;
            }
            Class collectorClass = Class.forName("com.soward.reports." +
                    reportCollection);
            ReportCollection dataCollector = (ReportCollection)collectorClass.newInstance();
            Collection reportData = null;
            try {
                //params for report generation
                String outputType = request.getParameter("outputType");
                outputType = outputType==null?"PDF":outputType;
                OutputStream outputStream = response.getOutputStream();
                String currDir = getServletContext().getRealPath("/jasper");//System.getProperty( "user.dir" );
                //currDir = "/Applications/cat/webapps/DMM/jasper/";
                if(StringUtils.isBlank( currDir )){
                    currDir = "/srv/webapps/jasper";
                }
                Object jasperReport = currDir+"/" + request.getParameter( "reportCollection" )+".jasper";
                System.out.println("jasperFileLocation: "+jasperReport);

                reportParameters = new HashMap(servletParameters);
                reportParameters.put( "outputType", outputType );
                reportParameters.put( "fromDate", new Date() );
                reportParameters.put( "toDate", new Date() );
                reportParameters.put( "session", session );
                boolean cleanHTML = false;

                if(outputType.equalsIgnoreCase("PDF")) {
                    response.setContentType("application/pdf");
                }   else if(outputType.equalsIgnoreCase("CSV") || outputType.equalsIgnoreCase("XLS"))   {
                    response.setContentType("text/csv");
                }   else {
                    response.setContentType("text/plain");
                }
                reportParameters.put("outputStream", response.getOutputStream());
                response.setHeader("Cache-Control", "");
                response.setHeader("Pragma", "");
                reportData = dataCollector.getData(reportParameters);
                Collection genErrors = ReportsUtil.exportToOutputStream(outputType,outputStream,
                        jasperReport,new HashMap(reportParameters),reportData,cleanHTML);
                System.out.println("DataCollector: "+request.getParameter( "reportCollection" )+"."+outputType+"run by: "+session.getAttribute( "username" )+" finished at " + new Date());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            /* ***************************** */
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public static Collection exportToOutputStream(String outputType, OutputStream outputStream,
            Object jasperReport, Map parameters,
            Object reportData, boolean cleanHTML)
    {
        int type = 0;
        if (outputType.equalsIgnoreCase("HTML")) {
            type = OUTPUT_HTML;
        } else if (outputType.equalsIgnoreCase("PDF")) {
            type = OUTPUT_PDF;
        } else if (outputType.equalsIgnoreCase("CSV")) {
            type = OUTPUT_CSV;
        } else if (outputType.equalsIgnoreCase("XLS")) {
            type = OUTPUT_XLS;
        }

        JasperReport report = null;
        if (jasperReport instanceof JasperReport) {
            report = (JasperReport)jasperReport;
        } else {
            try {
                report = JasperManager.loadReport(jasperReport.toString());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        JRDataSource dataSource = null;
        if (reportData instanceof JRDataSource) {
            dataSource = (JRDataSource)reportData;
        } else {
            dataSource = new JRMapCollectionDataSource((Collection)reportData);
        }

        return exportToOutputStream(type, outputStream, report,
                parameters, dataSource, cleanHTML);
    }

    public static void main( String args[] ) {

    }
    public static Collection exportToOutputStream(int outputType, OutputStream outputStream,
            JasperReport jasperReport, Map parameters,
            JRDataSource dataSource, boolean cleanHTML)
    {
        try {
            JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            HttpServletResponse response = (HttpServletResponse)parameters.get("response");
            HttpServletRequest request = (HttpServletRequest)parameters.get("request");

            switch (outputType) {

                case OUTPUT_HTML:
                    JRHtmlExporter htmlExporter = new JRHtmlExporter();

                    if (response != null) {
                        response.setContentType("text/html");
                    }

                    Map imagesMap = new HashMap();

                    if (request != null) {
                        request.getSession().setAttribute("IMAGES_MAP", imagesMap);
                    }

                    htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    StringBuffer stringBuffer = new StringBuffer();
                    if (cleanHTML) {
                        htmlExporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,
                                stringBuffer);
                    } else {
                        htmlExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
                    }

                    htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
                    htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "report?image=");

                    htmlExporter.exportReport();

                    if (cleanHTML) {
                        outputStream.write(cleanUpHTML(stringBuffer));
                    }

                    break;

                case OUTPUT_PDF:
                    byte[] array = JasperManager.printReportToPdf(jasperPrint);
                    if (array != null) {
                        if (response != null) {
                            response.setContentType("application/pdf");
                            response.setContentLength(array.length);
                        }
                        outputStream.write(array);
                        if (response != null) {
                            response.flushBuffer();
                        }
                    }
                    break;

                case OUTPUT_CSV:
                    JRCsvExporter csvExporter = new JRCsvExporter();

                    if (response != null) {
                        response.setContentType("text/comma-separated-values");
                    }

                    csvExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    csvExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);

                    csvExporter.exportReport();

                    break;

                case OUTPUT_XLS:
                    JRXlsExporter xlsExporter = new JRXlsExporter();

                    if (response != null) {
                        response.setContentType("application/x-ms-excel");
                    }

                    xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
                    xlsExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
                            Boolean.FALSE);

                    xlsExporter.exportReport();
                    break;

                default:
                    throw new Exception("Output type '" + outputType + "' not supported");
            }
        } catch (Throwable throwable) {
            System.out.println("Error exporting to JasperReports");
            throwable.printStackTrace();
            Collection errors = new Vector();
            errors.add(throwable);
            return errors;
        }
        return null;
    }
    private static byte[] cleanUpHTML(StringBuffer stringBuffer) {
        StringTokenizer stringTokenizer = new StringTokenizer(stringBuffer.toString(), "\n");
        int count = stringTokenizer.countTokens();
        String[] text = new String[count];
        for (int i = 0; i < count; i++) {
            text[i] = stringTokenizer.nextToken();
        }
        return cleanUpHTML(text).getBytes();
    }
    private static String cleanUpHTML(String[] text) {
        long startTime = System.currentTimeMillis();

        Vector vector = new Vector();
        try {
            int start, end;
            for (int i = 0; i < text.length; i++) {
                start = text[i].indexOf("rowspan");
                // Remove all "rowspan" attributes
                if (start != -1) {
                    end = text[i].indexOf(' ', start);
                    start--;
                    text[i] = text[i].substring(0, start) + text[i].substring(end);
                    // Remove table rows with only filler images unless there are no colspans
                } else if (text[i].equalsIgnoreCase("</tr>")) {
                    int last = vector.size() - 1;
                    boolean erase = true;
                    boolean hasColspans = false;
                    while (!((String)vector.get(last)).startsWith("<tr")) {
                        String checkLine = (String)vector.get(last);
                        // if this table row has at least one column without a filler
                        // image, don't delete it
                        if (checkLine.indexOf("image=px") == -1) {
                            erase = false;
                            break;
                        }
                        // check to see if we have colspans
                        if (checkLine.indexOf("colspan") != -1) {
                            hasColspans = true;
                        }
                        last--;
                    }
                    if (erase && hasColspans) {
                        while (true) {
                            try {
                                vector.remove(last);
                            } catch (Throwable throwable) {
                                break;
                            }
                        }
                        continue;
                    }
                }
                vector.add(text[i] + "\n");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        StringBuffer stringBuffer = new StringBuffer();
        Iterator iterator = vector.iterator();
        while (iterator.hasNext()) {
            stringBuffer.append(iterator.next());
        }
        return stringBuffer.toString();
    }

}
