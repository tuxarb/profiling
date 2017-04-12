![](https://cloud.githubusercontent.com/assets/15519803/24955260/57217880-1f8b-11e7-8425-1ef022f4c7ae.jpg)
## **Overview**
#### **The system provides the ability to test of any executable operating system program by runtime/capacity/speed.**
Runtime - the amount of time that the program was executing. (s)<br />
Capacity - the average amount of memory (RAM) that the program spent when executing. (KB)<br />
Speed - the average amount of memory that the program spent for 1 second of time when executing. (KB / s)<br />

### Supportable operating systems
* Windows XP and above
* Linux
* Mac OS X

### Modes
The system provided the next different modes:
* GUI mode
* Console mode (convenient for operating systems that doesn't support the graphical interface)
* Non-interactive console mode (convenient for servers)

### Opportunities
The profiling system provided the next opportunities:
* Test (one running of the program)
* Detailed test (multiple testing)
* Saving the result data in .txt file
* Saving the result data in a database
* Repeat of the testing
* Update of the .properties file [(see below)](#how-to-start)
* Display the graphics for |capacity-time| and |speed-time| (only for "gui" mode)
* Output all statistic information about execution of the program in .txt and .csv files (only for "console" and "non_interactive" modes)


### Logging
All log information is written in the log files by the next path: **<user.home>/.profiling_logs/...<br />**
Check that you have the permissions for reading/writing files by **<user.home>** path.<br/>
If you use "GUI" mode when starting the profiling system, all log information duplicates on the console so you may not see .log files.


## How to start
The system was developed on **Java 9** and requires its the installation. <br/>
Use the next steps:
* Download **Java 9** from https://jdk9.java.net/download/ if you haven't.
* Download profiling.jar from [here](https://github.com/tuxarb/profiling/blob/master/profiling.jar).
* Create **.properties file** on your computer and write down in this file the couples **key=value** using as example **example.properties** that you can see [here](https://github.com/tuxarb/profiling/blob/master/example.properties).
* Open console and run __profiling.jar__ using java commands:
    * "GUI" mode -> java -jar -Dmode=gui path/to/your/profiling.jar
    * "Non-interactive" mode -> java -jar -Dmode=non_interactive -Dpath=path/to/your/.properties_file path/to/your/profiling.jar
    * "Console" mode -> java -jar path/to/your/profiling.jar


## Additional information
If you want to get a more accurate result, you should use **detailed test** and specify in your .properties file the value for the key **number_tests** huge as possible to program can finish. <br />
Also for the serious purposes it is better to use **non_interactive** mode.
