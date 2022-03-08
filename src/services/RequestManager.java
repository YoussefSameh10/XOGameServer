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
        if(AvailableActions.ClientClose.getString().equals(parts[0])){
            int userID = Integer.parseInt(parts[1]);
            return new ClientClose(userID);
        }
             //"PlayMove",senderId,recieverId, CellNumber   PlayMove,1,2,5

         if(AvailableActions.Move.getString().equals(parts[0])){
            int senderId = Integer.parseInt(parts[1]) ;
            int recieverId = Integer.parseInt(parts[2]) ;
            int cellToPlay = Integer.parseInt(parts[3]) ;
            return new Move(cellToPlay, senderId, recieverId);
        }
        
         if (AvailableActions.GetOnlinePlayersList.getString().equals(parts[0])){
            return new GetOnlinePlayersList();
        }
        return new Register("x", "y");
    }
    
    public String process(ServerAction action, GameHandler gameHandler) { // bta5od el action bta3y w be check lw register aw login (else if login) // ha3ml check with UN 3a4an hoa unique
        if(action instanceof Register) {
            boolean isSuccess = db.registerPlayer(((Register) action).username, ((Register) action).password);
            if(isSuccess) {
                return "RegisterResponse,Success";
            }
            else{
                return "RegisterResponse,Failure";
            }
        }
        if(action instanceof Login){
            int userId = db.loginPlayer(((Login) action).userName, ((Login) action).password);
            if(userId != -1){
                 // de lma n3rf hangebha ezayyy 
                GameHandler.addOnlinePlayer(gameHandler);
                System.out.println(GameHandler.onlineClients.size());
                gameHandler.setID(userId);//Save id for each socket (client)
                return "LoginResponse,Success,"+userId;
            }else{
                return "LoginResponse,Failure";
            }
        }
        if(action instanceof ClientClose){
            int userid = db.closePlayer(((ClientClose) action).userID);
            for(GameHandler gh : GameHandler.onlineClients){
                if(gh.getID() == userid){
                    GameHandler.onlineClients.remove(gh);
                    System.out.println("user closed "+userid);
                    return "ClientClose,"+userid;
                }else{
                    return "Failure";
                }
            }
        }
         if(action instanceof Move){
             //"PlayMove",senderId,recieverId, CellNumber   PlayMove,1,2,5
           if(gameHandler.didStartGame()){
               gameHandler.playMove(((Move) action).cellNumber, ((Move) action).recieverId);
               
           }else{
               gameHandler.startNewGame(((Move) action).senderId, ((Move) action).recieverId, true);
               gameHandler.playMove(((Move) action).cellNumber, ((Move) action).recieverId);
           }
                
        }
        
        if (action instanceof GetOnlinePlayersList){
            System.out.println("I am in the action of type GetOnlineList");
            String usernames = "";
            int noOfOnlineClients = GameHandler.onlineClients.size();
            for (int i=0 ; i<noOfOnlineClients ; i++)
            {
                int currentPlayerID = GameHandler.onlineClients.get(i).getID();
                String currentPlayerUsername = db.getPlayerAtID(currentPlayerID);
                if(!currentPlayerUsername.equals(""));
                {
                    // usernames is empty 
                    if (i==0)
                    {
                        usernames = currentPlayerUsername + ":" + String.valueOf(currentPlayerID);
                    }
                    else 
                    {
                        // ahmed,mohamed
                        usernames = usernames + " " + currentPlayerUsername + ":" + String.valueOf(currentPlayerID);
                    }
                }
            }
            
            return "GetOnlinePlayersListResponse," + usernames;
        }
        
        return "";
    }
    //ha match the un w ba3del el pass w lw tmama harg3 el success, id (with separator)
}
