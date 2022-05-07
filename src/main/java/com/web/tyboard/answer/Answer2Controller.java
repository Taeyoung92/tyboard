package com.web.tyboard.answer;

import com.web.tyboard.question.Question2;
import com.web.tyboard.question.Question2Service;
import com.web.tyboard.user.SiteUser;
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

@RequestMapping("/answer2")
@RequiredArgsConstructor
@Controller
public class Answer2Controller {
    private final Question2Service question2Service;
    private final Answer2Service answer2Service;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam String content,
                               @Valid Answer2Form answer2Form, BindingResult bindingResult, Principal principal) {
        Question2 question2 = this.question2Service.getQuestion2(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("question2", question2);
            return "question2_detail";
        }
        Answer2 answer2 = this.answer2Service.create(question2,
                answer2Form.getContent(), siteUser);
        return String.format("redirect:/question2/detail/%s#answer2_%s",
                answer2.getQuestion2().getId(), answer2.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answer2Modify(Answer2Form answer2Form, @PathVariable("id") Integer id, Principal principal) {
        Answer2 answer2 = this.answer2Service.getAnswer2(id);
        if (!answer2.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answer2Service.modify(answer2, answer2Form.getContent());
        return String.format("redirect:/question2/detail/%s#answer2_%s",
                answer2.getQuestion2().getId(), answer2.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answer2Modify(@Valid Answer2Form answer2Form, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer2_form";
        }
        Answer2 answer2 = this.answer2Service.getAnswer2(id);
        if (!answer2.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answer2Service.modify(answer2, answer2Form.getContent());
        return String.format("redirect:/question2/detail/%s#answer2_%s", answer2.getQuestion2().getId(), answer2.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answer2Delete(Principal principal, @PathVariable("id") Integer id) {
        Answer2 answer2 = this.answer2Service.getAnswer2(id);
        if (!answer2.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answer2Service.delete(answer2);
        return String.format("redirect:/question2/detail/%s", answer2.getQuestion2().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answer2Vote(Principal principal, @PathVariable("id") Integer id) {
        Answer2 answer2 = this.answer2Service.getAnswer2(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answer2Service.vote(answer2, siteUser);
        return String.format("redirect:/question2/detail/%s#answer2_%s",
                answer2.getQuestion2().getId(), answer2.getId());
    }
}
