package engine.job;

import engine.entity.Click;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.Controller;

public class CustomJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(CustomJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        java.awt.Robot robot = (java.awt.Robot) context.getJobDetail().getJobDataMap().get("robot");
        pressKey(robot);
    }

    @SneakyThrows
    private void pressKey(java.awt.Robot robot) {

        for (Click click : Controller.COMMANDS) {

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

}
