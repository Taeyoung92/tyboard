package com.web.tyboard.answer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class Answer3Form {
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
