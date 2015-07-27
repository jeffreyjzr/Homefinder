package com.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Tools Class for reading parameters
 * 
 * @author Jeffrey
 * 
 */
public class ParameterUtil {
	/**
	 * read parameter
	 * 
	 * @param category
	 *            :parameter type
	 * @param pname
	 *            :parameter name
	 * @return String
	 */
	public static String readParameter(String category, String pname) {
		String value = "";
		SAXReader reader = new SAXReader();
		Document document = null;
		InputStream inputStream = null;
		try {
			// get path of config file
			inputStream = ParameterUtil.class.getResourceAsStream("/com/wiki/click/common/config/config.xml");;
			document = reader.read(inputStream);
			Element root = document.getRootElement();
			// get "category" node
			Element cate = root.element(category);
			List<Element> list = cate.elements();
			for (Element e : list) {
				if (e.attributeValue("name").equals(pname)) {
					value = e.attributeValue("value");
				}
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(inputStream!=null){
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}

}
