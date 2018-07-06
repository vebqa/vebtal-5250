package org.vebqa.vebtal.telenese;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tn5250j.framework.tn5250.ScreenOIA;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.Key;

public class Keypress extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Keypress.class);
	
	public Keypress(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		TerminalDriver driver = (TerminalDriver)aDriver;
		Response tResp = new Response();
		
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
		if (driver.getSession().getScreen().getOIA().getLevel() == ScreenOIA.OIA_LEVEL_INPUT_ERROR) {
			tResp.setCode("1");
			tResp.setMessage("Error while sending keys to screen.");
			logger.info(driver.getDumpScreen());
		} else {
			logger.info(driver.getDumpScreen());
			tResp.setCode("0");
		}
		return tResp;
	}

}
