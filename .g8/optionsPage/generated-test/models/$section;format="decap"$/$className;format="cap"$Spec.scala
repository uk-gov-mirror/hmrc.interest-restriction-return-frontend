package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import play.api.libs.json.{JsError, JsString, Json}

class $className;format="cap"$Spec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues {

  "$className;format="cap"$" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf($className;format="cap"$.values)

      forAll(gen) {
        $className;format="decap"$ =>

          JsString($className;format="decap"$.toString).validate[$className;format="cap"$].asOpt.value mustEqual $className;format="decap"$
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!$className;format="cap"$.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[$className;format="cap"$] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf($className;format="cap"$.values)

      forAll(gen) {
        $className;format="decap"$ =>

          Json.toJson($className;format="decap"$) mustEqual JsString($className;format="decap"$.toString)
      }
    }
  }
}
