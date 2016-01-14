package com.objdetect;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Created by victor on 17.11.15.
 */
public class TemplateMatching {

    public static void main(String[] args) {
        System.out.println("\nRunning Template Matching");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture capture = new VideoCapture();
        capture.open(0);
        Mat templ = Highgui.imread("2.png");
//        Mat img = Highgui.imread("1.png");

        int match_method = Imgproc.TM_CCOEFF_NORMED;

        Mat img = new Mat();
        MyFrame frame = new MyFrame();
        frame.setVisible(true);

        while (true){
            capture.read(img);
            // / Create the result matrix
            int result_cols = img.cols() - templ.cols() + 1;
            int result_rows = img.rows() - templ.rows() + 1;
            Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

            // / Do the Matching and Normalize
            Imgproc.matchTemplate(img, templ, result, match_method);
            Core.normalize(result, result, 0.9, 1, Core.NORM_MINMAX, -1, new Mat());
        Imgproc.threshold(result, result, 0.8, 1 , Imgproc.THRESH_TOZERO);
            // / Localizing the best match with minMaxLoc


            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

            Point matchLoc;
            if (match_method == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED) {
                matchLoc = mmr.minLoc;
            } else {
                matchLoc = mmr.maxLoc;
            }

            // / Show me what you got
//            Core.rectangle(img, matchLoc, new Point(matchLoc.x + templ.cols(),
//                    matchLoc.y + templ.rows()), new Scalar(0, 255, 0));

            double threshold = 0.9;
                if (mmr.maxVal >= threshold)
                {
                    /// Show me what you got
                    Core.rectangle(img, matchLoc, new Point(matchLoc.x + templ.cols(),
                            matchLoc.y + templ.rows()), new Scalar(0, 255, 0));
                }


            frame.render(img);
//            String outFile = "res00.png";

            // Save the visualized detection.
//        System.out.println("Writing "+ outFile);
//        Highgui.imwrite(outFile, img);
//        Highgui.
        }

    }
}

class MyFrame {

    private final JFrame frame;
    private final MyPanel panel;

    public MyFrame() {
        // JFrame which holds JPanel
        frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JPanel which is used for drawing image
        panel = new MyPanel();
        frame.getContentPane().add(panel);
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public void render(Mat image) {
        Image i = toBufferedImage(image);
        panel.setImage(i);
        panel.repaint();
        frame.pack();
    }

    public static Image toBufferedImage(Mat m){
        // Code from http://stackoverflow.com/questions/15670933/opencv-java-load-image-to-gui

        // Check if image is grayscale or color
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        // Transfer bytes from Mat to BufferedImage
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }
}

class MyPanel extends JPanel {

    private Image img;

    public MyPanel() {

    }

    public void setImage(Image img) {
        // Image which we will render later
        this.img = img;

        // Set JPanel size to image size
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}
