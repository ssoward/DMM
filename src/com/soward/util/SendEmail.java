package com.soward.util;

import org.apache.commons.lang.StringUtils;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.ArrayList;

public class SendEmail {
    public SendEmail(){}
//    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
//    private static final int SMTP_HOST_PORT = 465;
//    private static final String SMTP_AUTH_USER = "daymurraymusic.orders@gmail.com";
//    private static final String SMTP_AUTH_PWD  = "AdamDay@123";

    private static final String SMTP_HOST_NAME = "mail.daymurraymusic.com";
    private static final int    SMTP_HOST_PORT = 25;
    private static final String SMTP_AUTH_USER = "dmm@daymurraymusic.com";
    private static final String SMTP_AUTH_PWD  = "day-1946";//"dmm1946";

    public static void main(String[] args) throws Exception{
        try{
            ArrayList<String> eList = new ArrayList<String>();
            eList.add("amorvivir@yahoo.com");
            eList.add("estancado@yahoo.com");
            eList.add("estancado@yssahoo.com");
            Calendar cal = Calendar.getInstance();
            boolean sent = new SendEmail().sendOrderEmail(
                    "Day Murray Music Order",
                    "testContext "+TransUtil.sdf.format(cal.getTime()),
                    "estancado@yahoo.com,amorvivir@yahoo.com,scottlarock19@hotmail.com",
                    //eList,
                    "scott.soward@gmail.com");//,scottlarock19@hotmail.com");
            System.out.println(sent);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static boolean sendOrderEmail(String subject, String content, String to, String ccList){
        return sendOrderEmail(subject, content, to, ccList, null);
    }
    public static boolean sendOrderEmail(String subject, String content, String to, String ccList, String bccList){
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        if(StringUtils.isBlank(to)&&
                StringUtils.isBlank(bccList)&&
                StringUtils.isBlank(ccList)
                ){
            return false;
        }
        try{
            Session mailSession = Session.getDefaultInstance(props);
            //mailSession.setDebug(true);
            Transport transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setHeader("From", "dmm@daymurraymusic.com");
            message.setHeader("Date", new Date().toString());
            message.setSubject(subject);
            message.setContent(content, "text/html");
            try{
                String[] bList = bccList.split(",");
                if(bccList!=null&&bccList.length()>5){
                    for(String cc: bList){
                        if(!StringUtils.isBlank(cc)){
                            System.out.println("BCC: "+cc);
                            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(cc));
                        }
                    }
                }
            }catch (Exception e){
               // e.printStackTrace();
            }
            try{
                String[] eList = to.split(",");
                if(to!=null&&to.length()>5){
                    for(String cc: eList){
                        if(!StringUtils.isBlank(cc)){
                            System.out.println("To: "+cc);
                            message.addRecipient(Message.RecipientType.TO, new InternetAddress(cc));
                        }
                    }
                }
            }catch (Exception e){
                //e.printStackTrace();
            }
            try{
                String[] cList = ccList.split(",");
                if(ccList!=null&&ccList.length()>5){
                    for(String cc: cList){
                        if(!StringUtils.isBlank(cc)){
                            System.out.println("CC "+cc);
                            message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
            transport.sendMessage(message, message.getAllRecipients());//			Message.RecipientType.TO));
            transport.close();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
