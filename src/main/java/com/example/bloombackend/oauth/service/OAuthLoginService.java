package com.example.bloombackend.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bloombackend.oauth.controller.dto.response.KakaoInfoResponse;
import com.example.bloombackend.user.service.UserService;

@Service
public class OAuthLoginService {
	private final RequestKakaoInfoService requestKakaoInfoService;
	private final UserService userService;

	@Autowired
	private OAuthLoginService(RequestKakaoInfoService requestKakaoInfoService, UserService userService) {
		this.requestKakaoInfoService = requestKakaoInfoService;
		this.userService = userService;
	}

	public String login(String authorizationCode) {
		KakaoInfoResponse response = requestKakaoInfoService.request(authorizationCode);
		Long userId = userService.findOrCreateUser(response);
		return "genertaed-token";
	}

	public String getKakaoLoginUrl() {
		return requestKakaoInfoService.getRedirectUri();
	}
}
