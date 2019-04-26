package maddenscheduling;

import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author Joseph Madden
 * Date Created : 04/19/2019
 * Date Modified: 04/24/2019
 *
 * Madden Scheduling:
 *
 * This program simulates scheduling through deterministic modeling. The
 * execution schedule and average waiting time of processes is calculated based
 * on the SRTF, SJF or FCFS algorithm.
 *
 * The workload is specified as follows: a1 b1 a2 b2 a3 b3
 *
 * (a1 b1) are the arrival time and CPU burst length of a process. It is assumed
 * that each process enters in ascending order of arrival time. (ak is less than
 * ak+1)
 *
 * The program will output the execution schedule and average waiting time for
 * each scheduling algorithm. A, B, C.. will be used to denote the processes
 * based upon their arrival.
 *
 */
public class Main {

    static PriorityQueue<Process> readyQ;               // ready Queue to store processes
    static Process activeP;                             // active process to be stored in the ready Queue
    static int[] inputNum;                              // array for process input in integer form
    static char jobName;                                // name of the job, first on is job 'A'
    static int maxT, totalT, waitingT;                  // will store the necessary time values of a process 
    static float avgWT;                                 // will store the average waiting time for each process
    static String[] inputString;                        // array for process input in string form

    public static void main(String[] args) {
        gatherData();
        SRTF();
        SJF();
        FCFS();
    }
    
    /**
     * Simple method constructed to gather scanner input
     * from the keyboard and modularize the main method.
     */
    private static void gatherData(){
        
        Scanner in = new Scanner(System.in);            // scanner to gather process inputString
        int i;                                          // int value used for loops
        boolean invalid = true;                         // boolean value for while loop
        while (invalid) {                               // while input is invalid, could be various reasons
            System.out.print("Enter process arrival times & burst lengths: ");
            
            inputString = in.nextLine().split(" ");     // tokenizes input based on " "
            if ((inputString.length % 2) == 0) {        // if there is an even amount of data input
                invalid = false;                        // input format is recognized as correct
                inputNum = new int[inputString.length];
                for (i = 0; i < inputString.length; i++) {
                    try {
                        inputNum[i] = Integer.parseInt(inputString[i]);     // attempts to convert string value to an integer
                    } catch (NumberFormatException ne) {                    // catches the exception if it is can not convert
                        System.out.println("INVALID INTEGER INPUT -- input an even amount of integer values");
                        invalid = true;                                     // resets boolean value to true (invalid)
                        break;                                              // breaks from the if statement
                    }
                }
                for (i = 0; i < inputNum.length; i++) {    // calculates max time, only reaches if valid integer input
                    maxT += inputNum[i];                   // maxT + arrival + burst length
                }
            } else {
                System.out.println("INVALID DATA FORMAT -- input an even amount of integer values");
            }
        }
    }

    /**
     * Method used to reset the initial values of some variables necessary for
     * each of the algorithms.
     */
    private static void clean() {
        jobName = 'A';
        totalT = -1;
        waitingT = 0;
    }

    /**
     * First Come First Serve: Sensitive to the order in which jobs arrive. When
     * a job arrives it is placed in the ready queue.
     */
    private static void FCFS() {

        clean();
        System.out.print("\nFCFS: ");
        readyQ = new PriorityQueue<>(new Process.FCFSComp());

        while (totalT < maxT) {                             // this loop goes until the last job finishes
            totalT++;                                       // originally started at -1, increments throughout loop

            for (int i = 0; i < inputNum.length; i += 2) {  // searches integer array for values at current CPU time
                if (inputNum[i] == totalT)                  // totalT is also currentT in some aspects
                    readyQ.add(new Process(inputNum[i], inputNum[i + 1], jobName++)); // creates a new process with arrival time, burstlength (timeR), name          
            }

            if (!readyQ.isEmpty()) {                        // ready queue has atleast one job

                activeP = readyQ.peek();                    // retrieves the first job but does not remove it
                if (activeP.getStartT() < 0)                // values initially have start time of -1
                    activeP.setStartT(totalT);              // sets the start time to the current time
                    
                for (Process p : readyQ) {
                    if (p != activeP)
                        p.setWaitingT(p.getWaitingT() + 1); // increments the waitingtime of the process
                }

                activeP.setRemainingT(activeP.getRemainingT() - 1); // subtracts 1 from time remaining
                
                if (activeP.getRemainingT() == 0) {
                    System.out.print(activeP.getName() + Integer.toString(totalT - activeP.getStartT() + 1) + " ");
                    waitingT += activeP.getWaitingT();
                    readyQ.remove();
                }
            }
        }
        avgWT = (float) waitingT / ((float) inputNum.length / 2); // the values only account for half, hence / 2
        System.out.printf("\nAverage Waiting Time: %.2f \n", avgWT);
    }

    /**
     * Shortest Job First: When the CPU becomes available it is given to the
     * process whose next CPU burst is shortest among all of those in the ready
     * queue.
     */
    private static void SJF() {

        clean();
        System.out.print("\nSJF: ");
        readyQ = new PriorityQueue<>(new Process.SJFComp());

        while (totalT < maxT) {
            totalT++;

            for (int i = 0; i < inputNum.length; i += 2) {  // searches integer array for values at current CPU time
                if (inputNum[i] == totalT)                  // totalT is also currentT in some aspects
                {
                    readyQ.add(new Process(inputNum[i], inputNum[i + 1], jobName++)); // creates a new process with arrival time, burstlength (timeR), name
                    
                    if (readyQ.size() == 1)
                        activeP = readyQ.peek();            // starts immediately when the job enters
                    
                }
            }
            if (!readyQ.isEmpty()) {

                if (activeP.getStartT() < 0)
                    activeP.setStartT(totalT);

                for (Process p : readyQ) {
                    if (p != activeP)
                        p.setWaitingT(p.getWaitingT() + 1);
                }

                activeP.setRemainingT(activeP.getRemainingT() - 1); // subtracts 1 from time remaining

                if (activeP.getRemainingT() == 0) {
                    System.out.print(activeP.getName() + Integer.toString(totalT - activeP.getStartT() + 1) + " ");
                    waitingT += activeP.getWaitingT();
                    readyQ.remove(activeP);
                    activeP = readyQ.peek();
                }
            }
        }
        avgWT = (float) waitingT / ((float) inputNum.length / 2);   // the values only account for half, hence / 2
        System.out.printf("\nAverage Waiting Time: %.2f \n", avgWT);
    }

    /**
     * Shortest Remaining Time First: (SJF preemptive) Begins to execute the
     * first initial job, but when a job with a shorter burst length enters,
     * that one begins to execute.
     */
    private static void SRTF() {

        clean();
        System.out.print("\nSRTF: ");
        readyQ = new PriorityQueue<>(new Process.SJFComp());

        while (totalT < maxT) {
            totalT++;
            for (int i = 0; i < inputNum.length; i += 2) {
                if (inputNum[i] == totalT) {                // new job
                    if (!readyQ.isEmpty()) {                // ready queue not empty
                        activeP = readyQ.peek();
                        if (activeP.getRemainingT() < inputNum[i + 1]) {                        // if the new job has more remaining time
                            readyQ.add(new Process(inputNum[i], inputNum[i + 1], jobName++));   // add new job to ready queue
                            break;
                        } else {
                            readyQ.add(new Process(inputNum[i], inputNum[i + 1], jobName++));   // add new job to ready queue
                            System.out.print(activeP.getName() + Integer.toString(activeP.getBurstL() - activeP.getRemainingT()) + " "); // outputs the jobs current state
                        }
                        readyQ.add(new Process(totalT, activeP.getRemainingT(), activeP.getName())); // adds "new" shorter job to ready queue (remaining portion)
                        readyQ.remove(activeP);                                                 // removes original job
                        break;
                    }
                    readyQ.add(new Process(inputNum[i], inputNum[i + 1], jobName++));           // only reaches this statement if it is the first job
                }
            }

            if (!readyQ.isEmpty()) {                        // ready queue has atleast one job
                activeP = readyQ.peek();                    // retrieves the first job but does not remove it
                if (activeP.getStartT() < 0)                // values initially have start time of -1
                    activeP.setStartT(totalT);              // sets the start time to the current time
                    
                for (Process p : readyQ) {
                    if (p != activeP)
                        p.setWaitingT(p.getWaitingT() + 1); // increments the waitingtime of the process
                }

                activeP.setRemainingT(activeP.getRemainingT() - 1); // subtracts 1 from time remaining

                if (activeP.getRemainingT() == 0) {
                    System.out.print(activeP.getName() + Integer.toString(totalT - activeP.getStartT() + 1) + " ");
                    waitingT += activeP.getWaitingT();
                    readyQ.remove();
                    activeP = readyQ.peek();
                }
            }
        }
        avgWT = (float) waitingT / ((float) inputNum.length / 2); // the values only account for half, hence / 2
        System.out.printf("\nAverage Waiting Time: %.2f \n", avgWT);
    }
}
