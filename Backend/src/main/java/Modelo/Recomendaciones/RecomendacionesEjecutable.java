package Modelo.Recomendaciones;

import Repositorios.RepoOrganizaciones;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.CronScheduleBuilder.cronSchedule;

public class RecomendacionesEjecutable {

    static RepoOrganizaciones repoOrganizaciones = RepoOrganizaciones.instance;

    public static void main(String[] args) throws SchedulerException {
        JobDetail job = newJob(RecomendacionCalendarizada.class)
            .withIdentity("recomendacion-calendarizada")
            .build();

        Trigger trigger = newTrigger().withIdentity("trigger")
                //.withSchedule(cronSchedule("0/10 * * * * ?"))
            .withSchedule(cronSchedule("0 0 12 ? * MON *")) //todos los lunes a las 00 hs
            .build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        scheduler.scheduleJob(job,trigger);
    }

    public static class RecomendacionCalendarizada implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext) {
            System.out.println("recomende");
            new Recomendador(repoOrganizaciones).execute();
        }
    }
}
