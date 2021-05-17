package com.jongsul.fabinetgradle.Controller;

import com.jongsul.fabinetgradle.Config.SecurityUtil;
import com.jongsul.fabinetgradle.DTO.LoginDTO;
import com.jongsul.fabinetgradle.DTO.RegisterDTO;
import com.jongsul.fabinetgradle.Domain.Member;
import com.jongsul.fabinetgradle.Service.ImageService;
import com.jongsul.fabinetgradle.Service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*로그인
* 회원가입
* */
@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private final ImageService imageService;
    private final String salt = "haha";

    //민감한 키는 AwsConfig class에 옮겨둠

    @PostMapping("/login")
    public void doLogin(@RequestBody LoginDTO loginDTO, HttpServletResponse response, HttpSession session) throws IOException {

        System.out.println("Input id: "+loginDTO.getUserID()+", Input pw: "+loginDTO.getUserPW());

        //SHA256
        SecurityUtil sha = new SecurityUtil();
        String encryptPassword = sha.encryptSHA256(loginDTO.getUserPW()+salt);

        log.info("로그인 가능여부 판별");
        String result = memberService.login(loginDTO.getUserID(),encryptPassword);
        if(result.equals("F-2")){
            log.info("로그인실패 - 아이디에대한 비번이 일치하지 않는다");
            response.getWriter().write("F-2");
        }
        else if(result.equals("F-1")){  //아예 없는 아이디
            log.info("로그인실패 - 없는 아이디");
            response.getWriter().write("F-1");
        }
        else{
            log.info("로그인 성공");
            session.setAttribute("loginMemberId",loginDTO.getUserID());
            response.getWriter().write("Success");
        }
    }

    @PostMapping("/register")
    public void doRegister(@RequestBody RegisterDTO registerDTO, HttpServletResponse response) throws IOException {

        log.info(registerDTO.toString());

        //SHA256
        SecurityUtil sha = new SecurityUtil();
        String encryptPassword = sha.encryptSHA256(registerDTO.getUserPW()+salt);
        System.out.println("인코딩된 비밀번호: "+ encryptPassword);

        //AJAX쪽 변수이름과 DTO의 변수이름이 같아야 받아짐
        String result = memberService.isExistId(registerDTO.getUserID()); //입력받은 id가 이미 사용중인지 확인 위함
        //available이면 사용가능한 id
        //occupied면 이미 사용중인 id
        if(result.equals("occupied")){
            System.out.println("이미 등록된 ID");
            response.getWriter().write("occupied");
            return;
        }
        if(!registerDTO.getUserPW().equals(registerDTO.getUserPW2())){    //비밀번호 확인이 틀릴경우
            System.out.println("비밀번호확인이 틀림");
            response.getWriter().write("wrongCheck");
            return;
        }
        System.out.println("중복검사 통과");
        Member member = Member.builder()
                .name(registerDTO.getUserName())
                .loginId(registerDTO.getUserID())
                .loginPassword(encryptPassword)
                .Email(registerDTO.getUserEmail())
                .tel(registerDTO.getUserTel())
                .build();

//        //python에서 얼굴조회를 해야하기에 얼굴테이블을 따로 분리하여 만들었다.
//        Image image = new Image();
//        image.setImage(registerVO.getU_img());
//        imageService.join(image);
        
        String afterJoin = memberService.join(member);
        System.out.println(afterJoin+" 가입 완료");
        response.getWriter().write("available");
    }
}
