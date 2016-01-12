package com.ccwant.xlog.mall;

import java.io.File;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import android.util.Log;

/**
 * 发送电子邮件的抽象类
 * @author CCwant
 * https://github.com/CCwant/XLog
 */
public abstract class AbstractMail {
	
	private final static String TAG="Mail";
	private MimeMessage mimeMsg;
	private Session session;
	private Properties props;
	private String username;
	private String password;
	private Multipart mp;
	

	public AbstractMail() {
		
	}
	public void initMail(String smtp){
		initCommandMap();
		setSmtpHost(smtp);
		createMimeMessage();
	}

	public void setSmtpHost(String hostName) {
		Log.d(TAG,"set system property：mail.smtp.host=" + hostName);
		if (props == null) {
			props = System.getProperties();
		}
		props.put("mail.smtp.host", hostName);
	}

	public boolean createMimeMessage() {
		try {
			Log.d(TAG,"Ready to get mall session object !");
			session = Session.getDefaultInstance(props, null);
		} catch (Exception e) {
			Log.d(TAG,"Get mall session fail !",e);
			return false;
		}
		try {
			Log.d(TAG,"Ready to create MimeMessage object !");
			mimeMsg = new MimeMessage(session);
			mp = new MimeMultipart();
			return true;
		} catch (Exception e) {
			Log.e(TAG,"Create MimeMessage object fail !",e);
			return false;
		}
	}

	public void setNeedAuth(boolean need) {
		Log.d(TAG,"Set smtp identity authentication property : mail.smtp.auth = "+ need);
		if (props == null)
			props = System.getProperties();
		if (need) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
	}

	public void setNamePass(String name, String pass) {
		username = name;
		password = pass;
	}

	public boolean setSubject(String mailSubject) {
		Log.d(TAG,"Create mall subject :"+mailSubject);
		try {
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {
			Log.e(TAG,"Create mall subject fail :",e);
			return false;
		}
	}

	public boolean setBody(String mailBody) {
		Log.d(TAG,"Create mall body :"+mailBody);
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent("" + mailBody, "text/html;charset=UTF-8");
			mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			Log.e(TAG,"Create mall body fail :",e);
			return false;
		}
	}
	public boolean addAttachment(File file) {
		Log.d(TAG,"Create mall attachment :"+file.getName());
		try {
			BodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(file);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
			mp.addBodyPart(messageBodyPart);
			return true;
		} catch (Exception e) {
			Log.e(TAG,"Create mall attachment fail :",e);
			return false;
		}

	}
	public boolean setFrom(String from) {
		if (from == null)
			return false;
		Log.d(TAG,"Set from :"+from);
		try {
			mimeMsg.setFrom(new InternetAddress(from));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean setTo(String to) {
		if (to == null)
			return false;
		Log.d(TAG,"Set to :"+to);
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean setCopyTo(String copyto) {
		if (copyto == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.CC,(Address[]) InternetAddress.parse(copyto));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean sendOut(boolean debug) {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			Log.d(TAG,"Send mail...");
			Session mailSession = Session.getInstance(props, null);
			mailSession.setDebug(debug);// 开启后有调试信息
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), username,password);
			transport.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.TO));
			Log.d(TAG,"Send mail success !");
			transport.close();
			return true;
		} catch (Exception e) {
			Log.e(TAG,"Send mail fail : ",e);
			return false;
		}
	}
	public void initCommandMap(){
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mc);
	}
	
}