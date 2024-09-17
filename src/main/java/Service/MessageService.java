package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(){
        this.messageDAO=new MessageDAO();
        this.accountDAO=new AccountDAO();
    }
    //@return all messages from database
    public List<Message> getAllMessages() throws SQLException{
        return messageDAO.getAllMessage();
    }
    //creating the message
    public Message creatingMessage(Message message) throws SQLException{
        //checking whether the message is blank or not
        if(message.getMessage_text().isBlank()){
            return null;
        }
        //check length and real user constraints
        if(message.getMessage_text().length()<255){
            Account messagePostedByAcount=accountDAO.getAccountById(message.getPosted_by());
            if(messagePostedByAcount!=null){
                return messageDAO.createMessage(message);
            }else{
                return null;
            }
        }
        return null;
    }
    //@Return message by given id
    public Message getMessageById(int messageId) throws SQLException{
        Message messageRetreived=messageDAO.getMessageById(messageId);
        if(messageRetreived!=null){
            return messageRetreived;
        }else{
            return null;
        }
    }

    //@Return message updated after checking some constraints
    public Message updateMessageByMessageId(Message messageToUpdate) throws SQLException{
        //checking whether the message exists or not
        Message messageRetrieved=messageDAO.getMessageById(messageToUpdate.getMessage_id());
        if(messageRetrieved==null){
            return null;
        }
        //checking blank and length not over 255*/
        if(!messageToUpdate.getMessage_text().isBlank() && messageToUpdate.getMessage_text().length()<255){
            Message messageUpdated=messageDAO.updateMessage(messageToUpdate);
            if(messageUpdated!=null){
                return messageUpdated;
            }
        }
       
        return null;
    }

    public Message deleteMessageByMessageId(int message_id) throws SQLException{
        Message messageRetreivedForDeletion=messageDAO.getMessageById(message_id);
        if(messageRetreivedForDeletion!=null){
            int rowAffected=messageDAO.deleteMessage(message_id);
            if(rowAffected>0){
                return messageRetreivedForDeletion;
            }else{
                return null;
            }
        }
        return null;
    }
    
}
