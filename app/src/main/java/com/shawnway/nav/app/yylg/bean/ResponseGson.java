package com.shawnway.nav.app.yylg.bean;

import java.util.ArrayList;

public class ResponseGson<T> {
	T body;
	HeaderGson header;

	
	public T getBody() {
		return body;
	}
	
	public void setBody(T body) {
		this.body = body;
	}
	
	public HeaderGson getHeader() {
		return header;
	}
	
	public void setHeader(HeaderGson header) {
		this.header = header;
	}
	


	public String getCode(){
		return getHeader().getCode();
	}

	public String getError(){
		return getHeader().getError();
	}

	public ArrayList<HeaderGson.FieldError> getFields(){
		return getHeader().getFieldErrors();
	}
}
