package com.example.bloombackend.restdocs;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.bloombackend.global.config.JwtTokenProvider;
import com.example.bloombackend.oauth.OAuthProvider;
import com.example.bloombackend.user.controller.dto.request.UserRegisterInfoRequest;
import com.example.bloombackend.user.entity.Age;
import com.example.bloombackend.user.entity.Gender;
import com.example.bloombackend.user.entity.UserEntity;
import com.example.bloombackend.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class UserRestDocsTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@SpyBean
	private JwtTokenProvider jwtTokenProvider;

	private UserEntity testUser;

	private String mockToken;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		objectMapper = new ObjectMapper();

		// 사용자 생성 및 토큰 처리 모킹
		mockToken = "jwtToken";
		testUser = userRepository.save(new UserEntity(OAuthProvider.KAKAO, "testUser", "testId"));
		doReturn(testUser.getId()).when(jwtTokenProvider).getUserIdFromToken(mockToken);
	}

	@Test
	@DisplayName("API - 설문에 따른 유저 정보 업데이트")
	void updateUserInfoTest() throws Exception {
		//given
		UserRegisterInfoRequest request = new UserRegisterInfoRequest("원지", "FROM_18_TO_24", "F", true);

		//when & then
		mockMvc.perform(put("/api/user")
				.header("Authorization", mockToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(document("api-user-test/update-info",
				responseFields(
					fieldWithPath("nickname").description("변경할 유저의 닉네임"),
					fieldWithPath("age").description(
						"유저의 나잇대(UNDER_18, FROM_18_TO_24, FROM_25_TO_34, FROM_35_TO_44, OVER_45)"),
					fieldWithPath("gender").description("유저의 성별(M,F,O)"),
					fieldWithPath("isSurvey").description("유저의 설문 완료 여부")
				),
				responseFields(
					fieldWithPath("nickname").description("변경할 유저의 닉네임"),
					fieldWithPath("age").description("유저의 나잇대"),
					fieldWithPath("gender").description("유저의 성별"),
					fieldWithPath("isSurvey").description("유저의 설문 완료 여부")
				)
			));
	}

	@Test
	@DisplayName("API - 유저 정보 조회")
	void getUserInfoTest() throws Exception {
		//given
		testUser.updateUserSurveyInfo("원지", Age.FROM_18_TO_24, Gender.F, true);

		//when & then
		mockMvc.perform(get("/api/user")
				.header("Authorization", mockToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("api-user-test/get-info",
				responseFields(
					fieldWithPath("nickname").description("변경할 유저의 닉네임"),
					fieldWithPath("age").description("유저의 나잇대"),
					fieldWithPath("gender").description("유저의 성별"),
					fieldWithPath("isSurvey").description("유저의 설문 완료 여부")
				)
			));
	}
}