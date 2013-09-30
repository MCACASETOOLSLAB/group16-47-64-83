group16-47-64-83
================

The package is created for the purpose of E-learning of the topics process synchronization,semaphores,monitores,locks,mutex etc.
Dining Philosopher problem is taken to make the learning easy.
The problem is solved using semaphores,locks,mutex.
The working of locks is defined separately in the other page called lock.

To use the pakage follow the steps--
1. download the whole package.
2.make sure you've downloded the folders also.
3.right-click on "Dining-philo" file, open with any internet browser.

if u are just opening the file then the images or links will not work.

==========================================================================================================
C++ Version--


To use the software follow the steps--

1. download the whole package of C++ files.

2.compile the code file in any c++ IDE.

3. The initial states of the philosopher’s is given as thinking.

4. Press 1 if you want to exit.

5. Press 2 if any of the philosopher is hungry he will pick up his spoons if both are available, to do this he must pick them in a critical section.

6. Press 3 if you want to change the state of any philosopher from eating to thinking.

7. If we change the state of a philosopher from eating to thinking then immediate neighbours of that person will start eating
(if 1 is eating and we changing his state to thinking then philosopher 2 and 5 start eating). 

8. If any of the philosopher is eating(critical section) and his neighbor send request for eating then he will go to waiting state.

==========================================================================================================

Compiling and Running Java programs locally

  Requirement: JDK 1.6 or above
  
  Step1. Download all files in a folder "prog" (say)
  Step2. Press Windows key + r or open "run" 
         type cmd  :- command prompt will open
            go to the folder containing downloaded files using "cd" and "cd .."
  step3. type SET PATH = "jdk/bin folder"
          e.g. c:\prog> SET PATH = D:\jdk\bin
  step4. compile all the java files
          e.g. c:\prog>javac diners.java
          similarly for other java files
  step5. open applet viewer using appletviewer command
          e.g. c:\prog> appletviewer diners.htm
  step6. repeat step5 for all htm/html files in the repo.
  step7. wait and watch, then stop. 
          
    
