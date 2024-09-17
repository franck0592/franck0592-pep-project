package Controller;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.matches;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(){
        this.accountService=new AccountService();
        this.messageService=new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::userRegistrationHandler);
        app.post("/login",this::userLoginHandler);
        app.get("/messages",this::retrievingAllMessagesHandler);
        app.post("/messages",this::creatingMessageHandler);
        app.get("/messages/{message_id}",this::retrievingMessageByMessageIdHandler);
        app.patch("/messages/{message_id}",this::updatingMessageByMessageIdHandler);
        app.delete("/messages/{message_id}", this::deletingMessageByMessageIdHandler);
        app.get("/accounts/{account_id}/messages",this::getAllMessagesByAccountIdHandler);
        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    //Handler method to process registering request from endpoint:localhost:8080/register
    public void userRegistrationHandler(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper=new ObjectMapper();
        Account accountToRegister=mapper.readValue(ctx.body(), Account.class);
        Account registeredAccount=accountService.userRegistration(accountToRegister);
        if(registeredAccount!=null){
            ctx.json(mapper.writeValueAsString(registeredAccount));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }
    //Handler method to processs login request from endpoint:localhost:8080/login
    public void userLoginHandler(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper=new ObjectMapper();
        Account accountCredentials=mapper.readValue(ctx.body(), Account.class);
        Account accountRetrieved=accountService.userLogin(accountCredentials);
        if(accountRetrieved!=null){
            ctx.json(mapper.writeValueAsString(accountRetrieved));
            ctx.status(200);
        }else{
            ctx.status(401);
        }
    }
    //Handler method to retreive all messages into database
    public void retrievingAllMessagesHandler(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper=new ObjectMapper();
        List<Message> messageList=messageService.getAllMessages();
        ctx.json(mapper.writeValueAsString(messageList));
        ctx.status(200);
    }
    //Handler method to create message 
    public void creatingMessageHandler(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper=new ObjectMapper();
        Message messageRequest=mapper.readValue(ctx.body(), Message.class);
        Message messageCreated=messageService.creatingMessage(messageRequest);
        if(messageCreated!=null){
            ctx.json(mapper.writeValueAsString(messageCreated));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }
    //Handler method to retreive message by given message id
    public void retrievingMessageByMessageIdHandler(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper=new ObjectMapper();
        int messageId=Integer.parseInt(ctx.pathParam("message_id"));
        Message messageRetrieved=messageService.getMessageById(messageId);
        if(messageRetrieved!=null){
            ctx.json(mapper.writeValueAsString(messageRetrieved));
            ctx.status(200);
        }else{
            ctx.status(200);
        }

    }
    //Handler method for returning message update
    public void updatingMessageByMessageIdHandler(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper=new ObjectMapper();
        int messageId=Integer.parseInt(ctx.pathParam("message_id"));
        Message messageToUpdate=mapper.readValue(ctx.body(), Message.class);
        messageToUpdate.setMessage_id(messageId);
        Message messageUpdated=messageService.updateMessageByMessageId(messageToUpdate);
        if(messageUpdated!=null){
            ctx.json(mapper.writeValueAsString(messageUpdated));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    //Handler method for deletion of the message by given message id
    public void deletingMessageByMessageIdHandler(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper=new ObjectMapper();
        int messageId=Integer.parseInt(ctx.pathParam("message_id"));
        Message messageDeleted=messageService.deleteMessageByMessageId(messageId);
        if(messageDeleted!=null){
            ctx.json(mapper.writeValueAsString(messageDeleted));
            ctx.status(200);
        }else{
            ctx.status(200);
        }
    }
    //Handler method to retreive all messages by given account id
    public void getAllMessagesByAccountIdHandler(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper=new ObjectMapper();
        int accountId=Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messageListRetreived=messageService.getAllMessageByAccountId(accountId);
        if(messageListRetreived.size()>0){
            ctx.json(mapper.writeValueAsString(messageListRetreived));
            ctx.status(200);
        }else{
            ctx.json(mapper.writeValueAsString(messageListRetreived));
            ctx.status(200);
        }
    }


}