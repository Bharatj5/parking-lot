package com.detillens.parkingapp.command;

import com.detillens.parkingapp.config.Input;
import com.detillens.parkingapp.model.Ticket;

public interface Command {

    boolean validate(Input input);

    Ticket execute(Input input);

}
