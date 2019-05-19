package com.biyl.emmatesting.util;

import java.io.File;

/**
 * 根据要求填写测试用例Excel之后，运行该类能够自动生成指定模块的测试用例代码
 * @author Administrator
 *
 */
public class TestCaseFactory {
	public static void main(String[] args) {
		Test_Case_Factory("user","test");
	}

	/**
	 * 
	 * @Description: 根据模块名、功能名(excel名称)来自动生成测试用例脚本
	 * @param @param moduleName 模块名
	 * @param @param functionName 功能名
	 * @return void
	 * @throws
	 */
	public static void Test_Case_Factory(String moduleName, String functionName) {
		final String caseFolder = "src/main/java/com/biyl/emmatesting/testcase/";	//测试代码包路径
		File sourceFile = null;	//测试用例excel源文件
		String sheetName = null;	//测试用例excel中的sheet名字
		int sheetNum = 0;//sheet的号码
		String classname = "";
		
		//如果包名不存在，则新建
		File packAge = new File(caseFolder+"/"+"moduleName");
		if(packAge.exists()){
			System.out.println(moduleName+"包已经存在！");
			System.out.println("正在生成用例到"+moduleName+"包下，请稍等...");
		}else{
			packAge.mkdir();
			System.out.println(moduleName+"包已创建");
			System.out.println("正在生成用例到"+moduleName+"包下，请稍等...");
		}
		
		String dir = "data/";
		String path = "";
		File file = new File(dir+File.separator+moduleName+"/"+functionName+".xls");
		path = file.getPath();
		//根据传入的excel文件路径 获得模块中的sheet数量，也就是用例个数
		
	}
}
