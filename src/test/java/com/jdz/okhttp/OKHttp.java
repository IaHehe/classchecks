package com.jdz.okhttp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.classchecks.model.User;
import com.framework.common.util.image.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttp {
	//鎻愪氦json鏁版嵁  
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    private static final MediaType MARKDOWN  = MediaType.parse("text/x-markdown; charset=utf-8");
    
    public static void main(String [] args) {
    	
    	String url = "http://192.168.155.1:8090/classchecks/index/do-upload-map";
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("username", "zoudongjun");
    	File file = new File("E:\\img\\zhou.jpg");
    	
    	try {
			post_file(url, map, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    private static void post_file(final String url, final Map<String, Object> map, File file) throws IOException {
    	OkHttpClient client = new OkHttpClient.Builder()
    			.connectTimeout(5, TimeUnit.SECONDS)
    			.build();
    	
    	/*RequestBody requestBody = new MultipartBody.Builder()
    			.setType(MultipartBody.FORM)
    			.addFormDataPart("username", "zoudongjun")
    			.addFormDataPart("file", file.getName(), RequestBody.create(MARKDOWN, file))
    			.build();
    	Request request = new Request.Builder()
    			.url(url)
    			.post(requestBody)
    			.build();
    	client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				System.out.println(response.body().string());
				
			}
			
			@Override
			public void onFailure(Call call, IOException e) {
				System.out.println("onFailure.....");
				System.out.println(call.request().url());
			}
		});*/
    	// form琛ㄥ崟褰㈠紡涓婁紶
    	MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
    	if(null != file) {
    		InputStream in = new FileInputStream(file);
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		
    		int len = 0;
    		byte [] buf = new byte[128];
    		while((len = in.read(buf)) != -1) {
    			baos.write(buf, 0, len);
    		}
    		in.close();
    		
    		
    		// MediaType.parse() 閲岄潰鏄笂浼犳枃浠剁殑鏂囦欢绫诲瀷
    		RequestBody body = RequestBody.create(MediaType.parse("image/*"), baos.toByteArray());
    		String filename = file.getName();
    		// 鍙傛暟鍒嗗埆涓猴紝璇锋眰key锛屾枃浠跺悕绉帮紝RequestBody
    		requestBody.addFormDataPart("file", filename, body);
    		if(!map.isEmpty()) {
    			// map 閲岄潰鏄姹備腑鎵�闇�瑕佺殑 key 鍜� value
    			for(Map.Entry<String, Object> entry: map.entrySet()) {
    				requestBody.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
    			}
    		}
    		Request request = new Request.Builder().
    				url(url).
    				post(requestBody.build())
    				.build();
    		client.newCall(request).enqueue(new Callback() {

				@Override
				public void onFailure(Call call, IOException e) {
					System.out.println("onFailure:");
					System.out.println(call.request().url());
					System.out.println(call.request().body().toString());
					
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					if(response.isSuccessful()) {
						System.out.println(response.body().string());
					}else {
						System.out.println("response error:" + response.message() + " | " +response.code());
					}
					
				}
    			
    		});
    	}
    }
    
    
	public static void submitString() throws IOException {
		final long start = System.currentTimeMillis();
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String base64 = ImageUtils.covertImageToBase64("E:\\img\\zou.jpg");
				//System.out.println(base64);
				 OkHttpClient client = new OkHttpClient.Builder().connectTimeout(50, TimeUnit.SECONDS).build();
		         String url = "http://192.168.155.1:8090/classchecks/index/do-face-validate";
		         // 鏋勫缓璇锋眰鐨勬暟鎹�
		         RequestBody requestBody = new FormBody.Builder()
		                 .add("imageString", base64)
		                 .build();

		         Request request = new Request.Builder()
		                 .url(url)
		                 .post(requestBody)
		                 .build();

		         client.newCall(request).enqueue(new Callback() {
		             @Override
		             public void onFailure(Call call, IOException e) {
		                // 璁块棶澶辫触锛屾墦鍗拌闂湴鍧�
		            	 Request r = call.request();
		            	 System.out.println("璇锋眰澶辫触锛�" + r.url());
		            	 System.out.println(r.body().contentType());
		            	 long end = System.currentTimeMillis();
							
		 				System.out.println("鑰楁椂锛�"+(end-start)/1000);
		             }

		             @Override
		             public void onResponse(Call call, final Response response) throws IOException {
		                 if(!response.isSuccessful()) {
		                	throw new IOException("鏈嶅姟鍣ㄩ敊璇�...."); 
		                 }
		                 System.out.println(response.body().string());
		                 long end = System.currentTimeMillis();
							
		 				System.out.println("鑰楁椂锛�"+(end-start)/1000);
		             }
		         });
		        
			}
		}).start();;

	}

	public static void okHttpReq() throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("http://www.hao123.com/")//https://www.baidu.com
				.build();
		Response response = client.newCall(request).execute();
		if(!response.isSuccessful()) {
			throw new IOException("鏈嶅姟绔敊璇細" + response);
		}
		
		Headers responseHeaders = response.headers();
		for(int i = 0; i < responseHeaders.size(); i++) {
			System.out.println(responseHeaders.name(i)+": " + responseHeaders.value(i));
		}
		System.out.println("--------------response body----------------");
		System.out.println(response.body().string());
	}
	
	public static void okHttpReqJOSN() {
		String url = "http://192.168.155.1:8090/classchecks/index/login";
        // 鏋勫缓璇锋眰鐨勬暟鎹�
        User user = new User();
        user.setId(100);
        user.setUsername("android->okhttp3");
        user.setPassword("datatype:json");
        try {
            Gson gson = new Gson();
            String JSONString = gson.toJson(user);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .build();

            RequestBody requestBody = RequestBody.create(JSON, JSONString);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()) {
                throw new IOException("鏈嶅姟鍣ㄦ病鏈夊搷搴�");
            }
            User user1 = gson.fromJson(response.body().string(), User.class);
           System.out.println(user1); 
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		/*User user = new User();
		user.setId(100);
		user.setUsername("okhttp post");
		user.setPassword("datatype json");
		Gson gson = new Gson();
		String jsonStr = gson.toJson(user);

		OkHttpClient client = new OkHttpClient
				.Builder()
				.connectTimeout(5, TimeUnit.SECONDS)
				.build();
		//鍒涘缓涓�涓猂equestBody(鍙傛暟1锛氭暟鎹被鍨� 鍙傛暟2浼犻�掔殑json涓�)
		RequestBody requestBody = RequestBody.create(JSON, jsonStr);
		String url = "http://localhost:8090/classchecks/index/login";
		Request request = new Request.Builder()
				.url(url)
				.post(requestBody)
				.build();
		Response response = client.newCall(request).execute();
		if(!response.isSuccessful()) {
			System.out.println("鏈嶅姟鍣ㄩ敊璇�");
		}else {
			User user1 = gson.fromJson(response.body().string(), User.class);
			System.out.println(user1);
		}*/
	}
	
}
