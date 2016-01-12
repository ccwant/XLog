package com.ccwant.xlog.mall;

import java.io.File;

import com.ccwant.xlog.XLogConfiguration;

/**
 * 发送电子邮件
 * @author CCwant
 * https://github.com/CCwant/XLog
 */
public class XLogMail extends AbstractMail{

	private XLogConfiguration mConfiguration;
	
	public XLogMail() {
	}
	
	public void init(XLogConfiguration configuration){
		mConfiguration=configuration;
	}

	/**
	 * 发送普通邮件
	 * @param subject 标题
	 * @param content 内容
	 * @return 发送结果
	 */
	public boolean sendAndCc( String subject, String content) {

		final String smtp=mConfiguration.getMailParames().mSmtp;
		final String from=mConfiguration.getMailParames().mFrom;
		final String to=mConfiguration.getMailParames().mTo;
		final String copyto=mConfiguration.getMailParames().mCopyto;
		final String user=mConfiguration.getMailParames().mUser;
		final String password=mConfiguration.getMailParames().mPassword;
		final boolean debug=mConfiguration.getMailParames().mDebug;
		
		initMail(smtp);
		setNeedAuth(true); // 验证
		if (!setSubject(subject))
			return false;
		if (!setBody(content))
			return false;
		if (!setTo(to))
			return false;
		if (!setCopyTo(copyto))
			return false;
		if (!setFrom(from))
			return false;
		setNamePass(user, password);
		if (!sendOut(debug))
			return false;
		return true;
	}
	/**
	 * 发送带附件的邮件
	 * @param subject 标题
	 * @param content 内容
	 * @param file
	 * @return
	 */
	public boolean sendAndCc(String subject, String content, File file) {
		
		final String smtp=mConfiguration.getMailParames().mSmtp;
		final String from=mConfiguration.getMailParames().mFrom;
		final String to=mConfiguration.getMailParames().mTo;
		final String copyto=mConfiguration.getMailParames().mCopyto;
		final String user=mConfiguration.getMailParames().mUser;
		final String password=mConfiguration.getMailParames().mPassword;
		final boolean debug=mConfiguration.getMailParames().mDebug;
		
		initMail(smtp);
		
		setNeedAuth(true); // 验证
		if (!setSubject(subject))
			return false;
		if (!setBody(content))
			return false;
		if (!setTo(to))
			return false;
		if (!setCopyTo(copyto))
			return false;
		if (!setFrom(from))
			return false;
		if (!addAttachment(file))
			return false;
		setNamePass(user, password);
		if (!sendOut(debug))
			return false;
		return true;
	}
}
