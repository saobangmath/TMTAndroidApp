package com.example.dotgeneration.models;

public class Coordinates {
    int row;
    int column;


    public Coordinates(int row, int column){
        this.column = column;
        this.row = row;
    }

    public void setColumn(int column){
        this.column = column;
    }

    public void setRow(int row){
        this.row = row;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }

}
