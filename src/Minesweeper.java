import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Minesweeper extends JFrame {
    private final int SIZE = 10;
    private final int MINES = 15;
    private Cell[][] cells;
    private JLabel statusLabel;
    private JLabel mineCounterLabel;

    public Minesweeper() {
        setTitle("Minesweeper");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel for status and mine count
        JPanel topPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Status: Playing");
        mineCounterLabel = new JLabel("Mines: " + MINES);
        topPanel.add(mineCounterLabel, BorderLayout.WEST);
        topPanel.add(statusLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Game grid panel
        JPanel panel = new JPanel(new GridLayout(SIZE, SIZE));
        cells = new Cell[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new Cell(row, col);
                panel.add(cells[row][col]);
            }
        }
        add(panel, BorderLayout.CENTER);

        // Restart button
        JButton resetButton = new JButton("Restart");
        resetButton.addActionListener(e -> resetGame());
        add(resetButton, BorderLayout.SOUTH);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Minesweeper().setVisible(true));
    }
}