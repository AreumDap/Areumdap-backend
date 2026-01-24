package com.umc9th.areumdap.domain.question.controller;

import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.SuccessStatus;
import com.umc9th.areumdap.domain.mission.dto.request.CursorRequest;
import com.umc9th.areumdap.domain.question.controller.docs.UserQuestionControllerDocs;
import com.umc9th.areumdap.domain.user.dto.response.UserQuestionCursorResponse;
import com.umc9th.areumdap.domain.user.service.UserQuestionCommandService;
import com.umc9th.areumdap.domain.user.service.UserQuestionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/questions")
@RestController
@RequiredArgsConstructor
public class UserQuestionController implements UserQuestionControllerDocs {

    private final UserQuestionCommandService userQuestionCommandService;
    private final UserQuestionQueryService questionQueryService;

    @Override
    @PostMapping("/{chatHistoryId}")
    public ResponseEntity<ApiResponse<Void>> saveQuestion(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long chatHistoryId
    ) {
        userQuestionCommandService.saveInstantQuestion(userId, chatHistoryId);
        return ApiResponse.success(SuccessStatus.CREATE_QUESTION_SUCCESS,null);
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<UserQuestionCursorResponse>> getAllSavedQuestion(
            @AuthenticationPrincipal Long userId,
            @ModelAttribute CursorRequest request
    ) {
        return ApiResponse.success(
                SuccessStatus.GET_ALL_SAVED_QUESTION_SUCCESS,
                questionQueryService.getSavedQuestionsWithCursor(
                        userId, request
                )
        );
    }

}
