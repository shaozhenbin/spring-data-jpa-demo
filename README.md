# spring-data-jpa-demo
1.jpa实体之间关系
	注意：
	1.谁拥有外键谁就是关系维护端，拿这个例子说，Address就是关系维护端，而Person是关系被维护端；
	2.惰性加载，一般来说，关系维护端配置为fetch=FetchType.LAZY。
	3.拥有mappedBy注解的实体类为关系被维护端，另外的实体类为关系维护端的。顾名思意，关系的维护端对关系（在多对多为中间关联表）的CRUD做操作。关系的被维护端没有该操作，不能维护关系。
1.1 one-to-one
实体之间一对一关系配置如下，假如一个人对应一个地址：

	Person.java类
	public class Person {
		@Id
		@GeneratedValue(generator = "idGenerator")
		@GenericGenerator(name = "idGenerator", strategy = "uuid")
		private String id;
		private String name;
		@OneToOne(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
		private Address address;	
	}
	Address.java类
	public class Address {
		@Id
		@GeneratedValue(generator = "idGenerator")
		@GenericGenerator(name = "idGenerator", strategy = "uuid")
		private String id;

		private String city;

		@OneToOne(cascade=CascadeType.PERSIST,optional=false,fetch=FetchType.LAZY)
		@JoinColumn(name="PERSON_ID")
		private Person person;
	}

1.2 one-to-many
实体之间一对多关系配置如下，假如一个人拥有多辆车
	注意：
	1.谁拥有外键谁就是关系维护端，拿这个例子说，Car就是关系维护端，而Person是关系被维护端；
	2.惰性加载，一般来说，关系维护端配置为fetch=FetchType.LAZY。
	3.拥有mappedBy注解的实体类为关系被维护端，另外的实体类为关系维护端的。顾名思意，关系的维护端对关系（在多对多为中间关联表）的CRUD做操作。关系的被维护端没有该操作，不能维护关系。

	Person.java类
	public class Person {
		@Id
		@GeneratedValue(generator = "idGenerator")
		@GenericGenerator(name = "idGenerator", strategy = "uuid")
		private String id;
		private String name;
		@OneToMany(cascade=CascadeType.ALL,mappedBy="person",fetch=FetchType.LAZY)
		private Set<Car> cars = new HashSet<Car>();	
	}
	Car.java类
	public class Car {
		@Id
		@GeneratedValue(generator = "idGenerator")
		@GenericGenerator(name = "idGenerator", strategy = "uuid")
		private String id;
		private String name;
		@ManyToOne(cascade=CascadeType.PERSIST,optional=false,fetch=FetchType.LAZY)
		@JoinColumn(name="PERSON_ID")
		private Person person;
	}

1.3 many-to-many
实体之间对多关系配置如下，假如一个老师可以教多个学生，而一个学生也可以有多个任课老师
注意：
	1.谁拥有外键谁就是关系维护端，拿这个例子说，Student就是关系维护端，而Teacher是关系被维护端；
	2.惰性加载，一般来说，关系维护端配置为fetch=FetchType.LAZY。
	3.拥有mappedBy注解的实体类为关系被维护端，另外的实体类为关系维护端的。顾名思意，关系的维护端对关系（在多对多为中间关联表）的CRUD做操作。关系的被维护端没有该操作，不能维护关系。
	4.关系维护端删除时，如果中间表存在些纪录的关联信息，则会删除该关联信息;关系被维护端删除时，如果中间表存在些纪录的关联信息，则会删除失败 

	Teacher.java类
	public class Teacher {
		@Id
		@GeneratedValue(generator = "idGenerator")
		@GenericGenerator(name = "idGenerator", strategy = "uuid")
		private String id;
		private String name;
		@ManyToMany(cascade=CascadeType.ALL,
				mappedBy="teachers",//通过维护端属性关联
				fetch=FetchType.LAZY)
		private Set<Student> students = new HashSet<Student>();	
	}
	Student.java类
	public class Student {

		@Id
		@GeneratedValue(generator = "idGenerator")
		@GenericGenerator(name = "idGenerator", strategy = "uuid")
		private String id;
		private String name;
		@ManyToMany (cascade = CascadeType.REFRESH)
		@JoinTable (//关联表
		name = "student_teacher" , //关联表名
		inverseJoinColumns = @JoinColumn (name = "teacher_id" ),//被维护端外键
		joinColumns = @JoinColumn (name = "student_id" ))//维护端外键
		private Set<Teacher> teachers = new HashSet<Teacher>();	
	 }
 
 2.jpa实体级联操作
   注意：无论是实体一对一，一对多，多对一，还是多对多，由于级联操作具有可传递性，配置级联属性cascade需要根据业务需要，不能一昧地选择 CascadeType.ALL。不然后果很严重，有些数据怎么会凭空消失了尼。拿下面这个例子来说，作为关系被维护端的person,对address有级联持久化，更新
删除，保存前刷新能力，实现效果就是字面上意思，这很容易理解。可能级联保存前刷新比较难理解，比如person同时被A,B修改，A修改后提交了，B这时
也修改提交，person保存前每次会先更新到当前最新状态，再保存。
	
	@Entity
	@Table(name = "person")
	@Setter
	@Getter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public class Person {

	    @Id
	    @GeneratedValue(generator = "idGenerator")
	    @GenericGenerator(name = "idGenerator", strategy = "uuid")
	    private String id;

	    @Column(name = "CODE", unique = true, nullable = false, length = 32)
	    private String code;

	    @Column(name = "NAME", length = 20, nullable = false)
	    private String name;

	    /**
	     * 一对一关系
	     */
	    @OneToOne(mappedBy = "person", cascade = {
		    CascadeType.MERGE,//级联更新
		    CascadeType.PERSIST,//级联持久化实体
		    CascadeType.REMOVE,//级联删除
		    CascadeType.REFRESH,//级联保存前刷新
		    CascadeType.DETACH//?
	//            CascadeType.ALL 级联所有权限
	    }, fetch = FetchType.LAZY)
	    private Address address;
	 }
	 
	 
  
  
  
 
