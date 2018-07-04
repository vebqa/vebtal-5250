package org.vebqa.vebtal.telenese;

import java.io.IOException;

import org.vebqa.vebtal.GuiManager;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.sut.SutStatus;
import org.vebqa.vebtal.tn5250restserver.Tn5250TestAdaptionPlugin;

import com.terminaldriver.tn5250j.TerminalDriver;

public class Close extends AbstractCommand {

	public Close(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}
	
	@Override
	public Response executeImpl(TerminalDriver driver) {
		Response tResp = new Response();
		try {
			driver.close();
			tResp.setCode("0");
			tResp.setMessage("Successfully disconnected from host");
			GuiManager.getinstance().setTabStatus(Tn5250TestAdaptionPlugin.ID, SutStatus.DISCONNECTED);
		} catch (IOException e) {
			tResp.setCode("1");
			tResp.setMessage("Failed to disconnected from host, because: " + e.getMessage());
		}
		
		return tResp;
	}

}
