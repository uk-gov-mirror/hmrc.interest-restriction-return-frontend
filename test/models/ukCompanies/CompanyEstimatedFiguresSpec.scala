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
import play.api.i18n.DefaultMessagesApi
import forms.ukCompanies.CompanyEstimatedFiguresFormProvider
import pages.ukCompanies._
import models.CompanyEstimatedFigures._
import play.api.test.FakeRequest
import models.returnModels.fullReturn._
import assets.constants.fullReturn.UkCompanyConstants._

class CompanyEstimatedFiguresSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {

  "CompanyEstimatedFigures" must {

    "deserialise valid values" in {

      val gen = arbitrary[CompanyEstimatedFigures]

      forAll(gen) {
        companyEstimatedFigures =>

          JsString(companyEstimatedFigures.toString).validate[CompanyEstimatedFigures].asOpt.value mustEqual companyEstimatedFigures
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!CompanyEstimatedFigures.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[CompanyEstimatedFigures] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = arbitrary[CompanyEstimatedFigures]

      forAll(gen) {
        companyEstimatedFigures =>

          Json.toJson(companyEstimatedFigures) mustEqual JsString(companyEstimatedFigures.toString)
      }
    }
  }

  "options" must {

    val testMessages = Map("default" -> Map("foo" -> "bar"))
    val messagesApi = new DefaultMessagesApi(testMessages)
    implicit val messages = messagesApi.preferred(FakeRequest("GET", "/"))

    val form = (new CompanyEstimatedFiguresFormProvider).apply

    "return all values where all values exist in UserAnswers" in {
      val userAnswers = UserAnswers("id").set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
      val result = options(form, 1, userAnswers)
      result mustEqual CompanyEstimatedFigures.values.map(estimatedFigureToCheckboxItem(form, _))
    }

    "return no values where no values exist in UserAnswers" in {
      val company = UkCompanyModel(CompanyDetailsModel("123", "1123456789"))
      val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get
      val result = options(form, 1, UserAnswers("id"))
      result mustEqual Nil
    }

    "return completed values where only some values exist in UserAnswers" in {
      val company = UkCompanyModel(CompanyDetailsModel("123", "1123456789"))
          .copy(taxEBITDA = Some(BigDecimal(123)), netTaxInterest = Some(BigDecimal(123)))
      val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get

      val result = options(form, 1, userAnswers)
      val expectedValues = Seq(TaxEbitda, NetTaxInterest)
      result mustEqual expectedValues.map(estimatedFigureToCheckboxItem(form, _))
    }

    "return completed values where only one value exists in UserAnswers" in {
      val company = UkCompanyModel(CompanyDetailsModel("123", "1123456789"))
          .copy(taxEBITDA = Some(BigDecimal(123)))
      val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get
      
      val result = options(form, 1, userAnswers)
      val expectedValues = Seq(TaxEbitda)
      result mustEqual expectedValues.map(estimatedFigureToCheckboxItem(form, _))
    }
  }
}
