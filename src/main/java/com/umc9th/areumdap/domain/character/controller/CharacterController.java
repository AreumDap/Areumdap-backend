package com.umc9th.areumdap.domain.character.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.character.dto.response.CharacterMainResponse;
import com.umc9th.areumdap.domain.character.service.CharacterQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Character", description = "캐릭터 API")
@RequestMapping("/api/characters")
@RestController
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterQueryService characterQueryService;

    @GetMapping("/main")
    @Operation(summary = "캐릭터 메인 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "캐릭터 메인 조회 성공", content = @Content())
    })
    public ResponseEntity<ApiResponse<CharacterMainResponse>> getCharacterMain(
            @AuthenticationPrincipal Long userId
    ) {
        CharacterMainResponse response = characterQueryService.getCharacterMain(userId);
        return ApiResponse.success(SuccessStatus.GET_CHARACTER_MAIN_SUCCESS, response);
    }
}
