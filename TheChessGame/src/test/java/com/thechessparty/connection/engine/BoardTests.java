package com.thechessparty.connection.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.thechessparty.connection.jsonparsing.JsonConverter;
import com.thechessparty.connection.pojo.SimpleTestCaseJsonPOJO;
import com.thechessparty.engine.board.GameBoard;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTests {

    @Test
    void printBoard() {

        GameBoard gb = GameBoard.createInitialBoard();

        System.out.println(gb);
    }
}
