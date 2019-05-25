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

/**
 * this class extend threads and run 
 * program that read from file and count lines
 * the program return lines number
 * Date - May 23-2019
 * @author mulugeta
 *
 */
class LineCounter extends Thread {
	String file_Name="";
	private static int linenumber;
	//constructor
	public LineCounter(String file_Name) {
		this.file_Name=file_Name;
		linenumber = 0;
	}

	public void run() {

		readFile();
	}

	/**
	 * this program reading from file and count lines number
	 * @return line numbers
	 */
	public synchronized int readFile() {
		try{
			File file =new File("C:\\Users\\mulugeta\\eclipse-workspace\\Ex3\\"+file_Name);

			if(file.exists()){

				FileReader fr = new FileReader(file);
				LineNumberReader lnr = new LineNumberReader(fr);

				while (lnr.readLine() != null){
					linenumber++;
				}

				lnr.close();

			}else{
				System.out.println("File does not exists!");
			}

		}catch(IOException e){
			e.printStackTrace();
		}
		return linenumber;
	}

	public static int getLinenumbers() {
		// TODO Auto-generated method stub
		return linenumber;
	}
}
/* 
 * this class extend threads and call 
 * program that read from file and count lines
 * the program return lines number
 * Date - May 23-2019
 * */
class ThreadPool implements Callable<Integer>{
	String file_Name;
	private static int linenumber_pool;
	//constructor
	ThreadPool(String i){
		file_Name = i;
	}
	/* (non-Javadoc)
	 * return line numbers
	 * @see java.util.concurrent.Callable#call()
	 */ 
	@Override
	public Integer call() throws Exception {
		try{
			File file =new File("C:\\Users\\mulugeta\\eclipse-workspace\\Ex3\\"+file_Name);

			if(file.exists()){

				FileReader fr = new FileReader(file);
				LineNumberReader lnr = new LineNumberReader(fr);

				while (lnr.readLine() != null){
					linenumber_pool++;
					setLinenumber_pool(linenumber_pool);
				}

			}else{
				System.out.println("File does not exists!");
			}

		}catch(IOException e){
			e.printStackTrace();
		}

		return linenumber_pool;
	}

	public static int getLinenumber_pool() {
		return linenumber_pool;
	}
	public static void setLinenumber_pool(int linenumber_pool) {
		ThreadPool.linenumber_pool = linenumber_pool;
	}
}

