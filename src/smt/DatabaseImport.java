/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import static smt.SMT.getConnection;

/**
 *
 * @author MAHE
 */
public class DatabaseImport {
          public static int getStockName(String ll) throws Exception
    {
        GregorianCalendar yesterday = new GregorianCalendar();
        yesterday.add(Calendar.DATE, -1);
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdfr.format(yesterday.getTime());
   
        int fir=dateString.indexOf("/");
        int l=dateString.lastIndexOf("/");
        String month=dateString.substring(fir+1,l);
        String day=dateString.substring(0,2);
        String year=dateString.substring(6,10);
        int mon=Integer.parseInt(month);
        mon=mon-1;
        int da=Integer.parseInt(day);
        
        try{
          String a=getContents("http://real-chart.finance.yahoo.com/table.csv?s="+ll+"&d="+mon+"&e="+da+"&f="+year+"&g=d&a=0&b=3&c=2000&ignore=.csv");  
          File f= new File(ll+".csv");
          a=a.replace("Date,Open,High,Low,Close,Volume,Adj Close", "");
          a=a.trim();
          PrintWriter out = new PrintWriter(ll+".txt");
          out.println(a);
          out.close();
          return 1;
        }
        catch(Exception ex)
        {
            return 0;
        }

    }
          public static int updateTable(String ll) throws Exception
          {
              System.out.println(ll);
              
              int l= getStockName(ll);
              if (l==0)
                      return 0;
              String compname=ll.replace(".", "");
              System.out.println(compname);
              createTable(compname);
              fileToDatabase(ll,compname);
              return 1;
          }
    
    public static String getContents(String url) {
        URL u;
        StringBuilder builder = new StringBuilder();
        try {
            u = new URL(url);
            try {
                BufferedReader theHTML = new BufferedReader(new InputStreamReader(u.openStream()));
                String thisLine;
                while ((thisLine = theHTML.readLine()) != null) {
                    builder.append(thisLine).append("\n");
                } 
            } 
            catch (Exception e) {
                System.err.println(e);
            }
        } catch (MalformedURLException e) {
            System.err.println(url + " is not a parseable URL");
            System.err.println(e);
        }
        return builder.toString();
    }
    
    public static void createTable(String sname) throws Exception
    {
        try
        {
            Connection con=getConnection();
            PreparedStatement ps=con.prepareStatement("DROP TABLE IF EXISTS "+sname+";");
            ps.executeUpdate();
            ps=con.prepareStatement("CREATE TABLE "+sname+"(date varchar(10),open float,high float,low float,close float,volume int,adjclose float,amtchange float,percentchange float,primary key(date));");
            ps.executeUpdate();
            ps.close();
        }catch(Exception e)
        {
            System.out.println(e);
        }
        finally{
            System.out.println("Function completed.");
            
        }
    }
     public static void fileToDatabase(String ll,String sname) throws Exception
    {
        try{
        Connection con=getConnection();
        File f=new File(ll+".txt");
        FileReader fr=new FileReader(f);
        BufferedReader bfr =new BufferedReader(fr);
        String newl;
        con.setAutoCommit(false);
        PreparedStatement ps=con.prepareStatement("INSERT INTO "+sname+" VALUES (?,?,?,?,?,?,?,?,?)");
        int i=0;
        while((newl=bfr.readLine())!=null)
        {
            newl.trim();
            String entries[]=newl.split(",");
            
            String date=entries[0];
            
            float open=Float.parseFloat(entries[1]);
            float high=Float.parseFloat(entries[2]);
            float low=Float.parseFloat(entries[3]);
            float close=Float.parseFloat(entries[4]);
            int volume=Integer.parseInt(entries[5]);
            float adjclose=Float.parseFloat(entries[6]);
            float amt_change=close-open;
            float percent_change=(amt_change/open)*100;
             
           
            
            ps.setString(1, date);
            ps.setFloat(2, open);
            ps.setFloat(3, high);
            ps.setFloat(4, low);
            ps.setFloat(5, close);
            ps.setInt(6, volume);
            ps.setFloat(7, adjclose);
            ps.setFloat(8, amt_change);
            ps.setFloat(9, percent_change);
            ps.executeUpdate();
            i++;
            if(i%1000==0)
                con.commit();
        }
        con.commit();
        con.setAutoCommit(true);
        bfr.close();
        fr.close();
        ps.close();
        }
        catch (Exception e)
        {
                    System.out.println(e);
        }
    }
}
