package org.vebqa.vebtal.telenese;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;

public class Pause extends AbstractCommand {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(Pause.class);

	/**
	 * Constructor
	 * 
	 * @param aCommand  command
	 * @param aTarget	target
	 * @param aValue	value
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
		logger.debug("pause for some time: {} (ms)", target);
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
