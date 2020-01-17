package ru.chuchalov;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

/**
 * Класс - обработчик события закрытия приложения. Обеспечивает корректную обработку сигала.
 * 
 * @author Andrei Chuchalov
 * @version 1.0
 */
public class PHMessageApplicationListener implements ApplicationListener<ApplicationFailedEvent> {
	
	private static final Logger log = LoggerFactory.getLogger(PHMessageApplicationListener.class);
	private static final String msg = "There is an error while running application!";

	@Override
	public void onApplicationEvent(ApplicationFailedEvent event) {
		if(event.getException()!=null) {
			log.info(msg);
			event.getApplicationContext().close();
			System.exit(-1);
		}
	}

}
