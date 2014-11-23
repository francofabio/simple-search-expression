#simple-search-expression
This is a Java library to parse search expression like used in gmail.

_<sub>My english is terrible, therefore, any revision will be welcome</sub>_

##What is it?
The simple search expression is a string literal that defines a search criteria using the fields and values.

**Example 1**
```
name:John
```

In this example, the simple search expression defines that user want search for all records whose name is or contains _John_. **Like operation**

**Example 2**
```
age:18-31
```
In this example, the search expression defines that user want search for all records whose _age_ is between 18 and 31. **Interval operation**

**Example 3**
```
name:john age:18-31
```
This example joins two simple expression. The logical operator applied is _and_.<br/>
The search expression defines that user want search for all records whose _name_ contains _john_ (like operation) and _age_ is between 18 and 31 (interval operation).

**Example 4**
```
id:100,350,500
```
In this example, the search expression defines that user want search for all records whose id is 100 or 350 or 500. **In operation**

##The expression
The simple search expression is defined as a simple string, using colon as field and value separator.<br/>
It is possible to use an expression without informing the field. In this case, must be specified in the execution plan of the search expression (SimpleSearchExpressionPlan) the rules to resolve default fields.<br/>
Currently only the operators LIKE, EQUALS, INTERVAL and IN are supported.</br>
The definition of operator depends on the field type and/or expression value.
####Operators
The simple search expression supports LIKE, EQUALS, INTERVAL (-) and IN (,) operators, but the operator to apply on the expression, depends on the field type.

####Default operator resolver by field type
|Type            |Operator|Comments|
|----------------|--------|--------|
|java.lang.String|LIKE    |By default string not support other relational operators.|
|java.lang.Number|EQUALS  |When comma (,) is present in expression, the _IN_ operator is used. When (-) is present in expression, the _INTERVAL_ operator is used.|
|java.util.Date  |EQUALS  |When comma (,) is present in expression, the _IN_ operator is used. When (-) is present in expression, the _INTERVAL_ operator is used.|
|Others          |EQUALS  |When comma (,) is present in expression, the _IN_ operator are used. Interval (-) operator is not supported.|

The default operator can be changed, passing to `SimpleSearchExpressionField` constructor the default operator desired.<br/>
To define a custom operator resolver, pass to plan constructor the custom operator resolver.

##How to use?
To use this library, you need install the package in your maven local repository:
```
mvn install
```

And add this dependency in your pom.xml

```xml
<dependency>
    <groupId>br.com.binarti</groupId>
    <artifactId>simple-search-expression</artifactId>
    <version>1.0.1</version> <!-- or a last version -->
</dependency>
```
This library yet is not available in maven central repository, it is coming son.<br/>
If you not use maven. You can also build the jar and add in classpath of the Java app.<br/>
To build jar package, you need install maven and execute the command:
```
mvn clean package
```
The jar package is available in target/ directory.

##Quick start
Parse simple search expression.
```java
//This is a search expression for find a record where id is 10
String simpleSearchExpression = "id:10";
SimpleSearchExpressionField idExprField = new SimpleSearchExpressionField("id", Integer.class);
SimpleSearchExpressionPlan searchPlan = new SimpleSearchExpressionPlan(idExprField);
SimpleSearchParser simpleSearchParser = new SimpleSearchParser(searchPlan);
//Parse simple search expression and returns the expression representation with id field, value 10 and operation EQUALS
SimpleSearchExpression searchExpr = simpleSearchParser.parse(simpleSearchExpression);
```
The above example is not very useful, it is most useful when used together with a persistence library.

###MongoDB
To use simple search expression with mongodb, you have add the dependency of the mongodb driver library in your pom or classpath.

**pom.xml**
```xml
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongo-java-driver</artifactId>
    <version>2.11.3</version> <!-- The min version to use -->
</dependency>
```
Now, using mongodb expression builder.
```java
import br.com.binarti.simplesearchexpr.SimpleSearchExpressionField;
import br.com.binarti.simplesearchexpr.SimpleSearchExpressionPlan;
import br.com.binarti.simplesearchexpr.SimpleSearchParser;
import br.com.binarti.simplesearchexpr.SimpleSearchExpression;
import br.com.binarti.simplesearchexpr.builders.mongodb.SimpleSearchMongoDBBuilder;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

...

//This is a search expression for find all records where name contains john galt
String simpleSearchExpression = "name:john galt";
SimpleSearchExpressionPlan searchPlan = new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("name", String.class));
SimpleSearchParser simpleSearchParser = new SimpleSearchParser(searchPlan);
SimpleSearchExpression searchExpr = simpleSearchParser.parse(simpleSearchExpression);

//Build a mongodb query
DBObject query = new SimpleSearchMongoDBBuilder().build(searchExpr);

MongoClient mongoClient = new MongoClient("localhost", 27017);
DB db = mongoClient.getDB("mydb");
DBCollection personCollection = db.getCollection("person");
//Execute search query in mongodb
DBCursor cursor = db.find(query);
```

###Java Persistence API - JPA
To use simple search expression with JPA, you have add the dependency of a persistence provider in your pom or classpath.
The builder for JPA only supports JPQL (JPA Query Language). JPA Criteria yet is not supported.

Now, using JPA (JPQL) expression builder.
```java
import br.com.binarti.simplesearchexpr.SimpleSearchExpressionField;
import br.com.binarti.simplesearchexpr.SimpleSearchExpressionPlan;
import br.com.binarti.simplesearchexpr.SimpleSearchParser;
import br.com.binarti.simplesearchexpr.SimpleSearchExpression;
import br.com.binarti.simplesearchexpr.builders.jpql.SimpleSearchJPQLBuilder;

import javax.persistence.Query;

...

//This is a search expression for find all records where name contains john galt
String simpleSearchExpression = "name:john galt";
SimpleSearchExpressionPlan searchPlan = new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("name", String.class));
SimpleSearchParser simpleSearchParser = new SimpleSearchParser(searchPlan);
SimpleSearchExpression searchExpr = simpleSearchParser.parse(simpleSearchExpression);

//Build a JPQL where clause
JPQLWhereClause whereClause = new SimpleSearchJPQLBuilder().build(searchExpr);

//Joins the from and where clauses
String jpql = whereClause.join("from Person");
Query jpaQuery = em.createQuery(jpql, Person.class);
//Set parameter values in query
whereClause.applyParameters(jpaQuery);

List<Person> result = jpaQuery.getResultList();
```

##Using defaults field resolving
The simple search expression supports field resolving according to the contents of the search expression. To use defaults field resolving, pass to `SimpleSearchExpressionPlan` constructor a Map containing the rules to resolve field according to the search expression.

**Example**
```java
final Map<String, String> defaults = new HashMap<String, String>() {
    {
        //if the value of the search did not match any of the rules, so we will use the standard field set by the key __default__
        put("__default__", "name");
        //Match numbers with length 1 to 10. 
        put("id", "^[0-9]{1,10}$");
    }
};
List<SimpleSearchExpressionField> fields = Arrays.asList(new SimpleSearchExpressionField("id", Integer.class), new SimpleSearchExpressionField("name", String.class));
SimpleSearchExpressionPlan simpleSearchPlan = new SimpleSearchExpressionPlan(defaults, fields);
SimpleSearchParser simpleSearchParser = new SimpleSearchParser(simpleSearchPlan);
```

When parse the expression _john_, the name field are resolved.
```java
SimpleSearchExpression simpleSearchExpr = simpleSearchParser.parse("john");
```
The _`simpleSearchExpr`_ contains one operation for field _name_ with relational operator LIKE and value john.

When parse the expression _198900_, the id field are resolved.
```java
SimpleSearchExpression simpleSearchExpr = simpleSearchParser.parse("198900");
```
The _`simpleSearchExpr`_ contains one operation for field _id_ with relational operator EQUALS and value 198900.
