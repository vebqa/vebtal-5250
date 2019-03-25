package org.vebqa.vebtal.telenese;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn5250restserver.Tn5250TestAdaptionPlugin;

import com.terminaldriver.tn5250j.TerminalDriver;

@Keyword(module = Tn5250TestAdaptionPlugin.ID, command = "pause", description = "pause for some time", hintTarget = "<empty>", hintValue = "<empty")
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
		this.type = CommandType.ACTION;
	}

	/**
	 * Implementation of WaitForText command.
	 */
	@Override
	public Response executeImpl(Object aDriver) {
		TerminalDriver driver = (TerminalDriver)aDriver;
		int tWait = Integer.parseInt(target);
		logger.debug("pause for some time: {} (ms)", target);
		Response tResp = new Response();

		try {
			Thread.sleep(tWait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		tResp.setCode(Response.PASSED);
		tResp.setMessage("Finished sleep");
		return tResp;
	}

}
