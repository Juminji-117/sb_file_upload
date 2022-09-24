package com.ll.exam.app10.app.security.dto;

import com.ll.exam.app10.app.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
// 스프링 시큐리티 기본 제공 클래스 User -> userName, password, authorities 제공
// MemberContext 클래스를 새로 만들어 User 클래스를 상속받은 후 id와 profileImgUrl도 추가(스프링 시큐리티 기본 제공X)
public class MemberContext extends User {
    private final Long id;
    private final String email;
    private final String profileImgUrl;


    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities); // super로 부모클래스 호출
        this.id = member.getId();
        this.email = member.getEmail();
        this.profileImgUrl = member.getProfileImgUrl();
    }
}
