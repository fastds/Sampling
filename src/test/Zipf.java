package test;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.distribution.ZipfDistribution;
import org.junit.Test;


public class Zipf {
	
	@Test
	public void generateData(){
		int num = 100;
		Map<Integer,Integer> counter = new HashMap<Integer, Integer>();
		ZipfDistribution zipf = new ZipfDistribution(num, 0.87);
		for(int i =0 ;i < num ;i++){
			int sample = zipf.sample();
			Integer value = counter.get(sample);
			if(value == null)
				value = 0;
			counter.put(sample,++value);
		}
		System.out.println(counter);
	}
	
	@Test
	public void generateZipfTable(){
		int dataRange = 2000;		//数据分布范围为5000 (1-5000)
		int dataNum = 1000000;		//一百万条数据
		
		ZipfDistribution zipf = new ZipfDistribution(dataRange, 0.86);
		FileOutputStream fos = null;
		String[] groupOne = {"T","F"};
		String[] groupTwo = {"A","B"};
		String[] groupThree = {"X","Y"};
		try {
			fos = new FileOutputStream("test.sql");
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			bw.newLine();
			for(int i =0 ;i < dataNum ;i++){
				String group1 = Math.random()<0.4?groupOne[0]:groupOne[1];
				String group2 = Math.random()<0.4?groupTwo[0]:groupTwo[1];
				String group3 = Math.random()<0.4?groupThree[0]:groupThree[1];
				
				String updateZipfState = "INSERT INTO test values('"+ group1 +"','"+ group2 +"','"+ group3 +"',"+ zipf.sample() +");"; 
				
				bw.write(updateZipfState);
				bw.newLine();
				bw.flush();
			}
			bw.flush();
			fos.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
