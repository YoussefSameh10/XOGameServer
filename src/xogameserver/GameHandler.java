/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Board;
import models.Mark;
import models.Match;
import static services.AvailableActions.ChallengeRequest;
import static services.AvailableActions.ChallengeResponse;
import services.ChallengeRequest;
import services.ChallengeResponse;
import services.RequestManager;
import services.ServerAction;
import static models.Mark.*;
import services.DAO;
import services.Database;

/**
 *
 * @author sandra
 */
public class GameHandler extends Thread {

    DataInputStream dis;
    PrintStream ps;
    public static Vector<GameHandler> onlineClients = new Vector<GameHandler>();
    public static Vector<GameHandler> inGameClients = new Vector<GameHandler>();
    private int ID;
    RequestManager requestManager;
    private Match match;
    private boolean isGameStarter = false;
    private Database db;
    StringBuffer gameRec = new StringBuffer("");

    public GameHandler(Socket cs) {
        requestManager = RequestManager.getInstance();
        db = DAO.getInstance();
        try {
            dis = new DataInputStream(cs.getInputStream());
            ps = new PrintStream(cs.getOutputStream());

            //when login successfully
            //  GameHandler.onlineClients.add(this);
            this.start();
        } catch (IOException ex) {
            try {
                System.out.println("from server: i closed the connection");
                cs.close();
                ps.close();
                dis.close();
                requestManager.updateAllPlayersListForAllPlayers();
                System.out.println("Game Handler Initializer !!");
                FXMLDocumentController.showAlertForError("Server Error!!\nCan't close server!!");
                //Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex1) {
                FXMLDocumentController.showAlertForError("Server Error!!\nCan't close server!!");
                //Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public static void addOnlinePlayer(GameHandler onlinePlayer) {
        onlineClients.add(onlinePlayer);
    }

    //to be done by tasneeem
    public void startNewGame(int playerOneId, int playerTwoId, boolean isGameStarter) {
        if (match == null) {
            Board board = new Board();
            this.isGameStarter = isGameStarter;
            match = new Match(playerOneId, playerTwoId, board, isGameStarter);
        }
    }

    public DataInputStream getDis() {
        return dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public PrintStream getPs() {
        return ps;
    }

    public void setPs(PrintStream ps) {
        this.ps = ps;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
        System.out.println("the game handler id in setID = " + ID);
    }

    public static void addInGamePlayer(GameHandler inGamePlayer) {
        inGameClients.add(inGamePlayer);
    }

    public boolean didStartGame() {
        return !(match == null);
    }

    public void playMove(int cellNumber, int playerTwoId) {
        //gameRec
        //if iam the game starteer i play with x
        switch (cellNumber) {
            case 0:
                boolean b1 = match.getBoard().placeMark(0, 0);
                match.getBoard().printGame();
                gameRec.append(isGameStarter ? "X" + 0 : "O" + 0);
                if (b1) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            //forward action to player 2
            case 1:
                boolean b2 = match.getBoard().placeMark(0, 1);
                match.getBoard().printGame();
                gameRec.append(isGameStarter ? "X" + 1 : "O" + 1);

                if (b2) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 2:
                boolean b3 = match.getBoard().placeMark(0, 2);
                match.getBoard().printGame();
                gameRec.append(isGameStarter ? "X" + 2 : "O" + 2);

                if (b3) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 3:
                boolean b4 = match.getBoard().placeMark(1, 0);
                match.getBoard().printGame();
                gameRec.append(isGameStarter ? "X" + 3 : "O" + 3);

                if (b4) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 4:
                boolean b5 = match.getBoard().placeMark(1, 1);
                match.getBoard().printGame();
                gameRec.append(isGameStarter ? "X" + 4 : "O" + 4);

                if (b5) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 5:
                boolean b6 = match.getBoard().placeMark(1, 2);
                match.getBoard().printGame();
                gameRec.append(isGameStarter ? "X" + 5 : "O" + 5);

                if (b6) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 6:
                boolean b7 = match.getBoard().placeMark(2, 0);
                match.getBoard().printGame();
                gameRec.append(isGameStarter ? "X" + 6 : "O" + 6);

                if (b7) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 7:
                boolean b8 = match.getBoard().placeMark(2, 1);
                match.getBoard().printGame();
                gameRec.append(isGameStarter ? "X" + 7 : "O" + 7);

                if (b8) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 8:
                boolean b9 = match.getBoard().placeMark(2, 2);
                match.getBoard().printGame();
                gameRec.append(isGameStarter ? "X" + 8 : "O" + 8);

                if (b9) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            default:
                System.out.println("Default value in play move switch");
        }
        System.out.println(gameRec);

        if (match.getBoard().isGameOver()) {
            System.out.println("In GAME HANDLER RESULT");
            GameHandler player = null;

            //save player1  id  (starter)  save player2 id save game record save boolean false
            if (match.getBoard().getWinningMark() == Mark.X) {
                if (isGameStarter) {
                    this.ps.println("GameResult,Win," + match.getBoard().getBoardStatus());
                    this.db.IncreasePlayerScore(ID);

                    for (GameHandler gh : GameHandler.inGameClients) {
                        if (gh.getID() == playerTwoId) {
                            gh.match = match;
                            gh.ps.println("GameResult,Lose," + match.getBoard().getBoardStatus());

                            //   save myId as player1 ID , save gh.getID as player2 ID save game rec 
                            db.saveGameMoves(ID, gh.getID(), gameRec.toString());
                            gh.gameRec = new StringBuffer();
                            gameRec = new StringBuffer();
                            gh.match = null;
                            match = null;
                            player = gh;
                        }
                    }
                } else {
                    this.ps.println("GameResult,Lose," + match.getBoard().getBoardStatus());
                    for (GameHandler gh : GameHandler.inGameClients) {
                        if (gh.getID() == playerTwoId) {
                            gh.match = match;
                            gh.ps.println("GameResult,Win," + match.getBoard().getBoardStatus());
                            this.db.IncreasePlayerScore(gh.getID());
                            //   save  gh.getIDas player1 ID ,save  myId  as player2 ID save game rec 
                            db.saveGameMoves(gh.getID(), ID, gameRec.toString());
                            gh.gameRec = new StringBuffer();
                            gameRec = new StringBuffer();
                            gh.match = null;
                            match = null;
                            player = gh;

                        }
                    }

                }

            } else if (match.getBoard().getWinningMark() == Mark.O) {

                if (isGameStarter) {
                    this.ps.println("GameResult,Lose," + match.getBoard().getBoardStatus());
                    for (GameHandler gh : GameHandler.inGameClients) {
                        if (gh.getID() == playerTwoId) {
                            gh.match = match;
                            gh.ps.println("GameResult,Win," + match.getBoard().getBoardStatus());
                            //   save myId as player1 ID , save gh.getID as player2 ID save game rec 
                            db.saveGameMoves(ID, gh.getID(), gameRec.toString());

                            gh.gameRec = new StringBuffer();
                            gameRec = new StringBuffer();
                            gh.match = null;
                            match = null;
                            player = gh;

                        }
                    }
                } else {
                    this.ps.println("GameResult,Win," + match.getBoard().getBoardStatus());
                    for (GameHandler gh : GameHandler.onlineClients) {
                        if (gh.getID() == playerTwoId) {
                            gh.match = match;
                            gh.ps.println("GameResult,Lose," + match.getBoard().getBoardStatus());
                            //   save  gh.getIDas player1 ID ,save  myId  as player2 ID save game rec 
                            db.saveGameMoves(gh.getID(), ID, gameRec.toString());

                            gh.gameRec = new StringBuffer();
                            gameRec = new StringBuffer();
                            gh.match = null;
                            match = null;
                            player = gh;

                        }

                    }

                }

            } else {
                this.ps.println("GameResult,TIE," + match.getBoard().getBoardStatus());
                for (GameHandler gh : GameHandler.inGameClients) {
                    if (gh.getID() == playerTwoId) {
                        if (isGameStarter) {
                            System.out.println("IN TIE 1");
                            db.saveGameMoves(ID, gh.getID(), gameRec.toString());
                        } else {
                            System.out.println("IN TIE 2");

                            db.saveGameMoves(gh.getID(), ID, gameRec.toString());
                        }

                        gh.match = match;
                        gh.ps.println("GameResult,TIE," + match.getBoard().getBoardStatus());
                        gh.match = null;
                        match = null;
                        gh.gameRec = new StringBuffer();
                        gameRec = new StringBuffer();
                        player = gh;

                    }
                }
            }

            if (player != null) {
                GameHandler.inGameClients.remove(player);
                GameHandler.onlineClients.add(player);
                GameHandler.inGameClients.remove(this);
                GameHandler.onlineClients.add(this);
                updateAllPlayersListForAllPlayers();
            }

        }

    }

    public void forwardMoveToOpponent(int opponentId, int cellNumber) {
        for (GameHandler gh : GameHandler.inGameClients) {
            if (gh.getID() == opponentId) {
                gh.match = match;
                gh.gameRec = gameRec;
                System.out.println("Found opponent ");
                gh.ps.println("Move," + ID + "," + opponentId + "," + cellNumber);
            }
        }

    }

    public void getPlayerFromonlineVector(String id1, String id2, String name1, String name2, String score1, String score2) {
        for (int i = 0; i < GameHandler.onlineClients.size(); i++) {
            if (GameHandler.onlineClients.get(i).ID == Integer.parseInt(id2)) {
                System.out.println("serch for challangggggggggggggggggggggggwwwwwwwwwwwwwww");
                GameHandler.onlineClients.get(i).ps.println("ChallengeRequest," + id1 + "," + id2 + "," + name1 + "," + name2 + "," + score1 + "," + score2 + ",true");

                System.out.println(GameHandler.onlineClients.get(i).ps);
                System.out.println("index = " + i);
            }
        }
        System.out.println("the number of onlineClient" + GameHandler.onlineClients.size());

    }

    public void closeConnections() {
        onlineClients.remove(this);
        System.out.println("from server: i closed the connection");
        try {
            if (this.dis != null) {
                this.dis.close();
            }
            if (this.ps != null) {
                this.ps.close();
            }
        } catch (IOException ex) {
            FXMLDocumentController.showAlertForError("Server Error!!\nCan't close server!!");
            //Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void replyonPlayer(String s, String id1, String id2, String name1, String name2, String score1, String score2) {
        System.out.println("insiddddddddddddddddoooottttttttttto");
        int index1 = -1, index2 = -1;
        if (s.equals("accept")) {
            System.out.println("insiddddddddddddddddf");
            for (int i = 0; i < GameHandler.onlineClients.size(); i++) {

                System.out.println("the number of players" + onlineClients.size());
                System.out.println("the plyers idessssssssss" + id1 + id2);
                System.out.println("id playerssssssssssssss" + GameHandler.onlineClients.get(i).ID);
                if (GameHandler.onlineClients.get(i).ID == Integer.parseInt(id1)) {
                    System.out.println("hereacceeeeeeeeeeppppttt");
                    System.out.println("the sender iddd" + GameHandler.onlineClients.get(i).ID);
                    GameHandler.onlineClients.get(i).ps.println("ChallengeResponse," + s + "," + id1 + "," + id2 + "," + name1 + "," + name2 + "," + score1 + "," + score2 + ",false");
                    index1 = i;

                }
                if (GameHandler.onlineClients.get(i).ID == Integer.parseInt(id2)) {
                    index2 = i;
                }

            }
            GameHandler p1 = GameHandler.onlineClients.get(index1);
            GameHandler p2 = GameHandler.onlineClients.get(index2);
            GameHandler.inGameClients.add(p1);
            GameHandler.inGameClients.add(p2);
            GameHandler.onlineClients.remove(p1);
            GameHandler.onlineClients.remove(p2);
            requestManager.updateAllPlayersListForAllPlayers();

        } else if (s.equals("notAccept")) {
            System.out.println("serverrrrrrrrrrrnnnnnnnnnnotaccepttttttttttttttttttt");
            for (int i = 0; i < GameHandler.onlineClients.size(); i++) {
                if (GameHandler.onlineClients.get(i).ID == Integer.parseInt(id1)) {
                    System.out.println(GameHandler.onlineClients.get(i).ID);
                    GameHandler.onlineClients.get(i).ps.println("ChallengeResponse," + s + "," + id1 + "," + id2 + "," + name1 + "," + name2 + "," + score1 + "," + score2 + ",false");
                }
            }
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
    
    
    
    
    
    
    
    
    
    

    @Override
    public void run() {
        while (true) {

            try {
                //y2ra el string w y7wlo l msg ll client
                String msg = dis.readLine();
                if (msg != null) {
                    ServerAction action = requestManager.parse(msg);
                    requestManager.process(action, this);
                    System.out.println(msg);
                    // String response =
                    /*
                        for loop over all online players to end the  move to the right player
                     */
                    // ps.println(response);
                } else {
                    System.out.println("CAN'T PARSE");
                }
            } catch (IOException ex) {
                closeConnections();
                break;
            }
        }
    }

}
