package com.example.mypedometer;

import android.app.Service;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.content.Context;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import java.util.ArrayList;
import android.os.SystemClock;
import java.util.Collections;
import java.util.Comparator;
import android.os.IBinder;
import android.content.Intent;
import java.util.concurrent.TimeUnit;

public class StepsTrackerService extends Service {
    private SensorManager mSensorManager;
    private Sensor mStepDetectorSensor;
    private Sensor mAccelerometerSensor;
    private AccelerometerListener mAccelerometerListener;
    private StepDetectorListener mStepDetectorListener;
    StepsTrackerDBHelper mStepsTrackerDBHelper;
    private static final int WALKINGPEAK = 18;
    private static final int JOGGINGPEAK = 25;
    private static final int RUNNINGPEAK = 32;
    private static final int RUNNING = 3;
    private static final int JOGGING = 2;
    private static final int WALKING = 1;

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScheduledExecutorService.shutdown();
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            mStepDetectorListener = new StepDetectorListener();
            mSensorManager.registerListener(mStepDetectorListener, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
            System.out.println("######### the phone has step detector");
        }
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        mStepsTrackerDBHelper = new StepsTrackerDBHelper(this);

        // Step dector somehow does not work, so we bypass it and always run accelerometer!
        mAccelerometerListener = new AccelerometerListener();
        mSensorManager.registerListener(mAccelerometerListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        isAccelerometerRegistered = true;



        System.out.println("################## service started ########################");
    }

    ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(2);
    private ScheduledFuture mScheduledUnregisterAccelerometerTask;
    private ScheduledFuture mScheduledProcessDataTask;
    private UnregisterAccelerometerTask mUnregisterAccelerometerTask;
    private ProcessDataTask mProcessDataTask;
    private boolean isScheduleUnregistered = false;
    private boolean isAccelerometerRegistered = false;

    class StepDetectorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            System.out.println("################## step detected ########################");

            if (!isAccelerometerRegistered && mAccelerometerSensor != null) {
                mAccelerometerListener = new AccelerometerListener();
                mSensorManager.registerListener(mAccelerometerListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
                isAccelerometerRegistered = true;
            }
            if (isScheduleUnregistered) {
                mScheduledUnregisterAccelerometerTask.cancel(true);
            }
            mUnregisterAccelerometerTask = new UnregisterAccelerometerTask();
            mScheduledUnregisterAccelerometerTask =
                    mScheduledExecutorService.schedule(mUnregisterAccelerometerTask, 20000, TimeUnit.MILLISECONDS);
            isScheduleUnregistered = true;

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    class UnregisterAccelerometerTask implements Runnable {
        @Override
        public void run() {
            isAccelerometerRegistered = false;
            mSensorManager.unregisterListener(mAccelerometerListener);
            isScheduleUnregistered = false;
            mScheduledProcessDataTask.cancel(false);
        }
    }

    class AccelerometerData {
        public double value;
        public float x;
        public float y;
        public float z;
        public long time;
        public boolean isRealPeak = true;
    }

    private long timeOffsetValue;
    ArrayList<AccelerometerData> mAccelerometerDataList = new ArrayList<AccelerometerData>();
    ArrayList<AccelerometerData> mRawDataList = new ArrayList<AccelerometerData>();
    ArrayList<AccelerometerData> mAboveThresholdValuesList = new ArrayList<AccelerometerData>();
    ArrayList<AccelerometerData> mHighestPeakList = new ArrayList<AccelerometerData>();

    class AccelerometerListener implements SensorEventListener {
        public AccelerometerListener() {
            mProcessDataTask = new ProcessDataTask();
            mScheduledProcessDataTask =
                    mScheduledExecutorService.scheduleWithFixedDelay(mProcessDataTask, 10000, 10000, TimeUnit.MILLISECONDS);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // System.out.println("################ accelerometer sensor changed");
            AccelerometerData mAccelerometerData = new AccelerometerData();
            mAccelerometerData.x = event.values[0];
            mAccelerometerData.y = event.values[1];
            mAccelerometerData.z = event.values[2];
            mAccelerometerData.time = event.timestamp;
            mAccelerometerDataList.add(mAccelerometerData);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    class ProcessDataTask implements Runnable {
        @Override
        public void run() {
            System.out.println("################## Entering ProcessDataTask ****************");
            //Copy accelerometer data from main sensor array in separate array for processing
            mRawDataList.addAll(mAccelerometerDataList);
            mAccelerometerDataList.clear();
            //Calculating the magnitude (Square root of sum of squares of x, y, z) & converting time from
            // nano seconds from boot time to epoc time
            timeOffsetValue = System.currentTimeMillis() - SystemClock.elapsedRealtime();
            int dataSize = mRawDataList.size();
            for (int i = 0; i < dataSize; i++) {
                mRawDataList.get(i).value = Math.sqrt(Math.pow(mRawDataList.get(i).x, 2) +
                        Math.pow(mRawDataList.get(i).y, 2) + Math.pow(mRawDataList.get(i).z, 2));
                mRawDataList.get(i).time = (mRawDataList.get(i).time / 1000000L) + timeOffsetValue;
            }
            //Calculating the High Peaks
            findHighPeaks();
            //Remove high peaks close to each other which are within range of 0.4 seconds
            removeClosePeaks();
            //Find the type of step (Running, jogging, walking) & store in Database
            findStepTypeAndStoreInDB();
            mRawDataList.clear();
            mAboveThresholdValuesList.clear();
            mHighestPeakList.clear();
        }

        public void findHighPeaks() {
            System.out.println("################## Entering findHighPeaks ****************");
            //Calculating the High Peaks
            boolean isAboveMeanLastValueTrue = false;
            int dataSize = mRawDataList.size();
            for (int i = 0; i < dataSize; i++) {
                if (mRawDataList.get(i).value > WALKINGPEAK) {
                    // add all continuous data points that are bigger than WALKINGPEAK to the array
                    // One of these points include both the true peak and the false peaks, and many other points that are not peaks
                    mAboveThresholdValuesList.add(mRawDataList.get(i));
                    isAboveMeanLastValueTrue = false;
                } else {
                    // Once we see a point that is below the threshold, we are falling out of the peak region
                    // Now time to find the peaks
                    if (!isAboveMeanLastValueTrue && mAboveThresholdValuesList.size() > 0) {
                        // Sort the temporary data array. The last one in the array has the biggest value, which is the peak
                        // which could be true or false peak
                        Collections.sort(mAboveThresholdValuesList, new DataSorter());
                        mHighestPeakList.add(mAboveThresholdValuesList.get(mAboveThresholdValuesList.size() - 1));
                        mAboveThresholdValuesList.clear();
                    }
                    isAboveMeanLastValueTrue = true;
                }
            }
        }

        public void removeClosePeaks() {
            for (int i = 0; i < mHighestPeakList.size() - 1; i++) {

                if (mHighestPeakList.get(i).isRealPeak) {
                    if (mHighestPeakList.get(i + 1).time - mHighestPeakList.get(i).time < 400) {
                        if (mHighestPeakList.get(i + 1).value > mHighestPeakList.get(i).value) {
                            mHighestPeakList.get(i).isRealPeak = false;
                        } else {
                            mHighestPeakList.get(i + 1).isRealPeak = false;
                        }
                    }
                }
            }
        }

        public void findStepTypeAndStoreInDB() {
            int size = mHighestPeakList.size();
            System.out.println("number of highest peaks: "+size);
            for (int i = 0; i < size; i++) {
                if (mHighestPeakList.get(i).isRealPeak) {
                    if (mHighestPeakList.get(i).value > RUNNINGPEAK) {
                        mStepsTrackerDBHelper.createStepsEntry(mHighestPeakList.get(i).time, RUNNING);
                    } else {
                        if (mHighestPeakList.get(i).value > JOGGINGPEAK) {
                            mStepsTrackerDBHelper.createStepsEntry(mHighestPeakList.get(i).time, JOGGING);
                        } else {
                            mStepsTrackerDBHelper.createStepsEntry(mHighestPeakList.get(i).time, WALKING);
                        }
                    }
                }
            }
        }

        public class DataSorter implements Comparator<AccelerometerData> {
            public int compare(AccelerometerData obj1, AccelerometerData obj2) {
                int returnVal = 0;
                if (obj1.value < obj2.value) {
                    returnVal = -1;
                } else if (obj1.value > obj2.value) {
                    returnVal = 1;
                }
                return returnVal;
            }
        }


    }
}

