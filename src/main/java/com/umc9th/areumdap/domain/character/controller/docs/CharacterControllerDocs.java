package com.umc9th.areumdap.domain.character.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.character.dto.request.RegisterCharacterRequest;
import com.umc9th.areumdap.domain.character.dto.response.GetCharacterGrowthResponse;
import com.umc9th.areumdap.domain.character.dto.response.GetCharacterHistoryResponse;
import com.umc9th.areumdap.domain.character.dto.response.GetCharacterResponse;
import com.umc9th.areumdap.domain.character.dto.response.RegisterCharacterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Character", description = "캐릭터 API")
public interface CharacterControllerDocs {

    @PostMapping
    @Operation(summary = "캐릭터 생성")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "캐릭터 생성 성공", content = @Content(schema = @Schema(implementation = RegisterCharacterResponse.class))),
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
    ResponseEntity<ApiResponse<RegisterCharacterResponse>> registerCharacter(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody RegisterCharacterRequest registerCharacterRequest
    );

    @GetMapping("/me")
    @Operation(summary = "내 캐릭터 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "캐릭터 조회 성공", content = @Content(schema = @Schema(implementation = GetCharacterResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "캐릭터 정보 없음", content = @Content())
    })
    ResponseEntity<ApiResponse<GetCharacterResponse>> getCharacterMain(
            @AuthenticationPrincipal Long userId
    );

    @PostMapping("/growth")
    @Operation(summary = "캐릭터 성장")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "캐릭터 성장 성공", content = @Content(schema = @Schema(implementation = GetCharacterGrowthResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "XP 부족", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "최고 레벨일 경우", content = @Content())
    })
    ResponseEntity<ApiResponse<GetCharacterGrowthResponse>> levelUp(
            @AuthenticationPrincipal Long userId
    );

    @PostMapping("/history/summary")
    @Operation(summary = "캐릭터 성장 히스토리 업데이트")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성장 히스토리 업데이트 성공", content = @Content()),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "캐릭터 정보 없음", content = @Content())
    })
    ResponseEntity<ApiResponse<Void>> updateHistorySummary(
            @AuthenticationPrincipal Long userId
    );

    @GetMapping("/history")
    @Operation(summary = "캐릭터 성장 히스토리 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성장 히스토리 조회 성공", content = @Content(schema = @Schema(implementation = GetCharacterHistoryResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "캐릭터 정보 없음", content = @Content())
    })
    ResponseEntity<ApiResponse<GetCharacterHistoryResponse>> getCharacterHistory(
            @AuthenticationPrincipal Long userId
    );

}
