package to.us.phuonglm.jackRabbitSample;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.ConsoleReader;
import jline.NullCompletor;
import jline.SimpleCompletor;

public class JCRCommandLine {
	private static final Logger logger = LogManager.getLogger(JCRCommandLine.class);

	private String[] syntax = { "foo", "bar", "baz" };
	private Completor[] completors = {
			new SimpleCompletor(new String[] { "foo", "bar", "baz" }),
			new SimpleCompletor(new String[] { "xxx", "yyy", "xxx" }),
			new NullCompletor()
	};

	private ConsoleReader consoleReader;

	private void init() throws IOException {
		consoleReader = new ConsoleReader();
		consoleReader.addCompletor(new ArgumentCompletor(completors));

	}

	public JCRCommandLine() throws IOException {
		String command = "";
		String prependString = "";
		init();

		while (!command.equals("exit")) {
			command = consoleReader.readLine();
			System.out.println("Command: " + command);
			System.out.print(prependString);
		}
	}
}
