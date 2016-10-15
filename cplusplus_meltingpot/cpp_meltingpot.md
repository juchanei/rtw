# C++ Meltingpot

---
## Visual Studio 2015
잘 모르는 Visual Studio 2015의 기능

- nuget
- single file inteliscence
- PdbProject for Visual Studio 2015 (extension)
- cntl + Q
- cntl + ,
- cntl + shift + F
- formatting
- alt + shift + up/down
- cntl + .
- cntl + K + O
- cntl + R + M
- cntl + R + R
- C++ Quick Fixes (extension)
- shift + {}/()/[]

---
## C++17 Key Features Summary

### Major Features
#### Module
C++는 #include를 통해 헤더파일을 포함하게되는데, 이때 포함하고 싶지 않은 기능도 모두 포함해야만 하는 문제가 있다.
이러한 문제를 Module의 등장으로 해결할고 한다.

- module
  모듈 지정
- import
  가져올 모듈 이름 지정
- export
  내보낼 모듈 이름 지정

#### Concept
템플릿 매개변수에 제약을 건다.

- Basuc
- Library-wide
- Iterator
- ...

#### Ranges
생략

### Core Features
#### Fold expression
C++11 가변템플릿을 이용해 표현식을 간단하게 구현한다.

#### Attribute
java의 annotation과 비슷한 기능<br>

**C++14**
- noreturn
- carries_dependency
- deprecated

**C++17**
- falltrough
- nodiscard
- maybe_unused

#### Constexpr Lambda
  Constexpr : C++11/14, 메타프로그래밍, 변수의 값을 컴파일타임에 처리<br>
  Lambda : C++11<br>
  Constexpr Lambda : 상수표현식 + 람다, 컴파일타임에 처리되는 람다

#### Call Syntax
멤버함수의 동치되는 함수를 지원, ex) swap<br>
.도 이제 연산자 오버로딩이 가능

#### Nested namespace
네임스페이스를 중첩해서 사용 가능

```
namespace A::B::C {
  //...
}
```

#### Contract
포인터에 접근하기 전에 assert()를 이용해 예외처리하는 경우가 많음, 다소 직관성이 떨어짐<br>
사전조건과 사후조건을 추가해 assert()처럼 특정 조건을 만족하는지 검사할 수 있다.

- 사전조건 : expects
- 사후조건 : ensures

상속 시 사전조건이 강해지거나 약해지면 에러가 발생

#### static_assert
컴파일 타임에 assert를 처리

### Library Features
#### Parallel STL
병렬적인 STL처리를 지원

#### any
Boost.any 기반의 라이브러리, 모든 종류의 정보를 저장(복사생성자를 지원하는 경우만)

#### optional
Boost.optional 기반 라이브러리, 함수에서 "유효하지 않은 값"을 리턴하기 위해 만들어짐

#### filesystem
Boost.filesystem 기반 라이브러리, 파일/경로/디렉터리 관련된 작업을 수행할 수 있음

#### asio
Boost.asio 기반 라이브러리, 네트워크 및 비동기 프로그래밍 관련 라이브러리

### Summary
- 아직 표준화 작업이 진행중
- https://www.github.com/utilForever

---
## Ranges for The C++ Standard Library
모든 STL에 대해서 범위기반 라이브러리를 제공, 범위를 다루는 Generic Library, 차기 C++포함 예정
https://github.com/ericniebler/range-v3

### 이점
- 편의성
  코드가 빨라진다
- Lazy Evaluation
- 조합성

---
## C++ String 파고들기
ICU를 써라.

---
## 기타
발표자료는 페이스북 페이지 혹은 메일로 제공됩니다.
