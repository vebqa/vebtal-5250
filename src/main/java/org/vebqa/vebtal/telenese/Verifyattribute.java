package org.vebqa.vebtal.telenese;

import java.lang.annotation.Annotation;

import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.How;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.util.ScreenUtils;

public class Verifyattribute extends AbstractCommand {

	public Verifyattribute(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
	}

	@Override
	protected Response executeImpl(TerminalDriver driver) {
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
			case "labelText":
				tLabelText = parts[1];
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
		
		boolean result = ScreenUtils.checkFindBy(newFindBy, driver);
		if (result) {
			tResp.setCode("0");
		} else {
			tResp.setCode("1");
			tResp.setMessage("Cannot find text: " + value);
		}
		return tResp;
	}

}
