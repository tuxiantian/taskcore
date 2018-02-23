这是一个任务管理项目。  
安装私有jar包到maven仓库示例如下：
```
mvn install:install-file -Dfile=E:/webfront/ckeditor-java-core-3.5.3/ckeditor-java-core-3.5.3.jar -DgroupId=com.ckeditor -DartifactId=ckeditor-java-core -Dversion=3.6.6.2 -Dpackaging=jar  -DgeneratePom=true -DcreateChecksum=true
<dependency>
	<groupId>com.ckeditor</groupId>
	<artifactId>ckeditor-java-core</artifactId>
	<version>3.6.6.2</version>
</dependency>
```
WEB-INF/lib目录下面的私有jar包可以采用上面的方式安装。  
在使用过程中发现，若先在代码中删除现有的job,启动项目后会报错。所以若想清理项目中废弃的job，需要先在管理界面删除job，然后删除代码。
