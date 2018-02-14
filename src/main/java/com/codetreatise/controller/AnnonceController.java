package com.codetreatise.controller;

import com.codetreatise.bean.Agent;
import com.codetreatise.bean.Annonce;
import com.codetreatise.config.StageManager;
import com.codetreatise.service.AgentService;
import com.codetreatise.service.AnnonceService;
import com.codetreatise.service.PrintReport;
import com.codetreatise.utils.ImageUtils;
import com.codetreatise.view.FxmlView;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Controller
public class AnnonceController implements Initializable{

	@FXML
    private Button btnLogout;
	
	@FXML
    private Label annonceId;
	
	@FXML
    private TextField annonceTitre;

	@FXML
	private TextField annoncePrix;

	@FXML
	private TextArea annonceDesc;
    
    @FXML
    private Button reset;
	
	@FXML
    private Button saveAnnonce;

	@FXML
	private ImageView imageCover;

    @FXML
    private ImageView imageView1;

	@FXML
	private ImageView imageView2;

	@FXML
	private ImageView imageView3;

	@FXML
	private ImageView imageView4;

	@FXML
	private Label coverName;

	@FXML
	private Label photo1Name;

	@FXML
	private Label photo2Name;

	@FXML
	private Label photo3Name;

	@FXML
	private Label photo4Name;


	private File file1, file2, file3, file4, cover = null;

	@FXML
	private Label connectedAgentName;

	@FXML
	private TableView<Annonce> annonceTable;

	@FXML
	private TableColumn<Annonce, Long> colAnnonceId;

	@FXML
	private TableColumn<Annonce, String> colAnnonceTitre;

	@FXML
	private TableColumn<Annonce, String> colAnnonceDesc;

	@FXML
	private TableColumn<Annonce, String> colAnnoncePrix;

	
	@FXML
    private TableColumn<Annonce, Boolean> colEdit;
	
	@FXML
    private MenuItem deleteAnnonces;

	@FXML
	private Button printButton;
	
	@Lazy
    @Autowired
    private StageManager stageManager;
	
	@Autowired
	private AgentService agentService;

    @Autowired
    private AnnonceService annonceService;

	private  Agent authenticateAgent;
	
	private ObservableList<Annonce> annonceList = FXCollections.observableArrayList();

	private BooleanProperty isAnnonceSelected = new SimpleBooleanProperty(false);
	private Annonce selectedAnnonce;

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

	public Agent getAuthenticateAgent() {
		return authenticateAgent;
	}

	public void setAuthenticateAgent(Agent authenticateAgent) {
		this.authenticateAgent = authenticateAgent;
	}

	@FXML
	private void chooseCover(ActionEvent event) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choisissez les photos");
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Images files(*.jpg,*.png)","*.jpg", "*.png"),
				new FileChooser.ExtensionFilter("All Files", "*.*"));
		cover = stageManager.openChooseCoverDialog(fc);
		if(null != cover){
			Image image1 = new Image(cover.toURI().toString());

			imageCover.setImage(image1);
			coverName.setText(cover.getName());

		};
	}

	@FXML
	private void chooseFile(ActionEvent event) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choisissez les photos");
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Images files(*.jpg,*.png)","*.jpg", "*.png"),
				new FileChooser.ExtensionFilter("All Files", "*.*"));
		List<File> phil = stageManager.openChooseFileDialog(fc);
		if(null != phil){ //not cancel

			switch(phil.size()){
				case 0: //TODO display error
					break;

				case 1 : file1 = phil.get(0);
					if(null != file1){
						Image image1 = new Image(file1.toURI().toString());

						imageView1.setImage(image1);
						photo1Name.setText(file1.getPath());

					};
					break;

				case 2 : file1 = phil.get(0);
					file2 = phil.get(1);
					if(null != file1){
						Image image1 = new Image(file1.toURI().toString());

						imageView1.setImage(image1);
						photo1Name.setText(file1.getPath());
					};
					if(null != file2){
						Image image2 = new Image(file2.toURI().toString());

						imageView2.setImage(image2);
						photo2Name.setText(file2.getPath());
                    };
					break;

				case 3 : file1 = phil.get(0);
					file2 = phil.get(1);
					file3 = phil.get(2);
					if(null != file1){
						Image image1 = new Image(file1.toURI().toString());

						imageView1.setImage(image1);
						photo1Name.setText(file1.getPath());
					};
					if(null != file2){
						Image image2 = new Image(file2.toURI().toString());

						imageView2.setImage(image2);
						photo2Name.setText(file2.getPath());
					};
					if(null != file3){
						Image image3 = new Image(file3.toURI().toString());

						imageView3.setImage(image3);

						photo3Name.setText(file3.getPath());

					};
					break;

				case 4 : file1 = phil.get(0);
					file2 = phil.get(1);
					file3 = phil.get(2);
					file4 = phil.get(3);
					if(null != file1){
						Image image1 = new Image(file1.toURI().toString());

						imageView1.setImage(image1);
						photo1Name.setText(file1.getPath());
					};
					if(null != file2){
						Image image2 = new Image(file2.toURI().toString());

						imageView2.setImage(image2);
						photo2Name.setText(file2.getPath());
					};
					if(null != file3){
						Image image3 = new Image(file3.toURI().toString());

						imageView3.setImage(image3);
						photo3Name.setText(file3.getPath());
					};
					if(null != file4){
						Image image4 = new Image(file4.toURI().toString());

						imageView4.setImage(image4);
						photo4Name.setText(file4.getPath());
					};
					break;
			}
		}


	}

    @FXML
    private void saveAnnonce(ActionEvent event){
    	
    	if(emptyValidation("Titre", getAnnonceTitre().isEmpty()) &&
    	   emptyValidation("Description", getAnnonceDesc().isEmpty()) ){
    		
    		if(annonceId.getText() == null || annonceId.getText() == ""){

    				
				Annonce annonce = new Annonce();
				annonce.setAgent(this.getAuthenticateAgent());
				annonce.setDescription(this.getAnnonceDesc());
				annonce.setTitre(this.getAnnonceTitre());
				annonce.setPrix(this.getAnnoncePrix());

				saveAnnonceImages(annonce);

				Annonce createAnnonce = annonceService.save(annonce);

				this.setAuthenticateAgent(agentService.find(this.getAuthenticateAgent().getAgentId())); //TODO ne remet pas a jour avec la nouvele annonce

				saveAlert(createAnnonce);

    			
    		}else{
    			Annonce annonce = this.getAuthenticateAgent().getAnnonceById(Long.parseLong(annonceId.getText()));

				annonce.setDescription(this.getAnnonceDesc());
				annonce.setTitre(this.getAnnonceTitre());
				annonce.setPrix(this.getAnnoncePrix());

				saveAnnonceImages(annonce);
                Annonce updateAnnonce = annonceService.update(annonce);

				this.setAuthenticateAgent(agentService.find(this.getAuthenticateAgent().getAgentId())); //TODO ne remet pas a jour avec la nouvele annonce

    			updateAlert(updateAnnonce);
    		}
    		
    		clearFields();
    		loadAnnonces();
    	}
    	
    	
    }

    private void saveAnnonceImages(Annonce annonce){

    	if(null != file1){

			byte[] bFile1 = new byte[(int) file1.length()];

			try {
				FileInputStream fileInputStream = new FileInputStream(file1);
				fileInputStream.read(bFile1);
				fileInputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			annonce.setPhoto1(bFile1);

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

			annonce.setPhoto2(bFile2);

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

			annonce.setPhoto3(bFile3);

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

			annonce.setPhoto4(bFile4);

		}
		if(null != cover){

			byte[] bCover = new byte[(int) cover.length()];

			try {
				FileInputStream fileInputStream = new FileInputStream(cover);
				fileInputStream.read(bCover);
				fileInputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			annonce.setCover(bCover);

		}
	}

    @FXML
    private void deleteAnnonces(ActionEvent event){
    	List<Annonce> annonces = annonceTable.getSelectionModel().getSelectedItems();
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete selected?");
		Optional<ButtonType> action = alert.showAndWait();
		
		if(action.get() == ButtonType.OK) {
			annonceService.deleteInBatch(annonces);
			this.getAuthenticateAgent().getAnnonceList().removeAll(annonces);
		}
    	
    	loadAnnonces();
    }
    
   	private void clearFields() {
		annonceId.setText(null);
		annonceTitre.clear();
		annoncePrix.clear();
		annonceDesc.clear();
		imageView1.setImage(null);
		imageView2.setImage(null);
		imageView3.setImage(null);
		imageView4.setImage(null);
		imageCover.setImage(null);
		file1 = null;
		file2 = null;
		file3 = null;
		file4 = null;
		cover = null;

		isAnnonceSelected.set(false);
		selectedAnnonce = null;
	}
	
	private void saveAlert(Annonce annonce){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Annonce saved successfully.");
		alert.setHeaderText(null);
		alert.setContentText("L annonce "+annonce.getTitre() +" a été crée et \nson id est "+ annonce.getAnnonceId() +".");
		alert.showAndWait();
	}
	
	private void updateAlert(Annonce annonce){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Annonce updated successfully.");
		alert.setHeaderText(null);
		alert.setContentText("L annonce "+annonce.getTitre()+" - ID: "+annonce.getAnnonceId() +" a été maj.");

		alert.showAndWait();

	}


	public String getAnnonceTitre() {
		return annonceTitre.getText();
	}

	public String getAnnoncePrix() {
		return annoncePrix.getText();
	}

	public String getAnnonceDesc() {
		return annonceDesc.getText();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

    	initPrintButton();
		
		this.setAuthenticateAgent(agentService.getAuthenticateAgent());
		connectedAgentName.setText(this.getAuthenticateAgent().toString());
		
		annonceTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		setColumnProperties();

		
		// Add all users into table
		loadAnnonces();
	}
	
	private void initPrintButton(){

		Image image = new Image(getClass().getResourceAsStream("/images/pdf-icon.png"));
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(100.0);
		imageView.setFitWidth(100.0);
		printButton.setGraphic(imageView);
		printButton.visibleProperty().bind(isAnnonceSelected);
		printButton.setOnAction(e -> {

				// --- Show Jasper Report on click-----
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						System.out.println("now open dialog!");
						try {
							PrintReport viewReport = new PrintReport();
							selectedAnnonce.setAgent(authenticateAgent);
							viewReport.showReport(selectedAnnonce);
						} catch (JRException e1) {
							e1.printStackTrace();
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
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
		
		colAnnonceId.setCellValueFactory(new PropertyValueFactory<>("annonceId"));
		colAnnonceTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
		colAnnoncePrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
		colAnnonceDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
		colEdit.setCellFactory(cellFactory);
	}
	
	Callback<TableColumn<Annonce, Boolean>, TableCell<Annonce, Boolean>> cellFactory =
			new Callback<TableColumn<Annonce, Boolean>, TableCell<Annonce, Boolean>>()
	{
		@Override
		public TableCell<Annonce, Boolean> call( final TableColumn<Annonce, Boolean> param)
		{
			final TableCell<Annonce, Boolean> cell = new TableCell<Annonce, Boolean>()
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
							Annonce annonce = getTableView().getItems().get(getIndex());
							updateAnnonce(annonce);
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

				private void updateAnnonce(Annonce annonce) {
					annonceId.setText(Long.toString(annonce.getAnnonceId()));
					annonceDesc.setText(annonce.getDescription());
					annonceTitre.setText(annonce.getTitre());
					annoncePrix.setText(annonce.getPrix());


					imageView1.setImage(ImageUtils.convertToJavaFXImage(annonce.getPhoto1(),260,210));
					imageView2.setImage(ImageUtils.convertToJavaFXImage(annonce.getPhoto2(),260,210));
					imageView3.setImage(ImageUtils.convertToJavaFXImage(annonce.getPhoto3(),260,210));
					imageView4.setImage(ImageUtils.convertToJavaFXImage(annonce.getPhoto4(),260,210));

					imageCover.setImage(ImageUtils.convertToJavaFXImage(annonce.getCover(),360,245));

					isAnnonceSelected.set(true);
					selectedAnnonce = annonce;
				}
			};
			return cell;
		}
	};



	/*
	 *  Add All users to observable list and update table
	 */
	private void loadAnnonces(){

		annonceList.clear();
		annonceList.addAll(authenticateAgent.getAnnonceList());

		annonceTable.setItems(annonceList);
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

		if(empty) alert.setContentText("Please Enter "+ field);
		else alert.setContentText("Please Enter Valid "+ field);

        alert.showAndWait();
	}

    @FXML
    private void toUserView(ActionEvent event) {
        stageManager.switchScene(FxmlView.USER);
    }

}
