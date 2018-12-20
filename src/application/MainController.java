package application;



import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.rabbitmq.client.Channel;

public class MainController implements Initializable {

	private static final String STYLE_SUCCESS = "-fx-text-fill: green;";
	private static final String STYLE_ERROR = "-fx-text-fill: red;";
	ArrayList<String> listForMainChoiceBox;
	List<String> listForODEODChoiceBox;
	Map<String, CommandClassNew> commandFormatMap;
	List<String> serverDetails = new ArrayList<>();
	ArrayList<String> qNames = new ArrayList<>();

	@FXML
	ChoiceBox<String> od_eod_choiceBox;
	@FXML
	ChoiceBox<String> mainchoiceBox;
	ChoiceBox<String> prop1_choiceBox;

	Label label2;
	Label label3;
	Label label4;

	TextField feild1;
	TextField feild2;
	TextField feild3;
	TextField feild4;
	DatePicker cobDatePicker;

	@FXML
	TextField serverAddressFeild;

	@FXML
	TextField userNameFeild;

	@FXML
	TextField vHostFeild;

	@FXML
	TextArea cArea;

	@FXML
	Button generateButton;
	@FXML
	Button sendButton;
	@FXML
	Button showQButton;
	@FXML
	Button updateQButton;

	@FXML
	GridPane gridPane;

	@FXML
	PasswordField passwordFeild;

	@FXML
	TextArea errorArea;

	@FXML
	TextArea errorAreaGen;
	
	@FXML
	Label q1, q2, q3, c1, c2, c3, cc1, cc2,cc3;
	

	CommandClassNew cAtPresend;

	public CommandClassNew getcAtPresend() {
		return cAtPresend;
	}

	public void setcAtPresend(CommandClassNew cAtPresend) {
		this.cAtPresend = cAtPresend;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		init();
	
		mainchoiceBox.getItems().addAll(listForMainChoiceBox);
		mainchoiceBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String a = mainchoiceBox.getSelectionModel().getSelectedItem();
				mainChoiceBoxSelected(a);
			}
		});

		od_eod_choiceBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				makeCompleteLayout();

			}
		});

	}

	public void sendMsgToRMQ() {

	}

	private void makeCompleteLayout() {

		String x = mainchoiceBox.getValue();
		String y = od_eod_choiceBox.getValue();

		if (x != null && y != null) {
			List<Node> nodeList = new ArrayList<>();
			CommandClassNew c = commandFormatMap.get(x + y);
			gridPane.getChildren().clear();
			if (c != null) {
				setcAtPresend(c);
				if (c.getProp1DisplayNames() != null) {

					prop1_choiceBox = new ChoiceBox<>();
					feild1 = new TextField();

					nodeList.add(prop1_choiceBox);
					nodeList.add(feild1);

					prop1_choiceBox.getItems().clear();
					prop1_choiceBox.getItems().addAll(c.getProp1DisplayNames());
					prop1_choiceBox.setValue(c.getProp1DisplayNames().get(0));
				} else {
					prop1_choiceBox = null;
				}

				if (c.isCobDate()) {
					label2 = new Label(Constants.COB_DATE_DISPLAY_NAME);
					// feild2 = new TextField();
					cobDatePicker = new DatePicker();
					nodeList.add(label2);
					// nodeList.add(feild2);
					nodeList.add(cobDatePicker);
				} else {
					cobDatePicker = null;
				}

				if (c.isMktSnapCd()) {
					label3 = new Label(Constants.MKT_S_CD_DISPLAY_NAME);
					feild3 = new TextField();
					nodeList.add(label3);
					nodeList.add(feild3);

				} else {
					feild3 = null;
				}
				if (c.isEventType()) {
					label4 = new Label(Constants.EVENT_TYPE_DISPLAY_NAME);
					feild4 = new TextField();
					nodeList.add(label4);
					nodeList.add(feild4);
				} else {
					feild4 = null;
				}

				int j = 0;
				for (int i = 0; i < nodeList.size() / 2; i++) {
					gridPane.add(nodeList.get(j), 0, i);
					gridPane.add(nodeList.get(j + 1), 1, i);
					j = j + 2;

				}
				generateButton.setOpacity(1);

			}

		}

	}

	public void mainChoiceBoxSelected(String selectedValue) {

		listForODEODChoiceBox = new ArrayList<>();
		listForODEODChoiceBox.add(Constants.ON_DEMAND);
		listForODEODChoiceBox.add(Constants.RT);
		
		
		
		switch (selectedValue) {
		case Constants.POSITION:
		case Constants.CASHFLOW:
			listForODEODChoiceBox.add(Constants.ON_DEMAND_EOD);
			listForODEODChoiceBox.add(Constants.DYNAMIC_POLLING);
			listForODEODChoiceBox.add(Constants.EOD);
			break;

		default:
			break;
		}

		od_eod_choiceBox.getItems().clear();
		od_eod_choiceBox.getItems().addAll(listForODEODChoiceBox);
		od_eod_choiceBox.setValue(listForODEODChoiceBox.get(0));

	}

	public void generateShowQButtonCallback() {
		ArrayList<String> qNames = getQNames("cxl",od_eod_choiceBox.getValue(), mainchoiceBox.getValue());
		try {
			if (qNames.size() == 3) {
			q1.setText(qNames.get(0));		
			q2.setText(qNames.get(1));
			q3.setText(qNames.get(2));
		}else if (qNames.size() == 2) {
			q1.setText(qNames.get(0));		
			q2.setText(qNames.get(1));
		}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	
	}
	
	public void updateQButtonCallback() {
		
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(serverAddressFeild.getText().trim());
			factory.setUsername(userNameFeild.getText().trim());
			factory.setPassword(passwordFeild.getText().trim());
			factory.setVirtualHost(vHostFeild.getText().trim());
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
	
			
			if (qNames.size()==3) {
				AMQP.Queue.DeclareOk dok0 = channel.queueDeclarePassive(qNames.get(0));
				AMQP.Queue.DeclareOk dok1 = channel.queueDeclarePassive(qNames.get(1));
				AMQP.Queue.DeclareOk dok2 = channel.queueDeclarePassive(qNames.get(2));
				c1.setText(String.valueOf(dok0.getMessageCount()));
				cc1.setText(String.valueOf(dok0.getConsumerCount()));
				
				c2.setText(String.valueOf(dok1.getMessageCount()));
				cc2.setText(String.valueOf(dok1.getConsumerCount()));
				
				c3.setText(String.valueOf(dok2.getMessageCount()));
				cc3.setText(String.valueOf(dok2.getConsumerCount()));
				
			}else if (qNames.size() == 2) {
				AMQP.Queue.DeclareOk dok0 = channel.queueDeclarePassive(qNames.get(0));
				AMQP.Queue.DeclareOk dok1 = channel.queueDeclarePassive(qNames.get(1));
				c1.setText(String.valueOf(dok0.getMessageCount()));
				cc1.setText(String.valueOf(dok0.getConsumerCount()));
				
				c2.setText(String.valueOf(dok1.getMessageCount()));
				cc2.setText(String.valueOf(dok1.getConsumerCount()));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void generateButtonCallback() {

		try {
			String prop1 = null;
			String value1 = null;
			String cobDateString = null;
			String mktS = null;
			String eventType = null;

			if (prop1_choiceBox != null) {
				prop1 = prop1_choiceBox.getValue();
				value1 = feild1.getText();
			}

			if (cobDatePicker != null) {
				LocalDate cobDateInDateFormat = cobDatePicker.getValue();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				cobDateString = cobDateInDateFormat.format(formatter);
			}

			if (label3 != null) {
				mktS = feild3.getText().trim();
			}
			if (label4 != null) {
				eventType = feild4.getText().trim();

			}

			String type = getType(od_eod_choiceBox.getValue(), mainchoiceBox.getValue());
			String command = cAtPresend.generateCommandStructure(type, prop1, value1, cobDateString, mktS, eventType);

			//command = formatXML(command);
			cArea.setText(command);
			sendButton.setDisable(false);
			qNames = getQNames("cxl",od_eod_choiceBox.getValue(), mainchoiceBox.getValue());
			try {
				q1.setText(qNames.get(0));		
				q2.setText(qNames.get(1));
				q3.setText(qNames.get(2));
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
			System.out.println(qNames);
			
			
			
		} catch (Exception e) {
			errorAreaGen.setStyle(STYLE_ERROR);
			errorAreaGen.setText(e.toString());
		}

	}

	public void sendButtonCallback() {

		try {
			final String QUEUE_NAME = "test";
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(serverAddressFeild.getText().trim());
			factory.setUsername(userNameFeild.getText().trim());
			factory.setPassword(passwordFeild.getText().trim());
			factory.setVirtualHost(vHostFeild.getText().trim());
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			String message = cArea.getText();
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			errorArea.setVisible(true);
			errorArea.setStyle(STYLE_SUCCESS);
			errorArea.setText("Send Succesfully to: " + QUEUE_NAME);
			
			//Queue.DeclareOk response = channel.queueDeclarePassive("test");
			// returns the number of messages in Ready state in the queue
			//response.getMessageCount();
			// returns the number of consumers the queue has
			//response.getConsumerCount();
			
			
			//System.out.println("Message count in test: "+ response.getMessageCount());
			//errorArea.appendText(", Message count in test: "+ response.getMessageCount());
			
			channel.close();
			connection.close();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}
	}

	public void init() {

		errorArea.setVisible(false);
		errorArea.setEditable(false);
		errorArea.setCenterShape(true);

		errorAreaGen.setVisible(false);
		errorAreaGen.setEditable(false);
		errorAreaGen.setCenterShape(true);
		
		sendButton.setDisable(true);

		listForMainChoiceBox = new ArrayList<>();
		listForMainChoiceBox.add(Constants.TRADE);
		listForMainChoiceBox.add(Constants.CASHFLOW);
		listForMainChoiceBox.add(Constants.POSITION);

		commandFormatMap = new HashMap<>();

		List<String> prop1DisplayNamesCashflow = new ArrayList<>();
		prop1DisplayNamesCashflow.add("trade_num");
		prop1DisplayNamesCashflow.add("cashflow_num");

		CommandClassNew cashflowOD = new CommandClassNew(Constants.CASHFLOW + Constants.ON_DEMAND, "",prop1DisplayNamesCashflow, false, false, false, 0);
		CommandClassNew cashflowRT = new CommandClassNew(Constants.CASHFLOW + Constants.RT, "",null, false, false, false, 0);
		CommandClassNew cashflowODEOD = new CommandClassNew(Constants.CASHFLOW + Constants.ON_DEMAND_EOD, "",prop1DisplayNamesCashflow, true, false, false, 1);
		CommandClassNew cashflowDP = new CommandClassNew(Constants.CASHFLOW + Constants.DYNAMIC_POLLING, "",prop1DisplayNamesCashflow, false, false, true, 0);
		CommandClassNew cashflowEOD = new CommandClassNew(Constants.CASHFLOW + Constants.EOD, "", null, true, false,false, 0);

		commandFormatMap.put(cashflowOD.getKey(), cashflowOD);
		commandFormatMap.put(cashflowODEOD.getKey(), cashflowODEOD);
		commandFormatMap.put(cashflowDP.getKey(), cashflowDP);
		commandFormatMap.put(cashflowEOD.getKey(), cashflowEOD);
		commandFormatMap.put(cashflowRT.getKey(), cashflowRT);

		CommandClassNew TradeEOD = new CommandClassNew(Constants.TRADE + Constants.ON_DEMAND, "",prop1DisplayNamesCashflow, true, true, true, 0);
		CommandClassNew TradeRT = new CommandClassNew(Constants.TRADE + Constants.RT, "",null, false, false, false, 0);
		commandFormatMap.put(TradeEOD.getKey(), TradeEOD);
		commandFormatMap.put(TradeRT.getKey(), TradeRT);

		
	
	
	}

	private String getType(String mode, String module) {
		String type = null;
	
		switch (mode) {
		case Constants.ON_DEMAND:
		case Constants.DYNAMIC_POLLING:
		case Constants.ON_DEMAND_EOD:
			type = "RealTime" + module + "Extraction";
			break;
		case Constants.EOD:
			type = "EOD" + module + "Extraction";
			break;
		default:
			break;
		}

		return type;
	}
	
	private ArrayList<String> getQNames(String app, String mode, String module){
		
		qNames.clear();
		String appName_forQ = "";
		
		switch (app) {
		case Constants.CXL:
			appName_forQ = Constants.CXL_APP_forQ;
			break;
		case Constants.TPT:
			appName_forQ = Constants.TPT_APP_forQ;
			break;

		default:
			break;
		}
		
		String mode_forQ = "";
		switch (mode) {
		case Constants.ON_DEMAND:
		case Constants.DYNAMIC_POLLING:
		case Constants.ON_DEMAND_EOD:
			mode_forQ = Constants.OD_Q_NAME_forQ;
			break;
		case Constants.EOD:
			mode_forQ = Constants.EOD_Q_NAME_forQ;
			break;
		default:
			break;
		}
		
		String appIndentifier  = appName_forQ;
		String modeIndentifier = mode_forQ;
		String moduleIndentifier = "";
		switch(module) {
			
		case Constants.POSITION:
			break;	
		case Constants.CASHFLOW:
			moduleIndentifier = Constants.CASHFLOW_forQ;
			break;
		case Constants.REF_DATA:
			moduleIndentifier = Constants.REF_DATA_forQ;
			break;
		case Constants.TRADE:
			moduleIndentifier = Constants.TRADE_forQ;
			break;
		case Constants.INVOICE:
			moduleIndentifier = Constants.INVOICES_forQ;
			break;
		case Constants.PRICING:
			moduleIndentifier = Constants.PRICES_forQ;
			break;
		
		}
		
		
		String paramQ = appIndentifier + "_adaptor_" + modeIndentifier + "parameters_q";
		System.out.println(paramQ);
		String adaptorQ = appIndentifier + "_adaptor_" + modeIndentifier + moduleIndentifier + "_q";
		System.out.println(adaptorQ);
		String finalQ = appIndentifier + "_" + moduleIndentifier + "_q";
		System.out.println(finalQ);
		if (mode!=Constants.RT) {
			qNames.add(paramQ);
		}
		qNames.add(adaptorQ);
		qNames.add(finalQ);
		return qNames;
		
		
		
		
		
		
	
	
	}
	
	
	
}
