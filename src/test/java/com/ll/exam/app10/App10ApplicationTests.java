package com.ll.exam.app10;

import com.ll.exam.app10.app.home.controller.HomeController;
import com.ll.exam.app10.app.member.controller.MemberController;
import com.ll.exam.app10.app.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;



@SpringBootTest
@AutoConfigureMockMvc
@Transactional
// application.yml은 기본 설정이고 그 위에 application-dev.yml(-> base-addi)와 application-test.yml(-> test)가 활성화
// 따라서 기본설정과 추가설정으로 인해 DB가 두 개 이상이면 추가설정의 DB와 연동됨
@ActiveProfiles({"base-addi", "test"})
class App10ApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private MemberService memberService;


	@Test
	@DisplayName("메인화면에서는 안녕이 나와야 한다.")
	void t1() throws Exception {
		// WHEN
		// GET /
		ResultActions resultActions = mvc
				.perform(get("/"))
				.andDo(print());

		// THEN
		// 안녕
		resultActions
				.andExpect(status().is2xxSuccessful())
				.andExpect(handler().handlerType(HomeController.class))
				.andExpect(handler().methodName("main"))
				.andExpect(content().string(containsString("안녕")));
	}

	@Test
	@DisplayName("회원의 수")
	void t2() throws Exception {
		long count = memberService.count();
		assertThat(count).isGreaterThan(0);
	}

	@Test
	@DisplayName("user1로 로그인 후 프로필페이지에 접속하면 user1의 이메일이 보여야 한다.")
	void t3() throws Exception {
		// WHEN
		// GET /
		ResultActions resultActions = mvc
				.perform(
						get("/member/profile")
								.with(user("user1").password("1234").roles("user")) // 이 id와 password를 가지고 /member/profile에 접속
				)
				.andDo(print());

		// THEN
		// 안녕
		resultActions
				.andExpect(status().is2xxSuccessful())
				.andExpect(handler().handlerType(MemberController.class))
				.andExpect(handler().methodName("showProfile"))
				.andExpect(content().string(containsString("user1@test.com")));
	}


	@Test
	@DisplayName("user4로 로그인 후 프로필페이지에 접속하면 user4의 이메일이 보여야 한다.")
	void t4() throws Exception {
// WHEN
		// GET /
		ResultActions resultActions = mvc
				.perform(
						get("/member/profile")
								.with(user("user4").password("1234").roles("user"))
				)
				.andDo(print());

		// THEN
		// 안녕
		resultActions
				.andExpect(status().is2xxSuccessful())
				.andExpect(handler().handlerType(MemberController.class))
				.andExpect(handler().methodName("showProfile"))
				.andExpect(content().string(containsString("user4@test.com")));	}
}
