# Pimpl (Handle Body, Compilation Firewall, Cheshire Cat)
Pointer To Implementation (pImpl), "opaque pointer"라고 불리기도 하는 pImpl 이디엄은, 클래스의 구현을 추상화하는 방법을 제공합니다.<br>

C++에서는 클래스 정의 안에 멤버 변수를 선언해야 합니다. 따라서 클래스에는 적당한 크기의 메모리공간이 할당되어야만 하는데, 이는 구현부의 추상화가 불가능 함을 의미합니다.
반면, 구현 클래스를 가리키는 포인터를 이용하면, 추가적인 포인터의 역참조와 함수호출의 비용만으로 클래스를 추상화할 수 있습니다.

```
class Book
{
public:
  void print();
private:
  std::string  m_Contents;
}
```

이 Book클래스를 사용하는 누군가는 print() 함수에 대해서만 알면 되지만, 만약 Book 클래스에 몇가지를 추가하기를 원한다면 어떤일이 생길까요.

```
class Book
{
public:
  void print();
private:
  std::string  m_Contents;
  std::string  m_Title;
}
```

이제 Book 클래스를 사용하는 모든 유저는, 객체의 크기가 알고 있던 것 보다 증가했으므로 재컴파일 해야만 print()함수를 호출할 수 있습니다.
pImple은 아래 패턴을 구현하여 위 문제를 해결합니다.

```
/* public.h */
class Book
{
public:
  Book();
  ~Book();
  void print();
private:
  class BookImpl;
  BookImpl* m_p;
}
```

그리고 분리된 헤더에서

```
/* private.h */
#include "public.h"
#include <iostream>
class Book::BookImpl
{
public:
  void print();
private:
  std::string  m_Contents;
  std::string  m_Title;
}
The body of the Book class then would look something like

Book::Book()
{
  m_p = new BookImpl();
}

Book::~Book()
{
  delete m_p;
}

void Book::print()
{
  m_p->print();
}

/* then BookImpl functions */

void Book::BookImpl::print()
{
  std::cout << "print from BookImpl" << std::endl;
}
```

메인 함수들로부터 호출됩니다.

```
int main()
{
  Book *b = new Book();
  b->print();
  delete b;
}
```

또한, std::unique_ptr<BookImpl>와 같은 것들을 통해서 내부 포인터를 사용할 수 있습니다.
