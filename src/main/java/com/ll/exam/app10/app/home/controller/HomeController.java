package com.ll.exam.app10.app.home.controller;

import com.ll.exam.app10.app.member.service.MemberService;
import com.ll.exam.app10.app.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;

    @GetMapping("/")
    public String showMain() {
        return "home/main";
    }

    @GetMapping("/about")
    public String showAbout() {
        return "home/about";
    }

    // 회원정보 리턴하는 기존방법
    @GetMapping("/currentUserOrigin")
    @ResponseBody
    public Principal currentUserOrigin(Principal principal) {
        return principal;
    }


    // 회원정보 리턴하는 새로운 방법
    @GetMapping("/currentUser")
    @ResponseBody
    public MemberContext currentUser(@AuthenticationPrincipal MemberContext memberContext) { // @AuthenticationPrincipal 어노테이션 붙이면 memberContext 바로 가져올 수 있음
        return memberContext;
        /*
        ** MemberContext에서 얻을 수 있는 것
        * memberContext.getUserName() -> 기존 Principal만으로도 가능
        * memberContext.getId() -> MemberContext에서 새로 추가
        * memberContext.getProfileImgUrl() -> MemberContext에서 새로 추가
         */
    }
}