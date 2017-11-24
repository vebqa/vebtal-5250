package org.vebqa.vebtal.telenese;

import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;

public class Captureentirepagescreenshot extends AbstractCommand {

	public Captureentirepagescreenshot(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
	}

	@Override
	protected Response executeImpl(TerminalDriver driver) {
		Response tResp = new Response();
		
		return tResp;
	}

}
