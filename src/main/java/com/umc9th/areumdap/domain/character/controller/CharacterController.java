package com.umc9th.areumdap.domain.character.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.character.dto.request.CreateCharacterRequest;
import com.umc9th.areumdap.domain.character.dto.response.CharacterCreateResponse;
import com.umc9th.areumdap.domain.character.dto.response.CharacterGrowthResponse;
import com.umc9th.areumdap.domain.character.dto.response.CharacterHistoryResponse;
import com.umc9th.areumdap.domain.character.dto.response.CharacterMeResponse;
import com.umc9th.areumdap.domain.character.service.CharacterCommandService;
import com.umc9th.areumdap.domain.character.service.CharacterQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Character", description = "캐릭터 API")
@RequestMapping("/api/characters")
@RestController
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterQueryService characterQueryService;
    private final CharacterCommandService characterCommandService;

    @PostMapping("")
    @Operation(summary = "캐릭터 생성")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "캐릭터 생성 성공", content = @Content(schema = @Schema(implementation = CharacterCreateResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = "계절 미입력",
                                    summary = "계절이 null인 경우",
                                    value = "{\"isSuccess\": false, \"code\": \"COMM_400\", \"message\": \"계절은 필수 입력 값입니다.\", \"result\": null}"
                            ),
                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = "키워드 미입력",
                                    summary = "키워드가 null인 경우",
                                    value = "{\"isSuccess\": false, \"code\": \"COMM_400\", \"message\": \"키워드는 필수 입력 값입니다.\", \"result\": null}"
                            ),
                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = "키워드 개수 초과",
                                    summary = "키워드가 3개를 초과하는 경우",
                                    value = "{\"isSuccess\": false, \"code\": \"COMM_400\", \"message\": \"키워드는 최대 3개까지 선택 가능합니다.\", \"result\": null}"
                            ),
                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = "키워드 글자수 초과",
                                    summary = "키워드가 100자를 초과하는 경우",
                                    value = "{\"isSuccess\": false, \"code\": \"COMM_400\", \"message\": \"키워드는 100자를 초과할 수 없습니다.\", \"result\": null}"
                            )
                    }
            ))
    })
    public ResponseEntity<ApiResponse<CharacterCreateResponse>> createCharacter(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreateCharacterRequest request
    ) {
        CharacterCreateResponse response = characterCommandService.createCharacter(userId, request);
        return ApiResponse.success(SuccessStatus.CREATE_CHARACTER_SUCCESS, response);
    }

    @GetMapping("/me")
    @Operation(summary = "내 캐릭터 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "캐릭터 조회 성공", content = @Content(schema = @Schema(implementation = CharacterMeResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "캐릭터 정보 없음", content = @Content())
    })
    public ResponseEntity<ApiResponse<CharacterMeResponse>> getCharacterMain(
            @AuthenticationPrincipal Long userId
    ) {
        CharacterMeResponse response = characterQueryService.getCharacterMain(userId);
        return ApiResponse.success(SuccessStatus.GET_CHARACTER_MAIN_SUCCESS, response);
    }

    @PostMapping("/growth")
    @Operation(summary = "캐릭터 성장")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "캐릭터 성장 성공", content = @Content(schema = @Schema(implementation = CharacterGrowthResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "XP 부족", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "최고 레벨일 경우", content = @Content())
    })
    public ResponseEntity<ApiResponse<CharacterGrowthResponse>> levelUp(
            @AuthenticationPrincipal Long userId
    ) {
        CharacterGrowthResponse response = characterCommandService.levelUp(userId);
        return ApiResponse.success(SuccessStatus.CHARACTER_GROWTH_SUCCESS, response);
    }


    @GetMapping("/history")
    @Operation(summary = "캐릭터 성장 히스토리 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성장 히스토리 조회 성공", content = @Content(schema = @Schema(implementation = CharacterHistoryResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "캐릭터 정보 없음", content = @Content())
    })
    public ResponseEntity<ApiResponse<CharacterHistoryResponse>> getCharacterHistory(
            @AuthenticationPrincipal Long userId
    ) {
        CharacterHistoryResponse response = characterQueryService.getCharacterHistory(userId);
        return ApiResponse.success(SuccessStatus.GET_CHARACTER_HISTORY_SUCCESS, response);
    }
}
