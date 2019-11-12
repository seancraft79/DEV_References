
# Log4jHelper

### Log4jHelper
```
import org.apache.log4j.Logger;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class Log4jHelper {

	private final static LogConfigurator _logConfigurator = new LogConfigurator();

	/**
	 * Configure Log4j
	 * 
	 * @param fileName
	 *            Name of the log file
	 * @param maxBackupSize
	 *            Maximum number of backed up log files
	 * @param maxFileSize
	 *            Maximum size of log file until rolling
	 */
	public static void Configure(String fileName,
			int maxBackupSize, long maxFileSize) {

		_logConfigurator.setFileName(fileName);
		_logConfigurator.setMaxBackupSize(maxBackupSize);
		_logConfigurator.setMaxFileSize(maxFileSize);
		_logConfigurator.configure();

	}

	/**
	 * Get logging operations class
	 * 
	 * @param name
	 *            The name of the logger to retrieve.
	 * @return Logging operations class
	 */
	public static Logger getLogger(String name) {
		Logger logger = Logger.getLogger(name);
		return logger;
	}

}
```