package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;

    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        ChessPiece that = (ChessPiece) object;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    public int hashCode() {
        return java.util.Objects.hash(pieceColor, pieceType);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", pieceType=" + pieceType +
                '}';
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;

    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        if (this.pieceType == PieceType.ROOK) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            int moveUp = row + 1;
            while (moveUp <= 8) {
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(moveUp, col));
                if (pieceAtSquare == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveUp, col), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveUp, col), null));
                    break;
                } else {
                    break;
                }

                moveUp++;
            }


            int moveDown = row - 1;
            while (moveDown >= 1) {
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(moveDown, col));
                if (pieceAtSquare == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveDown, col), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveDown, col), null));
                    break;
                } else {
                    break;
                }

                moveDown--;
            }

            int moveRight = col + 1;
            while (moveRight <= 8) {
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(row, moveRight));
                if (pieceAtSquare == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, moveRight), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, moveRight), null));
                    break;
                } else {
                    break;
                }

                moveRight++;
            }

            int moveLeft = col - 1;
            while (moveLeft >= 1) {
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(row, moveLeft));
                if (pieceAtSquare == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, moveLeft), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, moveLeft), null));
                    break;
                } else {
                    break;
                }

                moveLeft--;
            }
        }



        if (this.pieceType == PieceType.BISHOP){
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            int diagUp = row + 1;
            int diagRight = col + 1;
            while (diagUp <= 8 && diagRight <= 8){
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(diagUp, diagRight));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(diagUp, diagRight), null));
                } else if(pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(diagUp, diagRight), null));
                    break;
                } else{
                    break;
                }
                diagUp++;
                diagRight++;
            }
            int diagUp2 = row + 1;
            int diagLeft = col - 1;
            while (diagUp2 <= 8 && diagLeft >= 1){
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(diagUp2, diagLeft));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(diagUp2, diagLeft), null));
                } else if(pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(diagUp2, diagLeft), null));
                    break;

                } else{
                    break;
                }
                diagUp2 ++;
                diagLeft --;
            }

            int diagDown = row - 1;
            int diagLeft2 = col -1;

            while(diagDown >= 1 && diagLeft2 >= 1){
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(diagDown, diagLeft2));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(diagDown, diagLeft2), null));
                } else if(pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(diagDown, diagLeft2), null));
                    break;
                } else{
                    break;
                }
                diagDown --;
                diagLeft2 --;
            }

            int diagDown2 = row - 1;
            int diagRight2 = col + 1;
            while(diagDown2 >= 1 && diagRight2 <= 8){
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(diagDown2, diagRight2));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(diagDown2, diagRight2), null));

                } else if(pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(diagDown2, diagRight2), null));
                    break;
                } else{
                    break;
                }
                diagDown2 --;
                diagRight2 ++;
            }
        }

        if (this.pieceType == PieceType.QUEEN){
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            int QnRight = col + 1;

            while(QnRight <= 8){
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(row, QnRight));
                if(pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, QnRight), null));

                } else if(pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, QnRight), null));
                    break;
                } else{
                    break;
                }
                QnRight ++;

            }

            int QnLeft = col - 1;

            while (QnLeft >= 1){
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(row, QnLeft));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, QnLeft), null));

                } else if(pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, QnLeft), null));
                    break;
                } else{
                    break;
                }
                QnLeft --;
            }

            int QnUp = row + 1;

            while(QnUp <= 8){
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(QnUp, col));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnUp, col), null));

                } else if(pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnUp, col), null));
                    break;
                } else{
                    break;
                }
                QnUp ++;

            }
            int QnDown = row - 1;

            while(QnDown >= 1){
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(QnDown, col));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDown, col), null));

                } else if(pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDown, col), null));
                    break;
                } else{
                    break;
                }
                QnDown --;

            }

            int QnDiagUp = row + 1;
            int QnDiagRight = col + 1;

            while (QnDiagUp <= 8 && QnDiagRight <= 8){
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(QnDiagUp, QnDiagRight));
                if(pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagUp, QnDiagRight), null));

                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagUp, QnDiagRight), null));
                    break;
                } else{
                    break;
                }
                QnDiagUp ++;
                QnDiagRight ++;
            }

            int QnDiagUp2 = row + 1;
            int QnDiagLeft = col - 1;

            while (QnDiagUp2 <= 8 && QnDiagLeft >= 1){
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(QnDiagUp2, QnDiagLeft));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagUp2, QnDiagLeft), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagUp2, QnDiagLeft), null));
                    break;
                } else{
                    break;
                }

                QnDiagUp2 ++;
                QnDiagLeft --;

            }

            int QnDiagDown = row - 1;
            int QnDiagLeft2 = col - 1;

            while (QnDiagDown >= 1 && QnDiagLeft2 >= 1){
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(QnDiagDown, QnDiagLeft2));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagDown, QnDiagLeft2), null));

                } else if(pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagDown, QnDiagLeft2), null));
                    break;

                } else{
                    break;
                }
                QnDiagDown --;
                QnDiagLeft2 --;
            }

            int QnDiagDown2 = row - 1;
            int QnDiagRight2 = col + 1;

            while (QnDiagDown2 >= 1 && QnDiagRight2 <= 8){
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(QnDiagDown2, QnDiagRight2));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagDown2, QnDiagRight2), null));

                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagDown2, QnDiagRight2), null));
                    break;
                } else{
                    break;
                }
                QnDiagDown2 --;
                QnDiagRight2 ++;
            }

        }



        return moves;


    }
}
