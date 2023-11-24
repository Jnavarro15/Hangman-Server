import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */


public class Server{

	private static final String[] ANIMALS = {"lion", "tiger", "elephant", "zebra", "monkey"};
	private static final String[] COUNTRIES = {"france", "italy", "spain", "india", "china"};
	private static final String[] MOVIES = {"inception", "titanic", "avatar", "joker", "matrix"};
	private String secretWord;
	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();

	HashMap<ClientThread, ArrayList<String>> clientUsedWords = new HashMap<>();

	TheServer server;
	String selectedWord;
	private Consumer<Serializable> callback;
//	private HashMap<String, WordInfo[]> wordInfoMap = new HashMap<>();
	
	
	Server(Consumer<Serializable> call, int portNum){

		callback = call;
		server = new TheServer();
		server.start();
	}

	
	
	public class TheServer extends Thread{
		
		public void run() {

			try(ServerSocket mysocket = new ServerSocket(GuiServer.portNum);){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
				ClientThread c = null;
				c = new ClientThread(mysocket.accept(), count, c);
				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				c.start();
				
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{

			ArrayList<String> usedWords = new ArrayList<>();
			Socket connection;
			int count;
			ClientThread client;
			ObjectInputStream in;
			ObjectOutputStream out;

			String word;
			
			ClientThread(Socket s, int count, ClientThread client){
				this.connection = s;
				this.count = count;
				this.client = client;
			}


			
			public void updateClients(String message) {
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
					 t.out.writeObject(message);
					}
					catch(Exception e) {}
				}
			}
			
			public void run(){
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				
//				updateClients("new client on server: client #"+count);
					
				 while(true) {
					    try {
					    	String data = in.readObject().toString();
							System.out.println("DATA: " + data);
							if (data.equals("Animals") || data.equals("Movies") || data.equals("Countries")) {
								callback.accept("client: " + count + " sent: " + data);
//								updateClients("client #" + count + " said: " + data);

								String processedCategory = processCategory(data, client);
								out.writeObject(processedCategory);
							} else if (data.contains("Complete")) {
								callback.accept("client: " + count + " sent: " + data);
							} else{
								int index = secretWord.indexOf(data);
//								System.out.println(index);
								callback.accept("client: " + count + " sent: " + data);
								boolean found = false;
								for (int i = 0; i < secretWord.length(); i++){
									if (secretWord.charAt(i) == data.charAt(0)){
										System.out.println(i);
										out.writeObject("I " + secretWord.charAt(i) + String.valueOf(i));
										found = true;
									}
								}

								if (!found){
									out.writeObject("-");
								}

//								callback.accept("client: " + count + " sent: " + data);
							}

						}
					    catch(Exception e) {
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
//					    	updateClients("Client #"+count+" has left the server!");
					    	clients.remove(this);
					    	break;
					    }
					}
				}//end of run

			class WordInfo {
				String word;
				Integer[] placeholderIndices;

				WordInfo(String word, Integer[] placeholderIndices) {
					this.word = word;
					this.placeholderIndices = placeholderIndices;
				}
			}
			private String processCategory(String category, ClientThread client) {
				// Your logic to process the received category and generate a response


				switch (category) {
					case "Animals":


						do {
							selectedWord = ANIMALS[(int) (Math.random() * ANIMALS.length)];
						} while (usedWords.contains(selectedWord));

						usedWords.add(selectedWord);

						secretWord = selectedWord;

						return String.valueOf(secretWord.length());
					case "Movies":

						do {
							selectedWord = ANIMALS[(int) (Math.random() * ANIMALS.length)];
						} while (usedWords.contains(selectedWord));

						usedWords.add(selectedWord);

						secretWord = selectedWord;
						secretWord = MOVIES[(int) (Math.random() * ANIMALS.length)];
						return String.valueOf(secretWord.length());
					case "Countries":
						do {
							selectedWord = ANIMALS[(int) (Math.random() * ANIMALS.length)];
						} while (usedWords.contains(selectedWord));

						usedWords.add(selectedWord);

						secretWord = selectedWord;
						secretWord = COUNTRIES[(int) (Math.random() * ANIMALS.length)];
						return String.valueOf(secretWord.length());
					default:
						return "No category";
				}
			}
			
			
		}//end of client thread
}


	
	

	
