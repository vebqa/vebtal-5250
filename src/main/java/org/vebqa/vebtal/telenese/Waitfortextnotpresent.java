package org.vebqa.vebtal.telenese;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.How;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.util.ScreenUtils;

public class Waitfortextnotpresent extends AbstractCommand {
	
	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(Waitfortextnotpresent.class);
	
	public Waitfortextnotpresent(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(TerminalDriver driver) {
		// Refactor: Context
		// target = value
		// column, row, labelText
		int tColumn = 0;
		int tRow = 0;
		long tTimeOut = 10000L;

		// Beispiel: row=4;col=23
		String[] someToken = target.split(";");

		for (String aToken : someToken) {
			// Needs an equal
			String[] parts = aToken.split("=");
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
			tResp.setCode("1");
			tResp.setMessage("Search pattern contains wild card only.");
			return tResp;
		}

		boolean finished = false;
		boolean result = false;

		long now = new Date().getTime();
		long stop = now + tTimeOut;

		while (!finished) {
			result = ScreenUtils.checkFindBy(newFindBy, driver);

			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				logger.error("I got interrupted!", e);
			}
			if (!result || stop > Calendar.getInstance().getTime().getTime()) {
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
