package com.eastinno.otransos.payment.tencent.mpweixin.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.eastinno.otransos.payment.tencent.mpweixin.utils.http.HttpClientConnectionManager;

public class GetWxOrderno {
	public static DefaultHttpClient httpclient;

	static {
		httpclient = new DefaultHttpClient();
		httpclient = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient);
	}

	/**
	 * description:获取预支付id
	 * 
	 * @param url
	 * @param xmlParam
	 * @return
	 * @author ex_yangxiaoyi
	 * @see
	 */
	// public static String getPayNo(String url, String xmlParam) {
	// DefaultHttpClient client = new DefaultHttpClient();
	// client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS,
	// true);
	// HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);
	// String prepay_id = "";
	// try {
	// httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
	// HttpResponse response = httpclient.execute(httpost);
	// String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
	// System.out.println("返回预支付信息：" + jsonStr);
	// if (jsonStr.indexOf("FAIL") != -1) {
	// return prepay_id;
	// }
	// Map map = doXMLParse(jsonStr);
	// prepay_id = (String) map.get("prepay_id");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return prepay_id;
	// }
	public static String getPayNo(String url, String xmlParam) {
		CloseableHttpClient httpClient = createSSLClientDefault();
		HttpPost post = new HttpPost(url);

		CloseableHttpResponse response = null;
		String prepay_id = "";
		post.setEntity(new StringEntity(xmlParam, "UTF-8"));

		String jsonStr = "";

		try {
			response = httpClient.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					jsonStr = EntityUtils.toString(entity, "UTF-8");
				}
			}

			System.out.println("返回预支付信息：" + jsonStr);
			if (jsonStr.indexOf("FAIL") != -1) {
				System.out.println("支付错误！！！！！！");
			}
			Map map = doXMLParse(jsonStr);
			prepay_id = (String) map.get("prepay_id");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != response) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException ex) {

				}
			}
		}
		return prepay_id;
	}

	private static CloseableHttpClient createSSLClientDefault() {

		SSLContext sslContext;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				@Override
				public boolean isTrusted(X509Certificate[] xcs, String string) {
					return true;
				}
			}).build();

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);

			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyStoreException ex) {
			ex.printStackTrace();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (KeyManagementException ex) {
			ex.printStackTrace();
		}

		return HttpClients.createDefault();
	}

	/**
	 * description:获取扫码支付连接
	 * 
	 * @param url
	 * @param xmlParam
	 * @return
	 * @author ex_yangxiaoyi
	 * @see
	 */
	public static String getCodeUrl(String url, String xmlParam) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);
		String code_url = "";
		try {
			httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
			HttpResponse response = httpclient.execute(httpost);
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			if (jsonStr.indexOf("FAIL") != -1) {
				return code_url;
			}
			Map map = doXMLParse(jsonStr);
			code_url = (String) map.get("code_url");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code_url;
	}

	/**
	 * 转钱到普通账户
	 * 
	 * @param url
	 * @param xmlParam
	 * @return
	 */
	public static Map<String, Object> tranWeisinreturn(String url, String xmlParam) {
		Map map = null;
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);
		try {
			httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
			HttpResponse response = httpclient.execute(httpost);
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			map = doXMLParse(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 转钱到普通账户2
	 * 
	 * @param url
	 * @param xmlParam
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> tranWeisinreturn2(String url, String xmlParam) throws Exception {
		Map map = null;
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream instream = new FileInputStream(new File("D:/10016225.p12"));
		try {
			keyStore.load(instream, "10016225".toCharArray());
		} finally {
			instream.close();
		}

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "10016225".toCharArray()).build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);
		try {
			httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
			HttpResponse response = httpclient.execute(httpost);
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			map = doXMLParse(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 * 
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map doXMLParse(String strxml) throws Exception {
		if (null == strxml || "".equals(strxml)) {
			return null;
		}

		Map m = new HashMap();
		InputStream in = String2Inputstream(strxml);
		SAXBuilder builder = new SAXBuilder();

		String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
		builder.setFeature(FEATURE, true);
		FEATURE = "http://xml.org/sax/features/external-general-entities";
		builder.setFeature(FEATURE, false);
		FEATURE = "http://xml.org/sax/features/external-parameter-entities";
		builder.setFeature(FEATURE, false);
		// Disable external DTDs as well
		FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
		builder.setFeature(FEATURE, false);

		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}

			m.put(k, v);
		}

		// 关闭流
		in.close();

		return m;
	}

	/**
	 * 获取子结点的xml
	 * 
	 * @param children
	 * @return String
	 */
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
	}

	public static InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}

}