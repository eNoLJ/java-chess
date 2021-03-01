package net.eno.chess;

import net.eno.pieces.*;

import java.util.*;
import java.util.stream.*;

public class Rank {

    private final List<Piece> rank;

    private Rank() {
        rank = new ArrayList<>();
    }

    public static Rank createOnePieceRank(Color color, PieceType pieceType) {
        Rank rank = new Rank();
        IntStream.range(0, 8)
                .forEach(i -> rank.addPiece((Piece.createPiece(color, pieceType))));
        return rank;
    }

    public static Rank createMultiplePieceRank(Color color) {
        Rank rank = new Rank();
        rank.addPiece(Piece.createPiece(color, PieceType.ROOK));
        rank.addPiece(Piece.createPiece(color, PieceType.KNIGHT));
        rank.addPiece(Piece.createPiece(color, PieceType.BISHOP));
        rank.addPiece(Piece.createPiece(color, PieceType.QUEEN));
        rank.addPiece(Piece.createPiece(color, PieceType.KING));
        rank.addPiece(Piece.createPiece(color, PieceType.BISHOP));
        rank.addPiece(Piece.createPiece(color, PieceType.KNIGHT));
        rank.addPiece(Piece.createPiece(color, PieceType.ROOK));
        return rank;
    }

    private void addPiece(Piece piece) {
        rank.add(piece);
    }

    public Piece findPiece(Position position) {
        return this.rank.get(position.getFileIndex());
    }

    public void setPiece(Position position, Piece piece) {
        this.rank.set(position.getFileIndex(), piece);
    }

    public double calculateRankPoint(Color color) {
        return this.rank.stream()
                .filter(piece -> piece.getColor() == color)
                .mapToDouble(Piece::getPoint)
                .sum();
    }

    public int countTargetPiece(Color color, PieceType pieceType) {
        Piece targetPiece = Piece.createPiece(color, pieceType);
        return (int)this.rank.stream()
                .filter(piece -> piece.equals(targetPiece))
                .count();
    }

    public List<Piece> getPieceListByColor(Color color) {
        return this.rank.stream()
                .filter(piece -> piece.getColor() == color)
                .collect(Collectors.toList());
    }

    public String showRank(Color color) {
        boolean isBlack = color == Color.BLACK;
        int rankSize = this.rank.size();
        return IntStream.range(0, rankSize)
                .mapToObj(i -> isBlack ? this.rank.get((rankSize - 1) - i) : this.rank.get(i))
                .map(piece -> String.valueOf(piece.getRepresentation(piece.getColor())))
                .collect(Collectors.joining());
    }

}
