package Ex3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

class LineCounter extends Thread {
	String file_Name="";
	static int linenumbers=0;;
	public LineCounter(String file_Name) {
		this.file_Name=file_Name;

	}

	public void run() {

		readFile();
	}
	public synchronized int readFile() {
		try{
			File file =new File("C:\\Users\\mulugeta\\eclipse-workspace\\Ex3\\"+file_Name);

			if(file.exists()){

				FileReader fr = new FileReader(file);
				LineNumberReader lnr = new LineNumberReader(fr);

				while (lnr.readLine() != null){
					linenumbers++;
				}

				//System.out.println("Total number of lines : " + linenumber_ThreadPool);

				lnr.close();


			}else{
				System.out.println("File does not exists!");
			}

		}catch(IOException e){
			e.printStackTrace();
		}
		return linenumbers;
	}
}
class ThreadPool implements Callable<Integer>{
	String file_Name;
	static int linenumber_ThreadPool;
	int id;
	ThreadPool(String i,int j){
		file_Name = i;
		id =j;
	}
	@Override
	public Integer call() throws Exception {
		//System.out.println("Thread id = "+id+" started!");
		try{
			File file =new File("C:\\Users\\mulugeta\\eclipse-workspace\\Ex3\\"+file_Name);

			if(file.exists()){

				FileReader fr = new FileReader(file);
				LineNumberReader lnr = new LineNumberReader(fr);

				while (lnr.readLine() != null){
					linenumber_ThreadPool++;
				}

				//	System.out.println("Total number of lines : " + linenumber_ThreadPool);

				lnr.close();

			}else{
				System.out.println("File does not exists!");
			}

		}catch(IOException e){
			e.printStackTrace();
		}

		return linenumber_ThreadPool;
	}

}

