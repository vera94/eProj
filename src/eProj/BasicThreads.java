package eProj;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class BasicThreads {
	public static long precision;
	public static long tasks;
	private static StringBuffer fileName = new StringBuffer("result.txt");
	public static boolean quietMode = false;
	private static long time;

	public static void main(String[] args) {

		long startTime, endTime;
		startTime = System.currentTimeMillis();
		try {
			parseArguments(args);
		} catch (IllegalArgumentException iae) {
			System.out.println(iae.getMessage());
			throw new RuntimeException();
		}
		final CountDownLatch cdl = new CountDownLatch((int) tasks);
		long part = precision / tasks;
		for (int i = 0; i < tasks; i++) {
			long from = i * part, to = (i + 1) * part;
			new Thread(new eComponent(cdl, from, to)).start();
		}
		try {
			cdl.await();
			endTime = System.currentTimeMillis();
			time = (endTime - startTime);
			if (!BasicThreads.quietMode) {
				System.out.println("Threads used in current run:" + tasks);
			}
			System.out.println("Total execution time for current run (millis):" + time);
			eComponent.getResultFile(fileName);

		} catch (InterruptedException e) {
			System.out.println(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void parseArguments(String[] args) throws IllegalArgumentException {
		int length = args.length;
		for (int i = 0; i < length; i++) {
			switch (args[i]) {
			case "-t":
				tasks = Integer.parseInt(args[++i]);
				break;
			case "-p":
				precision = Integer.parseInt(args[++i]);
				break;
			case "-o":
				fileName = new StringBuffer(args[++i]);
				break;
			case "-q":
				quietMode = true;
				break;
			default:
				throw new IllegalArgumentException("Incorrect arguments: " + args);
			}
		}
	}

}
