package com.biz.grade.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.biz.grade.config.DBContract;
import com.biz.grade.config.Lines;
import com.biz.grade.domain.ScoreVO;
import com.biz.grade.domain.StudentVO;

public class StudentServiceImplV1 implements StudentService {
	
	private List<StudentVO> studentList;
	private Scanner scan;
	private String fileName;
	
	public StudentServiceImplV1() {
		studentList= new ArrayList<StudentVO>();
		scan=new Scanner(System.in);
		fileName="src/com/biz/grade/exec/data/student.txt";
	}
	
	//studentList를 외부에서 참조(사용)할 수 있는 통로 생성
	public List<StudentVO> getStudentList() {
		return studentList;
	}
	
	public StudentVO getStudent(String st_num) {
		//1. studentVO를 null로 clear, null값을 studentVO에 할당
		StudentVO studentVO=null;
		
		//2. studentList를 (순서대로) 뒤지면서
		for(StudentVO stVO:studentList) {
			//3. 매개변수로 받은 st_num가 학생정보에 존재하는지 검사
			//4. 존재하는 경우 해당 학생 정보를 studentVO에 복사
			//5. 반복문 종료
			if(stVO.getNum().equals(st_num)) {
				studentVO=stVO;
				break;
			}
			//6. studentList에서 해당 학번을 찾지 못하면 반복문은 끝까지 실행됨
		}
		//7. 중간에 if, break를 만나고 for문이 중단되면 studentVO에는 stVO 값이 담기게 됨
		//8. for 반복문이 끝까지 진행된 상태인 경우 studentVO에는 null 값이 담기게 됨
		return studentVO;
	}

	@Override
	public void loadStudent() {
		FileReader fileReader=null;
		BufferedReader buffer=null;
		
		//필드변수를 가져다 쓸 경우 명확히 하기 위해 this 키워드를 사용하기도 함
		try {
			fileReader=new FileReader(this.fileName);
			buffer=new BufferedReader(fileReader);
			
			String reader=""
					;
			while(true) {
				reader=buffer.readLine();
				if(reader==null) {
					break;
				}
				
				String[] students=reader.split(":");
				
				StudentVO studentVO=new StudentVO();
				
				studentVO.setNum(students[DBContract.STUDENT.ST_NUM]);
				studentVO.setName(students[DBContract.STUDENT.ST_NAME]);
				studentVO.setDept(students[DBContract.STUDENT.ST_DEPT]);
				studentVO.setGrade(Integer.valueOf(students[DBContract.STUDENT.ST_GRADE]));
				studentVO.setTel(students[DBContract.STUDENT.ST_TEL]);
				
				studentList.add(studentVO);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("학생 정보 파일을 여는 데 실패했습니다.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("학생 정보 파일을 읽는 데 실패했습니다.");
		}
	}

	@Override
	public boolean inputStudent() {
		StudentVO studentVO=new StudentVO();
		
		System.out.print("학번(입력 종료 : END)>> ");
		/*
		 * 변수명명규칙
		 * camelCase : 두 단어 이상 사용할 때, 두 번째 이후 단어 첫글자를 대문자로 사용
		 * snake_case : 두 단어 이상 사용 시 단어를 _(under score)로 연결
		 */
		String st_num=scan.nextLine(); //DB와 연동할 때 호환되기 위하여 snake case로 변수명 작성
		if(st_num.equals("END")) {
			return false;
		}
		int intNum=0;
		try {
			intNum=Integer.valueOf(st_num);
		} catch (Exception e) {
			System.out.println("학번은 숫자로만 입력할 수 있습니다.");
			System.out.println("입력한 문자열 : "+st_num);
			return true;
		}
		if(intNum<1||intNum>99999) {
			System.out.println("학번은 1부터 최대 99999까지 입력할 수 있습니다.\n다시 입력하십시오.");
			return true;
		}
		st_num=String.format("%05d", intNum); //다섯자리 미만으로 입력된 수에 0을 채워넣어 다섯자리로 만들기
		
		for(StudentVO stuVO : studentList) {
			if(stuVO.getNum().equals(st_num)) {
				System.out.println(st_num+"번 학생의 성적이 이미 등록되어 있습니다.");
				return true;
			}
		}
		
		studentVO.setNum(st_num);
		
		System.out.print("이름(입력 종료 : END)>> ");
		String st_name=scan.nextLine();
		if(st_name.equals("END")) {
			return false;
		}
		studentVO.setName(st_name);
		
		System.out.print("학과(입력 종료 : END)>> ");
		String st_dept=scan.nextLine();
		if(st_dept.equals("END")) {
			return false;
		}
		studentVO.setDept(st_dept);
		
		System.out.print("학년(입력 종료 : END)>> ");
		String st_grade=scan.nextLine();
		if(st_grade.equals("END")) {
			return false;
		}
		int intGrade=0;
		try {
			intGrade=Integer.valueOf(st_grade);
		} catch (Exception e) {
			System.out.println("학년은 숫자로만 입력할 수 있습니다.");
			System.out.println("입력한 문자열 : "+st_grade);
			return true;
		}
		studentVO.setGrade(intGrade);
		
		System.out.print("전화번호*010-000-0000 형식으로 입력* (입력 종료 : END)>> ");
		String st_tel=scan.nextLine();
		if(st_tel.equals("END")) {
			return false;
		}
		studentVO.setTel(st_tel);
		
		studentList.add(studentVO); //입력한 데이터 리스트에 추가
		
		this.saveStudent(); //생성한 리스트 파일에 저장
		
		return true;
	}

	@Override
	public void saveStudent() {
		FileWriter fileWriter=null; //PrintWriter의 보조적인 역할
		PrintWriter pWriter=null;
		
		try {
			fileWriter=new FileWriter(this.fileName);
			pWriter=new PrintWriter(fileWriter);
			
			//내부의 Writer Buffer에 값을 기록
			for(StudentVO studentVO : studentList) {
				pWriter.printf("%s:",studentVO.getNum());
				pWriter.printf("%s:",studentVO.getName());
				pWriter.printf("%s:",studentVO.getDept());
				pWriter.printf("%d:",studentVO.getGrade());
				pWriter.printf("%s\n",studentVO.getTel());
			}
			pWriter.flush(); //Writer Buffer에 기록된 값을 파일에 저장
			pWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void studentList() {
		System.out.println("<학생 정보 일람표>");
		System.out.println(Lines.dLine);
		System.out.println("학번\t이름\t학과\t학년\t전화번호");
		System.out.println(Lines.sLine);
		
		for (StudentVO studentVO : studentList) {
			System.out.printf("%s\t", studentVO.getNum());
			System.out.printf("%s\t", studentVO.getName());
			System.out.printf("%s\t", studentVO.getDept());
			System.out.printf("%d\t", studentVO.getGrade());
			System.out.printf("%s\n", studentVO.getTel());
		}
		
		System.out.println(Lines.dLine);
	}

}
