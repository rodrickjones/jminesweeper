package com.rodrickjones.jminesweeper;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Coordinate extends Button {
    private int row;
    private int column;
    private boolean hasMine;
    private boolean flagged;
    private boolean opened;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public void setHasMine(boolean hasMine) {
        this.hasMine = hasMine;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
        if (flagged) {
            this.setText("F");
            this.setTextFill(Color.GREEN);
        } else {
            this.setText("");
        }
    }

    public void showMine() {
        this.setText("M");
        this.setTextFill(Color.RED);
    }

    public void showCount(int count) {
        this.setText(Integer.toString(count));
        this.setTextFill(Color.BLACK);
        this.setOnMouseClicked(null);
        opened = true;
    }

    public boolean isOpened() {
        return opened;
    }

    @Override
    public String toString() {
        return "[row=" + row + ", column=" + column + ", mine=" + hasMine + "]";
    }
}
