package net.eno.chess;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import net.eno.pieces.*;

import static net.eno.utils.StringUtils.appendNewLine;

public class BoardTest {

    private Board board;

    @BeforeEach
    public void init() {
        board = new Board();
        board.initialize();
    }

    @Test
    @DisplayName("초기화 된 보드의 기물의 배치가 일치해야 한다.")
    public void initialize() {
        String blankRank = appendNewLine("........");
        assertThat(board.showBoard(Color.WHITE)).isEqualTo(
                appendNewLine("RNBQKBNR") +
                appendNewLine("PPPPPPPP") +
                blankRank + blankRank + blankRank + blankRank +
                appendNewLine("pppppppp") +
                appendNewLine("rnbqkbnr")
        );
        assertThat(board.showBoard(Color.BLACK)).isEqualTo(
                appendNewLine("rnbkqbnr") +
                appendNewLine("pppppppp") +
                blankRank + blankRank + blankRank + blankRank +
                appendNewLine("PPPPPPPP") +
                appendNewLine("RNBKQBNR")
        );
    }

    @Test
    @DisplayName("초기화 된 보드가 출력되어야 한다.")
    public void print() {
        System.out.println("> 초기화 된 보드가 출력되어야 한다.");
        System.out.println(board.showBoard(Color.WHITE));
        System.out.println(board.showBoard(Color.BLACK));
    }

    @Test
    @DisplayName("타입 별 기물의 개수가 일치해야 한다.")
    public void count() {
        assertThat(board.countTargetPiece(Color.WHITE, PieceType.PAWN)).isEqualTo(8);
        assertThat(board.countTargetPiece(Color.BLACK, PieceType.ROOK)).isEqualTo(2);
        assertThat(board.countTargetPiece(Color.WHITE, PieceType.KNIGHT)).isEqualTo(2);
        assertThat(board.countTargetPiece(Color.BLACK, PieceType.BISHOP)).isEqualTo(2);
        assertThat(board.countTargetPiece(Color.WHITE, PieceType.QUEEN)).isEqualTo(1);
        assertThat(board.countTargetPiece(Color.BLACK, PieceType.KING)).isEqualTo(1);
        assertThat(board.countTargetPiece(Color.NOCOLOR, PieceType.NO_PIECE)).isEqualTo(32);
    }

    @Test
    @DisplayName("체스판에 해당하는 좌표의 기물을 가져올 수 있어야 한다.")
    public void findPiece() {
        assertThat(board.findPiece(new Position("a8"))).isEqualTo(Piece.createPiece(Color.BLACK, PieceType.ROOK));
        assertThat(board.findPiece(new Position("h8"))).isEqualTo(Piece.createPiece(Color.BLACK, PieceType.ROOK));
        assertThat(board.findPiece(new Position("a1"))).isEqualTo(Piece.createPiece(Color.WHITE, PieceType.ROOK));
        assertThat(board.findPiece(new Position("h1"))).isEqualTo(Piece.createPiece(Color.WHITE, PieceType.ROOK));
    }

    @Test
    @DisplayName("체스판 위에 기물들이 점수별로 정렬되어야 한다.")
    public void sortByPoint() {
        System.out.println("> 체스판 위에 기물들이 점수별로 정렬되어야 한다.");
        board.sortByPiecePoint(Color.WHITE, true)
                .forEach(piece -> System.out.println(piece.getColor() + " " + piece.getPieceType() + " " + piece.getPoint()));
        System.out.println();
        board.sortByPiecePoint(Color.WHITE, false)
                .forEach(piece -> System.out.println(piece.getColor() + " " + piece.getPieceType() + " " + piece.getPoint()));
    }

    @Test
    @DisplayName("체스판 위에 흰색, 검은색 기물의 점수가 일치해야 한다.")
    public void calculatePoint()  {
        System.out.println("> 체스판 위에 흰색, 검은색 기물의 점수가 일치해야 한다.");
        board.initializeEmpty();

        board.setPiece(new Position("b6"), Piece.createPiece(Color.BLACK, PieceType.PAWN));
        board.setPiece(new Position("e6"), Piece.createPiece(Color.BLACK, PieceType.QUEEN));
        board.setPiece(new Position("b8"), Piece.createPiece(Color.BLACK, PieceType.KING));
        board.setPiece(new Position("c8"), Piece.createPiece(Color.BLACK, PieceType.ROOK));

        board.setPiece(new Position("f2"), Piece.createPiece(Color.WHITE, PieceType.PAWN));
        board.setPiece(new Position("f3"), Piece.createPiece(Color.WHITE, PieceType.PAWN));
        board.setPiece(new Position("e1"), Piece.createPiece(Color.WHITE, PieceType.ROOK));
        board.setPiece(new Position("f1"), Piece.createPiece(Color.WHITE, PieceType.KING));

        assertThat(board.calculatePoint(Color.BLACK)).isEqualTo(15.0);
        assertThat(board.calculatePoint(Color.WHITE)).isEqualTo(6.0);

        System.out.println(board.showBoard(Color.WHITE));
    }

    @Test
    @DisplayName("체스판에 기물을 이동할 수 있어야 한다.")
    public void move() {
        System.out.println("> 체스판에 기물을 이동할 수 있어야 한다.");
        Position sourcePosition = new Position("b2");
        Position targetPosition = new Position("b3");

        board.movePiece(sourcePosition, targetPosition);
        assertThat(board.findPiece(sourcePosition)).isEqualTo(Piece.createPiece(Color.NOCOLOR, PieceType.NO_PIECE));
        assertThat(board.findPiece(targetPosition)).isEqualTo(Piece.createPiece(Color.WHITE, PieceType.PAWN));

        System.out.println(board.showBoard(Color.WHITE));
    }

}
