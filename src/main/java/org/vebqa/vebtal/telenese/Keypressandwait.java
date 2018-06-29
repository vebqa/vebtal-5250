package org.vebqa.vebtal.telenese;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.Key;

public class Keypressandwait extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Keypressandwait.class);
	
	public Keypressandwait(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(TerminalDriver driver) {
		Response tResp = new Response();
		boolean result = false;
		
		long tTimeOut = 10000L;

		// Needs an equal
		String[] allToken = target.split(";");
		for (String token : allToken) {
			String[] parts = token.split("=");
			switch (parts[0]) {
			case "timeout":
				tTimeOut = Long.parseLong(parts[1]);
				break;
			default:
				break;
			}
		}		
		
		String aButton = this.value.toUpperCase();
		if (aButton.startsWith("[") && aButton.endsWith("]")) {
			aButton = aButton.substring(1, aButton.length());
			aButton = aButton.substring(0, aButton.length() - 1);
		} else {
			tResp.setCode("1");
			tResp.setMessage("Need a Key as Value, e.g. [<Key>]");
			return tResp;
		}
		
		// Generischer Ansatz
		driver.keys().press(Key.valueOf(aButton));
		result = driver.waitForScreen(tTimeOut);
		
		if (result) {
			tResp.setCode("0");
		} else {
			logger.info(driver.getDumpScreen());
			tResp.setCode("1");
			tResp.setMessage("Error while waiting for a screen update.");
		}
		return tResp;
	}

}
