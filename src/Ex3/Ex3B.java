package Ex3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
//https://stackoverflow.com/questions/200469/what-is-the-difference-between-a-process-and-a-thread

/**
 * Date - May 23-2019
 * This class running Threads that read files
 * and print running time and total lines 
 * @author mulugeta
 *	
 */
public class Ex3B {
	static String arr[];           //  array to files
	static int size=0;            // size of array
	static int numOfLines = 0;   //lines number
	/*
	 * create array and return array size n that contains
	 * the name of files and every file  
	 * has a random numbers line
	 *  
	 */
	/**
	 * @param n  array size
	 * @return array size n contains files name
	 */
	public static String[] createFiles(int n) {
		size=n;
		arr = new String[size];
		String fileName="";
		//random number for lines
		Random r = new Random(n);
		int numLines = r.nextInt(1000);
		for (int i = 1; i <=arr.length; i++) {
			fileName = "File_"+i;
			arr[i-1] = fileName;
			try {
				FileWriter fos =  new FileWriter(fileName, true);
				BufferedWriter out = new BufferedWriter(fos);

				for (int j = 1; j <=numLines; j++) {
					out.write("HelloWorld");
					out.newLine();
				}
				out.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		return arr;

	}

	/**this program delete files from array by giving array of files
	 * first the program delete from library
	 * then it delete  from array 
	 * @param fileNames
	 */
	public static void deleteFiles(String[] fileNames) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < fileNames.length; j++) {
				//check if it is not null
				if((arr[i]!=null)&&(fileNames[j]!=null)){
					//check if the file names is same
					if(arr[i].contains(fileNames[j])) {
						File temp = new File(arr[i]);
						//delete from library
						temp.delete();
						//delete  from array 
						arr[i]=null;
					}
				}
			}
		}
		//if all index in the array null 
		for (int i = 0; i < arr.length; i++) {
			if(arr[i]!=null) return;
		}
		arr=null;

	}
	/*Thread
	A thread is an entity within a process that can be scheduled for execution.
 	All threads of a process share its virtual address space and system resources.
  	In addition, each thread maintains exception handlers, a scheduling priority, 
  	thread local storage, a unique thread identifier, 
  	and a set of structures the system will use to save the thread context until it is scheduled.
   	The thread context includes the thread's set of machine registers,
    the kernel stack, a thread environment block, 
    and a user stack in the address space of the thread's process. 
    Threads can also have their own security context, which can be used for impersonating clients.
	 * 
	 */
	/**
	 * this program create files and run to all files thread
	 * and print line number,running time and delete all
	 * the program not calculate create files time and delete time
	 * 
	 * @param numFiles number of files 
	 */
	public static void countLinesThreads(int numFiles) {
		createFiles(numFiles);
		long final_time=0,start_time=0,end_time=0;

		//threads array
		Thread[] threads = new Thread[numFiles];

		for(int i=0;i < numFiles; i++) {
			start_time=System.currentTimeMillis();
			threads[i] = new Thread(new LineCounter(arr[i]));
			threads[i].start();

			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			end_time=System.currentTimeMillis();
			final_time = final_time+(end_time-start_time);
		}
		for (int j = 0; j < numFiles; j++) {
			numOfLines = numOfLines+LineCounter.getLinenumbers();

		}
		//print number of lines and running time
		System.out.println("countLinesThreads Total lines: "+numOfLines);
		numOfLines=0;
		System.out.println("countLinesThreads running time:" +final_time+" ms");
		System.out.println();
		deleteFiles(arr);

	}
	/*
	 * A thread pool is a collection of threads which are 
	 * assigned to perform uniformed tasks. 
	 * The advantages of using thread pool pattern is 
	 * that you can define how many threads is allowed to execute simultaneously.
	 *  This is to avoid server crashing due to high CPU load or out of memory condition,
	 *  we will use ThreadPool if we have alo0tof small tasks
	 */

	/**
	 * this program create files and run to all files threadpool
	 * and print line number,running time and delete all
	 * the program not calculate create files time and delete time
	 * @param num  number of files 
	 */
	public static void countLinesThreadPool(int num) {
		//create files
		createFiles(num);
		long final_time=0,start=0,end=0;
		ExecutorService pool = Executors.newFixedThreadPool(num);
		Future<Integer> results[] = new Future[num];

		for (int i=0; i<num; i++){
			start=System.currentTimeMillis();
			results[i] = pool.submit(new ThreadPool(arr[i])); 
			/*
			 * * @return the computed result
			 * @throws CancellationException if the computation was cancelled
			 * @throws ExecutionException if the computation threw an
			 * exception
			 * @throws InterruptedException if the current thread was interrupted
			 * while waiting
			 */
			try {
				numOfLines = results[i].get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			end=System.currentTimeMillis();
			final_time =final_time+ (end-start);

		}
		pool.shutdown();
		//print number of lines and running time
		System.out.println("countLinesThreadPool Total lines: "+numOfLines);
		System.out.println("countLinesThreadPool running time:" + final_time+" ms");
		System.out.println();
		numOfLines=0;
		deleteFiles(arr);

	}
	/*
	 * Process
	Each process provides the resources needed to execute a program.
 	A process has a virtual address space, executable code, open handles to system objects,
  	a security context, a unique process identifier, environment variables, 
  	a priority class, minimum and maximum working set sizes, 
  	and at least one thread of execution. Each process is started with a single thread, 
  	often called the primary thread, but can create additional threads from any of its threads.
	 */
	/**
	 * this program create files and run to all files OneProcess
	 * and print line number,running time and delete all
	 * the program not calculate create files time and delete time
	 * @param numFiles  number of files 
	 */
	public static void countLinesOneProcess(int numFiles) {
		int linenumber_OneProcess = 0;
		long final_time=0,start=0,end=0;

		createFiles(numFiles);
		try{
			start=System.currentTimeMillis();
			for (int i = 0; i < arr.length; i++) {
				File file =new File(arr[i]);
				if(file.exists()){

					FileReader fr = new FileReader(file);
					LineNumberReader lnr = new LineNumberReader(fr);

					while (lnr.readLine() != null){
						linenumber_OneProcess++;
					}
					lnr.close();

				}else{
					System.out.println("File does not exists!");
				}
			}
			end=System.currentTimeMillis();
			final_time =final_time+ (end-start);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		//print number of lines and running time
		System.out.println("countLinesOneProcess Total lines: "+linenumber_OneProcess);;
		System.out.println("countLinesOneProcess running time:" + final_time+" ms");
		System.out.println();
		deleteFiles(arr);
	}
	/*
	 * A thread runs in a shared memory space, but a process runs in a separate memory space
A thread is a light-weight process, but a process is a heavy-weight process.
A thread is a subtype of process.

	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		countLinesThreads(1000);
		countLinesThreadPool(1000);
		countLinesOneProcess(1000);
	}

}