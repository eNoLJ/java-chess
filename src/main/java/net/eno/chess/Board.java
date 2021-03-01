package net.eno.chess;

import net.eno.pieces.*;

import static net.eno.utils.StringUtils.appendNewLine;

import java.util.*;
import java.util.stream.*;


public class Board {

    private List<Rank> board;

    public Board() {
        this.board = new ArrayList<>();
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

    private void addRank(Rank rank) {
        this.board.add(rank);
    }

    public Piece findPiece(Position position) {
        Rank targetRank = findRank(position);
        return targetRank.findPiece(position);
    }

    private Rank findRank(Position position) {
        return this.board.get(position.getRankIndex());
    }

    public void movePiece(Position sourcePosition, Position targetPosition) {
        Piece sourcePiece = findPiece(sourcePosition);
        setPiece(targetPosition, sourcePiece);
        Piece blankPiece = Piece.createPiece(Color.NOCOLOR, PieceType.NO_PIECE);
        setPiece(sourcePosition, blankPiece);
    }

    public void setPiece(Position position, Piece piece) {
        Rank targetRank = findRank(position);
        targetRank.setPiece(position, piece);
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
                .mapToObj(rank -> findPiece(new Position(String.valueOf(file) + rank)))
                .filter(piece -> piece.equals(pawn))
                .count();
        return (count > 1) ? count : 0;
    }

    public int countTargetPiece(Color color, PieceType pieceType) {
        return this.board.stream()
                .mapToInt(rank -> rank.countTargetPiece(color, pieceType))
                .sum();
    }

    public List<Piece> sortByPiecePoint(Color color, boolean isAscending) {
        List<Piece> pieceList = this.board.stream()
                .map(rank -> rank.getPieceListByColor(color))
                .flatMap(List::stream)
                .sorted(Comparator.comparingDouble(Piece::getPoint))
                .collect(Collectors.toList());
        if (!isAscending) {
            Collections.reverse(pieceList);
        }
        return pieceList;
    }

    public String showBoard(Color color) {
        boolean isBlack = color == Color.BLACK;
        int boardSize = this.board.size();
        return IntStream.range(0, boardSize)
                .mapToObj(i -> isBlack ? this.board.get((boardSize - 1) - i) : this.board.get(i))
                .map(rank -> appendNewLine(rank.showRank(color)))
                .collect(Collectors.joining());
    }

}
