# Algebraic Hierarchy

---
## 의미
밀접한 관계를 가진 여러개의 '대수적 추상'을 하나의 '일반적인 추상' 뒤로 숨김과 동시에, 일반적인 인터페이스를 제공하기 위해 사용합니다.

## 다른 이름
- **State** (Gamma et al.)

## 동기
스몰토크와 같이 순수한 객체 지향 언어에서는, 변수들이 객체에 런타임 바인딩 되어 라벨처럼 동작합니다.
변수를 객체에 바인딩 하는 것은 라벨을 붙이는 것과 같습니다.
이러한 언어들에서, 할당은 한 객체에서 라벨을 떼어내고 다른 곳에 붙이는 것과 유사합니다.
반면 C와 C++에서는, 변수는 객체의 라벨보다는 주소의 동의어이거나 오프셋을 의미합니다.
할당은 라벨을 다시 붙이는 것이 아닌, 오래된 컨텐츠를 새로운 컨텐츠에 덮어 쓰는 것을 의미합니다.
**Algebraic Hierarchy idiom uses delegated polymorphism to simulate weak variable to object binding in C++.**
Algebraic Hierarchy는 이를 구현하기 위해 Envelope Letter 이디엄을 사용합니다.
이 이디엄의 아래와 같은 코드를 작성하는데 사용됩니다.

```
Number n1 = Complex (1, 2); // Label n1 for a complex number
Number n2 = Real (10); // Label n2 for a real number
Number n3 = n1 + n2; // Result of addition is labelled n3
Number n2 = n3; // Re-labelling
```

## 솔루션과 샘플코드
아래 코드는 Algebraic Hierarchy 이디엄을 구현한 것입니다.
```
#include <iostream>

struct BaseConstructor { BaseConstructor(int=0) {} };

class RealNumber;
class Complex;
class Number;

class Number
{
    friend class RealNumber;
    friend class Complex;

  public:
    Number & operator = (const Number &n);
    Number (const Number &n);
    virtual ~Number();

    virtual Number operator + (Number const &n) const;
    void swap (Number &n) throw ();

    static Number makeReal (double r);
    static Number makeComplex (double rpart, double ipart);

  protected:
    Number ();
    Number (BaseConstructor);

  private:
    void redefine (Number *n);
    virtual Number complexAdd (Complex const &n) const;
    virtual Number realAdd (RealNumber const &n) const;

    Number *rep;
    short referenceCount;
};

class Complex : public Number
{
  friend class RealNumber;
  friend class Number;

  Complex (double d, double e);
  Complex (const Complex &c);
  virtual ~Complex ();

  virtual Number operator + (Number const &n) const;
  virtual Number realAdd (RealNumber const &n) const;
  virtual Number complexAdd (Complex const &n) const;

  double rpart, ipart;
};

class RealNumber : public Number
{
  friend class Complex;
  friend class Number;

  RealNumber (double r);
  RealNumber (const RealNumber &r);
  virtual ~RealNumber ();

  virtual Number operator + (Number const &n) const;
  virtual Number realAdd (RealNumber const &n) const;
  virtual Number complexAdd (Complex const &n) const;

  double val;
};

/// Used only by the letters.
Number::Number (BaseConstructor)
: rep (0),
  referenceCount (1)
{}

/// Used by static factory functions.
Number::Number ()
  : rep (0),
    referenceCount (0)
{}

/// Used by user and static factory functions.
Number::Number (const Number &n)
: rep (n.rep),
  referenceCount (0)
{
  std::cout << "Constructing a Number using Number::Number" << std::endl;
  if (n.rep)
    n.rep->referenceCount++;
}

Number Number::makeReal (double r)
{
  Number n;
  n.redefine (new RealNumber (r));
  return n;
}

Number Number::makeComplex (double rpart, double ipart)
{
  Number n;
  n.redefine (new Complex (rpart, ipart));
  return n;
}

Number::~Number()
{
  if (rep && --rep->referenceCount == 0)
    delete rep;
}

Number & Number::operator = (const Number &n)
{
  std::cout << "Assigning a Number using Number::operator=" << std::endl;
  Number temp (n);
  this->swap (temp);
  return *this;
}

void Number::swap (Number &n) throw ()
{
  std::swap (this->rep, n.rep);
}

Number Number::operator + (Number const &n) const
{
  return rep->operator + (n);
}

Number Number::complexAdd (Complex const &n) const
{
  return rep->complexAdd (n);
}

Number Number::realAdd (RealNumber const &n) const
{
  return rep->realAdd (n);
}

void Number::redefine (Number *n)
{
  if (rep && --rep->referenceCount == 0)
    delete rep;
  rep = n;
}

Complex::Complex (double d, double e)
  : Number (BaseConstructor()),
    rpart (d),
    ipart (e)
{
  std::cout << "Constructing a Complex" << std::endl;
}

Complex::Complex (const Complex &c)
  : Number (BaseConstructor()),
    rpart (c.rpart),
    ipart (c.ipart)
{
  std::cout << "Constructing a Complex using Complex::Complex" << std::endl;
}

Complex::~Complex()
{
  std::cout << "Inside Complex::~Complex()" << std::endl;
}

Number Complex::operator + (Number const &n) const
{
  return n.complexAdd (*this);
}

Number Complex::realAdd (RealNumber const &n) const
{
  std::cout << "Complex::realAdd" << std::endl;
  return Number::makeComplex (this->rpart + n.val,
                              this->ipart);
}

Number Complex::complexAdd (Complex const &n) const
{
  std::cout << "Complex::complexAdd" << std::endl;
  return Number::makeComplex (this->rpart + n.rpart,
                              this->ipart + n.ipart);
}

RealNumber::RealNumber (double r)
  : Number (BaseConstructor()),
    val (r)
{
  std::cout << "Constructing a RealNumber" << std::endl;
}

RealNumber::RealNumber (const RealNumber &r)
  : Number (BaseConstructor()),
    val (r.val)
{
  std::cout << "Constructing a RealNumber using RealNumber::RealNumber" << std::endl;
}

RealNumber::~RealNumber()
{
  std::cout << "Inside RealNumber::~RealNumber()" << std::endl;
}

Number RealNumber::operator + (Number const &n) const
{
  return n.realAdd (*this);
}

Number RealNumber::realAdd (RealNumber const &n) const
{
  std::cout << "RealNumber::realAdd" << std::endl;
  return Number::makeReal (this->val + n.val);
}

Number RealNumber::complexAdd (Complex const &n) const
{
  std::cout << "RealNumber::complexAdd" << std::endl;
  return Number::makeComplex (this->val + n.rpart, n.ipart);
}

namespace std
{
  template <>
  void swap (Number & n1, Number & n2)
  {
    n1.swap (n2);
  }
}

int main (void)
{
  Number n1 = Number::makeComplex (1, 2);
  Number n2 = Number::makeReal (10);
  Number n3 = n1 + n2;

  std::cout << "Finished" << std::endl;

  return 0;
}
```

## 관련 된 이디엄
- Handle Body
- Envelope Letter

## 참고
Advanced C++ Programming Styles and Idioms by James Coplien, Addison Wesley, 1992.
