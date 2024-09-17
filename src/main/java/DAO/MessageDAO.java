package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    private Connection connection=ConnectionUtil.getConnection();

    //persisting message into database
    public Message createMessage(Message messageRequest) throws SQLException{
        //Sql statement for persisting message into database
        String sqlStatement="INSERT INTO message(posted_by,message_text,time_posted_epoch) VALUES(?,?,?)";
        PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement,Statement.RETURN_GENERATED_KEYS);
       
        //adding preparedStatement parameters
        preparedStatement.setInt(1, messageRequest.getPosted_by());
        preparedStatement.setString(2, messageRequest.getMessage_text());
        preparedStatement.setLong(3, messageRequest.getTime_posted_epoch());
        preparedStatement.executeUpdate();
        
        //retrieve the message created
        ResultSet pkResultSet=preparedStatement.getGeneratedKeys();
        if(pkResultSet.next()){
            int messageIdCreated=pkResultSet.getInt("message_id");
            return new Message(messageIdCreated, messageRequest.getPosted_by(), messageRequest.getMessage_text(), messageRequest.getTime_posted_epoch());
        }

        return null;
    }
    //return all messages from database
    public List<Message> getAllMessage() throws SQLException{
        List<Message> messageList=new ArrayList<>();
        Message messageRetreived;
        //sql statement to retreive all messages
        String sqlStatement="SELECT * FROM message";
        //executing select request
        Statement statement=connection.createStatement();
        ResultSet results=statement.executeQuery(sqlStatement);
        //retreive data
        while(results.next()){
            messageRetreived=mapRowToMessage(results);
            messageList.add(messageRetreived);
        }
        return messageList;
    }
    //return matching message by given ID
    public Message getMessageById(int messageId) throws SQLException{
        //sql statement to retreive message by given id
        String sqlStatement="SELECT * FROM message WHERE message_id=?";
        PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
        //set preparedStatement parameter
        preparedStatement.setInt(1, messageId);
        //retrieving data
        ResultSet result=preparedStatement.executeQuery();
        if(result.next()){
            return mapRowToMessage(result);
        }
        return null;
    }
    //delete message by given ID
    public int deleteMessage(int messageId) throws SQLException{
        String sqlStatement="DELETE FROM message WHERE message_id=?";
        PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
        int rowAffected=preparedStatement.executeUpdate();
        if(rowAffected>0){
            return rowAffected;
        }else{
            return 0;
        }
    }
    //return update message by given message id
    public Message updateMessage(Message messageToUpdate) throws SQLException{
        String sqlStatement="UPDATE message SET message_text=?";
        PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, messageToUpdate.getMessage_text());
        //processing the request
        int rowAffected=preparedStatement.executeUpdate();
        if(rowAffected>0){
            return getMessageById(messageToUpdate.getMessage_id());
        }
        return null;
    }
    //return list of messages by given account id
    public List<Message> getAllMessageByAccountId(int accountId) throws SQLException{
        List<Message> messageList=new ArrayList<>();
        Message messageRetrieved;
        //sql statement to retreive messages by account id
        String sqlStatement="SELECT * FROM message WHERE posted_by=?";
        PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, accountId);
        //retreiving data
        ResultSet results=preparedStatement.executeQuery();
        while(results.next()){
            messageRetrieved=mapRowToMessage(results);
            messageList.add(messageRetrieved);
        }

        return messageList;
    }

    private Message mapRowToMessage(ResultSet resultSet) throws SQLException{
        Message message=new Message();
        message.setMessage_id(resultSet.getInt("message_id"));
        message.setPosted_by(resultSet.getInt("posted_by"));
        message.setMessage_text(resultSet.getString("message_text"));
        message.setTime_posted_epoch(resultSet.getLong("time_posted_epoch"));
        return message;
    }
    
}
