package com.example.sm.minh.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.sm.minh.eshop.utilities.EmailSender;

@SpringBootTest
public class SendEmailTest {
    @Test
    public void sendEmail() {
        // Thông tin tài khoản email
        String from = "dh52201052@student.stu.edu.vn";
        String password = "@Minh1182004";

        // Thông tin người nhận email
        String to = "minh11820004@gmail.com";

        // Nội dung email
        String subject = "Test Email from Java";
        String content = "This is a test email sent from Java.";

        // Gọi phương thức để gửi email
        EmailSender.sendEmail(from, password, to, subject, content);
    }
}
