All Android Directory Path
=================

[![Twitter](https://img.shields.io/badge/Twitter-@LopezMikhael-blue.svg?style=flat)](http://twitter.com/lopezmikhael)

### **1) System directories** ###
:warning: *We can't write to these folers*

| Method | Result |
|:-----------|:------------|
| Environment.getDataDirectory() | /data |
| Environment.getDownloadCacheDirectory() | /cache | 
| Environment.getRootDirectory() | /system | 

### **2) External storage directories** ###
:warning: *Need WRITE_EXTERNAL_STORAGE Permission*

| Method | Result |
|:-----------|:------------|
| Environment.getExternalStorageDirectory() | /storage/sdcard0 | 
| Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS) | /storage/sdcard0/Alarms | 
| Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) | /storage/sdcard0/DCIM | 
| Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) | /storage/sdcard0/Download | 
| Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) | /storage/sdcard0/Movies | 
| Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) | /storage/sdcard0/Music | 
| Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS) | /storage/sdcard0/Notifications | 
| Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) | /storage/sdcard0/Pictures | 
| Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS) | /storage/sdcard0/Podcasts | 
| Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES) | /storage/sdcard0/Ringtones | 

### **3) Application directories** ###

| Method | Result |
|:-----------|:------------|
| getCacheDir() | /data/data/*package*/cache | 
| getFilesDir() | /data/data/*package*/files | 
| getFilesDir().getParent() | /data/data/*package* | 

### **4) Application External storage directories** ###

| Method | Result |
|:-----------|:------------|
| getExternalCacheDir() | /storage/sdcard0/Android/data/*package*/cache | 
| getExternalFilesDir(null) | /storage/sdcard0/Android/data/*package*/files |
| getExternalFilesDir(Environment.DIRECTORY_ALARMS) | /storage/sdcard0/Android/data/*package*/files/Alarms | 
| getExternalFilesDir(Environment.DIRECTORY_DCIM) | /storage/sdcard0/Android/data/*package*/files/DCIM | 
| getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) | /storage/sdcard0/Android/data/*package*/files/Download | 
| getExternalFilesDir(Environment.DIRECTORY_MOVIES) | /storage/sdcard0/Android/data/*package*/files/Movies | 
| getExternalFilesDir(Environment.DIRECTORY_MUSIC) | /storage/sdcard0/Android/data/*package*/files/Music | 
| getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS) | /storage/sdcard0/Android/data/*package*/files/Notifications | 
| getExternalFilesDir(Environment.DIRECTORY_PICTURES) | /storage/sdcard0/Android/data/*package*/files/Pictures | 
| getExternalFilesDir(Environment.DIRECTORY_PODCASTS) | /storage/sdcard0/Android/data/*package*/files/Podcasts | 
| getExternalFilesDir(Environment.DIRECTORY_RINGTONES) | /storage/sdcard0/Android/data/*package*/files/Ringtones | 

***

:books: Best Android Gists
=================

You can see other best Android Gists or offer your just here [https://github.com/lopspower/BestAndroidGists](https://github.com/lopspower/BestAndroidGists) :+1:.