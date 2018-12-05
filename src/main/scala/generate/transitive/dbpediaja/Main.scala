package generate.transitive.dbpediaja

import java.io.PrintWriter

import org.apache.jena.rdf.model._
import org.apache.jena.reasoner.rulesys.{GenericRuleReasoner, Rule}
import org.apache.jena.util.FileManager

object Main {
  def main(args: Array[String]): Unit = {
    // read model file.
    val dbpedia_ja = FileManager.get.loadModel("./inputs/dbpedia_ja.nt")
    val dbpedia_en = FileManager.get.loadModel("./inputs/dbpedia_en.nt")

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

    // write out.
    val writer = new PrintWriter("./outputs/dbpedia_inferenced.nt")
    diffModel.write(writer, "N-TRIPLE")
  }
}
