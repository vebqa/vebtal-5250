package org.vebqa.vebtal.telenese;

import java.lang.annotation.Annotation;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.How;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.util.ScreenUtils;

public class Pause extends AbstractCommand {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(Waitfortext.class);

	/**
	 * Constructor
	 * 
	 * @param aCommand
	 * @param aTarget
	 * @param aValue
	 */
	public Pause(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
	}

	/**
	 * Implementation of WaitForText command.
	 */
	@Override
	public Response executeImpl(TerminalDriver driver) {
		int tWait = Integer.parseInt(target);

		Response tResp = new Response();

		try {
			Thread.sleep(tWait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		tResp.setCode("0");
		tResp.setMessage("Finished sleep");
		return tResp;
	}

}
