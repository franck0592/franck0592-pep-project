package Controller;

import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
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

    public SocialMediaController(){
        this.accountService=new AccountService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::userRegistrationHandler);
        app.post("/login",this::userLoginHandler);

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


}