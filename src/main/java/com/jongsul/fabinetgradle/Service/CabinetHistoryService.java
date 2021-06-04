package com.jongsul.fabinetgradle.Service;

import com.jongsul.fabinetgradle.Domain.Cabinet;
import com.jongsul.fabinetgradle.Domain.CabinetHistory;
import com.jongsul.fabinetgradle.Domain.Member;

import java.util.List;

public interface CabinetHistoryService {
    String save(Cabinet cabinet);
    List<Cabinet> getDate(Member member);
    List<Cabinet> findAllbyName(Member member);
    List<Cabinet> getOneCabinet(String userID);
    List<CabinetHistory> getAllCabinet(String userID);
}
