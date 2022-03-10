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
            int userId = db.loginPlayer(((Login) action).userName, ((Login) action).password);
            if(userId != -1){
                 // de lma n3rf hangebha ezayyy 
                GameHandler.addOnlinePlayer(gameHandler);
                System.out.println(GameHandler.onlineClients.size());
                gameHandler.setID(userId);//Save id for each socket (client)
                 String respons = "LoginResponse,Success,"+userId;
                 gameHandler.getPs().println(respons);
              //  return "LoginResponse,Success,"+userId;
            }else{
                String respons = "LoginResponse,Failure";
                 gameHandler.getPs().println(respons);
               // return "LoginResponse,Failure";
            }
        }
        if(action instanceof ClientClose){
            int userid = db.closePlayer(((ClientClose) action).userID);
            for(GameHandler gh : GameHandler.onlineClients){
                if(gh.getID() == userid){
                    GameHandler.onlineClients.remove(gh);
                    System.out.println("user closed "+userid);
                    String respons = "ClientClose,"+userid;
                   gameHandler.getPs().println(respons);
                   // return "ClientClose,"+userid;
                }else{
                     String respons = "Failure";
                   gameHandler.getPs().println(respons);
                 //   return "Failure";
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
    
}
