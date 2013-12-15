package net.sav;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.VCARD;

/**
 * Hello world!
 *
 */
public class App 
{
	static String personURI    = "http://somewhere/JohnSmith";
	static String fullName     = "John Smith";
	
    public static void main( String[] args ) throws IOException
    {
    	// create an empty Model
    	Model model = ModelFactory.createDefaultModel();

    	// create the resource
    	Resource johnSmith = model.createResource(personURI);

    	// add the property
    	johnSmith.addProperty(VCARD.FN, fullName);	
    	johnSmith.addProperty(VCARD.N, model.createResource().addProperty(VCARD.Given, "John").addProperty(VCARD.Family, "Smith"));
    	StmtIterator it = model.listStatements();
    	while(it.hasNext()) {
    		Statement stmt = it.nextStatement();
    		Resource res = stmt.getSubject();
    		Property pred = stmt.getPredicate();
    		RDFNode object = stmt.getObject();
    		System.out.format(">> %s -> %s -> %s\n", res.toString(), pred.toString(), (object instanceof Resource) ? object.toString() : "\\" + object.toString() + "\\");
    	}
    	
    	PrintWriter out
    	   = new PrintWriter(new BufferedWriter(new FileWriter("foo.xml")));
    	model.write(out);
    	out.close();
    	
    }
}
