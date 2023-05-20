# JDBC Mapping with Reflection and Generics

In traditional JDBC programming, mapping between a `ResultSet` and a list of entities requires a significant amount of time and effort. This is where Object-Relational Mapping (ORM) frameworks come into play, providing a magical solution. However, JDBC remains an awesome and faster API. By leveraging the power of reflection and generics in Java, we can achieve a generic mapping method that is suitable for any table or entity without relying on ORM frameworks and extensive configuration.

## Benefits of Using Reflection and Generics

- **Simplicity:** The generic mapping method eliminates the need for complex and repetitive code that would otherwise be required to map `ResultSet` data to entity objects manually.
- **Flexibility:** The method can be easily adapted to handle various database tables and entity types without making significant changes to the code.
- **Efficiency:** Reflection enables dynamic runtime access to class members, reducing development time and enhancing code efficiency.
- **Performance:** JDBC is known for its fast execution, and by utilizing reflection and generics, we can achieve efficient mapping without sacrificing performance.

## Getting Started

To use the generic mapping method, follow these steps:

1. **Setup your JDBC connection:** Establish a connection to your database using the appropriate JDBC driver and credentials. Ensure you have the necessary permissions to access the desired tables.

2. **Define your entity class:** Create a Java class that represents the structure of the entity you want to map the `ResultSet` to. Ensure the class members (fields) match the corresponding columns in the table.

3. **Implement the generic mapping method:** Write a Java method that utilizes reflection and generics to map the `ResultSet` to a list of entity objects. This method should take a `ResultSet` as input and return a list of entities.

4. **Execute a SQL query:** Use the JDBC API to execute a SQL query and obtain a `ResultSet` object. Pass this `ResultSet` to the generic mapping method, which will handle the mapping process automatically.

## Example Usage

Here's an example code snippet demonstrating the usage of the generic mapping method:

```java
public static void main(String[] args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
	String url = "jdbc:mysql://localhost:3306/security";
	String username = "***";
	String password = "***";
	try(Connection conn = DriverManager.getConnection(url, username, password)){
		PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM user");
		ResultSet resultSet = preparedStatement.executeQuery();

		//Use our Awesome method to map from resultset to list of entity
		List<User> users = mapResultSetToEntity(User.class, resultSet);
	}
}
```
Here's an example code of the generic mapping method:
```java
public static <E> List<E> mapResultSetToEntity(Class<E> entityType, ResultSet resultSet) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
	//Create empty list
	List<E> entities = new LinkedList<>();

	//Get fields of this entity
	Field[] fields = entityType.getDeclaredFields();

	//Iterate on resultSet
	while (resultSet.next()){
		//Create new and empty entity
		E entity = entityType.getConstructor().newInstance();
		//populate date from resultSet to entity
		for (Field field : fields) {
			String fieldName = field.getName();
			var cell = resultSet.getObject(fieldName);
			field.set(entity,cell);
		}
		//Add entity to the list
		entities.add(entity);
	}

	return entities;
}
```
