TravelRecordApp
모바일프로그래밍 기말 프로젝트로 제작하는 여행 계획 및 기록 앱입니다.

프로젝트 개요
한 학기 동안 학습한 Android 핵심 기술을 실제 앱으로 구현하는 개인 프로젝트입니다.

주제는 여행 계획 및 기록 앱이며 사용자가 다녀온 여행지를 기록, 조회, 수정, 삭제하고 사진과 메모를 함께 관리할 수 있도록 구현할 예정입니다.

제출 정보
- 제출 방식: `.apk` 파일 및 개인 GitHub Repository URL 제출
- 제출 기한: 2026년 6월 15일 오후 11:59
- 최소 SDK: API 26 이상
- 개발 언어: Kotlin
- 개발 환경: Android Studio

필수 구현 기술 요소
- Fragment 최소 2개 이상 사용
- Fragment 전환 및 백스택 관리 포함
- RecyclerView 활용
- RecyclerView 항목 클릭 이벤트 처리
- SQLite를 이용한 여행 기록 CRUD 전체 구현
- SQLiteOpenHelper를 상속한 DBHelper 클래스 직접 구현
- 기록 추가 및 수정 화면을 별도 Activity로 구현
- 카메라 촬영 또는 갤러리 선택 Intent 구현
- 옵션 메뉴 항목 2개 이상 구현
- 컨텍스트 메뉴 1개 이상 구현
- 삭제 시 AlertDialog 확인창 구현

추가 구현 가산점 목표
- Google Map API를 활용한 지도 화면 구현
- 여행 기록 위치를 지도 마커로 표시
- 사진 GPS 정보 추출 후 지도 마커 생성
- 스레드 또는 코루틴을 활용한 비동기 처리
- 이미지 로딩 시 ProgressBar 적용

- 2026.06.06: 여행 기록 상세 화면 구현 작업 진행