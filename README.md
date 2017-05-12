![](https://cloud.githubusercontent.com/assets/15519803/24955260/57217880-1f8b-11e7-8425-1ef022f4c7ae.jpg)
## **Overview**
#### **The system provides the ability to test any executable operating system program by runtime/capacity/speed.**
*Runtime - the period during which the computer program is executing. (s)*<br />
*Capacity - the average amount of memory (RAM) that is spent by executable program. (KB)*<br />
*Speed - the average amount of memory that the program spend per 1 second during executing. (KB / s)*<br />

### Supportable operating systems
* Windows XP and above
* Linux
* Mac OS X

### Modes
This system provides such different list of modes as:
* GUI mode
* Console mode (it is convenient for operating systems that doesn't support the graphical interface)
* Non-interactive console mode (it is convenient for a servers)

### Opportunities
The profiling system provides the next list of opportunities:
* Test (one running of the program)
* Detailed test (multiple testing)
* Saving the result data in .txt file
* Saving the result data in a database
* Repeat of the testing
* Update of the .properties file [(see below)](#how-to-start)
* Display the graphics for |capacity-time| and |speed-time| (only for "gui" mode)
* Output all statistic information about the program execution in .txt and .csv files (only for "console" and "non_interactive" modes)


### Logging
All log information is written in the log files by the next path: **<user.home>/.profiling_logs/...<br />**
Check that you have the permissions for reading/writing files by **<user.home>** path.<br/>
If you use "GUI" mode when starting the profiling system, all log information duplicates on the console so you may not see .log files.


## How to start
The system was developed on **Java 9** and requires its the installation. <br/>
Use the next steps:
* Download **Java 9** from http://jdk.java.net/9/ if you haven't.
* Download **profiling.jar** from [here](https://github.com/tuxarb/profiling/blob/master/profiling.jar).
* Create **.properties file** on your computer and write down in this file the couples **key=value** using as example **example.properties** that you can see [here](https://github.com/tuxarb/profiling/blob/master/example.properties).
* Open console and run __profiling.jar__ using java commands:
    * "GUI" mode -> **java -jar -Dmode=gui path/to/your/profiling.jar**
    * "Non-interactive" mode -> **java -jar -Dmode=non_interactive -Dpath=path/to/your/.properties_file path/to/your/profiling.jar**
    * "Console" mode -> **java -jar path/to/your/profiling.jar**
* After running and selecting your OS, specify .properties file, what you have already created in paragraph 3, using Open file.
* Start the testing.


## Additional information
If you want to get a more accurate result, you should use **detailed test** and set the value for the key **number_tests** as much as it possible in your .properties file. Use appropriate value for successfully program's finishing. <br />
Also for the serious purposes it is better to use **non_interactive** mode.
