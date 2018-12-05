package LearnJena
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.vocabulary.VCARD

object RDFCoreAPI {
  def main(args: Array[String]): Unit = {
    val personURI = "http://somewhere/JohnSmith"
    val givenName = "John"
    val familyName = "Smith"
    val fullName = givenName + " " + familyName

    // create an empty Model
    val model = ModelFactory.createDefaultModel

    // create the resource & add the property
    val johnSmith = model
      .createResource(personURI)
      .addProperty(VCARD.FN, fullName)
      .addProperty(VCARD.N,
        model
          .createResource()
          .addProperty(VCARD.Given, givenName)
          .addProperty(VCARD.Family, familyName))
    model.write(System.out, "N-TRIPLES")
  }
}
