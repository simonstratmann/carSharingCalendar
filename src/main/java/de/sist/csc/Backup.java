// (C) 2022 PPI AG
package de.sist.csc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Component
public class Backup {

    @Autowired
    private EntityManager entityManager;

    @Scheduled(fixedRate = 24, initialDelay = 0, timeUnit = TimeUnit.HOURS)
    @Transactional
    public void backup() {
        final File file = new File("backup/" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + ".sql");
        if (file.exists()) {
            return;
        }
        entityManager.createNativeQuery("BACKUP TO '" + file.getAbsolutePath().replace("\\", "/") + "';").executeUpdate();
    }
}
