package Ex3;
//https://stackoverflow.com/questions/50542940/java-stop-an-endless-loop-function-by-a-thread
/**
 *  Date - May 23-2019
 *  this class check if giving number is prime or not
 * @author mulugeta
 *
 */
public class Ex3 implements Runnable{

	static long number;
	static Boolean result=null;

	/**
	 * @param n  number to check if prime or not
	 * @param maxTime max time the program can run
	 * @return if the program success to check that the giving number is prime or not
	 * 	within a given time it will return true or false
	 * @throws RuntimeException if the program passed the given time and still return answer
	 * will throw runtime exception
	 */
	public boolean isPrime(long n, double maxTime) throws RuntimeException{

		number=n;

		long time=(long)(maxTime*1000);

		Thread prime=new Thread(this);
		prime.setDaemon(true);

		long start=System.currentTimeMillis();
		prime.start();
		long end=System.currentTimeMillis();

		while( (end-start)<=time){

			if (result!=null)
				return result;

			end=System.currentTimeMillis();
		}

		throw new RuntimeException();

	}


	@Override
	public void run() {

		result=Ex3_tester.isPrime(number);

	}
}