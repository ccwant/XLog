package com.example.xlogdemo;
import java.util.LinkedList;  
import java.util.List;  
  
import java.util.Stack;

import android.app.Activity;  

public class ActivityManager {  
  
    private Stack<Activity> activityStack = new Stack<Activity>();  
    private static ActivityManager instance;  
  
    private ActivityManager() {  
    }  
  
    public static ActivityManager getInstance() {  
        if (null == instance) {  
            instance = new ActivityManager();  
        }  
        return instance;  
    }  
  
    public void addActivity(Activity activity) {  
    	activityStack.push(activity);  
    }  
  
    public void exit() {  
        for (Activity activity : activityStack) {  
            activity.finish();  
        }  
    }  
    public void removeActivity(Activity activity)
    {
   
    	if (activity != null) {
            activityStack.remove(activity);
        }
    }
    public Activity getCurrentActivity() {
        return activityStack.lastElement();
    }
    public void finishCurrentActivity() {
        Activity activity = activityStack.pop();
        activity.finish();
    }
} 