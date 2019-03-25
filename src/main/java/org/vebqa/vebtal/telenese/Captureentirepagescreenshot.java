package org.vebqa.vebtal.telenese;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn5250restserver.Tn5250TestAdaptionPlugin;

import com.terminaldriver.tn5250j.TerminalDriver;

@Keyword(module = Tn5250TestAdaptionPlugin.ID, command = "captureEntirePageScreenshot", description = "take a screenshot", hintTarget = "<empty>", hintValue = "<empty")
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
