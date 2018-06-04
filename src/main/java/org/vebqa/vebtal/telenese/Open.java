package org.vebqa.vebtal.telenese;

import java.util.HashMap;
import java.util.Map;

import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn5250restserver.Tn5250Resource;

import com.terminaldriver.tn5250j.TerminalDriver;

public class Open extends AbstractCommand {

	public Open(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
	}

	@Override
	public Response executeImpl(TerminalDriver driver) {

		// Refactor: Context
		// target = value
		// column, row, labelText
		String host = "localhost";
		int port = 23;
		int codepage = 1141;
		String ssltype = "sslv3";
		
		// Beispiel: target= host=ctbtest;port=992;codepage=1141;ssltype=sslv3
		String[] someToken = target.split(";");

		for (String aToken : someToken) {
			// Needs an equal
			String[] parts = aToken.split("=");
			switch (parts[0]) {
			case "host":
				host = String.valueOf(parts[1]);
				break;
			case "port":
				port = Integer.valueOf(parts[1]);
				break;
			case "codepage":
				codepage = Integer.valueOf(parts[1]);
				break;
			case "ssltype":
				ssltype = String.valueOf(parts[1]);
				break;
			}
		}

		driver =  new TerminalDriver();
		
		Map<String, Object> configs = new HashMap<String, Object>();
		
		// create the config
		configs.put("codePage", String.valueOf(codepage));
		
		configs.put("SSL_TYPE", ssltype);
		
		// connect to system with custom config
		driver.connectTo(host, port, configs);
		Tn5250Resource.setDriver(driver);
		
		Response tResponse = new Response();
		tResponse.setCode("0");
		return tResponse;
	}

}
