/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package newspaperdelivery;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author kanel
 */
public class DatabaseConnector {
    private static PreparedStatement statemt;
    private static ResultSet resultst;
    private static Statement stmt;
//    private Connection con;
    private static String url;
    private static String user;
    private static String password;
    
    //this function creates the connection between our program and the database and returns the connection
    private static Connection createConnection(){
        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(url,user,password);


        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;    
    }
    //this function sets the values required to establish the connection to the database
    public  void connectToDatabase(String url, String user, String password){
        this.url        = url;
        this.password   = password;
        this.user       = user;    
    }
    //this function looks into the login_detail database and if the provided information manch then returns the true
    public boolean checkLoginDetail(String username, String password){
        
        String query = "select * from login_detail where username=? and password =?";
        boolean checking = false;
        try {
            statemt = createConnection().prepareStatement(query); 
            statemt.setString(1, username);
            statemt.setString(2, password);
            resultst = statemt.executeQuery();
            if(resultst.next()){
                checking = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checking;        
    }
    
    public boolean usernameExists(String username){
        boolean exists = false;
        String checkIfUsernameExists = "select * from login_detail where username=?";
        try {
 
            statemt = createConnection().prepareStatement(checkIfUsernameExists);
            statemt.setString(1,username);
            resultst = statemt.executeQuery();
            if(resultst.next()){
                exists = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exists;    
    }
    public void addDelivery(int newspaperid, String sun, String mon,String tue, String wed, String thu,
                            String fri,String sat){
        try {
            String query = "update deliverydetail set newspaperid=100, sunday=?, monday=? where newspaperid=0;";
            statemt = createConnection().prepareStatement(query);
            statemt.setString(1, sun);
            statemt.setString(2, mon);
            statemt.setString(3,tue);
            statemt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public void showDelivery(String day){
        OpenFile deliveryDetail = new OpenFile();
        String filePath = "/Volumes/Raj/"+day+".csv";
        String showDeliveryQuery ="(SELECT 'FIRST Name', 'ADDRESS','Newspaper') \n" +
"union all (select \n" +
"	customerdetail.firstname,\n" +
"	customerdetail.address,\n" +
"	newspaperdetail.newspapername \n" +
"from customerdetail inner join deliverydetail on customerdetail.customerid=deliverydetail.customerid \n" +
"inner join newspaperdetail on deliverydetail.newspaperid=newspaperdetail.newspaperid where "
+ "deliverydetail."+day+"=\"yes\" INTO OUTFILE ? "
+ "FIELDS TERMINATED BY '' ENCLOSED BY '' LINES TERMINATED BY \"\\n\");";
        try {
            statemt = createConnection().prepareStatement(showDeliveryQuery);
            //statemt.setString(1, day);
            statemt.setString(1, filePath);
            statemt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(showDeliveryQuery);
    deliveryDetail.open(filePath);
    
    }

    public void addSuscriber(String fname, String lname, String address, 
            String ssn, String email, String phone, String sdate, String edate, String custid){
        String query = "insert into deliverydetail (customerid) value(?);";
        String put = "insert into CustomerDetail values(?,?,?,?,?,?,?,?,?)";
        
        try{
            statemt = createConnection().prepareStatement(put);
            statemt.setString(1, custid);
            statemt.setString(2, ssn);
            statemt.setString(3,fname);
            statemt.setString(4,lname);
            statemt.setString(5,address);
            statemt.setString(6,email);
            statemt.setString(7,phone);
            statemt.setString(8,sdate);
            statemt.setString(9,edate);
            statemt.executeUpdate();
            statemt = createConnection().prepareStatement(query);
            statemt.setString(1, custid);
            statemt.execute();

            JOptionPane.showMessageDialog(null, "Suscriber Added Successfully.");   

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void showSearchDetail(JTable search) throws SQLException{
        statemt = createConnection().prepareStatement("SELECT firstname,ssn,address FROM customerdetail");
        resultst = statemt.executeQuery();


// get column names
        int len = resultst.getMetaData().getColumnCount();
        Vector cols= new Vector(len);
        for(int i=1; i<=len; i++){ // Note starting at 1
            cols.add(resultst.getMetaData().getColumnName(i));
            System.out.println(resultst.getMetaData().getColumnName(i));
        }

        // Add Data
        Vector data = new Vector();
        while(resultst.next())
        {
            Vector row = new Vector(len);
            for(int i=1; i<=len; i++)
            {
                row.add(resultst.getString(i));
                System.out.println(resultst.getString(i));
            }
            data.add(row);
        }

        // Now create the table
        search = new JTable(data, cols);
        //return search;
    }
}