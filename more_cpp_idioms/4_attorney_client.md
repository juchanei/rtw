# Attorney-Client

---
## 의미
한 클래스의 세부 구현에 접근하는 권한을 선택적으로 컨트롤합니다.

## 동기
C++의 friend 선언은 한 클래스 내부에 완벽히 접근할 수 있도록 해줍니다.
friend 선언은 그러나 조심스럽게 만들어진 캡슐화를 망가뜨려 인상을 찌푸리게 합니다.
C++의 friend 기능은 private 멤버의 일부에만 선택적으로 접근권한을 주는 기능은 제공하지 않습니다.
C++의 friend는 all-or-nothing 만을 제안합니다. 예를 들어, Foo라는 클래스가 선언되어있고 Bar클래스의 friend 라고 합시다.
따라서 Bar 클래스는 Foo 클래스의 모든 private 멤버에 대한 접근 권한을 가지고 있습니다.
이는 두 클래스간의 커플링을 증가시키기 때문에 바람직한 상황이 아닙니다.
Bar 클래스는 Foo 클래스 없이는 배포될 수 없습니다.

```
class Foo
{
private:
  void A(int a);
  void B(float b);
  void C(double c);
  friend class Bar;
};

class Bar {
// This class needs access to Foo::A and Foo::B only.
// C++ friendship rules, however, give access to all the private members of Foo.
};
```

필요할 경우 멤버의 일부분의 인터페이스를 변경할 필요가 있기 때문에, 멤버의 일부에만 선택적인 접근을 제공하는 것이 바람직합니다.
이는 두 클래스 사이에 커플링을 줄이는데 도움을 줍니다.
Attorney-Client 이디엄은 한 클래스의 friend들에 대한 접근권한을 컨트롤할 수 있도록 합니다.

## 솔루션과 샘플코드
Attorney-client 이디엄은 인다이렉션의 레벨을 추가함으로써 작동합니다.
자신의 세부사항에 대한 접근권한을 컨트롤 하고 싶은 클라이언트 클래스는, attorney를 만들고 이를 friend로 합니다.
attorney 클래스는 클라이언트에 대한 프록시를 제공합니다.
전형적인 프록시 클래스와 달리, attorney 클래스는 클라이언트가 제공하는 인터페이스의 일부분만을 복제합니다.
예를 들어, 자신의 구현 세부사항에 대한 접근권한을 컨트롤하고 싶은 Foo라는 클래스가 있다고 합시다.
더 명확히 하기 위해서 이름을 Client로 다시 짓습니다. Client는 Client::A와 Client::B만을 접근하도록 허락하는 attorney를 원합니다.

```
class Client
{
private:
  void A(int a);
  void B(float b);
  void C(double c);
  friend class Attorney;
};

class Attorney {
private:
  static void callA(Client & c, int a) {
    c.A(a);
  }
  static void callB(Client & c, float b) {
    c.B(b);
  }
  friend class Bar;
};

class Bar {
// Bar now has access to only Client::A and Client::B through the Attorney.
};
```

attorney 클래스는 응집성이 있는 함수들의 집합에 접근하는 것을 제한합니다.
attorney 클래스는 인라인 정적 멤버함수를 가지고 있고, 각각은 클라이언트의 인스턴스를 참조하며 함수 호출을 전달해줍니다.
일부는 attorney 클래스에 대해 관용적입니다.
이 클래스의 구현은 모두 private로 되어있어, 기대하지 않았던 클래스가 Client 클래스 내부 에 접근하지 못하도록 합니다.
attorney 클래스는 자신에 접근할 수 있는 다른 클래스, 멤버 함수, free 함수를 결정합니다.
이 클래스는 그것들을 friend로 선언하여 Client 클래스에 접근할 수 있도록 허용합니다.
attorney 클래스 없이는, Client 클래스는 자신의 내부를 제한없이 접근하도록 선언할 수 밖에 없습니다.

Client 클래스는 자신의 세부 구현에 접근할 수 있는 다양한 셋의 attorney 함수들을 가질 수도 있습니다.
예를 들어, AttorneyC 클래스는 Client::C 멤버 함수에만 접근을 제공합니다.
attorney 클래스가 몇개의 다른 클래스들의 중개인으써 그들에게 접근을 제공하는 경우, 흥미로운 상황이 벌어집니다.
C++에서는 friend는 상속이 불가능하기 때문에 이러한 디자인은 해당 클래스가 상속된 경우를 생각해 볼 수 있는데, private 가상 함수가 상속 된 클래스에서 재정의된 경우 기본 클래스의 private 가상 함수가 접근 가능한 경우 호출될 수 있습니다.
아래 예제를 보면, Attorney-Client 이디엄이 기본 클래스와 메인 함수 사이에 적용되어있습니다.
Derived::Func 함수는 다형성을 통해 호출됩니다.
하지만, Drived Class의 세부 구현에 접근하기 위해서 똑같은 이디엄이 적용됩니다.

```
#include <cstdio>

class Base {
private:
  virtual void Func(int x) = 0;
  friend class Attorney;
public:
  virtual ~Base() {}
};

class Derived : public Base {
private:
  virtual void Func(int x)  {
    printf("Derived::Func\n"); // This is called even though main is not a friend of Derived.
  }

public:
  ~Derived() {}
};

class Attorney {
private:
  static void callFunc(Base & b, int x) {
    return b.Func(x);
  }
  friend int main (void);
};

int main(void) {
  Derived d;
  Attorney::callFunc(d, 10);
}
```

## 알려진 예
- Boost.Iterators library
- Boost.Serialization: class boost::serialization::access

## 참고
- Friendship and the Attorney-Client Idiom (Dr. Dobb's Journal)
