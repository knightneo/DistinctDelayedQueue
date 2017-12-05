package message;

import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by knightneo on 2017/12/5.
 */
@Data
public class DelayedMessage<T> implements Delayed {

    /**
     * 到期时间
     * 单位:纳秒
     */
    private final long time;

    private T message;

    private int intervalSecond;

    public DelayedMessage(T message, int timeout, int intervalSecond) {
        this.message = message;
        long nanoTimeout = TimeUnit.NANOSECONDS.convert(timeout, TimeUnit.SECONDS);
        this.time = TimeUnit.NANOSECONDS.convert(nanoTimeout, TimeUnit.NANOSECONDS) + System.nanoTime();
        this.intervalSecond = intervalSecond;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(time - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o == null || ! (o  instanceof DelayedMessage)) {
            return 1;
        }
        if (o == this) {
            return 0;
        }
        if (o instanceof DelayedMessage) {
            DelayedMessage m = (DelayedMessage) o;
            long diff = time - m.time;
            if (diff < 0) {
                return -1;
            } else if (diff > 0) {
                return 1;
            }
        }
        return 0;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof DelayedMessage)) {
            return false;
        }
        DelayedMessage m = (DelayedMessage) o;
        if (m.getMessage().equals(getMessage())) {
            return isTimeDuplicate(m);
        }
        return false;
    }

    public boolean isTimeDuplicate(DelayedMessage m) {
        Long deltaNano = time - m.time;
        Long intervalNano = TimeUnit.NANOSECONDS.convert(intervalSecond, TimeUnit.SECONDS);
        if (deltaNano < intervalNano) {
            return true;
        }
        return false;
    }
}
