package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Main extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        launch(args);
    }

    private static void showError(Thread t, Throwable e) {

        LOG.info("***Handle exception***");
        if (Platform.isFxApplicationThread()) {
            showErrorDialog(e);
        } else {
            LOG.error("Ooops ... something went horribly wrong in " + t);
        }
    }

    private static void showErrorDialog(Throwable e) {
        StringWriter errorMsg = new StringWriter();
        e.printStackTrace(new PrintWriter(errorMsg));
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/error.fxml"));
        try {
            Parent root = loader.load();
            ((ErrorController) loader.getController()).setErrorText(e.getMessage());
            dialog.setScene(new Scene(root, 200, 75));
            dialog.show();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Thread.setDefaultUncaughtExceptionHandler(Main::showError);

        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("iTerminal");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> handleWindowClose(event));

    }

    @SneakyThrows
    private void handleWindowClose(Event event) {

        final Stage stage;
        String eventString;

        eventString = event.getEventType().toString();
        if ("WINDOW_CLOSE_REQUEST".equals(eventString)) {
            event.consume();
            stage = (Stage) event.getSource();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            if (scheduler.isStarted()) {
                scheduler.shutdown();
            }
            stage.close();
            Platform.exit();
            System.exit(0);
        }
    }

}
