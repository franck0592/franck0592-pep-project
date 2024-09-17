package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;


public class AccountDAO {
    
    // create the user account
    public Account createAccount(Account account) throws SQLException{
        Connection connection=ConnectionUtil.getConnection();
        Account accountCreated;
        //SQL statement to create new user account
        String sqlStatement="INSERT INTO account(username,password) VALUES(?,?)";
        PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

        //Initialize prepareStatement parameters 
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());

        preparedStatement.executeUpdate();
        ResultSet pKeyResultSet=preparedStatement.getGeneratedKeys();
        
        //Retreiving data
        if(pKeyResultSet.next()){
            int generatedAccountId=pKeyResultSet.getInt(1);
            accountCreated=new Account(generatedAccountId, account.getUsername(), account.getPassword());
            return accountCreated;
        }

        return null;
    }
    
    //Retrieve the user account by Given account (username and password)
    public Account getAccountByGivenAccount(Account account) throws SQLException{
        Connection connection=ConnectionUtil.getConnection();
        Account accountRetrieve;
        //SQL statement to retrieve account from database after check the given account
        String sqlStatement="SELECT * FROM account WHERE username=? and password=?";
        PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);

        //initialize prepareStatement parameters
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());

        ResultSet results=preparedStatement.executeQuery();
        if (results.next()) {
            accountRetrieve=new Account(results.getInt("account_id"), results.getString("username"), results.getString("password"));
            return accountRetrieve;
        }

        return null;
    }

    //Retrieve the user account by Given account username
    public Account getAccountByUsername(Account account) throws SQLException{
        Connection connection=ConnectionUtil.getConnection();
        Account accountRetrieve;
        //SQL statement to retrieve account from database after check the given account
        String sqlStatement="SELECT * FROM account WHERE username=?";
        PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);

        //initialize prepareStatement parameters
        preparedStatement.setString(1, account.getUsername());

        ResultSet results=preparedStatement.executeQuery();
        if (results.next()) {
            accountRetrieve=new Account(results.getInt("account_id"), results.getString("username"), results.getString("password"));
            return accountRetrieve;
        }

        return null;
    }

}
