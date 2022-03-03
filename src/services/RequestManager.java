/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;
import xogameserver.GameHandler;
/**
 *
 * @author Youssef
 */

public class RequestManager {
    
    private final Database db = DAO.getInstance();
    private static RequestManager instance;
    
    private RequestManager() {
        
    }
    
    public static RequestManager getInstance() {
        if(instance == null){
           instance =  new RequestManager(); 
        }
        return  instance;
    }
    
    public ServerAction parse(String msg) { // bta5od string w btrag3 action swa2 kan register aw login aw...
        String[] parts;
        parts = msg.split(",");
        
        if(AvailableActions.Register.getString().equals(parts[0])) {
            String username = parts[1];
            String password = parts[2];
            return new Register(username, password);
        }
        if(AvailableActions.Login.getString().equals(parts[0])){
            String userName = parts[1];
            String password = parts[2];
            return new Login(userName, password);
        }
        return new Register("x", "y");
    }
    
    public String process(ServerAction action, GameHandler gameHandler) { // bta5od el action bta3y w be check lw register aw login (else if login) // ha3ml check with UN 3a4an hoa unique
        if(action instanceof Register) {
            boolean isSuccess = db.registerPlayer(((Register) action).username, ((Register) action).password);
            if(isSuccess) {
                return "Success";
            }
            else{
                return "Failure";
            }
        }
        if(action instanceof Login){
            int userId = db.loginPlayer(((Login) action).userName, ((Login) action).password);
            if(userId != -1){
                 // de lma n3rf hangebha ezayyy 
                GameHandler.addOnlinePlayer(gameHandler);
                System.out.println(GameHandler.onlineClients.size());
                gameHandler.setID(userId);
                return "Success,"+userId;
            }else{
                return "Failure";
            }
        }
        return "";
    }
    //ha match the un w ba3del el pass w lw tmama harg3 el success, id (with separator)
}
