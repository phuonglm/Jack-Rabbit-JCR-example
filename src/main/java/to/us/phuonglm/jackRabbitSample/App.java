package to.us.phuonglm.jackRabbitSample;

import java.io.File;
import java.util.Properties;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;



/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Repository repository = new TransientRepository(new File("/home/phuong_lyminh/tmp/jcr"));
        try {
        	Session session = repository.login(new SimpleCredentials("phuonglm", new char[0]));
        	try {
                String user = session.getUserID();
                String name = repository.getDescriptor(Repository.REP_NAME_DESC);
                System.out.println(
                        "Logged in as " + user + " to a " + name + " repository.");
                Node rootNode = session.getRootNode();
                Node toNode =  rootNode.addNode("to");
                Node usNode = toNode.addNode("us");
                Node phuonglmNode = usNode.addNode("phuonglm");
                Node jackRabbitSampleNode = phuonglmNode.addNode("jackRabbitSample");
                jackRabbitSampleNode.setProperty("Class", "Main");
                System.out.println(jackRabbitSampleNode.getPath());
                System.out.println(jackRabbitSampleNode.getProperty("Class").getString());
                session.save();
        	}  finally {
                session.logout();
            }

		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
