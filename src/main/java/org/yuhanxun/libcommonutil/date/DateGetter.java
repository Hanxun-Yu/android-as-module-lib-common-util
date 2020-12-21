package org.yuhanxun.libcommonutil.date;

import android.content.Context;
import android.os.Handler;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dbstar-mac on 16/8/8.
 */
public class DateGetter {
    private ExecutorService executor;
    private TimeRunnable runnable;
    private DateChangeListener listener;
    private Handler mHandler;
    private final String[] WEEK_ARR_CH = new String[]{"日","一","二","三","四","五","六"};

    public interface DateChangeListener {
        void onDateChange(TimeBean bean);
    }

    Context context;

    public DateGetter(Context context) {
        this.context = context;
        mHandler = new Handler(context.getMainLooper());
    }

    public void setOnDateChangeListener(DateChangeListener listener) {
        this.listener = listener;
        start();
    }

    public void start() {
        if (runnable != null) {
            runnable.setStop(true);
        }
        if (executor != null) {
            executor.shutdown();
        }
        executor = Executors.newSingleThreadExecutor();
        executor.execute(runnable = new TimeRunnable());
    }

    public void shutdown() {
        if (runnable != null) {
            runnable.setStop(true);
            runnable = null;
        }
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
    }


    class TimeRunnable implements Runnable {
        public void setStop(boolean stop) {
            this.stop = stop;
        }

        boolean stop = false;

        @Override
        public void run() {
            while (!stop) {
                Calendar c = Calendar.getInstance();
                final TimeBean tb = new TimeBean(c.get(Calendar.DAY_OF_MONTH),
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        c.get(Calendar.MONTH) + 1,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.DAY_OF_WEEK));


                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onDateChange(tb);
                        }
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class TimeBean {
        public TimeBean(String day, String hour, String minute, String month, String year, String week) {
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.month = month;
            this.year = year;
            this.week = week;
        }

        public TimeBean(int day, int hour, int minute, int month, int year, int week) {
            this.day = String.valueOf(day);
            this.hour = String.valueOf(hour);
            this.minute = String.valueOf(minute);
            this.month = String.valueOf(month);
            this.year = String.valueOf(year);
            this.week = WEEK_ARR_CH[week-1];

            if (this.day.length() == 1) {
                this.day = "0" + this.day;
            }

            if (this.minute.length() == 1) {
                this.minute = "0" + this.minute;
            }
            if (this.hour.length() == 1) {
                this.hour = "0" + this.hour;
            }


        }

        public String hour, minute, year, month, day, week;
    }
}
