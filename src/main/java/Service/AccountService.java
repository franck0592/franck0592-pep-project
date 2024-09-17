package Service;

import java.sql.SQLException;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
   
    private AccountDAO accountDAO;

    //no argument constructor that creating accountDAO instance to use inside the class
    public AccountService(){
        this.accountDAO=new AccountDAO();
    }
    //@return created account after checking some constraints
    public Account userRegistration(Account givenAccount) throws SQLException{
    // checking blank username
    if(givenAccount.getUsername().isEmpty()){
    return null;}
    //checking password length not over 4
    if(givenAccount.getPassword().length()<=4){
        return null;}
    /*checking whether the account username is already used to avoid duplicate*/
    Account alreadyExistsAccount=accountDAO.getAccountByUsername(givenAccount);
    if(alreadyExistsAccount==null){
    Account accountRegistered=accountDAO.createAccount(givenAccount);
    return accountRegistered;}

    if(alreadyExistsAccount.getUsername()==(givenAccount.getUsername())){
        return null;
    }

        return null;
    }

    //@return matching account by given credentials
    public Account userLogin(Account givenCredentials) throws SQLException{
        Account accountRetrieve=accountDAO.getAccountByGivenAccount(givenCredentials);
        if(accountRetrieve!=null){
            return accountRetrieve;
        }else{
            return null;
        }
       
    }

}
