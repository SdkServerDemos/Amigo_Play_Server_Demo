package com.gionee.pay.main;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.apache.commons.lang3.StringUtils;

import com.gionee.pay.bean.Order;
import com.gionee.pay.util.HttpUtils;
import com.gionee.pay.util.PayUtil;

public class AmigoPlayDemo {
	private static final String GIONEE_PAY_INIT = "https://pay.gionee.com/order/create";
	// 跑通demo后替换成商户自己的api_key
	private static final String API_KEY = "DDFDAEC3DBF544DD99EB9F508B429905";
	// 跑通demo后，替换成商户自己的private_key
	private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJZJoM3tJzf96lejbCsQsCgnMAdLtyvf9ZkflExjEUuQ1O7g+OuWc4ZrvtE+coWZmUt2IjYkws63XAbFt/aMEHu/qerp3g/Mn1Y+tVFVbsME+hqhTVknOtt7kv+ZyVymHXZEBvzlTQXiybUDXkRqvvu+hO/+4BhS6fNmuggYpbTnAgMBAAECgYBhIABOVT+NTgnOzYywYD6YFItTi7k7H6fnZ6M9oqCgx171ams0Ra0vaB6Pt93nPZb2T8hYWXWUhqdwsQLs0SQs9+91881Nu1JE1nPAyeQKWv0t6Ol8BavecvEGUcJPXJ1/zZFU7r+s3pdz7OnQV2b77VhZiLsaQsmrxLPALdeTkQJBAMSxDuQymonH0q9KbMAgZ/c7ZraQhc1erf8eb6ni+3eXoJkJBlYsFmp+zFxo6gfj8N8nl0sV1sD3g2HwcUTOimsCQQDDmpG0uqTmAGJryfZOp8YB3VqhTZfFf1xcXQGNkUsaNXFpLMd5O4haRlL4FiROenVucYd0VU4eZIOi33CkYdZ1AkEAjxxUvWykTIN7o9b+8XuiqZwqy8Kz2A1/hBRdIrroRMeqLi8G0UQauzmu773WKg+LfpKL3jHxo01z5prPj0TIKQJBAJl5BMv2Cf4A3ThPnXd3gg/iewLG28d1N6Wsv9Qw5Olqd1KbdON1R3X1aZIH5XLB+LOwViR77jBAk1xOzpXbwiUCQDX4cj4IWy6brwCFFOkcqY23veDTIB8vXC2Kg//Kv1vUJ41DM82/Ga7fBAqCgTHSow3QlByoMFokAxmkU6Zc5EE=";
	// 网游类型接入时固定值
	private static final String DELIVER_TYPE = "1";
	// 成功响应状态码
	private static final String CREATE_SUCCESS_RESPONSE_CODE = "200010000";

	public static void main(String[] args) {
		Order order = new Order("201405280000001", "amigo_player001", "testPaymobile", API_KEY, new BigDecimal(
				"0.01"), new BigDecimal("0.01"), new Timestamp(System.currentTimeMillis()), null, null);

		String requestBody = null;
		try {
			requestBody = PayUtil.wrapCreateOrder(order, PRIVATE_KEY, DELIVER_TYPE);
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String response = null;
		try {
			response = HttpUtils.post(GIONEE_PAY_INIT, requestBody);
		} catch (Exception e) {
			// TODO : 处理异常;
			return;
		}

		JSONObject json = (JSONObject) JSONValue.parse(response);

		System.out.println(response);

		if (CREATE_SUCCESS_RESPONSE_CODE.equals(json.get("status"))) {

			String orderNo = (String) json.get("order_no");

			System.out.println("orderNo :" + orderNo);

			if (StringUtils.isBlank(orderNo)) {
				// TODO: 如果返回orderNo为空，处理异常
				return;
			}

			// TODO : 处理商户逻辑
			return;
		}

		// TODO : 处理异常状态
		return;
	}
}
