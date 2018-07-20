package sendEmail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail extends Thread{
	private String senderMail;
	private String senderPassword;
	private String reciverMail;
	private String mailText;
	/**
	 * 使用这个构造方法即可
	 * @param senderMail		发送人邮箱
	 * @param senderPassword	发送人邮箱密码
	 * @param reciverMail		接收人邮箱
	 * @param mailText			发送文本(可以为html文本)
	 */
	public SendMail(String senderMail,String senderPassword,String reciverMail,String mailText){
		this.senderMail=senderMail;
		this.senderPassword=senderPassword;
		this.reciverMail=reciverMail;
		this.mailText=mailText;
		this.start();
	}
     
        /* 重写run方法的实现，在run方法中发送邮件给指定的用户 
         * @see java.lang.Thread#run() 
         */  
        @Override
        public void run() {  
            try{  

                Properties prop = new Properties();   

                prop.setProperty("mail.transport.protocol", "smtp");  

                prop.setProperty("mail.smtp.host", "smtp.mxhichina.com");

                prop.setProperty("mail.smtp.auth", "true");  

//                final String smtpPort = "465";

                Session session = Session.getInstance(prop);

                session.setDebug(true);
//                MimeMessage message = new MimeMessage(session);
                MimeMessage message = createMimeMessage(session, senderMail, reciverMail,mailText);


                Transport transport = session.getTransport();

                transport.connect(senderMail, senderPassword);  

                transport.sendMessage(message, message.getAllRecipients());

                transport.close();  
            }catch (Exception e) {  
                throw new RuntimeException(e);  
            }  
        }  

        public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,String mailText) throws Exception {
            // 1. 创建一封邮件
            MimeMessage message = new MimeMessage(session);

            // 2. From: 发件人
            message.setFrom(new InternetAddress(sendMail, "用户激活邮件", "UTF-8"));

            // 3. To: 收件人（可以增加多个收件人、抄送、密送）
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "用户名", "UTF-8"));

            // 4. Subject: 邮件主题
            message.setSubject("用户激活", "UTF-8");

            // 5. Content: 邮件正文（可以使用html标签）
            message.setContent(mailText, "text/html;charset=UTF-8");

            // 6. 设置发件时间
            message.setSentDate(new Date());

            // 7. 保存设置
            message.saveChanges();

            return message;
        }
}
