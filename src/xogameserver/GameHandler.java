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
import services.ChallengeRequest;
import services.RequestManager;
import services.ServerAction;
import static models.Mark.*;

/**
 *
 * @author sandra
 */
public class GameHandler extends Thread {

    DataInputStream dis;
    PrintStream ps;
    public static Vector<GameHandler> onlineClients = new Vector<GameHandler>();
    static Vector<GameHandler> inGameClients = new Vector<GameHandler>();
    private int ID;
    RequestManager requestManager;
    private Match match;
    private boolean isGameStarter = false;

    public GameHandler(Socket cs) {
        requestManager = RequestManager.getInstance();
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
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex1) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex1);
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
    }

    public static void addInGamePlayer(GameHandler inGamePlayer) {
        inGameClients.add(inGamePlayer);
    }

    public boolean didStartGame() {
        return !(match == null);
    }

    public void playMove(int cellNumber, int playerTwoId) {
        switch (cellNumber) {
            case 0:
                boolean b1 = match.getBoard().placeMark(0, 0);
                match.getBoard().printGame();
                if (b1) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            //forward action to player 2
            case 1:
                boolean b2 = match.getBoard().placeMark(0, 1);
                match.getBoard().printGame();
                if (b2) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 2:
                boolean b3 = match.getBoard().placeMark(0, 2);
                match.getBoard().printGame();
                if (b3) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 3:
                boolean b4 = match.getBoard().placeMark(1, 0);
                match.getBoard().printGame();
                if (b4) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 4:
                boolean b5 = match.getBoard().placeMark(1, 1);
                match.getBoard().printGame();
                if (b5) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 5:
                boolean b6 = match.getBoard().placeMark(1, 2);
                match.getBoard().printGame();
                if (b6) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 6:
                boolean b7 = match.getBoard().placeMark(2, 0);
                match.getBoard().printGame();
                if (b7) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 7:
                boolean b8 = match.getBoard().placeMark(2, 1);
                match.getBoard().printGame();
                if (b8) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            case 8:
                boolean b9 = match.getBoard().placeMark(2, 2);
                match.getBoard().printGame();
                if (b9) {
                    forwardMoveToOpponent(playerTwoId, cellNumber);
                }
                break;

            default:
                System.out.println("Default value in play move switch");
        }

        if (match.getBoard().isGameOver()) {
            System.out.println("In GAME HANDLER RESULT");
            if (match.getBoard().getWinningMark() == Mark.X) {
                if (isGameStarter) {
                    this.ps.println("GameResult,Win," + match.getBoard().getBoardStatus());
                    for (GameHandler gh : GameHandler.onlineClients) {
                        if (gh.getID() == playerTwoId) {
                            gh.match = match;
                            gh.ps.println("GameResult,Lose," + match.getBoard().getBoardStatus());
                        }
                    }
                } else {
                    this.ps.println("GameResult,Lose," + match.getBoard().getBoardStatus());
                    for (GameHandler gh : GameHandler.onlineClients) {
                        if (gh.getID() == playerTwoId) {
                            gh.match = match;
                            gh.ps.println("GameResult,Win," + match.getBoard().getBoardStatus());
                        }
                    }

                }

            } else if (match.getBoard().getWinningMark() == Mark.O) {
                    
                  if (isGameStarter) {
                    this.ps.println("GameResult,Lose," + match.getBoard().getBoardStatus());
                    for (GameHandler gh : GameHandler.onlineClients) {
                        if (gh.getID() == playerTwoId) {
                            gh.match = match;
                            gh.ps.println("GameResult,Win," + match.getBoard().getBoardStatus());
                        }
                    }
                } else {
                    this.ps.println("GameResult,Win," + match.getBoard().getBoardStatus());
                    for (GameHandler gh : GameHandler.onlineClients) {
                        if (gh.getID() == playerTwoId) {
                            gh.match = match;
                            gh.ps.println("GameResult,Lose," + match.getBoard().getBoardStatus());
                        }
                    }

                }
                
                
                
            } else {
                               this.ps.println("GameResult,TIE," + match.getBoard().getBoardStatus());
                    for (GameHandler gh : GameHandler.onlineClients) {
                        if (gh.getID() == playerTwoId) {
                            gh.match = match;
                            gh.ps.println("GameResult,TIE," + match.getBoard().getBoardStatus());
                        }
                    }
            }

            System.out.println(match.getBoard().getWinningMark());
            System.out.println(match.getBoard().getBoardStatus());
        }

    }

    public void forwardMoveToOpponent(int opponentId, int cellNumber) {
        for (GameHandler gh : GameHandler.onlineClients) {
            if (gh.getID() == opponentId) {
                gh.match = match;
                System.out.println("Found opponent ");
                gh.ps.println("Move," + ID + "," + opponentId + "," + cellNumber);
            }
        }

    }
    
    public void getPlayerFromonlineVector(String s){
        for(int i = 0; i < GameHandler.onlineClients.size(); i++){
            if(GameHandler.onlineClients.get(i).ID == 2){
                   System.out.println("rrrrrrrrrrrrrrrrhhhhhhhhhhhhh");
                 GameHandler.onlineClients.get(i).ps.println("ChallengeRequests,"+"qqq");
                   GameHandler.onlineClients.get(i).ps.flush();
                 System.out.println(GameHandler.onlineClients.get(i).ps);
                   System.out.println("index = "+i);
            }
        }
        System.out.println("the number of onlineClient"+GameHandler.onlineClients.size());

    }
            
    public void closeConnections(){
        onlineClients.remove(this);
        System.out.println("from server: i closed the connection");
        try{
            if(this.dis != null){
                this.dis.close();
            }
            if(this.ps != null){
                this.ps.close();
            }
        }catch (IOException ex) {
            Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            
            try {
                //y2ra el string w y7wlo l msg ll client
                System.out.println("THE DIS IN THE GAMEHANDLER IS: "+dis);
                System.out.println("THE PS IN THE GAMEHANDLER IS: "+ps);
                String msg = dis.readLine();
                System.out.println("YESSSSSSSSSSSSSSSSSSSS "+msg);
                if(msg != null){
                    ServerAction action = requestManager.parse(msg);
                    String response = requestManager.process(action, this);
                    /*
                        for loop over all online players to end the  move to the right player
                     */
                    ps.println(response);
                }else{
                    System.out.println("CAN'T PARSE");
                }
            } catch (IOException ex) {
                closeConnections();
                break;
            }
        }
    }

}
