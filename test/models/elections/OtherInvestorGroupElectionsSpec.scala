package models
import pages.QuestionPage
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import play.api.libs.json.{JsError, JsString, Json}

class OtherInvestorGroupElectionsSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {

  "OtherInvestorGroupElections" must {

    "deserialise valid values" in {

      val gen = arbitrary[OtherInvestorGroupElections]

      forAll(gen) {
        otherInvestorGroupElections =>

          JsString(otherInvestorGroupElections.toString).validate[OtherInvestorGroupElections].asOpt.value mustEqual otherInvestorGroupElections
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!OtherInvestorGroupElections.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[OtherInvestorGroupElections] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = arbitrary[OtherInvestorGroupElections]

      forAll(gen) {
        otherInvestorGroupElections =>

          Json.toJson(otherInvestorGroupElections) mustEqual JsString(otherInvestorGroupElections.toString)
      }
    }
  }
}
