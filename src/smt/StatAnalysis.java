/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static smt.SMT.getConnection;

/**
 *
 * @author MAHE
 */
public class StatAnalysis {
    
    public static float[][] getAnalysis(String name) throws Exception
    {
        System.out.println(name); 
        
        try{
            
            
           
            
            
            Connection con=getConnection();
            con.setAutoCommit(false);
            
            String sqlquery="select count(*) as cc from portfolio where username = '"+name+"';";
            String sqlq= "select * from portfolio where username = '"+name+"';";
            PreparedStatement ps3= con.prepareStatement(sqlquery);
            ResultSet rs= ps3.executeQuery();
            rs.next();
            int count=rs.getInt("cc");
            System.out.println(count);
            float ret[][]=new float[count][10];
            int i=0;
            int j=0;
            ps3=con.prepareStatement(sqlq);
            rs=ps3.executeQuery();
            
            
            while(rs.next()){
          
            j=0;
            float nextdayincrease=0;
            float nextdaydecrease=0;
            float nextdaynochange=0;
            float total=0;
            
            float sumofdecreases=0;
            float sumofincreases=0;
            
            String tick=rs.getString("ports");
            tick=tick.replace(".","");
            
           
            String sql="SELECT date,percentchange FROM "+tick+" Where percentchange < '0' ORDER BY date ASC;";
            
            PreparedStatement ps= con.prepareStatement(sql);
            ResultSet res=ps.executeQuery();
            if (!res.isBeforeFirst() ) {    
                System.out.println("No data"); 
            }
            else
                System.out.println("Data present");
                while(res.next())
                {
                    
                    int fix=1;
                    String da=res.getString("date");
                    
                    String newquer="SELECT date,percentchange,amtchange,count(*) as  cc FROM "+tick+" WHERE date > '"+da+"' ORDER BY date ASC LIMIT "+fix;
                    PreparedStatement ps2= con.prepareStatement(newquer);
                    int rows=0;
                    float perch=0;
                    float amtch=0;
                    ResultSet res2=ps2.executeQuery(newquer);
                    while(res2.next()){
                        String dat=res2.getString("date");
                        perch=res2.getFloat("percentchange");
                        rows=res2.getInt("cc");
                        amtch=res2.getFloat("amtchange");
                      
                    }
                    
                    if (rows>=1)
                    {
                        if(perch>0)
                        {
                            nextdayincrease++;
                            total++;
                            sumofincreases+=amtch;
                        }
                        else if(perch<0)
                        {
                            nextdaydecrease++;
                            total++;
                            sumofdecreases+=amtch;
                        }
                        else
                        {
                            nextdaynochange++;
                            total++;
                        }
                    }
                    
                   
                    
                    
                }
                float up=nextdayincrease/total;
                float down=nextdaydecrease/total;
                float same=nextdaynochange/total;
                float net=sumofincreases+sumofdecreases;
                
                ret[i][j++]=nextdayincrease;
                ret[i][j++]=nextdaydecrease;
                ret[i][j++]=nextdaynochange;
                ret[i][j++]=total;
                ret[i][j++]=up;
                ret[i][j++]=down;
                ret[i][j++]=same;
                ret[i][j++]=sumofincreases;
                ret[i][j++]=sumofdecreases;
                ret[i][j++]=net;
                
                i++;
                        
               System.out.println(tick+"    "+nextdayincrease+"    "+nextdaydecrease+"    "+nextdaynochange+"    "+total+"    "+up+"      "+down+"      "+"     "+same+"      "+sumofincreases+"      "+sumofdecreases+"      "+net);
            }
            return ret;
        }
        catch(Exception e)
        {
            System.out.print(e);
        }
        float a[][]={{0}};
        return a;
    }
}
