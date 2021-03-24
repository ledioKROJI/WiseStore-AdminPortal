package be.ledio.adminportal.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import be.ledio.adminportal.model.Book;
import be.ledio.adminportal.service.BookService;

@Controller

// In this class we can define any kind of mapping and not add them to the SecurityConfig PUBLIC-MACHTERS list
// in order to keep a logic to the application requests and redirects but disable an anonymous user to 
// acces thos RequestMapping from the browser, they must be logged in in order to openly access thoses matchers
public class BookController {

	@Autowired
	BookService bookService;

	@RequestMapping(value = "/add-book", method = RequestMethod.GET)
	public String addBook(Model model) {
		Book book = new Book();
		model.addAttribute("book", book);
		return "add-book";
	}

	@RequestMapping(value = "/add-book", method = RequestMethod.POST)
	public String addBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {
		// We need to persist the book at the beginning because we are using its ID to
		// generate an image name ex : "120.png"
		bookService.save(book);

		MultipartFile bookImage = book.getBookImage();

		try {
			byte[] bytes = bookImage.getBytes();
			String name = book.getId() + ".png";
			BufferedOutputStream stream = new BufferedOutputStream(
					// When we update an image given that the path is in the IDE itself, we must re-
					// run the app...
					// usually it is saved in the could with Amazon S3B
					new FileOutputStream(new File("src/main/resources/static/image/book/" + name)));
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Here we use a redirect to drop the Book object of the next HTML page, this
		// way is there is a maching TL variable avaiable in the HTML it wont be matched
		// to the current Book instance
		return "redirect:book-list";
	}

	@RequestMapping("/book-list")
	public String bookList(Model model) {
		List<Book> bookList = bookService.findAll();
		model.addAttribute("bookList", bookList);
		return "book-list";
	}

	@RequestMapping("/book-info")
	public String bookInfo(Model model, @RequestParam("id") long id) {
		Optional<Book> optionalBook = bookService.findById(id);
		Book book = optionalBook.get();
		model.addAttribute("book", book);
		return "book-info";
	}

	@RequestMapping(value = "/update-book", method = RequestMethod.GET)
	public String updateBook(Model model, @RequestParam("id") long id) {
		Optional<Book> optionalBook = bookService.findById(id);
		Book book = optionalBook.get();
		model.addAttribute("book", book);
		return "book-update";
	}

	@RequestMapping(value = "/update-book", method = RequestMethod.POST)
	public String updateBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {
		bookService.save(book);

		MultipartFile bookImage = book.getBookImage();

		if (!bookImage.isEmpty()) {
			try {
				byte[] bytes = bookImage.getBytes();
				String name = book.getId() + ".png";

				Files.delete(Paths.get("src/main/resources/static/image/book/" + name));

				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File("src/main/resources/static/image/book/" + name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "redirect:/book-info?id=" + book.getId();
	}
}
