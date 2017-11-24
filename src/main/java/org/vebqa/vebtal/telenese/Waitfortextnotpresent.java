package org.vebqa.vebtal.telenese;

import java.lang.annotation.Annotation;
import java.util.Date;

import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.How;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.util.ScreenUtils;

public class Waitfortextnotpresent extends AbstractCommand {

	public Waitfortextnotpresent(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
	}

	@Override
	public Response executeImpl(TerminalDriver driver) {
		// Refactor: Context 
		// target = value
		// column, row, labelText
		int tColumn = 0;
		int tRow = 0;
		String tLabelText = "";
		
		// Needs an equal
		String[] parts = target.split("=");
		switch (parts[0]) {
			case "column":
				tColumn = Integer.valueOf(parts[1]);
				break;
			case "row":
				tRow = Integer.valueOf(parts[1]);
				break;
		}
		
		final String labelText = tLabelText;
		final int row = tRow;
		final int column = tColumn;
		
		Response tResp = new Response();
		
		FindBy newFindBy = new FindBy() {
			
			public String text() {
				return value;
			}
			
			public int row() {
				return Integer.valueOf(row);
			}

			public Class<? extends Annotation> annotationType() {
				return null;
			}

			public How how() {
				return How.UNSET;
			}

			public String using() {
				return "";
			}

			public String labelText() {
				return labelText;
			}

			public String name() {
				return "";
			}

			public int column() {
				return column;
			}

			public int length() {
				return 0;
			}

			public ScreenAttribute attribute() {
				return ScreenAttribute.UNSET;
			}
						
		};
		
		// Vorbedingungen: es darf nicht nur nach WildCard gesucht werden
		if (value.contentEquals("*")) {
			tResp.setCode("1");
			tResp.setMessage("Search pattern contains wild card only.");
			return tResp;
		}
		
		boolean finished = false;
		boolean result = false;
		
		long now = new Date().getTime();
		long stop = now + 6000;
		
		while (!finished) {
			result = ScreenUtils.checkFindBy(newFindBy, driver);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!result || stop > new Date().getTime()) {
				finished = true;
			}
		}
		
		if (!result) {
			tResp.setCode("0");
		} else {
			tResp.setCode("1");
			tResp.setMessage("Text never disappeared: " + value);
		}
		return tResp;
	}

}
