package org.vebqa.vebtal.telenese;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn5250restserver.Tn5250TestAdaptionPlugin;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.ScreenField;

@Keyword(module = Tn5250TestAdaptionPlugin.ID, command = "type", description = "type a string to a field", hintTarget = "<empty>", hintValue = "<empty")
public class Type extends AbstractCommand {

	public Type(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		TerminalDriver driver = (TerminalDriver)aDriver;
		Response tResp = new Response();

		// konvention: id=x
		int id = 0;
		String[] parts = target.split("=");
		switch (parts[0]) {
			case "id" :
				id = Integer.parseInt(parts[1]);
			default:
				break;
		}
		
		ScreenField field = null;
		if (id > 0) {
			field = driver.findFieldById(id);
		} else {
			// throw exception
		}
		if (field != null) {
			field.setString(this.value);
			tResp.setCode(Response.PASSED);
		} else {
			// Field not found!
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Field not found!");
		}
		
		return tResp;
	}

}
