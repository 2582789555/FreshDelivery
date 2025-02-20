package org.example.freshdeliveryserver.utils;

import org.example.freshdeliveryserver.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Component
public class JavaMailUtil {

    @Resource
    private JavaMailSender javaMailSender;
    @Autowired
    private HttpServletRequest request;
    private static final String FROM_EMAIL = "2582789555@qq.com";

    // 发送验证码
    public void sendCode(String mail){
        try{
            String code = createCode();
            String message = "验证码：" + code +", 该验证码30分钟内有效！";
            sendMessage(mail,"鲜送-验证码",message);
            storeCodeInSession(mail,code);
        }catch (Exception e){
            e.printStackTrace();
            throw new BizException("验证码发送失败");
        }
    }
    // 发送普通邮件
    @Async
    public void sendMessage(String mail, String subject, String message) {
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(FROM_EMAIL);
            simpleMailMessage.setTo(mail);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);
            javaMailSender.send(simpleMailMessage);
        }catch (MailException e){
            e.printStackTrace();
            throw new BizException("邮件发送失败");
        }
    }

    // 生成验证码
    private String createCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++){
            int type = random.nextInt(3);
            switch (type){
                case 0:
                    code.append((char) (random.nextInt(26) + 65)); // 大写字母
                    break;
                case 1:
                    code.append((char) (random.nextInt(26) + 97)); // 小写字母
                    break;
                case 2:
                    code.append(random.nextInt(10)); // 数字
                    break;
            }
        }
        return code.toString();
    }


    // 存储验证码到session中
    private void storeCodeInSession(String mail, String code) {
        HttpSession session = request.getSession();
        session.setAttribute(mail,code);
        session.setMaxInactiveInterval(30 * 60);
    }

    // 验证输入的验证码
    public boolean validateVerificationCode(String email, String code) {
        HttpSession session = request.getSession();
        String storedCode = (String) session.getAttribute(email);
        // 检查验证码是否存在并进行比对
        if (storedCode == null) {
            throw new BizException("验证码已过期或未发送");
        }
        if (!storedCode.equals(code)) {
            throw new BizException("验证码错误");
        }else{
            session.removeAttribute(email);
            return true;
        }
    }
}

