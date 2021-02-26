package net.eno.chess;

import net.eno.pieces.*;

import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        System.out.println("> start를 입력하면 체스 게임이 시작됩니다.");
        System.out.println("> 게임이 시작되면 move 명령어를 통해 기물을 이동할 수 있습니다.");
        System.out.println("> ex) move a1 a2");
        System.out.println("> end를 입력하면 체스 게임이 종료됩니다.");

        Scanner sc = new Scanner(System.in);
        Board board = new Board();
        boolean isStart = false;

        while (true) {
            System.out.print("> ");
            String command = sc.nextLine();

            if (command.equals("start")) {
                System.out.println("> 체스 게임을 시작합니다.");
                isStart = true;
                board.initialize();
                System.out.println(board.showBoard(Color.WHITE));
            }

            if (isStart && command.startsWith("move")) {
                String[] splitCommand = command.split(" ");
                Position sourcePosition = new Position(splitCommand[1]);
                Position targetPosition = new Position(splitCommand[2]);

                board.movePiece(sourcePosition, targetPosition);
                System.out.println(board.showBoard(Color.WHITE));
            }

            if (isStart && command.equals("end")) {
                System.out.println("> 체스 게임을 종료합니다.");
                sc.close();
                break;
            }
        }
    }

}
