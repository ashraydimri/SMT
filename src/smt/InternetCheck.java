/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author MAHE
 */
public class InternetCheck {
     public static String check() throws Exception
    {
        try{
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
        catch(Exception ex)
        {
            return "i";
    }
    }
}
