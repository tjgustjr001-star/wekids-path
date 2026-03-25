package com.spring.cmd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCreateCommand {

    private String studentName;
    private String loginId;
    private String email;
    private String initialPassword;
}