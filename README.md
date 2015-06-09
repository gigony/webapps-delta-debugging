Introduction
=============

A prototype tool for delta-debugging web applications.

This tool was used for conducting experiments in a paper submitted to [FSE15](https://sites.google.com/site/fsewebdd/) 

The followings are instructions for application installation and setup.

Build and Run
=======

1) `$ git clone https://github.com/gigony/webapps-delta-debugging.git`

2) Import the cloned project from Eclipse.

3) Run Main.java


Please uncomment lines in `main` method of `Main.java` to run for the other test cases. (Currently, only the first 'short' test case is uncommented.)

During the execution, logs for a test case will be appended to a log file (e.g., logs for 'experiment/tests/CanadaLong.txt' will be stored in 'experiment/tests/CanadaLong.log').

You may need FireFox web browser to run this tool and the tool was tested in OSX 10.10 (Yosemite).


