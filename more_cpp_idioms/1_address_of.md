# 1. Address Of

---
## 의미
단항의 앰퍼센드 연산자(&)를 재정의한 클래스의 객체 주소를 찾는다.

## 동기
C++는 클래스 내부에서 단항의 앰퍼센드 연산자(&)를 재정의 할 수 있도록 허용합니다. 이 연산자의 리턴타입이 반드시 객체의 실제 주소일 필요는 없는데, 이러한 클래스의 의도에 대해 논쟁의 여지가 있음에도 불구하고 사용 가능한 것이 현실입니다. Address-of 이디엄은 객체의 단항 앰퍼샌드 연산자의 재정의 여부와 관계없이 실제 주소를 찾는 방법 및 접근 보호에 대해 다룹니다.<br><br>
아래 예제를 보면, 'nonaddressable' 클래스의 경우 앰퍼샌드 연산자가 private 함수이기 때문에 main 함수는 컴파일 되지 않습니다. 만일 접근이 가능하더라도, 리턴 타입인 double에서 pointer 타입으로의 형변환이 불가능하거나 무의미합니다.
```
class nonaddressable
{
public:
    typedef double useless_type;
private:
    useless_type operator&() const;
};

int main()
{
  nonaddressable na;
  nonaddressable * naptr = &na; // Compiler error here.
}
```

## 솔루션과 샘플코드
Address-of 이디엄은 객체의 주소를 연속적인 캐스팅을 통해 얻어냅니다.
```
template <class T>
T * addressof(T & v)
{
  return reinterpret_cast<T *>(& const_cast<char&>(reinterpret_cast<const volatile char &>(v)));
}

int main()
{
  nonaddressable na;
  nonaddressable * naptr = addressof(na); // No more compiler error.
}
```

## 알려진 예
Boost addressof utility<br>
이 함수는 이미 새로운 C++11 표준인 "memory" 헤더에 포함되어 있습니다.
