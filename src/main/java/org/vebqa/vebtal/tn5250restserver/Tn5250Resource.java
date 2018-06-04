package org.vebqa.vebtal.tn5250restserver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.TestAdaptionResource;
import org.vebqa.vebtal.model.Command;
import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;

public class Tn5250Resource implements TestAdaptionResource {
	
	private static final Logger logger = LoggerFactory.getLogger(Tn5250Resource.class);

	/**
	 * Terminal
	 */
	private static TerminalDriver driver;
	
	public Response execute(Command cmd) {
		Tn5250TestAdaptionPlugin.addCommandToList(cmd);
		
		Response tResponse = new Response();
		
		// Test - to be refactored
		// Command instanziieren
		// erst alles klein schreiben
		String tCmd = cmd.getCommand().toLowerCase().trim();
		// erster Buchstabe gross
		String cmdFL = tCmd.substring(0, 1).toUpperCase(); 
		String cmdRest = tCmd.substring(1);
		tCmd = cmdFL + cmdRest;

		String tClass = "org.vebqa.vebtal.telenese." + tCmd;
		Response result = null;
		try {
			Class<?> cmdClass = Class.forName(tClass);
			Constructor<?> cons = cmdClass.getConstructor(String.class, String.class, String.class);
			Object cmdObj = cons.newInstance(cmd.getCommand(), cmd.getTarget(), cmd.getValue());
			Method m = cmdClass.getDeclaredMethod("executeImpl", TerminalDriver.class);
			result = (Response)m.invoke(cmdObj, driver);
		} catch (ClassNotFoundException e) {
			logger.error("Command implementation class not found!", e);
		} catch (NoSuchMethodException e) {
			logger.error("Execution method in command implementation class not found!", e);
		} catch (SecurityException e) {
			logger.error("Security exception", e);
		} catch (InstantiationException e) {
			logger.error("Cannot instantiate command implementation class!", e);
		} catch (IllegalAccessException e) {
			logger.error("Cannot access implementation class!", e);
		} catch (IllegalArgumentException e) {
			logger.error("Wrong arguments!", e);
		} catch (InvocationTargetException e) {
			logger.error("Error while invoking class!", e);
		}
		
		if (result == null) {
			tResponse.setCode("1");
			tResponse.setMessage("Cannot resolve findby.");
			return tResponse;
		}
		if (result.getCode() != "0") {
			Tn5250TestAdaptionPlugin.setLatestResult(false, result.getMessage());
		} else {
			String aDump = driver.getDumpScreen();
			// Password inside?
			if (tCmd.toLowerCase().indexOf("password") > 0) {
				aDump = aDump.replaceAll(cmd.getValue(), "**********");
			}
			Tn5250TestAdaptionPlugin.setLatestResult(true, aDump);
		}
		return result;
	}
	
	public static void setDriver(TerminalDriver aDriver) {
		driver = aDriver;
	}
}
