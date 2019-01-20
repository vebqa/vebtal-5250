package org.vebqa.vebtal.telenese;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.How;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.util.ScreenUtils;

public class Waitfortext extends AbstractCommand {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(Waitfortext.class);

	/**
	 * Constructor
	 * 
	 * @param aCommand
	 *            command
	 * @param aTarget
	 *            target
	 * @param aValue
	 *            value
	 */
	public Waitfortext(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	/**
	 * Implementation of WaitForText command.
	 */
	@Override
	public Response executeImpl(Object aDriver) {
		TerminalDriver driver = (TerminalDriver)aDriver;
		// Refactor: Context
		// target = value
		// column, row, labelText
		int tColumn = 0;
		int tRow = 0;
		long tTimeOut = 10000L;

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
			case "timeout":
				tTimeOut = Long.parseLong(parts[1]);
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

		boolean finished = false;
		boolean result = false;

		long now = new Date().getTime();
		long stop = now + tTimeOut;
		long testTime = 0L;

		while (!finished) {
			result = ScreenUtils.checkFindBy(newFindBy, driver);

			if (!result) {
				driver.waitForUpdate(250L);
			}

			testTime = Calendar.getInstance().getTime().getTime();
			if (result || stop > testTime) {
				finished = true;
			}
		}

		if (result) {
			tResp.setCode(Response.PASSED);
			tResp.setMessage("Text: " + value + " found after : " + (testTime - now) + " ms.");
		} else {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Text: " + value + " is not visible after waiting for: " + (stop - testTime) + " ms.");
		}
		return tResp;
	}
}