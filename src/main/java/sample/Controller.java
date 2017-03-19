package sample;

import engine.entity.Click;
import engine.enums.KeyEnum;
import engine.job.RandomMode;
import engine.job.Robot;
import engine.util.ClickerBuilderUtil;
import engine.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Controller {

    public static final List<Click> COMMANDS = new ArrayList<>();
    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);
    private static final int RANDOM_CLICKS_LIST_SIZE = 5;
    public static int INTERVAL = 0;
    private Thread rndThread;

    @FXML
    private Button addClick;
    @FXML
    private Button remove;
    @FXML
    private Button clear;
    @FXML
    private Button start;
    @FXML
    private Button stop;
    @FXML
    private Button startRandomMode;
    @FXML
    private ChoiceBox<String> choiceBoxOne;
    @FXML
    private ChoiceBox<String> choiceBoxTwo;
    @FXML
    private ChoiceBox<String> choiceBoxThree;
    @FXML
    private TableView clicksTable;
    @FXML
    private TableColumn columnOne;
    @FXML
    private TableColumn columnTwo;
    @FXML
    private TableColumn columnThree;
    @FXML
    private TextField delayField;
    @FXML
    private TextField repeatField;

    private ObservableList<Click> clicks = FXCollections.observableArrayList();

    @FXML
    protected void initialize() throws Exception {

        stop.setDisable(true);
        addClick.setOnAction(this::setAddClick);
        clear.setOnAction(this::setClear);
        remove.setOnAction(this::setRemove);
        choiceBoxOne.setItems(FXCollections.observableArrayList(getKeysNames()));
        choiceBoxTwo.setItems(FXCollections.observableArrayList(getKeysNames()));
        choiceBoxThree.setItems(FXCollections.observableArrayList(getKeysNames()));
        start.setOnAction(this::setStart);
        stop.setOnAction(this::setStop);
        startRandomMode.setOnAction(this::setStartRandomMode);
    }

    private void setClear(ActionEvent event) {
        choiceBoxOne.setValue(null);
        choiceBoxTwo.setValue(null);
        choiceBoxThree.setValue(null);
    }

    private void setRemove(ActionEvent event) {
        if (clicksTable.getSelectionModel().isEmpty()) {
            throw new RuntimeException(" Please, select button \n" +
                    " from table before removing ");
        }
        clicksTable.setEditable(true);
        int selectedIndex = clicksTable.getSelectionModel().getSelectedIndex();
        clicksTable.getItems().remove(selectedIndex);

        COMMANDS.remove(selectedIndex);

    }

    private void setStart(ActionEvent event) {

        Integer repeatTime;

        if (COMMANDS.isEmpty()) {
            throw new RuntimeException(" Add some clicks first ;) ");
        }

        if (delayField.getCharacters() == null) {
            throw new RuntimeException(" Please, enter interval between clicks ");
        }

        String delay = String.valueOf(delayField.getCharacters());
        String repeat = !StringUtils.isEmpty(String.valueOf(repeatField.getCharacters())) ? String.valueOf(repeatField.getCharacters()) : "0";

        try {
            INTERVAL = Integer.parseInt(delay);
            repeatTime = Integer.parseInt(repeat);
        } catch (Exception e) {
            delayField.clear();
            repeatField.clear();
            throw new RuntimeException(" Interval: \n only numbers allowed ");
        }

        stop.setDisable(false);
        start.setDisable(true);
        addClick.setDisable(true);
        clear.setDisable(true);
        remove.setDisable(true);

        Robot.startJobSession(repeatTime);
    }

    private void setStop(ActionEvent event) {

        Robot.stopJobSession();
        stopRndThread();

        start.setDisable(false);
        startRandomMode.setDisable(false);
        addClick.setDisable(false);
        clear.setDisable(false);
        remove.setDisable(false);
        stop.setDisable(true);
    }

    /**
     * Add click to <code>clicksTable</code> TableView
     *
     * @param event - JavaFX event on click button <code>addClick</code>
     */
    private void setAddClick(ActionEvent event) {

        if (choiceBoxOne.getValue() == null & choiceBoxTwo.getValue() == null & choiceBoxThree.getValue() == null) {
            throw new RuntimeException(" You must choose at least \n " +
                    "one button to perform click! ");
        }

        String firstButton = "";
        String secondButton = "";
        String thirdButton = "";

        /*
        *  Verify whether choiceBoxOne is present, and if its NULL but choiceBoxTwo is set - set data
        *  from choiceBoxTwo into <code> firstButton <code>.
        *  If choiceBoxTwo is also NULL, set data from choiceBoxThree into <code> firstButton <code>.
        * */
        if (choiceBoxOne.getValue() != null) {
            firstButton = choiceBoxOne.getValue();
        } else if (choiceBoxOne.getValue() == null && choiceBoxTwo.getValue() != null) {
            firstButton = choiceBoxTwo.getValue();
        } else if (choiceBoxOne.getValue() == null & choiceBoxTwo.getValue() == null) {
            firstButton = choiceBoxThree.getValue();
        }

        if (choiceBoxTwo.getValue() != null && choiceBoxOne.getValue() != null) {
            secondButton = choiceBoxTwo.getValue();
        } else if (choiceBoxTwo.getValue() == null && choiceBoxOne.getValue() != null
                && choiceBoxThree.getValue() != null) {
            secondButton = choiceBoxThree.getValue();
        } else if (choiceBoxOne.getValue() == null && choiceBoxTwo.getValue() != null
                && choiceBoxThree.getValue() != null) {
            firstButton = choiceBoxTwo.getValue();
            secondButton = choiceBoxThree.getValue();
        }

        if (choiceBoxOne.getValue() != null && choiceBoxTwo.getValue() != null
                && choiceBoxThree.getValue() != null) {
            thirdButton = choiceBoxThree.getValue();
        }

        Click click = ClickerBuilderUtil.createButtonClick(firstButton, secondButton, thirdButton);

        /*
        * Add click to ObservableList for TableView <code> clicksTable <code>
        * */
        clicks.add(click);

        /*
         * Add click to final list of COMMANDS to use it in worker or thread
         * */
        COMMANDS.add(click);

        clicksTable.setEditable(true);
        columnOne.setCellValueFactory(new PropertyValueFactory<KeyEnum, String>("buttonOne"));
        columnTwo.setCellValueFactory(new PropertyValueFactory<KeyEnum, String>("buttonTwo"));
        columnThree.setCellValueFactory(new PropertyValueFactory<KeyEnum, String>("buttonThree"));
        clicksTable.setItems(clicks);
    }

    private void setStartRandomMode(ActionEvent event) {

        Integer repeatTime;

        if (repeatField.getCharacters() == null) {
            throw new RuntimeException(" Please, enter repeat times!");
        }

        if (COMMANDS.size() < RANDOM_CLICKS_LIST_SIZE) {
            throw new RuntimeException(" To start RND MODE you need \n" +
                    "at least 5 clicks!");
        }

        String repeat = !StringUtils.isEmpty(String.valueOf(repeatField.getCharacters())) ? String.valueOf(repeatField.getCharacters()) : "0";
        try {
            repeatTime = Integer.parseInt(repeat);
        } catch (Exception e) {
            delayField.clear();
            repeatField.clear();
            throw new RuntimeException(" Repeat times: \n only numbers allowed ");
        }

        stop.setDisable(false);
        start.setDisable(true);
        startRandomMode.setDisable(true);
        addClick.setDisable(true);
        clear.setDisable(true);
        remove.setDisable(true);

        RandomMode rndMode = new RandomMode(COMMANDS, repeatTime);
        rndThread = new Thread(rndMode);
        rndThread.start();
    }

    private List<String> getKeysNames() {
        List<String> list = new ArrayList<>();
        KeyEnum[] values = KeyEnum.values();

        for (int i = 0; i < values.length - 1; i++) {
            list.add(values[i].name());
        }
        Collections.sort(list, Comparator.naturalOrder());
        return list;
    }

    private void stopRndThread() {

     try {
         rndThread.interrupt();
     } catch (Exception e) {
            LOG.info("Random Mode Thread stopped");
     }
    }

}
