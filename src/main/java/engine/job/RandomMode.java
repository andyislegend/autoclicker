package engine.job;

import engine.entity.Click;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings({"Duplicates"})
public class RandomMode implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(RandomMode.class);
    private static final int TO_MILLISECONDS = 60000;

    @Getter
    @Setter
    List<Click> clickList;

    @Getter
    @Setter
    int repeatTime;

    private Random rndGenerator = new Random();

    public RandomMode(List<Click> clicks, int repeatTime) {
        this.clickList = clicks;
        this.repeatTime = repeatTime;
    }

    @SneakyThrows
    @Override
    public void run() {

        java.awt.Robot randomRobot = new Robot();

        for (int i = 0; i < repeatTime; i++) {
            pressRNDKey(randomRobot);
            delayRobot();
        }
    }

    private void pressRNDKey(Robot robot) {

        for (Click click : randomClickForOneOperation(clickList)) {

            //PRESS ONLY 1 BUTTON
            if (click.getButtonTwo() == null && click.getButtonThree() == null) {

                if (click.getButtonOne().isMouseEvent()) {
                    robot.mousePress(click.getButtonOne().getVk());
                    robot.mouseRelease(click.getButtonOne().getVk());
                } else {
                    robot.keyPress(click.getButtonOne().getVk());
                    robot.keyRelease(click.getButtonOne().getVk());
                }

                LOG.info("Clicked " + click.getButtonOne().name() + " \n *****");

                //PRESS 2 BUTTONS
            } else if (click.getButtonTwo() != null && click.getButtonThree() == null) {

                if (click.getButtonTwo().isMouseEvent()) {
                    robot.keyPress(click.getButtonOne().getVk());
                    robot.mousePress(click.getButtonTwo().getVk());

                    robot.keyRelease(click.getButtonOne().getVk());
                    robot.mouseRelease(click.getButtonTwo().getVk());
                } else {
                    robot.keyPress(click.getButtonOne().getVk());
                    robot.keyPress(click.getButtonTwo().getVk());

                    robot.keyRelease(click.getButtonOne().getVk());
                    robot.keyRelease(click.getButtonTwo().getVk());
                }

                LOG.info("Clicked " + click.getButtonOne().name() + " and " + click.getButtonTwo().name() + " \n *****");

                // PRESS ALL BUTTONS
            } else {

                robot.keyPress(click.getButtonOne().getVk());
                robot.keyPress(click.getButtonTwo().getVk());
                robot.keyPress(click.getButtonThree().getVk());

                robot.keyRelease(click.getButtonOne().getVk());
                robot.keyRelease(click.getButtonTwo().getVk());
                robot.mousePress(click.getButtonThree().getVk());

                LOG.info("Clicked " + click.getButtonOne().name() + " and " + click.getButtonTwo().name() +
                        " and " + click.getButtonThree().name() + " \n *****");
            }

        }

    }

    private List<Click> randomClickForOneOperation(List<Click> producer) {

        List<Click> oneOperationList = new ArrayList<>();
        int clicksCombo = rndGenerator.nextInt(3) + 1;
        int amountOfClicks = clickList.size() - 1;
        for (int i = 0; i < clicksCombo; i++) {
            oneOperationList.add(producer.get(rndGenerator.nextInt(amountOfClicks)));
        }

        return oneOperationList;
    }

    @SneakyThrows
    private synchronized void delayRobot() {
        Thread.sleep((rndGenerator.nextInt(2) + 1) * TO_MILLISECONDS);
    }
}
