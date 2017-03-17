package test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import db.DBUtil;
import sampling.CongressionalSamples;

public class CongressionalSamplesTest {
	@Test
	public void testSampling(){
		CongressionalSamples cs = new CongressionalSamples();
		cs.house();
		cs.senate();
		cs.basicCongress();
		cs.congress();
		cs.materialize();
	}
	@Test
	public void testComputeSubGroupings(){
		
		List<String> attrs = new ArrayList<String>();
		attrs.add("a");
		attrs.add("b");
		attrs.add("c");
//		attrs.add("d");
//		attrs.add("f");
		//循环计算每一种分组属性上的子集(不包含空集、全集)
		for(int subNum = 1; subNum < attrs.size(); subNum++){
			
			int pos = 0;					//当前起始属性位置
			int remainNum = subNum-1;		//除去当前位置的属性，余下还需要取的属性数目
			List<Integer> currentSubGrouping = null;
			if(remainNum != 0){
			while((pos+subNum) <= attrs.size()){
				
				for(int i = pos+1; (i+remainNum) <= attrs.size(); i++){
					//对当前位置属性，每次向后移动一个位置，连续取remainNum个属性值
					currentSubGrouping = new ArrayList<Integer>();
					
						currentSubGrouping.add(pos);
						for(int j = 0; j<remainNum; j++){
							currentSubGrouping.add(i+j);
						}
						System.out.println(currentSubGrouping);
					
					
				}//for
				
				//当前位置向后移动一个单位
				pos++;
			}
			}else{
				for(int j = 0; j<attrs.size(); j++){
					System.out.println(j);
				}
			}
			
		}
	}
	/**
	 * 基于样本数据计算avg聚集操作的误差
	 */
	@Test
	public void computeAvgError(){
		String sql = "SELECT (sum(l_quantity)*sf)/(count(*)*sf) FROM lineitem_sample GROUP BY l_linestatus,l_returnflag ";
		String summarySql = "SELECT * FROM summary";
		DBUtil dbOne = new DBUtil();
		DBUtil dbTwo = new DBUtil();
		ResultSet rsOne = dbOne.excuteQuery(sql);
		ResultSet rsTwo = dbTwo.excuteQuery(summarySql);
		try {
			int i = 0;
			while(rsOne.next() && rsTwo.next()){
				float error = Math.abs(rsOne.getFloat(1)-rsTwo.getFloat(3))/rsTwo.getFloat(3);
				System.out.println("error-"+ (i++) +":" + error*100);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 基于样本数据计算sum聚集操作的误差
	 */
	@Test
	public void computeSumError(){
		String sql = "SELECT sum(l_quantity)*sf FROM lineitem_sample GROUP BY l_linestatus,l_returnflag ";
		String summarySql = "SELECT * FROM summary";
		DBUtil dbOne = new DBUtil();
		DBUtil dbTwo = new DBUtil();
		ResultSet rsOne = dbOne.excuteQuery(sql);
		ResultSet rsTwo = dbTwo.excuteQuery(summarySql);
		try {
			int i = 0;
			while(rsOne.next() && rsTwo.next()){
				float error = Math.abs(rsOne.getFloat(1)-rsTwo.getFloat(2))/rsTwo.getFloat(2);
				System.out.println("error-"+ (i++) +":" + error*100);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 基于样本数据计算count聚集操作的误差
	 */
	@Test
	public void computeCountError(){
		String sql = "SELECT count(*)*sf FROM lineitem_sample GROUP BY l_linestatus,l_returnflag ";
		String summarySql = "SELECT * FROM summary";
		DBUtil dbOne = new DBUtil();
		DBUtil dbTwo = new DBUtil();
		ResultSet rsOne = dbOne.excuteQuery(sql);
		ResultSet rsTwo = dbTwo.excuteQuery(summarySql);
		try {
			int i = 0;
			while(rsOne.next() && rsTwo.next()){
				float error = Math.abs(rsOne.getFloat(1)-rsTwo.getFloat(1))/rsTwo.getFloat(1);
				System.out.println("error-"+ (i++) +":" + error*100);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
