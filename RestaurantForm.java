import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import java.io.FileNotFoundException;
import java.io.File;

public class RestaurantForm extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage mainStage) throws FileNotFoundException {
        mainStage.setTitle("Restaurant Picker");
        BorderPane root = new BorderPane();
        ArrayList<Restaurant> restaurantList = new ArrayList<>();
        ArrayList<String> cuisineList = new ArrayList<>();
        ArrayList<String> addressList = new ArrayList<>();
        File file = new File("restaurants.txt");

        Label nameLabel = new Label("Restaurant Name:");
        TextField nameIn = new TextField();
        Label cuisineLabel = new Label("Cuisine:");
        TextField cuisineIn = new TextField();
        Label addressLabel = new Label("Address:");
        TextField addressIn = new TextField();
        Label desireLabel = new Label("Desire to try:");
        TextField desireIn = new TextField();
        VBox left = new VBox();
        HBox nameBox = new HBox();
        HBox cuisineBox = new HBox();
        HBox addressBox = new HBox();
        HBox desireBox = new HBox();
        nameBox.getChildren().addAll(nameLabel, nameIn);
        cuisineBox.getChildren().addAll(cuisineLabel, cuisineIn);
        addressBox.getChildren().addAll(addressLabel, addressIn);
        desireBox.getChildren().addAll(desireLabel, desireIn);
        left.getChildren().addAll(nameBox, cuisineBox, addressBox, desireBox);
        nameBox.setAlignment(Pos.CENTER_RIGHT);
        cuisineBox.setAlignment(Pos.CENTER_RIGHT);
        addressBox.setAlignment(Pos.CENTER_RIGHT);
        desireBox.setAlignment(Pos.CENTER_RIGHT);
        root.setLeft(left);

        TableView<Restaurant> table = new TableView<Restaurant>();
        table.setEditable(true);
        TableColumn<Restaurant, String> nameCol = new TableColumn<Restaurant, String>("Name");
        TableColumn<Restaurant, String> cuisineCol = new TableColumn<Restaurant, String>("Cuisine");
        TableColumn<Restaurant, String> addressCol = new TableColumn<Restaurant, String>("Address");
        TableColumn<Restaurant, Integer> desireCol = new TableColumn<Restaurant, Integer>("Desire to Try");
        table.getColumns().addAll(nameCol, cuisineCol, addressCol, desireCol);
        nameCol.setCellValueFactory(new PropertyValueFactory<Restaurant, String>("name"));
        cuisineCol.setCellValueFactory(new PropertyValueFactory<Restaurant, String>("cuisine"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Restaurant, String>("address"));
        desireCol.setCellValueFactory(new PropertyValueFactory<Restaurant, Integer>("desire"));
        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        cuisineCol.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        addressCol.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        desireCol.prefWidthProperty().bind(table.widthProperty().multiply(0.25));

        HBox bottom = new HBox();
        Button addBtn = new Button("Add Restaurant To List");
        addBtn.setOnAction(event -> {
            if (nameIn.getText().isEmpty()) {
                Alert emptyAlert = new Alert(AlertType.ERROR);
                emptyAlert.setContentText("The field for \"Name\" is empty");
                emptyAlert.show();
                return;
            }
            if (cuisineIn.getText().isEmpty()) {
                Alert emptyAlert = new Alert(AlertType.ERROR);
                emptyAlert.setContentText("The field for \"Cuisine\" is empty");
                emptyAlert.show();
                return;
            }
            if (addressIn.getText().isEmpty()) {
                Alert emptyAlert = new Alert(AlertType.ERROR);
                emptyAlert.setContentText("The field for \"Address\" is empty");
                emptyAlert.show();
                return;
            }
            if (desireIn.getText().isEmpty()) {
                Alert emptyAlert = new Alert(AlertType.ERROR);
                emptyAlert.setContentText("The field for \"Desire to try\" is empty");
                emptyAlert.show();
                return;
            }

            if (desireIn.getText().charAt(0) == '-') {
                Alert negNumAlert = new Alert(AlertType.ERROR);
                negNumAlert.setContentText("The input for \"Desire to try\" cannot be negative");
                negNumAlert.show();
                return;
            }

            try {
                Integer.parseInt(desireIn.getText());
            } catch (NumberFormatException nfe) {
                Alert notNumAlert = new Alert(AlertType.ERROR);
                notNumAlert.setContentText("The input for \"Desire to try\" is not a number");
                notNumAlert.show();
                return;
            }

            restaurantList.add(new Restaurant(nameIn.getText(), cuisineIn.getText(), addressIn.getText(), Integer.parseInt(desireIn.getText())));
            if (!cuisineList.contains(cuisineIn.getText())) {
                cuisineList.add(cuisineIn.getText());
            }
            if (!addressList.contains(addressIn.getText())) {
                addressList.add(addressIn.getText());
            }
            ObservableList<Restaurant> observableList = FXCollections.observableList(restaurantList);
            table.setItems(observableList);

            nameIn.clear();
            cuisineIn.clear();
            addressIn.clear();
            desireIn.clear();
        });

        Button resetBtn = new Button("Reset List");
        resetBtn.setStyle("-fx-background-color: #FF0000; ");
        resetBtn.setOnAction(event -> {
            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");
            Alert resetAlert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to reset the form?", yes, no);
            resetAlert.showAndWait().ifPresent(response -> {
                if (response == yes) {
                    if (file.exists()) {
                        file.delete();
                    }
                    restaurantList.clear();
                    nameIn.clear();
                    cuisineIn.clear();
                    addressIn.clear();
                    desireIn.clear();
                }
                ObservableList<Restaurant> observableList = FXCollections.observableList(restaurantList);
                table.setItems(observableList);
                return;
            });
        });

        Button fileBtn = new Button("Save to File");
        fileBtn.setOnAction(event -> {
            FileUtil.saveIdeasToFile(restaurantList, file);
        });
        
        Button importBtn = new Button("Import Items");
        importBtn.setOnAction(event -> {
            try {
            	File importFile = new File("restaurants.txt");
            	Scanner in = new Scanner(importFile);
                while (in.hasNextLine()){
                    String[] arr = in.nextLine().split(",");
                    restaurantList.add(new Restaurant(arr[0], arr[1], arr[2], Integer.parseInt(arr[3])));
                }
                in.close();
                ObservableList<Restaurant> observableList = FXCollections.observableList(restaurantList);
                table.setItems(observableList);
                return;
            } catch (FileNotFoundException fnfe) {
            	Alert noFile = new Alert(AlertType.ERROR);
                noFile.setContentText("File Not Found");
                noFile.show();
                return;
            }
        });
        
        Button chooseBtn = new Button("Choose Restaurant");
        chooseBtn.setOnAction(event -> {
            Stage chooseStage = new Stage();
            chooseStage.initOwner(mainStage);
            VBox chooseVBox = new VBox(20);
            Button randomlyBtn = new Button("Randomly");
            randomlyBtn.setOnAction(event1 -> {
                Restaurant randomRest = restaurantList.get((int) (Math.random() * restaurantList.size()));
                ButtonType ok = new ButtonType("Ok");
                Alert chosenRest = new Alert(AlertType.NONE, randomRest.toString(), ok);
                chosenRest.showAndWait();
                return;
            });
            Button cuisineBtn = new Button("By Cuisine");
            cuisineBtn.setOnAction(event2 -> {
                Stage cuisineStage = new Stage();
                cuisineStage.initOwner(chooseStage);
                VBox cuisineVBox = new VBox(20);
                ComboBox cuisineComboBox = new ComboBox();
                for (int i = 0; i < cuisineList.size(); i++) {
                    cuisineComboBox.getItems().add(cuisineList.get(i));
                }
                Button cuisineChosenBtn = new Button("Choose Cuisine");
                cuisineChosenBtn.setOnAction(event3 -> {
                    Restaurant randomRest = restaurantList.get((int) (Math.random() * restaurantList.size()));
                    do {
                        randomRest = restaurantList.get((int) (Math.random() * restaurantList.size()));
                    } while (!randomRest.getCuisine().equals(cuisineComboBox.getValue()));
                    ButtonType ok = new ButtonType("Ok");
                    Alert chosenRest = new Alert(AlertType.NONE, randomRest.toString(), ok);
                    chosenRest.showAndWait();
                    return;
                });
                cuisineVBox.getChildren().addAll(cuisineComboBox, cuisineChosenBtn);
                Scene chooseCuisine = new Scene(cuisineVBox, 300, 200);
                cuisineStage.setScene(chooseCuisine);
                cuisineStage.show();
                return;
            });
            Button addressBtn = new Button("By Town");
            addressBtn.setOnAction(event2 -> {
                Stage addressStage = new Stage();
                addressStage.initOwner(chooseStage);
                VBox addressVBox = new VBox(20);
                ComboBox addressComboBox = new ComboBox();
                for (int i = 0; i < addressList.size(); i++) {
                    addressComboBox.getItems().add(cuisineList.get(i));
                }
                Button addressChosenBtn = new Button("Choose Town");
                addressChosenBtn.setOnAction(event3 -> {
                    Restaurant randomRest = restaurantList.get((int) (Math.random() * restaurantList.size()));
                    do {
                        randomRest = restaurantList.get((int) (Math.random() * restaurantList.size()));
                    } while (!randomRest.getAddress().equals(addressComboBox.getValue()));
                    ButtonType ok = new ButtonType("Ok");
                    Alert chosenRest = new Alert(AlertType.NONE, randomRest.toString(), ok);
                    chosenRest.showAndWait();
                    return;
                });
                addressVBox.getChildren().addAll(addressComboBox, addressChosenBtn);
                Scene chooseAddress = new Scene(addressVBox, 300, 200);
                addressStage.setScene(chooseAddress);
                addressStage.show();
                return;
            });
            chooseVBox.getChildren().addAll(new Label("How would you like to choose your restaurant?"), randomlyBtn, cuisineBtn, addressBtn);
            Scene chooseRandomness = new Scene(chooseVBox, 300, 200);
            chooseStage.setScene(chooseRandomness);
            chooseStage.show();
            return;
        });

        bottom.getChildren().addAll(addBtn, resetBtn, fileBtn, importBtn, chooseBtn);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(10);
        root.setBottom(bottom);
        root.setCenter(table);


        Scene scene = new Scene(root, 750, 750);
        mainStage.setScene(scene);
        mainStage.show();
    }
    
    
}