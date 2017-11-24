package org.vebqa.vebtal.telenese;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.Key;

public class Keypressandwait extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Keypressandwait.class);
	
	public Keypressandwait(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
	}

	@Override
	public Response executeImpl(TerminalDriver driver) {
		Response tResp = new Response();
		boolean result = false;
		
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
		result = driver.waitForScreen(2000);
		
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
