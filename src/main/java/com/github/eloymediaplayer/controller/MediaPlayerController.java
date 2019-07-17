package com.github.eloymediaplayer.controller;

import java.net.URL;
import java.util.*;

import com.github.eloymediaplayer.model.MusicMP3File;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MediaPlayerController implements Runnable
{
    private Thread thread;
    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean playing = false;
    private boolean hasBeenRewound = false;
    private MusicMP3File currentPlayingMusicMP3File = null;
    private ListIterator musicListIterator = null;
    private ObservableList<MusicMP3File> musicMP3FileObservableList;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Slider mediaPlayerSlider;

    @FXML
    private Button playPauseButton;

    @FXML
    private Button rewindButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button forwardButton;

    @FXML
    void playPauseButtonAction(ActionEvent event)
    {
        if (mediaPlayer == null) playNextMusicFromPlayList();
        else
        {
            if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING))
            {
                mediaPlayer.pause();
                playPauseButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/icon/icon-play.png"), 40.0, 40.0, false, false)));

            }

            if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED) || mediaPlayer.getStatus().equals(MediaPlayer.Status.STOPPED))
            {
                mediaPlayer.play();
                playPauseButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/icon/icon-pause.png"), 40.0, 40.0, false, false)));
            }
        }


    }

    @FXML
    void forwardButtonAction(ActionEvent event)
    {
        playNextMusicFromPlayList();
    }

    @FXML
    void rewindButtonAction(ActionEvent event)
    {
    }

    @FXML
    void stopButtonAction(ActionEvent event)
    {

    }


    @FXML
    void initialize()
    {
        assert playPauseButton != null : "fx:id=\"playPauseButton\" was not injected: check your FXML file 'MediaPlayerController.fxml'.";
        assert rewindButton != null : "fx:id=\"rewindButton\" was not injected: check your FXML file 'MediaPlayerController.fxml'.";
        assert stopButton != null : "fx:id=\"stopButton\" was not injected: check your FXML file 'MediaPlayerController.fxml'.";
        assert forwardButton != null : "fx:id=\"forwardButton\" was not injected: check your FXML file 'MediaPlayerController.fxml'.";

    }


    @Override
    public void run()
    {
        playing = true;
        while (playing)
        {
            mediaPlayerSlider.setOnMouseReleased(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    mediaPlayer.seek(Duration.seconds(mediaPlayerSlider.getValue()));
                }
            });
            mediaPlayerSlider.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    mediaPlayer.seek(Duration.seconds(mediaPlayerSlider.getValue()));
                }
            });

            try
            {
                Thread.sleep(100);
                if (mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING) && !mediaPlayerSlider.isPressed())
                {
                    mediaPlayerSlider.setValue(mediaPlayer.getCurrentTime().toSeconds());
                }
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }

        System.out.println("Thread Exiting");
    }

    public void start(MediaPlayer mediaPlayer, long musicDuration)
    {
        this.mediaPlayer = mediaPlayer;
        System.out.println("Starting ");
        mediaPlayerSlider.setMin(0);
        mediaPlayerSlider.setMax(musicDuration);
        mediaPlayerSlider.setValue(0);
        if (thread == null)
        {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void setPlaying(boolean playing)
    {
        this.playing = playing;
    }


    protected void createNewMusicList(ObservableList<MusicMP3File> musicMP3FileObservableList)
    {
        this.musicMP3FileObservableList = musicMP3FileObservableList;
        musicListIterator = musicMP3FileObservableList.listIterator();
    }

    private void playNextMusicFromPlayList()
    {
        if (musicListIterator.hasNext())
        {
            if (mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING))
            {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            if (hasBeenRewound == true)
            {
                musicListIterator.next();
                hasBeenRewound = false;
            }

            MusicMP3File musicMP3File = (MusicMP3File) musicListIterator.next();
            media = new Media(musicMP3File.getFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.errorProperty().addListener(event ->
            {
                // todo: an error occurred: probably MediaException.Type MEDIA_UNSUPPORTED
                // todo: add new style for corrupted file
                mediaPlayer.stop();
                mediaPlayer.dispose();
                playNextMusicFromPlayList();
            });

            mediaPlayer.stop();
            mediaPlayer.setStartTime(new Duration(0)); // on OS-X, we need to set it.
            mediaPlayer.play();
            playPauseButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/icon/icon-pause.png"), 40.0, 40.0, false, false)));
            start(mediaPlayer, musicMP3File.getDuration().getSeconds());

            if (currentPlayingMusicMP3File != null)
            {
                currentPlayingMusicMP3File.getMusicMP3FileTableRow().getStyleClass().remove("table-view-row-playing");
            }
            currentPlayingMusicMP3File = musicMP3File;
            currentPlayingMusicMP3File.getMusicMP3FileTableRow().getStyleClass().add("table-view-row-playing");
        } else
        {
            playPauseButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/icon/icon-play.png"), 40.0, 40.0, false, false)));
        }

        mediaPlayer.setOnEndOfMedia(() ->
        {
//            mediaPlayerSlider.setPlaying(false);
            playNextMusicFromPlayList();
        });
    }


    protected void playSpecificTrack(MusicMP3File selectedMusicToPlay)
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        musicListIterator = this.musicMP3FileObservableList.listIterator();
        boolean find = false;
        while (find == false)
        {
            if (musicListIterator.hasNext())
            {
                MusicMP3File musicMP3File = (MusicMP3File) musicListIterator.next();
                if (musicMP3File.getMusicName().equals(selectedMusicToPlay.getMusicName()))
                {
                    musicListIterator.previous();
                    find = true;
                }
            }
        }
        playNextMusicFromPlayList();
    }
}
