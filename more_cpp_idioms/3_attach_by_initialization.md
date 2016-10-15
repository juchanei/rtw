# Attach by Initialization

---
## 의미
사용자 정의 객체를 프로그램이 실행되기 전에 프레임워크에 붙입니다.

## 다른 이름
- Static-object-with-constructor

## 동기
GUI 프레임워크와 object-request broker 같은, 특정 프레임워크는 내부에 자신만의 이벤트 루프를 가지고 어플리케이션을 컨트롤합니다.
프로그래머는 자신의 어플리케이션 메인함수에 코드를 작성할 자유를 갖거나 갖지 못할 수 도 있습니다.
종종, 메인함수는 어플리케이션 프레임워크 내부 깊숙히 숨어있기도 합니다. (예를 들면 MFC의 AfxWinMain와 같은 프레임워크)
메인함수에 접근하기 어려운 프레임워크는, 프로그래머의 초기화 코드가 프레임워크의 이벤트 루프가 시작되기 전 실행되도록 하기 어렵습니다.
Attach-by-initialization 이디엄은 해당 어플리케이션의 특정 코드를 프레임워크의 이벤트 루프가 시작되기 전에 수행할 수 있도록 해줍니다.

## 솔루션과 샘플코드
C++에서는 전역 네임스페이스의 전역 객체나 정적 객체의 경우 메인함수가 실행되기 전에 초기화되도록 되어있습니다.
이러한 객체는 정적기억수명을 가진 객체로도 알려져있습니다.
이러한 정적기억수명을 가진 객체들의 특성은 메인함수에 프로그래머가 직접 코드를 작성할 수 없는 경우에 객체를 시스템에 붙일 수 있도록 합니다.
예를들면, Microsoft Foundation Classes (MFC)에서 사용되는 예제를 보세요.

```
///// File = Hello.h
class HelloApp: public CWinApp
{
public:
    virtual BOOL InitInstance ();
};
///// File = Hello.cpp

#include <afxwin.h>
#include "Hello.h"
HelloApp myApp; // Global "application" object
BOOL HelloApp::InitInstance ()
{
  m_pMainWnd = new CFrameWnd();
  m_pMainWnd->Create(0,"Hello, World!!");
  m_pMainWnd->ShowWindow(SW_SHOW);
  return TRUE;
}
```

위 예제는 "Hello, World!"라는 이름의 윈도우를 만드는 것 외의 아무 동작도 하지 않습니다.
우리가 여기서 알아야하는 것은, HeloApp 타입의 myApp라는 객체가 전역에 있다는 것입니다.
myApp 객체는 메인함수가 실행되기 전에 디폴트로 초기화 됩니다.
해당 객체가 초기화 되는것에 부수적으로, CWinApp의 생성자 역시 호출됩니다.
CWinApp 클래스는 프레임워크의 일부이며, 프레임워크에 속한 클래스 생성자 몇개를 호출합니다.
이러한 생성자들이 실행되는 동안, 전역객체는 프레임워크에 붙습니다.
나중에, 그 객체는 MFC의 메인 함수 역할을 하는 AfxWinMain 함수에 의해 회수됩니다.
HelloApp::InitInstance의 멤버 함수는 완전성을 위해 보여질 뿐, 이 이디엄의 필수적인 부분은 아닙니다.
이 함수는 AfxWinMain이 실행되기 시작한 이후에 호출 됩니다.<br>

전역이고 정적인 객체는 여러가지 방법으로 초기화될 수 있습니다: 기본생성자, 파리미터를 가진 생성자, 어떤 함수의 리턴값으로 할당, 동적 초기화 등...<br>

> **경고**<br>
C++에서, 같은 컴파일 단위에 있는 객체들은 정의된 순서대로 생성됩니다.
하지만, 정적기억수명을 가진 객체들의 초기화 순서는 다른 컴파일 단위에서는 정의되어있지 않습니다.
한 네임스페이스 안의 객체들은, 해당 네임스페이스의 어떠한 함수/변수가 접근되기 전에 생성되도록 되어있습니다.
이는 메인함수가 시작되기 전 일수도 아닐수도 있습니다.
소멸 순서는 초기화 순서의 반대지만, 초기화 순서 자체가 표준화되어 있지 않습니다.
이러한 정의되지 않은 행동 때문에, 한 정적 객체의 생성자가 다른 정적 객체를 사용하려고 하지만 아직 초기화 되지 않은 경우 문제를 일으킵니다.
이 이디엄은 정적기억수명을 가진 객체에 의존하기 때문에 이러한 문제에 걸리기 쉽도록 만들어져 있습니다.

## 알려진 예
- Microsoft Foundation Classes (MFC)

## 참고
- Proposed C++ language extension to improve portability of the Attach by Initialization idiom
