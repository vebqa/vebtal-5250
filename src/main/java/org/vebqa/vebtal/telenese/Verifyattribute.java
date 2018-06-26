package org.vebqa.vebtal.telenese;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.obj.ScreenTextBlock;
import com.terminaldriver.tn5250j.util.ScreenFieldReader;

public class Verifyattribute extends AbstractCommand {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(Verifyattribute.class);

	public Verifyattribute(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
	}

	@Override
	public Response executeImpl(TerminalDriver driver) {
		// Refactor: Context
		// target = value
		// column, row, labelText
		String tText = "";

		// Needs an equal
		String[] parts = target.split("=");
		switch (parts[0]) {
		case "text":
			tText = parts[1].trim();
			break;
		}

		Response tResp = new Response();

		// Vorbedingungen: es darf nicht nur nach WildCard gesucht werden
		if (value.contentEquals("*")) {
			tResp.setCode("1");
			tResp.setMessage("Search pattern contains wild card only.");
			return tResp;
		}

		final ScreenFieldReader reader = new ScreenFieldReader(driver);
		reader.seek(0);
		ScreenTextBlock field = null;

		boolean found = false;
		boolean unique = true;
		ScreenTextBlock resultField = null;

		while ((field = reader.readField()) != null) {
			logger.info("f-><{}> ->attr-><{}>", field.getString(), field.getAttribute());

			if (tText.startsWith("*") && tText.endsWith("*")) {
				if (field.getString().contains(tText.substring(1, tText.length() - 1))) {
					if (found) {
						unique = false;
					} else {
						resultField = field;
					}
					found = true;
				}
			} else if (tText.startsWith("*")) {
				if (field.getString().endsWith(tText.substring(1))) {
					if (found) {
						unique = false;
					} else {
						resultField = field;
					}
					found = true;
				}
			} else if (tText.endsWith("*")) {
				if (field.getString().startsWith(tText.substring(0, tText.length() - 1))) {
					if (found) {
						unique = false;
					} else {
						resultField = field;
					}
					found = true;
				}
			} else {
				if (field.getString().contentEquals(tText)) {
					if (found) {
						unique = false;
					} else {
						resultField = field;
					}
					found = true;
				}
			}
		}

		if (found == false) {
			tResp.setCode("1");
			tResp.setMessage("Cannot find screenfield for text: " + tText);
		} else if (unique == false) {
			tResp.setCode("1");
			tResp.setMessage("Found multiple screenfields for text: " + tText);

		} else if (resultField != null) {
			ScreenAttribute searchScrAttr = ScreenAttribute.getAttrEnum(this.value);
			ScreenAttribute actualAttribute = ScreenAttribute.getAttrEnum(resultField.getAttribute());
			
			if (searchScrAttr == actualAttribute) {
				tResp.setCode("0");
			} else {
				tResp.setCode("1");
				tResp.setMessage(
						"Expected attribute is <{" + searchScrAttr.getHexCode() + "}>, but found attribute is <{" + actualAttribute.getHexCode() + "}>.");
			}
		}
		return tResp;
	}

}
