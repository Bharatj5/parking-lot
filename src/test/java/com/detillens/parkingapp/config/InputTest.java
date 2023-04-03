package com.detillens.parkingapp.config;

import com.detillens.parkingapp.exception.BadInputException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.StringUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InputTest {

    @ParameterizedTest
    @ValueSource(strings = {" config-in -c=   12 - cp =  4 ", "config-in -c=12 -cp=4"})
    void validCommand_shouldWork(final String command) {
        final Input input = new Input(command);
        final Map<String, String> expectedOptions = Map.of("c", "12", "cp", "4");

        assertThat(input.getCommand()).isEqualTo("config-in");
        assertThat(input.getInputStr()).isEqualTo(StringUtils.trimWhitespace(command));
        assertThat(input.getOptions()).containsAllEntriesOf(expectedOptions);
    }


    @ParameterizedTest
    @ValueSource(strings = {" check-in -c=   ETL 123", "check-in -c=ETL 123"})
    void validCommand_withOneOption_shouldWork(final String command) {
        final Input input = new Input(command);
        final Map<String, String> expectedOptions = Map.of("c", "ETL 123");

        assertThat(input.getCommand()).isEqualTo("check-in");
        assertThat(input.getInputStr()).isEqualTo(StringUtils.trimWhitespace(command));
        assertThat(input.getOptions()).containsAllEntriesOf(expectedOptions);
    }


    @ParameterizedTest
    @ValueSource(strings = {" config-in -c= - cp =  4 ", "config-in -c=-cp =  4 "})
    void invalidCommand_shouldThrowAnError(final String command) {
        assertThatThrownBy(() -> new Input(command)).isInstanceOf(BadInputException.class);
    }


    @ParameterizedTest
    @ValueSource(strings = {"check-in -m=123", "check-in -c=123"})
    void getVehicleType_validInput_shouldProvideRightVehicleType(final String command) {
        final Input input = new Input(command);
        assertThat(input.getVehicleType().getCommandName()).containsAnyOf("m", "c");
    }
}