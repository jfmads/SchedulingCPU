package maddenscheduling;

import java.util.Comparator;

/**
 *
 * @author Joseph Madden 
 * Date Created : 04/19/2019
 * Date Modified: 04/24/2019
 *
 * This class contains all of the data members and methods that are necessary
 * for a process.
 */
public class Process {

    private int arrivalT;           // the time the job arrived in the queue
    private double waitingT;        // the time the job spent waiting in the queue
    private int remainingT;         // the time the job has left to finish 
    private int startT;             // the time at which the job begins on CPU
    private final int burstL;       // the time length the job needs the CPU
    private final char name;        // char value given to name the job

    /**
     * This is the initial constructor for a process. It accepts values for
     * arrival time, remaining time and the name of the job. All of the private
     * values are initialized based on this input.
     *
     * @param a arrival time
     * @param r remaining time
     * @param n job name
     */
    public Process(int a, int r, char n) {
        this.arrivalT = a;
        this.waitingT = 0;
        this.remainingT = r;
        this.startT = -1;
        this.burstL = r;
        this.name = n;
    }

    public int getArrivalT() {
        return this.arrivalT;
    }

    public double getWaitingT() {
        return this.waitingT;
    }

    public int getRemainingT() {
        return this.remainingT;
    }

    public int getStartT() {
        return this.startT;
    }

    public int getBurstL() {
        return this.burstL;
    }

    public char getName() {
        return this.name;
    }

    public void setArrivalT(int aT) {
        this.arrivalT = aT;
    }

    public void setWaitingT(double wT) {
        this.waitingT = wT;
    }

    public void setRemainingT(int rT) {
        this.remainingT = rT;
    }

    public void setStartT(int sT) {
        this.startT = sT;
    }
    
    /**
     * Very simple class that implements the Comparator interface.
     * This process is based on which job arrives first.
     */
    public static class FCFSComp implements Comparator<Process> {

        @Override
        public int compare(Process p1, Process p2){
            return p1.getArrivalT() - p2.getArrivalT();
        }  
    }
    
    /**
     * Very simple class that implements the Comparator interface.
     * This process is based on which job arrives first and then shortest
     * job first.
     */
    public static class SJFComp implements Comparator<Process> {
        
        @Override
        public int compare(Process p1, Process p2){
            return p1.getBurstL() - p2.getBurstL();
        }
        
    }
    /**
     * Very simple class that implements the Comparator interface.
     * This process is based on which job arrives first and then preemptive
     * shortest job.
     */
    public static class SRTFComp implements Comparator<Process> {
        
        @Override
        public int compare(Process p1, Process p2){
            return p1.getRemainingT() - p2.getRemainingT();
        }
        
    }

}
