package com.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;


@Controller
@RequestMapping("/login")
public class AccountControl {
	 HttpTransport httpTransport = new NetHttpTransport();
	    JacksonFactory jsonFactory = new JacksonFactory();
	static ObjectMapper objMapper = new ObjectMapper();
	
	@RequestMapping(value= "/GoogleLogin",method= RequestMethod.POST)
	@ResponseBody
	public String googleLogin(@RequestBody String data,HttpServletRequest req, HttpServletResponse res) throws JsonParseException, JsonMappingException, IOException{
		
		System.out.println("into controller");
		HashMap <String, Object> map1 = objMapper.readValue(data, new TypeReference<Map<String,Object>>(){
		});
		String code = (String) map1.get("code");

		GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(httpTransport,jsonFactory,AuthData.URL,AuthData.Client_Id,AuthData.Client_Secret,code,"http://localhost:8080").execute();
		
		String accessToken = tokenResponse.getAccessToken();
		System.out.println("access token is "+accessToken);
		URL url = new URL("https://people.googleapis.com/v1/contactGroups?access_token=" + accessToken);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		String line, outputString = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		while ((line = reader.readLine()) != null) {
			outputString += line;
		}
		return outputString;
	}
	
}
