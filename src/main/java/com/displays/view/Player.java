package com.displays.view;

import com.displays.cams.Cams;
import com.sun.jna.Memory;
import javafx.application.Platform;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

import java.nio.ByteBuffer;

/**
 * Created by victor on 08.06.15.
 */
public class Player{


    private static final String FIRST_CAM = "v4l2:///dev/video0";
    private static final String SEOND_CAM = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
//        private static final String FIRST_CAM = "rtsp://192.168.2.232//rtsp_tunnel";
//        private static final String FIRST_CAM = "rtsp://192.168.2.2//rtsp_tunnel";
//    private static final String FIRST_CAM = "rtsp://admin:12345@192.168.2.64//rtsp_tunnel";
    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Cams getCams() {
        return cams;
    }

    public static String getFirstCam() {
        return FIRST_CAM;
    }

    private Cams cams;

    private ImageView imageView;

    private MediaPlayer mediaPlayer;

    private WritableImage writableImage;

    public Pane getPlayerHolder() {
        return playerHolder;
    }

    public void setPlayerHolder(Pane playerHolder) {
        this.playerHolder = playerHolder;
    }

    private Pane playerHolder;

    private WritablePixelFormat<ByteBuffer> pixelFormat;

    private FloatProperty videoSourceRatioProperty;

    private final String streamOption=":network-caching=200";

    public void initialize() {

        if(url.equals("v4l2:///dev/video0")){
            this.cams = Cams.WEBCAM;
        }
        else if(url.equals("rtsp://admin:12345@192.168.2.64//rtsp_tunnel")){
            this.cams = Cams.HICKVISION;
        }
        else if(url.equals("rtsp://192.168.2.232//rtsp_tunnel")){
            this.cams = Cams.BOSCH;
        } else if(url.equals("rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov")){
            this.cams = Cams.MOVIE;
        }

        mediaPlayer = new MediaPlayerFactory("-vvv", "--no-snapshot-preview",
                "--no-snapshot-sequential","--no-osd").newDirectMediaPlayer(new CanvasBufferFormatCallback(),
                (mediaPlayer1, nativeBuffers, bufferFormat) -> Platform.runLater(() -> {
                    Memory nativeBuffer = mediaPlayer1.lock()[0];
                    try {
                        ByteBuffer byteBuffer = nativeBuffer.getByteBuffer(0, nativeBuffer.size());
                        writableImage.getPixelWriter().setPixels(0, 0, bufferFormat.getWidth(), bufferFormat.getHeight(), pixelFormat, byteBuffer, bufferFormat.getPitches()[0]);
                    } finally {
                        mediaPlayer1.unlock();
                    }
                }));
        videoSourceRatioProperty = new SimpleFloatProperty(0.4f);
        pixelFormat = PixelFormat.getByteBgraPreInstance();
        initializeImageView();

        mediaPlayer.playMedia(url, streamOption);

    }
    public void stop(){
        mediaPlayer.stop();
    }

    public void camsChanger(){
        mediaPlayer.stop();
        if (this.cams == Cams.WEBCAM){
            setUrl(SEOND_CAM);
            initialize();
        } else if (this.cams == Cams.MOVIE){
            setUrl(getUrl());
            initialize();
        }
    }

    public void getSnapshot(){
        mediaPlayer.getSnapshot();
        mediaPlayer.nextFrame();
    }

    private void initializeImageView() {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        writableImage = new WritableImage((int) visualBounds.getWidth(), (int) visualBounds.getHeight());

        imageView = new ImageView(writableImage);
        playerHolder.getChildren().add(imageView);

        playerHolder.widthProperty().addListener((observable, oldValue, newValue) -> {
            fitImageViewSize(newValue.floatValue(), (float) playerHolder.getHeight());
        });

        playerHolder.heightProperty().addListener((observable, oldValue, newValue) -> {
            fitImageViewSize((float) playerHolder.getWidth(), newValue.floatValue());
        });

        videoSourceRatioProperty.addListener((observable, oldValue, newValue) -> {
            fitImageViewSize((float) playerHolder.getWidth(), (float) playerHolder.getHeight());
        });
    }

    private void fitImageViewSize(float width, float height) {
        Platform.runLater(() -> {
            float fitHeight = videoSourceRatioProperty.get() * width;
            if (fitHeight > height) {
                imageView.setFitHeight(height);
                double fitWidth = height / videoSourceRatioProperty.get();
                imageView.setFitWidth(fitWidth);
                imageView.setX((width - fitWidth) / 3);
                imageView.setY(0);
            }
            else {
                imageView.setFitWidth(width);
                imageView.setFitHeight(fitHeight);
                imageView.setY((height - fitHeight) / 2);
                imageView.setX(0);
            }
        });
    }


    private class CanvasBufferFormatCallback implements BufferFormatCallback {
        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            Platform.runLater(() -> videoSourceRatioProperty.set((float) sourceHeight / (float) sourceWidth));
            return new RV32BufferFormat((int) visualBounds.getWidth(), (int) visualBounds.getHeight());
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
