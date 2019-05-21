package Ex3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Ex3B {
	static String arr[];
	static int index=0;
	static int numOfLines = 0;

	public static String[] createFiles(int n) {
		index=n;
		arr = new String[index];
		String fileName="";
		for (int i = 0; i <arr.length; i++) {
			fileName = "File_"+i;
			arr[i] = fileName;
			try {
				FileWriter fos =  new FileWriter(fileName, true);
				BufferedWriter out = new BufferedWriter(fos);
				int getRandomNum = (int) (Math.random()*10);
				numOfLines=numOfLines+getRandomNum;
				for (int j = 0; j <getRandomNum; j++) {
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
	//delete 
	public static void deleteFiles(String[] fileNames) {
		for (int i = 0; i < fileNames.length; i++) {
			for (int j = 0; j < fileNames.length; j++) {
				if(arr[i].contains(fileNames[j])) {
					arr[i]=null;
					i++;
				}
			}
		}
		for (int i = 0; i < arr.length; i++) {
			if(arr[i]!=null) return;
		}
		arr=null;
	}
	public static void countLinesThreads(int numFiles) {
		createFiles(numFiles);
		long final_time=0;
		Thread[] threads = new Thread[numFiles];

		for(int i=0;i < numFiles; i++) {

			threads[i] = new Thread(new LineCounter(arr[i]));
			long start_time=System.currentTimeMillis();
			threads[i].start();

			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long end_time=System.currentTimeMillis();
			final_time = final_time+(end_time-start_time);
		}
		for (int j = 0; j < numFiles; j++) {
			//numOfLines = numOfLines+LineCounter.linenumbers;

		}
		System.out.println("countLinesThreadnumOfLines: "+numOfLines);
		numOfLines=0;
		System.out.println("countLinesThreads final_time :" +final_time);
		deleteFiles(arr);
	}



	public static void countLinesThreadPool(int num) {
		createFiles(num);
		long final_time=0;
		long start=0;
		ExecutorService pool = Executors.newFixedThreadPool(num);
		Future<Integer> results[] = new Future[num];
		for (int i=0; i<num; i++){
			start=System.currentTimeMillis();
			results[i] = pool.submit(new ThreadPool(arr[i],i)); 
			long end=System.currentTimeMillis();
			final_time =final_time+ (end-start);
		}

		System.err.println("countLinesThreadPool numOfLines: "+numOfLines);
		System.err.println("countLinesThreadPool final_time:" + final_time);
		numOfLines=0;
		pool.shutdown();
		deleteFiles(arr);
	}


	public static void countLinesOneProcess(int numFiles) {
		int linenumber_ThreadPool = 0;
		long final_time=0;
		long start=0;
		createFiles(numFiles);
		try{
			start=System.currentTimeMillis();
			for (int i = 0; i < arr.length; i++) {

				//String file_Name = "File_"+i;
				File file =new File(arr[i]);

				if(file.exists()){

					FileReader fr = new FileReader(file);
					LineNumberReader lnr = new LineNumberReader(fr);

					while (lnr.readLine() != null){
						linenumber_ThreadPool++;
					}
					lnr.close();

				}else{
					System.out.println("File does not exists!");
				}
			}
			long end=System.currentTimeMillis();
			final_time =final_time+ (end-start);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("countLinesThreadPool numOfLines: "+linenumber_ThreadPool);
		System.out.println("countLinesThreadPool final_time:" + final_time);
		deleteFiles(arr);
	}
	public static void main(String[] args) {

		countLinesThreads(10);
		countLinesThreadPool(10);
		countLinesOneProcess(10);
	}

}
