package com.umc9th.areumdap.domain.question.controller.docs;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.domain.mission.dto.request.CursorRequest;
import com.umc9th.areumdap.domain.user.dto.response.UserQuestionCursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Question", description = "질문 API")
public interface UserQuestionControllerDocs {

    @PostMapping("/{chatHistoryId}")
    @Operation(
            summary = "질문 저장",
            description = "사용자가 질문을 저장합니다."
    )
    ResponseEntity<ApiResponse<Void>> saveQuestion(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long chatHistoryId
    );

    @GetMapping
    @Operation(
            summary = "저장한 질문 목록 조회 (커서 페이징)",
            description = """
                    커서 기반 무한 스크롤 방식으로 저장한 질문 목록을 조회합니다.

                    - 첫 요청: cursorTime, cursorId 없이 호출
                    - 다음 요청: 이전 응답의 nextCursorTime, nextCursorId를 그대로 전달
                    """
    )
    ResponseEntity<ApiResponse<UserQuestionCursorResponse>> getAllSavedQuestion(
            @AuthenticationPrincipal Long userId,
            @Valid @ModelAttribute CursorRequest request
    );

    @DeleteMapping("/{userQuestionId}")
    @Operation(
            summary = "저장된 질문 삭제",
            description = "사용자가 저장한 질문을 삭제합니다. 대화에 사용 중인 질문은 목록에서 숨김 처리됩니다."
    )
    ResponseEntity<ApiResponse<Void>> deleteQuestion(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long userQuestionId
    );

}
