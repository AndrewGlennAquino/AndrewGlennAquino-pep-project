package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.*;

/**
 * A DAO class that mediates the transfer of data between Java and the tables in SocialMedia database
 * 
 * account:
 * account_id INT,
 * username VARCHAR(255) unique,
 * password VARCHAR(255)
 * 
 * message:
 * message_id INT,
 * posted_by INT,
 * message_text VARCHAR(255),
    time_posted_epoch BIGINT,
    foreign key (posted_by) references account(account_id)
 */
public class SocialMediaDAO {
    
    /**
     * Query account table for all records
     * 
     * @return all records in account, empty list if there are no records
     */
    public List<Account> getAllAccounts() {
        Connection conn = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();

        try {
            String sql = "SELECT * FROM account;";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Account acc = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password"));
                accounts.add(acc);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    /**
     * Query account table for a specific account using username and password
     * 
     * @param username account username to query
     * @param password account password to query
     * 
     * @return Account object if matching record is found in account, null otherwise
     */
    public Account getAccountByUsernamePassword(String username, String password) {
        Connection conn = ConnectionUtil.getConnection();

        try{ 
            String sql = "SELECT * FROM account WHERE username = (?) AND password = (?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Account acc = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password"));
                return acc;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Insert new Account object with username and password into account table if the given account username is not blank,
     * the password is at minimum 4 cahracters long, and an existing account does not exist with the same username
     * 
     * @param account account username and password to insert
     * 
     * @return Account object with account_id if successful, null otherwise
     */
    public Account insertAccount(Account account) {
        Connection conn = ConnectionUtil.getConnection();
        
        if(account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return null;
        }

        for(Account a : getAllAccounts()) {
            if(a.getUsername().equals(account.getUsername())) {
                return null;
            }
        }

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return getAccountByUsernamePassword(account.getUsername(), account.getPassword());
    }

    /**
     * Query message table for all messages
     * 
     * @return all records in message, empty list if there are no records
     */
    public List<Message> getAllMessages() {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Message mess = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(mess);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    /**
     * Query message table for a specific message using message_id
     * 
     * @param id message message_id to query
     * 
     * @return Message object if matching record is found in message, null otherwise
     */
    public Message getMessageById(int id) {
        Connection conn = ConnectionUtil.getConnection();

        try{ 
            String sql = "SELECT * FROM message WHERE message_id = (?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Message mess = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                return mess;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Query message table for all records of a specific account_id
     * 
     * @param id account_id to query
     * 
     * @return all messages of a specific account_id, empty list if there are no records
     */
    public List<Message> getAllMessageByAccountId(int id) {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = (?);";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Message mess = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(mess);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    /**
     * Insert new Message object with posted_by, message_text, and time_posted_epoch into message table if the given 
     * message_text is not blank and less than 255 characters and the posted_by id matches an existing account_id
     * 
     * @param message message posted_by, message_text, and time_posted_epoch to insert
     * 
     * @return Message object with message_id if successful, null otherwise
     */
    public Message insertMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();
        
        if(message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return null;
        }

        Boolean exists = false;
        for(Account a : getAllAccounts()) {
            if(a.getAccount_id() == message.getPosted_by()) {
                exists = true;
            }
        }

        if(exists) {
            try {
                String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setInt(1, message.getPosted_by());
                ps.setString(2, message.getMessage_text());
                ps.setLong(3, message.getTime_posted_epoch());

                ps.executeUpdate();

                for(Message m : getAllMessageByAccountId(message.getPosted_by())) {
                    if(m.getMessage_text().equals(message.getMessage_text()) &&
                    m.getTime_posted_epoch() == message.getTime_posted_epoch()) {
                        return m;
                    }
                }
            } catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return null;
    }

    /**
     * Delete message with matching message_id from message table. Return null if matching message_id does not exist 
     * 
     * @param id message_id to delete
     * 
     * @return deleted message object, null otherwise
     */
    public Message deleteMessageById(int id) {
        Connection conn = ConnectionUtil.getConnection();
        Message deleted = getMessageById(id);

        try {
            String sql = "DELETE FROM message WHERE message_id = (?);";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return deleted;
    }

    /**
     * Update message with matching message_id from message table. Return null if matching message_id does not exist
     * 
     * @param id message_id to update
     * @param newMessage new message_text to update
     * 
     * @return updated message object, null otherwise
     */
    public Message updateMessageById(int id, String newMessage) {
        Connection conn = ConnectionUtil.getConnection();

        if(newMessage.length() > 255) {
            return null;
        }

        if(getMessageById(id) == null) {
            return null;
        }

        try {
            String sql = "UPDATE message SET message_text = (?) WHERE message_id = (?);";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, newMessage);
            ps.setInt(2, id);

            ps.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return getMessageById(id);
    }
}