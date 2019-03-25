package org.vebqa.vebtal.telenese;

import java.util.HashMap;
import java.util.Map;

import org.vebqa.vebtal.GuiManager;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.sut.SutStatus;
import org.vebqa.vebtal.tn5250restserver.Tn5250Resource;
import org.vebqa.vebtal.tn5250restserver.Tn5250TestAdaptionPlugin;

import com.terminaldriver.tn5250j.TerminalDriver;

@Keyword(module = Tn5250TestAdaptionPlugin.ID, command = "open", description = "open terminal", hintTarget = "<empty>", hintValue = "<empty")
public class Open extends AbstractCommand {

	public Open(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		TerminalDriver driver = (TerminalDriver)aDriver;

		// Refactor: Context
		// target = value
		// column, row, labelText
		String host = "localhost";
		int port = 23;
		int codepage = 1141;
		String ssltype = "sslv3";
		
		String[] someToken = target.split(";");

		for (String aToken : someToken) {
			// Needs an equal
			String[] parts = aToken.split("=");
			switch (parts[0]) {
			case "host":
				host = String.valueOf(parts[1]);
				break;
			case "port":
				port = Integer.parseInt(parts[1]);
				break;
			case "codepage":
				codepage = Integer.parseInt(parts[1]);
				break;
			case "ssltype":
				ssltype = String.valueOf(parts[1]);
				break;
			default:
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
		tResponse.setCode(Response.PASSED);
		
		GuiManager.getinstance().setTabStatus(Tn5250TestAdaptionPlugin.ID, SutStatus.CONNECTED);
		
		return tResponse;
	}
}