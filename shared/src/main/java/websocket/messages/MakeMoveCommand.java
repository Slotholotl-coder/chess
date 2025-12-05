package websocket.messages;

import chess.ChessMove;

public class MakeMoveCommand extends ServerMessage {

    ChessMove chessMove;

    public MakeMoveCommand(ServerMessageType type) {
        super(type);
    }

    public void setChessMove(ChessMove chessMove){
        this.chessMove = chessMove;
    }

    public ChessMove getChessMove(){
        return chessMove;
    }

}