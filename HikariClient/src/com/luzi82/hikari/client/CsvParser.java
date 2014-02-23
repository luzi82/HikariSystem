package com.luzi82.hikari.client;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import au.com.bytecode.opencsv.CSVReader;

public class CsvParser {

	public static <Data> List<Data> toList(String csv, Class<Data> dataClass)
			throws Exception {
		StringReader sr = new StringReader(csv);
		CSVReader cr = new CSVReader(sr);
		List<String[]> csvDataList = cr.readAll();
		cr.close();
		Iterator<String[]> csvDataItr = csvDataList.iterator();

		Map<String, Integer> colNameToIdx = new HashMap<String, Integer>();
		String[] keyRow = csvDataItr.next();
		for (int i = 0; i < keyRow.length; ++i) {
			String cellValue = keyRow[i];
			if (cellValue == null)
				continue;
			if (cellValue.length() <= 0)
				continue;
			colNameToIdx.put(cellValue, i);
		}

		Field[] dataClassFieldAry = dataClass.getFields();

		LinkedList<Data> ret = new LinkedList<Data>();
		while (csvDataItr.hasNext()) {
			String[] dataRow = csvDataItr.next();
			Data data = dataClass.newInstance();
			for (Field dataClassField : dataClassFieldAry) {
				if (Modifier.isStatic(dataClassField.getModifiers())) {
					continue;
				}
				String fieldname = dataClassField.getName();
				int colIdx = colNameToIdx.get(fieldname);
				Class dataClassFieldType = dataClassField.getType();
				if (dataClassFieldType == String.class) {
					dataClassField.set(data, dataRow[colIdx]);
				} else if (dataClassFieldType == Integer.class) {
					dataClassField.set(data, Integer.parseInt(dataRow[colIdx]));
				} else if (dataClassFieldType == Integer.TYPE) {
					dataClassField.set(data, Integer.parseInt(dataRow[colIdx]));
				} else {
					throw new NotImplementedException("RHDTI3GN: "
							+ dataClassFieldType.getName());
				}
			}
			ret.add(data);
		}

		return ret;
	}

}