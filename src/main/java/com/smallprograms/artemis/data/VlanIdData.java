package com.smallprograms.artemis.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.smallprograms.artemis.data.vo.VlanID;
import com.smallprograms.utils.ExcelUtil;
import com.smallprograms.utils.FileUtils;
import com.smallprograms.utils.JSONConfigUtil;

/**
 * 解析VlanId数据（VLAN_20161005000000.xlsx）
 * 
 * @author LILJ
 * 
 */
public class VlanIdData {
	//E:\\山海诚信\\阿曼\\artemis_docs\\数据\\VLAN_20161005000000.xlsx
	public static void main(String[] args) throws Exception {
		
		List<List<Map<String, String>>> data = ExcelUtil.readExcelWithTitle("E:\\山海诚信\\阿曼\\artemis_docs\\数据\\VLAN_20161005000000.xlsx");
		
		for (List<Map<String, String>> list : data) {
			List<String> vlanIds = new ArrayList<String>();
			for (Map<String, String> map : list) {
				String lat = map.get("LAT");
				String lon = map.get("LON");
				
				if(lat == null || "".equals(lat.trim())){
					expCSV(map, "d:\\nolatlon");
					continue;
				}
				if(lon == null || "".equals(lon.trim())){
					expCSV(map, "d:\\nolatlon");
					continue;
				}
				String vlanid = map.get("Internet VLAN");
				if(vlanIds.contains(vlanid)){
					expCSV(map, "d:\\revlanid");
					continue;
				}
				vlanIds.add(vlanid);
				
				VlanID valneId = new VlanID();
				valneId.setLat(lat);
				valneId.setLon(lon);
				valneId.setVlanid(vlanid.replace(".0", ""));
				valneId.setOperator("omantel");
				valneId.setState(0);
				FileUtils.writeResult(JSONObject.fromObject(valneId, JSONConfigUtil.getNullPropJSONConfigFilter()).toString(), "D:\\valnid");
			}
		}
	}
	
	
	private static void expCSV(Map<String, String> map,String filepath){
		String header = "Type,Site Name,Site Code,NE Type,NE IP Address,Internet VLAN,Business ONT Internet VLAN,Physical Location,LAT,LON";
		List<String> keys = Arrays.asList(header.split(","));
		StringBuffer sb = new StringBuffer();
		for (String key : keys) {
			sb.append(map.get(key)+",");
		}
		FileUtils.writeResult(sb.substring(0,sb.length()-1).toString(), filepath);
	}
	
}
