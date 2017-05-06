/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guitartuner;

//import java.net.URL;
import guitartuner.gui.MainPanel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Tomáš
 */
public class GUIController extends Application {

    private Stage stage;
    //private ToneGenerator toneGen;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        //toneGen = new ToneGenerator();
        System.out.println("start()");
        
        MainPanel root = new MainPanel();
        Scene scene = new Scene(root, 550,400);

        this.stage.setScene(scene);
        this.stage.setTitle("Guitar tuner");
        this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });
        this.stage.show();
    }

    @Override
    public void stop() {
        System.out.println("stop(): Stage is closing");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
