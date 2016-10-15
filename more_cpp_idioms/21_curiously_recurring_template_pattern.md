# Curiously Recurring Template Pattern

---
## 의미
파생 클래스를 템플릿 인자로써 사용하는 기본 클래스를 위한 이디엄입니다.

## 다른 이름
- CRTP
- Mixin-from-above

## 동기
타입 의존성을 제거하지만 기본 클래스에서 함수성을 커스터마이징할 수 있고, 인터페이스/속성/행동을 파생 클래스에 혼합할 수 있도록 파생 클래스 커스터마이징 합니다.

## 솔루션과 샘플코드
CRTP 이디엄에서, 클래스 T는 T에 특성화된 템플릿을 상속받습니다.

```
class T : public X<T> {…};
```

이 템플릿은 오직 X<T>의 크기가 T에 독립적으로 결정될 수 있을 때만 유효합니다.
전형적으로, 멤버함수의 정의는 선언된지 한참 후에도 인스턴스화 되지 않는다는 사실을 활용하며, 파생 클래스의 멤버함수를 자신의 멤버 함수 안에서 static_cast를 통해 사용합니다. 예를 들면:

```
  template <class Derived>
  struct base
  {
      void interface()
      {
          // ...
          static_cast<Derived*>(this)->implementation();
          // ...
      }

      static void static_interface()
      {
          // ...
          Derived::static_implementation();
          // ...
      }

      // The default implementation may be (if exists) or should be (otherwise)
      // overridden by inheriting in derived classes (see below)
      void implementation();
      static void static_implementation();
  };

  // The Curiously Recurring Template Pattern (CRTP)
  struct derived_1 : base<derived_1>
  {
      // This class uses base variant of implementation
      //void implementation();

      // ... and overrides static_implementation
      static void static_implementation();
  };

  struct derived_2 : base<derived_2>
  {
      // This class overrides implementation
      void implementation();

      // ... and uses base variant of static_implementation
      //static void static_implementation();
  };
```

## 알려진 예
- Barton-Nackman trick

## 관련 된 이디엄
- Parameterized Base Class Idiom
- Barton-Nackman trick

## 참고
- Curiously Recurring Template Pattern on Wikipedia

---

## 개인적인 의견
코드를 완성해서 테스트 해봤다.

```
#include <iostream>
using namespace std;

template <class Derived>
struct base {
	void interface() {
		static_cast<Derived*>(this)->implementation();
	}

	static void static_interface() {
		Derived::static_implementation();
	}

	void implementation() {
		cout << "Base's implementation()" << endl;
	}
	static void static_implementation() {
		cout << "Base's static_implementation()" << endl;
	}
};

struct derived_1 : base<derived_1> {
	static void static_implementation() {
		cout << "Derived's static_implementation()" << endl;
	}
};

struct derived_2 : base<derived_2> {
	void implementation() {
		cout << "Derived's implementation()" << endl;
	}
};

int main() {

	cout << "derived_1" << endl;
	derived_1 d1;
	d1.interface();
	d1.static_implementation();

	cout << endl << "derived_2" << endl;
	derived_2 d2;
	d2.interface();
	d2.static_implementation();

	return 0;
}
```

사실상 동작은 가상함수의 동작과 같다.
하지만 이 이디엄이 의미가 있는 것은 **정적함수도 가상함수 처럼 동작** 시킬 수 있다는 것이다. (정적함수는 가상함수로 사용할 수 없다 == static virtual 키워드가 불가능 하다)

```
#include <iostream>
using namespace std;

template <typename T>
class base {
public:
	void foo() {
		static_cast<T*>(this)->fooImpl();
	}

protected:
	base() {}
	~base() {}
private:
	base(const base&);
	base& operator=(const base&);
};

class derived_1 :public base<derived_1> {
private:
	friend base<derived_1>;
	static void fooImpl() {
		cout << "derved_1 foo" << endl;
	}
};


class derived_2 :public base<derived_2> {
private:
	friend base<derived_2>;
	static void fooImpl() {
		cout << "derved_2 foo" << endl;
	}
};

int main() {
	derived_1 d1;
	d1.foo();

	derived_2 d2;
	d2.foo();

	return 0;
}
```

fooImpl()을 캡슐화 하기 위해서 friend를 사용한 것은 찝찝하지만 (DIP 위반) fooImpl이 멋대로 노출 되는것을 원하지 않는다면 어쩔 수 없는 듯 하다.
만약 상관 없다면 fooImpl()을 public으로 바꾸고 불필요해진 freind 부분을 삭제하면 된다.