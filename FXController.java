package LavaGCA;

import java.io.ByteArrayInputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class FXController {

    @FXML
    private Button start_btn;
    @FXML
    private ImageView currentFrame;

    private Pane rootElement;
    private Timer timer;
    private VideoCapture capture = new VideoCapture();

    @FXML
    protected void startCamera(Event event)
    {
            // check: the main class is accessible?
            if (this.rootElement != null)
            {
                    // get the ImageView object for showing the video stream
                    final ImageView frameView = currentFrame;
                    final BorderPane root = (BorderPane) this.rootElement;
                    // check if the capture stream is opened
                    if (!this.capture.isOpened())
                    {
                    		System.out.println("Starting Camera ...");
                            // start the video capture
                            this.capture.open(0);
                            // grab a frame every 33 ms (30 frames/sec)
                            TimerTask frameGrabber = new TimerTask() {
                                    @Override
                                    public void run()
                                    {
                                            javafx.scene.image.Image tmp = grabFrame();
                                            Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                            frameView.setImage(tmp);
                                                    }
                                            });

                                    }
                            };
                            this.timer = new Timer();
                            //set the timer scheduling, this allow you to perform frameGrabber every 33ms;
                            this.timer.schedule(frameGrabber, 0, 33);
                            this.start_btn.setText("Stop Camera");
                    }
                    else
                    {
                            this.start_btn.setText("Start Camera");
                            // stop the timer
                            if (this.timer != null)
                            {
                                    this.timer.cancel();
                                    this.timer = null;
                            }
                            // release the camera
                            this.capture.release();
                            // clear the image container
                            frameView.setImage(null);
                    }
            }
    }

    private Image grabFrame()
    {
            //init
            Image imageToShow = null;
            Mat frame = new Mat();
            // check if the capture is open
            if (this.capture.isOpened())
            {
                    try
                    {
                            // read the current frame
                            this.capture.read(frame);
                            // if the frame is not empty, process it
                            if (!frame.empty())
                            {
                                    // convert the image to gray scale
                                    // Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                                    // convert the Mat object (OpenCV) to Image (JavaFX)
                                    imageToShow = mat2Image(frame);
                            }
                    }
                    catch (Exception e)
                    {
                            // log the error
                            System.err.println("ERROR: " + e.getMessage());
                    }
            }
            return imageToShow;
    }

    private Image mat2Image(Mat frame)
    {
            // create a temporary buffer
            MatOfByte buffer = new MatOfByte();
            // encode the frame in the buffer
            // HighGui.imencode(".png", frame, buffer);
            Imgcodecs.imencode(".png", frame, buffer);
            // build and return an Image created from the image encoded in the buffer
            return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

    public void setRootElement(Pane root)
    {
            this.rootElement = root;
            
            for(Node aNode : root.getChildren() ) {
            	if (aNode instanceof ImageView) currentFrame = (ImageView) aNode;
            	if (aNode instanceof Button) start_btn = (Button) aNode;
            }
    }

}
