package com.umc9th.areumdap.domain.character.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.character.controller.docs.CharacterControllerDocs;
import com.umc9th.areumdap.domain.character.dto.request.CreateCharacterRequest;
import com.umc9th.areumdap.domain.character.dto.response.CharacterCreateResponse;
import com.umc9th.areumdap.domain.character.dto.response.CharacterGrowthResponse;
import com.umc9th.areumdap.domain.character.dto.response.CharacterHistoryResponse;
import com.umc9th.areumdap.domain.character.dto.response.CharacterMeResponse;
import com.umc9th.areumdap.domain.character.service.CharacterCommandService;
import com.umc9th.areumdap.domain.character.service.CharacterQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/characters")
@RestController
@RequiredArgsConstructor
public class CharacterController implements CharacterControllerDocs {

    private final CharacterQueryService characterQueryService;
    private final CharacterCommandService characterCommandService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<CharacterCreateResponse>> createCharacter(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateCharacterRequest request
    ) {
        CharacterCreateResponse response = characterCommandService.createCharacter(userId, request);
        return ApiResponse.success(SuccessStatus.CREATE_CHARACTER_SUCCESS, response);
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CharacterMeResponse>> getCharacterMain(
            @AuthenticationPrincipal Long userId
    ) {
        CharacterMeResponse response = characterQueryService.getCharacterMain(userId);
        return ApiResponse.success(SuccessStatus.GET_CHARACTER_MAIN_SUCCESS, response);
    }

    @Override
    @PostMapping("/growth")
    public ResponseEntity<ApiResponse<CharacterGrowthResponse>> levelUp(
            @AuthenticationPrincipal Long userId
    ) {
        CharacterGrowthResponse response = characterCommandService.levelUp(userId);
        return ApiResponse.success(SuccessStatus.CHARACTER_GROWTH_SUCCESS, response);
    }

    @Override
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<CharacterHistoryResponse>> getCharacterHistory(
            @AuthenticationPrincipal Long userId
    ) {
        CharacterHistoryResponse response = characterQueryService.getCharacterHistory(userId);
        return ApiResponse.success(SuccessStatus.GET_CHARACTER_HISTORY_SUCCESS, response);
    }
}
