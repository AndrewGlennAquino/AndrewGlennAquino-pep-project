package Controller;

import Model.Account;
import Model.Message;
import DAO.SocialMediaDAO;
import Service.SocialMediaService;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    SocialMediaService socialMediaService;

    /**
     * TODO: DOCUMENTATION
     */
    public SocialMediaController() {
        socialMediaService = new SocialMediaService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::getAccountHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAccountMessagesHandler);
        return app;
    }

    /**
     * TODO: DOCUMENTATION
     * @param ctx
     * @throws JsonProcessingException
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account addedAcc = socialMediaService.createAccount(acc);
        if(addedAcc != null) {
            ctx.json(mapper.writeValueAsString(addedAcc));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * TODO: DOCUMENTATION
     * @param ctx
     * @throws JsonProcessingException
     */
    private void getAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account getAcc = socialMediaService.getAccount(acc.getUsername(), acc.getPassword());
        if(getAcc != null) {
            ctx.json(mapper.writeValueAsString(getAcc));
            ctx.status(200);
        } else {
            ctx.status(401);
        }
    }

    /**
     * TODO: DOCUMENTATION
     * @param ctx
     * @throws JsonProcessingException
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message mes = mapper.readValue(ctx.body(), Message.class);
        Message addMes = socialMediaService.createMessage(mes);
        if(addMes != null) {
            ctx.json(mapper.writeValueAsString(addMes));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * TODO: DOCUMENTATION
     * @param ctx
     * @throws JsonProcessingException
     */
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = socialMediaService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    /**
     * TODO: DOCUMENTATION
     * @param ctx
     * @throws JsonProcessingException
     */
    private void getMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message getMes = socialMediaService.getMessage(Integer.parseInt(ctx.pathParam("message_id")));
        if(getMes != null) {
            ctx.json(mapper.writeValueAsString(getMes));
            ctx.status(200);
        } else {
            ctx.status(200);
        }
    }

    /**
     * TODO: DOCUMENTATION
     * @param ctx
     * @throws JsonProcessingException
     */
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message delMes = socialMediaService.deleteMessage(Integer.parseInt(ctx.pathParam("message_id")));
        if(delMes != null) {
            ctx.json(mapper.writeValueAsString(delMes));
            ctx.status(200);
        } else {
            ctx.status(200);
        }
    }

    /**
     * TODO: DOCUMENTATION
     * @param ctx
     * @throws JsonProcessingException
     */
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message mes = mapper.readValue(ctx.body(), Message.class);
        Message upMes = socialMediaService.updateMessage(
            Integer.parseInt(ctx.pathParam("message_id")), 
            mes.getMessage_text()
        );
        if(upMes != null) {
            ctx.json(mapper.writeValueAsString(upMes));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * TODO: DOCUMENTATION
     * @param ctx
     * @throws JsonProcessingException
     */
    private void getAccountMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = socialMediaService.getAllAccountMessages(
            Integer.parseInt(ctx.pathParam("account_id"))
        );
        ctx.json(messages);
        ctx.status(200);
    }
}