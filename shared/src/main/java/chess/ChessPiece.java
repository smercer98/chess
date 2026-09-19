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
        return moves;

    }
}
