package tictactoe;


import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);


		char[][] boardaaa = new char[3][3];
		for (int i = 0; i < 9; i++) {
			boardaaa[i / 3][i % 3] = '_';
		}
		final Board board = new Board();
		board.showBoard();

		int n = 0;
		while (true) {

			do {
				final String inputCoordinates = scan.nextLine();
				System.out.println("Enter the coordinates: > " + inputCoordinates);
				final String[] s = inputCoordinates.split(" ");
				if (s.length != 2 || !Arrays.stream(s).allMatch(s1 -> s1.matches("\\d"))) {
					System.out.println("You should enter numbers!");
					continue;
				}
				final int[] ints = Arrays.stream(s).mapToInt(Integer::parseInt).toArray();
				if (n % 2 == 0) {
					if (board.setX(ints[1], ints[0]))
						break;
				} else {
					if (board.setO(ints[1], ints[0])) {
						break;
					}
				}
			} while (true);

			n++;
			board.showBoard();
			final BoardStatus boardStatus = board.judgmentBoardStatus();
			if (!BoardStatus.GAME_NOT_FINISHED.equals(boardStatus)) {
				System.out.println(boardStatus.getMessage());
				break;
			}

		}


	}


}

class Board {
	public static final char X = 'X';
	public static final char O = 'O';
	private char[][] board = new char[3][3];

	Board() {
		for (int i = 0; i < 9; i++) {
			board[i / 3][i % 3] = '_';
		}
	}

	public boolean setX(int x, int y) {
		return setMark(x, y, X);
	}

	public boolean setO(int x, int y) {
		return setMark(x, y, O);
	}

	private boolean setMark(int x, int y, char mark) {
		if (x < 1 || x > 3 || y < 1 || y > 3) {
			System.out.println("Coordinates should be from 1 to 3!");
			return false;
		}
		if (board[y - 1][x - 1] != '_') {
			System.out.println("This cell is occupied! Choose another one!");
			return false;
		}
		board[y - 1][x - 1] = mark;
		return true;
	}

	public void showBoard() {
		System.out.println("---------");
		final String format = "| %s %s %s |\n";
		System.out.printf(format, board[2][0], board[2][1], board[2][2]);
		System.out.printf(format, board[1][0], board[1][1], board[1][2]);
		System.out.printf(format, board[0][0], board[0][1], board[0][2]);
		System.out.println("---------");
	}

	public BoardStatus judgmentBoardStatus() {
		final boolean winX = judgmentWin(X);
		final boolean winO = judgmentWin(O);
		if (winX && winO || Math.abs(countMark(X) - countMark(O)) >= 2) {
			return BoardStatus.IMPOSSIBLE;
		} else if (winX) {
			return BoardStatus.X_WINS;
		} else if (winO) {
			return BoardStatus.O_WINS;
		} else {
			if (countMark('_') > 0) {
				return BoardStatus.GAME_NOT_FINISHED;
			} else {
				return BoardStatus.DRAW;
			}

		}
	}

	private int countMark(char mark) {
		return Arrays.stream(board).mapToInt(chars -> {
			int n = 0;
			for (final char aChar : chars) {
				if (mark == aChar) {
					n++;
				}
			}
			return n;
		}).sum();
	}

	private boolean judgmentWin(char mark) {
		for (int i = 0; i < 3; i++) {
			if (Stream.of(board[i][0], board[i][1], board[i][2]).allMatch(character -> character.equals(mark))) {
				return true;
			}
			if (Stream.of(board[0][i], board[1][i], board[2][i]).allMatch(character -> character.equals(mark))) {
				return true;
			}
		}
		if (Stream.of(board[0][0], board[1][1], board[2][2]).allMatch(character -> character.equals(mark))) {
			return true;
		}
		if (Stream.of(board[0][2], board[1][1], board[2][0]).allMatch(character -> character.equals(mark))) {
			return true;
		}
		return false;
	}
}

enum BoardStatus {
	X_WINS("X wins"),
	O_WINS("O wins"),
	IMPOSSIBLE("Impossible"),
	GAME_NOT_FINISHED("Game not finished"),
	DRAW("Draw");

	BoardStatus(String message) {
		this.message = message;
	}

	private final String message;

	public String getMessage() {
		return message;
	}
}

