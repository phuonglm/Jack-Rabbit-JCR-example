package to.us.phuonglm.jackRabbitSample;

import java.io.File;
import java.io.IOException;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;

import org.apache.jackrabbit.commons.NamespaceHelper;
import org.apache.jackrabbit.commons.iterator.NodeIterable;
import org.apache.jackrabbit.core.TransientRepository;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.ConsoleReader;
import jline.NullCompletor;
import jline.SimpleCompletor;

public class JCRCommandLine {
	private static final Logger logger = LogManager
			.getLogger(JCRCommandLine.class);

	private String[] syntaxs = { "makeNode", "deleteNode", "changeNode", "listNode", "listProperty", "getProperty", "setProperty", "exit" };

	private Node currentNode;

	private Completor[] completors = { new SimpleCompletor(syntaxs),
			new NullCompletor() };

	private ConsoleReader consoleReader;

	private void init() throws IOException {
		consoleReader = new ConsoleReader(); 
		consoleReader.addCompletor(new ArgumentCompletor(completors));
	}

	private void cmdProcess(Node currentNode, String command) throws RepositoryException{
		if(command.startsWith("listNode")){
			
			NodeIterator nodeIterator = currentNode.getNodes();
			while ( nodeIterator.hasNext()) {
				System.out.println(nodeIterator.nextNode().getName() + " ");
			}
			
		} else if(command.startsWith("makeNode ")) {
			
			currentNode.addNode(command.substring("makeNode ".length()));
			
		} else if (command.startsWith("changeNode ")) {
			
			String changeTo = command.substring("changenode ".length());
			if(changeTo.equals("..")){
				currentNode = currentNode.getParent();
			} else {
				currentNode = currentNode.getNode(command.substring("changenode ".length()));
			}
			
		} else if ( command.startsWith("deleteNode ")) {
			
			String nodeToDelete = command.substring("deleteNode ".length());
			if (!nodeToDelete.isEmpty()){
				
				currentNode.getNode(nodeToDelete).remove();	
			}
		}
	}
	
	public JCRCommandLine() throws IOException {
		init();
	}
	
	public void startCMD() throws IOException {
		String command = "";
		Repository repository = new TransientRepository(new File("/tmp/jcr"));
		Session session = null;
		try {
			session = repository.login(new SimpleCredentials("phuonglm", new char[0]));
			if((new NamespaceHelper(session).getURI("exo") == null)){
				session.getWorkspace().getNamespaceRegistry().registerNamespace("exo", "http://jackrabbit.apache.org/exo");
			}
			currentNode = session.getRootNode();
			while (!command.equals("exit")) {
				consoleReader.setDefaultPrompt(currentNode.getPath() + "/:");
				command = consoleReader.readLine();
				command = command.trim();
				try {
					cmdProcess(currentNode, command);
					session.save();
				} catch (RepositoryException e) {
					System.out.println(e.toString());
				}
				
			}
			
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(session != null){
				session.logout();
			}
        }
	}
}
