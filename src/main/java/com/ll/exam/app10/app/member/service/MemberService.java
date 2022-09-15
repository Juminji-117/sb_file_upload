package com.ll.exam.app10.app.member.service;

import com.ll.exam.app10.app.member.entity.Member;
import com.ll.exam.app10.app.member.repository.MemberRepository;
import com.ll.exam.app10.app.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;

    private final MemberRepository memberRepository;

    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null); // findByUsername -> Optional<Member>
    }

    public Member join(String username, String password, String email, MultipartFile profileImg) { // join == create
        // create할 때 이미지 처리 로직 시작

        String profileImgDirName = "member/" + Util.date.getCurrentDateFormatted("yyyy_MM_dd"); // 'member/날짜' 형태로 폴더 생성

        String ext = Util.file.getExt(profileImg.getOriginalFilename()); // 사진파일 확장자 추출

        String fileName = UUID.randomUUID() + "." + ext;
        String profileImgDirPath = genFileDirPath + "/" + profileImgDirName;
        String profileImgFilePath = profileImgDirPath + "/" + fileName;

        new File(profileImgDirPath).mkdirs(); // 폴더가 혹시나 없다면 만들어준다.

        try {
            // 입력받은 이미지를 아까 새로 만든 .png 이미지파일로 넘김김
            profileImg.transferTo(new File(profileImgFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String profileImgRelPath = profileImgDirName + "/" + fileName;
        // 이미지 처리 로직 끝

        // builder 이용해서 set컬럼 말고 간단하게 멤버 저장
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .profileImg(profileImgRelPath)
                .build();

        memberRepository.save(member);

        return member;
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // UserDetail 인터페이스 오버라이드
        Member member = memberRepository.findByUsername(username).get();

        List<GrantedAuthority> authorities = new ArrayList<>(); // GrantedAuthority 인터페이스
        authorities.add(new SimpleGrantedAuthority("member")); // SimpleGrantedAuthority 인터페이스

        return new User(member.getUsername(), member.getPassword(), authorities); // UserDetail 인터페이스 사용시 이런 형식으로 User을 return하는 게 규칙
    }

    // 테스트 데이터용 join
    public Member join(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();

        memberRepository.save(member);

        return member;
    }

    public long count() {
        return memberRepository.count();
    }

    public void removeProfileImg(Member member) {
        member.removeProfileImgOnStorage(); // 파일삭제
        member.setProfileImg(null);

        memberRepository.save(member);
    }
}
