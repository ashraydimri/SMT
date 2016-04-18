
package smt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import matrix.Matrix;
import matrix.MatrixMathematics;
import matrix.NoSquareException;
import static smt.DatabaseImport.*;
import static smt.SMT.*;
import static smt.SMT.getConnection;
import static smt.SMT.*;
import static smt.SMT.getRating;
import static smt.SMT.highRating;
import static smt.SMT.lowRating;
import static smt.SMT.openRating;

public class Predictor {
    
    public static double predictor(String html) throws Exception
        {
            getStockTablePredict();
            createTablePredict();
            fileToDatabasePredict();
            int newsr=getRating();
            if (newsr==-99)
                    return -99;
            double news=(double)newsr;
            double openr=openRating(html);
            double highr=highRating(html);
            double lowr=lowRating(html);
            double arr[]=createMatrix();
            
            double ans=0;
            //System.out.println(arr[1]);
            ans=arr[0]+(arr[1]*openr)+(arr[2]*highr)+(arr[3]*lowr)+(arr[4]*news);
            return ans;
        }
    public static void fileToDatabasePredict() throws Exception
    {
        try{
        Connection con=getConnection();
        File f=new File("prediction.txt");
        FileReader fr=new FileReader(f);
        BufferedReader bfr =new BufferedReader(fr);
        String newl;
        con.setAutoCommit(false);
        PreparedStatement ps=con.prepareStatement("INSERT INTO BSEPREDICT VALUES (?,?,?,?,?)");
        PreparedStatement ps2=con.prepareStatement("INSERT INTO CLOSEPREDICT VALUES (?,?)");
        int i=0;
        while((newl=bfr.readLine())!=null)
        {
            newl.trim();
            String entries[]=newl.split(",");
            Random rn = new Random();
            int an=rn.nextInt(8 - (-3) + 1) + (-3);
            double ans=(double)an;
            String date=entries[0];
            
            float open=Float.parseFloat(entries[1]);
            float high=Float.parseFloat(entries[2]);
            float low=Float.parseFloat(entries[3]);
            float close=Float.parseFloat(entries[4]);
            int volume=Integer.parseInt(entries[5]);
            float adjclose=Float.parseFloat(entries[6]);
             
           
            
            ps.setString(1, date);
            ps.setDouble(2, open);
            ps.setDouble(3, high);
            ps.setDouble(4, low);
            ps.setDouble(5,ans);
            ps2.setString(1, date);
            ps2.setDouble(2, close);
            ps.executeUpdate();
            ps2.executeUpdate();
            i++;
            if(i%1000==0)
                con.commit();
        }
        con.commit();
        con.setAutoCommit(true);
        bfr.close();
        fr.close();
        ps.close();
        ps2.close();
        }
        catch (Exception e)
        {
                    System.out.println(e);
        }
    }
    public static void getStockTablePredict() throws Exception
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
        
        
          String a=DatabaseImport.getContents("http://real-chart.finance.yahoo.com/table.csv?s=%5EBSESN&a=00&b=1&c=2016&d="+mon+"&e="+da+"&f="+year+"&g=d&ignore=.csv");  
          File f= new File("prediction.csv");
          a=a.replace("Date,Open,High,Low,Close,Volume,Adj Close", "");
          a=a.trim();
          PrintWriter out = new PrintWriter("prediction.txt");
          out.println(a);
          out.close();
          
    }
    
     public static void createTablePredict() throws Exception
    {
        try
        {
            Connection con=getConnection();
            PreparedStatement ps=con.prepareStatement("DROP TABLE IF EXISTS BSEPREDICT;");
            ps.executeUpdate();
            ps=con.prepareStatement("DROP TABLE IF EXISTS CLOSEPREDICT;");
            ps.executeUpdate();
            ps=con.prepareStatement("CREATE TABLE BSEPREDICT(date varchar(10),open double,high double,low double,news double);");
            ps.executeUpdate();
            ps=con.prepareStatement("CREATE TABLE CLOSEPREDICT(date varchar(10),close double);");
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
     public static double[] createMatrix() throws Exception
        {
            Connection con=getConnection();
            PreparedStatement ps=con.prepareStatement("select count(*) as cc from bsepredict;");
            ResultSet rs=ps.executeQuery();
            rs.next();
           
            int count=rs.getInt("cc");
            
            Matrix X=new Matrix(count,4);
            Matrix Y=new Matrix(count,1);
            ps=con.prepareStatement("select open,high,low,news from BSEPREDICT;");
            rs=ps.executeQuery();
            int i=0;
            int j=0;
            
           
            while(rs.next())
            {
             
                j=0;
                double o=rs.getDouble("open");
                double h=rs.getDouble("high");
                double l=rs.getDouble("low");
                double n=rs.getDouble("news");
                X.setValueAt(i, j++, o);
                X.setValueAt(i, j++, h);
                X.setValueAt(i, j++, l);
                X.setValueAt(i, j++, n);
                i++;
            }
            ps=con.prepareStatement("select close from CLOSEPREDICT;");
            rs=ps.executeQuery();
      
            i=0;
           
            while(rs.next())
            {
                
                j=0;
                double c=rs.getDouble("close");
                Y.setValueAt(i, j, c);
                i++;
            }
            ps.close();
            
            final boolean bias=true;
            Matrix ans=calculate(X,Y,bias);
            int r=ans.getNrows();
            int c=ans.getNcols();
            double arr[] = new double[r];
            int k=0;
            //System.out.println(r+" "+c);
             for(int x=0;x<r;x++)
            {
                for(int y=0;y<c;y++)
                {
                    arr[k++]=ans.getValueAt(x, y);
                }
            }
          return arr;
        }
      
      public static Matrix calculate(Matrix X, Matrix Y,boolean bias ) throws NoSquareException {
		if (bias)
			X = X.insertColumnWithValue1();
		checkDiemnsion(X,Y,bias);
		Matrix Xtr = MatrixMathematics.transpose(X); //X'
		Matrix XXtr = MatrixMathematics.multiply(Xtr,X); //XX'
		Matrix inverse_of_XXtr = MatrixMathematics.inverse(XXtr); //(XX')^-1
		if (inverse_of_XXtr == null) {
			System.out.println("Matrix X'X does not have any inverse. So MLR failed to create the model for these data.");
			return null;
		}
		final Matrix XtrY = MatrixMathematics.multiply(Xtr,Y); //X'Y
		return MatrixMathematics.multiply(inverse_of_XXtr,XtrY); //(XX')^-1 X'Y
	}
      
      public static void checkDiemnsion(Matrix X, Matrix Y,boolean bias) {
		if (X == null) 
			throw new NullPointerException("X matrix cannot be null.");
		if (Y == null) 
			throw new NullPointerException("Y matrix cannot be null.");
		
		if (X.getNcols() > X.getNrows()) {
			throw new IllegalArgumentException("The number of columns in X matrix (descriptors) cannot be more than the number of rows");
		}
		if (X.getNrows() != Y.getNrows()) {
			throw new IllegalArgumentException("The number of rows in X matrix should be the same as the number of rows in Y matrix. ");
		}
        }
    
}
