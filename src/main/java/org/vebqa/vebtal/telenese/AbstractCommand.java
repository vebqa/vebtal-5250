package org.vebqa.vebtal.telenese;

import org.vebqa.vebtal.command.ICommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;

import com.terminaldriver.tn5250j.TerminalDriver;

public abstract class AbstractCommand implements ICommand {
	
	protected final String command;
	protected final String target;
	protected final String value;
	
	protected CommandType type;
	
	public AbstractCommand(String aCommand, String aTarget, String aValue) {
		this.command = aCommand.trim();
		this.target = aTarget.trim();
		this.value = aValue.trim();
	}
	
	protected abstract Response executeImpl(TerminalDriver driver);
	
	public CommandType getType() {
		return this.type;
	}
}
