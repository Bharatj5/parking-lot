package com.detillens.parkingapp;

import com.detillens.parkingapp.command.CheckInCommand;
import com.detillens.parkingapp.command.CheckOutCommand;
import com.detillens.parkingapp.command.Command;
import com.detillens.parkingapp.config.Input;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandLineReaderTest {

    private CommandLineReader classUnderTest;
    @Mock
    private CheckInCommand checkInCommand;
    @Mock
    private CheckOutCommand checkOutCommand;
    private InputStream sysInBackup;
    private PrintStream sysOutBackup;

    @BeforeEach
    void setUp() {
        classUnderTest = new CommandLineReader(getCommandMap());
        // hack to use Scanner class
        sysInBackup = System.in;
        sysOutBackup = System.out;
    }

    private Map<String, Command> getCommandMap() {
        return Map.of("check-in", checkInCommand,
                        "check-out", checkOutCommand);
    }

    @AfterEach
    void tearDown() {
        System.setIn(sysInBackup);
        System.setOut(sysOutBackup);
    }


    @Test
    void readInputs_validCheckInCommand_shouldAbleToCallTheImplementation() {
        //Given
        final String commands = "check-in -c= MYCAR 123 ";
        final Input input = new Input(commands);
        when(checkInCommand.validate(input)).thenReturn(true);
        final ByteArrayInputStream in = new ByteArrayInputStream(commands.getBytes());
        System.setIn(in);

        //When
        classUnderTest.readInputs();

        //Then
        verify(checkInCommand).validate(input);
        verify(checkInCommand).execute(input);
        verifyNoInteractions(checkOutCommand);
    }

    @Test
    void readInputs_validCheckOutCommand_shouldAbleToCallTheImplementation() {
        //Given
        final String commands = "check-out -c= MYCAR 123 ";
        final Input input = new Input(commands);
        when(checkOutCommand.validate(input)).thenReturn(true);
        final ByteArrayInputStream in = new ByteArrayInputStream(commands.getBytes());
        System.setIn(in);

        //When
        classUnderTest.readInputs();

        //Then
        verify(checkOutCommand).validate(input);
        verify(checkOutCommand).execute(input);
        verifyNoInteractions(checkInCommand);
    }


    @Test
    void readInputs_outOfScopeCommand_shouldThrowAnError() {
        //Given
        final String commands = "check-me -c= MYCAR 123 ";
        final ByteArrayInputStream in = new ByteArrayInputStream(commands.getBytes());
        System.setIn(in);

        //When
        classUnderTest.readInputs();

        verifyNoInteractions(checkOutCommand, checkInCommand);
    }
}