package com.cxytiandi.jdbc.keygen;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的主键生成器.
 * 
 * <p>
 * 长度为64bit,从高位到低位依次为
 * </p>
 * 
 * <pre>
 * 1bit   符号位 
 * 41bits 时间偏移量从2016年11月1日零点到现在的毫秒数
 * 10bits 工作进程Id
 * 12bits 同一个毫秒内的自增量
 * </pre>
 * 
 * <p>
 * 工作进程Id获取优先级: 系统变量{@code sharding-jdbc.default.key.generator.worker.id} 大于 环境变量{@code SHARDING_JDBC_DEFAULT_KEY_GENERATOR_WORKER_ID}
 * ,另外可以调用@{@code DefaultKeyGenerator.setWorkerId}进行设置
 * </p>
 * 
 */
public final class DefaultKeyGenerator implements KeyGenerator {
	private Logger logger = LoggerFactory.getLogger(DefaultKeyGenerator.class);
	
    public static final long EPOCH;
    
    public static final String WORKER_ID_PROPERTY_KEY = "sharding-jdbc.default.key.generator.worker.id";
    
    public static final String WORKER_ID_ENV_KEY = "SHARDING_JDBC_DEFAULT_KEY_GENERATOR_WORKER_ID";
    
    private static final long SEQUENCE_BITS = 12L;
    
    private static final long WORKER_ID_BITS = 10L;
    
    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;
    
    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;
    
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;
    
    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;
    
    
    private static TimeService timeService = new TimeService();
    
    private static long workerId;
    
    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.NOVEMBER, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        EPOCH = calendar.getTimeInMillis();
        initWorkerId();
    }
    
    private long sequence;
    
    private long lastTime;
    
    public static void initWorkerId() {
        String workerId = System.getProperty(WORKER_ID_PROPERTY_KEY);
        if (!Strings.isNullOrEmpty(workerId)) {
            setWorkerId(Long.valueOf(workerId));
            return;
        }
        workerId = System.getenv(WORKER_ID_ENV_KEY);
        if (Strings.isNullOrEmpty(workerId)) {
            return;
        }
        setWorkerId(Long.valueOf(workerId));
    }
    
    /**
     * 设置工作进程Id.
     * 
     * @param workerId 工作进程Id
     */
    public static void setWorkerId(final long workerId) {
        Preconditions.checkArgument(workerId >= 0L && workerId < WORKER_ID_MAX_VALUE);
        DefaultKeyGenerator.workerId = workerId;
    }
    
    /**
     * 生成Id.
     * 
     * @return 返回@{@link Long}类型的Id
     */
    @Override
    public synchronized Number generateKey() {
        long currentMillis = timeService.getCurrentMillis();
        Preconditions.checkState(lastTime <= currentMillis, "Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds", lastTime, currentMillis);
        if (lastTime == currentMillis) {
            if (0L == (sequence = ++sequence & SEQUENCE_MASK)) {
                currentMillis = waitUntilNextTime(currentMillis);
            }
        } else {
            sequence = 0;
        }
        lastTime = currentMillis;
        if (logger.isDebugEnabled()) {
        	logger.debug("{}-{}-{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(lastTime)), workerId, sequence);
        }
        return ((currentMillis - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS) | (workerId << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
    }
    
    private long waitUntilNextTime(final long lastTime) {
        long time = timeService.getCurrentMillis();
        while (time <= lastTime) {
            time = timeService.getCurrentMillis();
        }
        return time;
    }
}