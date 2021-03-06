package com.jongsul.fabinetgradle.Controller;

import com.jongsul.fabinetgradle.Config.MemberInformation;
import com.jongsul.fabinetgradle.Domain.Board;
import com.jongsul.fabinetgradle.Domain.Cabinet;
import com.jongsul.fabinetgradle.Domain.CabinetHistory;
import com.jongsul.fabinetgradle.Domain.Member;
import com.jongsul.fabinetgradle.Service.CabinetHistoryService;
import com.jongsul.fabinetgradle.Service.CabinetService;
import com.jongsul.fabinetgradle.Service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final CabinetHistoryService cabinetHistoryService;
    private final MemberInformation memberInformation;
    private final MemberService memberService;

    @GetMapping("/list")
    public ResponseEntity<List<CabinetHistory>> userCabinetList(HttpServletRequest request){
        String loginID = memberInformation.getUserName(request);
        List<CabinetHistory> allCabinet = cabinetHistoryService.getAllCabinet(loginID);
        return ResponseEntity
                .ok(allCabinet);
    }

    @GetMapping("/info")
    public ResponseEntity<Member> userInfo(HttpServletRequest request){
        String loginID = memberInformation.getUserName(request);
        Member findMember = memberService.findOne(loginID);
        return ResponseEntity
                .ok(findMember);
    }
}
