package org.vebqa.vebtal.telenese;

import java.lang.annotation.Annotation;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn5250restserver.Tn5250TestAdaptionPlugin;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.How;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.util.ScreenUtils;

@Keyword(module = Tn5250TestAdaptionPlugin.ID, command = "verifyText", description = "verify that a text is visible", hintTarget = "<empty>", hintValue = "<empty")
public class Verifytext extends AbstractCommand {

	public Verifytext(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		TerminalDriver driver = (TerminalDriver)aDriver;

		// Refactor: Context
		// target = value
		// column, row, labelText
		int tColumn = 0;
		int tRow = 0;

		// Needs an equal
		String[] allToken = target.split(";");
		for (String token : allToken) {
			String[] parts = token.split("=");
			switch (parts[0]) {
			case "col":
				tColumn = Integer.parseInt(parts[1]);
				break;
			case "row":
				tRow = Integer.parseInt(parts[1]);
				break;
			default:
				break;
			}
		}
		final int sRow = tRow;
		final int sColumn = tColumn;

		Response tResp = new Response();

		FindBy newFindBy = new FindBy() {

			public String text() {
				return value;
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

		// Vorbedingungen: es darf nicht nur nach WildCard gesucht werden
		if (value.contentEquals("*")) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Search pattern contains wild card only.");
			return tResp;
		}

		boolean result = ScreenUtils.checkFindBy(newFindBy, driver);
		if (result) {
			tResp.setCode(Response.PASSED);
		} else {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Cannot find text: " + value);
		}
		return tResp;
	}
}