=컴포지트 패턴&CBD=
==컴포지트 패턴==
:컴포지트 패턴은 기본객체와 구성객체들의 관계를 트리 모양의 상속 관계로 구성하여, 사용자는 두 종류의 객체를 마치 한 종류의 객체를 다루듯 동일한 인터페이스로 사용할 수 있도록 하는 설계 기법이다.
:컴포지트 패턴은 커맨드(Command) 패턴이나 방문자(Visitor) 패턴, 데코레이터(Decorator) 패턴 등에 응용될 수 있다.

===컴포지트 패턴/구조===
*component
*:객체들에게 동일한 인터페이스를 제공하기 위한 상위 클래스이다. leaf와 composite는 component를 상속받으며 해당 메소드 중 연관된 메소드만 재정의한다
*:모든 객체들이 반드시 가져야 하는 기본 기능은 순수 가상 메소드로 구현하여 component를 상속받은 객체 내에서 이를 구현하게 한다
*leaf(개별 객체)
*:클래스의 기본이 되는 개개의 객체를 의미한다.
*composite(복합 개체)
*:기본이 되는 여러 개의 개별 객체를 하나로 모아 구성한 복합 객체를 의미한다, 복합 객체는 내부에 개별 객체를 추가/삭제하기 위한 메소드를 추가로 보유한다

===컴포지트 패턴/설계===
#객체의 구성 및 상하위 체계를 파악한다
#파악된 객체들을 트리 구조로 설계한다
#객체와 복합 객체는 공통으로 사용할 수 있는 메소드가 정의된 인터 페이스/추상 클래스를 구현/상속한다

===컴포지트 패턴/구현 예제===
 class Component
 {
   public:
     // Component 클래스에서는 추상 함수를 통해 객체들에게 동일한 인터페이스를 제공함
     virtual void list() const = 0;
     virtual ~Component(){};
 }

 // Component를 상속받는 Leaf(개별 객체) 생성
 class Leaf : public Component 
 {
   public:
     Leaf(int val) : m_value(val) 
     {
     }
     void list() const 
     {
        cout << "   " << m_value << "\n";
     }
   private:
     int m_Value;
 };

 // Component를 상속받는 복합 객체 생성
 class Composite : public Component 
 {
   public:
     Composite(string id) : m_Id(id) 
     {
     }
     // 복합 객체는 내부에 개별 객체를 추가/삭제하는 메소드를 추가로 가진다
     void add(Component *obj)
     {
        m_Table.push_back(obj);
     }
     void list() const
     {
        cout << m_Id << ":" << "\n";
        for (vector<Component*>::const_iterator it = m_Table.begin(); it != m_Table.end(); ++it)
        {
           (*it)->list();
        }
     }
   private:
     vector <Component*> m_Table; // 복합 객체는 내부에 개별 객체의 포인터를 가지고 있다.
     string m_Id;
 };

==CBD(Component Base Development)==
: 컴포넌트 기반 개발은 기존의 시스템이나 소프트웨어를 구성하는 컴포넌트를 조립하여 하나의 새로운 응용 프로그램을 만드는 소프트웨어 개발 방법론이다.
: 쇼핑 바구니, 사용자 인증, 검색 엔진, 카탈로그 등 상업적으로 이용 가능한 컴포넌트를 결합하여 그들의 전자상거래 응용프로그램을 개발하는 컴포넌트 기반 개발을 사용한다

=옵저버 패턴=
:옵저버 패턴은 객체의 상태 변화를 관찰하는 관찰자들, 즉 옵저버들의 목록을 객체에 등록해 상태 변화가 있을 때마다 메서드 등을 통해 객체가 직접 목록의 각 옵저버에게 통지하여 자동으로 내용이 갱신되는 방식으로, 일대다 의존성을 정의한다. 
:옵저버 패턴은 주로 분산 이벤트 핸들링 시스템을 구현하는 데 사용된다.

===옵저버 패턴/구조===
*옵저버(또는 리스너)
*:특정 객체의 상태 변화를 관찰하는 객체이다. 각각의 옵저버들은 관찰 대상인 객체가 발생시키는 이벤트를 받아 처리한다. 이벤트가 발생하면 각 옵저버는 콜백을 받는다
*주체
*:옵저버의 관찰을 받는 객체로 이벤트를 발생시키는 주체라는 의미에서 Subject로 표시한다. 주체는 하나 이상의 옵저버를 등록한다
*:주체는 일반적으로 등록(register)과 제거(unregister)메서드를 가진다.
*:등록은 새로운 옵저버를 목록에 등록하고 제거는 목록에서 옵저버를 뺀다. 등록과 제거 메서드 이외에도, 임시로 작동을 멈추거나 재개하는 메서드를 이용해 이벤트가 계속해서 있을 때 홍수같이 발생하는 요청을 제어할 수도 있다.
*옵저버 패턴이 많이 쓰인 시스템에서는 순환 실행을 막는 메카니즘이 필요하다. 
*:이벤트 X가 발생하면 옵저버A가 옵저버B를 갱신한다고 가정해보자. 그런데 옵저버B가 이 처리를 위해 옵저버A를 갱신한다면, 이는 다시 A로 하여금 이벤트 X를 발생하게 한다. 이같은 상황을 막기 위해 이벤트 X가 한번 처리된 후에는 A가 이벤트 X를 다시 발생시키지 않는 방법이 요구된다.

===옵저버 패턴/구현 예제===
 class Observer{
  public:
   virtual void Update() = 0;
 };

 class Subject{
  public:
    virtual void NotifyObserver() = 0;
    virtual void AddObserver(Observer* input) = 0;
 }

 // 용도에 따라 Observer를 상속받는 옵저버 클래스를 생성한다
 class Graph : public Observer{
  public:
    virtual void Update(int data){
      m_data = data;
      Print();
    }
  private:
    void Print(){
      printf("data : %d",m_data);
    }
  private:
    int m_data;
 }

 // Subject를 상속받아 개별적인 이벤트를 발생시키는 주체 클래스를 생성한다
 // 내부에 옵저버를 추가/삭제하고 옵저버에게 이벤트 발생을 알리는 메소드를 가진다
 class Data : public Subject{
  public:
    virtual void NotifyObserver(){
      for(int i = 0; i < observerList.size(); i++)
      {
        observerList[i]->Update(m_data);
      }
    }
    virtual void AddObserver(Observer* input){
      observerList.push_back(input);
    }
    void setData(int data){
      m_data = data;
    }
  private:
    vector<Opserver*> ovserverList; //<내부에 옵저버의 포인터를 가진다.
    int m_data;
 }

----
자료 출처
#[http://ko.wikipedia.org/wiki/%EC%BB%B4%ED%8F%AC%EC%A7%80%ED%8A%B8_%ED%8C%A8%ED%84%B4 컴포지트 패턴-위키백과]
#[http://ko.wikipedia.org/wiki/%EC%BB%B4%ED%8F%AC%EB%84%8C%ED%8A%B8_%EA%B8%B0%EB%B0%98_%EC%86%8C%ED%94%84%ED%8A%B8%EC%9B%A8%EC%96%B4_%EA%B3%B5%ED%95%99 컴포넌트 기반 소프트웨어 공학-위키백과]
#[http://stackcanary.com/?p=225 컴포지트패턴, 복합패턴 (Composite Pattern)-Cloudrain's Cool World~!!]
#[http://dsmoon.tistory.com/entry/COMPOSITE-%ED%8C%A8%ED%84%B4 Composite패턴-Clean corder]
#[http://tiboy.tistory.com/entry/Composite-Pattern-%EC%BB%B4%ED%8F%AC%EC%A7%80%ED%8A%B8-%ED%8C%A8%ED%84%B4 [디자인패턴/Composite Pattern] 컴포지트 패턴-SoftWare]
#[http://ko.wikipedia.org/wiki/%EC%98%B5%EC%84%9C%EB%B2%84_%ED%8C%A8%ED%84%B4 옵서버 패턴-위키백과]
#[http://smallorbit.tistory.com/12 Observer Pattern 옵저버 패턴-SMALL ORBIT]