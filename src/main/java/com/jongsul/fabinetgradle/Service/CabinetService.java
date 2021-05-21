package com.jongsul.fabinetgradle.Service;


import com.jongsul.fabinetgradle.Domain.Board;
import com.jongsul.fabinetgradle.Domain.Cabinet;
import com.jongsul.fabinetgradle.Domain.Member;

import java.util.List;

public interface CabinetService {

    String calculateBill(Board board);

    long getBill(Member member);

    String chooseCanibet(Cabinet cabinet);

    List<Cabinet> findAllByID(Member member);

    List<String> getAllCabinet();
}
