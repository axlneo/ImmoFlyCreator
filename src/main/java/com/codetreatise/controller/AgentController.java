package com.codetreatise.controller;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codetreatise.bean.Agent;
import com.codetreatise.bean.Annonce;
import com.codetreatise.service.AgentService;
import com.codetreatise.service.PrintReport;
import com.codetreatise.utils.ImageUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.codetreatise.config.StageManager;
import com.codetreatise.view.FxmlView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Controller
public class AgentController implements Initializable{

    @FXML
    private Button btnLogout;

    @FXML
    private Label userId;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private DatePicker dob;

    @FXML
    private RadioButton rbMale;

    @FXML
    private ToggleGroup gender;

    @FXML
    private RadioButton rbFemale;

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button reset;

    @FXML
    private Button saveUser;

    @FXML
    private FileChooser fileChooser;

    @FXML
    private ImageView imageLogo;


    private File logoFile = null;

    @FXML
    private TableView<Agent> userTable;

    @FXML
    private TableColumn<Agent, Long> colUserId;

    @FXML
    private TableColumn<Agent, String> colFirstName;

    @FXML
    private TableColumn<Agent, String> colLastName;

    @FXML
    private TableColumn<Agent, LocalDate> colDOB;

    @FXML
    private TableColumn<Agent, String> colGender;

    @FXML
    private TableColumn<Agent, String> colRole;

    @FXML
    private TableColumn<Agent, String> colEmail;

    @FXML
    private TableColumn<Agent, Boolean> colEdit;

    @FXML
    private MenuItem deleteUsers;

    @FXML
    private Button printButton;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private AgentService agentService;

    private ObservableList<Agent> agentList = FXCollections.observableArrayList();
    private ObservableList<String> roles = FXCollections.observableArrayList("Admin", "User");

    private BooleanProperty isUserSelected = new SimpleBooleanProperty(false);
    private Agent selectedAgent;

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Logout and go to the login page
     */
    @FXML
    private void logout(ActionEvent event) throws IOException {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
    }



    @FXML
    private void chooseFile(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Get Text");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images files(*.jpg,*.png)","*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        logoFile = stageManager.openChooseCoverDialog(fc); //TODO verify
        if(null != logoFile){ //not cancel

            Image image1 = new Image(logoFile.toURI().toString());

            imageLogo.setImage(image1);

        }


    }

    @FXML
    private void saveUser(ActionEvent event){

        if(validate("First Name", getFirstName(), "[a-zA-Z]+") &&
                validate("Last Name", getLastName(), "[a-zA-Z]+") &&
                emptyValidation("DOB", dob.getEditor().getText().isEmpty()) &&
                emptyValidation("Role", getRole() == null) ){

            if(userId.getText() == null || userId.getText() == ""){
                if(validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+") &&
                        emptyValidation("Password", getPassword().isEmpty())){

                    Agent user = new Agent();
                    user.setFirstName(getFirstName());
                    user.setLastName(getLastName());
                    user.setDob(getDob());
                    user.setGender(getGender());
                    user.setRole(getRole());
                    user.setEmail(getEmail());
                    user.setPassword(getPassword());

                    saveUserImages(user);

                    Agent newUser = agentService.save(user);

                    saveAlert(newUser);
                }

            }else{
                Agent user = agentService.find(Long.parseLong(userId.getText()));
                user.setFirstName(getFirstName());
                user.setLastName(getLastName());
                user.setDob(getDob());
                user.setGender(getGender());
                user.setRole(getRole());

                saveUserImages(user);

                Agent updatedUser =  agentService.update(user);
                updateAlert(updatedUser);
            }

            clearFields();
            loadAgentDetails();
        }


    }

    private void saveUserImages(Agent agent){

        if(null != logoFile){

            byte[] bFile1 = new byte[(int) logoFile.length()];

            try {
                FileInputStream fileInputStream = new FileInputStream(logoFile);
                fileInputStream.read(bFile1);
                fileInputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            agent.setLogo(bFile1);

        }

    }

    @FXML
    private void deleteUsers(ActionEvent event){
        List<Agent> users = userTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if(action.get() == ButtonType.OK) agentService.deleteInBatch(users);

        loadAgentDetails();
    }

    private void clearFields() {
        userId.setText(null);
        firstName.clear();
        lastName.clear();
        dob.getEditor().clear();
        rbMale.setSelected(true);
        rbFemale.setSelected(false);
        cbRole.getSelectionModel().clearSelection();
        email.clear();
        password.clear();
        imageLogo.setImage(null);
        logoFile = null;

        isUserSelected.set(false);
		selectedAgent = null;
    }

    private void saveAlert(Agent user){

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("User saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user "+user.getFirstName()+" "+user.getLastName() +" has been created and \n"+getGenderTitle(user.getGender())+" id is "+ user.getAgentId() +".");
        alert.showAndWait();
    }

    private void updateAlert(Agent user){

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("*" + "ser updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user "+user.getFirstName()+" "+user.getLastName() +" has been updated.");

        alert.showAndWait();

    }

    private String getGenderTitle(String gender){
        return (gender.equals("Male")) ? "his" : "her";
    }

    public String getFirstName() {
        return firstName.getText();
    }

    public String getLastName() {
        return lastName.getText();
    }

    public LocalDate getDob() {
        return dob.getValue();
    }

    public String getGender(){
        return rbMale.isSelected() ? "Male" : "Female";
    }

    public String getRole() {
        return cbRole.getSelectionModel().getSelectedItem();
    }

    public String getEmail() {
        return email.getText();
    }

    public String getPassword() {
        return password.getText();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initPrintButton();

        cbRole.setItems(roles);

        userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

        // Add all users into table
        loadAgentDetails();
    }

    private void initPrintButton(){

        Image image = new Image(getClass().getResourceAsStream("/images/pdf-icon.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(100.0);
        printButton.setGraphic(imageView);

        printButton.visibleProperty().bind(isUserSelected);

        printButton.setOnAction(e -> {

            // --- Show Jasper Report on click-----
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("now open dialog!");

                        PrintReport viewReport = new PrintReport();
                        viewReport.showReport(selectedAgent);

                }
            });


        });
    }

    @FXML protected void printPdf(ActionEvent event) {

    }

    /*
     *  Set All userTable column properties
     */
    private void setColumnProperties(){
		/* Override date format in table
		 * colDOB.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
			 String pattern = "dd/MM/yyyy";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		     @Override
		     public String toString(LocalDate date) {
		         if (date != null) {
		             return dateFormatter.format(date);
		         } else {
		             return "";
		         }
		     }

		     @Override
		     public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateFormatter);
		         } else {
		             return null;
		         }
		     }
		 }));*/

        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Agent, Boolean>, TableCell<Agent, Boolean>> cellFactory =
            new Callback<TableColumn<Agent, Boolean>, TableCell<Agent, Boolean>>()
            {
                @Override
                public TableCell<Agent, Boolean> call( final TableColumn<Agent, Boolean> param)
                {
                    final TableCell<Agent, Boolean> cell = new TableCell<Agent, Boolean>()
                    {
                        Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
                        final Button btnEdit = new Button();

                        @Override
                        public void updateItem(Boolean check, boolean empty)
                        {
                            super.updateItem(check, empty);
                            if(empty)
                            {
                                setGraphic(null);
                                setText(null);
                            }
                            else{
                                btnEdit.setOnAction(e ->{
                                    Agent user = getTableView().getItems().get(getIndex());
                                    updateUser(user);
                                });

                                btnEdit.setStyle("-fx-background-color: transparent;");
                                ImageView iv = new ImageView();
                                iv.setImage(imgEdit);
                                iv.setPreserveRatio(true);
                                iv.setSmooth(true);
                                iv.setCache(true);
                                btnEdit.setGraphic(iv);

                                setGraphic(btnEdit);
                                setAlignment(Pos.CENTER);
                                setText(null);
                            }
                        }

                        private void updateUser(Agent user) {
                            userId.setText(Long.toString(user.getAgentId()));
                            firstName.setText(user.getFirstName());
                            lastName.setText(user.getLastName());
                            dob.setValue(user.getDob());
                            if(user.getGender().equals("Male")) rbMale.setSelected(true);
                            else rbFemale.setSelected(true);
                            cbRole.getSelectionModel().select(user.getRole());
                            email.setText(user.getEmail());

					        imageLogo.setImage(ImageUtils.convertToJavaFXImage(user.getLogo(),200,150));

                            isUserSelected.set(true);
                            selectedAgent = user;
                        }
                    };
                    return cell;
                }
            };


    /*
     *  Add All users to observable list and update table
     */
    private void loadAgentDetails(){
        agentList.clear();
        agentList.addAll(agentService.findAll());

        userTable.setItems(agentList);
    }

    /*
     * Validations
     */
    private boolean validate(String field, String value, String pattern){
        if(!value.isEmpty()){
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if(m.find() && m.group().equals(value)){
                return true;
            }else{
                validationAlert(field, false);
                return false;
            }
        }else{
            validationAlert(field, true);
            return false;
        }
    }

    private boolean emptyValidation(String field, boolean empty){
        if(!empty){
            return true;
        }else{
            validationAlert(field, true);
            return false;
        }
    }

    private void validationAlert(String field, boolean empty){
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if(field.equals("Role")) alert.setContentText("Please Select "+ field);
        else{
            if(empty) alert.setContentText("Please Enter "+ field);
            else alert.setContentText("Please Enter Valid "+ field);
        }
        alert.showAndWait();
    }
}
