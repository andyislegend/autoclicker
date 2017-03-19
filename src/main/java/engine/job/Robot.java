package engine.job;

import lombok.SneakyThrows;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import sample.Controller;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Robot {

    private static JobDataMap jobDataMap = new JobDataMap();

    @SneakyThrows
    public static void startJobSession(int repeat) {

        java.awt.Robot robot = new java.awt.Robot();
        jobDataMap.put("robot", robot);
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail pressButtonsJob = newJob(CustomJob.class).usingJobData(jobDataMap).build();

        Trigger trigger;
        if (repeat == 0) {
            trigger = newTrigger().startNow().
                    withSchedule(simpleSchedule().withIntervalInSeconds(Controller.INTERVAL).repeatForever()).build();
        } else {
            trigger = newTrigger().startNow().
                    withSchedule(simpleSchedule().withIntervalInSeconds(Controller.INTERVAL).withRepeatCount(repeat)).build();
        }

        scheduler.scheduleJob(pressButtonsJob, trigger);
        scheduler.start();
    }

    @SneakyThrows
    public static void stopJobSession() {

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        if (scheduler.isStarted()) {
            scheduler.shutdown();
        }

    }


}
