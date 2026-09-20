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

        if (this.pieceType == PieceType.QUEEN) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            int QnRight = col + 1;

            while (QnRight <= 8) {
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(row, QnRight));
                if (pieceAtSquare == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, QnRight), null));

                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, QnRight), null));
                    break;
                } else {
                    break;
                }
                QnRight++;

            }

            int QnLeft = col - 1;

            while (QnLeft >= 1) {
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(row, QnLeft));
                if (pieceAtSquare == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, QnLeft), null));

                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, QnLeft), null));
                    break;
                } else {
                    break;
                }
                QnLeft--;
            }

            int QnUp = row + 1;

            while (QnUp <= 8) {
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(QnUp, col));
                if (pieceAtSquare == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnUp, col), null));

                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnUp, col), null));
                    break;
                } else {
                    break;
                }
                QnUp++;

            }
            int QnDown = row - 1;

            while (QnDown >= 1) {
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(QnDown, col));
                if (pieceAtSquare == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDown, col), null));

                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDown, col), null));
                    break;
                } else {
                    break;
                }
                QnDown--;

            }

            int QnDiagUp = row + 1;
            int QnDiagRight = col + 1;

            while (QnDiagUp <= 8 && QnDiagRight <= 8) {
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(QnDiagUp, QnDiagRight));
                if (pieceAtSquare == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagUp, QnDiagRight), null));

                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagUp, QnDiagRight), null));
                    break;
                } else {
                    break;
                }
                QnDiagUp++;
                QnDiagRight++;
            }

            int QnDiagUp2 = row + 1;
            int QnDiagLeft = col - 1;

            while (QnDiagUp2 <= 8 && QnDiagLeft >= 1) {
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(QnDiagUp2, QnDiagLeft));
                if (pieceAtSquare == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagUp2, QnDiagLeft), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagUp2, QnDiagLeft), null));
                    break;
                } else {
                    break;
                }

                QnDiagUp2++;
                QnDiagLeft--;

            }

            int QnDiagDown = row - 1;
            int QnDiagLeft2 = col - 1;

            while (QnDiagDown >= 1 && QnDiagLeft2 >= 1) {
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(QnDiagDown, QnDiagLeft2));
                if (pieceAtSquare == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagDown, QnDiagLeft2), null));

                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagDown, QnDiagLeft2), null));
                    break;

                } else {
                    break;
                }
                QnDiagDown--;
                QnDiagLeft2--;
            }

            int QnDiagDown2 = row - 1;
            int QnDiagRight2 = col + 1;

            while (QnDiagDown2 >= 1 && QnDiagRight2 <= 8) {
                ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(QnDiagDown2, QnDiagRight2));
                if (pieceAtSquare == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagDown2, QnDiagRight2), null));

                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(QnDiagDown2, QnDiagRight2), null));
                    break;
                } else {
                    break;
                }
                QnDiagDown2--;
                QnDiagRight2++;

            }


            }

            if (this.pieceType == chess.ChessPiece.PieceType.KNIGHT){
                int row = myPosition.getRow();
                int col = myPosition.getColumn();

                int checkRow = row + 2;
                int checkCol = col + 1;

                if (checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8){
                    chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(checkRow, checkCol));
                    if(pieceAtSquare == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow, checkCol), null));

                    } else if(pieceAtSquare.getTeamColor() != this.getTeamColor()){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow, checkCol), null));
                    }
                }

                int checkRow2 = row + 2;
                int checkCol2 = col - 1;

                if (checkRow2 >= 1 && checkRow2 <= 8 && checkCol2 >= 1 && checkCol2 <= 8){
                    chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(checkRow2, checkCol2));
                    if (pieceAtSquare == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow2, checkCol2), null));
                    } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow2, checkCol2), null));
                    }
                }

                int checkRow3 = row - 2;
                int checkCol3 = col + 1;

                if (checkRow3 >= 1 && checkRow3 <= 8 && checkCol3 >= 1 && checkCol3 <= 8){
                    chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(checkRow3, checkCol3));
                    if (pieceAtSquare == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow3, checkCol3), null));
                    } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow3, checkCol3), null));
                    }
                }

                int checkRow4 = row - 2;
                int checkCol4 = col - 1;

                if (checkRow4 >= 1 && checkRow4 <= 8 && checkCol4 >= 1 && checkCol4 <= 8){
                    chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(checkRow4, checkCol4));
                    if (pieceAtSquare == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow4, checkCol4), null));
                    } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow4, checkCol4), null));
                    }
                }

                int checkRow5 = row + 1;
                int checkCol5 = col + 2;

                if (checkRow5 >= 1 && checkRow5 <= 8 && checkCol5 >= 1 && checkCol5 <= 8){
                    chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(checkRow5, checkCol5));
                    if (pieceAtSquare == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow5, checkCol5), null));
                    } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow5, checkCol5), null));
                    }
                }

                int checkRow6 = row - 1;
                int checkCol6 = col + 2;

                if (checkRow6 >= 1 && checkRow6 <= 8 && checkCol6 >= 1 && checkCol6 <= 8){
                    chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(checkRow6, checkCol6));
                    if (pieceAtSquare == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow6, checkCol6), null));
                    } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow6, checkCol6), null));
                    }
                }

                int checkRow7 = row + 1;
                int checkCol7 = col - 2;

                if (checkRow7 >= 1 && checkRow7 <= 8 && checkCol7 >= 1 && checkCol7 <= 8){
                    chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(checkRow7, checkCol7));
                    if (pieceAtSquare == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow7, checkCol7), null));
                    } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow7, checkCol7), null));
                    }
                }

                int checkRow8 = row - 1;
                int checkCol8 = col - 2;

                if (checkRow8 >= 1 && checkRow8 <= 8 && checkCol8 >= 1 && checkCol8 <= 8){
                    chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(checkRow8, checkCol8));
                    if (pieceAtSquare == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow8, checkCol8), null));
                    } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                        moves.add(new ChessMove(myPosition, new ChessPosition(checkRow8, checkCol8), null));
                    }
                }

                }

        if (this.pieceType == PieceType.KING){
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            int moveRow = row + 1;
            int moveCol = col;

            if (moveRow >= 1 && moveRow <= 8 && moveCol >= 1 && moveCol <= 8){

                chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(moveRow, moveCol));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, moveCol), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow, moveCol), null));
                }
            }


            int moveRow2 = row - 1;
            int moveCol2 = col;

            if (moveRow2 >= 1 && moveRow2 <= 8 && moveCol2 >= 1 && moveCol2 <= 8){

                chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(moveRow2, moveCol2));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow2, moveCol2), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow2, moveCol2), null));
                }
            }


            int moveRow3 = row;
            int moveCol3 = col + 1;

            if (moveRow3 >= 1 && moveRow3 <= 8 && moveCol3 >= 1 && moveCol3 <= 8){

                chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(moveRow3, moveCol3));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow3, moveCol3), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow3, moveCol3), null));
                }
            }


            int moveRow4 = row;
            int moveCol4 = col - 1;

            if (moveRow4 >= 1 && moveRow4 <= 8 && moveCol4 >= 1 && moveCol4 <= 8){

                chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(moveRow4, moveCol4));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow4, moveCol4), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow4, moveCol4), null));
                }
            }


            int moveRow5 = row + 1;
            int moveCol5 = col + 1;

            if (moveRow5 >= 1 && moveRow5 <= 8 && moveCol5 >= 1 && moveCol5 <= 8){

                chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(moveRow5, moveCol5));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow5, moveCol5), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow5, moveCol5), null));
                }
            }


            int moveRow6 = row + 1;
            int moveCol6 = col - 1;

            if (moveRow6 >= 1 && moveRow6 <= 8 && moveCol6 >= 1 && moveCol6 <= 8){

                chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(moveRow6, moveCol6));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow6, moveCol6), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow6, moveCol6), null));
                }
            }


            int moveRow7 = row - 1;
            int moveCol7 = col + 1;

            if (moveRow7 >= 1 && moveRow7 <= 8 && moveCol7 >= 1 && moveCol7 <= 8){

                chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(moveRow7, moveCol7));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow7, moveCol7), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow7, moveCol7), null));
                }

            }


            int moveRow8 = row - 1;
            int moveCol8 = col - 1;

            if (moveRow8 >= 1 && moveRow8 <= 8 && moveCol8 >= 1 && moveCol8 <= 8){

                chess.ChessPiece pieceAtSquare = board.getPiece(new ChessPosition(moveRow8, moveCol8));
                if (pieceAtSquare == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow8, moveCol8), null));
                } else if (pieceAtSquare.getTeamColor() != this.getTeamColor()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(moveRow8, moveCol8), null));
                }

            }

        }

        if (this.pieceType == PieceType.PAWN){
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            int direction;
            int startRow;
            int promotionRow;

            if (this.getTeamColor() == ChessGame.TeamColor.WHITE){
                direction = 1;
                startRow = 2;
                promotionRow = 8;
            } else{
                direction = -1;
                startRow = 7;
                promotionRow = 1;
            }

            int oneForwardRow = row + direction;

            if (oneForwardRow >= 1 && oneForwardRow <= 8){
                ChessPiece pieceAhead = board.getPiece(new ChessPosition(oneForwardRow, col));
                if (pieceAhead == null){
                    if (oneForwardRow == promotionRow){
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, col), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, col), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, col), PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, col), PieceType.KNIGHT));
                    } else{
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, col), null));
                    }

                    if (row == startRow){
                        int twoForwardRow = row + (2 * direction);
                        ChessPiece pieceTwoAhead = board.getPiece(new ChessPosition(twoForwardRow, col));
                        if (pieceTwoAhead == null){
                            moves.add(new ChessMove(myPosition, new ChessPosition(twoForwardRow, col), null));
                        }
                    }
                }

            }

            int captureColLeft = col - 1;
            if (oneForwardRow >= 1 && oneForwardRow <= 8 && captureColLeft >= 1 && captureColLeft <= 8) {
                ChessPiece pieceDiag = board.getPiece(new ChessPosition(oneForwardRow, captureColLeft));
                if (pieceDiag != null && pieceDiag.getTeamColor() != this.getTeamColor()) {
                    if (oneForwardRow == promotionRow) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, captureColLeft), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, captureColLeft), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, captureColLeft), PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, captureColLeft), PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, captureColLeft), null));
                    }
                }
            }

            int captureColRight = col + 1;
            if (oneForwardRow >= 1 && oneForwardRow <= 8 && captureColRight >= 1 && captureColRight <= 8) {
                ChessPiece pieceDiag = board.getPiece(new ChessPosition(oneForwardRow, captureColRight));
                if (pieceDiag != null && pieceDiag.getTeamColor() != this.getTeamColor()) {
                    if (oneForwardRow == promotionRow) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, captureColRight), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, captureColRight), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, captureColRight), PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, captureColRight), PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(oneForwardRow, captureColRight), null));
                    }
                }
            }
        }










        return moves;


    }
}
