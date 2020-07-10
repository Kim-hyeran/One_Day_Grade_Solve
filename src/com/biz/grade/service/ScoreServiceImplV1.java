package com.biz.grade.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.biz.grade.config.DBContract;
import com.biz.grade.config.Lines;
import com.biz.grade.domain.ScoreVO;
import com.biz.grade.domain.StudentVO;

public class ScoreServiceImplV1 implements ScoreService {
	
	private List<StudentVO> studentList;
	private List<ScoreVO> scoreList;
	private Scanner scan;
	private String fileName;
	
	//과목명을 문자열 배열로 선언하고, 과목명 문자열 배열 갯수만큼 점수를 담을 intScores 배열 선언
	private String[] strSubjects;
	private Integer[] intScores;
	private int[] totalSum;
	private int[] totalAvg;

	StudentService stService;
	
	public ScoreServiceImplV1() {
		scoreList=new ArrayList<ScoreVO>();
		scan=new Scanner(System.in);
		fileName="src/com/biz/grade/exec/data/score.txt";
		
		strSubjects=new String[]{"국어","영어","수학","음악"};
		intScores=new Integer[strSubjects.length];
		totalSum=new int[strSubjects.length];
		totalAvg=new int[strSubjects.length];

		stService=new StudentServiceImplV1();
		stService.loadStudent();
		
		//StudentService로부터 studentList를 추출하여 사용할 준비
		studentList=stService.getStudentList();
	}

	@Override
	public void loadScore() {
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
				
				String[] scores=reader.split(":");
				
				ScoreVO scoreVO=new ScoreVO();
				
				scoreVO.setNum(scores[DBContract.SCORE.SC_NUM]);
				scoreVO.setKor(Integer.valueOf(scores[DBContract.SCORE.SC_KOR]));
				scoreVO.setEng(Integer.valueOf(scores[DBContract.SCORE.SC_ENG]));
				scoreVO.setMath(Integer.valueOf(scores[DBContract.SCORE.SC_MATH]));
				scoreVO.setMusic(Integer.valueOf(scores[DBContract.SCORE.SC_MUSIC]));
				
				scoreList.add(scoreVO);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("성적 정보 파일을 여는 데 실패했습니다.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("성적 정보 파일을 읽는 데 실패했습니다.");
		}
	}
	
	//return type을 int(Primitive)가 아닌 Integer(Wrapper Class)로 설정
	//sc_score(매개변수)로 전달받은 값을 검사하는 코드
	//END 문자열을 입력 받으면 -1 return
	//숫자로 형변환이 불가능한 문자열, 혹은 점수 범위를 벗어나는 값이 입력되면 null return
	//정상적인 값이 입력되면 문자열을 정수로 형변환하여 return
	private Integer scoreCheck(String sc_score) {
		//입력 종료 키워드를 입력하면 -1 return
		if(sc_score.equals("END")) {
			return -1; //boolean false
		}
		/*
		 * int intScore=null; : 오류 발생
		 * 		primitive int형 변수는 null 값으로 clear, 초기화시킬 수 없음
		 * Integer intScore=null; : 정상 코드
		 * 		Wrapper class Integer형 변수는 null 값으로 clear, 초기화 가능
		 */
		Integer intScore=null; //null : 아무것도 아닌. 객체(primitive valuable 불가)를 clear시키는 용도
		try {
			intScore=Integer.valueOf(sc_score);
		} catch (Exception e) {
			System.out.println("점수는 숫자로만 입력할 수 있습니다.");
			System.out.println("입력한 문자열 : "+sc_score);
			return null; //정상적인 값이 입력되지 않으면 null return
		}
		if(intScore<0||intScore>100) {
			System.out.println("점수는 0부터 100까지 입력할 수 있습니다.\n다시 입력하십시오.");
			return null;
		}
		
		return intScore;
	}

	@Override
	public boolean inputScore() {
		ScoreVO scoreVO=new ScoreVO();
		
		System.out.print("학번(입력 종료 : END)>> ");
		String st_num=scan.nextLine();
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
		st_num=String.format("%05d", intNum);
		
		for(ScoreVO scoVO : scoreList) {
			if(scoVO.getNum().equals(st_num)) {
				System.out.println(st_num+"번 학생의 성적이 이미 등록되어 있습니다.");
				return true;
			}
		}
		
		//학생 정보에서 학번이 등록되어 있는지 확인(중복 검사)
		/*for(StudentVO stVO:studentList) {
			if(stVO.getNum().equals(st_num)) {
				System.out.println(st_num+"번의 학생 정보가 존재하지 않습니다.");
				System.out.println("성적을 입력할 수 없습니다.");
				return true;
			}
		}*/
		StudentVO retVO=stService.getStudent(st_num);
		if(retVO==null) {
			System.out.println(st_num+"번의 학생 정보가 존재하지 않습니다.");
			System.out.println("성적을 입력할 수 없습니다.");
			return true;
		}
		
		scoreVO.setNum(st_num);
		
		for(int i=0; i<strSubjects.length; i++) {
			System.out.printf("%s 점수(입력 종료 : END)>> ", strSubjects[i]);
			String sc_score=scan.nextLine();
			//intScore -1, null, 숫자 값이 담기게 된다
			Integer intScore=this.scoreCheck(sc_score);
			if(intScore==null) { //입력값이 오류일 경우
				//만약 입력한 점수에 오류 발생 시(문자열, 범위 오류) for() 반복문의 i값을 1 감소시켜 다시 반복문을 실행
				//국어 점수 입력하는 단계에서 발생한다면 국어 접수를 입력받는 화면이 계속 반복됨
				i--;
				continue; //정확한 값을 입력할 때까지 코드 반복
			} else if(intScore<0) {
				return false;
			}
			//정상 값이 입력되면 점수 배열에 값 저장
			intScores[i]=intScore;
		}
		
		scoreVO.setKor(intScores[0]);
		scoreVO.setEng(intScores[1]);
		scoreVO.setMath(intScores[2]);
		scoreVO.setMusic(intScores[3]);
		
		scoreList.add(scoreVO);
		this.saveScoreVO(scoreVO); //1명의 데이터를 추가 및 저장
		
		return true;
	}

	@Override
	public void saveScore() {

	}

	@Override
	public void scoreList() {
		//과목별 평균과 총점 계산 배열을 0으로 clear
		Arrays.fill(totalSum, 0);
		Arrays.fill(totalAvg, 0);
		
		System.out.println("<성적 일람표>");
		System.out.println(Lines.dLine);
		System.out.println("학번\t이름\t국어\t영어\t수학\t음악\t총점\t평균");
		System.out.println(Lines.sLine);
		
		for (ScoreVO scoreVO : scoreList) {
			System.out.printf("%s\t", scoreVO.getNum());
			StudentVO retVO=stService.getStudent(scoreVO.getNum());
			String st_name="[없음]";
			if(retVO!=null) {
				st_name=retVO.getName();
			}
			System.out.printf("%s\t", st_name);
			System.out.printf("%d\t", scoreVO.getKor());
			System.out.printf("%d\t", scoreVO.getEng());
			System.out.printf("%d\t", scoreVO.getMath());
			System.out.printf("%d\t", scoreVO.getMusic());
			System.out.printf("%d\t", scoreVO.getSum());
			System.out.printf("%5.2f\n", scoreVO.getAvg());
			
			totalSum[0]+=scoreVO.getKor();
			totalSum[1]+=scoreVO.getEng();
			totalSum[2]+=scoreVO.getMath();
			totalSum[3]+=scoreVO.getMusic();
		}
		
		System.out.println(Lines.sLine);
		System.out.print("과목총점\t");
		int sumAndSum=0;
		for(int sum : totalSum) {
			System.out.printf("%s\t", sum);
			sumAndSum+=sum;
		}
		System.out.printf("%s", sumAndSum);
		System.out.println();
		System.out.print("과목평균\t");
		float avgAndAvg=0f;
		for(int sum : totalAvg) {
			float avg=(float)sum/totalSum.length;
			System.out.printf("%5.2f\t", avg);
			avgAndAvg+=avg;
		}
		System.out.printf("\t%5.2s\n", (float)avgAndAvg/totalSum.length);
		System.out.println(Lines.dLine);
	}

	@Override
	public void saveScoreVO(ScoreVO scoreVO) {
		FileWriter fileWriter=null;
		PrintWriter pWriter=null;
		
		try {
			fileWriter=new FileWriter(this.fileName, true);
			pWriter=new PrintWriter(fileWriter);
			
			pWriter.printf("%s:", scoreVO.getNum());
			pWriter.printf("%d:", scoreVO.getKor());
			pWriter.printf("%d:", scoreVO.getEng());
			pWriter.printf("%d:", scoreVO.getMath());
			pWriter.printf("%d\n", scoreVO.getMusic());
			
			pWriter.flush();
			pWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void calcSum() {
		for(ScoreVO scoreVO : scoreList) {
			int sum=scoreVO.getKor()+scoreVO.getEng()+scoreVO.getMath()+scoreVO.getMusic();
			
			scoreVO.setSum(sum);
		}
	}

	@Override
	public void calcAvg() {
		for (ScoreVO scoreVO : scoreList) {
			float avg=(float)scoreVO.getSum()/4;
			
			scoreVO.setAvg(avg);
		}
	}

}