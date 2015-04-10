package com.gionee.pay.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;

import com.gionee.pay.util.RSASignature;

/**
 * <pre>
 * <p>Title: OrderNotify.java</p>
 * <p>Description:商户接收到金立支付系统notify通知后，处理通知内容</p>
 * <p>成功响应 :"success"</p>
 * <p>失败响应 :"fail"</p>
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company:金立通信设备有限公司</p>
 * </pre>
 * 
 * @author tianxb
 * @date 2012-11-30 下午3:06:07
 * @version V2.0
 */
@WebServlet("/order/notify")
public class OrderNotify extends HttpServlet {

	private static final long serialVersionUID = -1134956680120198016L;
	private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyJKqH/BwBvjQMWH+LCyPl1CLB/MwiFmnM6EucG7pYLi/h+KegizrOhMMFvAuX26SmrqLZP8rOUJvQ/ZRj/0f8ulK46W8jOL/DFsxIZCVZby7lhLa/1bSHX7YSwCOxkXUhmH7BED/I3j9eYeOUsBDalJhjOOR1PrKKB1rZKHesOwIDAQAB";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/************** 设置请求以及响应的内容类型、编码方式 **************/
		resp.setContentType("text/html;charset=UTF-8");
		req.setCharacterEncoding("utf-8");

		Map<String, String> map = new HashMap<String, String>();

		String sign = req.getParameter("sign");

		/*************************************** 组装重排序参数 *********************************************/
		Enumeration<String> attributeNames = req.getParameterNames();
		while (attributeNames.hasMoreElements()) {
			String name = attributeNames.nextElement();
			map.put(name, req.getParameter(name));
		}

		StringBuilder contentBuffer = new StringBuilder();
		Object[] signParamArray = map.keySet().toArray();
		Arrays.sort(signParamArray);
		for (Object key : signParamArray) {
			String value = map.get(key);
			if (!"sign".equals(key) && !"msg".equals(key)) {// sign和msg不参与签名
				contentBuffer.append(key + "=" + value + "&");
			}
		}

		String content = StringUtils.removeEnd(contentBuffer.toString(), "&");

		if (StringUtils.isBlank(sign)) {
			printResponse(resp, "fail");
			return;
		}

		/****************************** 签名验证 *******************************************/
		boolean isValid = false;
		try {
			isValid = RSASignature.doCheck(content, sign, PUBLIC_KEY, CharEncoding.UTF_8);
		} catch (Exception e) {
			printResponse(resp, "fail");
			return;
		}

		if (isValid) {
			printResponse(resp, "success");
			return;
		}

		printResponse(resp, "fail");
	}

	private static void printResponse(HttpServletResponse resp, String result) throws IOException {
		PrintWriter out = resp.getWriter();
		out.println(result);
		out.flush();
		out.close();
		return;
	}
}
