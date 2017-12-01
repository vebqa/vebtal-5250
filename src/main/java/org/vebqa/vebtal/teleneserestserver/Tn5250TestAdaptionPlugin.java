package org.vebqa.vebtal.teleneserestserver;

import org.vebqa.vebtal.AbstractTestAdaptionPlugin;
import org.vebqa.vebtal.TestAdaptionType;
import org.vebqa.vebtal.model.Command;
import org.vebqa.vebtal.model.CommandResult;
import org.vebqa.vebtal.model.Session;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

@SuppressWarnings("restriction")
public class Tn5250TestAdaptionPlugin extends AbstractTestAdaptionPlugin {

	public Tn5250TestAdaptionPlugin() {
		super(TestAdaptionType.ADAPTER);
	}
	
	/** Start/Stop Button **/
	private static final Button btnStartStop = new Button();
	
	private static final TableView<CommandResult> commandList = new TableView<>();
	private static final ObservableList<CommandResult> clData = FXCollections.observableArrayList();

	
	public String getName() {
		return "Telenese Plugin for RoboManager";
	}

	@Override
	public Class getImplementation() {
		return TeleneseResource.class;
	}
	
	@Override
	public Tab startup() {
		// Richtet den Plugin-spezifischen Tab ein
		
		Tab seleneseTab = new Tab();
		seleneseTab.setText("Telenese / TN5250");
		
		btnStartStop.setText("Dump screen");
		btnStartStop.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Dump screen and write to log
			}
		});

		// LogBox
		BorderPane root = new BorderPane();

		// Top bauen
		HBox hbox = new HBox();
		hbox.getChildren().addAll(btnStartStop);

		// Table bauen
		TableColumn selCommand = new TableColumn("Command");
		selCommand.setCellValueFactory(new PropertyValueFactory<CommandResult, String>("command"));
		selCommand.setSortable(false);
		selCommand.prefWidthProperty().bind(commandList.widthProperty().multiply(0.15));

		TableColumn selTarget = new TableColumn("Target");
		selTarget.setCellValueFactory(new PropertyValueFactory<CommandResult, String>("target"));
		selTarget.setSortable(false);
		selTarget.prefWidthProperty().bind(commandList.widthProperty().multiply(0.15));

		TableColumn selValue = new TableColumn("Value");
		selValue.setCellValueFactory(new PropertyValueFactory<CommandResult, String>("value"));
		selValue.setSortable(false);
		selValue.prefWidthProperty().bind(commandList.widthProperty().multiply(0.15));

		TableColumn selResult = new TableColumn("Result");
		selResult.setCellValueFactory(new PropertyValueFactory<CommandResult, Image>("result"));
		selResult.setSortable(false);
		selResult.prefWidthProperty().bind(commandList.widthProperty().multiply(0.10));

		TableColumn selInfo = new TableColumn("LogInfo");
		selInfo.setCellValueFactory(new PropertyValueFactory<CommandResult, String>("loginfo"));
		selInfo.setSortable(false);
		selInfo.prefWidthProperty().bind(commandList.widthProperty().multiply(0.45));

		commandList.setItems(clData);
		commandList.getColumns().addAll(selCommand, selTarget, selValue, selResult, selInfo);

		// einfuegen
		root.setTop(hbox);
		root.setCenter(commandList);
		seleneseTab.setContent(root);
	
		return seleneseTab;
	}
	
	public static void addCommandToList(Session aSess) {
		CommandResult tCR = new CommandResult("TNCreateSession", aSess.getHost() + ":" + aSess.getPort(), aSess.getCodepage() + "," + aSess.getSsltype());
		Platform.runLater(() -> clData.add(tCR));
	}

	public static void addCommandToList(Command aCmd) {
		String aValue = aCmd.getValue();
		if (aCmd.getCommand().toLowerCase().indexOf("password") > 0) {
			aValue = "*****";
		}
		CommandResult tCR = new CommandResult(aCmd.getCommand(), aCmd.getTarget(), aValue);
		Platform.runLater(() -> clData.add(tCR));
	}
	
	public static void setLatestResult(Boolean success, final String aResult) {
		Platform.runLater(() -> clData.get(clData.size() - 1).setLogInfo(aResult));
		Platform.runLater(() -> clData.get(clData.size() - 1).setResult(success));

		commandList.refresh();
		Platform.runLater(() -> commandList.scrollTo(clData.size() - 1));
	}
	
	@Override
	public boolean shutdown() {

		return true;
	}
}
