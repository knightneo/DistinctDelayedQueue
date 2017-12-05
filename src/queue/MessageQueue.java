package queue;

import message.DelayedMessage;

import java.util.Iterator;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by knightneo on 2017/12/5.
 */
public class MessageQueue<T extends Delayed> extends DelayQueue<T> {

    private final ReentrantLock putLock = new ReentrantLock();
    private final ReentrantLock lock = new ReentrantLock();

    public void put(T e) {
        putLock.lock();
        try {
            if (!contains(e)) {
                super.put(e);
                if (e instanceof DelayedMessage) {
                    DelayedMessage m = (DelayedMessage) e;
                }
            } else {
                if (e instanceof DelayedMessage) {
                    DelayedMessage m = (DelayedMessage) e;
                }
            }
        } finally {
            putLock.unlock();
        }
    }

    public boolean contains(Object e) {
        if (e == null) {
            return false;
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            for (Iterator<T> it = iterator(); it.hasNext(); ) {
                if (e.equals(it.next())) {
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}
