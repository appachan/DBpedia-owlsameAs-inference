package LearnJena

import java.io.PrintWriter

import org.apache.jena.rdf.model._
import org.apache.jena.reasoner.rulesys.{GenericRuleReasoner, Rule}
import org.apache.jena.util.FileManager

object InferenceAPI {
  def main(args: Array[String]): Unit = {
    // read model file.
    /*
    val model = ModelFactory.createDefaultModel()
    val in = FileManager.get.open(inputFileName)
    if (in == null) throw new IllegalArgumentException("File: " + inputFileName + " not found")
    model.read(in, null, "N-TRIPLE")
    */
    val dbpedia_ja = FileManager.get.loadModel("./inputs/dbpedia_ja.nt")
    val dbpedia_en = FileManager.get.loadModel("./inputs/dbpedia_en.nt")

    /*
    // SELECT * WHERE {?s <http://www.w3.org/2002/07/owl#sameAs> ?o .}
    val owl_sameAs= model.getProperty("http://www.w3.org/2002/07/owl#sameAs") // <,>は付けない
    val iter = model.listStatements(new SimpleSelector(null, owl_sameAs, null.asInstanceOf[RDFNode]))
    while (iter.hasNext) {
      val stmt = iter.nextStatement()
      println(stmt)
    }
    */

    /*
     * Inference Rules
     * [(?s owl:sameAs ?s_) (?o owl:sameAs ?o_) (?s_ ?p ?o_) -> (?s ?p ?o)]
     * [(?s owl:sameAs ?s_) (?s_ rdf:type ?c) -> (?s rdf:type ?c)]
     */
    val model = dbpedia_ja.union(dbpedia_en)
    val rules = "[(?s owl:sameAs ?s_) (?o owl:sameAs ?o_) (?s_ ?p ?o_) -> (?s ?p ?o)]" +
      "[(?s owl:sameAs ?s_) (?s_ rdf:type ?c) -> (?s rdf:type ?c)]"
    val reasoner = new GenericRuleReasoner(Rule.parseRules(rules))
    val infModel = ModelFactory.createInfModel(reasoner, model)
    val diffModel = infModel.difference(model)
    /*
    val iter = diffModel.listStatements(new SimpleSelector(null, null, null.asInstanceOf[RDFNode]))
    while (iter.hasNext) {
      val stmt = iter.nextStatement()
      println(stmt)
    }
    */
    val writer = new PrintWriter("./outputs/dbpedia_inferenced.nt")
    diffModel.write(writer, "N-TRIPLE")
  }
}

