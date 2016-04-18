package smt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JOptionPane;
import matrix.Matrix;
import matrix.MatrixMathematics;
import matrix.NoSquareException;

public class SMT {

    public static void main(String[] args) throws Exception {
           //float a[][]=getAnalysis("riju");
           HomePage form=new HomePage();
           form.setVisible(true);
    }
   
     public static String Connect2() throws Exception{
 
  //Set URL
  
  URL url = new URL("https://in.news.yahoo.com/rss/markets");
  URLConnection spoof = url.openConnection();
 
  //Spoof the connection so we look like a web browser
  spoof.setRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)" );
  BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
  String strLine = "";
  StringBuilder n=new  StringBuilder();
  //Loop through every line in the source
  while ((strLine = in.readLine()) != null){
 
   n.append(strLine);
   
  }
  return n.toString();
 }
          public static String Connect3(String x) throws Exception{
 
  //Set URL
  URL url = new URL(x);
  URLConnection spoof = url.openConnection();
 
  //Spoof the connection so we look like a web browser
  spoof.setRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)" );
  BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
  String strLine = "";
  StringBuilder n=new  StringBuilder();
  //Loop through every line in the source
  while ((strLine = in.readLine()) != null){
 
   n.append(strLine);
   
  }
  return n.toString();
 }
    
          public static double currentRatingn(String x) throws Exception
        {
            String html=Connect3(x);
            //System.out.println(html);
            int s=0;
            s=html.indexOf("yfs_l10_^nsei");
            s=s+15;
            int l=html.indexOf("<",s);
            String num=html.substring(s,l);
            num=num.replaceAll(",", "");
            double ans=Double.parseDouble(num);
            return ans;
            
        }
       public static double currentRating(String x) throws Exception
        {
            String html=Connect3(x);
            //System.out.println(html);
            int s=0;
            s=html.indexOf("yfs_l10_^bsesn");
            s=s+16;
            int l=html.indexOf("<",s);
            String num=html.substring(s,l);
            num=num.replaceAll(",", "");
            double ans=Double.parseDouble(num);
            return ans;
            
        }
     public static double openRating(String x) throws Exception
        {
            String html=Connect3(x);

            //System.out.println(html);
            int s=0;
            s=html.indexOf("yfnc_tabledata1");
            s=s+1;
            s=html.indexOf("yfnc_tabledata1",s);
            s=s+17;
            int l=html.indexOf("<",s);
            String num=html.substring(s,l);
            //System.out.println(num);
            num=num.replaceAll(",", "");
            double ans=Double.parseDouble(num);
            
            return ans;
            
        }
     public static double openRatingn(String x) throws Exception
        {
            String html=Connect3(x);

            //System.out.println(html);
            int s=0;
            s=html.indexOf("yfnc_tabledata1");
            s=s+1;
            s=html.indexOf("yfnc_tabledata1",s);
            s=s+17;
            int l=html.indexOf("<",s);
            String num=html.substring(s,l);
            //System.out.println(num);
            num=num.replaceAll(",", "");
            double ans=Double.parseDouble(num);
            
            return ans;
            
        }
        
            public static double highRating(String x) throws Exception
        {
            String html=Connect3(x);
            //System.out.println(html);
            int s=0;
            s=html.indexOf("yfs_h00_^bsesn");
            s=s+16;
            int l=html.indexOf("<",s);
            String num=html.substring(s,l);
            num=num.replaceAll(",", "");
            double ans=Double.parseDouble(num);
            return ans;
            
        }
            
            
        public static double lowRating(String x) throws Exception
        {
            String html=Connect3(x);
            //System.out.println(html);
            int s=0;
            s=html.indexOf("yfs_g00_^bsesn");
            s=s+16;
            int l=html.indexOf("<",s);
            String num=html.substring(s,l);
            num=num.replaceAll(",", "");
            double ans=Double.parseDouble(num);
            return ans;
            
        }
      
    public static String printNews() throws Exception
     {
         String html=SMT.Connect2();
         int start=html.indexOf("<item>");
         int lastIndex = 0;
         int count = 0;

         while(lastIndex != -1){
            lastIndex = html.indexOf("<item>",lastIndex);
            if(lastIndex != -1){
            count ++;
            lastIndex += "<item>".length();
            }
         }
         String news="";
         for(int i=0;i<count;i++)
         {
             int fis=html.indexOf("<title>",start);
             int las=html.indexOf("<",fis+1);
             news=news+((i+1)+" :: "+(html.substring(fis+7,las)))+"\n";
             
                start=las;
         }
         return news;
     }
    
       public static String Connect(String symbol) throws Exception{
 
  //Set URL
  try{
  URL url = new URL("https://in.finance.yahoo.com/q?s="+symbol);
  URLConnection spoof = url.openConnection();
 
  //Spoof the connection so we look like a web browser
  spoof.setRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)" );
  BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
  String strLine = "";
  StringBuilder n=new  StringBuilder();
  //Loop through every line in the source
  while ((strLine = in.readLine()) != null){
 
   n.append(strLine);
   
  }
 
  return n.toString();
  }
  catch(Exception e)
  {
      return "i";
  }
 }

    // Given symbol, get current stock price.
    public static double priceOf(String symbol) throws Exception {
        String html = Connect(symbol);
        if(html.equals("i"))
            return -1;
        int p     = html.indexOf("yfs_l84", 0);      // "yfs_l84" index
        int from  = html.indexOf(">", p);            // ">" index
        int to    = html.indexOf("</span>", from);   // "</span>" index
        String price = html.substring(from + 1, to);
        return Double.parseDouble(price.replaceAll(",", ""));
    }

    // Given symbol, get current stock name.
    public static String nameOf(String symbol) throws Exception {
        String html = Connect(symbol);
        int p    = html.indexOf("<title>", 0);  
        int from = html.indexOf("Summary for ", p);
        int to   = html.indexOf("- Yahoo!", from);
        StringBuilder name = new StringBuilder(html.substring(from + 12, to));
        return name.toString();
    }

    // Given symbol, get current date.
    public static String dateOf(String symbol) throws Exception {
        String html = Connect(symbol);
        int p    = html.indexOf("<span id=\"yfs_market_time\">", 0);
        int from = html.indexOf(">", p);
        int to   = html.indexOf("-", from);        // no closing small tag
        String date = html.substring(from + 1, to);
        return date;
    }
    
    public static String dayRange(String symbol) throws Exception
    {
        String html=Connect(symbol);
        int p   = html.indexOf("Day's Range:");
        int to   = html.indexOf(".bo",p);
        int from = html.indexOf("<",to);
        String a = html.substring(to+5,from);
        to= html.indexOf(".bo",from);
        from = html.indexOf("<",to);
        String b=html.substring(to+5,from);
        return(a+"-"+b);
    }
    
    public static String getNews(String symbol) throws Exception{
        String html=Connect(symbol);
        if(html.equals("i"))
            return "i";
        //System.out.println("COMPANY NEWS :: ");
       try{
        int q=0;
        String news="";
        if(html.indexOf(";o:i;\">")!=-1){
        for(int i=0;i<8;i++)
        {
            int p =html.indexOf(";o:i;\">",q);
            int last =html.indexOf("</a>",p);
            news=news + (html.substring(p+6, last))+"\n";
            q=last;
        }
            return news;
        }
        else
            return ("NO");
       }
       catch(Exception e)
       {
           return "i";
       }
    }
     
    
    public static Connection getConnection() throws Exception
    {
        try{
            String driver="com.mysql.jdbc.Driver";
            String url="jdbc:mysql://localhost:3306/mydb?useSSL=false";
            String user="root";
            String pass="mysqlash038";
            Class.forName(driver);
            
            Connection conn=DriverManager.getConnection(url, user, pass);
            System.out.println("Connected");
            return conn;
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }
    
    public static int rateCompNews(String symbol) throws Exception
    {
        int net=0;
        int pos=0;
        int neg=0;
        String text=getNews(symbol);
        if(text.equals(""))
        {
            //System.out.println("nOTHING");
            return -99;
        }
        else
        {
            
            //text.replace("\n", "");
            //System.out.println(text);
            String[] splitArray = text.split("\\s+");
            Connection con=getConnection();
            for(int i=0;i<splitArray.length;i++)
            {
                //System.out.println(splitArray[i]);
                splitArray[i]=splitArray[i].replaceAll("'", "");
                splitArray[i]=splitArray[i].replaceAll(",", "");
                //System.out.println(splitArray[i]);
                String quer1="Select name,count(*) as cc from positive where name = '"+splitArray[i]+"';";
                com.mysql.jdbc.PreparedStatement ps=(com.mysql.jdbc.PreparedStatement) con.prepareStatement(quer1);
                String quer2="Select name,count(*) as cc from negatIve where name = '"+splitArray[i]+"';";
                com.mysql.jdbc.PreparedStatement ps2=(com.mysql.jdbc.PreparedStatement) con.prepareStatement(quer2);
                ResultSet rs=ps.executeQuery();
                ResultSet rs2=ps2.executeQuery();
                rs.next();
                rs2.next();
                int p=rs.getInt("cc");
               // System.out.println(p);
                int n=rs2.getInt("cc");
               // System.out.println(n);
                pos=pos+p;
                //System.out.println(pos);
                neg=neg+n;
                //System.out.println(neg);
            }
            //System.out.println(pos);
            //System.out.println(neg);
            net=pos-neg;
            return net;
        }
    }
    
    
     
      public static int getRating() throws Exception
       {
           int net=0;
        int pos=0;
        int neg=0;
        String text=printNews();
        if(text.equals(""))
        {
            //System.out.println("NOTHING");
            return -99;
        }
        else
        {
            
            //text.replace("\n", "");
            //System.out.println(text);
            String[] splitArray = text.split("\\s+");
            Connection con=getConnection();
            for(int i=0;i<splitArray.length;i++)
            {
                //System.out.println(splitArray[i]);
                splitArray[i]=splitArray[i].replaceAll("'", "");
                splitArray[i]=splitArray[i].replaceAll(",", "");
                //System.out.println(splitArray[i]);
                String quer1="Select name,count(*) as cc from positive where name = '"+splitArray[i]+"';";
                com.mysql.jdbc.PreparedStatement ps=(com.mysql.jdbc.PreparedStatement) con.prepareStatement(quer1);
                String quer2="Select name,count(*) as cc from negatIve where name = '"+splitArray[i]+"';";
                com.mysql.jdbc.PreparedStatement ps2=(com.mysql.jdbc.PreparedStatement) con.prepareStatement(quer2);
                ResultSet rs=ps.executeQuery();
                ResultSet rs2=ps2.executeQuery();
                rs.next();
                rs2.next();
                int p=rs.getInt("cc");
               // System.out.println(p);
                int n=rs2.getInt("cc");
               // System.out.println(n);
                pos=pos+p;
                //System.out.println(pos);
                neg=neg+n;
                //System.out.println(neg);
            }
            //System.out.println(pos);
            //System.out.println(neg);
            net=pos-neg;
            //System.out.println(net);
            
        
           }
        return net;
       }
      
      
      
    
}
