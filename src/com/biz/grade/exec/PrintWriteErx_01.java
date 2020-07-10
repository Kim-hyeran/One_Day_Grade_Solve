package com.biz.grade.exec;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class PrintWriteErx_01 {
	
	public static void main(String[] args) {
		
		String fileName="src/com/biz/grade/exec/data/test1.txt";
		
		FileWriter fileWriter=null;
		PrintWriter pWriter=null;
		
		try {
			/*
			 * PrintWriter()는 보통 단독으로 사용하지 않고, FileWriter로 파일을 연 후 PrintWriter에 연결하여 사용
			 * FileWriter로 파일을 열 때, 두 번째 매개변수로 true 값을 주입하면 파일을 append mode로 연다
			 * 파일이 append mode로 열리면 기존에 저장된 내용이 삭제되지 않고 이어서 문자열을 추가하는 상태가 된다
			 */
			fileWriter=new FileWriter(fileName, true);
			pWriter=new PrintWriter(fileWriter);
			
			Date date=new Date();
			pWriter.println("대한민국만세 : "+date.toString());
			//PrintWriter는 값을 저장하면 일단 buffer에 임시 보관됨
			//flush() method를 호출하여 buffer에 담긴 데이터를 파일로 보낸 후 close()
			pWriter.flush();
			pWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}