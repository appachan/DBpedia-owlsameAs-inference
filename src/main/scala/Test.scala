//import $ivy.`org.apache.jena:jena-core:3.9.0`
import scala.collection.JavaConverters._
import org.apache.jena.rdf.model._
import org.apache.jena.ontology.OntModelSpec._

object Test {
  def main(args: Array[String]): Unit = {

    // ベースモデルを生成
    val SOURCE = "http://rdfs.org/sioc/ns"
    val NS = SOURCE  + "#"
    val base = ModelFactory.createOntologyModel(OWL_MEM)
    base.read(SOURCE, "RDF/XML")

    // ベースモデルによる推論モデルを生成
    val inf = ModelFactory.createOntologyModel(OWL_MEM_MICRO_RULE_INF, base)

    val userCls = base.getOntClass(NS + "UserAccont")
    val user1 = base.createIndividual(NS + "user1", userCls)
    println(user1.getOntClass())
    // <http://rdfs.org/sioc/ns#uesr1> is asserted in class <http://rdfs.org/sioc/ns#UserAccount>
    user1.listRDFTypes(true).toList.asScala.foreach{`type` =>
      println( user1.getURI() + " is asserted in class " + `type`)}

    /*
    //val inf_user = inf.getIndividual(NS + "user1")
    val inf_user = base.getIndividual(NS + "user1")
    inf_user.listRDFTypes(true).toList.asScala.foreach{`type` =>
      println( inf_user.getURI() + " is asserted in class " + `type`)}
    // http://rdfs.org/sioc/ns#account1 is asserted in class http://rdfs.org/sioc/ns#UserAccount
    // http://rdfs.org/sioc/ns#account1 is asserted in class http://rdfs.org/sioc/ns#User
    */
  }
}
