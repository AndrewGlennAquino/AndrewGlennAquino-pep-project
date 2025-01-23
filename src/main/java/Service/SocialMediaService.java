package Service;

import Model.Account;
import Model.Message;
import DAO.SocialMediaDAO;
import java.util.*;

/**
 * SocialMediaService class is the layer between SocialMediaDAO and SocialMediaController
 */
public class SocialMediaService {
    public SocialMediaDAO SocialMediaDAO;

    /**
     * No-arg constructor that creates a SocialMediaDAO
     */
    public SocialMediaService() {
        this.SocialMediaDAO = new SocialMediaDAO();
    }

    /**
     * Constructor that creates a SocialMediaDAO using a provided SocialMediaDAO
     * @param socialMediaDAO
     */
    public SocialMediaService(SocialMediaDAO socialMediaDAO) {
        this.SocialMediaDAO = socialMediaDAO;
    }

    /**
     * Retrieves all accounts
     * @return List of all accounts, empty list if there are no accounts
     */
    public List<Account> getAllAccounts() {
        return SocialMediaDAO.getAllAccounts();
    }

    /**
     * Retrieve a specific account using a valid username and password
     * @param username username to login
     * @param password password to login
     * @return Account with matching username and password, null otherwise
     */
    public Account getAccount(String username, String password) {
        return SocialMediaDAO.getAccountByUsernamePassword(username, password);
    }

    /**
     * Create an account if the username is not blank, the password is at minimum 4 characters, and an account does not 
     * already exist with the same username
     * @param account account to create
     * @return Created account if successful, null otherwise
     */
    public Account createAccount(Account account) {
        return SocialMediaDAO.insertAccount(account);
    }

    /**
     * Retrieves all messages from all accounts
     * @return List of all messages, empty list if there are no messages
     */
    public List<Message> getAllMessages() {
        return SocialMediaDAO.getAllMessages();
    }

    /**
     * Retrieve a specific message using a valid message id
     * @param id message id to retrieve
     * @return Message if successful, null otherwise
     */
    public Message getMessage(int id) {
        return SocialMediaDAO.getMessageById(id);
    }

    /**
     * Get all messages from a specific account using a valid account id
     * @param id account id to search
     * @return List of all messages if successful, empty list if there are no messages
     */
    public List<Message> getAllAccountMessages(int id) {
        return SocialMediaDAO.getAllMessageByAccountId(id);
    }

    /**
     * Create a new message with a valid account id, the body of the message is not blank and less than 255 characters,
     * and a valid time of post is given
     * @param message message to post
     * @return Posted message if successful, null otherwise
     */
    public Message createMessage(Message message) {
        return SocialMediaDAO.insertMessage(message);
    }

    /**
     * Delete a specific message using a valid message id
     * @param id message id to delete
     * @return Deleted message if successful, null otherwise
     */
    public Message deleteMessage(int id) {
        return SocialMediaDAO.deleteMessageById(id);
    }

    /**
     * Update a specific message using a valid message id and the new message is not blank and less than 255 characters
     * @param id message id to update
     * @param newMessage new message to insert
     * @return Updated message if successful, null otherwise
     */
    public Message updateMessage(int id, String newMessage) {
        return SocialMediaDAO.updateMessageById(id, newMessage);
    }
}
