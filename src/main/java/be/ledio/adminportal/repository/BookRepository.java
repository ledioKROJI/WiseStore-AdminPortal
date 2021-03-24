package be.ledio.adminportal.repository;

import org.springframework.data.repository.CrudRepository;

import be.ledio.adminportal.model.Book;

public interface BookRepository extends CrudRepository<Book, Long>{
	
}
