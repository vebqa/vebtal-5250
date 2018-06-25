package org.vebqa.vebtal.telenese;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import org.tn5250j.framework.tn5250.ScreenField;
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
		int tId = 0;

		boolean modeText = true;

		// Beispiel: row=4;column=23
		String[] someToken = target.split(";");

		for (String aToken : someToken) {
			// Needs an equal
			String[] parts = aToken.split("=");
			switch (parts[0]) {
			case "column":
				tColumn = Integer.parseInt(parts[1]);
				break;
			case "row":
				tRow = Integer.parseInt(parts[1]);
				break;
			case "id":
				tId = Integer.parseInt(parts[1]);
				modeText = false;
				break;
			}
		}

		if (modeText) {
			final int sRow = tRow;
			final int sColumn = tColumn;

			FindBy newFindBy = new FindBy() {

				public String text() {
					return "";
				}

				public int row() {
					return sRow;
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
					return "";
				}

				public String name() {
					return "";
				}

				public int column() {
					return sColumn;
				}

				public int length() {
					return 0;
				}

				public ScreenAttribute attribute() {
					return ScreenAttribute.UNSET;
				}

			};
			ScreenTextBlock block = ScreenUtils.applyFindScreenTextBlock(driver, newFindBy,
					new ScreenFieldReader(driver), 0);
			if (block == null) {
				tResp.setCode("1");
				tResp.setMessage("Cannot find screen text block for given target.");
			} else {
				tResp.setCode("0");
				tResp.setStoredKey(this.value);
				tResp.setStoredValue(block.getString().trim());
			}
		} else {
			// find by input id
			final List<org.tn5250j.framework.tn5250.ScreenField> screenFields = Arrays
					.asList(driver.getSession().getScreen().getScreenFields().getFields());
			
			if (!screenFields.isEmpty() && screenFields.size() >= tId) {
				ScreenField sScreenField = screenFields.get(tId - 1);
				tResp.setCode("0");
				tResp.setStoredKey(this.value);
				tResp.setStoredValue(sScreenField.getString());
			} else {
				tResp.setCode("1");
				tResp.setMessage("Cannot find a screenfield with given id="+tId + "!");
			}
		}
		return tResp;
	}

}
