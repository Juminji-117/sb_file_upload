package com.ll.exam.app10.app.member.entity;
import com.ll.exam.app10.app.base.AppConfig;
import com.ll.exam.app10.app.base.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.File;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity { // id, date같은 중복 엔티티 칼럼들은 BaseEntity로 관리
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private String profileImg; // 이미지 파일을 DB에서는 String으로 저장!

    public void removeProfileImgOnStorage() {
        if (profileImg == null || profileImg.trim().length() == 0) return;

        String profileImgPath = getProfileImgPath();

        new File(profileImgPath).delete();
    }

    private String getProfileImgPath() {
        return AppConfig.GET_FILE_DIR_PATH + "/" + profileImg;
    }
}
