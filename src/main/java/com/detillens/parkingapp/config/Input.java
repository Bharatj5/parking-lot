package com.detillens.parkingapp.config;

import com.detillens.parkingapp.exception.BadInputException;
import com.detillens.parkingapp.model.enums.VehicleType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Data
@Slf4j
public class Input {

    private final String inputStr;
    private String command;
    private Map<String, String> options = new HashMap<>();

    public Input(final String inputStr) {
        this.inputStr = StringUtils.trim(inputStr);
        parseOptions();
    }

    private void setCommand(final String command) {
        this.command = command;
    }

    private void parseOptions() {
        log.debug("Input str: {}", getInputStr());
        if (!getInputStr().isBlank()) {
            final String command = StringUtils.split(getInputStr(), " ")[0];
            final String[] commands = StringUtils.split(StringUtils.replace(getInputStr(), command, ""), "-");
            if (commands.length > 0) {
                setCommand(StringUtils.trim(command));
                final String[] optionsArr = Arrays.copyOfRange(commands, 1, commands.length);
                Stream.of(optionsArr)
                      .forEach(op -> {
                          final String[] split = op.split("=");
                          if (split.length == 2 && !StringUtils.trim(split[1])
                                                               .equals("")) {
                              options.put(StringUtils.trim(split[0]), StringUtils.trim(split[1]));
                          } else {
                              throw new BadInputException();
                          }
                      });
            }
        }

    }

    public String getVehicleRegistrationNumber() {
        return Optional.ofNullable(Optional.ofNullable(getOptions()
                .get(VehicleType.CAR.getCommandName()))
                                           .orElse(getOptions()
                                                   .get(VehicleType.MOTORCYCLE.getCommandName())))
                       .orElseThrow(BadInputException::new);
    }

    public VehicleType getVehicleType() {
        if (StringUtils.isNotBlank(getOptions().get(VehicleType.CAR.getCommandName()))) {
            return VehicleType.CAR;
        } else if (StringUtils.isNotBlank(getOptions().get(VehicleType.MOTORCYCLE.getCommandName()))){
            return VehicleType.MOTORCYCLE;
        }
        return null;
    }


}
