# Boost Startup
---
## Download
- www.boost.org 에서 boost_<version>.zip을 다운받습니다.
- *c:\\Program Files\\boost\\boost_<version>* 에 압축을 풉니다.

## Building
- 해당 경로의 *bootstrap.bat* 을 실행합니다.
- 조금 기다리면 *b2.exe*, *bjam.exe* 파일이 생성된다. 여기서 *b2.exe* 를 실행하여 빌드합니다.
- 빌드가 다 되면 *stage* 디렉토리와 내부에 *lib* 파일이 생성됩니다.

## Visual Studio Library Setting
- Solution Explorer에서 프로젝트명을 오른쪽 클릭하여 Properties를 선택합니다.
- *Configuration Properties > C/C++ > General > Additional Include Directories* 에 *c:\\Program Files\\boost\\boost_<version>* 를 입력합니다.
- *Configuration Properties > C/C++ > Precompiled Headers* 에서 *Use Precompiled Header (/Yu)* 를 *Not Using Precompiled Headers* 로 바꿉니다.
- *Configuration Properties > Linker > General > Additional Library Directories* 에 *c:\\Program Files\\boost\\boost_<version>\\stage\\lib* 을 입력합니다.
