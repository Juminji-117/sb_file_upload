package com.ll.exam.app10.app.security.dto;

import com.ll.exam.app10.app.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
// 스프링 시큐리티 기본 제공 클래스 User -> userName, password, authorities 제공
// MemberContext 클래스를 새로 만들어 User 클래스를 상속받은 후 id와 profileImgUrl도 추가(스프링 시큐리티 기본 제공X)
public class MemberContext extends User implements OAuth2User {
    private Map<String, Object> attributes;
    private String userNameAttributeName;
    private final Long id;
    private final String email;
    private final String profileImgUrl;


    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities); // super로 부모클래스 호출
        this.id = member.getId();
        this.email = member.getEmail();
        this.profileImgUrl = member.getProfileImgUrl();
    }

    public MemberContext(Member member, List<GrantedAuthority> authorities, Map<String, Object> attributes, String userNameAttributeName) {
        this(member, authorities);
        this.attributes = attributes;
        this.userNameAttributeName = userNameAttributeName;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return super.getAuthorities().stream().collect(Collectors.toSet());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.getAttribute(this.userNameAttributeName).toString();
    }
}
