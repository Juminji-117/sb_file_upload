package com.ll.exam.app10.app.member.service;

import com.ll.exam.app10.app.member.entity.Member;
import com.ll.exam.app10.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;

    private final MemberRepository memberRepository;

    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null); // findByUsername -> Optional<Member>
    }

    public Member join(String username, String password, String email, MultipartFile profileImg) { // join == create
        // create할 때 이미지 처리 로직 시작

        String profileImgRelPath = "member/" + UUID.randomUUID().toString() + ".png";
        // 일관된경로+.png인 이미지 파일 새로 만듦
        File profileImgFile = new File(genFileDirPath + "/" + profileImgRelPath);
        // 관련된 폴더가 혹시나 없다면 만들어준다. (mkdirs는 File의 기본 제공 함수)
        profileImgFile.mkdirs();

        try {
            // 입력받은 이미지를 아까 새로 만든 .png 이미지파일로 넘김김
           profileImg.transferTo(profileImgFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
}
