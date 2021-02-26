package net.eno.chess;

import net.eno.pieces.Color;
import net.eno.pieces.Piece;
import net.eno.pieces.PieceType;
import net.eno.pieces.Position;

import static net.eno.utils.StringUtils.appendNewLine;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Board {

    private List<Rank> board;

    public Board() {
        this.board = new ArrayList<>();
    }

    private void addRank(Rank rank) {
        this.board.add(rank);
    }

    public int countTargetPiece(Color color, PieceType pieceType) {
        return this.board.stream()
                .mapToInt(rank -> rank.countTargetPiece(color, pieceType))
                .sum();
    }

    public Piece findPiece(String position) {
        Position positionObj = new Position(position);
        Rank targetRank = this.board.get(positionObj.getRankIndex());
        return targetRank.findPiece(positionObj.getFileIndex());
    }

    public void move(String position, Piece piece) {
        Position positionObj = new Position(position);
        Rank targetRank = this.board.get(positionObj.getRankIndex());
        targetRank.move(positionObj.getFileIndex(), piece);
    }

    public double calculatePoint(Color color) {
        double point = this.board.stream()
                .mapToDouble(rank -> rank.calculateRankPoint(color))
                .sum();
        double pawnNumber = IntStream.rangeClosed('a', 'h')
                .mapToDouble(file -> countSameFilePawn((char)file, color))
                .sum();
        return point - (pawnNumber / 2);
    }

    private int countSameFilePawn(char file, Color color) {
        Piece pawn = Piece.createPiece(color, PieceType.PAWN);
        int count = (int)IntStream.rangeClosed(1, 8)
                .mapToObj(rank -> findPiece(String.valueOf(file) + rank))
                .filter(piece -> piece.equals(pawn))
                .count();
        return count > 1 ? count : 0;
    }

    public List<Piece> sortByPiecePoint(Color color, boolean isAscending) {
        List<Piece> pieceList = this.board.stream()
                .map(rank -> rank.getPieceListByColor(color))
                .flatMap(List::stream)
                .sorted()
                .collect(Collectors.toList());
        if (!isAscending) {
            Collections.reverse(pieceList);
        }
        return pieceList;
    }

    public void initialize() {
        this.board = new ArrayList<>();
        addRank(Rank.createMultiplePieceRank(Color.BLACK));
        addRank(Rank.createOnePieceRank(Color.BLACK, PieceType.PAWN));
        addRank(Rank.createOnePieceRank(Color.NOCOLOR, PieceType.NO_PIECE));
        addRank(Rank.createOnePieceRank(Color.NOCOLOR, PieceType.NO_PIECE));
        addRank(Rank.createOnePieceRank(Color.NOCOLOR, PieceType.NO_PIECE));
        addRank(Rank.createOnePieceRank(Color.NOCOLOR, PieceType.NO_PIECE));
        addRank(Rank.createOnePieceRank(Color.WHITE, PieceType.PAWN));
        addRank(Rank.createMultiplePieceRank(Color.WHITE));
    }

    public void initializeEmpty() {
        this.board = new ArrayList<>();
        IntStream.range(0, 8)
                .forEach(i -> addRank(Rank.createOnePieceRank(Color.NOCOLOR, PieceType.NO_PIECE)));
    }

    public String showBoard(Color color) {
        boolean isBlack = color == Color.BLACK;
        return IntStream.range(0, 8)
                .mapToObj(i -> isBlack ? this.board.get(7 - i) : this.board.get(i))
                .map(rank -> appendNewLine(rank.showRank(color)))
                .collect(Collectors.joining());
    }

}
