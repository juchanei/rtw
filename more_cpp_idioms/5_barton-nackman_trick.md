# Barton-Nackman Trick

---
## 의미
재정의 한 연산자를 네임스페이스안에 둘 수 없거나, 함수 템플릿의 'Overloading Resolution'을 할 수 없는 경우를 위한 이디엄입니다.

> **Overload Resolution**<br>
재정의된 함수를 호출하기 위해, 컴파일러는 함수 인자에 의존한 name-lookup을 수행합니다.
템플릿 함수의 경우엔 '템플릿 인자 추론'을 통해 같은 과정을 거치는데, 선택 될 후보함수의 개수가 여러개인 경우 하나를 선택하는 Overload Resolution을 수행합니다.
보통은 인자가 가장 비슷한 후보 함수를 선택하여, 해당 함수의 주소를 취합니다.<br>
출처 : http://en.cppreference.com/w/cpp/language/overload_resolution

## 다른 이름
이 이디엄은 Restricted Template Expansion을 참고해서 만들어졌지만, 이것이 널리 사용되진 않습니다.

## 동기
John Barton과 Lee Nackman은 1994년 C++ 구현의 한계를 느끼고 이 이디엄을 발표했습니다.
지금의 C++표준은 당시의 문제를 지원하고 있기 때문에, 이제 더이상 본래의 목적대로 사용되지 않지는 않습니다.

Barton과 Nackman 이 이디엄을 처음 개발했을 때, C++은 함수템플릿의 재정의를 지원하지 않았고 많은 구현이 네임스페이르를 지원하지 않았습니다.
이러한 문제는 클래스 템플릿에서 연산자를 재정의 할 경우 발생했습니다.
아래와 같은 클래스가 있습니다.

```
template<typename T>
class List {
   // ...
};
```

동등 연산자를 정의하기 위한 대부분의 방법은 네임스페이스 범위에 비멤버 함수로 만드는 방법이었습니다.(컴파일러가 네임스페이스를 지원하지 않는 경우에는, 전역으로 비멤버 함수를 만들었습니다)
operator==를 비멤버함수로 정의한다는 것은 두개의 인자가 대칭적으로 다뤄짐을 의미하며, 하나의 인자가 포인터인 경우는 일어나지 않았습니다.
이러한 동등 연산자는 아래와 같이 생겼습니다.

```
template<typename T>
bool operator==(List<T> const & lft, List<T> const & rgt) {
   //...
}
```

하지만, 이때 함수 템플릿은 재정의 할 수 없었고, 모든 플랫폼에서 이 함수 템플릿을 네임스페이스 안에 두는것이 보장되지 않았기 때문에, 이 방법은 단 하나의 클래스만이 이 동등 연산자 함수 템플릿을 가질수 있었습니다.
따라서, 다른 클래스에도 같은 일을 할 경우 모호성에 대한 오류가 발생하였습니다.

## 솔루션과 샘플코드
솔루션은 연산자를 클래스 안에 friend 함수로 정의하는 것입니다:

```
template<typename T>
class List {
 public:
    friend bool operator==(const List<T> & lft,
                           const List<T> & rgt) {
        // ...
    }
};
```

일시적으로 템플릿 함수가 아닌, 구체적인 타입의 인자를 가진 전역함수가 템플릿 내부로 들어오게 되었습니다.
이 비템플릿 함수는 다른 여타 비템플릿 함수처럼 Overload Resolution을 통해 선택될 수 있습니다.<br>

이 구현은 friend 함수를 'Curiously Recurring Template Pattern을 이용한 기본 클래스'의 일부로 제공함으로써 일반화 할 수 있습니다.

```
template<typename T>
class EqualityComparable {
public:
    friend bool operator==(const T & lft, const T & rgt) { return lft.equalTo(rgt); }
    friend bool operator!=(const T & lft, const T & rgt) { return !lft.equalTo(rgt); }
};

class ValueType :
    private EqualityComparable<ValueType> {
 public:
    bool equalTo(const ValueType & other) const;
};
```

## 알려진 예
- Boost.Operators library

## 관련 된 이디엄
- Curiously Recurring Template Pattern

## 참고
- Barton-Nackman trick on Wikipedia
