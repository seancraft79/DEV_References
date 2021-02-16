package com.sysgen.eom.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ServiceUtil {

   public void checkServices(Context context) {
       ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

       List<ActivityManager.RunningServiceInfo> list = am.getRunningServices(100);

       if(list != null && list.size() > 0) {
           for(ActivityManager.RunningServiceInfo info : list) {

               String className=info.service.getClassName();

               String packageName=info.service.getPackageName();

           }
       }
   }

}
