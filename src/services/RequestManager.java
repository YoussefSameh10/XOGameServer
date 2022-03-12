/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Player;
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
            return new ClientClose();
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
         if(AvailableActions.ChallengeRequest.getString().equals(parts[0])){
            String id1 =  parts[1];
            String id2 = parts[2];
            
            return new ChallengeRequest(id1, id2);
        }
          if(AvailableActions.ChallengeResponse.getString().equals(parts[0])){
            String challangeRespons =  parts[1];
            String id1 = parts[2];
            String id2 = parts[3];
            
            
            return new ChallengeResponse(challangeRespons,id1,id2);
        }
        return new Register("x", "y");
    }
    
    public void process(ServerAction action, GameHandler gameHandler) { // bta5od el action bta3y w be check lw register aw login (else if login) // ha3ml check with UN 3a4an hoa unique
        if(action instanceof Register) {
            boolean isSuccess = db.registerPlayer(((Register) action).username, ((Register) action).password);
            if(isSuccess) {
                String respons = "RegisterResponse,Success";
                 gameHandler.getPs().println(respons);
               // return "RegisterResponse,Success";
            }
            else{
                String respons = "RegisterResponse,Failure";
                 gameHandler.getPs().println(respons);
               // return "RegisterResponse,Failure";
            }
        }
        if(action instanceof Login){
            boolean isLogged = false;
            Player player = db.loginPlayer(((Login) action).userName, ((Login) action).password);
                for(GameHandler gh : GameHandler.onlineClients){
                    if(player != null && (player.getID() == gh.getID())){
                    //(player.getID() != GameHandler.onlineClients.indexOf(gameHandler))
                    isLogged = true;
                    break;
                }
            }
            if(player != null && isLogged == false){
                GameHandler.addOnlinePlayer(gameHandler);
                gameHandler.setID(player.getID());
                String respons = "LoginResponse,Success,"+player.getID()+","+player.getUsername()+","+player.getScore();
                gameHandler.getPs().println(respons);
//                for (GameHandler onlineClient : GameHandler.onlineClients) {
//                    onlineClient.getPs().println("GetOnlinePlayersListResponse," + getOnlinePlayersList());
//                }
                updateAllPlayersListForAllPlayers();
            }else{
                String respons = "LoginResponse,Failure";
                gameHandler.getPs().println(respons);
               // return "LoginResponse,Failure";
            }
        }
        if(action instanceof ClientClose){
            boolean isOnlineClient = false;
            for(GameHandler gh : GameHandler.onlineClients){
                
                if(gh.getID() == gameHandler.getID()){
                    isOnlineClient = true;
                    try {
                        gameHandler.getDis().close();
                        gameHandler.getPs().close();
                    } catch (IOException ex) {
                        Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            for(GameHandler gh : GameHandler.inGameClients){
                
                if(gh.getID() == gameHandler.getID()){
                    isOnlineClient = false;
                    try {
                        gameHandler.getDis().close();
                        gameHandler.getPs().close();
                    } catch (IOException ex) {
                        Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
            
            if(isOnlineClient){
                System.out.println("Removing player from onlineClients vector");
                GameHandler.onlineClients.remove(gameHandler);
            }else{
                System.out.println("Removing player from inGameClients vector");
                GameHandler.inGameClients.remove(gameHandler);
                
            }
            System.out.println("I reached end of client close !!");
            updateAllPlayersListForAllPlayers();
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
            String usernames = getOnlinePlayersList();
            
            String respons ="GetOnlinePlayersListResponse," + usernames ;
           gameHandler.getPs().println(respons);
            
        }
         if(action instanceof ChallengeRequest){
            System.out.println("i am in processhhhhhhhhhhhhhh  "+((ChallengeRequest) action).id1+((ChallengeRequest) action).id2);
            int id1 = Integer.parseInt(((ChallengeRequest) action).id1);
            int id2 = Integer.parseInt(((ChallengeRequest) action).id2);
           String name1= db.getPlayerUsername(id1);
            String name2= db.getPlayerUsername(id2);
            String score1 = Integer.toString(db.getPlayerScore(id1));
            String score2 = Integer.toString(db.getPlayerScore(id2));
            gameHandler.getPlayerFromonlineVector(((ChallengeRequest) action).id1,((ChallengeRequest) action).id2,name1,name2,score1,score2);
            // return ((ChallengeRequest) action).id2;
         }
         
         if(action instanceof ChallengeResponse){
            System.out.println(((ChallengeResponse) action).respons+"ggggggggggggggggg");
             int id1 = Integer.parseInt(((ChallengeResponse) action).id1);
            int id2 = Integer.parseInt(((ChallengeResponse) action).id2);
           String name1= db.getPlayerUsername(id1);
            String name2= db.getPlayerUsername(id2);
            String score1 = Integer.toString(db.getPlayerScore(id1));
            String score2 = Integer.toString(db.getPlayerScore(id2));
            String respons =((ChallengeResponse) action).respons;
           gameHandler.replyonPlayer( respons,((ChallengeResponse) action).id1,((ChallengeResponse) action).id2,name1,name2,score1,score2);
             System.out.println("gggggggggggggggkkkkkkkkkkkkkkkkllllllllllllllllll"+((ChallengeResponse) action).respons);
             //return ((ChallengeResponse) action).respons;
         }
        
       
    }
      public String getOnlinePlayersList()
    {
        String users = "";
        int noOfOnlineClients = GameHandler.onlineClients.size();
        for (int i=0 ; i<noOfOnlineClients ; i++)
        {
            int currentPlayerID = GameHandler.onlineClients.get(i).getID();
            int currentPlayerScore = db.getPlayerScore(currentPlayerID);
            String currentPlayerUsername = db.getPlayerUsername(currentPlayerID);
            if(!currentPlayerUsername.equals(""));
            {
                if (i==0)
                {
                    users = currentPlayerUsername + ":" + String.valueOf(currentPlayerID) + ":" +String.valueOf(currentPlayerScore);
                }
                else 
                {
                    users = users + " " + currentPlayerUsername + ":" + String.valueOf(currentPlayerID) + ":" + String.valueOf(currentPlayerScore);
                }
            }
        }
        return users;
    }
    public void updateAllPlayersListForAllPlayers()
    {
        System.out.println("Online Players: " + GameHandler.onlineClients.size());
        System.out.println("In-Game Players: " + GameHandler.inGameClients.size());
        for (GameHandler onlineClient : GameHandler.onlineClients) {
                    onlineClient.getPs().println("GetOnlinePlayersListResponse," + getOnlinePlayersList());
        }
    }
}
