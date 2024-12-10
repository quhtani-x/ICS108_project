    package com.example.ics_project_v2;

    import javafx.scene.Scene;
    import javafx.scene.control.TextField;
    import javafx.scene.layout.Background;
    import javafx.scene.layout.BackgroundFill;
    import javafx.scene.layout.Pane;
    import javafx.scene.layout.VBox;
    import javafx.scene.paint.Color;
    import javafx.scene.text.Text;
    import javafx.stage.Stage;

    public class WelcomeScene {

        MainPane mainPane = new MainPane();
        Pane pane ;

        private Scene mainScene ;

        private Scene welcomeScene;

        public WelcomeScene(Stage stage) {
            // Create a VBox for layout
            VBox layout = new VBox(20); // Spacing between elements
            layout.setPrefSize(800, 800);
            layout.setStyle("-fx-alignment: center; -fx-background-color: #000000;");

            // Create a Text element
            Text welcomeText = new Text("Enter your name to save your score");
            welcomeText.setFill(Color.WHITE);
            welcomeText.setStyle("-fx-font-size: 20;");

            // Create a TextField
            TextField nameInput = new TextField();
            nameInput.setPromptText("Enter your name...");
            nameInput.setMaxWidth(300);

            // Add an event handler for pressing Enter
            nameInput.setOnAction(e -> {
                // Switch to the main scene when Enter is pressed

                mainPane.setName(nameInput.getText());
                pane = mainPane.getPane();
                mainScene =  new Scene(pane, 800, 800, Color.BLACK);
                pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
                stage.setScene(mainScene);
                pane.requestFocus();

            });

            // Add components to the layout
            layout.getChildren().addAll(welcomeText, nameInput);

            // Create the scene
            welcomeScene = new Scene(layout, 800, 800);
        }

        public Scene getScene() {
            return welcomeScene;
        }
    }
