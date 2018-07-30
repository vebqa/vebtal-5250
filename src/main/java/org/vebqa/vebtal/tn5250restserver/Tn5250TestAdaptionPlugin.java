package org.vebqa.vebtal.tn5250restserver;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.vebqa.vebtal.AbstractTestAdaptionPlugin;
import org.vebqa.vebtal.TestAdaptionType;
import org.vebqa.vebtal.model.Command;
import org.vebqa.vebtal.model.CommandResult;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Session;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;

@SuppressWarnings("restriction")
public class Tn5250TestAdaptionPlugin extends AbstractTestAdaptionPlugin {

	public static final String ID = "tn5250";
	
	public Tn5250TestAdaptionPlugin() {
		super(TestAdaptionType.ADAPTER);
	}
	
	private static final TableView<CommandResult> commandList = new TableView<>();
	private static final ObservableList<CommandResult> clData = FXCollections.observableArrayList();
	
	public String getName() {
		return "Telenese Plugin for RoboManager";
	}

	@Override
	public Class<?> getImplementation() {
		return null;
	}
	
	@Override
	public Tab startup() {
		return createTab(ID, commandList, clData);
	}
	
	public static void addCommandToList(Command aCmd, CommandType aType) {
		String aValue = aCmd.getValue();
		if (aCmd.getCommand().toLowerCase().indexOf("password") > 0) {
			aValue = "*****";
		}
		CommandResult tCR = new CommandResult(aCmd.getCommand(), aCmd.getTarget(), aValue, aType);
		Platform.runLater(() -> clData.add(tCR));
	}
	
	public static void setLatestResult(Boolean success, final String aResult) {
		Platform.runLater(() -> clData.get(clData.size() - 1).setLogInfo(aResult));
		Platform.runLater(() -> clData.get(clData.size() - 1).setResult(success));
		Platform.runLater(() -> commandList.scrollTo(clData.size() - 1));
		Platform.runLater(() -> commandList.refresh());
	}
	
	@Override
	public boolean shutdown() {
		return true;
	}
	
	@Override
	public String getAdaptionID() {
		return ID;
	}

	@Override
	public FileBasedConfiguration loadConfigString() {
		// TODO Auto-generated method stub
		return null;
	}		
}
