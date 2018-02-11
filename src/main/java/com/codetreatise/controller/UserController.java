package com.codetreatise.controller;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.User;
import com.codetreatise.config.StageManager;
import com.codetreatise.service.UserService;
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

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Controller
public class UserController implements Initializable{

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
    private ImageView imageView1;

	@FXML
	private ImageView imageView2;

	@FXML
	private ImageView imageView3;

	@FXML
	private ImageView imageView4;


	private File file1, file2, file3, file4 = null;

	@FXML
	private TableView<User> userTable;

	@FXML
	private TableColumn<User, Long> colUserId;

	@FXML
	private TableColumn<User, String> colFirstName;

	@FXML
	private TableColumn<User, String> colLastName;

	@FXML
	private TableColumn<User, LocalDate> colDOB;

	@FXML
	private TableColumn<User, String> colGender;
	
	@FXML
    private TableColumn<User, String> colRole;

	@FXML
	private TableColumn<User, String> colEmail;
	
	@FXML
    private TableColumn<User, Boolean> colEdit;
	
	@FXML
    private MenuItem deleteUsers;
	
	@Lazy
    @Autowired
    private StageManager stageManager;
	
	@Autowired
	private UserService userService;
	
	private ObservableList<User> userList = FXCollections.observableArrayList();
	private ObservableList<String> roles = FXCollections.observableArrayList("Admin", "User");
	
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
		List<File> phil = stageManager.openChooseDialog(fc); //TODO verify
		if(null != phil){ //not cancel

			switch(phil.size()){
				case 0: //TODO display error
					break;

				case 1 : file1 = phil.get(0);
					if(null != file1){
						Image image1 = new Image(file1.toURI().toString());

						imageView1.setImage(image1);

					};
					break;

				case 2 : file1 = phil.get(0);
					file2 = phil.get(1);
					if(null != file1){
						Image image1 = new Image(file1.toURI().toString());

						imageView1.setImage(image1);
					};
					if(null != file2){
						Image image2 = new Image(file2.toURI().toString());

						imageView2.setImage(image2);
                    };
					break;

				case 3 : file1 = phil.get(0);
					file2 = phil.get(1);
					file3 = phil.get(2);
					if(null != file1){
						Image image1 = new Image(file1.toURI().toString());

						imageView1.setImage(image1);
					};
					if(null != file2){
						Image image2 = new Image(file2.toURI().toString());

						imageView2.setImage(image2);
					};
					if(null != file3){
						Image image3 = new Image(file3.toURI().toString());

						imageView3.setImage(image3);
					};
					break;

				case 4 : file1 = phil.get(0);
					file2 = phil.get(1);
					file3 = phil.get(2);
					file4 = phil.get(3);
					if(null != file1){
						Image image1 = new Image(file1.toURI().toString());

						imageView1.setImage(image1);
					};
					if(null != file2){
						Image image2 = new Image(file2.toURI().toString());

						imageView2.setImage(image2);
					};
					if(null != file3){
						Image image3 = new Image(file3.toURI().toString());

						imageView3.setImage(image3);
					};
					if(null != file4){
						Image image4 = new Image(file4.toURI().toString());

						imageView4.setImage(image4);
					};
					break;
			}
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
    				
    				User user = new User();
        			user.setFirstName(getFirstName());
        			user.setLastName(getLastName());
        			user.setDob(getDob());
        			user.setGender(getGender());
        			user.setRole(getRole());
        			user.setEmail(getEmail());
        			user.setPassword(getPassword());

        			saveUserImages(user);

        			User newUser = userService.save(user);
        			
        			saveAlert(newUser);
    			}
    			
    		}else{
    			User user = userService.find(Long.parseLong(userId.getText()));
    			user.setFirstName(getFirstName());
    			user.setLastName(getLastName());
    			user.setDob(getDob());
    			user.setGender(getGender());
    			user.setRole(getRole());

				saveUserImages(user);

    			User updatedUser =  userService.update(user);
    			updateAlert(updatedUser);
    		}
    		
    		clearFields();
    		loadUserDetails();
    	}
    	
    	
    }

    private void saveUserImages(User user){

    	if(null != file1){

			byte[] bFile1 = new byte[(int) file1.length()];

			try {
				FileInputStream fileInputStream = new FileInputStream(file1);
				fileInputStream.read(bFile1);
				fileInputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			user.setImage1(bFile1);

		}
		if(null != file2){

			byte[] bFile2 = new byte[(int) file2.length()];

			try {
				FileInputStream fileInputStream = new FileInputStream(file2);
				fileInputStream.read(bFile2);
				fileInputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			user.setImage2(bFile2);

		}
		if(null != file3){

			byte[] bFile3 = new byte[(int) file3.length()];

			try {
				FileInputStream fileInputStream = new FileInputStream(file3);
				fileInputStream.read(bFile3);
				fileInputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			user.setImage3(bFile3);

		}
		if(null != file4){

			byte[] bFile4 = new byte[(int) file4.length()];

			try {
				FileInputStream fileInputStream = new FileInputStream(file4);
				fileInputStream.read(bFile4);
				fileInputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			user.setImage4(bFile4);

		}

	}

    @FXML
    private void deleteUsers(ActionEvent event){
    	List<User> users = userTable.getSelectionModel().getSelectedItems();
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete selected?");
		Optional<ButtonType> action = alert.showAndWait();
		
		if(action.get() == ButtonType.OK) userService.deleteInBatch(users);
    	
    	loadUserDetails();
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
		imageView1.setImage(null);
		imageView2.setImage(null);
		imageView3.setImage(null);
		imageView4.setImage(null);
		file1 = null;
		file2 = null;
		file3 = null;
		file4 = null;
	}
	
	private void saveAlert(User user){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User saved successfully.");
		alert.setHeaderText(null);
		alert.setContentText("The user "+user.getFirstName()+" "+user.getLastName() +" has been created and \n"+getGenderTitle(user.getGender())+" id is "+ user.getId() +".");
		alert.showAndWait();
	}
	
	private void updateAlert(User user){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User updated successfully.");
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
		
		cbRole.setItems(roles);
		
		userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		setColumnProperties();
		
		// Add all users into table
		loadUserDetails();
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
	
	Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>> cellFactory =
			new Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>>()
	{
		@Override
		public TableCell<User, Boolean> call( final TableColumn<User, Boolean> param)
		{
			final TableCell<User, Boolean> cell = new TableCell<User, Boolean>()
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
							User user = getTableView().getItems().get(getIndex());
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

				private void updateUser(User user) {
					userId.setText(Long.toString(user.getId()));
					firstName.setText(user.getFirstName());
					lastName.setText(user.getLastName());
					dob.setValue(user.getDob());
					if(user.getGender().equals("Male")) rbMale.setSelected(true);
					else rbFemale.setSelected(true);
					cbRole.getSelectionModel().select(user.getRole());


						imageView1.setImage(convertToJavaFXImage(user.getImage1(),200,150));




						imageView2.setImage(convertToJavaFXImage(user.getImage2(),200,150));



						imageView3.setImage(convertToJavaFXImage(user.getImage3(),200,150));



						imageView4.setImage(convertToJavaFXImage(user.getImage4(),200,150));


					/*if(null != user.getImage4()){
						try {
							FileOutputStream os = new FileOutputStream("image4.png");
							os.write(user.getImage4());
							os.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

						imageView4 = new ImageView("file://image4.png");
					}*/
				}
			};
			return cell;
		}
	};

	private static Image convertToJavaFXImage(byte[] raw, final int width, final int height) {
		if(null != raw){

			WritableImage image = new WritableImage(width, height);
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(raw);
				BufferedImage read = ImageIO.read(bis);
				image = SwingFXUtils.toFXImage(read, null);
			} catch (IOException ex) {
				//Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
				ex.printStackTrace();
			}
			return image;
		} else {
			return null;
		}
	}

	/*
	 *  Add All users to observable list and update table
	 */
	private void loadUserDetails(){
		userList.clear();
		userList.addAll(userService.findAll());

		userTable.setItems(userList);
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
