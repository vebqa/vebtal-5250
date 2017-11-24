package org.vebqa.vebtal.telenese;

import java.lang.annotation.Annotation;

import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.How;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.obj.ScreenTextBlock;
import com.terminaldriver.tn5250j.util.ScreenFieldReader;
import com.terminaldriver.tn5250j.util.ScreenUtils;

public class Storetext extends AbstractCommand {

	public Storetext(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
	}

	@Override
	public Response executeImpl(TerminalDriver driver) {
		Response tResp = new Response();

		// Refactor: Context
		// target = value
		// column, row, labelText
		int tColumn = 0;
		int tRow = 0;
		String tLabelText = "";

		// Beispiel: row=4;column=23
		String[] someToken = target.split(";");

		for (String aToken : someToken) {
			// Needs an equal
			String[] parts = aToken.split("=");
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
		}

		final String labelText = tLabelText;
		final int row = tRow;
		final int column = tColumn;

		FindBy newFindBy = new FindBy() {

			public String text() {
				return "";
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
		ScreenTextBlock block = ScreenUtils.applyFindScreenTextBlock(driver, newFindBy, new ScreenFieldReader(driver),
				0);
		if (block == null) {
			tResp.setCode("1");
			tResp.setMessage("Cannot find screen text block for given target.");
		} else {
			tResp.setCode("0");
			tResp.setStoredKey(this.value);
			tResp.setStoredValue(block.getString().trim());
		}
		return tResp;
	}

}
