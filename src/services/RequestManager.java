/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

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
    
    public ServerAction parse(String msg) {
        String[] parts;
        parts = msg.split(",");
        
        if(AvailableActions.Register.getString().equals(parts[0])) {
            String username = parts[1];
            String password = parts[2];
            return new Register(username, password);
        }
        return new Register("x", "y");
    }
    
    public String process(ServerAction action) {
        if(action instanceof Register) {
            boolean isSuccess = db.registerPlayer(((Register) action).username, ((Register) action).password);
            if(isSuccess) {
                return "Success";
            }
            else{
                return "Failure";
            }
        }
        return "";
    }
}
