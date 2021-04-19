~/Android/Sdk/platform-tools/adb shell dumpsys alarm > ~/Desktop/alarmdump.txt
grep -A 5 -B 5 "SavedTimerReceiver" ~/Desktop/alarmdump.txt
gedit ~/Desktop/alarmdump.txt