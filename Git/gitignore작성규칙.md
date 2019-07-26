
# gitignore 작성 규칙


#### [github/gitignore](https://github.com/github/gitignore)



#### .gitignore 파일 규칙
- #은 주석의 역할
- 표준 glob 패턴을 사용
- / 를 사용하면 규칙이 프로젝트 전체에 적용되지 않음
- / 로 끝나는 것은 폴더로 인식
- ! 를 사용하면 무시가 되지 않음 (단, 한번 제외된 폴더의 내의 파일들은 다시 추가 할 수가 없다!!)
- *.a : 모든 .a 확장자를 가진 파일을 예외처리
- !lib.a : 위의 .a확장자에서 !lib.a 파일은 예외하지 않는다.
- /TODO : 루트의 TODO만 예외처리.
- build/ : build 폴더 내부의 모든 파일들을 예외처리.
- doc/*.txt : doc 폴더 내부의 *.txt를 예외처리.
- [Ll]ibrary/ : Library 또는 library 폴더 내부의 모든 파일들을 예외처리.