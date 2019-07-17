package com.github.eloymediaplayer.model;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableRow;
import java.io.*;
import java.time.Duration;
import java.util.Objects;

public class MusicMP3File
{
    private SimpleStringProperty musicName;
    private SimpleStringProperty musicDuration;
    private Duration duration;
    private File file;

    private TableRow<MusicMP3File> musicMP3FileTableRow;

    public MusicMP3File(File file)
    {
        this.musicName = new SimpleStringProperty(file.getName());
        this.file = file;

        try
        {
            Mp3File mp3file = new Mp3File(file);

            long durationLong = mp3file.getLengthInMilliseconds();
            this.duration = Duration.ofMillis(durationLong);
            Long minutes = duration.toMinutes();
            Long seconds = duration.minusMinutes(minutes).getSeconds();
            this.musicDuration = new SimpleStringProperty(minutes + ":" + seconds);
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (UnsupportedTagException e)
        {
            e.printStackTrace();
        } catch (InvalidDataException e)
        {
            System.out.println(musicName);
            e.printStackTrace();
        }

    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicMP3File that = (MusicMP3File) o;
        return Objects.equals(musicName, that.musicName) && Objects.equals(musicDuration, that.musicDuration) && Objects.equals(file, that.file);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(musicName, musicDuration, file);
    }

    public TableRow<MusicMP3File> getMusicMP3FileTableRow()
    {
        return musicMP3FileTableRow;
    }

    public void setMusicMP3FileTableRow(TableRow<MusicMP3File> musicMP3FileTableRow)
    {
        this.musicMP3FileTableRow = musicMP3FileTableRow;
    }

    public String getMusicName()
    {
        return musicName.get();
    }

    public SimpleStringProperty musicNameProperty()
    {
        return musicName;
    }

    public void setMusicName(String musicName)
    {
        this.musicName.set(musicName);
    }

    public String getMusicDuration()
    {
        return musicDuration.get();
    }

    public SimpleStringProperty musicDurationProperty()
    {
        return musicDuration;
    }

    public void setMusicDuration(String musicDuration)
    {
        this.musicDuration.set(musicDuration);
    }

    public Duration getDuration()
    {
        return duration;
    }

    public void setDuration(Duration duration)
    {
        this.duration = duration;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }
}
