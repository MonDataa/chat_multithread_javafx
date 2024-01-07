package com.example.tp_chat_multithread;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.application.Platform.exit;

public class ClientApplication_v3 extends Application {
    PrintWriter pw;
    Socket socket;
    InputStream inputStream;
    InputStreamReader isr;
    BufferedReader bufferedReader;
    boolean isconnected=true;

    public static void main(String[] args) {

        launch();
    }
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        BorderPane borderpane = new BorderPane();
        Label labelHost=new Label("Host :");
        TextField textFieldHost=new TextField("localhost");
        Label labelPort=new Label("Port :");
        TextField textFieldPort=new TextField("8888");
        Label labelUser=new Label("name :");
        TextField textFieldUser=new TextField("mounsif");
        Button buttonConnecter=new Button("Connecter");
        Button buttonDeconnecter=new Button("Deconnecter");

        HBox hBox=new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10,10,10,10)) ;
        hBox.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));
        hBox.getChildren().addAll(labelHost,textFieldHost,labelPort,textFieldPort,labelUser,textFieldUser,buttonConnecter,buttonDeconnecter);
        borderpane.setTop(hBox);

        VBox vBox=new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10,10,10,10)) ;
        ObservableList<String> listModel= FXCollections.observableArrayList();
        ListView<String> listView=new ListView<String>(listModel);
        vBox.getChildren().add(listView);
        borderpane.setCenter(vBox);

        Label labelMessage=new Label("Message :");
        TextField textFieldMessage=new TextField();
        textFieldMessage.setPrefSize(300,30);
        Button buttonEnvoyer=new Button("Envoye");

        HBox hBox2=new HBox();
        hBox2.setSpacing(10);
        hBox2.setPadding(new Insets(10)) ;
        hBox2.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));
        hBox2.getChildren().addAll(labelMessage,textFieldMessage,buttonEnvoyer);
        borderpane.setBottom(hBox2);


        Scene scene = new Scene(borderpane, 800, 600);
        stage.setTitle("client chat!");
        stage.setScene(scene);
        stage.show();

        buttonConnecter.setOnAction((evt)->{
            String host=textFieldHost.getText();
            int port=Integer.parseInt(textFieldPort.getText());
            try {
                socket=new Socket(host,port);
                inputStream=socket.getInputStream();
                isr=new InputStreamReader(inputStream);
                bufferedReader=new BufferedReader(isr);
                pw=new PrintWriter(socket.getOutputStream(),true);


                new Thread(()->{
                    String sendname=textFieldUser.getText();
                    pw.println(sendname);
                    System.out.println("sendname envoyer : "+sendname);
                    buttonConnecter.setVisible(false);
                    while (isconnected) {
                        try {
                                String response = bufferedReader.readLine();
                                Platform.runLater(() -> {
                                    listModel.add(response);
                                });
                                //listModel.set(0,response);
                                String[] data = response.substring(1, response.length() - 1).split(",");
                                String name_envoye = data[0];
                                String message = data[1];
                                System.out.println("affichage du Nom cote client: " + name_envoye + ",et Message cote client: " + message);

                                System.out.println("response : " + response);
                                System.out.println("response type : " + response.getClass());

                                Platform.runLater(() -> {
                                    listModel.remove(response);
                                    listModel.add(message + " ( " + name_envoye + " ) ");

                                });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }).start();

            }catch (Exception e){
                e.printStackTrace();
            }
        });

        buttonEnvoyer.setOnAction((evt)->{
            List<String> message_name=new ArrayList<>();
            String name=textFieldUser.getText();
            String message=textFieldMessage.getText();
            message_name.add(name);
            message_name.add(message);
            System.out.println("arraylist  message_name contient le nom : "+message_name.get(0)+" et le message "+message_name.get(1));
            /*new Thread(()->{
                pw.println(name);
            }).start();*/
            pw.println(message_name);
            System.out.println("message envoyer : "+message);
            System.out.println("nom qui a envoyer : "+name);
            textFieldMessage.clear();
        });

        buttonDeconnecter.setOnAction((evt) -> {
            try {
                isconnected=false;
                if (pw != null) {
                    pw.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Platform.exit();
            }
        });
    }

}