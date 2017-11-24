package org.vebqa.vebtal.teleneserestserver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Command;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.model.Session;

import com.terminaldriver.tn5250j.TerminalDriver;

@Path("telenese")
public class TeleneseResource {
	
	private static final Logger logger = LoggerFactory.getLogger(TeleneseResource.class);

	/**
	 * Terminal
	 */
	private static TerminalDriver driver;
	
	@POST
	@Path("execute")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeTelenese(Command cmd) {
		Tn5250TestAdaptionPlugin.addCommandToList(cmd);
		
		Response tResponse = new Response();
		
		// Test - to be refactored
		// Command instanziieren
		// erst alles klein schreiben
		String tCmd = cmd.getCommand().toLowerCase().trim();
		// erster Buchstabe gross
		tCmd = WordUtils.capitalizeFully(tCmd);
		String tClass = "org.veba.roborest.telenese." + tCmd;
		Response result = null;
		try {
			Class<?> cmdClass = Class.forName(tClass);
			Constructor<?> cons = cmdClass.getConstructor(String.class, String.class, String.class);
			Object cmdObj = cons.newInstance(cmd.getCommand(), cmd.getTarget(), cmd.getValue());
			Method m = cmdClass.getDeclaredMethod("executeImpl", TerminalDriver.class);
			result = (Response)m.invoke(cmdObj, driver);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	@POST
	@Path("createsession")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createSession(Session sess) {
		Tn5250TestAdaptionPlugin.addCommandToList(sess);
		
		this.driver =  new TerminalDriver();
		
		Map<String, Object> configs = new HashMap<String, Object>();
		
		// create the config
		if (sess.getCodepage() != null) {
			configs.put("codePage", sess.getCodepage());
		}
		
		if (sess.getSsltype() != null) {
			configs.put("SSL_TYPE", sess.getSsltype());
		}
		
		// connect to system with custom config
		this.driver.connectTo(sess.getHost(), Integer.valueOf(sess.getPort()), configs);
		
		Response tResponse = new Response();
		tResponse.setCode("0");
		Tn5250TestAdaptionPlugin.setLatestResult(true, this.driver.getDumpScreen());
		return tResponse;
	}	
}
