import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 *
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를
 * 유지하는 데이터베이스이다.
 */
public class MovieDB {

	public MyLinkedList<Genre> genres;
	public int numGenres;
    public MovieDB() {
        // FIXME implement this
    	genres = new MyLinkedList<>();
		numGenres = 0;
    	// HINT: MovieDBGenre 클래스를 정렬된 상태로 유지하기 위한
    	// MyLinkedList 타입의 멤버 변수를 초기화 한다.
    }

    public void insert(MovieDBItem item) {
        // FIXME implement this
        // Insert the given item to the MovieDB.
		String genre = item.getGenre();
		String title = item.getTitle();

		if (numGenres == 0) {
			//System.out.println("New genre added");
			genres.add(new Genre(genre, title));
			//add new Genre and insert Movie inside it;
			numGenres ++;
			return;
		}

		//MyLinkedListIterator<Genre> genreIterator = new MyLinkedListIterator<>(genres);
		int insert_index = -1 ;
		for(Genre g : genres) {
			if (g.getItem().equals(genre)) {
				//System.out.println("Insert in genre : " + genre);
				g.add_Movie(title);
				return;
			}
			else if (g.getItem().compareTo(genre) < 0) {
				insert_index++;
			}
			else if (g.getItem().compareTo(genre) > 0) {
				break;
			}
		}
		genres.add_index(insert_index, new Genre(genre, title));
		numGenres ++;
		//System.out.println("New genre added");

		// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
        //System.err.printf("[trace] MovieDB: INSERT [%s] [%s]\n", item.getGenre(), item.getTitle());
    }

    public void delete(MovieDBItem item) {
        // FIXME implement this
        // Remove the given item from the MovieDB.
		String genre = item.getGenre();
		String title = item.getTitle();

		for (Genre g : genres) {
			if (g.getItem().equals(genre)) {
				g.movieList.remove(title);
				if (g.movieList.isEmpty()) {
					genres.removeItem(g);
					numGenres --;
				}
				return;
			}
		}
    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
        //System.err.printf("[trace] MovieDB: DELETE [%s] [%s]\n", item.getGenre(), item.getTitle());
    }

    public MyLinkedList<MovieDBItem> search(String term) {
        // FIXME implement this
        // Search the given term from the MovieDB.
        // You should return a linked list of MovieDBItem.
        // The search command is handled at SearchCmd class.
    	
    	// Printing search results is the responsibility of SearchCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

		MyLinkedList<MovieDBItem> foundItems = new MyLinkedList<>();

		for (Genre genre : genres) {
			Node<String> curr = genre.movieList.dummyHead;
			while (curr.getNext() != null) {
				String title = curr.getNext().getItem();
				if (title.contains(term)) {
					foundItems.add(new MovieDBItem(genre.getItem(), title));
				}
				curr = curr.getNext();
			}
		}
		return foundItems;


        // This tracing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
    	//System.err.printf("[trace] MovieDB: SEARCH [%s]\n", term);

    }
    
    public MyLinkedList<MovieDBItem> items() {
        // FIXME implement this
        // Search the given term from the MovieDatabase.
        // You should return a linked list of QueryResult.
        // The print command is handled at PrintCmd class.

    	// Printing movie items is the responsibility of PrintCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

		MyLinkedList<MovieDBItem> items = new MyLinkedList<>();
		for (Genre genre : genres) {
			Node<String> curr = genre.movieList.dummyHead;
			while (curr.getNext() != null) {
				items.add(new MovieDBItem(genre.getItem(), curr.getNext().getItem()));
				curr = curr.getNext();
			}

		}

		return items;

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
        //System.err.printf("[trace] MovieDB: ITEMS\n");

    }
}

class Genre extends Node<String> implements Comparable<Genre> {

	public MovieList movieList;
	//public String genreName;
	public Genre(String name) {
		super(name);
		movieList = new MovieList();
		//throw new UnsupportedOperationException("not implemented yet");
	}

	public Genre (String genre, String title) {
		super(genre);
		movieList = new MovieList();
		movieList.add(title);
	}

	public void add_Movie (String title) {
		movieList.add(title);
	}


	
	@Override
	public int compareTo(Genre o) {
		return this.getItem().compareTo(o.getItem());
		//throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getItem() == null) ? 0 : this.getItem().hashCode());
		result = prime * result + ((this.getNext() == null) ? 0 : this.getNext().hashCode());
		return result;

		//throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Genre other = (Genre) obj;
		if (this.getItem() == null) {
			return other.getItem() == null;
		} else return this.getItem().equals(other.getItem());

		//throw new UnsupportedOperationException("not implemented yet");
	}
}

class MovieList implements ListInterface<String> {

	public Node<String> dummyHead;
	public int size;
	public MovieList() {
		dummyHead = new Node<>(null);
		size = 0;
	}

	@Override
	public Iterator<String> iterator() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean isEmpty() {
		return size == 0;

		//throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int size() {
		return size;
		//throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void add(String item) {
		Node<String> current = dummyHead;
		while (current.getNext() != null && current.getNext().getItem().compareTo(item) < 0) {
			current = current.getNext();
			//System.out.println("Passing through : " + current.getItem());
		}
		if (current.getNext() != null && current.getNext().getItem().equals(item)) {
			//System.out.println("Already existing item");
			return;
		}
		Node<String> newNode = new Node<>(item);
		newNode.setNext(current.getNext());
		current.setNext(newNode);
		size++;

		//throw new UnsupportedOperationException("not implemented yet");
	}

	public void remove(String item){
		Node<String> current = dummyHead;
		while (current.getNext() != null) {
			if (current.getNext().getItem().equals(item)) {
				current.setNext(current.getNext().getNext());
				size--;
				return;
			}
			current = current.getNext();
		}

	}

	@Override
	public String first() {
		return dummyHead.getNext().getItem();

		//throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void removeAll() {
		dummyHead.setNext(null);
		size = 0;

		//throw new UnsupportedOperationException("not implemented yet");
	}
}