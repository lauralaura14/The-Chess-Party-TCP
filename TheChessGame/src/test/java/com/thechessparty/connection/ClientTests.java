package com.thechessparty.connection;

import com.fasterxml.jackson.databind.JsonNode;
import com.thechessparty.connection.jsonparsing.JsonConverter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTests {



    @Test
    void stringSanatizer() throws IOException {
        String output = Client.inputSanatizer("2");
        assertEquals("2", output);
    }
}
