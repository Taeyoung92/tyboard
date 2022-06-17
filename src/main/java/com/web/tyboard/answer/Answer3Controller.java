package com.web.tyboard.answer;

import com.web.tyboard.question.Question3;
import com.web.tyboard.question.Question3Service;
import com.web.tyboard.user.User;
import com.web.tyboard.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/answer3")
@RequiredArgsConstructor
@Controller
public class Answer3Controller {
    private final Question3Service question3Service;
    private final Answer3Service answer3Service;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam String content,
                               @Valid Answer3Form answer3Form, BindingResult bindingResult, Principal principal) {
        Question3 question3 = this.question3Service.getQuestion3(id);
        User user = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("question3", question3);
            return "question3_detail";
        }
        Answer3 answer3 = this.answer3Service.create(question3,
                answer3Form.getContent(), user);
        return String.format("redirect:/question3/detail/%s#answer3_%s",
                answer3.getQuestion3().getId(), answer3.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answer3Modify(Answer3Form answer3Form, @PathVariable("id") Integer id, Principal principal) {
        Answer3 answer3 = this.answer3Service.getAnswer3(id);
        if (!answer3.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answer3Form.setContent(answer3.getContent());
        return "answer3_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answer3Modify(@Valid Answer3Form answer3Form, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer3_form";
        }
        Answer3 answer3 = this.answer3Service.getAnswer3(id);
        if (!answer3.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answer3Service.modify(answer3, answer3Form.getContent());
        return String.format("redirect:/question3/detail/%s#answer3_%s", answer3.getQuestion3().getId(), answer3.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answer3Delete(Principal principal, @PathVariable("id") Integer id) {
        Answer3 answer3 = this.answer3Service.getAnswer3(id);
        if (!answer3.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answer3Service.delete(answer3);
        return String.format("redirect:/question3/detail/%s", answer3.getQuestion3().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answer3Vote(Principal principal, @PathVariable("id") Integer id) {
        Answer3 answer3 = this.answer3Service.getAnswer3(id);
        User user = this.userService.getUser(principal.getName());
        this.answer3Service.vote(answer3, user);
        return String.format("redirect:/question3/detail/%s#answer3_%s",
                answer3.getQuestion3().getId(), answer3.getId());
    }
}
