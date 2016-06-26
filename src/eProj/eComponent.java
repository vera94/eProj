package eProj;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

public class eComponent implements Runnable {
	private static BigDecimal eResult = BigDecimal.ZERO;
	private CountDownLatch latch;
	private long from, to;

	public eComponent() {
	}

	public eComponent(CountDownLatch latch, long from, long to) {
		this.from = from;
		this.to = to;
		this.latch = latch;
	}

	@Override
	public void run() {
		BigDecimal currentRes = BigDecimal.ZERO;
		String threadName = Thread.currentThread().getName();
		if (!BasicThreads.quietMode) {
			System.out.println("Thread-" + threadName + " started.");
		}
		for (long i = from; i < to; i++) {
			BigDecimal pow = (new BigDecimal(i));
			BigDecimal d = (new BigDecimal(3)).subtract((new BigDecimal(4)).multiply(pow.multiply(pow)));
			BigDecimal f = fact(2 * i + 1);
			BigDecimal part = d.divide(f, 5, BigDecimal.ROUND_HALF_UP);
			currentRes = currentRes.add(part);
		}
		addResult(currentRes);
		latch.countDown();
		if (!BasicThreads.quietMode) {
			System.out.println("Thread-" + threadName + " stopped.");
		}
	}

	public static synchronized void addResult(BigDecimal currentRes) {
		eResult = eResult.add(currentRes);
	}

	public static void getResultFile(StringBuffer fileName) throws IOException {
		String content = String.valueOf(eResult);
		File file = new File(System.getProperty("user.dir") + File.separator + fileName.toString());
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();

	}

	public static BigDecimal fact(long x) {
		BigDecimal result = BigDecimal.ONE;
		if (x == 1 || x == 0) {
			return result;
		}
		while (x != 0) {
			BigDecimal temp = new BigDecimal(x);
			result = result.multiply(temp);
			x--;
		}
		return result;
	}

}
