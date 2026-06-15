# TravelRecordApp

## 프로젝트 정보
* 프로젝트명: TravelRecordApp
* 과목: 모바일프로그래밍 기말 프로젝트
* 개발자: 컴퓨터소프트웨어공학과 20233684 이주현
* 개발 환경: Android Studio, Kotlin, XML
* 데이터베이스: SQLiteOpenHelper
* 최소 SDK: API 30

## 프로젝트 소개
TravelRecordApp은 여행 장소, 방문 날짜, 메모, 사진, 위치 정보를 저장하고 관리할 수 있는 여행 기록 앱이다. 사용자는 여행 기록을 추가하고, 목록에서 확인하며, 상세 화면에서 내용을 수정하거나 삭제할 수 있다. 또한 Google Map API를 활용하여 여행 위치를 지도에 저장하고 마커로 확인할 수 있다.

## 주요 기능
### 1. 홈 화면
* 전체 여행 기록 수 표시
* 최근 여행 기록 표시
* 기록 탭과 지도 탭으로 이동하는 버튼 제공

### 2. 여행 기록 관리
* 여행 기록 추가
* 여행 기록 목록 조회
* 여행 기록 상세 조회
* 여행 기록 수정
* 여행 기록 삭제
* 전체 여행 기록 삭제
* 최신순/오래된순 정렬

### 3. 사진 선택 기능
* 갤러리 Intent를 활용한 사진 선택
* 선택한 사진 URI 저장
* 목록 화면과 상세 화면에 사진 표시

### 4. 지도 기능
* Google Map API 연결
* 실제 Google Map 화면 표시
* 지도 길게 누르기를 통한 위치 저장
* 기존 여행 기록에 위치 정보 저장
* 지도에서 저장된 위치를 마커로 표시
* 상세 화면에서 해당 기록 위치를 지도에서 확인

### 5. 통계 기능
* 총 여행 기록 수 표시
* 사진이 포함된 기록 수 표시
* 위치 정보가 포함된 기록 수 표시
* 최근 기록 정보 표시

### 6. 일정 기능
* CalendarView를 이용한 날짜 선택
* TimePicker를 이용한 시간 선택
* 예정 여행지 저장
* 저장된 일정 확인 및 초기화

## 사용 기술
* Kotlin
* XML Layout
* Fragment
* Activity
* RecyclerView
* SQLiteOpenHelper
* Intent
* AlertDialog
* PopupMenu
* Context Menu
* CalendarView
* TimePicker
* Google Maps SDK for Android
* Thread 기반 로딩 처리

## 구현 조건 반영
* Fragment 2개 이상 사용
* RecyclerView 사용
* SQLite CRUD 구현
* 옵션 메뉴 2개 이상 구현
* 컨텍스트 메뉴 구현
* AlertDialog 사용
* 갤러리 사진 선택 Intent 사용
* Google Map API 활용
* 기록 추가/수정 화면 Activity 분리
* Room, Firebase, Retrofit 미사용

## 주의 사항
Google Maps API Key는 `local.properties` 파일에 저장하였고, GitHub에는 업로드하지 않았다.
`AndroidManifest.xml`에서는 `${MAPS_API_KEY}` placeholder를 통해 API Key를 참조한다.

## 제출 정보
* 제출 파일: APK 파일
* 제출 링크: GitHub Repository URL