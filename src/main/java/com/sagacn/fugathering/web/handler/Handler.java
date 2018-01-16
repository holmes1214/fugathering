package com.sagacn.fugathering.web.handler;


import com.sagacn.fugathering.dto.ClientRequest;

public interface Handler {

	 Object process(ClientRequest clientRequest) throws Exception;
	
	 boolean checkToken();
	
	 boolean validateData(ClientRequest clientRequest);
}
