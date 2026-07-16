package com.library.config;

import com.library.entity.Book;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public DatabaseSeeder(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedBooks();
    }

    private void seedUsers() {
        // Seed Aditi Raj
        if (userRepository.findByEmail("aditi@example.com").isEmpty()) {
            User u1 = new User();
            u1.setName("Aditi Raj");
            u1.setEmail("aditi@example.com");
            u1.setPhone("9876543210");
            u1.setPassword("aditi123");
            userRepository.save(u1);
            System.out.println("Seeded member: aditi@example.com (pass: aditi123)");
        }

        // Seed John Doe
        if (userRepository.findByEmail("john@example.com").isEmpty()) {
            User u2 = new User();
            u2.setName("John Doe");
            u2.setEmail("john@example.com");
            u2.setPhone("8765432109");
            u2.setPassword("john123");
            userRepository.save(u2);
            System.out.println("Seeded member: john@example.com (pass: john123)");
        }

        // Seed Jane Smith
        if (userRepository.findByEmail("jane@example.com").isEmpty()) {
            User u3 = new User();
            u3.setName("Jane Smith");
            u3.setEmail("jane@example.com");
            u3.setPhone("7654321098");
            u3.setPassword("jane123");
            userRepository.save(u3);
            System.out.println("Seeded member: jane@example.com (pass: jane123)");
        }
    }

    private void seedBooks() {
        if (bookRepository.count() < 40) {
            System.out.println("Seeding 50 books with cover pages and info...");
            List<Book> books = new ArrayList<>();

            // 1-10: Tech & Software Engineering
            books.add(createBook("Clean Code", "Robert C. Martin", "9780132350884", 5, 
                "Even bad code can function. But if code isn't clean, it can bring a development organization to its knees.", 
                "https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg", 2008, "Software Engineering"));
            
            books.add(createBook("The Pragmatic Programmer", "Andy Hunt & Dave Thomas", "9780135957059", 4, 
                "Direct, practical advice for software developers to help them write better, more maintainable code.", 
                "https://covers.openlibrary.org/b/isbn/9780135957059-L.jpg", 1999, "Software Engineering"));
            
            books.add(createBook("Design Patterns", "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides", "9780201633610", 3, 
                "A catalog of simple and succinct solutions to commonly occurring design problems in object-oriented systems.", 
                "https://covers.openlibrary.org/b/isbn/9780201633610-L.jpg", 1994, "Software Engineering"));
            
            books.add(createBook("Introduction to Algorithms", "Thomas H. Cormen", "9780262033848", 6, 
                "The bible of computer science algorithms, covering design, analysis, and data structures.", 
                "https://covers.openlibrary.org/b/isbn/9780262033848-L.jpg", 1990, "Computer Science"));
            
            books.add(createBook("Refactoring", "Martin Fowler", "9780201485677", 4, 
                "Improving the design of existing code without changing its external behavior.", 
                "https://covers.openlibrary.org/b/isbn/9780201485677-L.jpg", 1999, "Software Engineering"));
            
            books.add(createBook("Clean Architecture", "Robert C. Martin", "9780134494166", 5, 
                "A practical guide to software structure, design, principles, patterns, and system boundaries.", 
                "https://covers.openlibrary.org/b/isbn/9780134494166-L.jpg", 2017, "Software Engineering"));
            
            books.add(createBook("Code Complete", "Steve McConnell", "9780735619678", 3, 
                "Widely considered one of the best practical guides to programming and code construction.", 
                "https://covers.openlibrary.org/b/isbn/9780735619678-L.jpg", 1993, "Software Engineering"));
            
            books.add(createBook("The Mythical Man-Month", "Frederick P. Brooks Jr.", "9780201835953", 4, 
                "Influential essays on software engineering and project management.", 
                "https://covers.openlibrary.org/b/isbn/9780201835953-L.jpg", 1975, "Project Management"));
            
            books.add(createBook("Domain-Driven Design", "Eric Evans", "9780321125217", 3, 
                "Tackling complexity in the heart of software by matching design to real-world domain models.", 
                "https://covers.openlibrary.org/b/isbn/9780321125217-L.jpg", 2003, "Software Engineering"));
            
            books.add(createBook("Working Effectively with Legacy Code", "Michael Feathers", "9780131177055", 3, 
                "Strategies for making code changes, writing tests, and refactoring in existing systems without test suites.", 
                "https://covers.openlibrary.org/b/isbn/9780131177055-L.jpg", 2004, "Software Engineering"));

            // 11-20: Sci-Fi & Fantasy
            books.add(createBook("Dune", "Frank Herbert", "9780441172719", 6, 
                "Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides, who would become the Messiah.", 
                "https://covers.openlibrary.org/b/isbn/9780441172719-L.jpg", 1965, "Sci-Fi"));
            
            books.add(createBook("Neuromancer", "William Gibson", "9780441569595", 4, 
                "The classic cyberpunk novel that coined the term 'cyberspace' and predicted the rise of the internet.", 
                "https://covers.openlibrary.org/b/isbn/9780441569595-L.jpg", 1984, "Sci-Fi"));
            
            books.add(createBook("The Hobbit", "J.R.R. Tolkien", "9780048231888", 5, 
                "Bilbo Baggins, a home-loving hobbit, is whisked away on a quest to raid the treasure-hoard of Smaug.", 
                "https://covers.openlibrary.org/b/isbn/9780048231888-L.jpg", 1937, "Fantasy"));
            
            books.add(createBook("The Fellowship of the Ring", "J.R.R. Tolkien", "9780618346257", 5, 
                "The first part of the epic masterpiece Lord of the Rings, following Frodo's quest to destroy the One Ring.", 
                "https://covers.openlibrary.org/b/isbn/9780618346257-L.jpg", 1954, "Fantasy"));
            
            books.add(createBook("Fahrenheit 451", "Ray Bradbury", "9780743247221", 4, 
                "A dystopian story of a future society where books are outlawed and firemen burn any that are found.", 
                "https://covers.openlibrary.org/b/isbn/9780743247221-L.jpg", 1953, "Sci-Fi"));
            
            books.add(createBook("Foundation", "Isaac Asimov", "9780553293357", 4, 
                "A mathematician predicts the fall of the Galactic Empire and creates a foundation to preserve knowledge.", 
                "https://covers.openlibrary.org/b/isbn/9780553293357-L.jpg", 1951, "Sci-Fi"));
            
            books.add(createBook("Ender's Game", "Orson Scott Card", "9780312932084", 5, 
                "Child prodigy Ender Wiggin is trained in orbital battle schools to lead the defense against alien invaders.", 
                "https://covers.openlibrary.org/b/isbn/9780312932084-L.jpg", 1985, "Sci-Fi"));
            
            books.add(createBook("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", "9780345391803", 6, 
                "Arthur Dent is rescued from Earth right before it is demolished to make way for a hyperspace bypass.", 
                "https://covers.openlibrary.org/b/isbn/9780345391803-L.jpg", 1979, "Sci-Fi / Humor"));
            
            books.add(createBook("Brave New World", "Aldous Huxley", "9780060850524", 5, 
                "A dystopian future where citizens are engineered, conditioned, and drugged to maintain social stability.", 
                "https://covers.openlibrary.org/b/isbn/9780060850524-L.jpg", 1932, "Sci-Fi"));
            
            books.add(createBook("1984", "George Orwell", "9780451524935", 8, 
                "Winston Smith struggles against the totalitarian regime of Big Brother, where thoughtcrime is punishable by death.", 
                "https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg", 1949, "Sci-Fi / Classics"));

            // 21-30: Classics
            books.add(createBook("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", 5, 
                "The story of the mysteriously wealthy Jay Gatsby and his love for the beautiful Daisy Buchanan.", 
                "https://covers.openlibrary.org/b/isbn/9780743273565-L.jpg", 1925, "Classics"));
            
            books.add(createBook("To Kill a Mockingbird", "Harper Lee", "9780061120084", 6, 
                "The childhood memories of Scout Finch in Alabama, dealing with racial injustice and her father Atticus.", 
                "https://covers.openlibrary.org/b/isbn/9780061120084-L.jpg", 1960, "Classics"));
            
            books.add(createBook("Pride and Prejudice", "Jane Austen", "9780141439518", 5, 
                "The tempestuous relationship between Elizabeth Bennet and the rich, proud Mr. Darcy.", 
                "https://covers.openlibrary.org/b/isbn/9780141439518-L.jpg", 1813, "Classics"));
            
            books.add(createBook("Moby Dick", "Herman Melville", "9781503280786", 3, 
                "Captain Ahab's obsessive quest for revenge against the giant white whale that bit off his leg.", 
                "https://covers.openlibrary.org/b/isbn/9781503280786-L.jpg", 1851, "Classics"));
            
            books.add(createBook("Hamlet", "William Shakespeare", "9780743477123", 4, 
                "The tragic story of Hamlet, Prince of Denmark, who seeks revenge on his uncle for murdering his father.", 
                "https://covers.openlibrary.org/b/isbn/9780743477123-L.jpg", 1603, "Drama / Classics"));
            
            books.add(createBook("The Odyssey", "Homer", "9780140268867", 3, 
                "The legendary journey of Odysseus trying to return home to Ithaca after the Trojan War.", 
                "https://covers.openlibrary.org/b/isbn/9780140268867-L.jpg", -800, "Epic Poetry"));
            
            books.add(createBook("Crime and Punishment", "Fyodor Dostoevsky", "9780140449136", 4, 
                "The mental anguish and moral dilemmas of Raskolnikov, an impoverished ex-student who kills a pawnbroker.", 
                "https://covers.openlibrary.org/b/isbn/9780140449136-L.jpg", 1866, "Classics"));
            
            books.add(createBook("The Catcher in the Rye", "J.D. Salinger", "9780316769174", 5, 
                "The classic story of teenage alienation and rebellion, narrated by the iconic Holden Caulfield.", 
                "https://covers.openlibrary.org/b/isbn/9780316769174-L.jpg", 1951, "Classics"));
            
            books.add(createBook("The Picture of Dorian Gray", "Oscar Wilde", "9780141439570", 4, 
                "A young man sells his soul so that a portrait of him will age and show his sins while he stays young.", 
                "https://covers.openlibrary.org/b/isbn/9780141439570-L.jpg", 1890, "Classics"));
            
            books.add(createBook("Frankenstein", "Mary Shelley", "9780141439471", 4, 
                "Victor Frankenstein creates a living creature from body parts, with tragic consequences.", 
                "https://covers.openlibrary.org/b/isbn/9780141439471-L.jpg", 1818, "Gothic Horror"));

            // 31-40: Self-Help & Personal Growth
            books.add(createBook("Atomic Habits", "James Clear", "9780735211292", 8, 
                "An easy and proven way to build good habits and break bad ones, using small, incremental improvements.", 
                "https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg", 2018, "Self-Help"));
            
            books.add(createBook("The 7 Habits of Highly Effective People", "Stephen R. Covey", "9781451639612", 6, 
                "A step-by-step pathway for living with fairness, integrity, honesty, and human dignity.", 
                "https://covers.openlibrary.org/b/isbn/9781451639612-L.jpg", 1989, "Self-Help"));
            
            books.add(createBook("Thinking, Fast and Slow", "Daniel Kahneman", "9780374275631", 5, 
                "A detailed analysis of the two systems of human thought: fast, intuitive, and slow, deliberative.", 
                "https://covers.openlibrary.org/b/isbn/9780374275631-L.jpg", 2011, "Psychology"));
            
            books.add(createBook("The Power of Habit", "Charles Duhigg", "9780812981605", 4, 
                "Why habits exist, how they control our lives, and how we can change them for personal and business success.", 
                "https://covers.openlibrary.org/b/isbn/9780812981605-L.jpg", 2012, "Self-Help"));
            
            books.add(createBook("Mindset: The New Psychology of Success", "Carol S. Dweck", "9780345472328", 5, 
                "How our beliefs about our capabilities (fixed vs. growth mindset) determine our success and development.", 
                "https://covers.openlibrary.org/b/isbn/9780345472328-L.jpg", 2006, "Self-Help"));
            
            books.add(createBook("Start With Why", "Simon Sinek", "9781591846444", 5, 
                "How great leaders inspire everyone to take action by starting with the fundamental question of 'Why'.", 
                "https://covers.openlibrary.org/b/isbn/9781591846444-L.jpg", 2009, "Business / Leadership"));
            
            books.add(createBook("Quiet", "Susan Cain", "9780307352156", 4, 
                "The power of introverts in a world that can't stop talking.", 
                "https://covers.openlibrary.org/b/isbn/9780307352156-L.jpg", 2012, "Psychology"));
            
            books.add(createBook("Deep Work", "Cal Newport", "9781455586691", 6, 
                "Rules for focused success in a distracted world, helping you achieve peak cognitive performance.", 
                "https://covers.openlibrary.org/b/isbn/9781455586691-L.jpg", 2016, "Productivity"));
            
            books.add(createBook("Grit", "Angela Duckworth", "9781501111105", 4, 
                "The power of passion and perseverance, proving that effort and long-term commitment matter more than talent.", 
                "https://covers.openlibrary.org/b/isbn/9781501111105-L.jpg", 2016, "Self-Help"));
            
            books.add(createBook("Outliers", "Malcolm Gladwell", "9780316017923", 5, 
                "The story of success, exploring the hidden factors that make some people high achievers.", 
                "https://covers.openlibrary.org/b/isbn/9780316017923-L.jpg", 2008, "Sociology"));

            // 41-50: Popular Non-Fiction & Business
            books.add(createBook("Sapiens", "Yuval Noah Harari", "9780062316097", 8, 
                "A brief history of humankind, tracing our evolution from ancient hominids to the modern technological age.", 
                "https://covers.openlibrary.org/b/isbn/9780062316097-L.jpg", 2011, "History"));
            
            books.add(createBook("Homo Deus", "Yuval Noah Harari", "9780062464316", 5, 
                "An examination of the future of humanity, looking at biotechnology, AI, and our quest for immortality.", 
                "https://covers.openlibrary.org/b/isbn/9780062464316-L.jpg", 2015, "Futurism"));
            
            books.add(createBook("Zero to One", "Peter Thiel", "9780804139298", 6, 
                "Notes on startups, or how to build the future by creating completely new products rather than copying.", 
                "https://covers.openlibrary.org/b/isbn/9780804139298-L.jpg", 2014, "Business"));
            
            books.add(createBook("The Lean Startup", "Eric Ries", "9780307887894", 6, 
                "How modern entrepreneurs use continuous innovation to create radically successful businesses.", 
                "https://covers.openlibrary.org/b/isbn/9780307887894-L.jpg", 2011, "Business"));
            
            books.add(createBook("The Innovator's Dilemma", "Clayton M. Christensen", "9780875845852", 3, 
                "The revolutionary book that explains how great companies can fail by doing everything right if they miss disruptive tech.", 
                "https://covers.openlibrary.org/b/isbn/9780875845852-L.jpg", 1997, "Business"));
            
            books.add(createBook("Good to Great", "Jim Collins", "9780066620992", 5, 
                "Why some companies make the leap from average to outstanding, and others don't.", 
                "https://covers.openlibrary.org/b/isbn/9780066620992-L.jpg", 2001, "Business"));
            
            books.add(createBook("Freakonomics", "Steven D. Levitt & Stephen J. Dubner", "9780060731328", 5, 
                "A rogue economist explores the hidden side of everything, looking at the incentives behind human decisions.", 
                "https://covers.openlibrary.org/b/isbn/9780060731328-L.jpg", 2005, "Economics"));
            
            books.add(createBook("Educated", "Tara Westover", "9780399588174", 4, 
                "An unforgettable memoir of a young girl who leaves her survivalist family to seek an education, earning a PhD.", 
                "https://covers.openlibrary.org/b/isbn/9780399588174-L.jpg", 2018, "Memoir"));
            
            books.add(createBook("The Immortal Life of Henrietta Lacks", "Rebecca Skloot", "9781400052172", 4, 
                "The story of Henrietta Lacks, whose cells (HeLa) were taken without consent and became vital to modern medicine.", 
                "https://covers.openlibrary.org/b/isbn/9781400052172-L.jpg", 2010, "Science / History"));
            
            books.add(createBook("Shoe Dog", "Phil Knight", "9781501135910", 6, 
                "A candid memoir by the creator of Nike, detailing the startup struggles and ultimate triumph of the brand.", 
                "https://covers.openlibrary.org/b/isbn/9781501135910-L.jpg", 2016, "Biography / Business"));

            bookRepository.saveAll(books);
            System.out.println("Successfully seeded " + books.size() + " books into the database!");
        }
    }

    private Book createBook(String title, String author, String isbn, Integer copies, 
                            String description, String coverUrl, Integer year, String genre) {
        Book b = new Book();
        b.setTitle(title);
        b.setAuthor(author);
        b.setIsbn(isbn);
        b.setTotalCopies(copies);
        b.setAvailableCopies(copies);
        b.setDescription(description);
        b.setCoverUrl(coverUrl);
        b.setPublishedYear(year);
        b.setGenre(genre);
        return b;
    }
}
