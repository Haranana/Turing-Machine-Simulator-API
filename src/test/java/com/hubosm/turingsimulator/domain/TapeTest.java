package com.hubosm.turingsimulator.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TapeTest {

    private Tape tape;
    private final String tapeBlankChar = String.valueOf('_');

    @BeforeEach
    void setUp(){
        tape = new Tape(tapeBlankChar);
    }

    @Test
    @DisplayName("readHead() on tape empty reads blank character")
    void shouldReadBlankIfTapeEmpty(){
        assertEquals(tape.readHead(), tapeBlankChar, "Read character is not blank");
    }

    @Test
    @DisplayName("readHead() on tape empty reads blank character")
    void shouldReadBlankIfEmptyTapeCell(){
        assertEquals(tape.readHead(), tapeBlankChar, "Read character is not blank");
        tape.moveHeadRight();
        assertEquals(tape.readHead(), tapeBlankChar, "Read character is not blank");
        tape.moveHeadRight();
        assertEquals(tape.readHead(), tapeBlankChar, "Read character is not blank");
        tape.moveHeadLeft();
        assertEquals(tape.readHead(), tapeBlankChar, "Read character is not blank");
    }

    @Test
    @DisplayName("writeOnHead() on empty tape puts character on head position")
    void shouldWriteCharacterOnTape(){
        final char charToWrite = 'H';
        tape.writeOnHead(charToWrite);
        assertEquals(tape.readHead(), charToWrite, "written character is not correct");
    }

    @Test
    @DisplayName("moveHead on both direction should correctly move head, write string and expand tape if needed")
    void shouldMoveHeadLeftAndRight(){
        final char[] charsToWrite = new char[]{'B','C','D','A'};


        assertDoesNotThrow(()->{
                    tape.writeOnHead(charsToWrite[0]);
                    tape.moveHeadRight();
                    tape.writeOnHead(charsToWrite[1]);
                    tape.moveHeadRight();
                    tape.writeOnHead(charsToWrite[2]);
                    tape.moveHeadLeft();
                    tape.moveHeadLeft();
                    tape.moveHeadLeft();
                    tape.writeOnHead(charsToWrite[3]);

                    assertEquals(tape.readHead() , 'A' , "read unexpected character");
                    tape.moveHeadRight();
                    assertEquals(tape.readHead() , 'B' , "read unexpected character");
                    tape.moveHeadRight();
                    assertEquals(tape.readHead() , 'C' , "read unexpected character");
                    tape.moveHeadRight();
                    assertEquals(tape.readHead() , 'D' , "read unexpected character");
                    tape.moveHeadRight();
                } , "exception in tape traversal");
    }

    @Test
    @DisplayName("placeText() should correctly place chosen string onto tape without changing head position")
    void shouldPlaceTextOnEmptyTape(){
        final String text = "test";
        tape.placeText(text);
        assertEquals(tape.readHead() , text.charAt(0) , "read unexpected character");
        tape.moveHeadRight();
        assertEquals(tape.readHead() , text.charAt(1) , "read unexpected character");
        tape.moveHeadRight();
        assertEquals(tape.readHead() , text.charAt(2) , "read unexpected character");
        tape.moveHeadRight();
        assertEquals(tape.readHead() , text.charAt(3) , "read unexpected character");
    }
}
