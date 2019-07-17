package com.github.eloymediaplayer.controller;

import java.io.File;
import java.net.URL;
import java.util.*;

import com.github.eloymediaplayer.model.MusicMP3File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainView
{
    protected ObservableList<MusicMP3File> musicMP3FileObservableList = FXCollections.observableArrayList();

    @FXML
    private MediaPlayerController mediaPlayerController;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane borderPane;

    @FXML
    private MenuItem openMenuItem;

    @FXML
    private TableView<MusicMP3File> playlistTableView;

    @FXML
    private TableColumn<MusicMP3File, String> nameTableColumn;

    @FXML
    private TableColumn<MusicMP3File, String> durationTableColumn;

    @FXML
    void initialize()
    {
        assert openMenuItem != null : "fx:id=\"openMenuItem\" was not injected: check your FXML file 'MainView.fxml'.";

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<MusicMP3File, String>("musicName"));
        durationTableColumn.setCellValueFactory(new PropertyValueFactory<MusicMP3File, String>("musicDuration"));

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileChooserExtensionFilter = new FileChooser.ExtensionFilter("music files", "*.mp3");
        fileChooser.getExtensionFilters().add(fileChooserExtensionFilter);


        openMenuItem.setOnAction(event ->
        {
            Stage stage = (Stage) borderPane.getScene().getWindow();

            int num = 0;
            List<File> files = fileChooser.showOpenMultipleDialog(stage);
            for (File file : files)
            {
                MusicMP3File musicMP3File = new MusicMP3File(file);
                musicMP3FileObservableList.add(musicMP3File);
                num++;
                System.out.println(num);

            }
//            Collections.shuffle(musicsListNotPlayed);

            playlistTableView.getItems().setAll(musicMP3FileObservableList);
            mediaPlayerController.createNewMusicList(musicMP3FileObservableList);
        });


        playlistTableView.setRowFactory(tv ->
        {
            TableRow<MusicMP3File> row = new TableRow<>();
            // setting the TableRow of each mp3-file
            row.itemProperty().addListener((obs, oldItem, newItem) ->
            {
                MusicMP3File RowMusicMP3File = row.getItem();
                for (MusicMP3File musicMP3File : musicMP3FileObservableList)
                {
                    if (RowMusicMP3File != null && RowMusicMP3File.equals(musicMP3File))
                    {
                        musicMP3File.setMusicMP3FileTableRow(row);
                        break;
                    }
                }
            });

            // each row's double click listener
            row.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 2 && (!row.isEmpty()))
                    mediaPlayerController.playSpecificTrack(row.getItem());
            });
            return row;
        });


    }
}
