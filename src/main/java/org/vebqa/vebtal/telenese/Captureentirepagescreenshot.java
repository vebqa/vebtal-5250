package org.vebqa.vebtal.telenese;

import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;

public class Captureentirepagescreenshot extends AbstractCommand {

	public Captureentirepagescreenshot(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.UTILLITY;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		TerminalDriver driver = (TerminalDriver)aDriver;
		Response tResp = new Response();
		
		return tResp;
	}

}
