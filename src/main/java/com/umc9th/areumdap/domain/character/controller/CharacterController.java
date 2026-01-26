package com.umc9th.areumdap.domain.character.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.character.controller.docs.CharacterControllerDocs;
import com.umc9th.areumdap.domain.character.dto.request.RegisterCharacterRequest;
import com.umc9th.areumdap.domain.character.dto.response.CharacterGrowthResponse;
import com.umc9th.areumdap.domain.character.dto.response.GetCharacterHistoryResponse;
import com.umc9th.areumdap.domain.character.dto.response.GetCharacterResponse;
import com.umc9th.areumdap.domain.character.dto.response.RegisterCharacterResponse;
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
    @PostMapping("")
    public ResponseEntity<ApiResponse<RegisterCharacterResponse>> registerCharacter(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody RegisterCharacterRequest registerCharacterRequest
    ) {
        RegisterCharacterResponse response = characterCommandService.registerCharacter(userId, registerCharacterRequest);
        return ApiResponse.success(SuccessStatus.REGISTER_CHARACTER_SUCCESS, response);
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<GetCharacterResponse>> getCharacterMain(
            @AuthenticationPrincipal Long userId
    ) {
        GetCharacterResponse response = characterQueryService.getCharacter(userId);
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
    public ResponseEntity<ApiResponse<GetCharacterHistoryResponse>> getCharacterHistory(
            @AuthenticationPrincipal Long userId
    ) {
        GetCharacterHistoryResponse response = characterQueryService.getCharacterHistory(userId);
        return ApiResponse.success(SuccessStatus.GET_CHARACTER_HISTORY_SUCCESS, response);
    }

}
