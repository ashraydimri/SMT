
package smt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TickerTesting {
    
    public static int tickTest(String name) throws Exception
    {   
        
        Connection con=SMT.getConnection();
        String na=name.replace(".BO", "");
        System.out.println(na);
        PreparedStatement ps=con.prepareStatement("SELECT count(*) as cc from complist where tick = '"+na+"';");
        ResultSet rs= ps.executeQuery();
        rs.next();
        int count =rs.getInt("cc");
        return count;
    }
    
    
}
