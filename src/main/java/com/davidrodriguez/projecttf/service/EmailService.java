package com.davidrodriguez.projecttf.service;


import com.davidrodriguez.projecttf.dto.EmailMessage;
import com.davidrodriguez.projecttf.dto.UserDto;

public interface EmailService {
    boolean send(EmailMessage emailMessage);
    public boolean sendIntroEmail(String to, UserDto userDto);
    public boolean sendRecoveryEmail(String to, UserDto userDto);
}
