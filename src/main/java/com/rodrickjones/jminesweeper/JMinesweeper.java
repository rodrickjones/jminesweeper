package com.rodrickjones.jminesweeper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class JMinesweeper extends VBox implements Initializable {
    @FXML
    private Button restartButton;
    @FXML
    private GridPane minefieldPane;

    private int mineCount = 10;
    private int rows = 10;
    private int columns = 10;
    private Coordinate[][] coordinates = new Coordinate[10][10];
    private List<Coordinate> mines = new ArrayList<>(mineCount);

    public void initialize(URL location, ResourceBundle resources) {
        setupGame();
        restartButton.setOnAction(event -> setupGame());
    }

    public void setupGame() {
        mines.clear();
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                final Coordinate coordinate = new Coordinate(row, column);
                coordinates[row][column] = coordinate;
                minefieldPane.add(coordinate, column, row);
                coordinate.setOnMouseClicked(event -> {
                    switch (event.getButton()) {
                        case PRIMARY:
                            open(coordinate);
                            if (checkForWin()) {
                                showDialog("Congratulations!", "You win!");
                            }
                            break;
                        case SECONDARY:
                            coordinate.setFlagged(!coordinate.isFlagged());
                            break;
                    }
                });
                coordinate.setMaxWidth(100);
                coordinate.setMaxHeight(100);
            }
        }

        Random rng = new Random();
        while (mines.size() < mineCount) {
            int row = rng.nextInt(rows);
            int column = rng.nextInt(columns);
            Coordinate coordinate = coordinates[row][column];
            if (!coordinate.hasMine()) {
                coordinate.setHasMine(true);
                mines.add(coordinate);
            }
        }
    }

    private int countNeighbouringMines(Coordinate coordinate) {
        int neighbouringMines = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nRow = coordinate.getRow() + dy;
                int nCol = coordinate.getColumn() + dx;
                if (nRow < 0 || nRow >= rows || nCol < 0 || nCol >= columns) {
                    continue;
                }
                if (coordinates[nRow][nCol].hasMine()) {
                    neighbouringMines++;
                }
            }
        }
        return neighbouringMines;
    }

    private void open(Coordinate coordinate) {
        if (coordinate.hasMine()) {
            coordinate.showMine();
            for (Coordinate[] arr : coordinates) {
                for (Coordinate c : arr) {
                    if (c.hasMine()) {
                        c.showMine();
                    } else {
                        c.showCount(countNeighbouringMines(c));
                    }
                    c.setOnMouseClicked(null);
                }
            }
            showDialog("Unlucky!", "You lose!");
        } else {
            //calculate neighbour count
            int neighbouringMines = countNeighbouringMines(coordinate);
            coordinate.showCount(neighbouringMines);
            if (neighbouringMines == 0) {
                Queue<Coordinate> zeroNeighbours = new LinkedList<>();
                zeroNeighbours.add(coordinate);
                while (!zeroNeighbours.isEmpty()) {
                    Coordinate c = zeroNeighbours.poll();
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int nRow = c.getRow() + dy;
                            int nCol = c.getColumn() + dx;
                            if (nRow < 0 || nRow >= rows || nCol < 0 || nCol >= columns) {
                                continue;
                            }
                            Coordinate neighbour = coordinates[nRow][nCol];
                            if (neighbour.isOpened()) {
                                continue;
                            }
                            int nCount = countNeighbouringMines(neighbour);
                            neighbour.showCount(nCount);
                            if (nCount == 0) {
                                zeroNeighbours.add(neighbour);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean checkForWin() {
        return rows * columns - mineCount == Arrays.stream(coordinates).flatMap(Arrays::stream).filter(Coordinate::isOpened).count();
    }

    private void showDialog(String title, String contentText) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setContentText(contentText);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.show();
    }
}
