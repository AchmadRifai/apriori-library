package achmad.rifai.apriori.library.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadUtils {

	public static int blocking(ScheduledThreadPoolExecutor exec) {
		int i = 0;
		while (exec.getActiveCount() > 0) {
			i++;
			if (i == 10) 
				i = 0;
		}
		return i;
	}

	public static int onMax(ScheduledThreadPoolExecutor exec) {
		int i = 0;
		while (exec.getActiveCount() >= 20) {
			i++;
			if (i == 10) 
				i = 0;
		}
		return i;
	}

}
