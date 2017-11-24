package org.vebqa.vebtal.telenese;


import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.ScreenField;

public class Type extends AbstractCommand {

	public Type(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
	}

	@Override
	public Response executeImpl(TerminalDriver driver) {
		Response tResp = new Response();

		// konvention: id=x
		int id = 0;
		String[] parts = target.split("=");
		switch (parts[0]) {
			case "id" :
				id = Integer.valueOf(parts[1]);
		}
		
		ScreenField field = null;
		if (id > 0) {
			field = driver.findFieldById(id);
		} else {
			// throw exception
		}
		if (field != null) {
			field.setString(this.value);
			tResp.setCode("0");
		} else {
			// Field not found!
			tResp.setCode("1");
			tResp.setMessage("Field not found!");
		}
		
		return tResp;
	}

}
