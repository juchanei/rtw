# Envelope Letter

---
##  의미
라이프타임에 걸쳐, 하나의 추상데이터타입(ADT)의 인스턴스를 여러개 구현하는 것을 지원합니다.

## 동기
Handle Body 이디엄과 (혹은) Counted Body 이디엄을 사용할 때, 하나의 커먼 시그네처를 공유하는 ADT를 여러개 구현하거나,  ADT의 인터페이스(핸들)과 시그네처를 공유하는 ADT들을 모두 구현하는 상황을 꽤 자주 만납니다.
이런 케이스에는, 핸들/바디의 클래스를 동시에 업데이트하는 새로운 오퍼레이션을 추가합니다.
만약 핸들과 바디 인터페이스의 관계를 포착하고 싶다면, envelope/letter 이디엄을 사용하면 됩니다.

## 솔루션과 샘플코드
하나의 기본 클래스로부터 여러 바디 클래스를 상속합니다.
핸들과 바디간의 시그네처 안에서 일관성을 반영하기 위해서, 바디를 대체할 기본 클래스로 핸들 클래스를 이용합니다.
핸들 클래스의 멤버를 가상함수로 만듭니다.
핸들 클래스를 상속한 구현 클래스에서 (클래스가 정의한 인터페이스와 구현의 역할에 맞게) 가상함수를 재정의 합니다.
구현 된 기본 클래스 멤버 함수들은 핸들 클래스의 기능을 정의합니다: 이는 관련 요청을 바디 클래스 인스턴스에 전달합니다.

```
class RealNumber;
class ComplexNumber;

class Number
{
public:
  virtual ~Number() {}
  virtual Number multiply (Number const &);
protected:
  Number * nptr;
};

class ComplexNumber : public Number
{
public:
  virtual Number multiply (Number const & n) {
    // ...
    if (this becomes a RealNumber number after multiplication) {
      this->nptr = new RealNumber (...); // metamorphize
      // ....
    }
    // ....
  }
};

class RealNumber : public Number
{
  // ...
};
```

ADT 인스턴스는 지금 런타임 동안 바디 클래스 사이에서 "metamorphize"를 할 수 있습니다.
예를들어, ComplexNumber는 허수부를 0으로 만드는 수학적 과정을 통해 자기자신을 RealNumber로 바꿀 수 있습니다.
이러한 동적인 타입 변화의 장점은 효율을 올립니다.
더군다나, 새로운 시그네처는 중복되지 않고 하나의 장소에만 추가되어도 됩니다.
이 이디엄은 Virtual Constructor 이디엄의 기본입니다.
Algebraic Hierarchy에서, 이 패턴이 Promotion Ladder의 기초로 사용됩니다.

## 관련 된 이디엄
- Handle Body

## 참고
- http:/ / users. rcn. com/ jcoplien/ Patterns/ C+ + Idioms/ EuroPLoP98. html#EnvelopeLetter
- Advanced C++ Programming Styles and Idioms by James Coplien, Addison Wesley, 1992.
- "Changing behavior of an object at runtime" (http:/ / stackoverflow. com/ questions/ 729905/changing-behavior-of-an-object-at-runtime) from Stack Overflow.

## 저작권
Copyright ©1998 Lucent Technologies, Bell Labs Innovations. All rights reserved. Permission granted to reprint verbatim for non-commercial use provided this copyright notice appears.

## 확장 된 예제
```
// envelope_letter.cpp
// complete example for http://en.wikibooks.org/wiki/More_C%2B%2B_Idioms/Envelope_Letter .
// Rough draft.
// FIXME: horrific memory leakage.
// FIXME: way too much coupling between classes.
// FIXME: Surely there's a way to do this without a bunch of "dynamic_cast"?
// FIXME: multiplying a complex times 0 or 0.0 returns a complex.
// Should it metamorphose to a integer or a real instead?
// 2010-06-26:DAV: cleanup.
// 2010-06-25:DAV: technically "runs", and gives numerically correct results.
// 2010-06-21:DAV: David Cary http://en.wikibooks.org/wiki/User:DavidCary started
// public domain.

#include <iostream>
// "Number" is the Envelope class
// Client code only uses the "Number" class.
// Client code never directly uses "RealNumber, "ComplexNumber", etc.
class Number {
public:
  Number();
  Number( int i );
  Number( double f );
  Number( double x, double y );
  virtual Number * clone();
  Number( const Number &copy ): letter(copy.letter->clone()) {}; // nice class
  virtual Number & operator= (const Number &rhs); // nice class
  virtual ~Number(){ // destructor
    delete letter;
    letter = 0;
  };
  virtual Number * multiply ( const Number & rhs ){
    letter = letter->multiply( rhs );
    return this;
  };
  // use Virtual Friend Function idiom,
  // since friend functions can't be made virtual.
  virtual void print(std::ostream& out) const{
    letter->print(out);
  };
  friend std::ostream& operator<<( std::ostream& out, const Number& number){
    number.print(out);
    return out;
  };
  // FIXME: should this be private or protected?
  // "letter" always points to an object of one of the child classes,
  // never to an object of the Number base class.
  Number * letter;
};
// file Number.hpp ends here
```

```
// file Number.cpp
#include "Number.hpp"
#include <cassert>
Number * Number::clone()
{
	Number * r = new Number();
	if (letter) {
		r->letter = letter->clone();
	}
	else {
		r->letter = 0;
	};
	return r;
};

Number & Number::operator= (const Number &rhs)
{
	if (this != &rhs) {
		delete letter;
		letter = 0;
		letter = rhs.letter->clone();
	};
	return *this;
}; // nice class

// RealNumber and ComplexNumber are two kinds of Letter classes
// They derive from the Envelope class Number
// in its role as the class defining the interface to the implementations.
// (Although they technically inherit a Number * letter member,
// ComplexNumber never uses it -- its letter member is always the NULL pointer).

class IntegerNumber;
class RealNumber;
class ComplexNumber;

class ComplexNumber : public Number
{
public:
	ComplexNumber(double x, double y);
	Number * multiply(const ComplexNumber & rhs);
	Number * multiply(const Number & rhs);
	void print(std::ostream& out) const
	{
		out << "(" << first_part << ", " << second_part << ")";
	};
private:
	double first_part;
	double second_part;
};

class IntegerNumber : public Number
{
public:
	IntegerNumber(int i) : the_integer(i)
	{
		std::cout << "IntegerNumber:: creating IntegerNumber." << std::endl;
	};
	IntegerNumber * clone()
	{
		IntegerNumber * r = new IntegerNumber(the_integer);
		return r;
	};
	void print(std::ostream& out) const
	{
		out << the_integer;
	};
	Number * multiply(const Number & rhs)
	{
		std::cout << "IntegerNumber:: multiply by some Number." << std::endl;
		// if letter and rhs are both Integers, letter->add returns an Integer
		// if letter is a Complex, or rhs is a Complex, what comes back is Complex
		const Number * the_letter = 0;
		if (rhs.letter) { // peel off redundant layer of abstraction
			std::cout << "rhs is an envelope -- extract the letter" << std::endl;
			the_letter = rhs.letter;
		}
		else {
			std::cout << "rhs is a letter." << std::endl;
			the_letter = &rhs;
		};
		const IntegerNumber * int_rhs = dynamic_cast<const IntegerNumber *>(the_letter);
		if (int_rhs) {
			std::cout << "IntegerNumber:: multiply by IntegerNumber." << std::endl;
			the_integer *= int_rhs->the_integer;
			return this;
		};
		// since the dynamic_cast "failed",
		// the_letter is clearly *not* an IntegerNumber.
		// Perhaps it's Real? or Complex?
		std::cout << "IntegerNumber:: metamorphizing to Complex in order to multiply..." << std::endl;
		ComplexNumber * r = new ComplexNumber(the_integer, 0.0);
		r->multiply(rhs);
		std::cout << "IntegerNumber:: finished metamorphosis and multiply by some Number..." << std::endl;
		return r;
	};
	// FIXME: shouldn't this be private?
	// private:
	int the_integer;
};

class RealNumber : public Number
{
public:
	RealNumber(double x) : real_part(x)
	{
		std::cout << "RealNumber:: creating RealNumber." << std::endl;
	};
	void print(std::ostream& out) const
	{
		out << real_part;
	};
	Number * multiply(const Number & rhs)
	{
		std::cout << "RealNumber:: multiply by some Number." << std::endl;
		// Should we try to check if the rhs is an integer or real,
		// before deferring to CompledNumber::multiply?
		std::cout << "RealNumber:: metamorphizing to Complex in order to multiply..." << std::endl;
		ComplexNumber * r = new ComplexNumber(real_part, 0.0);
		r->multiply(rhs);
		std::cout << "IntegerNumber:: finished metamorphosis and multiply by some Number..." << std::endl;
		return r;
	};
	// FIXME: shouldn't this be private?
	// private:
	double real_part;
};

ComplexNumber::ComplexNumber(double x, double y) :first_part(x), second_part(y)
{
	std::cout << "ComplexNumber:: creating ComplexNumber." << std::endl;
};

Number * ComplexNumber::multiply(const ComplexNumber & rhs)
{
	std::cout << "ComplexNumber:: multiply by ComplexNumber." << std::endl;
	double a = first_part*rhs.first_part -
		second_part*rhs.second_part;
	double b = first_part*rhs.second_part +
		second_part*rhs.first_part;
	first_part = a;
	second_part = b;
	if (0 == second_part) {
		std::cout << "ComplexNumber:: metamorphosis" << std::endl;
		return(new RealNumber(first_part)); // metamorphize
	}
	return this;
}

Number * ComplexNumber::multiply(const Number & rhs)
{
	std::cout << "ComplexNumber:: multiply by some Number." << std::endl;
	const Number * the_letter = 0;
	if (rhs.letter) { // peel off redundant layer of abstraction
		std::cout << "rhs is an envelope -- extract the letter" << std::endl;
		the_letter = rhs.letter;
	}
	else {
		std::cout << "rhs is a letter." << std::endl;
		the_letter = &rhs;
	};
	const ComplexNumber * complex_rhs = dynamic_cast<const ComplexNumber *>(the_letter);
	if (complex_rhs) {
		std::cout << "ComplexNumber:: multiply by ComplexNumber." << std::endl;
		std::cout << "ComplexNumber:: multiplying ..." << std::endl;
		return(multiply(*complex_rhs));
	};
	const IntegerNumber * integer_rhs = dynamic_cast<const IntegerNumber *>(the_letter);
	if (integer_rhs) {
		first_part *= integer_rhs->the_integer;
		second_part *= integer_rhs->the_integer;
		return this;
	};
	const RealNumber * real_rhs = dynamic_cast<const RealNumber *>(the_letter);
	if (real_rhs) {
		first_part *= real_rhs->real_part;
		second_part *= real_rhs->real_part;
		return this;
	};
	// unexpected: what else could it be?
	assert(0);
}

Number::Number() : letter(0)
{
	std::cout << "Number:: creating default Number()." << std::endl;
};

Number::Number(int i) : letter(0)
{
	std::cout << "Number:: creating IntegerNumber." << std::endl;
	letter = new IntegerNumber(i);
};

Number::Number(double f) : letter(0)
{
	std::cout << "Number:: creating RealNumber." << std::endl;
	letter = new RealNumber(f);
};

Number::Number(double x, double y) : letter(0)
{
	std::cout << "Number:: creating ComplexNumber." << std::endl;
	letter = new ComplexNumber(x, y);
};

/* Example client code: */
void print_it(Number &x)
{
	std::cout << "value: " << x << std::endl;
};

void test_Number()
{
	std::cout << "testing ..." << std::endl;
	Number a(2);
	Number b(3.1, 7.5);
	Number c(3);
	std::cout << "testing a ..." << a << std::endl;
	std::cout << "testing b ..." << b << std::endl;
	std::cout << "testing function called with derived class ..." << std::endl;
	print_it(a);
	print_it(b);
	print_it(c);
	std::cout << "testing integer multiply : 3*2 ..." << std::endl;
	c.multiply(a);
	print_it(c);
	std::cout << "testing integer*complex multiply :" << a << "*" << b << " ..." << std::endl;
	a.multiply(b);
	print_it(a);
}

int main()
{
	test_Number();
}
```
