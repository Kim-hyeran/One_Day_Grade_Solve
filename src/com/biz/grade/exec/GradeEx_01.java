package com.biz.grade.exec;

import java.util.Scanner;

import com.biz.grade.config.DBContract;
import com.biz.grade.config.Lines;
import com.biz.grade.service.ScoreService;
import com.biz.grade.service.ScoreServiceImplV1;
import com.biz.grade.service.StudentService;
import com.biz.grade.service.StudentServiceImplV1;

public class GradeEx_01 {
	
	public static void main(String[] args) {
		
		Scanner scan=new Scanner(System.in);
		StudentService stService=new StudentServiceImplV1();
		ScoreService scService=new ScoreServiceImplV1();
		
		stService.loadStudent();
		scService.loadScore();
		
		while(true) {
			System.out.println("\t<빛고을 대학 학사 관리 시스템 v1>");
			System.out.println(Lines.dLine);
			System.out.println("1. 학생 정보 등록");
			System.out.println("2. 학생 정보 출력");
			System.out.println("3. 성적 등록");
			System.out.println("4. 성적 일람표 출력");
			System.out.println(Lines.sLine);
			System.out.println("QUIT. 업무 종료");
			System.out.println(Lines.dLine);
			
			System.out.print("업무 선택>> ");
			String stMenu=scan.nextLine();
			if(stMenu.equals("QUIT")) {
				break;
			}
			
			int intMenu=0;
			try {
				intMenu=Integer.valueOf(stMenu);
			} catch (Exception e) {
				System.out.println("메뉴는 숫자로만 선택할 수 있습니다.");
				continue;
			}
			
			if(intMenu==DBContract.MENU.학생정보등록) {
				while(true) {
					if(!stService.inputStudent()) {
						break;
					}
				}
			} else if (intMenu==DBContract.MENU.학생목록출력) {
				stService.studentList();
			} else if (intMenu==DBContract.MENU.성적등록) {
				while(scService.inputScore());
				/* 학생정보 등록 시 사용하는 while문과 같은 코드
				 * 1. inputScore() 호출, 코드 수행
				 * 2-1. true를 return하면 무한 반복
				 * 2-2. false를 return하면 break
				 */
			} else if (intMenu==DBContract.MENU.성적일람표) {
				//총점과 평균 계산을 성적일람표 출력 전 총점과 평균 계산 method호출
				scService.calcSum();
				scService.calcAvg();
				scService.scoreList();
			}
		}
		System.out.println("업무 종료");
		
	}

}
