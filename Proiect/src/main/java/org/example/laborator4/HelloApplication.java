package org.example.laborator4;

import Domain.Inchiriere;
import Domain.Masina;
import Repository.IRepository;
import Repository.InchirieriDBRepository;
import Repository.MasiniDBRepository;
import Repository.MemoryRepository;
import Service.InchiriereService;
import Service.MasinaService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        IRepository<Masina> carRepo = new MasiniDBRepository();
        //IRepository<Masina> carRepo = new MemoryRepository<>();
        MasinaService carService = new MasinaService(carRepo);

        IRepository<Inchiriere> inchiriereRepo = new InchirieriDBRepository();
        //IRepository<Inchiriere> inchiriereRepo = new MemoryRepository<>();
        InchiriereService inchiriereService = new InchiriereService(inchiriereRepo, carRepo);


        VBox mainVerticalBox = new VBox();
        mainVerticalBox.setPadding(new Insets(10));

        ObservableList<Masina> cars  = FXCollections.observableArrayList(carService.getAll());
        ObservableList<Inchiriere> inchirieri  = FXCollections.observableArrayList(inchiriereService.getAll());

        ListView<Masina> masiniListView = new ListView<Masina>(cars);
        ListView<Inchiriere> inchirieriListView = new ListView<Inchiriere>(inchirieri);

        HBox tabele = new HBox();
        tabele.getChildren().add(masiniListView);
        tabele.getChildren().add(inchirieriListView);

        mainVerticalBox.getChildren().add(tabele);

        GridPane gridPane = new GridPane();

        Label idLabel = new Label("Id: ");
        idLabel.setPadding(new Insets(10,0,10,0));

        Label marcaLabel = new Label("Marca: ");
        marcaLabel.setPadding(new Insets(10,0,10,0));

        Label modelLabel = new Label("Model: ");
        modelLabel.setPadding(new Insets(10,0,10,0));

        TextField idTextField = new TextField();
        TextField marcaTextField = new TextField();
        TextField modelTextField = new TextField();

        gridPane.add(idLabel, 0, 0);
        gridPane.add(marcaLabel, 0, 1);
        gridPane.add(modelLabel, 0, 2);

        gridPane.add(idTextField, 1, 0);
        gridPane.add(marcaTextField, 1, 1);
        gridPane.add(modelTextField, 1, 2);

        HBox hBox = new HBox();
        hBox.getChildren().add(gridPane);
        hBox.setPadding(new Insets(0,10,0,0));
        hBox.setAlignment(Pos.CENTER);

        GridPane gridPane2 = new GridPane();

        Label idLabel2 = new Label("Id: ");
        idLabel2.setPadding(new Insets(10,0,10,0));

        Label idMasinaLabel = new Label("Id Masina: ");
        idMasinaLabel.setPadding(new Insets(10,0,10,0));

        Label data_inceputLabel = new Label("Data inceput: ");
        data_inceputLabel.setPadding(new Insets(10,0,10,0));

        Label data_sfarsitLabel = new Label("Data sfarsit: ");
        data_sfarsitLabel.setPadding(new Insets(10,0,10,0));

        TextField idTextField2 = new TextField();
        TextField idMasinaTextField = new TextField();
        TextField data_inceputTextField = new TextField();
        TextField data_sfarsitTextField = new TextField();

        gridPane2.add(idLabel2, 0, 0);
        gridPane2.add(idMasinaLabel, 0, 1);
        gridPane2.add(data_inceputLabel, 0, 2);
        gridPane2.add(data_sfarsitLabel, 0, 3);

        gridPane2.add(idTextField2, 1, 0);
        gridPane2.add(idMasinaTextField, 1, 1);
        gridPane2.add(data_inceputTextField, 1, 2);
        gridPane2.add(data_sfarsitTextField, 1, 3);

        HBox hBox2 = new HBox();
        hBox2.getChildren().add(gridPane2);
        hBox2.setPadding(new Insets(0,0,0,10));
        hBox2.setAlignment(Pos.CENTER);

        HBox inserare = new HBox();
        inserare.setPadding(new Insets(0,10,0,10));
        inserare.setAlignment(Pos.CENTER);
        inserare.getChildren().add(hBox);
        inserare.getChildren().add(hBox2);

        mainVerticalBox.getChildren().add(inserare);

        HBox butoaneBox = new HBox();

        Button addButton = new Button("Adauga");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    if( ! idTextField.getText().isBlank() && ! marcaTextField.getText().isBlank() && ! modelTextField.getText().isBlank()){
                        int id1 = Integer.parseInt(idTextField.getText());
                        String marca = marcaTextField.getText();
                        String model = modelTextField.getText();

                        carService.add(id1, marca, model);
                        cars.setAll(carService.getAll());


                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Eroare");
                        alert.setHeaderText("Eroare");
                        alert.setContentText("Nu ati introdus datele necesare");
                        alert.showAndWait();
                    }

                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Eroare");
                    alert.setHeaderText("Eroare");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        Button addButtonI= new Button("Adauga inchiriere");
        addButtonI.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    if(! idTextField2.getText().isBlank() && ! idMasinaTextField.getText().isBlank() && ! data_inceputTextField.getText().isBlank() && ! data_sfarsitTextField.getText().isBlank()){
                        int id2 = Integer.parseInt(idTextField2.getText());
                        int id_masina = Integer.parseInt(idMasinaTextField.getText());
                        LocalDateTime data_inceput = LocalDateTime.parse(data_inceputTextField.getText());
                        LocalDateTime data_sfarsit = LocalDateTime.parse(data_sfarsitTextField.getText());

                        inchiriereService.add(id2, carService.cautare_masina(id_masina), data_inceput, data_sfarsit);
                        inchirieri.setAll(inchiriereService.getAll());

                    }else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Eroare");
                        alert.setHeaderText("Eroare");
                        alert.setContentText("Nu ati introdus datele necesare");
                        alert.showAndWait();
                    }

                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Eroare");
                    alert.setHeaderText("Eroare");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        butoaneBox.getChildren().add(addButton);
        butoaneBox.getChildren().add(addButtonI);


        Button removeButton = new Button("Sterge");
        removeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    if( ! idTextField.getText().isBlank()){
                        int id = Integer.parseInt(idTextField.getText());
                        carService.remove(id);
                        cars.setAll(carService.getAll());
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Eroare");
                        alert.setHeaderText("Eroare");
                        alert.setContentText("Nu ati introdus datele necesare");
                        alert.showAndWait();
                    }

                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Eroare");
                    alert.setHeaderText("Eroare");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        Button removeButtonI = new Button("Sterge inchiriere");
        removeButtonI.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    if(! idTextField2.getText().isBlank()){
                        int id = Integer.parseInt(idTextField2.getText());
                        inchiriereService.remove(id);
                        inchirieri.setAll(inchiriereService.getAll());
                    }else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Eroare");
                        alert.setHeaderText("Eroare");
                        alert.setContentText("Nu ati introdus datele necesare");
                        alert.showAndWait();
                    }

                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Eroare");
                    alert.setHeaderText("Eroare");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        butoaneBox.getChildren().add(removeButton);
        butoaneBox.getChildren().add(removeButtonI);

        Button updateButton = new Button("Modifica");
        updateButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    if( ! idTextField.getText().isBlank() && ! marcaTextField.getText().isBlank() && ! modelTextField.getText().isBlank()){
                        int id = Integer.parseInt(idTextField.getText());
                        String marca = marcaTextField.getText();
                        String model = modelTextField.getText();

                        carService.update(id, marca, model);
                        cars.setAll(carService.getAll());
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Eroare");
                        alert.setHeaderText("Eroare");
                        alert.setContentText("Nu ati introdus datele necesare");
                        alert.showAndWait();
                    }

                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Eroare");
                    alert.setHeaderText("Eroare");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        Button updateButtonI = new Button("Modifica inchiriere");
        updateButtonI.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    if(! idTextField2.getText().isBlank() && ! idMasinaTextField.getText().isBlank() && ! data_inceputTextField.getText().isBlank() && ! data_sfarsitTextField.getText().isBlank()){
                        int id = Integer.parseInt(idTextField2.getText());
                        int id_masina = Integer.parseInt(idMasinaTextField.getText());
                        LocalDateTime data_inceput = LocalDateTime.parse(data_inceputTextField.getText());
                        LocalDateTime data_sfarsit = LocalDateTime.parse(data_sfarsitTextField.getText());

                        inchiriereService.update(id, carService.cautare_masina(id_masina), data_inceput, data_sfarsit);
                        inchirieri.setAll(inchiriereService.getAll());
                    }else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Eroare");
                        alert.setHeaderText("Eroare");
                        alert.setContentText("Nu ati introdus datele necesare");
                        alert.showAndWait();
                    }

                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Eroare");
                    alert.setHeaderText("Eroare");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        butoaneBox.getChildren().add(updateButton);
        butoaneBox.getChildren().add(updateButtonI);

        ListView resultArea = new ListView();
        resultArea.setPadding(new Insets(10));

        Button cmdim = new Button("CMDIM"); //cmdim = cele mai des inchiriate masini

        cmdim.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    resultArea.getItems().clear();
                    Collection<Inchiriere> inchirieri = inchiriereService.getAll();
                    for(String s: inchiriereService.cmdim())
                        resultArea.getItems().add(s);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button inchirierilunare = new Button("Inchirieri lunare");
        inchirierilunare.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    resultArea.getItems().clear();
                    Collection<Inchiriere> inchirieri = inchiriereService.getAll();
                    for(String s: inchiriereService.inchirierilunare())
                        resultArea.getItems().add(s);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button mostused = new Button("Most used"); // most used car by the total days of rental
        mostused.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    resultArea.getItems().clear();
                    Collection<Inchiriere> inchirieri = inchiriereService.getAll();
                    for(String s: inchiriereService.mostused())
                        resultArea.getItems().add(s);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        HBox rapoarte = new HBox();
        rapoarte.setPadding(new Insets(10));
        VBox butoanerapoarte = new VBox(40);
        butoanerapoarte.setPadding(new Insets(10));
        butoanerapoarte.getChildren().add(cmdim);
        butoanerapoarte.getChildren().add(inchirierilunare);
        butoanerapoarte.getChildren().add(mostused);

        rapoarte.getChildren().add(butoanerapoarte);
        rapoarte.getChildren().add(resultArea);
        mainVerticalBox.getChildren().add(butoaneBox);
        mainVerticalBox.getChildren().add(rapoarte);

        Scene scene = new Scene(mainVerticalBox, 500, 700);
        stage.setTitle("Aplicatie");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}