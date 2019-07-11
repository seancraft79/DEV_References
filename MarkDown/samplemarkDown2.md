=������Ʈ ����&CBD=
==������Ʈ ����==
:������Ʈ ������ �⺻��ü�� ������ü���� ���踦 Ʈ�� ����� ��� ����� �����Ͽ�, ����ڴ� �� ������ ��ü�� ��ġ �� ������ ��ü�� �ٷ�� ������ �������̽��� ����� �� �ֵ��� �ϴ� ���� ����̴�.
:������Ʈ ������ Ŀ�ǵ�(Command) �����̳� �湮��(Visitor) ����, ���ڷ�����(Decorator) ���� � ����� �� �ִ�.

===������Ʈ ����/����===
*component
*:��ü�鿡�� ������ �������̽��� �����ϱ� ���� ���� Ŭ�����̴�. leaf�� composite�� component�� ��ӹ����� �ش� �޼ҵ� �� ������ �޼ҵ常 �������Ѵ�
*:��� ��ü���� �ݵ�� ������ �ϴ� �⺻ ����� ���� ���� �޼ҵ�� �����Ͽ� component�� ��ӹ��� ��ü ������ �̸� �����ϰ� �Ѵ�
*leaf(���� ��ü)
*:Ŭ������ �⺻�� �Ǵ� ������ ��ü�� �ǹ��Ѵ�.
*composite(���� ��ü)
*:�⺻�� �Ǵ� ���� ���� ���� ��ü�� �ϳ��� ��� ������ ���� ��ü�� �ǹ��Ѵ�, ���� ��ü�� ���ο� ���� ��ü�� �߰�/�����ϱ� ���� �޼ҵ带 �߰��� �����Ѵ�

===������Ʈ ����/����===
#��ü�� ���� �� ������ ü�踦 �ľ��Ѵ�
#�ľǵ� ��ü���� Ʈ�� ������ �����Ѵ�
#��ü�� ���� ��ü�� �������� ����� �� �ִ� �޼ҵ尡 ���ǵ� ���� ���̽�/�߻� Ŭ������ ����/����Ѵ�

===������Ʈ ����/���� ����===
 class Component
 {
   public:
     // Component Ŭ���������� �߻� �Լ��� ���� ��ü�鿡�� ������ �������̽��� ������
     virtual void list() const = 0;
     virtual ~Component(){};
 }

 // Component�� ��ӹ޴� Leaf(���� ��ü) ����
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

 // Component�� ��ӹ޴� ���� ��ü ����
 class Composite : public Component 
 {
   public:
     Composite(string id) : m_Id(id) 
     {
     }
     // ���� ��ü�� ���ο� ���� ��ü�� �߰�/�����ϴ� �޼ҵ带 �߰��� ������
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
     vector <Component*> m_Table; // ���� ��ü�� ���ο� ���� ��ü�� �����͸� ������ �ִ�.
     string m_Id;
 };

==CBD(Component Base Development)==
: ������Ʈ ��� ������ ������ �ý����̳� ����Ʈ��� �����ϴ� ������Ʈ�� �����Ͽ� �ϳ��� ���ο� ���� ���α׷��� ����� ����Ʈ���� ���� ������̴�.
: ���� �ٱ���, ����� ����, �˻� ����, īŻ�α� �� ��������� �̿� ������ ������Ʈ�� �����Ͽ� �׵��� ���ڻ�ŷ� �������α׷��� �����ϴ� ������Ʈ ��� ������ ����Ѵ�

=������ ����=
:������ ������ ��ü�� ���� ��ȭ�� �����ϴ� �����ڵ�, �� ���������� ����� ��ü�� ����� ���� ��ȭ�� ���� ������ �޼��� ���� ���� ��ü�� ���� ����� �� ���������� �����Ͽ� �ڵ����� ������ ���ŵǴ� �������, �ϴ�� �������� �����Ѵ�. 
:������ ������ �ַ� �л� �̺�Ʈ �ڵ鸵 �ý����� �����ϴ� �� ���ȴ�.

===������ ����/����===
*������(�Ǵ� ������)
*:Ư�� ��ü�� ���� ��ȭ�� �����ϴ� ��ü�̴�. ������ ���������� ���� ����� ��ü�� �߻���Ű�� �̺�Ʈ�� �޾� ó���Ѵ�. �̺�Ʈ�� �߻��ϸ� �� �������� �ݹ��� �޴´�
*��ü
*:�������� ������ �޴� ��ü�� �̺�Ʈ�� �߻���Ű�� ��ü��� �ǹ̿��� Subject�� ǥ���Ѵ�. ��ü�� �ϳ� �̻��� �������� ����Ѵ�
*:��ü�� �Ϲ������� ���(register)�� ����(unregister)�޼��带 ������.
*:����� ���ο� �������� ��Ͽ� ����ϰ� ���Ŵ� ��Ͽ��� �������� ����. ��ϰ� ���� �޼��� �̿ܿ���, �ӽ÷� �۵��� ���߰ų� �簳�ϴ� �޼��带 �̿��� �̺�Ʈ�� ����ؼ� ���� �� ȫ������ �߻��ϴ� ��û�� ������ ���� �ִ�.
*������ ������ ���� ���� �ý��ۿ����� ��ȯ ������ ���� ��ī������ �ʿ��ϴ�. 
*:�̺�Ʈ X�� �߻��ϸ� ������A�� ������B�� �����Ѵٰ� �����غ���. �׷��� ������B�� �� ó���� ���� ������A�� �����Ѵٸ�, �̴� �ٽ� A�� �Ͽ��� �̺�Ʈ X�� �߻��ϰ� �Ѵ�. �̰��� ��Ȳ�� ���� ���� �̺�Ʈ X�� �ѹ� ó���� �Ŀ��� A�� �̺�Ʈ X�� �ٽ� �߻���Ű�� �ʴ� ����� �䱸�ȴ�.

===������ ����/���� ����===
 class Observer{
  public:
   virtual void Update() = 0;
 };

 class Subject{
  public:
    virtual void NotifyObserver() = 0;
    virtual void AddObserver(Observer* input) = 0;
 }

 // �뵵�� ���� Observer�� ��ӹ޴� ������ Ŭ������ �����Ѵ�
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

 // Subject�� ��ӹ޾� �������� �̺�Ʈ�� �߻���Ű�� ��ü Ŭ������ �����Ѵ�
 // ���ο� �������� �߰�/�����ϰ� ���������� �̺�Ʈ �߻��� �˸��� �޼ҵ带 ������
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
    vector<Opserver*> ovserverList; //<���ο� �������� �����͸� ������.
    int m_data;
 }

----
�ڷ� ��ó
#[http://ko.wikipedia.org/wiki/%EC%BB%B4%ED%8F%AC%EC%A7%80%ED%8A%B8_%ED%8C%A8%ED%84%B4 ������Ʈ ����-��Ű���]
#[http://ko.wikipedia.org/wiki/%EC%BB%B4%ED%8F%AC%EB%84%8C%ED%8A%B8_%EA%B8%B0%EB%B0%98_%EC%86%8C%ED%94%84%ED%8A%B8%EC%9B%A8%EC%96%B4_%EA%B3%B5%ED%95%99 ������Ʈ ��� ����Ʈ���� ����-��Ű���]
#[http://stackcanary.com/?p=225 ������Ʈ����, �������� (Composite Pattern)-Cloudrain's Cool World~!!]
#[http://dsmoon.tistory.com/entry/COMPOSITE-%ED%8C%A8%ED%84%B4 Composite����-Clean corder]
#[http://tiboy.tistory.com/entry/Composite-Pattern-%EC%BB%B4%ED%8F%AC%EC%A7%80%ED%8A%B8-%ED%8C%A8%ED%84%B4 [����������/Composite Pattern] ������Ʈ ����-SoftWare]
#[http://ko.wikipedia.org/wiki/%EC%98%B5%EC%84%9C%EB%B2%84_%ED%8C%A8%ED%84%B4 �ɼ��� ����-��Ű���]
#[http://smallorbit.tistory.com/12 Observer Pattern ������ ����-SMALL ORBIT]