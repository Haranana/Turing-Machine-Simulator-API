package com.hubosm.turingsimulator.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Tape {

    private final String blank;
    private TapeCell head;

    public Tape(String blank) {
        this.blank = blank;
        head = new TapeCell(blank);
    }

    public void moveHeadRight(){
        if(head.rightCell == null){
            head.rightCell = new TapeCell(blank);
            head.rightCell.leftCell = head;
        }
        head = head.rightCell;
    }

    public void moveHeadLeft(){
        if(head.leftCell == null){
            head.leftCell = new TapeCell(blank);
            head.leftCell.rightCell = head;
        }
        head = head.leftCell;
    }

    public String readHead(){
        return head.value;
    }

    public char readHeadChar(){
        return head.value.toCharArray()[0];
    }

    public void writeOnHead(String value){
        head.value = value;
    }

    public void writeOnHead(char value){
        head.value = String.valueOf(value);
    }

    //places text onto tape, starting from current head position and moving right
    //doesn't change head position
    public void placeText(String input){
        TapeCell originalHead = head;
        for(char inputChar : input.toCharArray()){
            writeOnHead(String.valueOf(inputChar));
            moveHeadRight();
        }
        head = originalHead;
    }

    public TapeState ToTapeState(boolean blankToWhiteSpace){

        Map<Integer, String> tapeMap = new HashMap<>();
        int headId = 0;

        TapeCell tempHead = head;

        while(tempHead.leftCell!=null){
            tempHead = tempHead.leftCell;
            headId++;
        }

        int mapIt = 0;
        while(tempHead!=null){
            tapeMap.put(mapIt ,  String.valueOf(tempHead.value).equals(blank)? "" : String.valueOf(tempHead.value));
            mapIt++;
            tempHead = tempHead.rightCell;
        }

        return new TapeState(headId ,tapeMap);
    }

    //TapeCell holds string as value but it should only store 1-characters string
    //Except when writing blank, which will be deleted before  tape to application
    private static class TapeCell {
        private String value;
        private TapeCell leftCell = null;
        private TapeCell rightCell = null;

        public TapeCell(String value) {
            this.value = value;
        }
    }
}
