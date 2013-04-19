package services.javatohttp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.params.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class HttpCli {
	final public Logger logger = LoggerFactory.getLogger(this.getClass()); 
	private static String sessionId="";

	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param queryString
	 *            请求的查询参数,可以为null
	 * @return 返回请求响应的HTML
	 */
	public  String get1(String url) {
		String strResult = null;
		int token = (int)Math.floor(Math.random() * 100000);
		/*try {//转码
			name = URLEncoder.encode(name, "UTF-8");
			queryString = URLEncoder.encode(queryString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String uriAPI = "http://127.0.0.1/html4/login_mobile.php?"
				+ name
				+ "="
				+ queryString;
				*/

		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url+"&_token="+token);
		HttpResponse httpResponse;
		
		try {
			if (sessionId != null||sessionId!="") {
                //设置sessionid，把第一次请求的id放在之后要请求的request报文头里
				httpGet.setHeader("Cookie", sessionId);
				logger.info(" Cookie: {}",  sessionId );
				  
				//System.out.println("sessionid1:"+sessionId);
			}
		    client.getParams().setParameter(HttpProtocolParams.HTTP_CONTENT_CHARSET,"UTF-8"); 
			//httpGet.setHeader( "Connection", "Keep-Alive" );
			httpResponse = client.execute(httpGet);


			if (httpResponse.getStatusLine().getStatusCode() == 200) 
			{
				// 第3步：使用getEntity方法获得返回结果
				strResult = EntityUtils.toString(httpResponse.getEntity());
				/*HttpEntity entity = httpResponse.getEntity();
				StringBuffer sb = new StringBuffer();
				if (entity != null) {
					InputStream is = entity.getContent();
					byte[] bytes = new byte[4096];
					int size = 0;
					while ((size = is.read(bytes)) > 0) {
						String str = new String(bytes, 0, size, "UTF-8");
						sb.append(str);
					}
				is.close();
				}
				strResult = sb.toString();*/
				
				// 去掉返回结果中的"\r"字符，
				Header[] headers = httpResponse.getHeaders("set-cookie");
				if (sessionId == null||sessionId.equals(""))
				{
				for (int i = 0; i < headers.length; i++) {
//                  Log.e("sessionid", headers<i>.getValue());
					System.out.println("headers:"+i+":"+headers[i]);
                  String value = headers[i].getValue();
                  
                  sessionId = value.substring(0, value.indexOf(";"));
				}
				}
				//System.out.println("sessionid2:"+sessionId);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return strResult;
	}


	/**
	 * 执行一个HTTP POST请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @return 返回请求响应的HTML
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static String doPost(String url, Map<String, String> params)
			throws IllegalStateException, IOException {
		String strResult = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			postData.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
			System.out.print(entry.getValue());
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData,HTTP.UTF_8);//过时了?
		post.setEntity(entity);
		HttpResponse response = httpClient.execute(post);


		// 若状态码为200 ok
		if (response.getStatusLine().getStatusCode() == 200) {
			// 取出回应字串
			strResult = EntityUtils.toString(response.getEntity());
		}
		return strResult;
	}


	/*public static void main(String[] args) throws IllegalStateException,
			IOException {
		HashMap<String, String> user = new HashMap<String, String>();
		user.put("username", "admin");
		user.put("password", "123");
		String post = doPost("http://127.0.0.1/html4/login_mobile.php", user);
		String get = get1("http://127.0.0.1/html4/login_mobile.php", "name",
				"admin");
		System.out.println("Post:" + post);
		System.out.println("Get:" + get);
	}*/
}
