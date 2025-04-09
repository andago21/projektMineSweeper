import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Minesweeper extends JFrame {
    private boolean[][] mines;
    private boolean firstClick = true;
    private int revealedCells = 0;
    private int flaggedMines = 0;

    // Game logic methods
    private void placeMines(int safeRow, int safeCol) {
        Random random = new Random();
        int placedMines = 0;
        while (placedMines < MINES) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (!mines[row][col] && (row != safeRow || col != safeCol)) {
                mines[row][col] = true;
                placedMines++;
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int r = row + i, c = col + j;
                if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && mines[r][c]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void revealCell(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || cells[row][col].isRevealed()) return;
        cells[row][col].reveal();
        revealedCells++;
        int adjacentMines = countAdjacentMines(row, col);
        if (adjacentMines > 0) {
            cells[row][col].setText(String.valueOf(adjacentMines));
            cells[row][col].setForeground(getColorForNumber(adjacentMines));
        } else {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    revealCell(row + i, col + j);
                }
            }
        }
        checkWin();
    }

    private Color getColorForNumber(int number) {
        switch (number) {
            case 1: return Color.BLUE;
            case 2: return Color.GREEN;
            case 3: return Color.RED;
            case 4: return Color.MAGENTA;
            case 5: return Color.ORANGE;
            case 6: return Color.CYAN;
            case 7: return Color.BLACK;
            case 8: return Color.GRAY;
            default: return Color.BLACK;
        }
    }

    private void checkWin() {
        if (flaggedMines == MINES && revealedCells == SIZE * SIZE - MINES) {
            statusLabel.setText("You win!");
            disableAllCells();
        }
    }

    private void gameOver() {
        statusLabel.setText("Game Over!");
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (mines[row][col]) {
                    cells[row][col].setText("X");
                }
                cells[row][col].setEnabled(false);
            }
        }
    }

    private void disableAllCells() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setEnabled(false);
            }
        }
    }

    private void resetGame() {
        firstClick = true;
        revealedCells = 0;
        flaggedMines = 0;
        statusLabel.setText("Status: Playing");
        mineCounterLabel.setText("Mines: " + MINES);
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].reset();
                mines[row][col] = false;
            }
        }
    }

    private class Cell extends JButton {
        private int row, col;
        private boolean revealed = false;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
            setFont(new Font("Arial", Font.BOLD, 18));
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        toggleFlag();
                    } else {
                        handleClick();
                    }
                }
            });
        }

        private void toggleFlag() {
            if (!revealed) {
                if (getText().equals("F")) {
                    setText("");
                    flaggedMines--;
                } else {
                    setText("F");
                    flaggedMines++;
                }
                checkWin();
            }
        }

        private void handleClick() {
            if (revealed || getText().equals("F")) return;
            if (firstClick) {
                placeMines(row, col);
                firstClick = false;
            }
            if (mines[row][col]) {
                gameOver();
            } else {
                revealCell(row, col);
            }
        }

        public void reveal() {
            revealed = true;
            setEnabled(false);
            setBackground(Color.LIGHT_GRAY);
        }

        public boolean isRevealed() {
            return revealed;
        }

        public void reset() {
            setText("");
            setEnabled(true);
            revealed = false;
            setBackground(null);
        }
    }
}
