package com.ll.exam.app10.app.member.controller;

import com.ll.exam.app10.app.member.entity.Member;
import com.ll.exam.app10.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join")
    public String showJoin() {
        return "member/join";
    }

    // HttpSession session를 인수로 넣으면 세션 바로 접근 가능
    @PostMapping("/join")
    @ResponseBody
    public String join(String username, String password, String email, MultipartFile profileImg, HttpSession session) {
        Member oldMember = memberService.getMemberByUsername(username);

        if (oldMember != null) {
            return "redirect:/?errorMsg=Already done.";
        }

        Member member = memberService.join(username, "{noop}" + password, email, profileImg);

        session.setAttribute("loginedMemberId", member.getId()); // 세션에 로그인  정보 저장 == 세션으로 로그인 처리

        return "redirect:/member/profile";
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session) {
        // 세션으로 로그인 체크
        Long loginedMemberId = (Long) session.getAttribute("loginedMemberId");
        boolean isLogined = loginedMemberId != null;

        if (isLogined == false) {
            return "redirect:/?errorMsg=Need to login!";
        }

        // 로그인 되어 있으면 페이지 리턴
        return "member/profile";
    }
}
