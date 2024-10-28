# Mybatis3 入门

## 1.pom依赖
```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.47</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.2</version>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
    </dependency>
</dependencies>
```
## 2.从 XML 中构建 SqlSessionFactory
```java
private static SqlSessionFactory sqlSessionFactory;

    /**
     * 从 XML 中构建 SqlSessionFactory
     */
    static {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * 从 SqlSessionFactory 中获取 SqlSession
     * @return SqlSession
     */
    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }
```
## 3.实体类
```java
public class User {
    private String id;
    private String name;
    private String password;
    public User() {
    }
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
```
## 4.dao
```java
public interface UserDao {
    List<User> getUserList();
}

```
## 5.Mapper
```xml
<mapper namespace="com.kuang.dao.UserDao">
    <select id="getUserList" resultType="com.kuang.pojo.User">
        select * from mybatis.user
    </select>
</mapper>
```
## 6.测试
```java
 @Test
    public void test(){
        //org.apache.ibatis.binding.BindingException: Type interface com.kuang.dao.UserDao is not known to the MapperRegistry.
        //MapperRegistry是什么

        //Could not find resource com/kuang/dao/UserMapper.xml

        SqlSession session=Mybatisutil.getSqlSession();
        UserDao mapper=session.getMapper(UserDao.class);
        List<User> userList=mapper.getUserList();
        for (User user:userList) {
            System.out.println(user.toString());
        }
        session.close();
    }
```
## 7.注意点
### 7.1 not known to the MapperRegistry
```text
org.apache.ibatis.binding.BindingException: Type interface com.kuang.dao.UserDao is not known to the MapperRegistry.
```
这个报错是因为每一个Mapper.xml都需要在Mybatis核心配置文件中注册！！
```xml
<mappers>
    <mapper resource="com/kuang/dao/UserMapper.xml"></mapper>
 </mappers>
```
### 7.2  Could not find resource com/kuang/dao/UserMapper.xml
在编译时过滤掉了xml文件，可以在pom如下配置
```xml
<build>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>true</filtering>
        </resource>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>true</filtering>
        </resource>

    </resources>
</build>
```

## 8.增删改

parameterType 可以是Map，Object或者基础数据类型（int，String等）

```xml
	<insert id="addUser" parameterType="com.kuang.pojo.User">
        insert into user(`id`, `name`, `password`)  values(#{id},#{name},#{password});
    </insert>
    <update id="updateUser" parameterType="com.kuang.pojo.User">
        update user set name=#{name},password=#{password} where id=#{id}
    </update>
    <delete id="deleteUser" parameterType="String">
        delete from user  where id=#{id}
    </delete>
```

### 注意的

```java
@Test
    public void addUser(){
        SqlSession sqlsession=Mybatisutil.getSqlSession();
        UserDao mapper=sqlsession.getMapper(UserDao.class);
        //方式一
        User user=new User("4","4","4");
        int a=mapper.addUser(user);
        List<User> userList=mapper.getUserList(); 	
        for (User user0:userList) {
            System.out.println(user0.toString());
        }
        //!!增删改需要提交事务，否则不能存进数据库!!
        sqlsession.commit();
        sqlsession.close();
    }
```

***增删改操作时一定要提交事务，否则操作无效***

## 9 万能Map(数据库字段很多可以考虑)

```java
//接口
    int addMapUser(Map map);
//mapper
	<insert id="addMapUser" parameterType="map">
        insert into user(`id`, `name`, `password`)  values(#{userid},#{username},#{userpwd});
    </insert>
//test
    @Test
    public void addMapUser(){
        SqlSession sqlsession=Mybatisutil.getSqlSession();
        UserDao mapper=sqlsession.getMapper(UserDao.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userid","6");
        map.put("username","6");
        map.put("userpwd","6");
        mapper.addMapUser(map);
        List<User> userList=mapper.getUserList();
        for (User user0:userList) {
            System.out.println(user0.toString());
        }
        //!!增删改需要提交事务，否则不能存进数据库!!
        sqlsession.commit();
        sqlsession.close();
    }
```

# 配置

## 1.properties

```xml
//引入外部配置文件
 <!--引入外部配置文件，获取driver，url，username，password的值-->
    <properties resource="db.properties"/>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"></transactionManager>
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>
```

这是db.properties外部配置文件

```properties
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis?&amp;useUnicode=true&amp;useSSL=false&amp;characterEncoding=UTF-8
username=root
password=root
```

## 2.类型别名（typeAliases）

### 2.1自定义

将com.kuang.pojo.User自定义为user1

```xml
<typeAliases>
  <typeAlias alias="user1" type="com.kuang.pojo.User"/>
</typeAliases>
```

Mapper的resultType="user1"

```xml
    <select id="getUserList" resultType="user1">
        select * from mybatis.user
    </select>
```

### 2.2扫包

配置扫包路径`com.kuang.pojo`

```xml
    <typeAliases>
        <package name="com.kuang.pojo"/>
    </typeAliases>
```

2.2.1每一个在包 `com.kuang.pojo` 中的 Java Bean，在没有注解的情况下，会使用 Bean 的首字母小写的非限定类名来作为它的别名。 比如 `com.kuang.pojo.User` 的别名为 `user`；

```xml
  <select id="getUserList" resultType="user">
        select * from mybatis.user
    </select>
```

2.2.2若有注解，则别名为其注解值。见下面的例子：

```java
//@Alias("user")内的user可以是任意自己起的
@Alias("user2")
public class User {
    ....
}
```

Mapper的resultType="user2"

```xml
  <select id="getUserList" resultType="user2">
        select * from mybatis.user
    </select>
```

下面是一些为常见的 Java 类型内建的类型别名。它们都是不区分大小写的，注意，为了应对原始类型的命名重复，采取了特殊的命名风格。

| 别名                      | 映射的类型   |
| ------------------------- | :----------- |
| _byte                     | byte         |
| _char (since 3.5.10)      | char         |
| _character (since 3.5.10) | char         |
| _long                     | long         |
| _short                    | short        |
| _int                      | int          |
| _integer                  | int          |
| _double                   | double       |
| _float                    | float        |
| _boolean                  | boolean      |
| string                    | String       |
| byte                      | Byte         |
| char (since 3.5.10)       | Character    |
| character (since 3.5.10)  | Character    |
| long                      | Long         |
| short                     | Short        |
| int                       | Integer      |
| integer                   | Integer      |
| double                    | Double       |
| float                     | Float        |
| boolean                   | Boolean      |
| date                      | Date         |
| decimal                   | BigDecimal   |
| bigdecimal                | BigDecimal   |
| biginteger                | BigInteger   |
| object                    | Object       |
| date[]                    | Date[]       |
| decimal[]                 | BigDecimal[] |
| bigdecimal[]              | BigDecimal[] |
| biginteger[]              | BigInteger[] |
| object[]                  | Object[]     |
| map                       | Map          |
| hashmap                   | HashMap      |
| list                      | List         |
| arraylist                 | ArrayList    |
| collection                | Collection   |
| iterator                  | Iterator     |

# 设置

这是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为。 下表描述了设置中各项设置的含义、默认值等。

| 设置名                             | 描述                                                         | 有效值                                                       | 默认值                                                |
| :--------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- | :---------------------------------------------------- |
| cacheEnabled                       | 全局性地开启或关闭所有映射器配置文件中已配置的任何缓存。     | true \| false                                                | true                                                  |
| lazyLoadingEnabled                 | 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置 `fetchType` 属性来覆盖该项的开关状态。 | true \| false                                                | false                                                 |
| aggressiveLazyLoading              | 开启时，任一方法的调用都会加载该对象的所有延迟加载属性。 否则，每个延迟加载属性会按需加载（参考 `lazyLoadTriggerMethods`)。 | true \| false                                                | false （在 3.4.1 及之前的版本中默认为 true）          |
| multipleResultSetsEnabled          | 是否允许单个语句返回多结果集（需要数据库驱动支持）。         | true \| false                                                | true                                                  |
| useColumnLabel                     | 使用列标签代替列名。实际表现依赖于数据库驱动，具体可参考数据库驱动的相关文档，或通过对比测试来观察。 | true \| false                                                | true                                                  |
| useGeneratedKeys                   | 允许 JDBC 支持自动生成主键，需要数据库驱动支持。如果设置为 true，将强制使用自动生成主键。尽管一些数据库驱动不支持此特性，但仍可正常工作（如 Derby）。 | true \| false                                                | False                                                 |
| autoMappingBehavior                | 指定 MyBatis 应如何自动映射列到字段或属性。 NONE 表示关闭自动映射；PARTIAL 只会自动映射没有定义嵌套结果映射的字段。 FULL 会自动映射任何复杂的结果集（无论是否嵌套）。 | NONE, PARTIAL, FULL                                          | PARTIAL                                               |
| autoMappingUnknownColumnBehavior   | 指定发现自动映射目标未知列（或未知属性类型）的行为。`NONE`: 不做任何反应`WARNING`: 输出警告日志（`'org.apache.ibatis.session.AutoMappingUnknownColumnBehavior'` 的日志等级必须设置为 `WARN`）`FAILING`: 映射失败 (抛出 `SqlSessionException`) | NONE, WARNING, FAILING                                       | NONE                                                  |
| defaultExecutorType                | 配置默认的执行器。SIMPLE 就是普通的执行器；REUSE 执行器会重用预处理语句（PreparedStatement）； BATCH 执行器不仅重用语句还会执行批量更新。 | SIMPLE REUSE BATCH                                           | SIMPLE                                                |
| defaultStatementTimeout            | 设置超时时间，它决定数据库驱动等待数据库响应的秒数。         | 任意正整数                                                   | 未设置 (null)                                         |
| defaultFetchSize                   | 为驱动的结果集获取数量（fetchSize）设置一个建议值。此参数只可以在查询设置中被覆盖。 | 任意正整数                                                   | 未设置 (null)                                         |
| defaultResultSetType               | 指定语句默认的滚动策略。（新增于 3.5.2）                     | FORWARD_ONLY \| SCROLL_SENSITIVE \| SCROLL_INSENSITIVE \| DEFAULT（等同于未设置） | 未设置 (null)                                         |
| safeRowBoundsEnabled               | 是否允许在嵌套语句中使用分页（RowBounds）。如果允许使用则设置为 false。 | true \| false                                                | False                                                 |
| safeResultHandlerEnabled           | 是否允许在嵌套语句中使用结果处理器（ResultHandler）。如果允许使用则设置为 false。 | true \| false                                                | True                                                  |
| mapUnderscoreToCamelCase           | 是否开启驼峰命名自动映射，即从经典数据库列名 A_COLUMN 映射到经典 Java 属性名 aColumn。 | true \| false                                                | False                                                 |
| localCacheScope                    | MyBatis 利用本地缓存机制（Local Cache）防止循环引用和加速重复的嵌套查询。 默认值为 SESSION，会缓存一个会话中执行的所有查询。 若设置值为 STATEMENT，本地缓存将仅用于执行语句，对相同 SqlSession 的不同查询将不会进行缓存。 | SESSION \| STATEMENT                                         | SESSION                                               |
| jdbcTypeForNull                    | 当没有为参数指定特定的 JDBC 类型时，空值的默认 JDBC 类型。 某些数据库驱动需要指定列的 JDBC 类型，多数情况直接用一般类型即可，比如 NULL、VARCHAR 或 OTHER。 | JdbcType 常量，常用值：NULL、VARCHAR 或 OTHER。              | OTHER                                                 |
| lazyLoadTriggerMethods             | 指定对象的哪些方法触发一次延迟加载。                         | 用逗号分隔的方法列表。                                       | equals,clone,hashCode,toString                        |
| defaultScriptingLanguage           | 指定动态 SQL 生成使用的默认脚本语言。                        | 一个类型别名或全限定类名。                                   | org.apache.ibatis.scripting.xmltags.XMLLanguageDriver |
| defaultEnumTypeHandler             | 指定 Enum 使用的默认 `TypeHandler` 。（新增于 3.4.5）        | 一个类型别名或全限定类名。                                   | org.apache.ibatis.type.EnumTypeHandler                |
| callSettersOnNulls                 | 指定当结果集中值为 null 的时候是否调用映射对象的 setter（map 对象时为 put）方法，这在依赖于 Map.keySet() 或 null 值进行初始化时比较有用。注意基本类型（int、boolean 等）是不能设置成 null 的。 | true \| false                                                | false                                                 |
| returnInstanceForEmptyRow          | 当返回行的所有列都是空时，MyBatis默认返回 `null`。 当开启这个设置时，MyBatis会返回一个空实例。 请注意，它也适用于嵌套的结果集（如集合或关联）。（新增于 3.4.2） | true \| false                                                | false                                                 |
| logPrefix                          | 指定 MyBatis 增加到日志名称的前缀。                          | 任何字符串                                                   | 未设置                                                |
| logImpl                            | 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。        | SLF4J \| LOG4J（3.5.9 起废弃） \| LOG4J2 \| JDK_LOGGING \| COMMONS_LOGGING \| STDOUT_LOGGING \| NO_LOGGING | 未设置                                                |
| proxyFactory                       | 指定 Mybatis 创建可延迟加载对象所用到的代理工具。            | CGLIB （3.5.10 起废弃） \| JAVASSIST                         | JAVASSIST （MyBatis 3.3 以上）                        |
| vfsImpl                            | 指定 VFS 的实现                                              | 自定义 VFS 的实现的类全限定名，以逗号分隔。                  | 未设置                                                |
| useActualParamName                 | 允许使用方法签名中的名称作为语句参数名称。 为了使用该特性，你的项目必须采用 Java 8 编译，并且加上 `-parameters` 选项。（新增于 3.4.1） | true \| false                                                | true                                                  |
| configurationFactory               | 指定一个提供 `Configuration` 实例的类。 这个被返回的 Configuration 实例用来加载被反序列化对象的延迟加载属性值。 这个类必须包含一个签名为`static Configuration getConfiguration()` 的方法。（新增于 3.2.3） | 一个类型别名或完全限定类名。                                 | 未设置                                                |
| shrinkWhitespacesInSql             | 从SQL中删除多余的空格字符。请注意，这也会影响SQL中的文字字符串。 (新增于 3.5.5) | true \| false                                                | false                                                 |
| defaultSqlProviderType             | 指定一个拥有 provider 方法的 sql provider 类 （新增于 3.5.6）. 这个类适用于指定 sql provider 注解上的`type`（或 `value`） 属性（当这些属性在注解中被忽略时）。 (e.g. `@SelectProvider`) | 类型别名或者全限定名                                         | 未设置                                                |
| nullableOnForEach                  | 为 'foreach' 标签的 'nullable' 属性指定默认值。（新增于 3.5.9） | true \| false                                                | false                                                 |
| argNameBasedConstructorAutoMapping | 当应用构造器自动映射时，参数名称被用来搜索要映射的列，而不再依赖列的顺序。（新增于 3.5.10） | true \| false                                                | false                                                 |

```xml
<settings>
  <setting name="cacheEnabled" value="true"/>
  <setting name="lazyLoadingEnabled" value="true"/>
  <setting name="aggressiveLazyLoading" value="true"/>
  <setting name="multipleResultSetsEnabled" value="true"/>
  <setting name="useColumnLabel" value="true"/>
  <setting name="useGeneratedKeys" value="false"/>
  <setting name="autoMappingBehavior" value="PARTIAL"/>
  <setting name="autoMappingUnknownColumnBehavior" value="WARNING"/>
  <setting name="defaultExecutorType" value="SIMPLE"/>
  <setting name="defaultStatementTimeout" value="25"/>
  <setting name="defaultFetchSize" value="100"/>
  <setting name="safeRowBoundsEnabled" value="false"/>
  <setting name="safeResultHandlerEnabled" value="true"/>
  <setting name="mapUnderscoreToCamelCase" value="false"/>
  <setting name="localCacheScope" value="SESSION"/>
  <setting name="jdbcTypeForNull" value="OTHER"/>
  <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
  <setting name="defaultScriptingLanguage" value="org.apache.ibatis.scripting.xmltags.XMLLanguageDriver"/>
  <setting name="defaultEnumTypeHandler" value="org.apache.ibatis.type.EnumTypeHandler"/>
  <setting name="callSettersOnNulls" value="false"/>
  <setting name="returnInstanceForEmptyRow" value="false"/>
  <setting name="logPrefix" value="exampleLogPreFix_"/>
  <setting name="logImpl" value="SLF4J | LOG4J | LOG4J2 | JDK_LOGGING | COMMONS_LOGGING | STDOUT_LOGGING | NO_LOGGING"/>
  <setting name="proxyFactory" value="CGLIB | JAVASSIST"/>
  <setting name="vfsImpl" value="org.mybatis.example.YourselfVfsImpl"/>
  <setting name="useActualParamName" value="true"/>
  <setting name="configurationFactory" value="org.mybatis.example.ConfigurationFactory"/>
</settings>
```

# 映射器（mappers）

## 1.使用相对于类路径的资源引用(推荐)

```xml
    <mappers>
         <mapper resource="com/kuang/dao/UserMapper.xml"/>
    </mappers>
```



## 2.使用映射器接口实现类的完全限定类名

```xml
    <mappers>
        <mapper class="com.kuang.dao.UserMapper"/>
    </mappers>
```

### 注意

			* 接口和Mapper必须在**同一包下**
			* 接口和Mapper必须**同名**

## 3.将包内的映射器接口全部注册为映射器

```xml
    <mappers>
        <package name="com.kuang.dao"/>
    </mappers>
```

### 注意		

   * 接口和Mapper必须在**同一包下**
   * 接口和Mapper必须**同名**

# 作用域（Scope）和生命周期

## SqlSessionFactoryBuilder

这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了。

## SqlSessionFactory

* SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。
* 因此SqlSessionFactory 最佳作用域是应用层
* 可以想象成是连接池
* 最简单就是使用单例模式或静态单例模式

## SqlSession

* 每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。	
* 连接到连接池的一个请求
* 用完即关闭



# 结果映射

User实体类

```java
public class User {
    private String id;
    private String name;
    private String pwd;
    ...
}
```

Mapper

```xml
<mapper namespace="com.kuang.dao.UserMapper">
    <!-- id="UerMap"与resultMap="UerMap"对应   type="com.kuang.pojo.User" 是实体类-->
    <resultMap id="UerMap" type="com.kuang.pojo.User">
        <result column="id" property="id"></result>
        <result column="name" property="name"></result>
        <!--column="password"的password是数据库字段  property="pwd"是User实体类属性  -->
        <result column="password" property="pwd"></result>
    </resultMap>
    <select id="getUserList" resultMap="UerMap">
        select id,name,password pwd from mybatis.user
    </select>
</mapper>
```

**作用**:将select语句查询出的pwd字段映射到User实体类的pwd属性

# 日志

| logImpl | 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。 | SLF4J \| LOG4J（3.5.9 起废弃） \| LOG4J2 \| JDK_LOGGING \| COMMONS_LOGGING \| STDOUT_LOGGING \| NO_LOGGING | 未设置 |
| ------- | ----------------------------------------------------- | ------------------------------------------------------------ | ------ |

## 1.1设置

在mybatis配置文件中配置setting

```xml
<settings>
    <setting name="logImpl" value="STDOUT_LOGGING"/>
</settings>
```

配置完后输出log

```text
Created connection 988800485.
Setting autocommit to false on JDBC Connection [com.mysql.jdbc.JDBC4Connection@3aefe5e5]
==>  Preparing: select id,name,password from mybatis.user 
==> Parameters: 
<==    Columns: id, name, password
<==        Row: 1, 1, 1
<==        Row: 2, 112, 2
<==        Row: 6, 6, 6
<==      Total: 3
User{id='1', name='1', pwd='1'}
User{id='2', name='112', pwd='2'}
User{id='6', name='6', pwd='6'}
Resetting autocommit to true on JDBC Connection [com.mysql.jdbc.JDBC4Connection@3aefe5e5]
Closing JDBC Connection [com.mysql.jdbc.JDBC4Connection@3aefe5e5]
Returned connection 988800485 to pool.
```

## 1.2Log4J

Log4J是什么

* 控制日志信息输送的目的地是[控制台](https://baike.baidu.com/item/控制台/2438626?fromModule=lemma_inlink)、文件、GUI组件，甚至是[套接口](https://baike.baidu.com/item/套接口/10058888?fromModule=lemma_inlink)服务器、NT的事件记录器、[UNIX](https://baike.baidu.com/item/UNIX/0?fromModule=lemma_inlink) [Syslog](https://baike.baidu.com/item/Syslog/0?fromModule=lemma_inlink)[守护进程](https://baike.baidu.com/item/守护进程/966835?fromModule=lemma_inlink)等
* 控制每一条日志的输出格式
* 定义每一条日志信息的级别
* 可以通过一个[配置文件](https://baike.baidu.com/item/配置文件/286550?fromModule=lemma_inlink)来灵活地进行配置

Log4J的使用

1. 导入依赖

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.30</version>
    <scope>compile</scope>
</dependency>
```

2. 配置log4j.properties

```properties
### Log4j配置 ###
log4j.rootLogger=DEBUG,console,file

#1 定义日志输出位置为控制台
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold=DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=[%p] %d{yyyy-MM-dd HH:mm:ss} [class:%c]-%m%n

#2 文件大小到达指定尺寸的时候产生一个新的文件
log4j.appender.file = org.apache.log4j.RollingFileAppender
#日志文件输出目录
log4j.appender.file.File=src/main/resources/log/log.log
#定义文件最大大小
log4j.appender.file.MaxFileSize=10mb
###输出日志信息###
#最低级别
log4j.appender.file.Threshold=INFO
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=[%p] %d{yyyy-MM-dd HH:mm:ss} [class:%c]-%m%n
#4 mybatis 显示SQL语句部分
log4j.logger.org.mybatis=DEBUG
log4j.logger.java.sql=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.ResultSet=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG
```

3. 在mybatis配置文件中配置setting

```xml
<settings>
    <setting name="logImpl" value="log4j"/>
</settings>
```

4. 测试

```java
import com.kuang.dao.UserMapper;
import com.kuang.pojo.User;
import com.kuang.utils.Mybatisutil;
import org.apache.ibatis.session.SqlSession;
//引入org.apache.log4j.Logger;
import org.apache.log4j.Logger;
import org.junit.Test;
import java.util.List;
public class test {
    //log的对象
    static Logger logger=Logger.getLogger(test.class);
    @Test
    public void testLog4j(){
        //log输出
        logger.info("info");
        logger.debug("debug");
        logger.error("error");
    }

}

```

# 分页

## 1.limit实现

1. 接口

```java
xxxxxxxxxx List<User> getUser	LimitList(Map map);
```

2. Mapper

```xml
<select id="getUserLimitList" resultMap="UerMap" parameterType="map">
        select * from mybatis.user limit #{startIndex},#{pageSize}
    </select>
```

3. 测试

```java
@Test
    public void getUserLimitList(){
        SqlSession session= Mybatisutil.getSqlSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        HashMap<String, Integer> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("startIndex",0);
        stringObjectHashMap.put("pageSize",2);
        List<User> userList=mapper.getUserLimitList(stringObjectHashMap);
        for (User user : userList) {
            logger.info(user);
        }
        session.close();
    }
```

## 2.RowBounds实现

1.接口

```java
List<User>getUserRowBoundsList();
```

2.mapper

```xml
<select id="getUserRowBoundsList" resultMap="UerMap">
    select * from mybatis.user
</select>
```

3.测试

```java
@Test
public void getUserRowBoundsList(){
    SqlSession sqlSession = Mybatisutil.getSqlSession();
    RowBounds rowBounds=new RowBounds(0,2);
    List<User> userList=sqlSession.selectList("com.kuang.dao.UserMapper.getUserRowBoundsList",null,rowBounds);
    for (User user : userList) {
        logger.info(user);
    }
    sqlSession.close();
}
```

## 3.使用插件PageHelper分页

https://pagehelper.github.io/



# 注解开发

## 1.注解查询

1. 添加注解

```java
@Select("select * from user")
List<User> getUserList();
```

2. 配置mybatis-config.xml的mapper

```xml
<mappers>
    <mapper class="com.kuang.dao.UserMapper"/>
</mappers>
```

3. 测试

```java
@Test
public void test(){
    SqlSession session= Mybatisutil.getSqlSession();
    UserMapper mapper=session.getMapper(UserMapper.class);
    List<User> userList=mapper.getUserList();
    for (User user:userList) {
        System.out.println(user);
    }
    session.close();
}
```

本质：反射机制实现

底层：动态代理

## 2.注解CURD

1.Mybatis自动提交事务

```java
public static SqlSession getSqlSession(){
    return sqlSessionFactory.openSession(true);
}
```

2.接口

```java
@Insert("insert into user(`id`,`name`,`password`) value(#{id},#{name},#{password})")
int addUser(User user);

@Delete("delete from user where id=#{uid}")
int deleteUser(@Param("uid") String id);
```

3.测试

```java
//注解添加User
SqlSession session= Mybatisutil.getSqlSession();
UserMapper mapper=session.getMapper(UserMapper.class);
int a=mapper.addUser(new User("5","5","5"));
//注解删除User
int b=mapper.deleteUser("6");
```

## 关于@Param

* 基本类型参数或者String需要加@Param,引用类型不需要加

* 当添加注解后（@Param("userId") int id），也就是说外部想要取出传入的id值，只需要取它的参数名***userId***就可以了。

* 不使用@Param注解时，参数只能有一个，并且是***Javabean***。必须使用的是#{}来取参数。使用${}方式取值会报错。

* @Param注解来声明参数的时候，SQL语句取值***使用#{}，${}取值都可以***。

  1. #{} 是**预编译处理**，${} 是**直接替换**
  2.  ${} 存在**SQL注入**的问题，而 #{} 不存在；

* Java的基本类型：

  - byte
  - short
  - int
  - long

  - float
  - double

  - char

  - boolean

# Lombok

## 1.在idea中安装lombok插件

## 2.导入依赖

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.28</version>
</dependency>
```

## 3.注解

```text

@Getter and @Setter
@FieldNameConstants
@ToString
@EqualsAndHashCode
@AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor
@Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger, @CustomLog
@Data
@Builder
@SuperBuilder
@Singular
@Delegate
@Value
@Accessors
@Wither
@With
@SneakyThrows
@val
@var
experimental @var
@UtilityClass
```

@Data ： 注在类上，提供类的**无参构造器、get、set、equals、hashCode、canEqual、toString方法**
@AllArgsConstructor ： 注在类上，提供类的**全参构造**
@NoArgsConstructor ： 注在类上，提供类的**无参构造**
@Setter ： 注在属性上，提供 set 方法
@Getter ： 注在属性上，提供 get 方法
@EqualsAndHashCode ： 注在类上，提供对应的 equals 和 hashCode 方法
@Log4j/@Slf4j ： 注在类上，提供对应的 Logger 对象，变量名为 log

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private String password;

}
```

# 多对一处理

## 1.按照查询嵌套处理

pojo 

```java
@Data
public class Student {
    private int id;
    private String name;
    private Teacher teacher;
}

@Data
public class Teacher {
    private int id;
    private String name;

}
```

mapper

```xml
<!--   相当于子查询 select id,name,tid from student where tid=(select * from )这么个意思
       思路：1.查询所有的学生
            2.根据查询出来的tid寻找对应的老师
    -->
<mapper namespace="com.kuang.dao.StudentMapper">
    <!--resultMap="StudentTeacher"与resultMap id="StudentTeacher"对应-->
    <select id="getStudent" resultMap="StudentTeacher">
        select * from student
    </select>
    <resultMap id="StudentTeacher" type="com.kuang.pojo.Student" >
        <result property="id" column="id"></result>
        <result property="name" column="name"></result>
        <!-- association：对象  collection：集合   这里的association是一个复杂类型的关联；许多结果将包装成这种类型 -->
        <!--property="teacher" javaType="com.kuang.pojo.Teacher"与pojo的Student的Teacher teacher属性对应-->
        <!--select="getTeacher" 与下面的select id="getTeacher"对应-->
        <association property="teacher" javaType="com.kuang.pojo.Teacher" column="tid" select="getTeacher"/>
    </resultMap>
    <select id="getTeacher" resultType="com.kuang.pojo.Teacher">
        select * from teacher where id=#{id}
    </select>

</mapper>
```

测试

```java
@Test
public void test(){
    SqlSession session= Mybatisutil.getSqlSession();
    StudentMapper mapper=session.getMapper(StudentMapper.class);
    List<Student> students=mapper.getStudent(1);
    for (Student student : students) {
        System.out.println(student);
    }
    session.close();
}

//结果为
//Student(id=1, name=小王, teacher=Teacher(id=1, name=老师))
//Student(id=2, name=小张, teacher=Teacher(id=1, name=老师))
```

## 2.按照结果嵌套处理

接口

```java
List<Student> getStudent2(@Param("id") int id);
```

mapper

```xml
<!--按照结果嵌套处理  相当于连表查询-->
    <select id="getStudent2" resultMap="StudentTeacher2" parameterType="Integer">
    select s.id sid,s.name sname,t.name tname from student s,teacher t where s.tid=t.id and t.id=#{id}
</select>
    <resultMap id="StudentTeacher2" type="com.kuang.pojo.Student">
    <result property="id" column="sid"></result>
    <result property="name" column="sname"></result>
    <association property="teacher" javaType="com.kuang.pojo.Teacher">
    <result property="name" column="tname"></result>
    </association>
    </resultMap>
```

测试

```java
@Test
public void test2(){
    SqlSession session= Mybatisutil.getSqlSession();
    StudentMapper mapper=session.getMapper(StudentMapper.class);
    List<Student> students=mapper.getStudent2(1);
    for (Student student : students) {
        System.out.println(student);
    }
    session.close();
}

//结果
//Student(id=1, name=小王, teacher=Teacher(id=0, name=老师))
//Student(id=2, name=小张, teacher=Teacher(id=0, name=老师))
```

# 一对多

pojo

```java
@Data
public class Student {
    private int id;
    private String name;
    private int tid;
}

@Data
public class Teacher {
    private int id;
    private String name;
    private List<Student> students;
}
```

## 1.按照结果嵌套处理

接口

```java
List<Teacher> getTeacher(@Param("tid")int id);  //方式1连表查询
```

Mapper

```xml
<!--方式1 连表查询 -->
<select id="getTeacher" resultMap="TeacherStudent">
    select s.id sid,s.name sname,t.name tname,t.id tid
    from teacher t,student s
    where t.id=s.tid and t.id=#{tid}
</select>
<resultMap id="TeacherStudent" type="com.kuang.pojo.Teacher">
    <result property="id" column="tid"></result>
    <result property="name" column="tname"></result>
    <!--复杂的属性我们需要单独处理 对象：association  集合：collection-->
    <!--javaType 指定属性的类型为Student-->
    <!--ofType   集合中的的类型为Student-->
    <collection property="students" ofType="com.kuang.pojo.Student">
        <result property="id" column="sid"></result>
        <result property="name" column="sname"></result>
        <result property="tid" column="tid"></result>
    </collection>
</resultMap>
```

测试

```java
@Test
public void test(){
    SqlSession session= Mybatisutil.getSqlSession();
    TeacherMapper mapper=session.getMapper(TeacherMapper.class);

    List<Teacher> students=mapper.getTeacher(1);
    for (Teacher student : students) {
        System.out.println(student);
    }

    session.close();
}
//结果Teacher(id=1, name=老师, students=[Student(id=1, name=小王, tid=1), Student(id=2, name=小张, tid=1)])
```

## 2.按照查询嵌套处理

接口

```java

List<Teacher> getTeacher2(@Param("tid")int id); //方式2子查询
```

Mapper

```xml
<!--方式2子查询 查询一个老师对应的多个学生-->
<select id="getTeacher2" resultMap="TeacherStudent2">
    select id id1,name from teacher where id=#{tid}
</select>
<resultMap id="TeacherStudent2" type="com.kuang.pojo.Teacher">
    <result property="name" column="name"></result>
     private List<Student> students;
    <!--这里的ofType="com.kuang.pojo.Student" 与Teacher实体类的private List<Student> students对应-->
    <!--这里的javaType="ArrayList" 与Teacher实体类的private List<Student> students对应-->
    <!--这里的 column="id1" 与select id id1,name from teacher where id=#{tid}对应-->
    <collection property="students" ofType="com.kuang.pojo.Student" javaType="ArrayList" column="id1" select="getTeacherByTeacherId"></collection>
</resultMap>
<select id="getTeacherByTeacherId" resultType="com.kuang.pojo.Student">
    select * from student where tid=#{tid}
</select>
```

测试

```java
@Test
public void test2(){
    SqlSession session= Mybatisutil.getSqlSession();
    TeacherMapper mapper=session.getMapper(TeacherMapper.class);

    List<Teacher> students=mapper.getTeacher2(1);
    for (Teacher student : students) {
        System.out.println(student);
    }

    session.close();
}
//结果Teacher(id=1, name=老师, students=[Student(id=1, name=小王, tid=1), Student(id=2, name=小张, tid=1)])
```

## 总结

1. 关联-association  【多对一】
2. 集合-collection      【一对多】
3. javaType & ofType
   * javaType 用来指定实体类中属性的类型  如 private List<Student> students中的List
   * ofType 用来指定映射到List或集合中的pojo类型，泛型中的约束类型 如 private List<Student> students中的Student

注意点：

1. 保证SQL的可读性
2. 注意一对多，多对一中，属性名和字段的问题

# 面试高频

1. Mysql引擎
2. InnoDB底层原理
3. 索引
4. 索引优化

# 动态SQL

什么是动态SQL：根据不同的条件生成不用SQL

- if
- choose (when, otherwise)
- trim (where, set)
- foreach



## if

表结构

```sql
CREATE TABLE `blog` (
  `id` varchar(50) NOT NULL COMMENT 'id',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `author` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `views` int NOT NULL COMMENT '浏览量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
```

表数据

```text
1                               	title	title	2023-08-05 14:07:24	1
5382138f3f5248d38d4e1f387953a944	title	wang	2023-08-05 13:47:31	200
c0ab8031c9064d0195e76aa44d4fa6cc	title	wang	2023-08-05 13:44:04	500
e756dbaa13ff4d57bf570ad2be247711	title	wang	2023-08-05 13:48:09	600
```

mybatis-config.xml

```xml
<settings>
    <!--一般数据库字段不缺分大小写，所以一般使用下划线分割，比如create_time，但是在Java类中我们规范使用驼峰命名为createTime,在不使用xml中resultMap标签的配置情况下，mybatis提供mapUnderscoreToCamelCase属性设置自动转换驼峰命名。-->
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

实体类

```java
@Data
public class Blog {
    private String id;
    private String title;
    private String author;
    private Date createTime;//属性名和数据库字段名不一致
    private int views;
}
```

接口

```java
public interface BlogMapper {
    List<Blog> qureyBlog(Map map);
}
```

Mapper 若title不为空，添加title查询条件，若author不为空，添加author查询条件

```xml
<mapper namespace="com.kuang.dao.BlogMapper">
    <select id="qureyBlog" parameterType="map" resultType="com.kuang.pojo.Blog">
        select * from blog where 1=1
        <if test="title!=null">
            and title = #{title}
        </if>
        <if test="author!=null">
            and author = #{author}
        </if>
    </select>
</mapper>
```

测试

```java
@Test
    public void testQueryBlogIfa(){
        SqlSession session= Mybatisutil.getSqlSession();
        BlogMapper mapper=session.getMapper(BlogMapper.class);
        Map map= new HashMap<>();
        map.put("title","title");
        List<Blog> list=mapper.qureyBlog(map);
        for (Blog blog : list) {
            System.out.println(blog);
        }
        session.close();
    }

/*结果
Blog(id=1, title=title, author=title, createTime=Sat Aug 05 14:07:24 CST 2023, views=1)
Blog(id=5382138f3f5248d38d4e1f387953a944, title=title, author=wang, createTime=Sat Aug 05 13:47:31 CST 2023, views=200)
Blog(id=c0ab8031c9064d0195e76aa44d4fa6cc, title=title, author=wang, createTime=Sat Aug 05 13:44:04 CST 2023, views=500)
Blog(id=e756dbaa13ff4d57bf570ad2be247711, title=title, author=wang, createTime=Sat Aug 05 13:48:09 CST 2023, views=600)
**/
```

## choose、when、otherwise

Mapper

```xml
<select id="qureyBlogChoose" parameterType="map" resultType="com.kuang.pojo.Blog">
    select * from blog where 1=1
    <choose>
        <when test="title!=null">
            and title = #{title}
        </when>
        <when test="author!=null">
            and author = #{author}
        </when>
        <otherwise>
            and 1=1
        </otherwise>
    </choose>
</select>
```

解释：相当与java的switch case  default

## trim、where、set

where的mapper

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG
  <where>
    <if test="state != null">
         state = #{state}
    </if>
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
  </where>
</select>
```

解释:<where> 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，*where* 元素也会将它们去除。

set的Mapper

```xml
<update id="qureyBlogSet" >
    update blog
    <set>
        id='asd1',
        author = '2'
    </set>
    <where>
        id='asd'
    </where>
</update>
```

解释： <set>会动态地在行首插入 SET 关键字，并会删掉额外的逗号（这些逗号是在使用条件语句给列赋值时引入的）

trim的Mapper

```xml
//相当于<set>
<trim prefix="SET" suffixOverrides=",">
  ...
</trim>
//相当于<where>
<trim prefix="WHERE" prefixOverrides="AND |OR ">
  ...
</trim>
```



## SQL片段

有时候可能需要将公共部分提取出来方便复用

1. 复用部分

```xml
<sql id="includIf">
    <if test="title!=null">
        and title = #{title}
    </if>
    <if test="author!=null">
        and author = #{author}
    </if>
</sql>
```

2. 在需要引用的地方使用<include>标签引用即可

```xml
<select id="qureyBlogInclud" parameterType="map" resultType="com.kuang.pojo.Blog">
    select * from blog where 1=1
    <include refid="includIf">
    </include>
</select>
```

注意：

* 最好时基于单表来定义的SQL片段
* 不要存在<where>标签



## foreach

mapper

```xml
<select id="qureyBlogForeach" resultType="com.kuang.pojo.Blog" parameterType="map">
    SELECT * FROM BLOG
    <where>
        id in
        <foreach collection="ids" open="(" separator="," close=")" item="id">
            #{id}
        </foreach>
    </where>
</select>
```

接口

```java
List<Blog> qureyBlogForeach(Map map);
```

测试

```java
@Test
    public void testQueryBlogForeach(){
        SqlSession session= Mybatisutil.getSqlSession();
        BlogMapper mapper=session.getMapper(BlogMapper.class);
        ArrayList<Integer> ids=new ArrayList<>();
        ids.add(1);
        ids.add(2);
        Map map= new HashMap<>();
        map.put("ids",ids);
        List<Blog> list=mapper.qureyBlogForeach(map);
        for (Blog blog : list) {
            System.out.println(blog);
        }
        session.close();
    }
```

你可以将任何可迭代对象（如 List、Set 等）、Map 对象或者数组对象作为集合参数传递给 *foreach*。当使用可迭代对象或者数组时，index 是当前迭代的序号，item 的值是本次迭代获取到的元素。当使用 Map 对象（或者 Map.Entry 对象的集合）时，index 是键，item 是值。

# 缓存

## 一级缓存

一级缓存也叫本地缓存  SqlSession

* 与数据库同一次会话期间查询到的数据会放在本地缓存
* 以后如果要取相同的数据会直接在缓存中取

缓存失效的情况

* 查询不同的数据
* 增删改操作时会刷新缓存
* 手动清理缓存 

小结：一级缓存时默认开启的，只在一次SQL Session中有效，也就是拿到连接到连接结束这区间

## 二级缓存

* 二级缓存也叫全局缓存
* 基于namespace级别的缓存，一个命名空间对应一个二级缓存

开启缓存

```xml
<!--显式开启全局缓存-->
<setting name="cacheEnabled" value="true"/>
```

在Mapper.xml使用二级缓存

```xml
<cache/>
```

小结：

* 只要开了了二级缓存，在同一个Mapper下就有效
* 所有数据都会先放在一级缓存中，只有当会话提交了或关闭了才会提交到二级缓存中















