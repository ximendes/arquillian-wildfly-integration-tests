# Integration Test with Arquillian and Wildfly 

## Table of contents
* [General info](#general-info)
* [What is Arquillian](#what-is-arquillian)
* [Technologies](#technologies)
* [Configuration](#configuration)
* [Setup](#setup)

## General info
Simple project with Arquillian, Wildfly and H2 database

## What is Arquillian
Arquillian is an integration testing platform that can be used for Java testing. With the main goal of making integration tests as simple to write as unit tests, it brings the tests to the runtime environment, freeing developers from managing the runtime from within the test.
	
## Technologies
* [Arquillian 1.6.0.final](https://arquillian.org/)
* [Arquillian Persistence DBunit](http://arquillian.org/arquillian-extension-persistence/)
* [Junit 4.13](https://junit.org/junit4/)
* [Wildfly 9.0.1.final](https://www.wildfly.org/downloads/)
* H2 database
* Java 8
* Lombok

## Configuration

Arquillian Persistence help us deal with the underlying data storage.
```
<dependency>
    <groupId>org.jboss.arquillian.extension</groupId>
    <artifactId>arquillian-persistence-dbunit</artifactId>
    <version>${version.arquillian.persistence}</version>
    <scope>test</scope>
</dependency>
```

The database used is H2 defined in persistence-h2.xml and uses default datasource in wildfly:
``` java
    @Deployment
    public static Archive<?> createDeploymentPackage() {
        return ShrinkWrap.create(WebArchive.class)
                         .addPackage(Pedido.class.getPackage())
                         .addClass(PedidoService.class)
                         .addAsResource("persistence-h2.xml", "META-INF/persistence.xml");
    }
```

However, if you want use postgres instead h2, you can use persistence-postgres.xml:
```
    @Deployment
    public static Archive<?> createDeploymentPackage() {
        return ShrinkWrap.create(WebArchive.class)
                         .addPackage(Pedido.class.getPackage())
                         .addClass(PedidoService.class)
                         .addAsResource("persistence-postgres.xml", "META-INF/persistence.xml");
    }
```
In this case is necessary add postgres datasource to wilfly. And to be easyer, I add an docker-compose to run postgres database,
you only need run the commnad bellow in the root project:
```
docker-compose up -d
```

To clean database we define clean-database.sql scripts in @CleanupUsingScript anotation, in class or method.
By default DBunit store script files in /scripts
``` java
@CleanupUsingScript("clean-database.sql")
```

To set initial data we define init-database.xml in @UsingDataSet anotation, in class or method.
By default Arquillian store dataset files in /datasets
``` java
@CleanupUsingScript("init-database.xml")
```

When test is run, wildfly is downloaded in target folder, wich is defined in pom.xml.
```
 <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>2.8</version>
    <executions>
        <execution>
            <id>unpack</id>
            <phase>process-test-classes</phase>
            <goals>
                <goal>unpack</goal>
            </goals>
            <configuration>
                <artifactItems>
                    <artifactItem>
                        <groupId>org.wildfly</groupId>
                        <artifactId>wildfly-dist</artifactId>
                        <version>9.0.1.Final</version>
                        <type>zip</type>
                        <overWrite>false</overWrite>
                        <outputDirectory>target</outputDirectory>
                    </artifactItem>
                </artifactItems>
            </configuration>
        </execution>
    </executions>
</plugin>
```

If you want to use your own wildfly you can remove the code above and change tag in pom.xml
```
<jboss.home>${project.basedir}/target/wildfly-9.0.1.Final</jboss.home>
<module.path>${project.basedir}/target/wildfly-9.0.1.Final/modules</module.path>
```
to
```
<jboss.home>${user.home}/your-wildfly-directory</jboss.home>
<module.path>${user.home}/your-wildfly-directory</module.path>
```

And in arquillian.xml
```
<property name="jbossHome">target/wildfly-9.0.1.Final</property>
```
to
```
<property name="jbossHome">${user.home}/your-wildfly-directory</property>
```

Note that in the pom.xml file contains the wildFly profile it has the id “wildfly-managed”
```
<id>wildfly-managed</id>
```
 Which in turn has a link to the qualifier "wildfly-managed" in the arquillian.xml file
```
    <container qualifier="wildfly-managed" default="true">
        <configuration>
            <property name="jbossHome">target/wildfly-9.0.1.Final</property>
        </configuration>
    </container>
```
It is important to keep the same name, because it helps Arquillian to identify the container to be called for execution.

## Setup
To run this project:

```
$ git clone https://github.com/ximendes/arquillian-wildfly-integration-tests.git
$ cd arquillian-wildfly-integration-tests
$ mvn clean install -Pwildfly-managed
```
