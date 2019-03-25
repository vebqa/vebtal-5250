package org.vebqa.vebtal.telenese;

import java.io.IOException;

import org.vebqa.vebtal.GuiManager;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.sut.SutStatus;
import org.vebqa.vebtal.tn5250restserver.Tn5250TestAdaptionPlugin;

import com.terminaldriver.tn5250j.TerminalDriver;

@Keyword(module = Tn5250TestAdaptionPlugin.ID, command = "close", description = "Close terminal", hintTarget = "<empty>", hintValue = "<empty")
public class Close extends AbstractCommand {

	public Close(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}
	
	@Override
	public Response executeImpl(Object aDriver) {
		TerminalDriver driver = (TerminalDriver)aDriver;
		Response tResp = new Response();
		try {
			driver.close();
			tResp.setCode(Response.PASSED);
			tResp.setMessage("Successfully disconnected from host");
			GuiManager.getinstance().setTabStatus(Tn5250TestAdaptionPlugin.ID, SutStatus.DISCONNECTED);
		} catch (IOException e) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Failed to disconnected from host, because: " + e.getMessage());
		}
		return tResp;
	}
}