package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//		SpringApplication.run(DemoApplication.class, args);
		String url = "jdbc:mysql://localhost:3306/security";
		String username = "root";
		String password = "root";
		try(Connection conn = DriverManager.getConnection(url, username, password)){
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM user");
			ResultSet resultSet = preparedStatement.executeQuery();

			//Use our Awesome method to map from resultset to list of entity
			List<User> users = mapResultSetToEntity(User.class, resultSet);
		}
	}

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

}
