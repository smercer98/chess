package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardPrinter {
    private static final String[] COLUMN_LETTERS = {"a", "b", "c", "d", "e", "f", "g", "h"};

    public static String boardToString(ChessBoard board, ChessGame.TeamColor perspective) {
        boolean whitePerspective = perspective != ChessGame.TeamColor.BLACK;
        var sb = new StringBuilder();

        sb.append(columnHeaders(whitePerspective));
        for (int displayRow = 0; displayRow < 8; displayRow++) {
            int row = whitePerspective ? 8 - displayRow : displayRow + 1;
            sb.append(rowToString(board, row, whitePerspective));
        }
        sb.append(columnHeaders(whitePerspective));

        return sb.toString();
    }

    private static String columnHeaders(boolean whitePerspective) {
        var sb = new StringBuilder(EscapeSequences.EMPTY);
        for (int i = 0; i < 8; i++) {
            int letterIndex = whitePerspective ? i : 7 - i;
            sb.append(" ").append(COLUMN_LETTERS[letterIndex]).append(" ");
        }
        sb.append(EscapeSequences.EMPTY).append("\n");
        return sb.toString();
    }

    private static String rowToString(ChessBoard board, int row, boolean whitePerspective) {
        var sb = new StringBuilder(" " + row + " ");
        for (int i = 0; i < 8; i++) {
            int col = whitePerspective ? i + 1 : 8 - i;
            boolean isLight = (row + col) % 2 != 0;
            sb.append(isLight ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_DARK_GREY);
            sb.append(pieceToString(board.getPiece(new ChessPosition(row, col))));
        }
        sb.append(EscapeSequences.RESET_BG_COLOR).append(" ").append(row).append(" \n");
        return sb.toString();
    }

    private static String pieceToString(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.EMPTY;
        }
        boolean isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        return switch (piece.getPieceType()) {
            case KING -> isWhite ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
            case QUEEN -> isWhite ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
            case BISHOP -> isWhite ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> isWhite ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            case ROOK -> isWhite ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
            case PAWN -> isWhite ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
        };
    }
}