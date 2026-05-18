package com.example.employee.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor @Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    public void sendWelcomeEmail(String to,String name){
        try{
            SimpleMailMessage msg=new SimpleMailMessage();
            msg.setTo(to); msg.setSubject("Welcome to the Team!");
            msg.setText("Dear "+name+",\n\nWelcome! Your profile has been created.\n\nHR Team");
            mailSender.send(msg);
        }catch(Exception e){log.warn("Email failed: "+e.getMessage());}
    }
    public void sendLeaveStatusNotification(String to,String name,String status,String reason){
        try{
            SimpleMailMessage msg=new SimpleMailMessage();
            msg.setTo(to); msg.setSubject("Leave Request "+status);
            String body="Dear "+name+",\n\nYour leave has been "+status+".";
            if(reason!=null&&!reason.isEmpty()) body+="\nReason: "+reason;
            msg.setText(body+"\n\nHR Team");
            mailSender.send(msg);
        }catch(Exception e){log.warn("Email failed: "+e.getMessage());}
    }
}
