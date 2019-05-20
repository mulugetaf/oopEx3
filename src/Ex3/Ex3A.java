package Ex3;
public class Ex3A implements Runnable{

	static long number;
	static Boolean result=null;

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

		result=Ex3A_tester.isPrime(number);

	}
}