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
	 * ʹ��������췽������
	 * @param senderMail		����������
	 * @param senderPassword	��������������
	 * @param reciverMail		����������
	 * @param mailText			�����ı�(����Ϊhtml�ı�)
	 */
	public SendMail(String senderMail,String senderPassword,String reciverMail,String mailText){
		this.senderMail=senderMail;
		this.senderPassword=senderPassword;
		this.reciverMail=reciverMail;
		this.mailText=mailText;
		this.start();
	}
     
        /* ��дrun������ʵ�֣���run�����з����ʼ���ָ�����û� 
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
            // 1. ����һ���ʼ�
            MimeMessage message = new MimeMessage(session);

            // 2. From: ������
            message.setFrom(new InternetAddress(sendMail, "�û������ʼ�", "UTF-8"));

            // 3. To: �ռ��ˣ��������Ӷ���ռ��ˡ����͡����ͣ�
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "�û���", "UTF-8"));

            // 4. Subject: �ʼ�����
            message.setSubject("�û�����", "UTF-8");

            // 5. Content: �ʼ����ģ�����ʹ��html��ǩ��
            message.setContent(mailText, "text/html;charset=UTF-8");

            // 6. ���÷���ʱ��
            message.setSentDate(new Date());

            // 7. ��������
            message.saveChanges();

            return message;
        }
}
