package com.umc9th.areumdap.domain.ai.contoller;

import com.umc9th.areumdap.domain.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/ai")
@RestController
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;

    @GetMapping()
    public String getUsers() {
        return this.aiService.test();
    }
}
