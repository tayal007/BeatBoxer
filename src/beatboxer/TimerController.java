/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beatboxer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author kunal
 */
public class TimerController implements Initializable {

    @FXML
    private Label minuteLabel;
    @FXML
    private Label secondLabel;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;
    @FXML
    private Button stop;
    @FXML
    private Slider minuteSlider;
    @FXML
    private Slider secondSlider;
    @FXML
    private void stop(){
        BeatBoxer.timer.stop();
        BeatBoxer.timer = null;
        minuteSlider.setValue(5);
        secondSlider.setValue(0);
        stop.setDisable(true);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        minuteLabel.textProperty().bind(Bindings.format("%02.0f",minuteSlider.valueProperty()));
        secondLabel.textProperty().bind(Bindings.format("%02.0f",secondSlider.valueProperty()));
        if(BeatBoxer.timer!=null){
            stop.setDisable(false);
            BeatBoxer.timer.currentTimeProperty().addListener(new ChangeListener<Duration>(){
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    minuteSlider.setValue(Math.floor(BeatBoxer.timer.getTotalDuration().toMinutes() - newValue.toMinutes()));
                    secondSlider.setValue(Math.floor(BeatBoxer.timer.getTotalDuration().toSeconds() - newValue.toSeconds())%60);
                } 
            });
        }
        else
            stop.setDisable(true);
    }    

    @FXML
    private void okExecute(ActionEvent event) {
        double d = Double.parseDouble(minuteLabel.getText())*60 + Double.parseDouble(secondLabel.getText());
        BeatBoxer.timer = new Timeline(new KeyFrame(Duration.seconds(d), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(BeatBoxer.mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
                    BeatBoxer.play();
                }
                BeatBoxer.timer = null;
            }
        }));
        BeatBoxer.timer.setCycleCount(0);
        BeatBoxer.timer.play();
        System.out.println(BeatBoxer.timer.getTotalDuration().toSeconds());
        System.out.println(BeatBoxer.timer.getCurrentTime().toSeconds());
        cancel(new ActionEvent());
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage currentStage = (Stage) cancel.getScene().getWindow();
        currentStage.close();
    }
    
}
