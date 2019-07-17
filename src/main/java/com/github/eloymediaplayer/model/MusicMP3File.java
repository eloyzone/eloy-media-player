package com.github.eloymediaplayer.model;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableRow;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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

    private Duration calculateDuration(File file)
    {
        Duration duration = null;
        try
        {
            InputStream input = new FileInputStream(file);
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();

            // List all metadata
//            String[] metadataNames = metadata.names();

//            for (String name : metadataNames)
//            {
//                System.out.println(name + ": " + metadata.get(name));
//            }

            double durationDouble = Double.parseDouble(metadata.get("xmpDM:duration"));

            duration = Duration.ofMillis((long) durationDouble);

            // Retrieve the necessary info from metadata
            // Names - title, xmpDM:artist etc. - mentioned below may differ based
//            System.out.println("----------------------------------------------");
//            System.out.println("Title: " + metadata.get("title"));
//            System.out.println("Artists: " + metadata.get("xmpDM:artist"));
//            System.out.println("Composer : " + metadata.get("xmpDM:composer"));
//            System.out.println("Genre : " + metadata.get("xmpDM:genre"));
//            System.out.println("Album : " + metadata.get("xmpDM:album"));

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (SAXException e)
        {
            e.printStackTrace();
        } catch (TikaException e)
        {
            e.printStackTrace();
        }
        return duration;
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
