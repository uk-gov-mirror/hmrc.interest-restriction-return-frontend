/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import play.api.libs.json.{JsError, JsString, Json}
import pages.groupLevelInformation._
import forms.groupLevelInformation.EstimatedFiguresFormProvider
import EstimatedFigures._
import play.api.i18n.DefaultMessagesApi
import play.api.test.FakeRequest

class EstimatedFiguresSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {

  "EstimatedFigures" must {

    "deserialise valid values" in {

      val gen = arbitrary[EstimatedFigures]

      forAll(gen) {
        estimatedFigures =>

          JsString(estimatedFigures.toString).validate[EstimatedFigures].asOpt.value mustEqual estimatedFigures
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!EstimatedFigures.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[EstimatedFigures] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = arbitrary[EstimatedFigures]

      forAll(gen) {
        estimatedFigures =>

          Json.toJson(estimatedFigures) mustEqual JsString(estimatedFigures.toString)
      }
    }

  }

  "optionsFilteredByUserAnswers" must {

    val testMessages = Map("default" -> Map("foo" -> "bar"))
    val messagesApi = new DefaultMessagesApi(testMessages)
    implicit val messages = messagesApi.preferred(FakeRequest("GET", "/"))

    val form = (new EstimatedFiguresFormProvider).apply

    "return all values where all values exist in UserAnswers" in {
      val userAnswers = (for {
        a                 <- UserAnswers("id").set(EnterANGIEPage, BigDecimal(123))
        b                 <- a.set(EnterQNGIEPage, BigDecimal(123))
        c                 <- b.set(EnterQNGIEPage, BigDecimal(123))
        d                 <- c.set(GroupEBITDAPage, BigDecimal(123))
        e                 <- d.set(InterestReactivationsCapPage, BigDecimal(123))
        f                 <- e.set(DisallowedAmountPage, BigDecimal(123)) 
        g                 <- f.set(InterestAllowanceBroughtForwardPage, BigDecimal(123))
        h                 <- g.set(GroupInterestAllowancePage, BigDecimal(123))
        finalUserAnswers  <- h.set(GroupInterestCapacityPage, BigDecimal(123))
      } yield finalUserAnswers).get

      val result = optionsFilteredByUserAnswers(form, userAnswers)
      result mustEqual EstimatedFigures.values.map(estimatedFigureToCheckboxItem(form, _))
    }

    "return no values where no values exist in UserAnswers" in {
      val result = optionsFilteredByUserAnswers(form, UserAnswers("id"))
      result mustEqual Nil
    }

    "return completed values where only some values exist in UserAnswers" in {
      val userAnswers = (for {
        a                 <- UserAnswers("id").set(EnterANGIEPage, BigDecimal(123))
        b                 <- a.set(EnterQNGIEPage, BigDecimal(123))
        c                 <- b.set(InterestReactivationsCapPage, BigDecimal(123))
        d                 <- c.set(DisallowedAmountPage, BigDecimal(123)) 
        finalUserAnswers  <- d.set(GroupInterestCapacityPage, BigDecimal(123))
      } yield finalUserAnswers).get

      val result = optionsFilteredByUserAnswers(form, userAnswers)
      val expectedValues = Seq(Angie, Qngie, ReactivationCap, TotalDisallowedAmount, InterestCapacity)
      result mustEqual expectedValues.map(estimatedFigureToCheckboxItem(form, _))
    }

    "return completed values where only one value exists in UserAnswers" in {
      val userAnswers = UserAnswers("id").set(EnterANGIEPage, BigDecimal(123)).get

      val result = optionsFilteredByUserAnswers(form, userAnswers)
      val expectedValues = Seq(Angie)
      result mustEqual expectedValues.map(estimatedFigureToCheckboxItem(form, _))
    }

  }

}
