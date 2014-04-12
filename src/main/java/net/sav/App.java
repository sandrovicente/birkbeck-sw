package net.sav;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
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
    	//rdfTest();
    	queryRdf();
    }

	private static void queryRdf() throws FileNotFoundException, IOException {
		// Open the bloggers RDF graph from the filesystem
    	InputStream in = new FileInputStream(new File("bloggers.rdf"));

    	// Create an empty in-memory model and populate it from the graph
    	Model model = ModelFactory.createMemModelMaker().createModel("bloggers");
    	model.read(in,null); // null base URI, since model URIs are absolute
    	in.close();

    	// Create a new query
    	String queryString = 
    		"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
    		"SELECT ?url " +
    		"WHERE {" +
    		"      ?contributor foaf:name \"Dave Beckett\" . " +
    		"      ?contributor foaf:weblog ?url . " +
    		"      }";

    	Query query = QueryFactory.create(queryString);

    	// Execute the query and obtain results
    	QueryExecution qe = QueryExecutionFactory.create(query, model);
    	ResultSet results = qe.execSelect();

    	// Output query results	
    	ResultSetFormatter.out(System.out, results, query);

    	// Important - free up resources used running the query
    	qe.close();
	}

	private static void rdfTest() throws IOException {
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
