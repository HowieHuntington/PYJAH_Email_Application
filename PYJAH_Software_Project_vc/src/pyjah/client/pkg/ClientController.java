package pyjah.client.pkg;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import pyjah.util.pkg.Email;
import pyjah.util.pkg.User;

public class ClientController implements Initializable {
	@FXML
	private TabPane tPane;
	@FXML
	private TextArea messageBody = new TextArea();
	@FXML
	private TextField toLine = new TextField();
	@FXML
	private TextField subjectLine = new TextField();
	@FXML
	private Button sendButton = new Button();
	@FXML
	Label userIdLabel;
	@FXML
	private RadioButton userARadioButton;
	@FXML
	private RadioButton userBRadioButton;
	@FXML
	private Button loginButton;
	@FXML
	private Tab loginTab;
	@FXML
	private Tab composeTab;
	@FXML
	private Tab inboxTab;
	@FXML
	private Tab sentTab;
	@FXML
	private Button logoutButton;

	@FXML
	private ListView<Email> inboxListView;

	@FXML
	private ListView<Email> sentboxListView;

	@FXML
	private String sentSelected;

	@FXML
	private String inboxSelected;

	@FXML
	private TextArea inboxTextArea;

	@FXML
	private TextArea sentTextArea;

	ToggleGroup group = new ToggleGroup();

	@FXML
	private Button inboxRefreshButton;

	@FXML
	private Button sentRefreshButton;

	private ArrayList<Email> inboxAL = new ArrayList<Email>();
	private ArrayList<Email> sentBoxAL = new ArrayList<Email>();

	// private User currentUser;
	private HashMap messageFields = new HashMap();
	private User user;
	// private Email email;

	Client pyjahClient = new Client();

	// Method to send the input from the GUI fields to a location on button click.
	// As of now it just prints the text fields inputted.

	// <<<<<<< HEAD
	// Composes a new email object with appropriate fields.
	@FXML
	public void handleSendButtonClick(ActionEvent event) {
		Email email = new Email(user.getUsername(), toLine.getText(), subjectLine.getText(), messageBody.getText());// ,
		email.setTime(); // email.getTime(),

		pyjahClient.addToSentBox(email);
		// pyjahClient.sendUser(pyjahClient.getCurrentUser());
		pyjahClient.sendEmail(email);
		toLine.clear();
		subjectLine.clear();
		messageBody.clear();

		updateInbox();
		updateSentbox();

	}

	@FXML
	void loginOnButtonClick(ActionEvent event) {
		pyjahClient.sendUser(pyjahClient.getCurrentUser());
		pyjahClient.setLoggedIn(true);
		composeTab.setDisable(false);
		inboxTab.setDisable(false);
		sentTab.setDisable(false);
		tPane.getSelectionModel().select(composeTab);
		this.user = pyjahClient.getCurrentUser();

		System.out.println("This is the user" + pyjahClient.getCurrentUser().getUsername());
		// Thread.sleep(500);

		/*
		 * System.out.println(thread1.getState()); if (thread1.isAlive()==false) {
		 * thread1.start(); } System.out.println(thread1.getState());
		 */

		updateInbox();
		updateSentbox();

		loginTab.setDisable(true);

	}

	@FXML
	void radioSetToUserA(ActionEvent event) {

		userARadioButton.setToggleGroup(group);
		userARadioButton.setSelected(true);

		this.user = new User();
		user.setUsername("User A");
		pyjahClient.setUser(this.user);
		this.userIdLabel.setText(this.user.getUsername());
	}

	@FXML
	void radioSetToUserB(ActionEvent event) {
		System.out.println("radio b");
		userBRadioButton.setToggleGroup(group);
		userBRadioButton.setSelected(true);

		this.user = new User();
		user.setUsername("User B");
		pyjahClient.setUser(this.user);
		this.userIdLabel.setText(this.user.getUsername());
	}

	// AKA log off button :P
	@FXML
	void backToLoginButtonClick(ActionEvent event) {
		pyjahClient.setLoggedIn(false);
		loginTab.setDisable(false);
		tPane.getSelectionModel().select(loginTab);
		composeTab.setDisable(true);
		inboxTab.setDisable(true);
		sentTab.setDisable(true);
	}

	public void updateInbox() {
		ArrayList<Email> userInbox = pyjahClient.user.getInboxAL();
		/*ArrayList<String> testList = new ArrayList<String>();
		for (int i = 0; i < pyjahClient.user.getInboxAL().size(); i++) {

			testList.add("From: \"" + pyjahClient.user.getInboxAL().get(i).getSender() + "\" At: "
					+ pyjahClient.user.getInboxAL().get(i).getTime() + " Subject: \""
					+ pyjahClient.user.getInboxAL().get(i).getSubject() + "\"");
		}*/

		inboxListView.setItems(FXCollections.observableList(userInbox));
	}

	public void updateSentbox() {
		ArrayList<Email> userSentbox = pyjahClient.user.getSentboxAL();
		/*ArrayList<String> testList = new ArrayList<String>();
		for (int i = 0; i < pyjahClient.user.getSentboxAL().size(); i++) {
			testList.add("From: \"" + pyjahClient.user.getSentboxAL().get(i).getSender() + "\" At: "
					+ pyjahClient.user.getSentboxAL().get(i).getTime() + " Subject: \""
					+ pyjahClient.user.getSentboxAL().get(i).getSubject() + "\"");
		}*/

		sentboxListView.setItems(FXCollections.observableList(userSentbox));

	}

	@FXML
	void onInboxRefreshButtonClick(ActionEvent event) {
		System.out.println(pyjahClient.getCurrentUser().getUsername());
		updateInbox();
	}

	@FXML
	void onSentRefreshButtonClick(ActionEvent event) {
		System.out.println("Refresh");
		updateSentbox();
	}

	@FXML
	void handleInboxSelection(MouseEvent event) {
		inboxSelected = "To: " + inboxListView.getSelectionModel().getSelectedItem().getRecipient() + "\nFrom: "
				+ inboxListView.getSelectionModel().getSelectedItem().getSender() + "\nSent At: "
				+ inboxListView.getSelectionModel().getSelectedItem().getTime() + "\nBody: "
				+ inboxListView.getSelectionModel().getSelectedItem().getBody();
		printToInbox(inboxSelected);

	}

	@FXML
	void handleSentSelection(MouseEvent event) {
		sentSelected = "To: " + sentboxListView.getSelectionModel().getSelectedItem().getRecipient() + "\nFrom: "
				+ sentboxListView.getSelectionModel().getSelectedItem().getSender() + "\nSent At: "
				+ sentboxListView.getSelectionModel().getSelectedItem().getTime() + "\nBody: "
				+ sentboxListView.getSelectionModel().getSelectedItem().getBody();
		printToSent(sentSelected);

	}

	@FXML
	void printToInbox(String subject) {
		inboxTextArea.setText(subject);
	}

	@FXML
	void printToSent(String subject) {
		sentTextArea.setText(subject);
	}

	// This is the thread to update the inbox and sent box during startup
	Thread thread1 = new Thread() {
		public void run() {
			while (true) { // pyjahClient.isLoggedIn() ==
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updateSentbox();
				updateInbox();
			}

		}

	};

	@Override
	public void initialize(URL arg0, ResourceBundle arg1r) {
		// thread1.start();
	}

}