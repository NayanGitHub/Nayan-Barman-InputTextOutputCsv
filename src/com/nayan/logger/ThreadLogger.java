package com.nayan.logger;
/*
 *
 *   Copyright Utilibill  26/07/2010
 *
 */



import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * Class    : ThreadLogger
 * @author  : John Fairhall
 * Purpose  : map threads to loggers
 *
 */
public class ThreadLogger {

    // map of thread names to loggers
    private static Map<String, Logger> threadLoggerMap = new HashMap<String, Logger>();

    /**
     * set a logger for this thread.
     * @param logger
     */
    public synchronized static void setThreadLogger(Logger logger)
    {
        threadLoggerMap.put(Thread.currentThread().getName(), logger);
    }


    /**
     * @return get the logger matching this thread name
     */
    public static Logger getLogger()
    {
        Logger logger = threadLoggerMap.get(Thread.currentThread().getName());
        if (logger == null)
            return Logger.getRootLogger();

        return logger;
    }


    /**
     * get logger. If logging for the current thread, then return that logger
     * @param clazz
     * @return get the logger matching this thread name + class name
     */
    public static Logger getLogger(Class clazz)
    {
        if (clazz == null)
            return getLogger();

        return getLogger(clazz.getName());
    }

    
    /**
     * get logger. If logging for the current thread, then return that logger
     * @param className
     * @return get the logger matching this thread name + class name
     */
    public static Logger getLogger(String className)
    {
        Logger logger = threadLoggerMap.get(Thread.currentThread().getName());
        if (logger == null)
            logger = Logger.getLogger(className);
        else
            logger = Logger.getLogger(logger.getName() + "./" + className);
        
        if (logger == null)
            logger = Logger.getRootLogger();

        return logger;
    }
    
    public synchronized static void endThreadLogger()
    {
        Logger logger = threadLoggerMap.get(Thread.currentThread().getName());
        if (logger != null)
        {
           threadLoggerMap.remove(Thread.currentThread().getName());
           logger.removeAllAppenders();
        }
        
    }
}
