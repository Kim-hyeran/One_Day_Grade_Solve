package com.biz.grade.config;

public class DBContract {
	
	public static class STUDENT {
		public static final int ST_NUM=0; //학번
		public static final int ST_NAME=1; //학생 이름
		public static final int ST_DEPT=2; //학과
		public static final int ST_GRADE=3; //학년
		public static final int ST_TEL=4; //전화번호
	}
	
	public static class SCORE {
		public static final int SC_NUM=0; //학번
		public static final int SC_KOR=1; //국어점수
		public static final int SC_ENG=2; //영어점수
		public static final int SC_MATH=3; //수학점수
		public static final int SC_MUSIC=4; //음악점수
		public static final int SC_SUM=5; //총점
		public static final int SC_AVG=6; //평균
	}
	
	public static class MENU {
		public static final int 학생정보등록=1;
		public static final int 학생목록출력=2;
		public static final int 성적등록=3;
		public static final int 성적일람표=4;
	}

}
